package org.sea.battle.game.view;

import org.sea.battle.game.model.AI;
import org.sea.battle.game.model.Difficulty;
import org.sea.battle.game.model.GameLogic;
import org.sea.battle.game.model.Player;
import org.sea.battle.game.utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TimeAttackScreenPanel extends JPanel {

    public TimeAttackScreenPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_DARK);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_DARK);
        content.setBorder(new EmptyBorder(48, 48, 36, 48));

        JLabel title = Theme.titleLabel("ОХОТА НА ЧАС");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 0, 24, 0));

        JLabel desc = new JLabel("<html><center>5 хвилин часу.<br>Знищ максимум кораблів супротивника.<br>Результат зберігається у статистиці.</center></html>", SwingConstants.CENTER);
        desc.setFont(Theme.FONT_BODY);
        desc.setForeground(Theme.TEXT_MUTED);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        desc.setBorder(new EmptyBorder(0, 0, 32, 0));

        JLabel diff = new JLabel("Виберіть складність:");
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
            btn.setMaximumSize(new Dimension(260, 48));
            btn.addActionListener(e -> startGame(d));
            content.add(btn);
            content.add(Box.createVerticalStrut(10));
        }

        JButton back = Theme.styledButton("Назад", Theme.BG_PANEL_LIGHT);
        back.setAlignmentX(Component.CENTER_ALIGNMENT);
        back.setMaximumSize(new Dimension(260, 48));
        back.addActionListener(e -> NavigationManager.get().showMainMenu());
        content.add(Box.createVerticalStrut(20));
        content.add(back);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG_DARK);
        add(scroll, BorderLayout.CENTER);
    }

    private void startGame(Difficulty difficulty) {
        Player human = new Player("Гравець");
        AI ai = new AI("Супротивник (" + difficulty + ")", difficulty);
        ai.autoPlaceShips();

        ShipPlacementPanel placement = new ShipPlacementPanel(human, () -> {
            GameLogic logic = new GameLogic(human, ai, false);
            NavigationManager.get().showDynamic(new TimedGamePanel(logic));
        });
        NavigationManager.get().showDynamic(placement);
    }
}