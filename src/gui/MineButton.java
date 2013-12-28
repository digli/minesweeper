package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class MineButton extends JButton implements MouseListener {

	public boolean isFlagged;

	private static final long serialVersionUID = 1L;
	private long lastClicked;
	private int x, y;
	private MineField mf;

	public MineButton(MineField mf, int x, int y) {
		this.mf = mf;
		this.x = x;
		this.y = y;
		setForeground(Color.RED);
		setFont(new Font("Consolas", Font.BOLD, 24));
		lastClicked = 0;
		isFlagged = false;
		addMouseListener(this);
		setPreferredSize(new Dimension(35, 35));
		setSize(35, 35);
		setFocusable(false);
		setMargin(new Insets(0, 0, 0, 0));
	}

	public void reset() {
		setEnabled(true);
		isFlagged = false;
		setText("");
	}

	public void click(boolean manual) {
		if (isEnabled() && !isFlagged) {
			setEnabled(false);
			switch (mf.mines[x][y]) {
			case -1:
				setText("�");
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
			if (mf.progress == 0 && manual)
				mf.end(MineField.WIN);
		}
		if (!isEnabled() && manual) {
			if (System.currentTimeMillis() - lastClicked < 300) {
				mf.checkAdjacent(x, y);
			}
			lastClicked = System.currentTimeMillis();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			mf.generate(x, y);
			click(true);
		}
		if (e.getButton() == MouseEvent.BUTTON3 && isEnabled()) {
			if (isFlagged)
				setText("");
			else
				setText("!");
			isFlagged = !isFlagged;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}