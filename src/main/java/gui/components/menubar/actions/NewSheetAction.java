package gui.components.menubar.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;

import gui.Constants;
import gui.Page;
import gui.SheetRow;
import gui.components.MainFrame;

public class NewSheetAction extends AbstractAction {
	private final MainFrame frame;

	public NewSheetAction(MainFrame frame) {
		super("New");
		this.frame = frame;
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		ArrayList<SheetRow> initialRows = new ArrayList<>(Constants.ROW_STARTER_NUM);
		Page page = new Page(initialRows);
		frame.getCanvas().setPage(page);
	}

}
