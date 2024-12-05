package gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import gui.components.menubar.MenuBar;

public class MainFrame extends JFrame{
	private SheetMusicCanvas canvas;
	private ControlPanel controlPanel;
	private MenuBar menuBar;
	
	public MainFrame() {
		super("Sheet Music Maker");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(800, 600));
		
		setLayout(new BorderLayout());
		canvas = new SheetMusicCanvas();
		controlPanel = new ControlPanel(canvas, this);
		menuBar = new MenuBar(this);
		
		add(canvas, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.WEST);
		setJMenuBar(menuBar);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
