package org;

import org.GUI.MainFrame;

import javax.swing.*;

public class MazeGenerator {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
