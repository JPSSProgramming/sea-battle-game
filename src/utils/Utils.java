package src.utils;

public class Utils {
    public static final int BOARD_SIZE = 10;

    public static final int CELL_SIZE = 30;

    public static final int[] SHIP_SIZES = {1,1,1,1,2,2,2,3,3,4};

    public static boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < BOARD_SIZE && y < BOARD_SIZE;
    }
}