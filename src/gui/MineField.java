package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MineField {

	public static final int LOSS = 0, WIN = 1, AUTO = 2;
	public boolean isStarted = false;
	public int[][] mines;
	public int progress;

	private int height = 10, width = 10, nbrOfMines = 20;
	private String[] options = { "B�rja om", "Avsluta" };
	private long startTime;
	private MineButton[][] matrix;
	private ConfigButton cb;
	private MineCounter mc;
	private TimeCounter tc;
	private TimeHandler th;
	private Solver solver;
	private JPanel container;
	private JPanel footer;
	private JFrame frame;

	public MineField() {
		frame = new JFrame();
		frame.setTitle("F1 R�j");
		container = new JPanel();
		footer = new JPanel();
		matrix = new MineButton[width][height];
		mc = new MineCounter(nbrOfMines);
		tc = new TimeCounter();
		cb = new ConfigButton(this);
		solver = new Solver(this, matrix);

		// footer.add(tc);
		// footer.add(new SpaceFiller(100));
		// footer.add(cb);
		// footer.add(new SpaceFiller(100));
		footer.add(new SolveButton(solver));
		footer.add(new SpaceFiller(100));
		footer.add(mc);

		init();
		newGame();

		frame.add(container);
		frame.add(footer, BorderLayout.SOUTH);
		frame.pack();
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
	}

	public void newGame() {
		progress = height * width - nbrOfMines;
		isStarted = false;
		th = new TimeHandler(tc);
		tc.reset();
		mc.reset();
		solver.reset();
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

	public void updateMineCount(int diff) {
		mc.update(diff);
	}

	public void resetConfigButton() {
		cb.reset();
	}

	public void end(long time) {
		th.interrupt();
		int choice = JOptionPane.showOptionDialog(frame,
				"Minr�jaren gjorde ditt jobb p� " + time + " ms.", "R�j",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				options, 0);
		if (choice == 0) newGame();
		else frame.dispose();
	}

	public void end(int ending) {
		th.interrupt();
		int choice = 0;
		switch (ending) {
		case MineField.LOSS:
			checkMines();
			choice = JOptionPane.showOptionDialog(frame, getTime()
					+ " sekunder.", "R�j", JOptionPane.DEFAULT_OPTION,
					JOptionPane.PLAIN_MESSAGE, null, options, 0);
			break;
		case MineField.WIN:
			choice = JOptionPane.showOptionDialog(frame, "Du r�jde rubbet p� "
					+ getTime() + " sekunder!", "R�j",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
					null, options, 0);
			break;
		}
		if (choice == 0) newGame();
		else frame.dispose();
	}

	private void checkMines() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (mines[j][i] == -1) {
					if (!matrix[j][i].isFlagged()) {
						matrix[j][i].setForeground(new Color(160, 160, 160));
						matrix[j][i].setText("�");
					}
				} else if (matrix[j][i].isFlagged()) {
					matrix[j][i].setForeground(Color.RED);
				}
			}
		}
	}

	public boolean clickAdjacent(int x, int y) {
		boolean change = false;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				try {
					if (matrix[x + j][y + i].click(false)) change = true;
				} catch (ArrayIndexOutOfBoundsException e) {
					// do nothing
				}
			}
		}
		return change;
	}

	public boolean checkAdjacent(int x, int y) {
		boolean lost = false;
		int tempX = -1, tempY = -1, flags = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				try {
					if (matrix[x + j][y + i].isFlagged()) flags++;
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
			if (lost) {
				matrix[tempX][tempY].click(false);
				return true;
			} else return clickAdjacent(x, y);
		}
		return false;
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
		} while ((tempX == x - 1 || tempX == x || tempX == x + 1)
				&& (tempY == y - 1 || tempY == y || tempY == y + 1)
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
								if (mines[x + j][y + i] != -1)
									mines[x + j][y + i]++;
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

		// try {
		// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// } catch (UnsupportedLookAndFeelException e) {
		// } catch (ClassNotFoundException e) {
		// } catch (InstantiationException e) {
		// } catch (IllegalAccessException e) {
		// }

		new MineField();
	}
}
