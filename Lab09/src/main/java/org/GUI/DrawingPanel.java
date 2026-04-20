package org.GUI;

import org.Logic.Cell;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DrawingPanel extends JPanel {
    private final MainFrame frame;
    private int rows = 0, cols = 0;
    private Cell[][] grid;

    public DrawingPanel(MainFrame frame) {
        this.frame = frame;
        setBackground(Color.WHITE);
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
        generateMazeRandom();
        repaint();
    }

    public void createMazeDFS(){
        if (grid == null) return;
        resetGrid();
        generateMazeDFS(0,0);
        repaint();
    }

    private void generateMazeRandom() {
        Random random = new Random();
        boolean tmp = false;


        for(int nr = 0; nr < rows; nr++){
            for(int nc = 0; nc <  cols; nc++){
                //Am vrut sa fie weighted 25:75. De aici abominatia asta.
                tmp = random.nextBoolean() ^ random.nextBoolean();
                grid[nr][nc].setTop(tmp);
                if(nr-1 >= 0)
                grid[nr-1][nc].setBottom(tmp);

                tmp = random.nextBoolean() ^ random.nextBoolean();
                grid[nr][nc].setBottom(tmp);
                if(nr+1 < rows)
                grid[nr+1][nc].setTop(tmp);

                tmp = random.nextBoolean() ^ random.nextBoolean();
                grid[nr][nc].setLeft(tmp);
                if(nc-1 >= 0)
                grid[nr][nc-1].setRight(tmp);

                tmp = random.nextBoolean() ^ random.nextBoolean();
                grid[nr][nc].setRight(tmp);
                if(nc+1 < cols)
                grid[nr][nc+1].setLeft(tmp);
            }
        }
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (grid == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));

        int padding = 20;
        int cellSize = Math.min((getWidth() - 2 * padding) / cols, (getHeight() - 2 * padding) / rows);
        int startX = (getWidth() - (cellSize * cols)) / 2;
        int startY = (getHeight() - (cellSize * rows)) / 2;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = startX + j * cellSize;
                int y = startY + i * cellSize;
                Cell cell = grid[i][j];

                g2.setColor(new Color(230, 240, 255));
                g2.fillRect(x, y, cellSize, cellSize);

                g2.setColor(Color.BLACK);
                if (cell.hasTop())    g2.drawLine(x, y, x + cellSize, y);
                if (cell.hasBottom()) g2.drawLine(x, y + cellSize, x + cellSize, y + cellSize);
                if (cell.hasLeft())   g2.drawLine(x, y, x, y + cellSize);
                if (cell.hasRight())  g2.drawLine(x + cellSize, y, x + cellSize, y + cellSize);
            }
        }
    }
}