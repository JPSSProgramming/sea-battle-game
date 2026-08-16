package org.sea.battle.game.view;

import org.sea.battle.game.model.AI;
import org.sea.battle.game.model.GameLogic;
import org.sea.battle.game.model.Player;
import org.sea.battle.game.utils.GameStats;
import org.sea.battle.game.utils.ParticleSystem;
import org.sea.battle.game.utils.SoundManager;
import org.sea.battle.game.utils.Theme;
import org.sea.battle.game.utils.Utils;
import java.awt.GraphicsEnvironment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class TimedGameWindow extends JFrame {
    private final GameLogic logic;
    private final GameBoard leftBoard;
    private final GameBoard rightBoard;
    private final JLabel timeLabel;
    private final JLabel shipsLabel;
    private final ParticleSystem particles;
    private int timeRemaining = 300;
    private int shipsSunk = 0;
    private long gameStartTime;

    public TimedGameWindow(GameLogic logic) {
        this.logic = logic;
        this.leftBoard = logic.getPlayer1().getBoard();
        this.rightBoard = logic.getPlayer2().getBoard();
        this.particles = new ParticleSystem();
        this.gameStartTime = System.currentTimeMillis();

        rightBoard.setShowShips(false);

        setTitle("Охота на час");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setUndecorated(true);
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
        Theme.styleFrame(this);

        timeLabel = new JLabel("5:00", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        timeLabel.setForeground(Theme.WARNING);

        shipsLabel = new JLabel("Знищено: 0", SwingConstants.CENTER);
        shipsLabel.setFont(Theme.FONT_HEADING);
        shipsLabel.setForeground(Theme.ACCENT);

        JPanel boards = new JPanel(new GridLayout(1, 2, 24, 0));
        boards.setBorder(new EmptyBorder(20, 20, 10, 20));
        boards.setBackground(Theme.BG_DARK);
        boards.add(wrapBoard(leftBoard, "Ви"));
        boards.add(wrapBoard(rightBoard, "Ворог"));
        add(boards, BorderLayout.CENTER);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        top.setBackground(Theme.BG_PANEL);
        top.add(timeLabel);
        top.add(shipsLabel);
        add(top, BorderLayout.NORTH);

        MouseAdapter clickListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / Utils.CELL_SIZE;
                int y = e.getY() / Utils.CELL_SIZE;
                if (rightBoard.getCell(x, y) == null || rightBoard.getCell(x, y).isHit()) return;

                GameLogic.ShotOutcome outcome = logic.shoot(x, y);
                leftBoard.repaint();
                rightBoard.repaint();
                if (outcome.sunkShip() != null) {
                    shipsSunk++;
                    shipsLabel.setText("Знищено: " + shipsSunk);
                    particles.burst(x * Utils.CELL_SIZE + Utils.CELL_SIZE / 2,
                            y * Utils.CELL_SIZE + Utils.CELL_SIZE / 2, 20, Theme.HIT_COLOR);
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

        Timer particleTimer = new Timer(30, e -> {
            particles.update();
            leftBoard.repaint();
            rightBoard.repaint();
        });
        particleTimer.start();

        Timer gameTimer = new Timer(1000, e -> {
            timeRemaining--;
            int mins = timeRemaining / 60;
            int secs = timeRemaining % 60;
            timeLabel.setText(String.format("%d:%02d", mins, secs));
            if (timeRemaining <= 0) {
                ((Timer) e.getSource()).stop();
                particleTimer.stop();
                endGame();
            }
        });
        gameTimer.start();

        setVisible(true);
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
        long gameTime = (System.currentTimeMillis() - gameStartTime) / 1000;
        GameStats.get().addPlayTime(gameTime);

        String msg = "Час вийшов!\nЗнищено кораблів: " + shipsSunk;
        JOptionPane.showMessageDialog(this, msg, "Охота на час закінчена", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new MainMenu();
    }
}