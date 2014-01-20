package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class SolveButton extends JButton implements ActionListener {

	private static final long serialVersionUID = 1L;
	private Solver s;

	public SolveButton(Solver s) {
		super("Solve");
		this.s = s;
		addActionListener(this);
		setFocusable(false);
		setPreferredSize(new Dimension(140, 44));
		setFont(new Font("Consolas", Font.BOLD, 30));
		setMargin(new Insets(5, 0, 0, 0));
		setForeground(new Color(160, 160, 160));
		setBackground(new Color(240, 240, 240));
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		s.solve();
	}

}
