package org.sea.battle.game.view;

import org.sea.battle.game.controller.GameWithAI;
import org.sea.battle.game.controller.GameWithPlayer;
import org.sea.battle.game.model.Difficulty;
import org.sea.battle.game.utils.ProgressStore;
import org.sea.battle.game.utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Морський бій");
        setSize(460, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        Theme.styleFrame(this);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_DARK);
        content.setBorder(new EmptyBorder(36, 48, 36, 48));

        JLabel title = Theme.titleLabel("МОРСЬКИЙ БІЙ");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Класична стратегічна гра", SwingConstants.CENTER);
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel coins = new JLabel("\uD83E\uDE99 " + ProgressStore.get().getCoins() + " монет", SwingConstants.CENTER);
        coins.setFont(Theme.FONT_HEADING);
        coins.setForeground(Theme.WARNING);
        coins.setAlignmentX(Component.CENTER_ALIGNMENT);
        coins.setBorder(new EmptyBorder(6, 0, 24, 0));

        JButton campaign = Theme.styledButton("Кампанія (рівні)", Theme.ACCENT_DARK);
        JButton shop = Theme.styledButton("Магазин кораблів", Theme.WARNING.darker());
        JButton playAI = Theme.styledButton("Швидка гра проти комп'ютера", Theme.BG_PANEL_LIGHT);
        JButton playFriend = Theme.styledButton("Гра з другом (2 гравці)", Theme.BG_PANEL_LIGHT);
        JButton exit = Theme.styledButton("Вихід", new Color(90, 30, 30));

        for (JButton b : new JButton[]{campaign, shop, playAI, playFriend, exit}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(320, 52));
        }

        campaign.addActionListener(e -> {
            dispose();
            new CampaignScreen();
        });
        shop.addActionListener(e -> {
            dispose();
            new ShopScreen();
        });
        playAI.addActionListener(e -> showAiSetupDialog());
        playFriend.addActionListener(e -> showPvpSetupDialog());
        exit.addActionListener(e -> System.exit(0));

        content.add(title);
        content.add(subtitle);
        content.add(coins);
        content.add(campaign);
        content.add(Box.createVerticalStrut(12));
        content.add(shop);
        content.add(Box.createVerticalStrut(20));
        content.add(playAI);
        content.add(Box.createVerticalStrut(12));
        content.add(playFriend);
        content.add(Box.createVerticalStrut(20));
        content.add(exit);

        add(content, BorderLayout.CENTER);
        setVisible(true);
    }

    private void showAiSetupDialog() {
        JDialog dialog = new JDialog(this, "Налаштування гри проти ШІ", true);
        dialog.setLayout(new BorderLayout());
        Theme.styleFrame(dialog);
        dialog.setSize(380, 320);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.BG_DARK);
        panel.setBorder(new EmptyBorder(24, 28, 24, 28));

        JLabel heading = new JLabel("Рівень складності ШІ");
        heading.setFont(Theme.FONT_HEADING);
        heading.setForeground(Theme.TEXT_PRIMARY);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        ButtonGroup group = new ButtonGroup();
        JRadioButton easy = radio("Легко — стріляє майже навмання", group, false);
        JRadioButton medium = radio("Середньо — добиває підбитий корабель", group, true);
        JRadioButton hard = radio("Складно — визначає напрямок корабля", group, false);

        JCheckBox salvo = new JCheckBox("Режим «Залп» (пострілів = кількості кораблів)");
        salvo.setFont(Theme.FONT_BODY);
        salvo.setForeground(Theme.TEXT_PRIMARY);
        salvo.setOpaque(false);
        salvo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton start = Theme.styledButton("Почати гру", Theme.ACCENT_DARK);
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.setMaximumSize(new Dimension(220, 46));
        start.addActionListener(e -> {
            Difficulty difficulty = easy.isSelected() ? Difficulty.EASY
                    : hard.isSelected() ? Difficulty.HARD
                    : Difficulty.MEDIUM;
            dialog.dispose();
            dispose();
            new GameWithAI(difficulty, salvo.isSelected());
        });

        panel.add(heading);
        panel.add(Box.createVerticalStrut(12));
        panel.add(easy);
        panel.add(medium);
        panel.add(hard);
        panel.add(Box.createVerticalStrut(16));
        panel.add(salvo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(start);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showPvpSetupDialog() {
        JDialog dialog = new JDialog(this, "Налаштування гри з другом", true);
        dialog.setLayout(new BorderLayout());
        Theme.styleFrame(dialog);
        dialog.setSize(380, 220);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.BG_DARK);
        panel.setBorder(new EmptyBorder(24, 28, 24, 28));

        JCheckBox salvo = new JCheckBox("Режим «Залп» (пострілів = кількості кораблів)");
        salvo.setFont(Theme.FONT_BODY);
        salvo.setForeground(Theme.TEXT_PRIMARY);
        salvo.setOpaque(false);
        salvo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton start = Theme.styledButton("Почати гру", Theme.ACCENT_DARK);
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.setMaximumSize(new Dimension(220, 46));
        start.addActionListener(e -> {
            dialog.dispose();
            dispose();
            new GameWithPlayer(salvo.isSelected());
        });

        panel.add(Box.createVerticalStrut(8));
        panel.add(salvo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(start);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private JRadioButton radio(String text, ButtonGroup group, boolean selected) {
        JRadioButton r = new JRadioButton(text, selected);
        r.setFont(Theme.FONT_BODY);
        r.setForeground(Theme.TEXT_PRIMARY);
        r.setOpaque(false);
        r.setAlignmentX(Component.CENTER_ALIGNMENT);
        group.add(r);
        return r;
    }
}