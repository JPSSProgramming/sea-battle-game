package org.sea.battle.game.model;

import java.util.List;

public class LevelCatalog {

    public static final List<Level> LEVELS = List.of(
            new Level(1, "Intelligence",
                    "A weak opponent, ideal for warming up",
                    Difficulty.EASY, false, false, 0, 50),
            new Level(2, "Coastal Battle",
                    "The same enemy, but now he fires in a single volley",
                    Difficulty.EASY, true, false, 0, 60),
            new Level(3, "Enemy fleet",
                    "The enemy is smarter - he finishes off the damaged ship",
                    Difficulty.MEDIUM, false, false, 0, 80),
            new Level(4, "Storm volley",
                    "Medium difficulty level and volley fire at the same time",
                    Difficulty.MEDIUM, true, false, 0, 100),
            new Level(5, "Unequal forces",
                    "You have 2 fewer boats than usual",
                    Difficulty.MEDIUM, false, false, 2, 130),
            new Level(6, "Experienced opponent",
                    "The AI calculates the ship's direction after the second hit",
                    Difficulty.HARD, false, false, 0, 160),
            new Level(7, "Lightning volley",
                    "A complex AI that fires multiple projectiles at once per turn",
                    Difficulty.HARD, true, false, 0, 200),
            new Level(8, "Enemy flagship",
                    "The enemy has an additional heavy ship. Campaign finale.",
                    Difficulty.HARD, false, true, 0, 280)
    );
}