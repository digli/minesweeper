package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MineField {

	public static final int LOSS = 0, WIN = 1;
	public int[][] mines;
	public int progress;

	private int height = 16, width = 30, nbrOfMines = 99;
	private String[] options = { "Börja om", "Avsluta" };
	private boolean isStarted = false;
	private long startTime;
	private MineButton[][] matrix;
	private ConfigButton cb;
	private MineCounter mc;
	private TimeCounter tc;
	private TimeHandler th;
	private JPanel container;
	private JPanel footer;
	private JFrame frame;

	public MineField() {
		frame = new JFrame();
		frame.setTitle("F1 Röj");
		container = new JPanel();
		footer = new JPanel();
		matrix = new MineButton[width][height];
		mc = new MineCounter(nbrOfMines);
		tc = new TimeCounter();
		cb = new ConfigButton(this);
		
		footer.add(tc);
		footer.add(new SpaceFiller());
		footer.add(cb);
		footer.add(new SpaceFiller());
		footer.add(mc);
		
		init();
		newGame();
		
		frame.add(container, BorderLayout.CENTER);
		frame.add(footer, BorderLayout.SOUTH);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private void init() {
		container.setLayout(new GridLayout(height, width));
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				matrix[j][i] = new MineButton(this, mc, j, i);
				container.add(matrix[j][i]);
			}
		}
	}

	public void newGame() {
		progress = height * width - nbrOfMines;
		isStarted = false;
		th = new TimeHandler(tc);
		tc.reset();
		mc.reset();
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				matrix[j][i].reset();
	}
	
	public void setConfig(int width, int height, int mines) {
		// TODO
	}

	public int getTime() {
		return 1 + (int) (System.currentTimeMillis() - startTime) / 1000;
	}

	public void resetCW() {
		cb.reset();
	}
	
	public void end(int ending) {
		th.interrupt();
		int choice = 0;
		switch (ending) {
		case MineField.LOSS:
			checkMines();
			choice = JOptionPane.showOptionDialog(frame, "noob", "Röj", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
					options, 0);
			break;
		case MineField.WIN:
			choice = JOptionPane.showOptionDialog(frame, "Du röjde rubbet på " + getTime() + " sekunder!", "Röj",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, 0);
			break;
		}
		if (choice == 0) newGame();
		else frame.dispose();
	}

	private void checkMines() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (mines[j][i] == -1) {
					if (!matrix[j][i].isFlagged) {
						matrix[j][i].setForeground(new Color(160, 160, 160));
						matrix[j][i].setText("¤");
					}
				} else if (matrix[j][i].isFlagged) {
					matrix[j][i].setForeground(Color.RED);
				}
			}
		}
	}

	public void clickAdjacent(int x, int y) {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				try {
					matrix[x + j][y + i].click(false);
				} catch (ArrayIndexOutOfBoundsException e) {
					// do nothing
				}
			}
		}
	}

	public void checkAdjacent(int x, int y) {
		boolean lost = false;
		int tempX = -1, tempY = -1, flags = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				try {
					if (matrix[x + j][y + i].isFlagged) flags++;
					else {
						if (mines[x + j][y + i] == -1) {
							lost = true;
							tempX = x + j;
							tempY = y + i;
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					// do nothing
				}
			}
		}
		if (flags == mines[x][y]) {
			if (lost) matrix[tempX][tempY].click(false);
			else clickAdjacent(x, y);
		}
	}

	public void generate(int x, int y) {
		if (isStarted) return;
		
		th.start();
		mines = new int[width][height];

		for (int i = 0; i < nbrOfMines; i++)
			createMine(x, y);

		createAdjacent();

		startTime = System.currentTimeMillis();
		isStarted = true;
	}

	private void createMine(int x, int y) {
		int tempX, tempY;
		do {
			tempX = (int) Math.floor((Math.random() * width));
			tempY = (int) Math.floor((Math.random() * height));
		} while ((tempX == x - 1 || tempX == x || tempX == x + 1) && (tempY == y - 1 || tempY == y || tempY == y + 1)
				|| mines[tempX][tempY] == -1);

		mines[tempX][tempY] = -1;
	}

	private void createAdjacent() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (mines[x][y] == -1) {
					for (int i = -1; i < 2; i++) {
						for (int j = -1; j < 2; j++) {
							try {
								if (mines[x + j][y + i] != -1) mines[x + j][y + i]++;
							} catch (ArrayIndexOutOfBoundsException e) {
								// do nothing
							}
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		new MineField();
	}
}