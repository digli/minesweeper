package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;

public class ConfigButton extends JButton {

	private static final long serialVersionUID = 1L;

	public ConfigButton() {
		super("Config");
		setFocusable(false);
		setPreferredSize(new Dimension(140, 44));
		setFont(new Font("Consolas", Font.BOLD, 30));
		setMargin(new Insets(5, 0, 0, 0));
	}
	
}
