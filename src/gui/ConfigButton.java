package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ConfigButton extends JButton implements ActionListener {

	private static final long serialVersionUID = 1L;
	private boolean isPressed;
	private MineField mf;

	public ConfigButton(MineField mf) {
		super("Config");
		this.mf = mf;
		isPressed = false;
		addActionListener(this);
		setFocusable(false);
		setPreferredSize(new Dimension(140, 44));
		setFont(MineField.consolas.deriveFont(30f));
		setMargin(new Insets(5, 0, 0, 0));
		setForeground(new Color(160, 160, 160));
		setBackground(new Color(240, 240, 240));
	}

	public void reset() {
		isPressed = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!isPressed) {
			isPressed = true;
			new ConfigWindow(mf);
		}
	}
}