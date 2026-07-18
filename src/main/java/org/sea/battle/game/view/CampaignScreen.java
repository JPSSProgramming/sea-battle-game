package org.sea.battle.game.view;

import org.sea.battle.game.model.AI;
import org.sea.battle.game.model.GameLogic;
import org.sea.battle.game.model.Level;
import org.sea.battle.game.model.LevelCatalog;
import org.sea.battle.game.model.Player;
import org.sea.battle.game.utils.Fleets;
import org.sea.battle.game.utils.ProgressStore;
import org.sea.battle.game.utils.Theme;
import org.sea.battle.game.utils.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CampaignScreen extends JFrame {

    public CampaignScreen() {
        setTitle("Кампанія — рівні");
        setSize(580, 660);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        Theme.styleFrame(this);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Theme.BG_DARK);
        top.setBorder(new EmptyBorder(16, 16, 8, 16));
        top.add(Theme.titleLabel("Кампанія"), BorderLayout.NORTH);

        JLabel coins = new JLabel("Монети: " + ProgressStore.get().getCoins(), SwingConstants.CENTER);
        coins.setFont(Theme.FONT_HEADING);
        coins.setForeground(Theme.WARNING);
        top.add(coins, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        JPanel list = new JPanel();
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        list.setBackground(Theme.BG_DARK);
        list.setBorder(new EmptyBorder(8, 16, 8, 16));

        int unlocked = ProgressStore.get().getMaxLevelUnlocked();
        for (Level level : LevelCatalog.LEVELS) {
            list.add(buildRow(level, level.index() <= unlocked));
            list.add(Box.createVerticalStrut(10));
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG_DARK);
        add(scroll, BorderLayout.CENTER);

        JButton back = Theme.styledButton("Назад у меню", Theme.BG_PANEL_LIGHT);
        back.addActionListener(e -> {
            dispose();
            new MainMenu();
        });
        JPanel bottom = new JPanel();
        bottom.setBackground(Theme.BG_DARK);
        bottom.setBorder(new EmptyBorder(8, 16, 16, 16));
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel buildRow(Level level, boolean unlocked) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(Theme.BG_PANEL);
        row.setBorder(new EmptyBorder(10, 14, 10, 14));
        row.setMaximumSize(new Dimension(2000, 96));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel name = new JLabel((unlocked ? "" : "\uD83D\uDD12 ") + level.index() + ". " + level.name());
        name.setFont(Theme.FONT_HEADING);
        name.setForeground(unlocked ? Theme.TEXT_PRIMARY : Theme.TEXT_MUTED);

        JLabel desc = new JLabel("<html><body style='width: 300px'>" + level.description() + "</body></html>");
        desc.setFont(Theme.FONT_BODY);
        desc.setForeground(Theme.TEXT_MUTED);

        StringBuilder tags = new StringBuilder("Нагорода: " + level.coinReward() + " монет");
        if (level.salvo()) tags.append(" · Залп");
        if (level.bossShip()) tags.append(" · Флагман");
        if (level.playerHandicap() > 0) tags.append(" · У вас менше кораблів");

        JLabel reward = new JLabel(tags.toString());
        reward.setFont(Theme.FONT_MONO);
        reward.setForeground(Theme.ACCENT);

        textPanel.add(name);
        textPanel.add(desc);
        textPanel.add(reward);

        JButton play = Theme.styledButton(unlocked ? "Грати" : "Заблоковано",
                unlocked ? Theme.ACCENT_DARK : new Color(60, 60, 60));
        play.setEnabled(unlocked);
        play.addActionListener(e -> playLevel(level));

        row.add(textPanel, BorderLayout.CENTER);
        row.add(play, BorderLayout.EAST);
        return row;
    }

    private void playLevel(Level level) {
        Player human = new Player("Гравець");
        int[] playerFleet = level.playerHandicap() > 0
                ? Fleets.withHandicap(level.playerHandicap())
                : Utils.SHIP_SIZES;
        int[] aiFleet = level.bossShip() ? Fleets.withBoss() : Utils.SHIP_SIZES;

        AI ai = new AI("Ворог (" + level.difficulty() + ")", level.difficulty());
        ai.autoPlaceShips(aiFleet);

        dispose();
        new ShipPlacementScreen(human, playerFleet, () -> {
            GameLogic logic = new GameLogic(human, ai, level.salvo());
            new GameWindow(logic, true, winner -> {
                boolean playerWon = (winner == human);
                if (playerWon) {
                    ProgressStore.get().addCoins(level.coinReward());
                    ProgressStore.get().unlockLevel(level.index() + 1);
                }
                String msg = playerWon
                        ? "Перемога! Отримано " + level.coinReward() + " монет."
                        : "Поразка. Спробуйте ще раз.";
                JOptionPane.showMessageDialog(null, msg,
                        playerWon ? "Рівень пройдено" : "Рівень не пройдено",
                        playerWon ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
                new CampaignScreen();
            });
        });
    }
}