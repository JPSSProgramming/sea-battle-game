package org.sea.battle.game.view;

import org.sea.battle.game.utils.ProgressStore;
import org.sea.battle.game.utils.ShipSkin;
import org.sea.battle.game.utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ShopScreen extends JFrame {
    private final JLabel coinsLabel;
    private final JPanel listPanel;

    public ShopScreen() {
        setTitle("Магазин кораблів");
        setSize(500, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        Theme.styleFrame(this);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Theme.BG_DARK);
        top.setBorder(new EmptyBorder(16, 16, 8, 16));
        top.add(Theme.titleLabel("Магазин"), BorderLayout.NORTH);

        coinsLabel = new JLabel("", SwingConstants.CENTER);
        coinsLabel.setFont(Theme.FONT_HEADING);
        coinsLabel.setForeground(Theme.WARNING);
        top.add(coinsLabel, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Theme.BG_DARK);
        listPanel.setBorder(new EmptyBorder(8, 16, 8, 16));

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG_DARK);
        add(scroll, BorderLayout.CENTER);

        JButton back = Theme.styledButton("Назад у меню", Theme.BG_PANEL_LIGHT);
        back.addActionListener(e -> {
            dispose();
            new MainMenu();
        });
        JPanel bottom = new JPanel();
        bottom.setBackground(Theme.BG_DARK);
        bottom.setBorder(new EmptyBorder(8, 16, 16, 16));
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        refresh();
        setVisible(true);
    }

    private void refresh() {
        coinsLabel.setText("Монети: " + ProgressStore.get().getCoins());
        listPanel.removeAll();
        for (ShipSkin skin : ShipSkin.values()) {
            listPanel.add(buildRow(skin));
            listPanel.add(Box.createVerticalStrut(10));
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel buildRow(ShipSkin skin) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(Theme.BG_PANEL);
        row.setBorder(new EmptyBorder(10, 14, 10, 14));
        row.setMaximumSize(new Dimension(2000, 70));

        JPanel preview = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(skin.fill);
                g2.fillRoundRect(2, 2, 90, 30, 8, 8);
                g2.setColor(skin.border);
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(2, 2, 90, 30, 8, 8);
                g2.dispose();
            }
        };
        preview.setOpaque(false);
        preview.setPreferredSize(new Dimension(96, 34));

        JLabel label = new JLabel(skin.label + (skin.price > 0 ? " — " + skin.price + " монет" : " (базовий)"));
        label.setFont(Theme.FONT_BODY);
        label.setForeground(Theme.TEXT_PRIMARY);

        boolean owned = ProgressStore.get().ownsSkin(skin.id);
        boolean selected = ProgressStore.get().getSelectedSkin().equals(skin.id);

        JButton action;
        if (selected) {
            action = Theme.styledButton("Обрано", new Color(60, 60, 60));
            action.setEnabled(false);
        } else if (owned) {
            JButton btn = Theme.styledButton("Активувати", Theme.ACCENT_DARK);
            btn.addActionListener(e -> {
                ProgressStore.get().selectSkin(skin.id);
                refresh();
            });
            action = btn;
        } else {
            JButton btn = Theme.styledButton("Купити", Theme.WARNING.darker());
            btn.addActionListener(e -> {
                if (ProgressStore.get().spendCoins(skin.price)) {
                    ProgressStore.get().buySkin(skin.id);
                    ProgressStore.get().selectSkin(skin.id);
                    refresh();
                } else {
                    JOptionPane.showMessageDialog(this, "Недостатньо монет.",
                            "Купівля неможлива", JOptionPane.WARNING_MESSAGE);
                }
            });
            action = btn;
        }

        JPanel left = new JPanel(new BorderLayout(10, 0));
        left.setOpaque(false);
        left.add(preview, BorderLayout.WEST);
        left.add(label, BorderLayout.CENTER);

        row.add(left, BorderLayout.CENTER);
        row.add(action, BorderLayout.EAST);
        return row;
    }
}