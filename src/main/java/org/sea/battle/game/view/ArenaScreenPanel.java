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

public class ArenaScreenPanel extends JPanel {

    public ArenaScreenPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_DARK);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_DARK);
        content.setBorder(new EmptyBorder(48, 48, 36, 48));

        JLabel title = Theme.titleLabel("ARENA");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 24, 0));

        JLabel desc = new JLabel("<html><center>Win 3 consecutive matches<br>Each victory gives you 1 token<br>Save the series and get rewarded!</center></html>", SwingConstants.CENTER);
        desc.setFont(Theme.FONT_BODY);
        desc.setForeground(Theme.TEXT_MUTED);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        desc.setBorder(new EmptyBorder(0, 0, 32, 0));

        JLabel diff = new JLabel("Choose an opponent:");
        diff.setFont(Theme.FONT_HEADING);
        diff.setForeground(Theme.TEXT_PRIMARY);
        diff.setAlignmentX(Component.CENTER_ALIGNMENT);
        diff.setBorder(new EmptyBorder(0, 0, 12, 0));

        content.add(title);
        content.add(desc);
        content.add(diff);

        for (Difficulty d : Difficulty.values()) {
            JButton btn = Theme.styledButton(d.toString(), Theme.ACCENT_DARK);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(240, 48));
            btn.addActionListener(e -> startArena(d));
            content.add(btn);
            content.add(Box.createVerticalStrut(8));
        }

        JButton back = Theme.styledButton("Back", Theme.BG_PANEL_LIGHT);
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.setMaximumSize(new Dimension(240, 48));
        back.addActionListener(e -> NavigationManager.get().showMainMenu());
        content.add(Box.createVerticalStrut(20));
        content.add(back);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG_DARK);
        add(scroll, BorderLayout.CENTER);
    }

    private void startArena(Difficulty difficulty) {
        playMatch(difficulty, 1, 0);
    }

    private void playMatch(Difficulty difficulty, int matchNum, int wins) {
        if (matchNum > 3) {
            GameStats.get().recordGame(true, 0, 0);
            SoundManager.get().playVictory();
            JOptionPane.showMessageDialog(NavigationManager.get().getWindow(),
                    "The arena is complete!\nYou won in" + wins + " duels!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            NavigationManager.get().showMainMenu();
            return;
        }

        Player human = new Player("Player");
        AI ai = new AI("Opponent " + matchNum + " (" + difficulty + ")", difficulty);
        ai.autoPlaceShips();

        ShipPlacementPanel placement = new ShipPlacementPanel(human, () -> {
            GameLogic logic = new GameLogic(human, ai, false);
            GamePanel gamePanel = new GamePanel(logic, true, winner -> {
                if (winner == human) {
                    GameStats.get().recordGame(true, 10, 80);
                    SoundManager.get().playVictory();
                    int newWins = wins + 1;
                    int resp = JOptionPane.showConfirmDialog(NavigationManager.get().getWindow(),
                            "You won! " + newWins + "/3\n\nContinue the arena?",
                            "Duel " + matchNum, JOptionPane.YES_NO_OPTION);
                    if (resp == JOptionPane.YES_OPTION) {
                        playMatch(difficulty, matchNum + 1, newWins);
                    } else {
                        NavigationManager.get().showMainMenu();
                    }
                } else {
                    GameStats.get().recordGame(false, 0, 0);
                    SoundManager.get().playDefeat();
                    JOptionPane.showMessageDialog(NavigationManager.get().getWindow(),
                            "You lost the duel " + matchNum + "\nYour series:" + wins + "victories ",
                            "Arena completed", JOptionPane.WARNING_MESSAGE);
                    NavigationManager.get().showMainMenu();
                }
            });
            NavigationManager.get().showDynamic(gamePanel);
        });
        NavigationManager.get().showDynamic(placement);
    }
}