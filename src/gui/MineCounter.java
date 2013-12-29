package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;

public class MineCounter extends JButton {

	private static final long serialVersionUID = 1L;
	private int nbrOfMines;
	private int currentMines;

	public MineCounter(int nbrOfMines) {
		this.nbrOfMines = nbrOfMines;
		currentMines = nbrOfMines;
		setEnabled(false);
		setFocusable(false);
		setPreferredSize(new Dimension(100, 44));
		setFont(new Font("Consolas", Font.BOLD, 30));
		setMargin(new Insets(5, 0, 0, 0));
		setText(currentMines + "");
	}

	public void reset() {
		currentMines = nbrOfMines;
		setText(currentMines + "");
	}

	public void update(int diff) {
		currentMines += diff;
		setText(currentMines + "");
	}
}
