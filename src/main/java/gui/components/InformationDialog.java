package gui.components;

import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class InformationDialog extends JDialog{
	protected JPanel contentPanel;
	protected JTextArea textArea;
	protected JButton okButton;
	
	public InformationDialog(Window owner) {
		super(owner);
		setTitle("Information");
		setModalityType(ModalityType.APPLICATION_MODAL);
		contentPanel = new JPanel();
		textArea = new JTextArea();
		textArea.setText("""
This application is created by:
	- Norbert Ádám Mihalkó
	- Péter Mihály Vörös
	- Péter Rajcsányi
	- Richard Antonio Nagy
	- Róbert Tóth""");
		
		contentPanel.setLayout(new FlowLayout());
		contentPanel.add(textArea);
		add(contentPanel);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
	}
}
