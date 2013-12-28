package gui;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MineField {

	public static final int LOSS = 0, WIN = 1;
	public int[][] mines;
	public int progress;

	private final int height = 16, width = 26, nbrOfMines = 0;
	private String[] options = { "Börja om", "Avsluta" };
	private boolean isStarted = false;
	private MineButton[][] matrix;
	private JPanel container;
	private long startTime;
	private JFrame frame;

	public MineField() {
		frame = new JFrame();
		frame.setTitle("F1 Röj");
		container = new JPanel();
		matrix = new MineButton[width][height];
		
		init();
		newGame();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private void init() {
		container.setLayout(new GridLayout(height, width));
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				matrix[j][i] = new MineButton(this, j, i);
				container.add(matrix[j][i]);
			}
		}
		frame.add(container);
		frame.pack();
	}

	public void newGame() {
		progress = height * width - nbrOfMines;
		isStarted = false;
		container = new JPanel();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				matrix[j][i].reset();
			}
		}
	}

	public int getTime() {
		return (int) (System.currentTimeMillis() - startTime) / 1000;
	}

	public void end(int ending) {
		int choice = 0;
		switch (ending) {
		case MineField.LOSS:
			choice = JOptionPane.showOptionDialog(frame, "noob. " + getTime() + " sekunder.", "Röj",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, 0);
			break;
		case MineField.WIN:
			choice = JOptionPane.showOptionDialog(frame, "Du röjde rubbet på " + getTime() + " sekunder!", "Röj",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, 0);
			break;
		}
		if (choice == 0) {
			newGame();
		} else {
			frame.dispose();
		}
	}

	public void clickAdjacent(int x, int y) {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				try {
					matrix[x + i][y + j].click(false);
				} catch (ArrayIndexOutOfBoundsException e) {
					// do nothing
				}
			}
		}
	}

	public void generate(int x, int y) {
		if (isStarted) return;
		mines = new int[width][height];
		for (int i = 0; i < nbrOfMines; i++) {
			createMine(x, y);
		}
		createAdjacent();

		startTime = System.currentTimeMillis();
		isStarted = true;
	}

	public void checkAdjacent(int x, int y) {
		boolean lost = false;
		int tempX = -1, tempY = -1, flags = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				try {
					if (matrix[x + i][y + j].isFlagged) {
						flags++;
					} else {
						if (mines[x + i][y + j] == -1) {
							lost = true;
							tempX = x + i;
							tempY = y + j;
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					// do nothing
				}
			}
		}
		if (flags == mines[x][y]) {
			if (lost) {
				matrix[tempX][tempY].click(false);
			} else {
				clickAdjacent(x, y);
			}
		}
	}

	private void createMine(int x, int y) {
		int tempX, tempY;
		do {
			tempX = (int) Math.floor((Math.random() * width));
			tempY = (int) Math.floor((Math.random() * height));
		} while ((tempX == x || tempX == x - 1 || tempX == x + 1) && (tempY == y - 1 || tempY == y || tempY == y + 1)
				|| mines[tempX][tempY] == -1);

		mines[tempX][tempY] = -1;
	}

	private void createAdjacent() {
		for (int i = 0; i < width; i++) {
			for (int p = 0; p < height; p++) {
				if (mines[i][p] == -1) {
					for (int k = -1; k < 2; k++) {
						for (int j = -1; j < 2; j++) {
							try {
								if (mines[i + k][p + j] != -1)
									mines[i + k][p + j]++;
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