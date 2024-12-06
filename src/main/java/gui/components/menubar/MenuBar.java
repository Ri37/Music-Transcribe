package gui.components.menubar;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import gui.components.SheetMusicCanvas;

import gui.components.menubar.actions.NewSheetAction;
import gui.components.menubar.actions.OpenAudioAction;
import gui.components.menubar.actions.SaveSheetAction;
import gui.components.menubar.actions.ShowInformationAction;

public class MenuBar extends JMenuBar {

	private final SheetMusicCanvas canvas;
	private final JFrame frame;

	private JMenu fileMenu;
	private JMenuItem newSheetMenuItem;
	private JMenuItem saveSheetMenuItem;
	private JMenuItem openAudioMenuItem;
	private JMenuItem infoMenuItem;

	public MenuBar(SheetMusicCanvas canvas, JFrame frame) {
		super();

		this.canvas = canvas;
		this.frame = frame;


		fileMenu = new JMenu("File");
		newSheetMenuItem = new JMenuItem(new NewSheetAction());
		saveSheetMenuItem = new JMenuItem(new SaveSheetAction());
		openAudioMenuItem = new JMenuItem(new OpenAudioAction(this.frame, this.canvas));
		infoMenuItem = new JMenuItem(new ShowInformationAction(this.frame));

		fileMenu.add(newSheetMenuItem);
		fileMenu.add(saveSheetMenuItem);
		fileMenu.add(openAudioMenuItem);
		fileMenu.add(new JSeparator());
		fileMenu.add(infoMenuItem);

		add(fileMenu);
	}
}
