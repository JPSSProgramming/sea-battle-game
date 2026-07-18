package org.sea.battle.game.model;

public record Level(
        int index,
        String name,
        String description,
        Difficulty difficulty,
        boolean salvo,
        boolean bossShip,
        int playerHandicap,
        int coinReward
) {
}