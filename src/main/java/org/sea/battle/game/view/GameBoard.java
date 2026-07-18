package org.sea.battle.game.view;

import org.sea.battle.game.model.Cell;
import org.sea.battle.game.model.Ship;
import org.sea.battle.game.utils.Theme;
import org.sea.battle.game.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameBoard extends JPanel {
    private final Cell[][] cells;
    private final List<Ship> ships;
    private boolean showShips = true;

    private int hoverX = -1, hoverY = -1;

    public List<Cell> previewCells = null;

    public GameBoard() {
        this.cells = new Cell[Utils.BOARD_SIZE][Utils.BOARD_SIZE];
        this.ships = new ArrayList<>();

        for (int y = 0; y < Utils.BOARD_SIZE; y++) {
            for (int x = 0; x < Utils.BOARD_SIZE; x++) {
                cells[x][y] = new Cell(x, y);
            }
        }

        setBackground(Theme.BG_PANEL);
        setPreferredSize(new Dimension(Utils.BOARD_SIZE * Utils.CELL_SIZE, Utils.BOARD_SIZE * Utils.CELL_SIZE));
    }

    public Cell getCell(int x, int y) {
        if (!Utils.inBounds(x, y)) return null;
        return cells[x][y];
    }

    public boolean addShip(Ship ship) {
        if (ship == null || ship.cells().isEmpty()) return false;
        for (Cell c : ship.cells()) {
            if (c == null || !Utils.inBounds(c.getX(), c.getY())) return false;
        }
        if (!ships.contains(ship)) ships.add(ship);
        repaint();
        return true;
    }

    public void removeShip(Ship ship) {
        if (ship == null) return;
        for (Cell c : ship.cells()) {
            if (Utils.inBounds(c.getX(), c.getY())) {
                getCell(c.getX(), c.getY()).setShip(false);
            }
        }
        ships.remove(ship);
        repaint();
    }

    public List<Ship> getShips() { return ships; }

    public void setShowShips(boolean v) { this.showShips = v; repaint(); }

    public void setPreviewCells(List<Cell> preview) { this.previewCells = preview; repaint(); }
    public void clearPreview() { this.previewCells = null; repaint(); }

    public void setHoverCell(int x, int y) {
        if (hoverX == x && hoverY == y) return;
        hoverX = x;
        hoverY = y;
        repaint();
    }

    public void clearHover() { setHoverCell(-1, -1); }

    public Ship findShipAt(int x, int y) {
        for (Ship s : ships) {
            for (Cell c : s.cells()) {
                if (c.getX() == x && c.getY() == y) return s;
            }
        }
        return null;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int y = 0; y < Utils.BOARD_SIZE; y++) {
            for (int x = 0; x < Utils.BOARD_SIZE; x++) {
                cells[x][y].setHover(x == hoverX && y == hoverY);
                cells[x][y].draw(g, showShips);
            }
        }

        if (previewCells != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f));
            boolean valid = org.sea.battle.game.model.ShipPlacementValidator.canPlaceShip(this, previewCells);
            for (Cell c : previewCells) {
                if (c == null) continue;
                int px = c.getX() * Utils.CELL_SIZE;
                int py = c.getY() * Utils.CELL_SIZE;
                g2.setColor(valid ? Theme.ACCENT : Theme.HIT_COLOR);
                g2.fillRoundRect(px + 2, py + 2, Utils.CELL_SIZE - 4, Utils.CELL_SIZE - 4, 8, 8);
            }
            g2.dispose();
        }
    }

    public boolean shoot(int x, int y) {
        Cell c = getCell(x, y);
        if (c == null) return false;
        c.hit();
        return c.hasShip();
    }
}
