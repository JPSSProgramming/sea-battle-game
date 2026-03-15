package src.controller;

import src.model.Cell;
import src.model.Ship;
import src.model.ShipPlacementValidator;
import src.utils.Utils;
import src.view.GameBoard;


import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class DragAndDropManager extends MouseAdapter {

    private Ship selectedShip;
    private java.util.List<Cell> backupCells;
    private int anchorX;
    private int anchorY;
    private java.util.List<Cell> previewCells;
    private final GameBoard board; private final List<Ship> ships;
    public DragAndDropManager(GameBoard board, List<Ship> ships) {
        this.board = board;
        this.ships = ships;
        board.addMouseListener(this);
        board.addMouseMotionListener(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int gx = e.getX() / Utils.CELL_SIZE;
        int gy = e.getY() / Utils.CELL_SIZE;
        Ship s = board.findShipAt(gx, gy);
        if (s == null) return;
        selectedShip = s;
        backupCells = new ArrayList<>(s.cells());
        anchorX = backupCells.get(0).getX();
        anchorY = backupCells.get(0).getY();

        for (Cell c : backupCells) board.getCell(c.getX(), c.getY()).setShip(false);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (selectedShip == null) return;
        int gx = e.getX() / Utils.CELL_SIZE;
        int gy = e.getY() / Utils.CELL_SIZE;

        int dx = gx - anchorX;
        int dy = gy - anchorY;

        List<Cell> newCells = new ArrayList<>();
        List<Cell> old = backupCells;
        boolean horizontal = true;
        int y0 = old.get(0).getY();
        for (Cell c : old) if (c.getY() != y0) { horizontal = false; break; }

        for (int i = 0; i < old.size(); i++) {
            int nx = (horizontal ? old.get(0).getX() + i : old.get(0).getX()) + dx;
            int ny = (horizontal ? old.get(0).getY() : old.get(0).getY() + i) + dy;
            if (!Utils.inBounds(nx, ny)) {
                board.setPreviewCells(null);
                return;
            }
            newCells.add(board.getCell(nx, ny));
        }

        board.setPreviewCells(newCells);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (selectedShip == null) return;
        List<Cell> newCells = board.previewCells;
        if (newCells != null && ShipPlacementValidator.canPlaceShip(board, newCells)) {
            selectedShip.setCells(newCells);
            board.getShips().add(selectedShip);
        } else {
            selectedShip.setCells(backupCells);
        }
        board.clearPreview();
        selectedShip = null;
        backupCells = null;
    }

}
