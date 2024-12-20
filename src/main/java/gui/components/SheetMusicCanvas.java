package gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Iterator;

import javax.swing.JPanel;

import gui.Camera;
import gui.Constants;
import gui.Constants.CanvasMode;
import gui.Constants.NoteType;
import gui.Note;
import gui.Page;
import gui.SheetRow;

public class SheetMusicCanvas extends JPanel implements MouseWheelListener, MouseMotionListener, MouseListener {
	private CanvasMode mode;

	private final Camera camera;
	private Page page;
	private Point lastMousePosition;
	private int menuBarWidth;

	public SheetMusicCanvas() {
		this.page = Page.createBociBociPage();
		this.camera = new Camera();
		setBackground(Color.WHITE);

		addMouseWheelListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
	}

	public CanvasMode getCanvasMode() {
		return mode;
	}

	public void setCanvasMode(CanvasMode mode) {
		this.mode = mode;
		clearBlueprintNotes();
	}

	public Camera getCamera() {
		return camera;
	}

	public void setPage(Page page) {
		this.page = page;
		System.out.println("New page set with rows: " + this.page.getRows().size());
		revalidate();
		repaint();
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

		camera.setPosition(camera.getX() + (mouseX - newMouseX), camera.getY() + (mouseY - newMouseY));

		repaint();
	}

	public void addRow() {
		if (page.getRows().size() < 12) {
			page.addRow();
			repaint();
		}
	}

	private Point applyCameraTransform(Point mousePoint) {
		double x = (mousePoint.x / camera.getZoom()) + camera.getX();
		double y = (mousePoint.y / camera.getZoom()) + camera.getY();
		return new Point((int)x, (int)y);
	}

	private void clearBlueprintNotes()
	{
		for (SheetRow row : page.getRows()) {
			Iterator<Note> iterator = row.getNotes().iterator();
			while (iterator.hasNext()) {
				Note note = iterator.next();
				if (note.getNoteType() == NoteType.BLUEPRINT) {
					iterator.remove();
				}
			}
		}
		repaint();
	}

	public void addMouseMoved(MouseEvent e) {
		clearBlueprintNotes();

		Point mousePoint = applyCameraTransform(e.getPoint());
		SheetRow targetRow = null;

		for (SheetRow row : page.getRows()) {
			if (mousePoint.y >= row.getStartY() && mousePoint.y < row.getStartY() + Constants.ROW_SPACING) {
				targetRow = row;
				break;
			}
		}

		if (targetRow == null) {
			return;
		}

		int noteIndex = (mousePoint.x - (getWidth() - Constants.ROW_WIDTH) / 2) / 50 - 1;
		if (noteIndex < 0) noteIndex = 0;
		if (noteIndex > targetRow.getNotes().size()) noteIndex = targetRow.getNotes().size();

		Note blueprintNote = new Note(0, "quarter", NoteType.BLUEPRINT);
		targetRow.addNote(blueprintNote, noteIndex);
		repaint();
	}

	public void deleteMouseMoved(MouseEvent e) {
		Point mousePoint = applyCameraTransform(e.getPoint());

		for (SheetRow row : page.getRows()) {
			for (int i = 0; i < row.getNotes().size(); i++) {
				Note note = row.getNotes().get(i);
				Point position = row.getNotePosition(note, i, getWidth());

				// Hit box
				int startX = position.x - 15;
				int startY = position.y - 60;
				int endX = position.x + 35;
				int endY = position.y + 20;

				if (mousePoint.x >= startX && mousePoint.x <= endX &&
					mousePoint.y >= startY && mousePoint.y <= endY) {
					if (note.getNoteType() != NoteType.HOVERING) {
						note.setNoteType(NoteType.HOVERING);
					}
				} else {
					note.setNoteType(NoteType.NONE);
				}
			}
		}
		repaint();
	}

	public void addMouseClicked(MouseEvent e) {
		Point mousePoint = applyCameraTransform(e.getPoint());
		SheetRow targetRow = null;

		for (SheetRow row : page.getRows()) {
			if (mousePoint.y >= row.getStartY() && mousePoint.y < row.getStartY() + Constants.ROW_SPACING) {
				targetRow = row;
				break;
			}
		}

		if (targetRow == null) {
			return;
		}

		for (Note note : targetRow.getNotes()) {
			if (note.getNoteType() == NoteType.BLUEPRINT) {
				note.setNoteType(NoteType.NONE);
				repaint();
				return;
			}
		}
	}

	public void deleteMouseClicked(MouseEvent e) {
		Point mousePoint = applyCameraTransform(e.getPoint());

		for (SheetRow row : page.getRows()) {
			for (int i = 0; i < row.getNotes().size(); i++) {
				Note note = row.getNotes().get(i);
				Point position = row.getNotePosition(note, i, getWidth());

				// Hit box
				int startX = position.x - 15;
				int startY = position.y - 60;
				int endX = position.x + 35;
				int endY = position.y + 20;

				if (mousePoint.x >= startX && mousePoint.x <= endX &&
					mousePoint.y >= startY && mousePoint.y <= endY) {
					row.removeNote(note);
					repaint();
					return; // Remove only one note
				}
			}
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		camera.setPosition(camera.getX(), camera.getY());

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
			camera.move((-deltaX) / camera.getZoom(), (-deltaY) / camera.getZoom());
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
	public void mouseMoved(MouseEvent e) {
		switch (this.mode) {
			case DRAG: break;
			case ADD: addMouseMoved(e); break;
			case DELETE: deleteMouseMoved(e); break;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch (this.mode) {
			case DRAG: break;
			case ADD: addMouseClicked(e); break;
			case DELETE: deleteMouseClicked(e); break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
