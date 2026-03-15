package src.model;

import src.utils.Utils;

import java.awt.*;

public class Cell {
    private final int x, y;
    private boolean hasShip;
    private boolean isHit;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.hasShip = false;
        this.isHit = false;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean hasShip() { return hasShip; }
    public void setShip(boolean v) { this.hasShip = v; }

    public boolean isHit() { return isHit; }
    public void hit() { this.isHit = true; }

    public void draw(Graphics g, boolean showShips) {
        int px = x * Utils.CELL_SIZE;
        int py = y * Utils.CELL_SIZE;

        g.setColor(new Color(0, 85, 255));
        g.fillRect(px, py, Utils.CELL_SIZE, Utils.CELL_SIZE);

        if (hasShip && showShips) {
            g.setColor(new Color(0, 0, 0));
            g.fillRect(px + 2, py + 2, Utils.CELL_SIZE - 4, Utils.CELL_SIZE - 4);
        }

        if (isHit) {
            g.setColor(hasShip ? Color.RED : Color.GREEN);
            g.fillRect(px + 4, py + 4, Utils.CELL_SIZE - 8, Utils.CELL_SIZE - 8);
        }

        g.setColor(Color.BLACK);
        g.drawRect(px, py, Utils.CELL_SIZE, Utils.CELL_SIZE);
    }
}