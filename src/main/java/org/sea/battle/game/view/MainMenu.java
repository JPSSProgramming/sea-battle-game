package org.sea.battle.game.view;

import org.sea.battle.game.controller.GameWithAI;
import org.sea.battle.game.controller.GameWithPlayer;
import org.sea.battle.game.model.Difficulty;
import org.sea.battle.game.utils.ProgressStore;
import org.sea.battle.game.utils.SoundManager;
import org.sea.battle.game.utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Морський бій");
        setSize(500, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        Theme.styleFrame(this);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_DARK);
        content.setBorder(new EmptyBorder(28, 36, 28, 36));

        JLabel title = Theme.titleLabel("МОРСЬКИЙ БІЙ");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Класична стратегічна гра", SwingConstants.CENTER);
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel coins = new JLabel("Монети: " + ProgressStore.get().getCoins(), SwingConstants.CENTER);
        coins.setFont(Theme.FONT_HEADING);
        coins.setForeground(Theme.WARNING);
        coins.setAlignmentX(Component.CENTER_ALIGNMENT);
        coins.setBorder(new EmptyBorder(6, 0, 20, 0));


        JLabel modes = new JLabel("= РЕЖИМИ ГРАВЦІВ =");
        modes.setFont(Theme.FONT_BODY);
        modes.setForeground(Theme.TEXT_MUTED);
        modes.setAlignmentX(Component.CENTER_ALIGNMENT);
        modes.setBorder(new EmptyBorder(0, 0, 12, 0));

        JButton campaign = makeButton("Кампанія", Theme.ACCENT_DARK,
                e -> {
                    dispose();
                    new CampaignScreen();
                });
        JButton shop = makeButton("Магазин кораблів", Theme.WARNING.darker(),
                e -> {
                    dispose();
                    new ShopScreen();
                });
        JButton stats = makeButton("Статистика", Theme.BG_PANEL_LIGHT,
                e -> {
                    dispose();
                    new StatsScreen();
                });

        JLabel special = new JLabel("= СПЕЦІАЛЬНІ РЕЖИМИ =");
        special.setFont(Theme.FONT_BODY);
        special.setForeground(Theme.TEXT_MUTED);
        special.setAlignmentX(Component.CENTER_ALIGNMENT);
        special.setBorder(new EmptyBorder(16, 0, 12, 0));

        JButton timeAttack = makeButton("Охота на час (5 хв)", new Color(220, 100, 100),
                e -> {
                    dispose();
                    new TimeAttackScreen();
                });
        JButton arena = makeButton("Арена (3 поєдинки)", new Color(100, 150, 220),
                e -> {
                    dispose();
                    new ArenaScreen();
                });
        JButton daily = makeButton("Щоденний виклик", new Color(200, 150, 50),
                e -> {
                    dispose();
                    new DailyChallengeScreen();
                });

        JLabel classic = new JLabel("= КЛАСИЧНІ РЕЖИМИ =");
        classic.setFont(Theme.FONT_BODY);
        classic.setForeground(Theme.TEXT_MUTED);
        classic.setAlignmentX(Component.CENTER_ALIGNMENT);
        classic.setBorder(new EmptyBorder(16, 0, 12, 0));

        JButton playAI = makeButton("Гра проти комп'ютера", Theme.ACCENT_DARK,
                e -> showAiSetupDialog());
        JButton playFriend = makeButton("Гра з другом", Theme.BG_PANEL_LIGHT,
                e -> showPvpSetupDialog());

        JCheckBox sound = new JCheckBox("Звуки вмикнені");
        sound.setFont(Theme.FONT_BODY);
        sound.setForeground(Theme.TEXT_PRIMARY);
        sound.setOpaque(false);
        sound.setSelected(SoundManager.get().isEnabled());
        sound.addActionListener(e -> SoundManager.get().setEnabled(sound.isSelected()));
        sound.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton exit = makeButton("Вихід", new Color(90, 30, 30),
                e -> System.exit(0));

        content.add(title);
        content.add(subtitle);
        content.add(coins);
        content.add(modes);
        content.add(campaign);
        content.add(Box.createVerticalStrut(8));
        content.add(shop);
        content.add(Box.createVerticalStrut(8));
        content.add(stats);
        content.add(special);
        content.add(timeAttack);
        content.add(Box.createVerticalStrut(8));
        content.add(arena);
        content.add(Box.createVerticalStrut(8));
        content.add(daily);
        content.add(classic);
        content.add(playAI);
        content.add(Box.createVerticalStrut(8));
        content.add(playFriend);
        content.add(Box.createVerticalStrut(12));
        content.add(sound);
        content.add(Box.createVerticalStrut(8));
        content.add(exit);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG_DARK);
        add(scroll, BorderLayout.CENTER);
        setVisible(true);
    }


    private JButton makeButton(String text, Color color, ActionListener action) {
        JButton btn = Theme.styledButton(text, color);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(340, 46));
        btn.addActionListener(action);
        return btn;
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
        JRadioButton easy = radio("Легко - стріляє майже навмання", group, false);
        JRadioButton medium = radio("Середньо - добиває підбитий корабель", group, true);
        JRadioButton hard = radio("Складно - визначає напрямок корабля", group, false);

        JCheckBox salvo = new JCheckBox("Режим Залп (пострілів = кількості кораблів)");
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

        JCheckBox salvo = new JCheckBox("Режим Залп (пострілів = кількості кораблів)");
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