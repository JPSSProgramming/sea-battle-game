package org.sea.battle.game.utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Theme {
    public static final Color BG_DARK        = new Color(15, 23, 42);
    public static final Color BG_PANEL       = new Color(24, 33, 56);
    public static final Color BG_PANEL_LIGHT = new Color(32, 44, 72);

    public static final Color WATER          = new Color(20, 60, 110);
    public static final Color WATER_LIGHT    = new Color(33, 90, 150);
    public static final Color WATER_HOVER    = new Color(52, 130, 195);

    public static final Color SHIP_COLOR     = new Color(70, 80, 96);
    public static final Color SHIP_BORDER    = new Color(210, 215, 225);

    public static final Color HIT_COLOR      = new Color(235, 64, 52);
    public static final Color MISS_COLOR     = new Color(180, 200, 220);
    public static final Color SUNK_COLOR     = new Color(120, 20, 20);

    public static final Color ACCENT         = new Color(45, 212, 191);
    public static final Color ACCENT_DARK    = new Color(20, 160, 145);
    public static final Color WARNING        = new Color(245, 158, 11);

    public static final Color TEXT_PRIMARY   = new Color(240, 244, 248);
    public static final Color TEXT_MUTED     = new Color(148, 163, 184);

    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BUTTON  = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_MONO    = new Font("Consolas", Font.PLAIN, 13);

    public static JPanel gradientPanel(Color top, Color bottom) {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, top, 0, getHeight(), bottom);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        return p;
    }

    public static JButton styledButton(String text, Color base) {
        JButton b = new JButton(text) {
            private boolean hover = false;
            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setBorder(new EmptyBorder(12, 22, 12, 22));
                setForeground(Color.WHITE);
                setFont(FONT_BUTTON);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override public void mouseEntered(java.awt.event.MouseEvent e) { hover = true; repaint(); }
                    @Override public void mouseExited(java.awt.event.MouseEvent e) { hover = false; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill = hover ? base.brighter() : base;
                g2.setColor(fill);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        return b;
    }

    public static void styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(BG_DARK);
    }

    public static void styleFrame(JDialog dialog) {
        dialog.getContentPane().setBackground(BG_DARK);
    }

    public static JLabel titleLabel(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(FONT_TITLE);
        l.setForeground(TEXT_PRIMARY);
        return l;
    }
}
