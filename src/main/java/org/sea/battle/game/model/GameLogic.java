package org.sea.battle.game.model;

public class GameLogic {
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private Player opponent;

    public GameLogic(Player p1, Player p2) {
        this.player1 = p1;
        this.player2 = p2;
        this.currentPlayer = player1;
        this.opponent = player2;
    }

    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }

    public Player getCurrentPlayer() { return currentPlayer; }
    public Player getOpponent() { return opponent; }

    public boolean makeMove(int x, int y) {
        boolean hit = opponent.getBoard().shoot(x, y);
        if (!hit) switchTurn();
        return hit;
    }

    public Player checkWinner() {
        if (player1.hasLost()) return player2;
        if (player2.hasLost()) return player1;
        return null;
    }

    private void switchTurn() {
        if (currentPlayer == player1) {
            currentPlayer = player2;
            opponent = player1;
        } else {
            currentPlayer = player1;
            opponent = player2;
        }
    }
}