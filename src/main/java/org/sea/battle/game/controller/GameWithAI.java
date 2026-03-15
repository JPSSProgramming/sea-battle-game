package org.sea.battle.game.controller;

import org.sea.battle.game.model.AI;
import org.sea.battle.game.model.Player;
import org.sea.battle.game.view.GameWindow;
import org.sea.battle.game.view.ShipPlacementScreen;
import org.sea.battle.game.model.GameLogic;

public class GameWithAI {
    public GameWithAI() {
        Player human = new Player("Гравець");
        AI ai = new AI("Комп'ютер");

        ai.autoPlaceShips();

        new ShipPlacementScreen(human, () -> {
            GameLogic logic = new GameLogic(human, ai);
            new GameWindow(logic, true);
        });
    }
}