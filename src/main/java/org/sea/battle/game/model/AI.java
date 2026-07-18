package org.sea.battle.game.model;

import org.sea.battle.game.utils.Utils;
import org.sea.battle.game.view.GameBoard;

import java.util.*;

public class AI extends Player {
    private final Random random = new Random();
    private final Difficulty difficulty;

    private final Set<String> tried = new HashSet<>();
    private final Deque<int[]> targetQueue = new ArrayDeque<>();
    private final List<int[]> currentHits = new ArrayList<>();
    private int[] huntDir = null;

    public AI(String name, Difficulty difficulty) {
        super(name);
        this.difficulty = difficulty;
    }

    public Difficulty getDifficulty() { return difficulty; }

    private static String key(int x, int y) { return x + "," + y; }

    public int[] pickTarget(GameBoard enemyBoard) {
        if (difficulty != Difficulty.EASY) {
            while (!targetQueue.isEmpty()) {
                int[] t = targetQueue.poll();
                if (!Utils.inBounds(t[0], t[1]) || tried.contains(key(t[0], t[1]))) {
                    if (difficulty == Difficulty.HARD) flipDirectionIfHunting();
                    continue;
                }
                return t;
            }
        }
        return randomUntried(difficulty == Difficulty.EASY);
    }

    private int[] randomUntried(boolean ignoreParity) {
        List<int[]> candidates = new ArrayList<>();
        for (int x = 0; x < Utils.BOARD_SIZE; x++) {
            for (int y = 0; y < Utils.BOARD_SIZE; y++) {
                if (tried.contains(key(x, y))) continue;
                if (!ignoreParity && ((x + y) & 1) != 0) continue;
                candidates.add(new int[]{x, y});
            }
        }
        if (candidates.isEmpty()) {
            for (int x = 0; x < Utils.BOARD_SIZE; x++) {
                for (int y = 0; y < Utils.BOARD_SIZE; y++) {
                    if (!tried.contains(key(x, y))) candidates.add(new int[]{x, y});
                }
            }
        }
        if (candidates.isEmpty()) return new int[]{0, 0};
        return candidates.get(random.nextInt(candidates.size()));
    }

    private void flipDirectionIfHunting() {
        if (huntDir == null || currentHits.isEmpty()) return;
        int[] anchor = currentHits.get(0);
        huntDir = new int[]{-huntDir[0], -huntDir[1]};
        targetQueue.clear();
        targetQueue.offer(new int[]{anchor[0] + huntDir[0], anchor[1] + huntDir[1]});
    }

    public void feedback(int x, int y, GameLogic.ShotResult result) {
        tried.add(key(x, y));

        if (result == GameLogic.ShotResult.MISS) {
            if (difficulty == Difficulty.HARD) flipDirectionIfHunting();
            return;
        }

        currentHits.add(new int[]{x, y});

        if (result == GameLogic.ShotResult.SUNK) {
            currentHits.clear();
            huntDir = null;
            targetQueue.clear();
            return;
        }

        if (difficulty == Difficulty.HARD) {
            if (currentHits.size() >= 2 && huntDir == null) {
                int[] a = currentHits.get(0);
                int[] b = currentHits.get(1);
                huntDir = new int[]{b[0] - a[0], b[1] - a[1]};
            }
            if (huntDir != null) {
                int[] last = currentHits.get(currentHits.size() - 1);
                targetQueue.clear();
                targetQueue.offer(new int[]{last[0] + huntDir[0], last[1] + huntDir[1]});
            } else {
                addNeighbors(x, y);
            }
        } else if (difficulty == Difficulty.MEDIUM) {
            addNeighbors(x, y);
        }
    }

    private void addNeighbors(int x, int y) {
        int[][] d = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] v : d) {
            int nx = x + v[0], ny = y + v[1];
            if (Utils.inBounds(nx, ny) && !tried.contains(key(nx, ny))) {
                targetQueue.offer(new int[]{nx, ny});
            }
        }
    }

    public void autoPlaceShips() {
        for (int size : Utils.SHIP_SIZES) {
            boolean placed = false;
            int attempts = 0;
            while (!placed && attempts < 10000) {
                attempts++;
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
