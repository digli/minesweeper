package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ConfigWindow extends JFrame {

	// work in progress
	private static final long serialVersionUID = 1L;
	private MineField mf;

	public ConfigWindow(MineField mf) {
		this.mf = mf;

		add(new JLabel("ConfigWindow is WIP"), BorderLayout.NORTH);
		add(new OkButton(this), BorderLayout.SOUTH);

		int width = 0, height = 0, mines = 0;
		mf.setConfig(width, height, mines); // TODO

		pack();
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public class OkButton extends JButton implements ActionListener {

		private static final long serialVersionUID = 1L;
		private ConfigWindow cw;

		public OkButton(ConfigWindow cw) {
			super("OK");
			this.cw = cw;
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			mf.resetConfigButton();
			cw.dispose();
		}
	}
}