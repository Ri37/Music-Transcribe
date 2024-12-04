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

import javax.swing.JPanel;

import gui.Camera;
import gui.Constants;
import gui.Page;

public class SheetMusicCanvas extends JPanel implements MouseWheelListener, MouseMotionListener, MouseListener {
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

		camera.setPosition(camera.getX() + (mouseX - newMouseX), camera.getY() + (mouseY - newMouseY));

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
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
