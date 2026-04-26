package org.Logic;
import java.io.Serializable;

public class Cell implements Serializable {
    private static final long serialVersionUID = 1L; // Bună practică pentru serializare
    private final int row, col;
    private boolean top = true, right = true, bottom = true, left = true;
    private boolean visited = false;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public boolean hasTop() { return top; }
    public boolean hasRight() { return right; }
    public boolean hasBottom() { return bottom; }
    public boolean hasLeft() { return left; }
    public boolean isVisited() { return visited; }

    public void setTop(boolean top) { this.top = top; }
    public void setRight(boolean right) { this.right = right; }
    public void setBottom(boolean bottom) { this.bottom = bottom; }
    public void setLeft(boolean left) { this.left = left; }
    public void setVisited(boolean visited) { this.visited = visited; }

    public void resetWalls() {
        this.top = this.right = this.bottom = this.left = true;
        this.visited = false;
    }
}