package org.GUI;

import org.Logic.Cell;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class DrawingPanel extends JPanel {
    private final MainFrame frame;
    private int rows = 0, cols = 0;
    private Cell[][] grid;

    // Variabile pentru calculul coordonatelor la desenare și click
    private int cellSize;
    private int startX;
    private int startY;

    public DrawingPanel(MainFrame frame) {
        this.frame = frame;
        setBackground(Color.WHITE);
        initMouseListener();
    }

    private void initMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (grid == null) return;
                handleWallToggle(e.getX(), e.getY());
            }
        });
    }

    private void handleWallToggle(int mouseX, int mouseY) {
        // Determinăm indexul celulei pe baza coordonatelor mouse-ului
        int j = (mouseX - startX) / cellSize;
        int i = (mouseY - startY) / cellSize;

        if (i >= 0 && i < rows && j >= 0 && j < cols) {
            int localX = mouseX - (startX + j * cellSize);
            int localY = mouseY - (startY + i * cellSize);
            int threshold = 8; // Sensibilitatea click-ului lângă perete

            if (localY < threshold) toggleWall(i, j, 0); // Top
            else if (localX > cellSize - threshold) toggleWall(i, j, 1); // Right
            else if (localY > cellSize - threshold) toggleWall(i, j, 2); // Bottom
            else if (localX < threshold) toggleWall(i, j, 3); // Left

            repaint();
        }
    }

    private void toggleWall(int r, int c, int dir) {
        Cell cell = grid[r][c];
        if (dir == 0) { // Top
            cell.setTop(!cell.hasTop());
            if (r > 0) grid[r - 1][c].setBottom(cell.hasTop());
        } else if (dir == 1) { // Right
            cell.setRight(!cell.hasRight());
            if (c < cols - 1) grid[r][c + 1].setLeft(cell.hasRight());
        } else if (dir == 2) { // Bottom
            cell.setBottom(!cell.hasBottom());
            if (r < rows - 1) grid[r + 1][c].setTop(cell.hasBottom());
        } else if (dir == 3) { // Left
            cell.setLeft(!cell.hasLeft());
            if (c > 0) grid[r][c - 1].setRight(cell.hasLeft());
        }
    }

    public void initGrid(int rows, int columns) {
        this.rows = rows;
        this.cols = columns;
        grid = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j] = new Cell(i, j);
            }
        }
        repaint();
    }

    public void resetGrid() {
        if (grid == null) return;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j].resetWalls();
            }
        }
        repaint();
    }

    public void createMazeRandom() {
        if (grid == null) return;
        resetGrid();
        Random random = new Random();
        for (int nr = 0; nr < rows; nr++) {
            for (int nc = 0; nc < cols; nc++) {
                boolean wall = random.nextBoolean() ^ random.nextBoolean();
                grid[nr][nc].setTop(wall);
                if (nr > 0) grid[nr - 1][nc].setBottom(wall);

                wall = random.nextBoolean() ^ random.nextBoolean();
                grid[nr][nc].setLeft(wall);
                if (nc > 0) grid[nr][nc - 1].setRight(wall);
            }
        }
        repaint();
    }

    public void createMazeDFS() {
        if (grid == null) return;
        resetGrid();
        generateMazeDFS(0, 0);
        repaint();
    }

    private void generateMazeDFS(int r, int c) {
        grid[r][c].setVisited(true);
        int[] dr = {-1, 0, 1, 0};
        int[] dc = {0, 1, 0, -1};

        List<Integer> dirs = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(dirs);

        for (int dir : dirs) {
            int nr = r + dr[dir];
            int nc = c + dc[dir];

            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !grid[nr][nc].isVisited()) {
                if (dir == 0) { grid[r][c].setTop(false); grid[nr][nc].setBottom(false); }
                else if (dir == 1) { grid[r][c].setRight(false); grid[nr][nc].setLeft(false); }
                else if (dir == 2) { grid[r][c].setBottom(false); grid[nr][nc].setTop(false); }
                else if (dir == 3) { grid[r][c].setLeft(false); grid[nr][nc].setRight(false); }
                generateMazeDFS(nr, nc);
            }
        }
    }

    public boolean checkTraversable() {
        if (grid == null) return false;
        boolean[][] visited = new boolean[rows][cols];
        Queue<Cell> queue = new LinkedList<>();
        queue.add(grid[0][0]);
        visited[0][0] = true;

        while (!queue.isEmpty()) {
            Cell curr = queue.poll();
            int r = curr.getRow();
            int c = curr.getCol();

            if (r == rows - 1 && c == cols - 1) return true;

            // Verificăm vecinii și dacă peretele este spart
            if (!curr.hasTop() && r > 0 && !visited[r - 1][c]) { visited[r - 1][c] = true; queue.add(grid[r - 1][c]); }
            if (!curr.hasBottom() && r < rows - 1 && !visited[r + 1][c]) { visited[r + 1][c] = true; queue.add(grid[r + 1][c]); }
            if (!curr.hasLeft() && c > 0 && !visited[r][c - 1]) { visited[r][c - 1] = true; queue.add(grid[r][c - 1]); }
            if (!curr.hasRight() && c < cols - 1 && !visited[r][c + 1]) { visited[r][c + 1] = true; queue.add(grid[r][c + 1]); }
        }
        return false;
    }

    public void saveAsPNG() {
        if (grid == null) return;
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        this.paint(g2);
        g2.dispose();
        try {
            ImageIO.write(image, "png", new File("maze_export.png"));
            JOptionPane.showMessageDialog(this, "Maze saved to maze_export.png");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving image: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (grid == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));

        int padding = 20;
        cellSize = Math.min((getWidth() - 2 * padding) / cols, (getHeight() - 2 * padding) / rows);
        startX = (getWidth() - (cellSize * cols)) / 2;
        startY = (getHeight() - (cellSize * rows)) / 2;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = startX + j * cellSize;
                int y = startY + i * cellSize;
                Cell cell = grid[i][j];

                g2.setColor(new Color(245, 245, 245));
                g2.fillRect(x, y, cellSize, cellSize);

                g2.setColor(Color.BLACK);
                if (cell.hasTop())    g2.drawLine(x, y, x + cellSize, y);
                if (cell.hasBottom()) g2.drawLine(x, y + cellSize, x + cellSize, y + cellSize);
                if (cell.hasLeft())   g2.drawLine(x, y, x, y + cellSize);
                if (cell.hasRight())  g2.drawLine(x + cellSize, y, x + cellSize, y + cellSize);
            }
        }
    }

    public Cell[][] getGrid() { return grid; }
    public void setGrid(Cell[][] grid) {
        this.grid = grid;
        this.rows = grid.length;
        this.cols = grid[0].length;
        repaint();
    }
}