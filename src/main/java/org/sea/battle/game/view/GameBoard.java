package org.sea.battle.game.view;

import org.sea.battle.game.model.Cell;
import org.sea.battle.game.model.Ship;
import org.sea.battle.game.utils.Utils;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameBoard extends JPanel {
    private  Cell[][] cells;
    private  List<Ship> ships;
    private boolean showShips = true;


    public List<Cell> previewCells = null;

    public GameBoard() {

        this.cells = new Cell[Utils.BOARD_SIZE][Utils.BOARD_SIZE];
        this.ships = new ArrayList<>();

        for (int y = 0; y < Utils.BOARD_SIZE; y++) {
            for (int x = 0; x < Utils.BOARD_SIZE; x++) {
                cells[x][y] = new Cell(x, y);
            }
        }

        setPreferredSize(new Dimension(Utils.BOARD_SIZE * Utils.CELL_SIZE, Utils.BOARD_SIZE * Utils.CELL_SIZE));
    }

    public Cell getCell(int x, int y) {
        if (!Utils.inBounds(x, y)) return null;
        return cells[x][y];
    }

    public boolean addShip(Ship ship) {
        if (ship == null) return false;
        for (Cell c : ship.cells()) {
            if (c == null) return false;
            if (!Utils.inBounds(c.getX(), c.getY())) return false;
            if (getCell(c.getX(), c.getY()).hasShip()) return false;
        }
        for (Cell c : ship.cells()) {
            getCell(c.getX(), c.getY()).setShip(true);
        }
        ships.add(ship);
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
                cells[x][y].draw(g, showShips);
            }
        }

        if (previewCells != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
            for (Cell c : previewCells) {
                if (c == null) continue;
                int px = c.getX() * Utils.CELL_SIZE;
                int py = c.getY() * Utils.CELL_SIZE;
                g2.setColor(new Color(180, 255, 180));
                g2.fillRect(px + 2, py + 2, Utils.CELL_SIZE - 4, Utils.CELL_SIZE - 4);
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