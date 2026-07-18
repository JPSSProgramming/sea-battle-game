package org.sea.battle.game.view;

import org.sea.battle.game.model.Player;
import org.sea.battle.game.model.Ship;
import org.sea.battle.game.utils.Theme;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FleetTrackerPanel extends JPanel {
    private final Player player;

    public FleetTrackerPanel(Player player) {
        this.player = player;
        setOpaque(false);
        setPreferredSize(new Dimension(100, 26));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<Ship> ships = player.getShips().stream()
                .sorted(Comparator.comparingInt(Ship::size).reversed())
                .collect(Collectors.toList());

        int cellPx = 9;
        int gapWithinShip = 2;
        int gapBetweenShips = 6;
        int x = 0;
        int y = 4;
        int h = 16;

        for (Ship s : ships) {
            boolean sunk = s.isSunk();
            int w = s.size() * cellPx + (s.size() - 1) * gapWithinShip;

            g2.setColor(sunk ? new Color(75, 75, 80) : Theme.ACCENT);
            g2.fillRoundRect(x, y, w, h, 5, 5);

            if (sunk) {
                g2.setColor(Theme.HIT_COLOR);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(x + 1, y + 1, x + w - 1, y + h - 1);
                g2.drawLine(x + w - 1, y + 1, x + 1, y + h - 1);
            }

            x += w + gapBetweenShips;
        }

        g2.dispose();
    }

    public String summaryText() {
        long total = player.getShips().size();
        long sunk = player.getShips().stream().filter(Ship::isSunk).count();
        return "Потоплено кораблів: " + sunk + " з " + total;
    }
}