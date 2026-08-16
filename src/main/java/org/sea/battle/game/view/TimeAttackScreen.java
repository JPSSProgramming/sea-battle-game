package org.sea.battle.game.view;

import org.sea.battle.game.model.AI;
import org.sea.battle.game.model.Difficulty;
import org.sea.battle.game.model.GameLogic;
import org.sea.battle.game.model.Player;
import org.sea.battle.game.utils.Theme;
import java.awt.GraphicsEnvironment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TimeAttackScreen extends JFrame {

    public TimeAttackScreen() {
        setTitle("Охота на час");
        setSize(500, 400);
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

        JLabel title = Theme.titleLabel("ОХОТА НА ЧАС");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 24, 0));

        JLabel desc = new JLabel("<html><center>5 хвилин часу.<br>Знищ максимум кораблів супротивника.<br>Результат зберігається.</center></html>");
        desc.setFont(Theme.FONT_BODY);
        desc.setForeground(Theme.TEXT_MUTED);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        desc.setBorder(new EmptyBorder(0, 0, 32, 0));

        JLabel diff = new JLabel("Виберіть складність:");
        diff.setFont(Theme.FONT_HEADING);
        diff.setForeground(Theme.TEXT_PRIMARY);
        diff.setAlignmentX(Component.CENTER_ALIGNMENT);
        diff.setBorder(new EmptyBorder(0, 0, 12, 0));

        for (Difficulty d : Difficulty.values()) {
            JButton btn = Theme.styledButton(d.toString(), Theme.ACCENT_DARK);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(260, 48));
            btn.addActionListener(e -> startGame(d));
            content.add(btn);
            content.add(Box.createVerticalStrut(10));
        }

        JButton back = Theme.styledButton("Назад", Theme.BG_PANEL_LIGHT);
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.setMaximumSize(new Dimension(260, 48));
        back.addActionListener(e -> {
            dispose();
            new MainMenu();
        });
        content.add(Box.createVerticalStrut(20));
        content.add(back);

        add(content, BorderLayout.CENTER);
        setVisible(true);
    }

    private void startGame(Difficulty difficulty) {
        Player human = new Player("Гравець");
        AI ai = new AI("Супротивник (" + difficulty + ")", difficulty);
        ai.autoPlaceShips();

        dispose();
        new ShipPlacementScreen(human, () -> {
            GameLogic logic = new GameLogic(human, ai, false);
            new TimedGameWindow(logic);
        });
    }
}