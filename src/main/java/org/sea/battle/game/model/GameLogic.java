package org.sea.battle.game.model;

public class GameLogic {

    public enum ShotResult { MISS, HIT, SUNK }

    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private Player opponent;
    private final boolean salvoMode;

    public GameLogic(Player p1, Player p2) {
        this(p1, p2, false);
    }

    public GameLogic(Player p1, Player p2, boolean salvoMode) {
        this.player1 = p1;
        this.player2 = p2;
        this.currentPlayer = player1;
        this.opponent = player2;
        this.salvoMode = salvoMode;
    }

    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }

    public Player getCurrentPlayer() { return currentPlayer; }
    public Player getOpponent() { return opponent; }

    public boolean isSalvoMode() { return salvoMode; }

    public int shotsPerTurn() {
        return salvoMode ? Math.max(1, currentPlayer.remainingShipsCount()) : 1;
    }

    public ShotResult shoot(int x, int y) {
        boolean hit = opponent.getBoard().shoot(x, y);
        if (!hit) return ShotResult.MISS;

        Ship ship = opponent.getBoard().findShipAt(x, y);
        if (ship != null) {
            ship.markSunkIfNeeded();
            if (ship.isSunk()) return ShotResult.SUNK;
        }
        return ShotResult.HIT;
    }

    public void endTurn() {
        if (currentPlayer == player1) {
            currentPlayer = player2;
            opponent = player1;
        } else {
            currentPlayer = player1;
            opponent = player2;
        }
    }

    public Player checkWinner() {
        if (player1.hasLost()) return player2;
        if (player2.hasLost()) return player1;
        return null;
    }
}
