package gui;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class SheetRow {
    private final List<Note> notes;
    private final int startY;

    public SheetRow(int startY) {
        this.notes = new ArrayList<>();
        this.startY = startY;
    }

    public int getStartY() {
        return startY;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void addNote(Note note, int index) {
        if (index < 0 || index > notes.size()) {
            addNote(note);
        } else {
            notes.add(index, note);
        }
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public void removeNote(Note note) {
        notes.remove(note);
    }

    public Point getNotePosition(Note note, int noteIndex, int canvasWidth) {
        int xOffset = (canvasWidth - Constants.ROW_WIDTH) / 2;
        int noteX = xOffset + 50 + (noteIndex * 50);
    
        double offsetPerHalfStep = Constants.LINE_SPACING / 2.0;
    
        int absoluteDiatonicOffset = calculateAbsoluteDiatonicOffset(note.getPitch());
    
        int noteY = (int) (startY + ((absoluteDiatonicOffset + 10) * offsetPerHalfStep));
    
        return new Point(noteX, noteY);
    }
    
    private int calculateAbsoluteDiatonicOffset(double pitch) {
        int intPitch = (int) pitch;
        int octaveOffset = (int)Math.floor(intPitch / 12.0) * 7;;
        int normalizedPitch = Math.floorMod(intPitch, 12);
    
        int diatonicOffset = calculateDiatonicOffset(normalizedPitch);
    
        return -octaveOffset + diatonicOffset; 
    }
    
    
    private int calculateDiatonicOffset(int normalizedPitch) {
        switch (normalizedPitch) {
            case 0: return 0;  // C
            case 2: return -1; // D
            case 4: return -2; // E
            case 5: return -3; // F
            case 7: return -4; // G
            case 9: return -5; // A
            case 11: return -6; // B
            default:
                // Sharps
                if (normalizedPitch == 1) return 0;  // C#
                if (normalizedPitch == 3) return -1; // D#
                if (normalizedPitch == 6) return -3; // F#
                if (normalizedPitch == 8) return -4; // G#
                if (normalizedPitch == 10) return -5; // A#
        }
        return 0;
    }

    public void draw(Graphics2D g2d, int canvasWidth) {
        int xOffset = (canvasWidth - Constants.ROW_WIDTH) / 2;

        for (int i = 0; i < 5; i++) {
            int y = startY + (i * Constants.LINE_SPACING);
            g2d.drawLine(xOffset, y, xOffset + Constants.ROW_WIDTH, y);
        }

        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            Point notePosition = getNotePosition(note, i, canvasWidth);
            note.draw(g2d, notePosition.x, notePosition.y);
        }
    }
}
