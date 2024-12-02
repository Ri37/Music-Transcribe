package gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

public class MainFrame extends JFrame{
	private SheetMusicCanvas canvas;
	private MenuBarPanel menuBar;
	
	public MainFrame() {
		super("Sheet Music Maker");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(800, 600));
		
		setLayout(new BorderLayout());
		canvas = new SheetMusicCanvas();
		menuBar = new MenuBarPanel(canvas, this);
		
		add(canvas, BorderLayout.CENTER);
		add(menuBar, BorderLayout.WEST);
		
		pack();
		setVisible(true);
	}
}
