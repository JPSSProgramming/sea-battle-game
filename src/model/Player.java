package src.model;

import src.view.GameBoard;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final GameBoard board;
    private final List<Ship> ships;

    public Player(String name) {
        this.name = name;
        this.board = new GameBoard();
        this.ships = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public GameBoard getBoard() {
        return board;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void addShip(Ship ship) {
        ships.add(ship);
    }

    public boolean hasLost() {
        for (Ship s : ships) {
            if (!s.isSunk()) return false;
        }
        return true;
    }
}
