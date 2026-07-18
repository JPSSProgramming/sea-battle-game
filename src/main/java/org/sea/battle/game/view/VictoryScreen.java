package org.sea.battle.game.view;

import org.sea.battle.game.model.AI;
import org.sea.battle.game.model.Difficulty;
import org.sea.battle.game.model.GameLogic;
import org.sea.battle.game.model.Player;
import org.sea.battle.game.utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VictoryScreen extends JFrame {

    public VictoryScreen(Player winner, boolean vsAI, boolean salvoMode, Difficulty difficulty) {
        setTitle("Кінець гри");
        setSize(440, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        Theme.styleFrame(this);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_DARK);
        content.setBorder(new EmptyBorder(32, 32, 20, 32));

        JLabel trophy = new JLabel("\uD83C\uDFC6", SwingConstants.CENTER);
        trophy.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        trophy.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(winner.getName() + " переміг!", SwingConstants.CENTER);
        label.setFont(Theme.FONT_TITLE);
        label.setForeground(Theme.ACCENT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(new EmptyBorder(12, 0, 24, 0));

        JButton playAgainBtn = Theme.styledButton("Грати знову", Theme.ACCENT_DARK);
        JButton mainMenuBtn = Theme.styledButton("Головне меню", Theme.BG_PANEL_LIGHT);
        playAgainBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainMenuBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainBtn.setMaximumSize(new Dimension(240, 48));
        mainMenuBtn.setMaximumSize(new Dimension(240, 48));

        playAgainBtn.addActionListener(e -> {
            dispose();
            prepareNewGame(vsAI, salvoMode, difficulty);
        });

        mainMenuBtn.addActionListener(e -> {
            dispose();
            new MainMenu();
        });

        content.add(trophy);
        content.add(label);
        content.add(playAgainBtn);
        content.add(Box.createVerticalStrut(12));
        content.add(mainMenuBtn);

        add(content, BorderLayout.CENTER);
        setVisible(true);
    }

    private void prepareNewGame(boolean vsAI, boolean salvoMode, Difficulty difficulty) {
        Player p1 = new Player("Гравець 1");
        Player p2 = vsAI ? new AI("Комп'ютер (" + difficulty + ")", difficulty) : new Player("Гравець 2");
        GameLogic newLogic = new GameLogic(p1, p2, salvoMode);

        new ShipPlacementScreen(p1, () -> {
            if (vsAI) {
                ((AI) p2).autoPlaceShips();
                new GameWindow(newLogic, true);
            } else {
                new ShipPlacementScreen(p2, () -> new GameWindow(newLogic, false));
            }
        });
    }
}
