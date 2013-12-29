package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JButton;

public class TimeCounter extends JButton {

	private static final long serialVersionUID = 1L;
	private int time;
	
	public TimeCounter() {
		time = 0;
		setEnabled(false);
		setFocusable(false);
		setPreferredSize(new Dimension(100, 44));
		setFont(new Font("Consolas", Font.BOLD, 30));
		setMargin(new Insets(5, 0, 0, 0));
		setText(time + "");
	}
	
	public void reset() {
		time = 0;
		setText(time + "");
	}
	
	public void increment() {
		time++;
		setText(time + "");
	}
}
