package org.sea.battle.game.controller;

import org.sea.battle.game.model.AI;
import org.sea.battle.game.model.Difficulty;
import org.sea.battle.game.model.GameLogic;
import org.sea.battle.game.model.Player;
import org.sea.battle.game.view.GameWindow;
import org.sea.battle.game.view.ShipPlacementScreen;

public class GameWithAI {
    public GameWithAI(Difficulty difficulty, boolean salvoMode) {
        Player human = new Player("Гравець");
        AI ai = new AI("Комп'ютер (" + difficulty + ")", difficulty);

        ai.autoPlaceShips();

        new ShipPlacementScreen(human, () -> {
            GameLogic logic = new GameLogic(human, ai, salvoMode);
            new GameWindow(logic, true);
        });
    }
}
