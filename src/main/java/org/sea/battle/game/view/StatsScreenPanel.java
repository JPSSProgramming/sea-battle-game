package org.sea.battle.game.view;

import org.sea.battle.game.utils.GameStats;
import org.sea.battle.game.utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StatsScreenPanel extends JPanel {

    public StatsScreenPanel() {
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

        JLabel title = Theme.titleLabel("СТАТИСТИКА");
        title.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_DARK);
        content.setBorder(new EmptyBorder(16, 32, 16, 32));

        GameStats stats = GameStats.get();

        content.add(statRow("Усього ігор", String.valueOf(stats.getTotalGames())));
        content.add(statRow("Перемог", String.valueOf(stats.getTotalWins())));
        content.add(statRow("Процент перемог", String.format("%.1f%%", stats.getWinRate())));
        content.add(statRow("Кораблів знищено", String.valueOf(stats.getTotalShipsSunk())));
        content.add(statRow("Найдовша серія перемог", String.valueOf(stats.getLongestWinStreak())));
        content.add(statRow("Точність", stats.getAccuracy() + "%"));
        content.add(statRow("Загальний час гри", formatTime(stats.getTotalPlayTimeSeconds())));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG_DARK);
        add(scroll, BorderLayout.CENTER);

        JButton back = Theme.styledButton("Назад", Theme.BG_PANEL_LIGHT);
        back.addActionListener(e -> NavigationManager.get().showMainMenu());
        JPanel bottom = new JPanel();
        bottom.setBackground(Theme.BG_DARK);
        bottom.setBorder(new EmptyBorder(12, 0, 12, 0));
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JPanel statRow(String label, String value) {
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
        return row;
    }

    private String formatTime(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        return String.format("%d год. %d хв.", hours, minutes);
    }
}