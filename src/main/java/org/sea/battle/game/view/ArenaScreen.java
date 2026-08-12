package org.sea.battle.game.view;

import org.sea.battle.game.model.AI;
import org.sea.battle.game.model.Difficulty;
import org.sea.battle.game.model.GameLogic;
import org.sea.battle.game.model.Player;
import org.sea.battle.game.utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class ArenaScreen extends JFrame {

    public ArenaScreen() {
        setTitle("Арена");
        setSize(500, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        Theme.styleFrame(this);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_DARK);
        content.setBorder(new EmptyBorder(36, 48, 36, 48));

        JLabel title = Theme.titleLabel("АРЕНА");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 24, 0));

        JLabel desc = new JLabel("<html><center>Перемож 3 послідовних поєдинків.<br>" +
                "Кожна перемога дає вам 1 жетон.<br>Зберегти серію — отримати винагороду!</center></html>");
        desc.setFont(Theme.FONT_BODY);
        desc.setForeground(Theme.TEXT_MUTED);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        desc.setBorder(new EmptyBorder(0, 0, 32, 0));

        JLabel diff = new JLabel("Виберіть опонента:");
        diff.setFont(Theme.FONT_HEADING);
        diff.setForeground(Theme.TEXT_PRIMARY);
        diff.setAlignmentX(Component.CENTER_ALIGNMENT);
        diff.setBorder(new EmptyBorder(0, 0, 12, 0));

        for (Difficulty d : Difficulty.values()) {
            JButton btn = Theme.styledButton(d.toString(), Theme.ACCENT_DARK);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(240, 48));
            btn.addActionListener(e -> startArena(d));
            content.add(btn);
            content.add(Box.createVerticalStrut(8));
        }

        JButton back = Theme.styledButton("Назад", Theme.BG_PANEL_LIGHT);
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.setMaximumSize(new Dimension(240, 48));
        back.addActionListener(e -> { dispose(); new MainMenu(); });
        content.add(Box.createVerticalStrut(20));
        content.add(back);

        add(content, BorderLayout.CENTER);
        setVisible(true);
    }

    private void startArena(Difficulty difficulty) {
        dispose();
        playMatch(difficulty, 1, 0);
    }

    private void playMatch(Difficulty difficulty, int matchNum, int wins) {
        if (matchNum > 3) {
            JOptionPane.showMessageDialog(null,
                    "🏆 Арена завершена!\nВи перемогли у " + wins + " поєдинках!",
                    "Успіх", JOptionPane.INFORMATION_MESSAGE);
            new MainMenu();
            return;
        }

        Player human = new Player("Гравець");
        AI ai = new AI("Опонент " + matchNum + " (" + difficulty + ")", difficulty);
        ai.autoPlaceShips();

        new ShipPlacementScreen(human, () -> {
            GameLogic logic = new GameLogic(human, ai, false);
            new GameWindow(logic, true, winner -> {
                if (winner == human) {
                    int newWins = wins + 1;
                    int resp = JOptionPane.showConfirmDialog(null,
                            "Ви перемогли! " + newWins + "/3\n\nПродовжити арену?",
                            "Поєдинок " + matchNum, JOptionPane.YES_NO_OPTION);
                    if (resp == JOptionPane.YES_OPTION) {
                        playMatch(difficulty, matchNum + 1, newWins);
                    } else {
                        new MainMenu();
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Ви програли на поєдинку " + matchNum + "\nВаша серія: " + wins + " перемог.",
                            "Арена завершена", JOptionPane.WARNING_MESSAGE);
                    new MainMenu();
                }
            });
        });
    }
}