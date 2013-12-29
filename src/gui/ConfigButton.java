package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ConfigButton extends JButton implements ActionListener {

	private static final long serialVersionUID = 1L;
	private MineField mf;

	public ConfigButton(MineField mf) {
		super("Config");
		this.mf = mf;
		addActionListener(this);
		setFocusable(false);
		setPreferredSize(new Dimension(140, 44));
		setFont(new Font("Consolas", Font.BOLD, 30));
		setMargin(new Insets(5, 0, 0, 0));
		setForeground(new Color(160, 160, 160));
		setBackground(new Color(240, 240, 240));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new ConfigWindow(mf);
	}
	
}
