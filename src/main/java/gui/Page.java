package gui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Page {
    private final List<SheetRow> rows;

    public Page(List<SheetRow> initialRows) {
        rows = initialRows != null ? initialRows : new ArrayList<>();

        while (rows.size() < Constants.ROW_STARTER_NUM) {
            addRow();
        }
    }

    public Page() {
        rows = new ArrayList<>();

        for (int i = 0; i < Constants.ROW_STARTER_NUM; i++) {
            addRow();
        }
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

    public static Page createBociBociPage() {
        Page page = new Page();

        /*
        SheetRow firstRow = page.rows.get(0);
        SheetRow secondRow = page.rows.get(1);

        // Elso utem: Boci boci tarka
        firstRow.addNote(new Note(0, "8th"));  // Bo-
        firstRow.addNote(new Note(4, "8th"));  // ci
        firstRow.addNote(new Note(0, "8th"));  // bo-
        firstRow.addNote(new Note(4, "8th"));  // ci
        firstRow.addNote(new Note(7, "quarter")); // tar-
        firstRow.addNote(new Note(7, "quarter")); // ka
    
        // Masodik utem: Se füle se farka
        firstRow.addNote(new Note(0, "8th"));  // Se
        firstRow.addNote(new Note(4, "8th"));  // fü-
        firstRow.addNote(new Note(0, "8th"));  // le
        firstRow.addNote(new Note(4, "8th"));  // se
        firstRow.addNote(new Note(7, "quarter")); // far-
        firstRow.addNote(new Note(7, "quarter")); // ka
    
        // Harmadik utem: Oda megyünk lakni
        secondRow.addNote(new Note(12, "8th"));  // O-
        secondRow.addNote(new Note(11, "8th"));  // da
        secondRow.addNote(new Note(9, "8th"));  // me-
        secondRow.addNote(new Note(7, "8th"));  // gyünk
        secondRow.addNote(new Note(5, "quarter")); // lak-
        secondRow.addNote(new Note(9, "quarter")); // ni
    
        // Negyedik utem: Ahol tejet kapni
        secondRow.addNote(new Note(7, "8th"));  // A-
        secondRow.addNote(new Note(5, "8th"));  // hol
        secondRow.addNote(new Note(4, "8th"));  // te-
        secondRow.addNote(new Note(2, "8th"));  // jet
        secondRow.addNote(new Note(0, "quarter")); // kap-
        secondRow.addNote(new Note(0, "quarter")); // ni
        */
        return page;
    }      
}
