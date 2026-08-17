package org.sea.battle.game.view;

import org.sea.battle.game.model.GameLogic;
import org.sea.battle.game.utils.GameStats;
import org.sea.battle.game.utils.SoundManager;
import org.sea.battle.game.utils.Theme;
import org.sea.battle.game.utils.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class TimedGamePanel extends JPanel implements Stoppable {
    private final GameLogic logic;
    private final GameBoard leftBoard;
    private final GameBoard rightBoard;
    private final JLabel timeLabel;
    private final JLabel shipsLabel;
    private final Timer particleTimer;
    private final Timer gameTimer;
    private int timeRemaining = 300;
    private int shipsSunk = 0;
    private final long gameStartTime;
    private boolean finished = false;

    public TimedGamePanel(GameLogic logic) {
        this.logic = logic;
        this.leftBoard = logic.getPlayer1().getBoard();
        this.rightBoard = logic.getPlayer2().getBoard();
        this.gameStartTime = System.currentTimeMillis();

        rightBoard.setShowShips(false);

        setLayout(new BorderLayout());
        setBackground(Theme.BG_DARK);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Theme.BG_PANEL);
        topBar.setBorder(new EmptyBorder(8, 12, 8, 12));
        JButton menuBtn = Theme.styledButton("to menu (Esc)", Theme.BG_PANEL_LIGHT);
        menuBtn.addActionListener(e -> NavigationManager.get().showMainMenu());
        topBar.add(menuBtn, BorderLayout.WEST);

        timeLabel = new JLabel("5:00", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        timeLabel.setForeground(Theme.WARNING);

        shipsLabel = new JLabel("Destroy: 0", SwingConstants.CENTER);
        shipsLabel.setFont(Theme.FONT_HEADING);
        shipsLabel.setForeground(Theme.ACCENT);

        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 4));
        center.setOpaque(false);
        center.add(timeLabel);
        center.add(shipsLabel);
        topBar.add(center, BorderLayout.CENTER);
        add(topBar, BorderLayout.NORTH);

        JPanel boards = new JPanel(new GridLayout(1, 2, 24, 0));
        boards.setBorder(new EmptyBorder(20, 20, 10, 20));
        boards.setBackground(Theme.BG_DARK);
        boards.add(wrapBoard(leftBoard, "You"));
        boards.add(wrapBoard(rightBoard, "Enemy"));
        add(boards, BorderLayout.CENTER);

        MouseAdapter clickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (finished) return;
                int x = e.getX() / Utils.CELL_SIZE;
                int y = e.getY() / Utils.CELL_SIZE;
                if (rightBoard.getCell(x, y) == null || rightBoard.getCell(x, y).isHit()) return;

                GameLogic.ShotOutcome outcome = logic.shoot(x, y);
                leftBoard.repaint();
                rightBoard.repaint();
                if (outcome.sunkShip() != null) {
                    shipsSunk++;
                    shipsLabel.setText("Destroy: " + shipsSunk);
                    rightBoard.getParticles().burst(
                            x * Utils.CELL_SIZE + Utils.CELL_SIZE / 2,
                            y * Utils.CELL_SIZE + Utils.CELL_SIZE / 2,
                            22, Theme.HIT_COLOR);
                    SoundManager.get().playSunk();
                } else if (outcome.result() == GameLogic.ShotResult.HIT) {
                    SoundManager.get().playHit();
                } else {
                    SoundManager.get().playMiss();
                }
            }
        };
        rightBoard.addMouseListener(clickListener);
        rightBoard.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int gx = e.getX() / Utils.CELL_SIZE;
                int gy = e.getY() / Utils.CELL_SIZE;
                if (Utils.inBounds(gx, gy)) rightBoard.setHoverCell(gx, gy);
            }
        });

        particleTimer = new Timer(30, e -> {
            rightBoard.getParticles().update();
            rightBoard.repaint();
        });
        particleTimer.start();

        gameTimer = new Timer(1000, e -> {
            timeRemaining--;
            int mins = timeRemaining / 60;
            int secs = timeRemaining % 60;
            timeLabel.setText(String.format("%d:%02d", mins, secs));
            if (timeRemaining <= 0) {
                endGame();
            }
        });
        gameTimer.start();
    }

    @Override
    public void stopTimers() {
        particleTimer.stop();
        gameTimer.stop();
    }

    private JPanel wrapBoard(GameBoard board, String title) {
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

    private void endGame() {
        if (finished) return;
        finished = true;
        stopTimers();

        long gameTime = (System.currentTimeMillis() - gameStartTime) / 1000;
        GameStats.get().addPlayTime(gameTime);
        GameStats.get().recordGame(shipsSunk > 0, shipsSunk, 70);
        SoundManager.get().playVictory();

        String msg = "Time is up!\n" +
                "Ships destroyed: " + shipsSunk;
        JOptionPane.showMessageDialog(this, msg, "The hunt for time is over.", JOptionPane.INFORMATION_MESSAGE);
        NavigationManager.get().showMainMenu();
    }
}