package org.sea.battle.game.utils;

public enum GameMode {
    CLASSIC("Класичний", "Традиційна гра"),
    TIMED("Охота на час", "5 хвилин на знищення максимуму кораблів"),
    ARENA("Арена", "3 послідовні поєдинки з бонусами"),
    DAILY("Щоденний виклик", "Змагання всіх гравців за часом"),
    FORTUNE("Фортуни", "Випадкові бонуси змінюють гру");

    private final String label;
    private final String description;

    GameMode(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}