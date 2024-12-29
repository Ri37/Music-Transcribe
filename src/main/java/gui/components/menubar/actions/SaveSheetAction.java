package gui.components.menubar.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import gui.Note;
import gui.SheetRow;
import gui.components.MainFrame;

public class SaveSheetAction extends AbstractAction {
	
	private MainFrame owner;
	
	public SaveSheetAction(MainFrame owner) {
		super("Save");
		this.owner = owner;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			String outputFileName = chooseOutput();
			if(outputFileName == null)
				return;
			
            // 1. Sequence létrehozása
            Sequence sequence = new Sequence(Sequence.PPQ, 24);

            // 2. Track hozzáadása
            Track track = sequence.createTrack();

            // 3. Hangjegyek hozzáadása
            int tick = 0;
            for(SheetRow row : owner.getCanvas().getPage().getRows()) {
            	tick += saveRow(row, track, tick);
            }
//            owner.getCanvas().getPage().getRows().stream().forEach(row -> saveRow(row, track));

            // 4. MIDI fájl mentése
            MidiSystem.write(sequence, 1, new java.io.File(outputFileName));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
	}
	
	private String chooseOutput() {
		// Létrehozunk egy JFileChooser-t
        JFileChooser fileChooser = new JFileChooser();

        // Beállítunk egy egyedi FileFilter-t
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                // Elfogadjuk a mappákat, hogy lehessen böngészni
                if (file.isDirectory()) {
                    return true;
                }

                // Ellenőrizzük a fájl kiterjesztését
                String fileName = file.getName().toLowerCase();
                return fileName.endsWith(".midi");
            }

            @Override
            public String getDescription() {
                // A leírás, ami megjelenik a felhasználónak
                return "MIDI Files (*.midi)";
            }
        });

        // Megnyitjuk a mentési párbeszédablakot
        int result = fileChooser.showSaveDialog(owner);

        if (result == JFileChooser.APPROVE_OPTION) {
            // A felhasználó kiválasztott egy fájlt
            File selectedFile = fileChooser.getSelectedFile();

            // Ellenőrizzük, hogy a fájlnév tartalmazza-e a .midi kiterjesztést
            if (!selectedFile.getName().toLowerCase().endsWith(".midi")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".midi");
            }

            // Ellenőrizzük, hogy a fájl már létezik-e
            if (selectedFile.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(null,
                        "A fájl már létezik. Felülírja?",
                        "Fájl felülírása",
                        JOptionPane.YES_NO_OPTION);

                if (overwrite != JOptionPane.YES_OPTION) {
                    return null;
                }
            }
            return selectedFile.getAbsolutePath();
        }
        
        return null;
	}
	
	private int saveRow(SheetRow row, Track track, int tick) {
		for(Note note : row.getNotes()) {
			try {
				ShortMessage noteOn = new ShortMessage();
				noteOn.setMessage(ShortMessage.NOTE_ON, 0, note.getMidiPitch(), 93);
				track.add(new MidiEvent(noteOn, tick));
				ShortMessage noteOff = new ShortMessage();
				noteOff.setMessage(ShortMessage.NOTE_OFF, 0, note.getMidiPitch(), 0);
				track.add(new MidiEvent(noteOff, tick + note.getTick()));
			} catch (InvalidMidiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tick+=note.getTick();			
		}
		
		return tick;
	}

}
