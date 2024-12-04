package gui;

public class Camera {
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