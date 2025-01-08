package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.Font;
import java.awt.geom.AffineTransform;

import gui.Constants.NoteType;

public class Note {
    private final int pitch; // Pitch value, e.g., 0 = Middle C, 1 = C#, 2 = D, etc.
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
        int staffSpacing = Constants.LINE_SPACING;
        int ledgerY;
        int topLinePitch = -2;
        int bottomLinePitch = -10;

        Color originalColor = g2d.getColor();
        Stroke originalStroke = g2d.getStroke();
        Font originalFont = g2d.getFont();
        
        for (int r : Constants.HALF_NOTE_REMAINDERS) {
            if (Math.abs(pitch + 60) % 12 == r) {
                Font largerFont = originalFont.deriveFont(originalFont.getSize() + 5.0f);
                g2d.setFont(largerFont);
                g2d.drawString("#", x - 10, y + 5);
                g2d.setFont(originalFont);
            }
        }
    
        int diatonicOffset = calculateAbsoluteDiatonicOffset(pitch);

        if (diatonicOffset > topLinePitch) {
            int extraLines = (diatonicOffset - topLinePitch + 1) / 2;
            for (int i = 0; i < extraLines; i++) {
                int isOnLine = (diatonicOffset % 2 == 0) ? 0 : 1;
                ledgerY = y - (i + isOnLine) * staffSpacing + staffSpacing/2*isOnLine;
                if (diatonicOffset % 2 == 0 || i < extraLines - 1) {
                    g2d.drawLine(x - 10, ledgerY, x + 30, ledgerY);
                }
            }
        } else if (diatonicOffset < bottomLinePitch) {
            int extraLines = (bottomLinePitch - diatonicOffset + 1) / 2;
            for (int i = 0; i < extraLines; i++) {
                int isOnLine = (diatonicOffset % 2 == 0) ? 0 : 1;
                ledgerY = y + (i + isOnLine) * staffSpacing - staffSpacing/2*isOnLine;
                if (diatonicOffset % 2 == 0 || i < extraLines - 1) {
                    g2d.drawLine(x - 10, ledgerY, x + 30, ledgerY);
                }
            }
        }
        
        if (this.noteType == NoteType.BLUEPRINT) {
            g2d.setColor(Color.GREEN);
        }

        switch (length) {
            case "full":
                drawRotatedOval(g2d, x, y - 5, 20, 10, -15);
                break;
            case "half":
                drawRotatedOval(g2d, x, y - 5, 20, 10, -15);
                g2d.drawLine(x + 20, y, x + 20, y - 50);
                break;
            case "quarter":
                drawRotatedFilledOval(g2d, x, y - 5, 20, 10, -15);
                g2d.drawLine(x + 20, y, x + 20, y - 50);
                break;
            case "8th":
                drawRotatedFilledOval(g2d, x, y - 5, 20, 10, -15);
                g2d.drawLine(x + 20, y, x + 20, y - 50);
                g2d.drawLine(x + 20, y - 50, x + 30, y - 40);
                break;
            case "16th":
                drawRotatedFilledOval(g2d, x, y - 5, 20, 10, -15);
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

    private void drawRotatedOval(Graphics2D g2d, int x, int y, int width, int height, double angle) {
        AffineTransform originalTransform = g2d.getTransform();
        g2d.rotate(Math.toRadians(angle), x + width / 2.0, y + height / 2.0);
        g2d.drawOval(x, y, width, height);
        g2d.setTransform(originalTransform);
    }
    
    private void drawRotatedFilledOval(Graphics2D g2d, int x, int y, int width, int height, double angle) {
        AffineTransform originalTransform = g2d.getTransform();
        g2d.rotate(Math.toRadians(angle), x + width / 2.0, y + height / 2.0);
        g2d.fillOval(x, y, width, height);
        g2d.setTransform(originalTransform);
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
