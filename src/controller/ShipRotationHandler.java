//package src.controller;
//
//import src.model.Cell;
//import src.model.Ship;
//import src.model.ShipPlacementValidator;
//import src.utils.Utils;
//import src.view.GameBoard;
//
//import java.util.ArrayList; import java.util.List;
//
//public class ShipRotationHandler { private final GameBoard board;
//
//    public ShipRotationHandler(GameBoard board) {
//        this.board = board;
//    }
//
//    public boolean rotate(Ship ship) {
//        List<Cell> old = new ArrayList<>(ship.cells());
//        if (old.isEmpty()) return false;
//
//        boolean horizontal = true;
//        int y0 = old.get(0).getY();
//        for (Cell c : old) if (c.getY() != y0) { horizontal = false; break; }
//
//        int anchorX = old.get(0).getX();
//        int anchorY = old.get(0).getY();
//        int len = old.size();
//
//        for (Cell c : old) board.getCell(c.getX(), c.getY()).setShip(false);
//
//        List<Cell> cand = new ArrayList<>();
//        if (horizontal) {
//            for (int i = 0; i < len; i++) {
//                int nx = anchorX;
//                int ny = anchorY + i;
//                if (!Utils.inBounds(nx, ny)) {
//                    for (Cell c : old) board.getCell(c.getX(), c.getY()).setShip(true);
//                    return false;
//                }
//                cand.add(board.getCell(nx, ny));
//            }
//        } else {
//            for (int i = 0; i < len; i++) {
//                int nx = anchorX + i;
//                int ny = anchorY;
//                if (!Utils.inBounds(nx, ny)) {
//                    for (Cell c : old) board.getCell(c.getX(), c.getY()).setShip(true);
//                    return false;
//                }
//                cand.add(board.getCell(nx, ny));
//            }
//        }
//
//        boolean ok = ShipPlacementValidator.canPlaceShip(board, cand);
//        if (ok) {
//            ship.setCells(cand);
//        } else {
//            for (Cell c : old) board.getCell(c.getX(), c.getY()).setShip(true);
//        }
//
//        board.repaint();
//        return ok;
//    }
//}
