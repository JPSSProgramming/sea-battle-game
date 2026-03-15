package src.controller;

import src.model.GameLogic;
import src.model.Player;
import src.view.GameWindow;
import src.view.ShipPlacementScreen;

public class GameWithPlayer {
    private final Player player1;
    private final Player player2;

    public GameWithPlayer() {
        this.player1 = new Player("player 1");
        this.player2 = new Player("player 2");

        new ShipPlacementScreen(player1, () -> {
            new ShipPlacementScreen(player2, () -> {
                GameLogic logic = new GameLogic(player1, player2);
                new GameWindow(logic, false);
            });
        });
    }
}
