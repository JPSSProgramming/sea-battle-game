package org.sea.battle.game.model;

import org.sea.battle.game.utils.Theme;
import org.sea.battle.game.utils.Utils;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class Cell {
    private final int x, y;
    private boolean hasShip;
    private boolean isHit;
    private boolean sunk;
    private boolean hover;

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

    public boolean isSunk() { return sunk; }
    public void setSunk(boolean v) { this.sunk = v; }

    public void setHover(boolean v) { this.hover = v; }

    public void draw(Graphics g, boolean showShips) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int px = x * Utils.CELL_SIZE;
        int py = y * Utils.CELL_SIZE;
        int size = Utils.CELL_SIZE;

        Color water = hover ? Theme.WATER_HOVER : ((x + y) % 2 == 0 ? Theme.WATER_LIGHT : Theme.WATER);
        g2.setColor(water);
        g2.fillRect(px, py, size, size);

        if (hasShip && showShips) {
            g2.setColor(sunk ? Theme.SUNK_COLOR : Theme.SHIP_COLOR);
            g2.fill(new RoundRectangle2D.Double(px + 3, py + 3, size - 6, size - 6, 8, 8));
            g2.setColor(Theme.SHIP_BORDER);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Double(px + 3, py + 3, size - 6, size - 6, 8, 8));
        }

        if (isHit) {
            if (hasShip) {
                g2.setColor(Theme.HIT_COLOR);
                g2.setStroke(new BasicStroke(3f));
                int m = size / 4;
                g2.drawLine(px + m, py + m, px + size - m, py + size - m);
                g2.drawLine(px + size - m, py + m, px + m, py + size - m);
            } else {
                g2.setColor(Theme.MISS_COLOR);
                int d = size / 3;
                g2.fill(new Ellipse2D.Double(px + (size - d) / 2.0, py + (size - d) / 2.0, d, d));
            }
        }

        g2.setColor(new Color(10, 16, 28));
        g2.drawRect(px, py, size, size);
        g2.dispose();
    }
}
