package org.GUI;

import org.Logic.Cell;
import javax.swing.*;
import java.io.*;

public class ControlPanel extends JPanel {
    private final MainFrame frame;
    private JButton createBtn, createDFSBtn, validateBtn, saveBtn, loadBtn, exportBtn, resetBtn, exitBtn;

    public ControlPanel(MainFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        createBtn = new JButton("Random");
        createDFSBtn = new JButton("Perfect");
        validateBtn = new JButton("Validate");
        saveBtn = new JButton("Save");
        loadBtn = new JButton("Load");
        exportBtn = new JButton("PNG");
        resetBtn = new JButton("Reset");
        exitBtn = new JButton("Exit");

        // Adăugare componente
        add(createBtn);
        add(createDFSBtn);
        add(validateBtn);
        add(saveBtn);
        add(loadBtn);
        add(exportBtn);
        add(resetBtn);
        add(exitBtn);

        // Mapare acțiuni
        exitBtn.addActionListener(e -> System.exit(0));
        resetBtn.addActionListener(e -> frame.getCanvas().resetGrid());
        createBtn.addActionListener(e -> frame.getCanvas().createMazeRandom());
        createDFSBtn.addActionListener(e -> frame.getCanvas().createMazeDFS());
        exportBtn.addActionListener(e -> frame.getCanvas().saveAsPNG());

        validateBtn.addActionListener(e -> {
            boolean pathExists = frame.getCanvas().checkTraversable();
            if (pathExists) {
                JOptionPane.showMessageDialog(frame, "Maze is traversable from Start to End!");
            } else {
                JOptionPane.showMessageDialog(frame, "No path found between Start and End.");
            }
        });

        saveBtn.addActionListener(e -> serializeMaze());
        loadBtn.addActionListener(e -> deserializeMaze());
    }

    private void serializeMaze() {
        Cell[][] grid = frame.getCanvas().getGrid();
        if (grid == null) return;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("maze.ser"))) {
            oos.writeObject(grid);
            JOptionPane.showMessageDialog(frame, "Maze state saved to maze.ser");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Save error: " + ex.getMessage());
        }
    }

    private void deserializeMaze() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("maze.ser"))) {
            Cell[][] loadedGrid = (Cell[][]) ois.readObject();
            frame.getCanvas().setGrid(loadedGrid);
            JOptionPane.showMessageDialog(frame, "Maze state loaded from maze.ser");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Load error: " + ex.getMessage());
        }
    }
}