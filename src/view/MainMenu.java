package src.view;
import src.controller.GameWithAI;
import src.controller.GameWithPlayer;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Морський бій - Меню");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton playFriend = new JButton("Гра з другом");
        JButton playAI = new JButton("Гра з AI");
        JButton exit = new JButton("Вихід");


        playFriend.addActionListener(e -> {
            dispose();
            new GameWithPlayer();
        });

        playAI.addActionListener(e -> {
            dispose();
            new GameWithAI();
        });


        exit.addActionListener(e -> System.exit(0));

        panel.add(playFriend);
        panel.add(playAI);
        panel.add(exit);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }


}
