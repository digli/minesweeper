package gui;

import java.awt.Color;
import java.util.ArrayList;

public class Solver {

	private MineField mf;
	private MineButton[][] matrix;
	private int[][] flags;
	private boolean hasChanged;

	public Solver(MineField mf, MineButton[][] matrix) {
		this.mf = mf;
		this.matrix = matrix;
	}

	public void reset() {
		flags = new int[matrix.length][matrix[0].length];
	}

	public void solve() {

		hasChanged = true;
		long start = System.currentTimeMillis();

		if (!mf.isStarted) {
			matrix[(int) Math.floor((Math.random() * matrix.length))][(int) Math
					.floor((Math.random() * matrix[0].length))].click(true);
		}

		while (mf.progress > 0 && hasChanged) {
			hasChanged = false;
			for (int y = 0; y < matrix[0].length; y++) {
				for (int x = 0; x < matrix.length; x++) {
					if (!matrix[x][y].isEnabled()) {
						if (countAdjacent(x, y) == mf.mines[x][y]) {
							if (flagAdjacent(x, y))
								hasChanged = true;
						}
						if (flags[x][y] == mf.mines[x][y]) {
							if (mf.checkAdjacent(x, y))
								hasChanged = true;
						}
					}
				}
			}
		}

		if (!hasChanged) {
			// create list of tiles to check
			ArrayList<MineButton> borderTiles = new ArrayList<MineButton>();
			ArrayList<MineButton> clickedTiles = new ArrayList<MineButton>();

			for (int y = 0; y < matrix[0].length; y++) {
				for (int x = 0; x < matrix.length; x++) {
					if (!matrix[x][y].isEnabled()) {
						if (countAdjacent(x, y) > mf.mines[x][y]) {
							addAdjacent(x, y, borderTiles);
							clickedTiles.add(matrix[x][y]);
						}
					}
				}
			}

			int wasd = 0;
			for (MineButton b : borderTiles) {
				b.setBackground(Color.PINK);
				// b.setText("" + wasd++);
			}

			for (MineButton b : clickedTiles) {
				b.setBackground(Color.YELLOW);
			}

			int[][] a = new int[clickedTiles.size()][borderTiles.size()];
			int[] b = new int[clickedTiles.size()];

			for (int i = 0; i < clickedTiles.size(); i++) {
				MineButton temp = clickedTiles.get(i);
				// Creates column matrix with corresponding mine value
				b[i] = mf.mines[temp.x()][temp.y()];
				for (int j = 0; j < borderTiles.size(); j++) {
					if (clickedTiles.get(i).isAdjacentTo(borderTiles.get(j))) {
						a[i][j] = 1;
					}
				}
			}

			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a[0].length; j++) {
					System.out.print(a[i][j] + " ");
				}
				System.out.println("\t" + b[i]);
			}
			System.out.println();
			gauss(a, b);

			for (int i = 0; i < a.length; i++) {
				for (int j = 0; j < a[0].length; j++) {
					System.out.print(a[i][j] + " ");
				}
				System.out.println("\t" + b[i]);
			}
		}

		if (mf.progress == 0)
			mf.end(System.currentTimeMillis() - start);
	}

	private void gauss(int[][] a, int[] b) {
		int y = a.length;
		int x = a[0].length;

		int column = -1;

		for (int i = 0; i < y && column < x; i++) {
			// Find pivot row
			int max = i;

			do {
				column++;
				for (int j = i + 1; j < y && column < x; j++) {
					if (Math.abs(a[j][column]) > Math.abs(a[max][column])) {
						max = j;
					}
				}
			} while (column < x - 1 && a[max][column] == 0);

			// Swap rows
			int[] temp = a[i];
			a[i] = a[max];
			a[max] = temp;

			int t = b[i];
			b[i] = b[max];
			b[max] = t;

			for (int j = i + 1; j < y; j++) {
				if (a[j][column] != 0) {
					int alpha = a[j][column] / a[i][column];
					b[j] -= alpha * b[i];
					for (int k = i; k < x; k++) {
						a[j][k] -= alpha * a[i][k];
					}
				}
			}
		}
	}

	private void addAdjacent(int x, int y, ArrayList<MineButton> list) {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				try {
					if (matrix[x + j][y + i].isEnabled()
							&& !list.contains(matrix[x + j][y + i])
							&& !matrix[x + j][y + i].isFlagged()) {
						list.add(matrix[x + j][y + i]);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
	}

	private int countAdjacent(int x, int y) {
		if (mf.mines[x][y] == 0) {
			flags[x][y] = -1;
			return -1;
		}
		int sum = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				try {
					if (matrix[x + j][y + i].isEnabled())
						sum++;
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		return sum;
	}

	private boolean flagAdjacent(int x, int y) {
		boolean change = false;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				try {
					if (matrix[x + j][y + i].isEnabled()
							&& !matrix[x + j][y + i].isFlagged()) {
						flag(x + j, y + i);
						change = true;
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		return change;
	}

	private void flag(int x, int y) {
		matrix[x][y].setFlagged();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (!(j == 0 && i == 0)) {
					try {
						flags[x + j][y + i]++;
					} catch (ArrayIndexOutOfBoundsException e) {
					}
				}
			}
		}
	}
}
