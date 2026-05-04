package org.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ConfigPanel extends JPanel {
    final MainFrame frame;
    JLabel label;
    JSpinner rowsSpinner, colsSpinner;
    JButton drawBtn;
    JLabel delayLabel;
    JSlider delaySlider;

    public ConfigPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        label = new JLabel("Dimensions (Rows / Cols):");
        rowsSpinner = new JSpinner(new SpinnerNumberModel(20, 2, 100, 1));
        colsSpinner = new JSpinner(new SpinnerNumberModel(20, 2, 100, 1));
        drawBtn = new JButton("Draw Grid");

        delayLabel = new JLabel("Delay (ms):");
        delaySlider = new JSlider(0, 200, 20);
        delaySlider.setMajorTickSpacing(50);
        delaySlider.setMinorTickSpacing(10);
        delaySlider.setPaintTicks(true);
        delaySlider.setPaintLabels(true);

        add(label);
        add(rowsSpinner);
        add(colsSpinner);
        add(drawBtn);
        add(delayLabel);
        add(delaySlider);

        drawBtn.addActionListener(this::drawGrid);
    }

    private void drawGrid(ActionEvent e) {
        int rows = (int) rowsSpinner.getValue();
        int cols = (int) colsSpinner.getValue();
        frame.getCanvas().initGrid(rows, cols);
    }

    public int getDelay() {
        return delaySlider.getValue();
    }
}