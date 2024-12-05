package gui.components.menubar.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javax.swing.AbstractAction;

public class OpenAudioAction extends AbstractAction {

	private final JFrame frame;

	public OpenAudioAction(JFrame frame) {
		super("Read audio");

		this.frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser fileChooser = new JFileChooser();
		int res = fileChooser.showOpenDialog(frame);

		if (res == JFileChooser.APPROVE_OPTION) {
			// TODO
		}
	}

}
