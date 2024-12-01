import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SheetMusicMaker {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Sheet Music Maker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        frame.setLayout(new BorderLayout());

        SheetMusicCanvas canvas = new SheetMusicCanvas();
        frame.add(canvas, BorderLayout.CENTER);

        JPanel menuBar = new MenuBarPanel(canvas, frame);
        frame.add(menuBar, BorderLayout.WEST);

        frame.setVisible(true);
    }
}

class Constants {
    public static final int ROW_STARTER_NUM = 8;
    public static final int ROW_WIDTH = 800;
    public static final int ROW_SPACING = 100;
    public static final int LINE_SPACING = 10;

    public static final int MENU_BAR_WIDTH = 150;
    public static final int MENU_BAR_THRESHOLD = 600;
    public static final double MENU_BAR_PERCENTAGE = 0.2;

    public static final double ZOOM_MIN = 0.5;
    public static final double ZOOM_MAX = 3.0;
}

class MenuBarPanel extends JPanel {
    private final SheetMusicCanvas canvas;
    private final JFrame frame;
    private final List<JButton> buttons = new ArrayList<>();

    public MenuBarPanel(SheetMusicCanvas canvas, JFrame frame) {
        this.canvas = canvas;
        this.frame = frame;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.LIGHT_GRAY);
        adjustMenuBarWidth();

        addButton("Zoom In", e -> canvas.zoom(0.1, canvas.getWidth() / 2, canvas.getHeight() / 2));
        addButton("Zoom Out", e -> canvas.zoom(-0.1, canvas.getWidth() / 2, canvas.getHeight() / 2));
        addButton("Reset View", e -> {
            canvas.getCamera().setPosition(0, 0);
            canvas.getCamera().setZoom(1.0);
            canvas.repaint();
        });
        addButton("Add Row", e -> canvas.addRow());

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustMenuBarWidth();
            }
        });
    }

    private void adjustMenuBarWidth() {
        int windowWidth = frame.getWidth();
        int newWidth = (windowWidth > Constants.MENU_BAR_THRESHOLD)
                ? (int) (windowWidth * Constants.MENU_BAR_PERCENTAGE)
                : (int) (Constants.MENU_BAR_THRESHOLD * Constants.MENU_BAR_PERCENTAGE);

        setPreferredSize(new Dimension(newWidth, 0));

        // Update button sizes based on menu bar width
        for (JButton button : buttons) {
            button.setMaximumSize(new Dimension(newWidth - 20, 40));
            button.setPreferredSize(new Dimension(newWidth - 20, 40));
        }

        canvas.setMenuBarWidth(newWidth);

        revalidate();
        repaint();
    }

    private void addButton(String label, ActionListener action) {
        JButton button = new JButton(label);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(action);

        int initialWidth = (frame.getWidth() > Constants.MENU_BAR_THRESHOLD)
                ? (int) (frame.getWidth() * Constants.MENU_BAR_PERCENTAGE)
                : (int) (Constants.MENU_BAR_THRESHOLD * Constants.MENU_BAR_PERCENTAGE);

        button.setMaximumSize(new Dimension(initialWidth - 20, 40));
        button.setPreferredSize(new Dimension(initialWidth - 20, 40));

        buttons.add(button);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(button);
    }
}

class Camera {
    private double x;
    private double y;
    private double zoom;

    public Camera() {
        this.x = 0;
        this.y = 0;
        this.zoom = 1.0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZoom() {
        return zoom;
    }

    public void setPosition(double x, double y) {
        this.x = x;//Math.max(-200, Math.min(x, 600));
        this.y = y;//Math.max(-200, Math.min(y, 600));
    }

    public void move(double dx, double dy) {
        setPosition(x + dx, y + dy);
    }

    public void setZoom(double zoom) {
        this.zoom = Math.max(Constants.ZOOM_MIN, Math.min(zoom, Constants.ZOOM_MAX));
    }
}


class Note {
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


class SheetRow {
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

    public void addNote(Note note) {
        notes.add(note);
    }

    public void draw(Graphics2D g2d, int canvasWidth) {
        int xOffset = (canvasWidth - Constants.ROW_WIDTH) / 2;

        for (int i = 0; i < 5; i++) {
            int y = startY + (i * Constants.LINE_SPACING);
            g2d.drawLine(xOffset, y, xOffset + Constants.ROW_WIDTH, y);
        }

        double middleCPitch = 10.0;

        int noteX = xOffset + 50;
        for (Note note : notes) {
            double offset = (middleCPitch - note.getPitch()) * Constants.LINE_SPACING / 2.0;
            int noteY = (int) (startY + offset);

            note.draw(g2d, noteX, noteY);
            noteX += 50;
        }
    }
}


class Page {
    private final List<SheetRow> rows;

    public Page() {
        rows = new ArrayList<>();
        for (int i = 0; i < Constants.ROW_STARTER_NUM; i++) {
            addRow();
        }

        addBociBociToSheetRow();
    }

    public List<SheetRow> getRows() {
        return rows;
    }

    public void addRow() {
        int startY = 50 + rows.size() * Constants.ROW_SPACING;
        rows.add(new SheetRow(startY));
    }

    public int getTotalHeight() {
        return (rows.size() + 2) * Constants.ROW_SPACING;
    }

    public void draw(Graphics2D g2d, int canvasWidth) {
        for (SheetRow row : rows) {
            row.draw(g2d, canvasWidth);
        }
    }

