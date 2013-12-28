package gui;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MineField {

	public static final int LOSS = 0, WIN = 1;
	public boolean isStarted = false;
	public int progress;
	public int[][] mines;
	public MineButton[][] matrix;

	private final int height = 16, width = 26, nbrOfMines = 99;
	private String[] options = { "Börja om", "Avsluta" };
	private long startTime;
	private JFrame frame;
	private JPanel container;

	public MineField() {
		matrix = new MineButton[width][height];
		frame = new JFrame();
		frame.setTitle("F1 Röj");
		container = new JPanel();
		init();
		newGame();

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private void init() {
		container = new JPanel();
		GridLayout grid = new GridLayout(height, width);
		container.setLayout(grid);
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

	public void exit() {
		frame.dispose();
	}

	public int getTime() {
		return (int) (System.currentTimeMillis() - startTime) / 1000;
	}

	public void end(int ending) {
		int choice = 0;
		switch (ending) {
		case MineField.LOSS:
			choice = JOptionPane.showOptionDialog(frame, "Ha, du förlorade! " + getTime() + " sekunder.", "Röj",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, 0);
			break;
		case MineField.WIN:
			choice = JOptionPane.showOptionDialog(frame, "Grattis, du vann på " + getTime() + " sekunder!", "Röj",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, 0);

			break;
		}
		if (choice == 0) {
			newGame();
		} else {
			exit();
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

		mines = new int[width][height];
		for (int i = 0; i < nbrOfMines; i++) {
			createMine(x, y);
		}
		createAdjacent();

		startTime = System.currentTimeMillis();
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