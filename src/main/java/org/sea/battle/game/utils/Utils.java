package org.sea.battle.game.utils;

public class Utils {
    public static final int BOARD_SIZE = 10;

    public static final int CELL_SIZE = 36;

    public static final int[] SHIP_SIZES = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};

    public static String shipTypeName(int size) {
        return switch (size) {
            case 4 -> "Лінкор";
            case 3 -> "Крейсер";
            case 2 -> "Есмінець";
            default -> "Катер";
        };
    }

    public static boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < BOARD_SIZE && y < BOARD_SIZE;
    }
}
