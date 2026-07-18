package org.sea.battle.game.model;

import org.sea.battle.game.utils.ProgressStore;
import org.sea.battle.game.utils.ShipSkin;
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
    private boolean flash;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.hasShip = false;
        this.isHit = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hasShip() {
        return hasShip;
    }

    public void setShip(boolean v) {
        this.hasShip = v;
    }

    public boolean isHit() {
        return isHit;
    }

    public void hit() {
        this.isHit = true;
    }

    public boolean isSunk() {
        return sunk;
    }

    public void setSunk(boolean v) {
        this.sunk = v;
    }

    public void setHover(boolean v) {
        this.hover = v;
    }

    public void setFlash(boolean v) {
        this.flash = v;
    }

    public void draw(Graphics g, boolean showShips) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int px = x * Utils.CELL_SIZE;
        int py = y * Utils.CELL_SIZE;
        int size = Utils.CELL_SIZE;

        Color water = hover ? Theme.WATER_HOVER : ((x + y) % 2 == 0 ? Theme.WATER_LIGHT : Theme.WATER);
        g2.setColor(water);
        g2.fillRect(px, py, size, size);

        if (hasShip && showShips && !sunk) {
            ShipSkin skin = ShipSkin.byId(ProgressStore.get().getSelectedSkin());
            g2.setColor(skin.fill);
            g2.fill(new RoundRectangle2D.Double(px + 3, py + 3, size - 6, size - 6, 8, 8));
            g2.setColor(skin.border);
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(new RoundRectangle2D.Double(px + 3, py + 3, size - 6, size - 6, 8, 8));
        }

        if (sunk) {
            g2.setColor(flash ? new Color(255, 200, 60) : new Color(18, 14, 14));
            g2.fillRect(px + 1, py + 1, size - 2, size - 2);

            g2.setColor(flash ? Color.BLACK : Theme.HIT_COLOR);
            g2.setStroke(new BasicStroke(4f));
            int m = size / 5;
            g2.drawLine(px + m, py + m, px + size - m, py + size - m);
            g2.drawLine(px + size - m, py + m, px + m, py + size - m);

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(3f));
            g2.drawRect(px + 1, py + 1, size - 3, size - 3);
        } else if (isHit) {
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