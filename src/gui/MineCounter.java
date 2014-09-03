package gui;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;

public class MineCounter extends JButton {

	private static final long serialVersionUID = 1L;
	private int currentMines;

	public MineCounter(int nbrOfMines) {
		setEnabled(false);
		setFocusable(false);
		setPreferredSize(new Dimension(100, 44));
		setFont(MineField.consolas.deriveFont(30f));
		setMargin(new Insets(5, 0, 0, 0));
		setText(nbrOfMines + "");
	}

	public void reset(int nbrOfMines) {
		currentMines = nbrOfMines;
		setText(currentMines + "");
	}

	public void update(int diff) {
		currentMines += diff;
		setText(currentMines + "");
	}
}
