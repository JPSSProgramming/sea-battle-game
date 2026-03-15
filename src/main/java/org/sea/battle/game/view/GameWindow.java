package org.sea.battle.game.view;

import org.sea.battle.game.model.AI;
import org.sea.battle.game.model.GameLogic;
import org.sea.battle.game.model.Player;
import org.sea.battle.game.utils.Utils;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private final GameLogic logic;
    private final GameBoard leftBoard;
    private final GameBoard rightBoard;
    private final JLabel status;
    private final boolean vsAI;

    public GameWindow(GameLogic logic, boolean vsAI) {
        this.logic = logic;
        this.vsAI = vsAI;

        this.leftBoard  = logic.getPlayer1().getBoard();
        this.rightBoard = logic.getPlayer2().getBoard();

        if (vsAI) {
            if (logic.getPlayer2() instanceof AI) rightBoard.setShowShips(false);
            if (logic.getPlayer1() instanceof AI) leftBoard.setShowShips(false);
        }

        setTitle("Морський бій");
        setSize(900, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boards = new JPanel(new GridLayout(1, 2, 16, 0));
        boards.add(wrap(leftBoard,  logic.getPlayer1().getName()));
        boards.add(wrap(rightBoard, logic.getPlayer2().getName()));
        add(boards, BorderLayout.CENTER);

        status = new JLabel("Хід: " + logic.getCurrentPlayer().getName(), SwingConstants.CENTER);
        add(status, BorderLayout.SOUTH);

        java.awt.event.MouseAdapter listener = new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                GameBoard clicked = (GameBoard) e.getSource();
                GameBoard opponentBoard = getOpponentBoard();

                if (clicked != opponentBoard) return;
                int x = e.getX() / Utils.CELL_SIZE;
                int y = e.getY() / Utils.CELL_SIZE;

                if (opponentBoard.getCell(x, y) == null || opponentBoard.getCell(x, y).isHit()) return;

                boolean hit = logic.makeMove(x, y);
                refreshBoards();
                updateStatus(hit ? "Влучив!" : "Промах.");

                Player winner = logic.checkWinner();
                if (winner != null) endWithWinner(winner);

                playAITurnsIfNeeded();
            }
        };
        leftBoard.addMouseListener(listener);
        rightBoard.addMouseListener(listener);

        setVisible(true);

        playAITurnsIfNeeded();
    }

    private JPanel wrap(GameBoard board, String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(board, BorderLayout.CENTER);
        p.add(new JLabel(title, SwingConstants.CENTER), BorderLayout.SOUTH);
        return p;
    }

    private GameBoard getOpponentBoard() {
        return (logic.getOpponent() == logic.getPlayer1()) ? leftBoard : rightBoard;
    }

    private void refreshBoards() {
        leftBoard.repaint();
        rightBoard.repaint();
    }

    private void updateStatus(String msg) {
        status.setText("Хід: " + logic.getCurrentPlayer().getName() + " — " + msg);
    }

    private void playAITurnsIfNeeded() {
        while (logic.getCurrentPlayer() instanceof AI) {
            AI ai = (AI) logic.getCurrentPlayer();
            int[] t = ai.pickTarget(logic.getOpponent().getBoard());
            boolean hit = logic.makeMove(t[0], t[1]);
            ai.feedback(t[0], t[1], hit);

            refreshBoards();
            updateStatus(hit ? "AI влучив!" : "AI промахнувся.");

            Player winner = logic.checkWinner();
            if (winner != null) { endWithWinner(winner); return; }

            if (!hit) break;
        }
    }
    private void endWithWinner(Player winner) {
        JOptionPane.showMessageDialog(this, winner.getName() + " переміг!");
        dispose();
        new MainMenu();
    }
}
