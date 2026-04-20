package org.GUI;

import javax.swing.*;

public class ControlPanel extends JPanel {
    final MainFrame frame;
    JButton createBtn, resetBtn, exitBtn, createDFSBtn;

    public ControlPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        createBtn = new JButton("Create");
        resetBtn = new JButton("Reset");
        exitBtn = new JButton("Exit");
        createDFSBtn = new JButton("Perfect Create");

        add(createBtn);
        add(resetBtn);
        add(exitBtn);
        add(createDFSBtn);

        exitBtn.addActionListener(e -> System.exit(0));
        resetBtn.addActionListener(e -> frame.getCanvas().resetGrid());
        createBtn.addActionListener(e -> frame.getCanvas().createMazeRandom());
        createDFSBtn.addActionListener(e -> frame.getCanvas().createMazeDFS());
    }
}