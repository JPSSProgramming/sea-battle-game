package org.sea.battle.game.utils;

public enum GameMode {
    CLASSIC("Classic", "Traditional game"),
    TIMED("Hunting for time", "5 minutes to destroy maximum ships"),
    ARENA("Arena", "3 consecutive matches with bonuses"),
    DAILY("Daily Challenge", "All players compete against time"),
    FORTUNE("Fortunes", "Random bonuses change the game");

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