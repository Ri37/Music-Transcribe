package main;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gui.components.MenuBarPanel;
import gui.components.SheetMusicCanvas;

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