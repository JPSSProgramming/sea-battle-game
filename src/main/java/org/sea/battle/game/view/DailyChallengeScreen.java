package org.sea.battle.game.view;

import org.sea.battle.game.model.AI;
import org.sea.battle.game.model.Difficulty;
import org.sea.battle.game.model.GameLogic;
import org.sea.battle.game.model.Player;
import org.sea.battle.game.utils.GameStats;
import org.sea.battle.game.utils.SoundManager;
import org.sea.battle.game.utils.Theme;
import java.awt.GraphicsEnvironment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class DailyChallengeScreen extends JFrame {

    public DailyChallengeScreen() {
        setTitle("Щоденний виклик");
        setSize(520, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setUndecorated(true);
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
        Theme.styleFrame(this);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_DARK);
        content.setBorder(new EmptyBorder(36, 48, 36, 48));

        JLabel title = Theme.titleLabel("ЩОДЕННИЙ ВИКЛИК");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel date = new JLabel("Дата: " + LocalDate.now());
        date.setFont(Theme.FONT_BODY);
        date.setForeground(Theme.TEXT_MUTED);
        date.setAlignmentX(Component.CENTER_ALIGNMENT);
        date.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel desc = new JLabel("<html><center>Щодня однакова карта для всіх гравців.<br>Один поєдинок, результат зберігається.<br>Змаганння за часом!</center></html>");
        desc.setFont(Theme.FONT_BODY);
        desc.setForeground(Theme.TEXT_MUTED);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        desc.setBorder(new EmptyBorder(0, 0, 32, 0));

        JButton play = Theme.styledButton("Почати виклик", Theme.ACCENT_DARK);
        play.setAlignmentX(Component.CENTER_ALIGNMENT);
        play.setMaximumSize(new Dimension(280, 52));
        play.addActionListener(e -> startDailyChallenge());

        JButton leaderboard = Theme.styledButton("Таблиця лідерів (імітація)", Theme.BG_PANEL_LIGHT);
        leaderboard.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboard.setMaximumSize(new Dimension(280, 52));
        leaderboard.addActionListener(e -> showLeaderboard());

        JButton back = Theme.styledButton("Назад", Theme.BG_PANEL_LIGHT);
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.setMaximumSize(new Dimension(280, 52));
        back.addActionListener(e -> { dispose(); new MainMenu(); });

        content.add(date);
        content.add(desc);
        content.add(play);
        content.add(Box.createVerticalStrut(12));
        content.add(leaderboard);
        content.add(Box.createVerticalStrut(20));
        content.add(back);

        add(content, BorderLayout.CENTER);
        setVisible(true);
    }

    private void startDailyChallenge() {
        Player human = new Player("Гравець");
        AI ai = new AI("Ворог (середньо)", Difficulty.MEDIUM);
        ai.autoPlaceShips();

        dispose();
        new ShipPlacementScreen(human, () -> {
            GameLogic logic = new GameLogic(human, ai, false);
            new GameWindow(logic, true, winner -> {
                if (winner == human) {
                    GameStats.get().recordGame(true, 10, 85);
                    SoundManager.get().playVictory();
                    JOptionPane.showMessageDialog(null, "Ви перемогли в щоденному виклику!",
                            "Успіх", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    GameStats.get().recordGame(false, 0, 0);
                    SoundManager.get().playDefeat();
                    JOptionPane.showMessageDialog(null, "Спробуйте завтра в щоденному виклику!",
                            "Результат", JOptionPane.WARNING_MESSAGE);
                }
                new MainMenu();
            });
        });
    }

    private void showLeaderboard() {
        JDialog dialog = new JDialog(this, "Таблиця лідерів на сьогодні", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        Theme.styleFrame(dialog);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_DARK);
        content.setBorder(new EmptyBorder(16, 16, 16, 16));

        String[] leaders = {
                "1. Олександр - 2:34",
                "2. Марія - 3:12",
                "3. Іван - 3:45"
        };

        for (String leader : leaders) {
            JLabel l = new JLabel(leader);
            l.setFont(Theme.FONT_BODY);
            l.setForeground(Theme.TEXT_PRIMARY);
            l.setBorder(new EmptyBorder(8, 0, 8, 0));
            content.add(l);
        }

        JButton close = Theme.styledButton("Закрити", Theme.BG_PANEL_LIGHT);
        close.addActionListener(e -> dialog.dispose());
        content.add(Box.createVerticalStrut(20));
        content.add(close);

        dialog.add(new JScrollPane(content));
        dialog.setVisible(true);
    }
}