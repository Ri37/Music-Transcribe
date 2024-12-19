package gui.components;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import gui.Constants;
import gui.Constants.CanvasMode;

public class ControlPanel extends JPanel {
    private final SheetMusicCanvas canvas;
    private final JFrame frame;
    private final List<JButton> buttons = new ArrayList<>();
    private final ButtonGroup toggleGroup = new ButtonGroup();

    public ControlPanel(SheetMusicCanvas canvas, JFrame frame) {
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

        add(Box.createVerticalGlue());

        addToggleButton(toggleGroup, "View", e -> canvas.setCanvasMode(CanvasMode.DRAG), true);
        addToggleButton(toggleGroup, "Add Note", e -> canvas.setCanvasMode(CanvasMode.ADD), false);
        addToggleButton(toggleGroup, "Remove Note", e -> canvas.setCanvasMode(CanvasMode.DELETE), false);

        add(Box.createRigidArea(new Dimension(0, 10)));

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

        toggleGroup.getElements().asIterator().forEachRemaining(abstractButton -> {
            if (abstractButton instanceof JToggleButton toggleButton) {
                toggleButton.setMaximumSize(new Dimension(newWidth - 20, 40));
                toggleButton.setPreferredSize(new Dimension(newWidth - 20, 40));
            }
        });

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

    private void addToggleButton(ButtonGroup group, String label, ActionListener action, boolean activate) {
        JToggleButton toggleButton = new JToggleButton(label);
        toggleButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        toggleButton.addActionListener(action);

        int initialWidth = (frame.getWidth() > Constants.MENU_BAR_THRESHOLD)
                ? (int) (frame.getWidth() * Constants.MENU_BAR_PERCENTAGE)
                : (int) (Constants.MENU_BAR_THRESHOLD * Constants.MENU_BAR_PERCENTAGE);

        toggleButton.setMaximumSize(new Dimension(initialWidth - 20, 40));
        toggleButton.setPreferredSize(new Dimension(initialWidth - 20, 40));

        group.add(toggleButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(toggleButton);

        if (activate) {
            toggleButton.doClick();
        }
    }
}
