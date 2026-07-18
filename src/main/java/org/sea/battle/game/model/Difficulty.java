package org.sea.battle.game.model;

public enum Difficulty {
    EASY("Легко"),
    MEDIUM("Середньо"),
    HARD("Складно");

    private final String label;

    Difficulty(String label) { this.label = label; }

    @Override
    public String toString() { return label; }
}
