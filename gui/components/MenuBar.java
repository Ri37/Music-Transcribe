package gui.components;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

public class MenuBar extends JMenuBar {
	
	private JMenu fileMenu;
	private JMenuItem newSheetMenuItem;
	private JMenuItem saveSheetMenuItem;
	private JMenuItem openAudioMenuItem;
	private JMenuItem infoMenuItem;
	
	public MenuBar() {
		fileMenu = new JMenu("File");
		newSheetMenuItem = new JMenuItem();
		saveSheetMenuItem = new JMenuItem();
		openAudioMenuItem = new JMenuItem();
		infoMenuItem = new JMenuItem();
		
		newSheetMenuItem.setAction(new AbstractAction("New") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO create new sheet action
			}
		});
		
		saveSheetMenuItem.setAction(new AbstractAction("Save") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO save sheet action
			}
		});
		
		openAudioMenuItem.setAction(new AbstractAction("Read audio") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO open audio action
			}
		});
		
		infoMenuItem = new JMenuItem(new AbstractAction("Information") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO open info dialog action	
			}
		});
		
		fileMenu.add(newSheetMenuItem);
		fileMenu.add(saveSheetMenuItem);
		fileMenu.add(openAudioMenuItem);
		fileMenu.add(new JSeparator());
		fileMenu.add(infoMenuItem);
		
		add(fileMenu);
	}
}
