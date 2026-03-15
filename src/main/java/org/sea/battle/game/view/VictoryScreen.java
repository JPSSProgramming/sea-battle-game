package org.sea.battle.game.view;

import org.sea.battle.game.model.AI;
import org.sea.battle.game.model.GameLogic;
import org.sea.battle.game.model.Player;
import javax.swing.*;
import java.awt.*;

public class VictoryScreen extends JFrame {

    public VictoryScreen(Player winner, boolean vsAI) {
        setTitle("Кінець гри");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel(winner.getName() + " переміг!", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(label, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton playAgainBtn = new JButton("Грати знову");
        JButton mainMenuBtn = new JButton("Головне меню");

        playAgainBtn.addActionListener(e -> {
            dispose();
            prepareNewGame(vsAI);
        });

        mainMenuBtn.addActionListener(e -> {
            dispose();
            new MainMenu();
        });

        buttonPanel.add(playAgainBtn);
        buttonPanel.add(mainMenuBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void prepareNewGame(boolean vsAI) {
        Player p1 = new Player("Гравець 1");
        Player p2 = vsAI ? new AI("Комп'ютер") : new Player("Гравець 2");
        GameLogic newLogic = new GameLogic(p1, p2);

        new ShipPlacementScreen(p1, () -> {
            if (vsAI) {
                ((AI) p2).autoPlaceShips();
                new GameWindow(newLogic, true);
            } else {
                new ShipPlacementScreen(p2, () -> {
                    new GameWindow(newLogic, false);
                });
            }
        });
    }
}