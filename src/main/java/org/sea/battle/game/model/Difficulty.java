package org.sea.battle.game.model;

public enum Difficulty {
    EASY("Easy"),
    MEDIUM("Average"),
    HARD("Difficult");

    private final String label;

    Difficulty(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}