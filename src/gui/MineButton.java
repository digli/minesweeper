package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class MineButton extends JButton implements MouseListener {

	// Solver only
	public int probability = 0;

	private static final long serialVersionUID = 1L;
	private boolean isFlagged, leftClick, leftBuffer, rightClick, bufferTime;
	private long lastClicked;
	private int x, y;
	private MineField mf;

	// private JLabel label;

	public MineButton(MineField mf, int x, int y) {
		this.mf = mf;
		this.x = x;
		this.y = y;
		leftClick = false;
		leftBuffer = false;
		rightClick = false;
		bufferTime = false;
		setFont(MineField.consolas.deriveFont(Font.BOLD, 34f));
		lastClicked = 0;
		addMouseListener(this);
		setPreferredSize(new Dimension(35, 35));
		setFocusable(false);
		setMargin(new Insets(5, 0, 0, 0));

		// label = new JLabel();
		// label.setFont(new Font("Consolas", Font.BOLD, 34));
		// label.setAlignmentX(CENTER_ALIGNMENT);
		// label.setAlignmentY(CENTER_ALIGNMENT);
		// label.setAutoscrolls(true);
		// add(label, BorderLayout.CENTER);
	}

	// Solver only
	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	// @Override
	// public void setText(String s) {
	// label.setText(s);
	// }

	public void reset() {
		setEnabled(true);
		isFlagged = false;

		setText("");
		setForeground(null);
		setBackground(null);

		// Solver only
		probability = 0;
	}

	// Solver only
	public void setFlagged() {
		if (isFlagged) return;
		setText("!");
		isFlagged = true;
		mf.updateMineCount(-1);
	}

	public boolean isFlagged() {
		return isFlagged;
	}

	// Returns true if button was clicked
	public boolean click(boolean manual) {
		// slowest method in history
		if (getBackground() != null) setBackground(null);
		boolean change = false;
		if (isEnabled() && !isFlagged && !bufferTime) {
			change = true;
			mf.generate(x, y);
			setEnabled(false);
			switch (mf.mines[x][y]) {
			case -1:
				setText("¤");
				setBackground(Color.RED);
				mf.end(MineField.LOSS);
				break;
			case 0:
				mf.progress--;
				mf.clickAdjacent(x, y);
				break;
			default:
				mf.progress--;
				setText(mf.mines[x][y] + "");
				break;
			}
		} else if (!isEnabled() && manual) {
			if (System.currentTimeMillis() - lastClicked < 300)
				mf.checkAdjacent(x, y);
			lastClicked = System.currentTimeMillis();
		}
		if (mf.progress == 0 && manual) mf.end(MineField.WIN);
		return change;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!isEnabled()) click(true);
		else {
			if (e.getButton() == MouseEvent.BUTTON1) leftClick = true;
			if (e.getButton() == MouseEvent.BUTTON3) {
				if (!leftClick) {
					if (isFlagged) {
						mf.updateMineCount(1);
						setText("");
					} else {
						mf.updateMineCount(-1);
						setText("!");
					}
					isFlagged = !isFlagged;
				}
				rightClick = true;
			}
			if (leftClick && rightClick) bufferTime = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (leftClick) {
				click(true);
				leftClick = false;
			}
			leftBuffer = false;
			bufferTime = false;
		}
		if (e.getButton() == MouseEvent.BUTTON3 && rightClick) {
			rightClick = false;
			if (!leftClick) bufferTime = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (leftBuffer) leftClick = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (leftClick) {
			leftClick = false;
			leftBuffer = true;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}
}
