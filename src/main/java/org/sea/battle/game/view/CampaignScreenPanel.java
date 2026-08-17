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

public class CampaignScreenPanel extends JPanel {
    private JLabel coinsLabel;
    private JPanel list;

    public CampaignScreenPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_DARK);
        rebuild();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        rebuild();
    }

    private void rebuild() {
        removeAll();

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Theme.BG_DARK);
        top.setBorder(new EmptyBorder(16, 16, 8, 16));
        top.add(Theme.titleLabel("Campaign"), BorderLayout.NORTH);

        coinsLabel = new JLabel("Coins: " + ProgressStore.get().getCoins(), SwingConstants.CENTER);
        coinsLabel.setFont(Theme.FONT_HEADING);
        coinsLabel.setForeground(Theme.WARNING);
        top.add(coinsLabel, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        list = new JPanel();
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

        JButton back = Theme.styledButton("Back to menu", Theme.BG_PANEL_LIGHT);
        back.addActionListener(e -> NavigationManager.get().showMainMenu());
        JPanel bottom = new JPanel();
        bottom.setBackground(Theme.BG_DARK);
        bottom.setBorder(new EmptyBorder(8, 16, 16, 16));
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JPanel buildRow(Level level, boolean unlocked) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(Theme.BG_PANEL);
        row.setBorder(new EmptyBorder(10, 14, 10, 14));
        row.setMaximumSize(new Dimension(2000, 96));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

        JLabel name = new JLabel((unlocked ? "" : "[BLOCKED] ") + level.index() + ". " + level.name());
        name.setFont(Theme.FONT_HEADING);
        name.setForeground(unlocked ? Theme.TEXT_PRIMARY : Theme.TEXT_MUTED);

        JLabel desc = new JLabel("<html><body style='width: 300px'>" + level.description() + "</body></html>");
        desc.setFont(Theme.FONT_BODY);
        desc.setForeground(Theme.TEXT_MUTED);

        StringBuilder tags = new StringBuilder("Award: " + level.coinReward() + "coins ");
        if (level.salvo()) tags.append("Volley");
        if (level.bossShip()) tags.append("Flagship");
        if (level.playerHandicap() > 0) tags.append("Handicap");

        JLabel reward = new JLabel(tags.toString());
        reward.setFont(Theme.FONT_MONO);
        reward.setForeground(Theme.ACCENT);

        textPanel.add(name);
        textPanel.add(desc);
        textPanel.add(reward);

        JButton play = Theme.styledButton(unlocked ? "Play" : "Blocked",
                unlocked ? Theme.ACCENT_DARK : new Color(60, 60, 60));
        play.setEnabled(unlocked);
        play.addActionListener(e -> playLevel(level));

        row.add(textPanel, BorderLayout.CENTER);
        row.add(play, BorderLayout.EAST);
        return row;
    }

    private void playLevel(Level level) {
        Player human = new Player("Player");
        int[] playerFleet = level.playerHandicap() > 0
                ? Fleets.withHandicap(level.playerHandicap())
                : Utils.SHIP_SIZES;
        int[] aiFleet = level.bossShip() ? Fleets.withBoss() : Utils.SHIP_SIZES;

        AI ai = new AI("Enemy (" + level.difficulty() + ")", level.difficulty());
        ai.autoPlaceShips(aiFleet);

        ShipPlacementPanel placement = new ShipPlacementPanel(human, playerFleet, () -> {
            GameLogic logic = new GameLogic(human, ai, level.salvo());
            GamePanel gamePanel = new GamePanel(logic, true, winner -> {
                boolean playerWon = (winner == human);
                if (playerWon) {
                    ProgressStore.get().addCoins(level.coinReward());
                    ProgressStore.get().unlockLevel(level.index() + 1);
                }
                String msg = playerWon
                        ? "Victory! Received " + level.coinReward() + "coins"
                        : "Defeat. Try again.";
                JOptionPane.showMessageDialog(NavigationManager.get().getWindow(), msg,
                        playerWon ? "Level passed" : "Level not passed",
                        playerWon ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
                NavigationManager.get().showCampaign();
            });
            NavigationManager.get().showDynamic(gamePanel);
        });
        NavigationManager.get().showDynamic(placement);
    }
}