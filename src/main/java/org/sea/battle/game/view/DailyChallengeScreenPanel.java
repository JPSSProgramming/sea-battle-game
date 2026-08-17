package org.sea.battle.game.view;

import org.sea.battle.game.model.AI;
import org.sea.battle.game.model.Difficulty;
import org.sea.battle.game.model.GameLogic;
import org.sea.battle.game.model.Player;
import org.sea.battle.game.utils.GameStats;
import org.sea.battle.game.utils.SoundManager;
import org.sea.battle.game.utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class DailyChallengeScreenPanel extends JPanel {

    public DailyChallengeScreenPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_DARK);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_DARK);
        content.setBorder(new EmptyBorder(48, 48, 36, 48));

        JLabel title = Theme.titleLabel("ЩОДЕННИЙ ВИКЛИК");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 12, 0));

        JLabel date = new JLabel("Дата: " + LocalDate.now(), SwingConstants.CENTER);
        date.setFont(Theme.FONT_BODY);
        date.setForeground(Theme.TEXT_MUTED);
        date.setAlignmentX(Component.CENTER_ALIGNMENT);
        date.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel desc = new JLabel("<html><center>Один поєдинок на день, результат зберігається.<br>Змагання за часом!</center></html>", SwingConstants.CENTER);
        desc.setFont(Theme.FONT_BODY);
        desc.setForeground(Theme.TEXT_MUTED);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        desc.setBorder(new EmptyBorder(0, 0, 32, 0));

        JButton play = Theme.styledButton("Почати виклик", Theme.ACCENT_DARK);
        play.setAlignmentX(Component.CENTER_ALIGNMENT);
        play.setMaximumSize(new Dimension(280, 52));
        play.addActionListener(e -> startDailyChallenge());

        JButton leaderboard = Theme.styledButton("Таблиця лідерів (демо)", Theme.BG_PANEL_LIGHT);
        leaderboard.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboard.setMaximumSize(new Dimension(280, 52));
        leaderboard.addActionListener(e -> showLeaderboard());

        JButton back = Theme.styledButton("Назад", Theme.BG_PANEL_LIGHT);
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.setMaximumSize(new Dimension(280, 52));
        back.addActionListener(e -> NavigationManager.get().showMainMenu());

        content.add(title);
        content.add(date);
        content.add(desc);
        content.add(play);
        content.add(Box.createVerticalStrut(12));
        content.add(leaderboard);
        content.add(Box.createVerticalStrut(20));
        content.add(back);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG_DARK);
        add(scroll, BorderLayout.CENTER);
    }

    private void startDailyChallenge() {
        Player human = new Player("Гравець");
        AI ai = new AI("Ворог (середньо)", Difficulty.MEDIUM);
        ai.autoPlaceShips();

        ShipPlacementPanel placement = new ShipPlacementPanel(human, () -> {
            GameLogic logic = new GameLogic(human, ai, false);
            GamePanel gamePanel = new GamePanel(logic, true, winner -> {
                if (winner == human) {
                    GameStats.get().recordGame(true, 10, 85);
                    SoundManager.get().playVictory();
                    JOptionPane.showMessageDialog(NavigationManager.get().getWindow(),
                            "Ви перемогли в щоденному виклику!",
                            "Успіх", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    GameStats.get().recordGame(false, 0, 0);
                    SoundManager.get().playDefeat();
                    JOptionPane.showMessageDialog(NavigationManager.get().getWindow(),
                            "Спробуйте завтра в щоденному виклику!",
                            "Результат", JOptionPane.WARNING_MESSAGE);
                }
                NavigationManager.get().showMainMenu();
            });
            NavigationManager.get().showDynamic(gamePanel);
        });
        NavigationManager.get().showDynamic(placement);
    }

    private void showLeaderboard() {
        String[] leaders = {
                "1. Олександр — 2:34",
                "2. Марія — 3:12",
                "3. Іван — 3:45"
        };
        StringBuilder sb = new StringBuilder("<html>");
        for (String l : leaders) sb.append(l).append("<br>");
        sb.append("</html>");
        JOptionPane.showMessageDialog(NavigationManager.get().getWindow(), sb.toString(),
                "Таблиця лідерів на сьогодні", JOptionPane.INFORMATION_MESSAGE);
    }
}