package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import gui.Constants.NoteType;

public class Note {
    private final int pitch; // Pitch value, e.g., 0 = Middle C, 2 = D, 4 = E, etc.
    private final String length; // "full", "half", "quarter", "8th", "16th"
    private NoteType noteType;
    private float startTime = 0; //in seconds
    private float endTime = 0; //in seconds

    public Note(int pitch, String length, NoteType noteType) {
        this.pitch = pitch;
        this.length = length;
        this.noteType = noteType;
    }
    
    public Note(int pitch, String length) {
        this(pitch, length, NoteType.NONE);
    }

    public Note(int pitch, String length, float startTime, float endTime) {
        this.pitch = pitch;
        this.length = length;
        this.startTime = startTime;
        this.endTime = endTime;
        this.noteType = NoteType.NONE;
    }

    public int getPitch() {
        return pitch;
    }
    
    public int getMidiPitch() {
    	return pitch + 60;
    }

    public String getLength() {
        return length;
    }

    public NoteType getNoteType() {
        return this.noteType;
    }

    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }

    public void draw(Graphics2D g2d, int x, int y) {
        int staffSpacing = Constants.LINE_SPACING / 2;
        int ledgerY;
        int topLinePitch = 10;
        int bottomLinePitch = 2;

        Color originalColor = g2d.getColor();
        Stroke originalStroke = g2d.getStroke();
        
        for (int r : Constants.HALF_NOTE_REMAINDERS) {
            if (Math.abs(pitch) % 12 == r) {
                g2d.drawString("#", x - 10, y);
            }
        }
    
        if (pitch > topLinePitch || pitch < bottomLinePitch) {
            ledgerY = y;
            if (pitch % 2 != 0) {
                if (bottomLinePitch > pitch) {
                    ledgerY = y - staffSpacing;
                }
                if (pitch > topLinePitch) {
                    ledgerY = y + staffSpacing;
                }
            }

            if (pitch > topLinePitch) {
                for (int currentPitch = topLinePitch + 2; currentPitch <= pitch; currentPitch += 2) {
                    g2d.drawLine(x - 10, ledgerY, x + 30, ledgerY);
                    ledgerY += staffSpacing * 2;
                }
            } else {
                for (int currentPitch = bottomLinePitch + 2; currentPitch >= pitch; currentPitch -= 2) {
                    g2d.drawLine(x - 10, ledgerY, x + 30, ledgerY);
                    ledgerY -= staffSpacing * 2;
                }
            }
        }

        if (this.noteType == NoteType.BLUEPRINT) {
            g2d.setColor(Color.GREEN);
        }

        switch (length) {
            case "full":
                g2d.drawOval(x, y - 5, 20, 10);
                break;
            case "half":
                g2d.drawOval(x, y - 5, 20, 10);
                g2d.drawLine(x + 20, y, x + 20, y - 50);
                break;
            case "quarter":
                g2d.fillOval(x, y - 5, 20, 10);
                g2d.drawLine(x + 20, y, x + 20, y - 50);
                break;
            case "8th":
                g2d.fillOval(x, y - 5, 20, 10);
                g2d.drawLine(x + 20, y, x + 20, y - 50);
                g2d.drawLine(x + 20, y - 50, x + 30, y - 40);
                break;
            case "16th":
                g2d.fillOval(x, y - 5, 20, 10);
                g2d.drawLine(x + 20, y, x + 20, y - 50);
                g2d.drawLine(x + 20, y - 45, x + 30, y - 35);
                g2d.drawLine(x + 20, y - 50, x + 30, y - 40);
                break;
        }

        if (this.noteType == NoteType.HOVERING) {
            int startX = x - 15;
            int startY = y - 60;
            int endX = x + 35;
            int endY = y + 20;

            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2.f));
            g2d.drawLine(startX, startY, endX, endY);
            g2d.drawLine(endX, startY, startX, endY);
        }

        g2d.setStroke(originalStroke);
        g2d.setColor(originalColor);
    }
    
    public int getTick() { //PPQ 24 mellett
    	return switch (length) {
		case "full" -> 96;
        case "half" -> 48;
        case "quarter" -> 24;
        case "8th" -> 12;
        case "16th" -> 6;
		default -> throw new IllegalArgumentException("Unexpected value: " + length);
		};
    }
}
