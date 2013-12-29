package gui;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ConfigWindow extends JFrame {
	
	// work in project
	private static final long serialVersionUID = 1L;

	public ConfigWindow(MineField mf) {
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(3, 2));
		
		int width = 0, height = 0, mines = 0;

		p.add(new JLabel("Set height: "));
		p.add(new JTextField());
		
		p.add(new JLabel("Set width: "));
		p.add(new JTextField());
		
		p.add(new JLabel("Set number of mines: "));

//		JLayeredPane pan = new JLayeredPane();
//		JLabel ontop = new JLabel("asdasd");
//		JButton bottom = new JButton("click me");
//		
//		pan.setLayer(ontop, 2);
//		pan.setLayer(bottom,  1);
//		pan.add(ontop);
//		pan.add(bottom);
//		
//		p.add(pan);
		
		mf.setConfig(width, height, mines); // TODO
		
		add(p);
		
		pack();
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}
