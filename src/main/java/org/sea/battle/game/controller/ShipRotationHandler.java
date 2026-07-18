package org.sea.battle.game.controller;

import org.sea.battle.game.model.Cell;
import org.sea.battle.game.model.Ship;
import org.sea.battle.game.model.ShipPlacementValidator;
import org.sea.battle.game.utils.Utils;
import org.sea.battle.game.view.GameBoard;

import java.util.ArrayList;
import java.util.List;

public class ShipRotationHandler {
    private final GameBoard board;

    public ShipRotationHandler(GameBoard board) {
        this.board = board;
    }

    public boolean rotate(Ship ship) {
        List<Cell> old = new ArrayList<>(ship.cells());
        if (old.isEmpty()) return false;

        boolean horizontal = true;
        int y0 = old.get(0).getY();
        for (Cell c : old) if (c.getY() != y0) { horizontal = false; break; }

        int anchorX = old.get(0).getX();
        int anchorY = old.get(0).getY();
        int len = old.size();

        for (Cell c : old) board.getCell(c.getX(), c.getY()).setShip(false);

        List<Cell> cand = new ArrayList<>();
        boolean outOfBounds = false;
        for (int i = 0; i < len; i++) {
            int nx = horizontal ? anchorX : anchorX + i;
            int ny = horizontal ? anchorY + i : anchorY;
            if (!Utils.inBounds(nx, ny)) { outOfBounds = true; break; }
            cand.add(board.getCell(nx, ny));
        }

        if (outOfBounds || !ShipPlacementValidator.canPlaceShip(board, cand)) {
            for (Cell c : old) board.getCell(c.getX(), c.getY()).setShip(true);
            board.repaint();
            return false;
        }

        ship.setCells(cand);
        board.repaint();
        return true;
    }
}