    private void addBociBociToSheetRow() {
        SheetRow firstRow = rows.get(0);
        SheetRow secondRow = rows.get(1);
        SheetRow thirdRow = rows.get(2);
    
        // Elso utem: Boci boci tarka
        firstRow.addNote(new Note(-2, "8th"));  // Bo-
        firstRow.addNote(new Note(0, "8th"));  // Bo-
        firstRow.addNote(new Note(2, "8th"));  // ci
        firstRow.addNote(new Note(0, "8th"));  // bo-
        firstRow.addNote(new Note(2, "8th"));  // ci
        firstRow.addNote(new Note(4, "quarter")); // tar-
        firstRow.addNote(new Note(4, "quarter")); // ka
    
        // Masodik utem: Se füle se farka
        firstRow.addNote(new Note(0, "8th"));  // Se
        firstRow.addNote(new Note(2, "8th"));  // fü-
        firstRow.addNote(new Note(0, "8th"));  // le
        firstRow.addNote(new Note(2, "8th"));  // se
        firstRow.addNote(new Note(4, "quarter")); // far-
        firstRow.addNote(new Note(4, "quarter")); // ka
    
        // Harmadik utem: Oda megyünk lakni
        secondRow.addNote(new Note(7, "8th"));  // O-
        secondRow.addNote(new Note(6, "8th"));  // da
        secondRow.addNote(new Note(5, "8th"));  // me-
        secondRow.addNote(new Note(4, "8th"));  // gyünk
        secondRow.addNote(new Note(3, "quarter")); // lak-
        secondRow.addNote(new Note(5, "quarter")); // ni
    
        // Negyedik utem: Ahol tejet kapni
        secondRow.addNote(new Note(4, "8th"));  // A-
        secondRow.addNote(new Note(3, "8th"));  // hol
        secondRow.addNote(new Note(2, "8th"));  // te-
        secondRow.addNote(new Note(1, "8th"));  // jet
        secondRow.addNote(new Note(0, "quarter")); // kap-
        secondRow.addNote(new Note(0, "quarter")); // ni

        thirdRow.addNote(new Note(-6, "8th"));  // A-
        thirdRow.addNote(new Note(-5, "8th"));  // hol
        thirdRow.addNote(new Note(-4, "8th"));  // te-
        thirdRow.addNote(new Note(-3, "8th"));  // jet
        thirdRow.addNote(new Note(-2, "quarter")); // kap-
        thirdRow.addNote(new Note(-1, "quarter")); // ni

        thirdRow.addNote(new Note(9, "8th"));  // A-
        thirdRow.addNote(new Note(10, "8th"));  // hol
        thirdRow.addNote(new Note(11, "8th"));  // te-
        thirdRow.addNote(new Note(12, "8th"));  // jet
        thirdRow.addNote(new Note(13, "quarter")); // kap-
        thirdRow.addNote(new Note(14, "quarter")); // ni
    }      
}


class SheetMusicCanvas extends JPanel implements MouseWheelListener, MouseMotionListener, MouseListener {
    private final Page page;
    private final Camera camera;
    private Point lastMousePosition;
    private int menuBarWidth;

    public SheetMusicCanvas() {
        this.page = new Page();
        this.camera = new Camera();
        setBackground(Color.WHITE);

        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public Camera getCamera() {
        return camera;
    }

    public Page getPage() {
        return this.page;
    }

    public void setMenuBarWidth(int menuBarWidth) {
        this.menuBarWidth = menuBarWidth;
    }

    public int getMenuBarWidth() {
        return this.menuBarWidth;
    }

    public void zoom(double zoomDelta, int centerX, int centerY) {
        double oldZoom = camera.getZoom();
        double newZoom = oldZoom + zoomDelta;
    
        if (newZoom < Constants.ZOOM_MIN || newZoom > Constants.ZOOM_MAX) {
            return;
        }
    
        double mouseX = centerX / oldZoom + camera.getX();
        double mouseY = centerY / oldZoom + camera.getY();
    
        camera.setZoom(newZoom);
    
        double newMouseX = centerX / newZoom + camera.getX();
        double newMouseY = centerY / newZoom + camera.getY();
    
        camera.setPosition(
            camera.getX() + (mouseX - newMouseX),
            camera.getY() + (mouseY - newMouseY)
        );
    
        repaint();
    }
    

    public void addRow() {
        if (page.getRows().size() < 12) {
            page.addRow();
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        camera.setPosition(
            camera.getX(),
            camera.getY()
        );

        g2d.scale(camera.getZoom(), camera.getZoom());
        g2d.translate(-camera.getX(), -camera.getY());

        page.draw(g2d, getWidth());
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        double zoomDelta = -e.getPreciseWheelRotation() * 0.1;
        zoom(zoomDelta, mouseX, mouseY);
    }

    @Override
public void mouseDragged(MouseEvent e) {
    if (lastMousePosition != null) {
        int deltaX = e.getX() - lastMousePosition.x;
        int deltaY = e.getY() - lastMousePosition.y;
        camera.move(
            (-deltaX) / camera.getZoom(),
            (-deltaY) / camera.getZoom()
        );
        repaint();
    }
    lastMousePosition = e.getPoint();
}


    @Override
    public void mousePressed(MouseEvent e) {
        lastMousePosition = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastMousePosition = null;
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
