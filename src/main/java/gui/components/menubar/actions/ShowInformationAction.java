package gui.components.menubar.actions;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import gui.components.InformationDialog;

public class ShowInformationAction extends AbstractAction {
	private InformationDialog infoDialog;
	
	public ShowInformationAction(Window owner) {
		super("Information");
		infoDialog = new InformationDialog(owner);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		infoDialog.setVisible(true);;
	}

}
