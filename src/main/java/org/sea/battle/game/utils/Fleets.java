package org.sea.battle.game.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fleets {

    public static int[] withHandicap(int removeSmallest) {
        List<Integer> list = new ArrayList<>();
        for (int s : Utils.SHIP_SIZES) list.add(s);
        list.sort(Integer::compareTo);
        for (int i = 0; i < removeSmallest && !list.isEmpty(); i++) list.remove(0);

        int[] arr = new int[list.size()];
        for (int i = 0; i < arr.length; i++) arr[i] = list.get(i);
        return arr;
    }

    public static int[] withBoss() {
        int[] base = Utils.SHIP_SIZES;
        int[] arr = Arrays.copyOf(base, base.length + 1);
        arr[arr.length - 1] = 5;
        return arr;
    }
}