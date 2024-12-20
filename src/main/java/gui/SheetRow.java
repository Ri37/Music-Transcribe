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

        double middleCPitch = 10.0;
        double offset = (middleCPitch - note.getPitch()) * Constants.LINE_SPACING / 2.0;
        int noteY = (int) (startY + offset);

        return new Point(noteX, noteY);
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
