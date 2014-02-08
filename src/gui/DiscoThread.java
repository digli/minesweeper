package gui;

import java.awt.Color;
import java.util.LinkedList;

public class DiscoThread extends Thread {

	private MineButton[][] matrix;
	private int width, height, tiles;
	private Color[] colors = { Color.MAGENTA, Color.ORANGE, Color.CYAN,
			Color.YELLOW, Color.BLUE, Color.GREEN, Color.RED };

	public DiscoThread(MineButton[][] matrix, int width, int height) {
		this.matrix = matrix;
		this.width = width;
		this.height = height;
		tiles = height * width / 5;
	}

	public void run() {
		LinkedList<MineButton> list = new LinkedList<MineButton>();
		while (!interrupted()) {
			for (int i = 0; i < tiles; i++) {
				int tempX = (int) (Math.random() * width);
				int tempY = (int) (Math.random() * height);
				list.add(matrix[tempX][tempY]);

				matrix[tempX][tempY].setBackground(colors[i % colors.length]);
			}

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				for (MineButton b : list)
					b.setBackground(null);
				return;
			}

			for (MineButton b : list)
				b.setBackground(null);
		}
	}
}
