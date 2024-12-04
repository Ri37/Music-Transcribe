package gui.components.menubar;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import gui.components.menubar.actions.NewSheetAction;
import gui.components.menubar.actions.OpenAudioAction;
import gui.components.menubar.actions.SaveSheetAction;
import gui.components.menubar.actions.ShowInformationAction;

public class MenuBar extends JMenuBar {
	
	private JMenu fileMenu;
	private JMenuItem newSheetMenuItem;
	private JMenuItem saveSheetMenuItem;
	private JMenuItem openAudioMenuItem;
	private JMenuItem infoMenuItem;
	
	public MenuBar() {
		super();
		
		fileMenu = new JMenu("File");
		newSheetMenuItem = new JMenuItem(new NewSheetAction());
		saveSheetMenuItem = new JMenuItem(new SaveSheetAction());
		openAudioMenuItem = new JMenuItem(new OpenAudioAction());
		infoMenuItem = new JMenuItem(new ShowInformationAction());
		
		fileMenu.add(newSheetMenuItem);
		fileMenu.add(saveSheetMenuItem);
		fileMenu.add(openAudioMenuItem);
		fileMenu.add(new JSeparator());
		fileMenu.add(infoMenuItem);
		
		add(fileMenu);
	}
}