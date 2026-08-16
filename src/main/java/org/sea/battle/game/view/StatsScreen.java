package org.sea.battle.game.view;

import org.sea.battle.game.utils.GameStats;
import org.sea.battle.game.utils.Theme;
import java.awt.GraphicsEnvironment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StatsScreen extends JFrame {

    public StatsScreen() {
        setTitle("Статистика");
        setSize(520, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setUndecorated(true);
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
        Theme.styleFrame(this);

        JLabel title = Theme.titleLabel("СТАТИСТИКА");
        title.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_DARK);
        content.setBorder(new EmptyBorder(16, 32, 16, 32));

        GameStats stats = GameStats.get();

        add(statRow(content, "Усього ігор", String.valueOf(stats.getTotalGames())));
        add(statRow(content, "Перемог", String.valueOf(stats.getTotalWins())));
        add(statRow(content, "Процент перемог", String.format("%.1f%%", stats.getWinRate())));
        add(statRow(content, "Кораблів знищено", String.valueOf(stats.getTotalShipsSunk())));
        add(statRow(content, "Найдовша серія перемог", String.valueOf(stats.getLongestWinStreak())));
        add(statRow(content, "Точність", stats.getAccuracy() + "%"));
        add(statRow(content, "Загальний час гри", formatTime(stats.getTotalPlayTimeSeconds())));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG_DARK);
        add(scroll, BorderLayout.CENTER);

        JButton back = Theme.styledButton("Назад", Theme.BG_PANEL_LIGHT);
        back.addActionListener(e -> {
            dispose();
            new MainMenu();
        });
        JPanel bottom = new JPanel();
        bottom.setBackground(Theme.BG_DARK);
        bottom.setBorder(new EmptyBorder(12, 0, 12, 0));
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel statRow(JPanel parent, String label, String value) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(2000, 50));
        row.setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.FONT_BODY);
        lbl.setForeground(Theme.TEXT_PRIMARY);

        JLabel val = new JLabel(value);
        val.setFont(Theme.FONT_HEADING);
        val.setForeground(Theme.ACCENT);
        val.setHorizontalAlignment(SwingConstants.RIGHT);

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);
        parent.add(row);
        parent.add(Box.createVerticalStrut(4));
        return row;
    }

    private String formatTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        return String.format("%d год. %d хв.", hours, minutes);
    }
}