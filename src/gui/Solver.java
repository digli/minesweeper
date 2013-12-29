package gui;

public class Solver {

	private MineField mf;
	private MineButton[][] matrix;
	private int[][] flags;

	public Solver(MineField mf, MineButton[][] matrix) {
		this.mf = mf;
		this.matrix = matrix;
		flags = new int[matrix.length][matrix[0].length];
	}

	public boolean solve() {
		// while (mf.progress > 0) {
		// }
		for (int y = 0; y < matrix[0].length; y++) {
			for (int x = 0; x < matrix.length; x++) {
				if (!matrix[x][y].isEnabled()) {

					if (countAdjacent(x, y) == mf.mines[x][y]) {
						flagAdjacent(x, y);
					}

					if (flags[x][y] == mf.mines[x][y]) {
						mf.checkAdjacent(x, y);
					}
				}
			}
		}
		return true;
	}

	public void reset() {
		flags = new int[matrix.length][matrix[0].length];
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
					if (matrix[x + j][y + i].isEnabled()) sum++;
				} catch (ArrayIndexOutOfBoundsException e) {
					// do nothing
				}
			}
		}
		return sum;
	}

	private void flagAdjacent(int x, int y) {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				try {
					if (matrix[x + j][y + i].isEnabled()
							&& !matrix[x + j][y + i].isFlagged()) {
						flag(x + j, y + i);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					// do nothing
				}
			}
		}
	}

	private void flag(int x, int y) {
		matrix[x][y].setFlagged();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (!(j == 0 && i == 0)) {
					try {
						flags[x + j][y + i]++;
					} catch (ArrayIndexOutOfBoundsException e) {
						// do nothing
					}
				}
			}
		}
	}
}
