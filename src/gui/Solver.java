package gui;

public class Solver {

	private MineField mf;
	private MineButton[][] matrix;
	private int[][] flags;
	private boolean hasChanged;
	private Fraction[][] frac;

	public Solver(MineField mf, MineButton[][] matrix) {
		this.mf = mf;
		this.matrix = matrix;
		flags = new int[matrix.length][matrix[0].length];
		frac = new Fraction[matrix.length][matrix[0].length];
	}

	public void solve() {

		if (!mf.isStarted) {
			matrix[(int) Math.floor((Math.random() * matrix.length))][(int) Math
					.floor((Math.random() * matrix[0].length))].click(true);
		}

		hasChanged = false;

		// while (mf.progress > 0) {
		// }

		for (int y = 0; y < matrix[0].length; y++) {
			for (int x = 0; x < matrix.length; x++) {
				if (!matrix[x][y].isEnabled()) {

					if (countAdjacent(x, y) == mf.mines[x][y]) {
						if (flagAdjacent(x, y)) hasChanged = true;
					}

					if (flags[x][y] == mf.mines[x][y]) {
						if (mf.checkAdjacent(x, y)) hasChanged = true;
					}
				}
			}
		}

		if (!hasChanged) {

			for (int y = 0; y < matrix[0].length; y++) {
				for (int x = 0; x < matrix.length; x++) {
					if (!matrix[x][y].isEnabled()) {
						int sum = countFlaglessAdjacent(x, y);
						if (sum > mf.mines[x][y]) {
							System.out.println("(" + y + ";" + x + ") - " + sum);
							fracAdjacent(x, y, sum);
						}
					}
				}
			}
			for (int y = 0; y < matrix[0].length; y++) {
				for (int x = 0; x < matrix.length; x++) {
					if (matrix[x][y].isEnabled() && frac[x][y] != null) {
						matrix[x][y].setText(frac[x][y].toString());
					}
				}
			}

		}

		if (mf.progress == 0) mf.end(MineField.AUTO);
	}

	public void reset() {
		flags = new int[matrix.length][matrix[0].length];
		frac = new Fraction[matrix.length][matrix[0].length];
	}

	private void fracAdjacent(int x, int y, int sum) {
		Fraction temp = new Fraction(sum);
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				try {
					if (!matrix[x + j][y + i].isEnabled()) {
						if (frac[x + j][y + i] != null) {
							if (frac[x + j][y + i].compareTo(temp) == 1)
								frac[x + j][y + i] = temp;
						} else frac[x + j][y + i] = new Fraction(sum);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					// nop
				}
			}
		}
	}

	private int countFlaglessAdjacent(int x, int y) {
		int sum = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				try {
					if (matrix[x + j][y + i].isEnabled()
							&& !matrix[x + j][y + i].isFlagged()) sum++;
				} catch (ArrayIndexOutOfBoundsException e) {
					// nop
				}
			}
		}
		return sum;
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
					// nop
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
					// nop
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
						// nop
					}
				}
			}
		}
	}
}
