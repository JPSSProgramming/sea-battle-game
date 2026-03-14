package src.model;

import java.util.ArrayList;
import java.util.List;

public record Ship(List<Cell> cells) {
    public Ship(List<Cell> cells) {
        this.cells = new ArrayList<>();
        setCells(cells);
    }

    public void setCells(List<Cell> newCells) {
        for (Cell c : this.cells) c.setShip(false);
        this.cells.clear();

        this.cells.addAll(newCells);
        for (Cell c : this.cells) c.setShip(true);
    }

    public boolean isSunk() {
        for (Cell c : cells) {
            if (!c.isHit()) return false;
        }
        return true;
    }
}