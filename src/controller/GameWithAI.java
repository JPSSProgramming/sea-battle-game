package src.controller;

import src.model.AI;
import src.model.Player;
import src.view.GameWindow;
import src.view.ShipPlacementScreen;
import src.model.GameLogic;

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