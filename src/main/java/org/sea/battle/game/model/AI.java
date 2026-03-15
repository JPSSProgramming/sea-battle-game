package org.sea.battle.game.model;

import org.sea.battle.game.utils.Utils;
import org.sea.battle.game.view.GameBoard;

import java.util.*;

public class AI extends Player {
    private final Random random = new Random();
    private final Set<String> tried = new HashSet<>();
    private final Queue<int[]> targets = new ArrayDeque<>();

    public AI(String name) {
        super(name);
    }

    public int[] pickTarget(GameBoard enemyBoard) {
        int x, y;

        while (!targets.isEmpty()) {
            int[] t = targets.poll();
            x = t[0]; y = t[1];
            if (Utils.inBounds(x, y) && !tried.contains(x + "," + y)) {
                return new int[]{x, y};
            }
        }

        do {
            x = random.nextInt(Utils.BOARD_SIZE);
            y = random.nextInt(Utils.BOARD_SIZE);
        } while (tried.contains(x + "," + y) || ((x + y) & 1) != 0);

        return new int[]{x, y};
    }

    public void feedback(int x, int y, boolean hit) {
        tried.add(x + "," + y);
        if (hit) addNeighbors(x, y);
    }

    private void addNeighbors(int x, int y) {
        int[][] d = {{1,0},{-1,0},{0,1},{0,-1}};
        for (int[] v : d) {
            int nx = x + v[0], ny = y + v[1];
            if (Utils.inBounds(nx, ny) && !tried.contains(nx + "," + ny)) {
                targets.offer(new int[]{nx, ny});
            }
        }
    }

    public void autoPlaceShips() {
        for (int size : Utils.SHIP_SIZES) {
            boolean placed = false;
            while (!placed) {
                int x = random.nextInt(Utils.BOARD_SIZE);
                int y = random.nextInt(Utils.BOARD_SIZE);
                boolean horizontal = random.nextBoolean();

                List<Cell> cand = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    int nx = horizontal ? x + i : x;
                    int ny = horizontal ? y : y + i;
                    if (!Utils.inBounds(nx, ny)) { cand.clear(); break; }
                    cand.add(getBoard().getCell(nx, ny));
                }
                if (!cand.isEmpty() && ShipPlacementValidator.canPlaceShip(getBoard(), cand)) {
                    Ship s = new Ship(cand);
                    addShip(s);
                    getBoard().addShip(s);
                    placed = true;
                }
            }
        }
    }
}