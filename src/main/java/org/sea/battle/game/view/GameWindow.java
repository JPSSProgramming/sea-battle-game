package org.sea.battle.game.view;

import org.sea.battle.game.model.AI;
import org.sea.battle.game.model.Difficulty;
import org.sea.battle.game.model.GameLogic;
import org.sea.battle.game.model.Player;
import org.sea.battle.game.utils.Theme;
import org.sea.battle.game.utils.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class GameWindow extends JFrame {
    private final GameLogic logic;
    private final GameBoard leftBoard;
    private final GameBoard rightBoard;
    private final JLabel status;
    private final JLabel shotsLabel;
    private final boolean vsAI;

    private int shotsRemaining;

    public GameWindow(GameLogic logic, boolean vsAI) {
        this.logic = logic;
        this.vsAI = vsAI;
        this.shotsRemaining = logic.shotsPerTurn();

        this.leftBoard  = logic.getPlayer1().getBoard();
        this.rightBoard = logic.getPlayer2().getBoard();

        if (vsAI) {
            if (logic.getPlayer2() instanceof AI) rightBoard.setShowShips(false);
            if (logic.getPlayer1() instanceof AI) leftBoard.setShowShips(false);
        }

        setTitle("Морський бій" + (logic.isSalvoMode() ? " — режим «Залп»" : ""));
        setSize(980, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        Theme.styleFrame(this);

        JPanel boards = new JPanel(new GridLayout(1, 2, 24, 0));
        boards.setBorder(new EmptyBorder(20, 20, 10, 20));
        boards.setBackground(Theme.BG_DARK);
        boards.add(wrap(leftBoard,  logic.getPlayer1().getName()));
        boards.add(wrap(rightBoard, logic.getPlayer2().getName()));
        add(boards, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setBackground(Theme.BG_PANEL);
        statusPanel.setBorder(new EmptyBorder(12, 16, 12, 16));

        status = new JLabel("", SwingConstants.CENTER);
        status.setFont(Theme.FONT_HEADING);
        status.setForeground(Theme.TEXT_PRIMARY);
        status.setAlignmentX(Component.CENTER_ALIGNMENT);

        shotsLabel = new JLabel("", SwingConstants.CENTER);
        shotsLabel.setFont(Theme.FONT_BODY);
        shotsLabel.setForeground(Theme.TEXT_MUTED);
        shotsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusPanel.add(status);
        statusPanel.add(Box.createVerticalStrut(4));
        statusPanel.add(shotsLabel);
        add(statusPanel, BorderLayout.SOUTH);

        MouseAdapter clickListener = new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                handleClick((GameBoard) e.getSource(), e);
            }
            @Override public void mouseExited(MouseEvent e) {
                ((GameBoard) e.getSource()).clearHover();
            }
        };
        MouseMotionAdapter hoverListener = new MouseMotionAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                handleHover((GameBoard) e.getSource(), e);
            }
        };
        leftBoard.addMouseListener(clickListener);
        rightBoard.addMouseListener(clickListener);
        leftBoard.addMouseMotionListener(hoverListener);
        rightBoard.addMouseMotionListener(hoverListener);

        refreshTurnLabels();
        setVisible(true);
        playAITurnsIfNeeded();
    }

    private JPanel wrap(GameBoard board, String title) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(Theme.BG_DARK);
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(Theme.FONT_HEADING);
        label.setForeground(Theme.TEXT_PRIMARY);
        p.add(label, BorderLayout.NORTH);

        JPanel boardWrap = new JPanel(new GridBagLayout());
        boardWrap.setBackground(Theme.BG_DARK);
        boardWrap.add(board);
        p.add(boardWrap, BorderLayout.CENTER);
        return p;
    }

    private GameBoard getOpponentBoard() {
        return (logic.getOpponent() == logic.getPlayer1()) ? leftBoard : rightBoard;
    }

    private void handleHover(GameBoard src, MouseEvent e) {
        if (logic.getCurrentPlayer() instanceof AI) return;
        GameBoard opponentBoard = getOpponentBoard();
        if (src != opponentBoard) return;

        int gx = e.getX() / Utils.CELL_SIZE;
        int gy = e.getY() / Utils.CELL_SIZE;
        if (Utils.inBounds(gx, gy)) opponentBoard.setHoverCell(gx, gy);
    }

    private void handleClick(GameBoard clicked, MouseEvent e) {
        if (logic.getCurrentPlayer() instanceof AI) return;

        GameBoard opponentBoard = getOpponentBoard();
        if (clicked != opponentBoard) return;

        int x = e.getX() / Utils.CELL_SIZE;
        int y = e.getY() / Utils.CELL_SIZE;
        if (opponentBoard.getCell(x, y) == null || opponentBoard.getCell(x, y).isHit()) return;

        GameLogic.ShotResult result = logic.shoot(x, y);
        refreshBoards();
        updateStatus(logic.getCurrentPlayer().getName(), result);

        Player winner = logic.checkWinner();
        if (winner != null) {
            endWithWinner(winner);
            return;
        }

        boolean turnOver;
        if (logic.isSalvoMode()) {
            shotsRemaining--;
            turnOver = shotsRemaining <= 0;
        } else {
            turnOver = (result == GameLogic.ShotResult.MISS);
        }

        if (turnOver) {
            opponentBoard.clearHover();
            logic.endTurn();
            shotsRemaining = logic.shotsPerTurn();
            refreshTurnLabels();
            playAITurnsIfNeeded();
        } else {
            refreshTurnLabels();
        }
    }

    private void refreshBoards() {
        leftBoard.repaint();
        rightBoard.repaint();
    }

    private void refreshTurnLabels() {
        status.setText("Хід: " + logic.getCurrentPlayer().getName());
        if (logic.isSalvoMode()) {
            shotsLabel.setText("Пострілів залишилось у цьому ході: " + shotsRemaining);
        } else {
            shotsLabel.setText("Влучив — стріляй ще раз. Промах — хід переходить супернику.");
        }
    }

    private void updateStatus(String actorName, GameLogic.ShotResult result) {
        String msg = switch (result) {
            case MISS -> "Промах.";
            case HIT -> "Влучення!";
            case SUNK -> "Корабель потоплено!";
        };
        status.setText(actorName + " — " + msg);
        if (result != GameLogic.ShotResult.MISS) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void playAITurnsIfNeeded() {
        while (logic.getCurrentPlayer() instanceof AI ai) {
            if (logic.isSalvoMode()) {
                int shots = logic.shotsPerTurn();
                for (int i = 0; i < shots; i++) {
                    GameLogic.ShotResult result = fireAIShot(ai);
                    if (result == null) return;
                }
            } else {
                GameLogic.ShotResult result;
                do {
                    result = fireAIShot(ai);
                    if (result == null) return;
                } while (result != GameLogic.ShotResult.MISS);
            }
            logic.endTurn();
            shotsRemaining = logic.shotsPerTurn();
            refreshTurnLabels();
        }
        refreshTurnLabels();
    }

    private GameLogic.ShotResult fireAIShot(AI ai) {
        int[] t = ai.pickTarget(logic.getOpponent().getBoard());
        GameLogic.ShotResult result = logic.shoot(t[0], t[1]);
        ai.feedback(t[0], t[1], result);
        refreshBoards();
        updateStatus(logic.getCurrentPlayer().getName(), result);

        Player winner = logic.checkWinner();
        if (winner != null) {
            endWithWinner(winner);
            return null;
        }
        return result;
    }

    private void endWithWinner(Player winner) {
        this.dispose();
        new VictoryScreen(winner, vsAI, logic.isSalvoMode(), vsAI ? findAiDifficulty() : null);
    }

    private Difficulty findAiDifficulty() {
        if (logic.getPlayer1() instanceof AI ai1) return ai1.getDifficulty();
        if (logic.getPlayer2() instanceof AI ai2) return ai2.getDifficulty();
        return Difficulty.MEDIUM;
    }
}
