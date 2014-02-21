package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ConfigWindow extends JFrame {

	// work in progress
	// TODO: only allow typing of numbers in text fields
	private static final long serialVersionUID = 1L;
	private MineField mf;
	private JTextField widthInput, heightInput, minesInput;

	public ConfigWindow(MineField mf) {
		this.mf = mf;

		JPanel container = new JPanel();
		container.setLayout(new GridLayout(3, 2));

		container.add(new JLabel("New width:"));
		widthInput = new JTextField();
		widthInput.setText(mf.getGridWidth() + "");
		container.add(widthInput);
		container.add(new JLabel("New height:"));
		heightInput = new JTextField();
		heightInput.setText(mf.getGridHeight() + "");
		container.add(heightInput);
		container.add(new JLabel("Number of mines:"));
		minesInput = new JTextField();
		minesInput.setText(mf.getNbrOfMines() + "");
		container.add(minesInput);

		add(container);
		add(new JLabel("ConfigWindow is WIP"), BorderLayout.NORTH);
		add(new OkButton(this), BorderLayout.SOUTH);

		pack();

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2
				- getSize().height / 2);
		setVisible(true);
		setDefaultCloseOperation(close());
	}

	private int close() {
		mf.resetConfigButton();
		return DISPOSE_ON_CLOSE;
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
			cw.dispose();
			int width = mf.getGridWidth(), height = mf.getGridHeight(), mines = mf
					.getNbrOfMines();

			try {
				width = Integer.parseInt(widthInput.getText());
				height = Integer.parseInt(heightInput.getText());
				mines = Integer.parseInt(minesInput.getText());
			} catch (NumberFormatException e) {
				System.out.println("NumberFormatException");
			}

			mf.setConfig(width, height, mines);
			mf.resetConfigButton();
		}
	}
}
