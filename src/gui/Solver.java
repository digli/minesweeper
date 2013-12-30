package gui;

import java.awt.Point;

public class Solver {

	private MineField mf;
	private MineButton[][] matrix;
	private int[][] flags;
	private boolean hasChanged, isReset;
	private float tempMax;
	private Point nextFlag;

	public Solver(MineField mf, MineButton[][] matrix) {
		this.mf = mf;
		this.matrix = matrix;
		isReset = false;
	}

	public void solve() {

		hasChanged = true;
		long start = System.currentTimeMillis();

		if (!mf.isStarted) {
			matrix[(int) Math.floor((Math.random() * matrix.length))][(int) Math
					.floor((Math.random() * matrix[0].length))].click(true);
		}

		tempMax = 0; // remember to move this if needed

		while (mf.progress > 0 && hasChanged) {
			hasChanged = false;
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

		}

		if (!hasChanged) {

			float[][] temp = new float[matrix.length][matrix[0].length];

			for (int y = 0; y < matrix[0].length; y++) {
				for (int x = 0; x < matrix.length; x++) {
					temp[x][y] = (float) mf.mines[x][y];
					temp[x][y] -= adjacentFlags(x, y);
				}
			}

			for (int y = 0; y < matrix[0].length; y++) {
				for (int x = 0; x < matrix.length; x++) {
					if (!matrix[x][y].isEnabled() && temp[x][y] != 0) {
						// This is wrong!
						fracAdjacent(x, y, countAdjacent(x, y), temp);
					}
				}
			}
			if (isReset) isReset = false;
			else flag(nextFlag.x, nextFlag.y);
		}

		if (mf.progress == 0) mf.end(System.currentTimeMillis() - start);
	}

	public void reset() {
		flags = new int[matrix.length][matrix[0].length];
		// nextFlag = new Point(-1, -1);
		isReset = true;
	}

	private int adjacentFlags(int x, int y) {
		int sum = 0;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				try {
					if (matrix[x + j][y + i].isFlagged()) sum++;
				} catch (ArrayIndexOutOfBoundsException e) {
					// nop
				}
			}
		}
		return sum;
	}

	private void fracAdjacent(int x, int y, int mines, float[][] temp) {

		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				try {
					if (matrix[x + j][y + i].isEnabled()
							&& !matrix[x + j][y + i].isFlagged()) {
						temp[x + j][y + i] += temp[x][y] / (float) mines;
						if (temp[x + j][y + i] > tempMax) {
							tempMax = temp[x + j][y + i];
							nextFlag = new Point(x + j, y + i);
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					// nop
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
