package gui;

public class Constants {
    public static enum CanvasMode {
        DRAG,
        ADD,
        DELETE
    }

    public static enum NoteType {
        NONE,
        HOVERING,
        BLUEPRINT
    }

    public static final int ROW_STARTER_NUM = 8;
    public static final int ROW_WIDTH = 800;
    public static final int ROW_SPACING = 100;
    public static final int LINE_SPACING = 10;

    public static final int MENU_BAR_WIDTH = 150;
    public static final int MENU_BAR_THRESHOLD = 600;
    public static final double MENU_BAR_PERCENTAGE = 0.2;

    public static final double ZOOM_MIN = 0.5;
    public static final double ZOOM_MAX = 3.0;

    public static final int NOTES_PER_ROW = 16;
    public static final int[] HALF_NOTE_REMAINDERS = {1, 3, 6, 8, 10};
}
