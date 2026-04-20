package org.GUI;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private ConfigPanel configPanel;
    private ControlPanel controlPanel;
    private DrawingPanel canvas;

    public MainFrame() {
        super("Maze Generator");
        init();
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        canvas = new DrawingPanel(this);
        configPanel = new ConfigPanel(this);
        controlPanel = new ControlPanel(this);

        add(configPanel, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setSize(800, 600);
        setLocationRelativeTo(null);
    }


    public DrawingPanel getCanvas() {
        return canvas;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public ConfigPanel getConfigPanel() {
        return configPanel;
    }
}