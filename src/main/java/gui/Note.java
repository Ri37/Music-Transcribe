package gui;

import java.awt.Graphics2D;

public class Note {
    private final int pitch; // Pitch value, e.g., 0 = Middle C, 2 = D, 4 = E, etc.
    private final String length; // "full", "half", "quarter", "8th", "16th"

    public Note(int pitch, String length) {
        this.pitch = pitch;
        this.length = length;
    }

    public int getPitch() {
        return pitch;
    }

    public String getLength() {
        return length;
    }

    public void draw(Graphics2D g2d, int x, int y) {
        int staffSpacing = Constants.LINE_SPACING / 2;
        int ledgerY;
        int topLinePitch = 10;
        int bottomLinePitch = 2;

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
    }
}