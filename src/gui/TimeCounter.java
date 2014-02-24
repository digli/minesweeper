package gui;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JButton;

public class TimeCounter extends JButton {

	private static final long serialVersionUID = 1L;
	private int time;

	public TimeCounter() {
		setEnabled(false);
		setFocusable(false);
		setPreferredSize(new Dimension(100, 44));
		setFont(MineField.consolas.deriveFont(30f));
		setMargin(new Insets(5, 0, 0, 0));
		setText(0 + "");
	}

	public void reset() {
		time = 0;
		setText(time + "");
	}

	public void increment() {
		setText(++time + "");
	}
}
