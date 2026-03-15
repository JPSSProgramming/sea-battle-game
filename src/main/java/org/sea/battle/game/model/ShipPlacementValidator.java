package org.sea.battle.game.model;

import org.sea.battle.game.utils.Utils;
import org.sea.battle.game.view.GameBoard;

import java.util.List;

public class ShipPlacementValidator {

    public static boolean canPlaceShip(GameBoard board, List<Cell> candidateCells) {
        for (Cell c : candidateCells) {
            if (!Utils.inBounds(c.getX(), c.getY())) {
                return false;
            }

            if (c.hasShip()) {
                return false;
            }

            if (hasNeighborShip(board, c)) {
                return false;
            }
        }
        return true;
    }

    private static boolean hasNeighborShip(GameBoard board, Cell cell) {
        int[][] dirs = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1},  {1, 0},  {1, 1}
        };

        for (int[] d : dirs) {
            int nx = cell.getX() + d[0];
            int ny = cell.getY() + d[1];
            if (Utils.inBounds(nx, ny)) {
                if (board.getCell(nx, ny).hasShip()) {
                    return true;
                }
            }
        }
        return false;
    }
}
