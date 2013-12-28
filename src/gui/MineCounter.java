package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;

public class MineCounter extends JButton {

	private static final long serialVersionUID = 2L;
	private int nbrOfMines;
	private int currentMines;

	public MineCounter(int nbrOfMines) {
		this.nbrOfMines = nbrOfMines;
		currentMines = nbrOfMines;
		setEnabled(false);
		setFocusable(false);
		setPreferredSize(new Dimension(70, 44));
		setFont(new Font("Consolas", Font.BOLD, 30));
		setMargin(new Insets(5, 0, 0, 0));
		display();
	}

	public void reset() {
		currentMines = nbrOfMines;
		display();
	}

	public void update(int diff) {
		currentMines += diff;
		display();
	}

	private void display() {
		setText(currentMines + "");
	}
}
