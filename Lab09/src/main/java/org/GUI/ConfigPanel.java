package org.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ConfigPanel extends JPanel {
    final MainFrame frame;
    JLabel label;
    JSpinner rowsSpinner, colsSpinner;
    JButton drawBtn;

    public ConfigPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        label = new JLabel("Dimensions (Rows / Cols):");
        rowsSpinner = new JSpinner(new SpinnerNumberModel(10, 2, 100, 1));
        colsSpinner = new JSpinner(new SpinnerNumberModel(10, 2, 100, 1));
        drawBtn = new JButton("Draw Grid");

        add(label);
        add(rowsSpinner);
        add(colsSpinner);
        add(drawBtn);

        drawBtn.addActionListener(this::drawGrid);
    }

    private void drawGrid(ActionEvent e) {
        int rows = (int) rowsSpinner.getValue();
        int cols = (int) colsSpinner.getValue();
        frame.getCanvas().initGrid(rows, cols);
    }
}