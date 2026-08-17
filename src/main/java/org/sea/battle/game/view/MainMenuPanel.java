package org.sea.battle.game.view;

import org.sea.battle.game.model.AI;
import org.sea.battle.game.model.Difficulty;
import org.sea.battle.game.model.GameLogic;
import org.sea.battle.game.model.Player;
import org.sea.battle.game.utils.ProgressStore;
import org.sea.battle.game.utils.SoundManager;
import org.sea.battle.game.utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;

public class MainMenuPanel extends JPanel {
    private static final Dimension DEFAULT_BTN_SIZE = new Dimension(340, 46);
    private static final Dimension DIALOG_BTN_SIZE = new Dimension(220, 46);

    private JLabel coinsLabel;

    public MainMenuPanel() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_DARK);
        rebuild();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        refreshCoins();
    }

    private void refreshCoins() {
        if (coinsLabel != null) {
            coinsLabel.setText("Coins: " + ProgressStore.get().getCoins());
        }
    }

    private void rebuild() {
        removeAll();

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Theme.BG_DARK);
        content.setBorder(new EmptyBorder(28, 36, 28, 36));

        JLabel title = Theme.titleLabel("SEA BATTLE");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Classic strategy game", SwingConstants.CENTER);
        subtitle.setFont(Theme.FONT_BODY);
        subtitle.setForeground(Theme.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        coinsLabel = new JLabel("Coins: " + ProgressStore.get().getCoins(), SwingConstants.CENTER);
        coinsLabel.setFont(Theme.FONT_HEADING);
        coinsLabel.setForeground(Theme.WARNING);
        coinsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        coinsLabel.setBorder(new EmptyBorder(6, 0, 20, 0));

        JCheckBox sound = checkbox("Sounds are on", SoundManager.get().isEnabled());
        sound.addActionListener(e -> SoundManager.get().setEnabled(sound.isSelected()));

        content.add(title);
        content.add(subtitle);
        content.add(coinsLabel);

        // Player Modes
        content.add(sectionLabel("= PLAYER MODES ="));
        addWithStrut(content, makeButton("Campaign", Theme.ACCENT_DARK, e -> NavigationManager.get().showCampaign()), 8);
        addWithStrut(content, makeButton("Ship shop", Theme.WARNING.darker(), e -> NavigationManager.get().showShop()), 8);
        content.add(makeButton("Statistics", Theme.BG_PANEL_LIGHT, e -> NavigationManager.get().showStats()));

        // Special Modes
        content.add(sectionLabel("= SPECIAL MODES ="));
        addWithStrut(content, makeButton("Time Hunt (5 min)", new Color(220, 100, 100), e -> NavigationManager.get().showTimeAttack()), 8);
        addWithStrut(content, makeButton("Arena (3 matches)", new Color(100, 150, 220), e -> NavigationManager.get().showArena()), 8);
        content.add(makeButton("Daily Challenge", new Color(200, 150, 50), e -> NavigationManager.get().showDailyChallenge()));

        // Classic Modes
        content.add(sectionLabel("= CLASSIC MODES ="));
        addWithStrut(content, makeButton("Playing against the computer", Theme.ACCENT_DARK, e -> showAiSetupDialog()), 8);
        content.add(makeButton("Playing with a friend", Theme.BG_PANEL_LIGHT, e -> showPvpSetupDialog()));

        content.add(Box.createVerticalStrut(12));
        addWithStrut(content, sound, 8);
        content.add(makeButton("Exit", new Color(90, 30, 30), e -> System.exit(0)));

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG_DARK);
        add(scroll, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_BODY);
        l.setForeground(Theme.TEXT_MUTED);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        l.setBorder(new EmptyBorder(16, 0, 12, 0));
        return l;
    }

    private JButton makeButton(String text, Color color, ActionListener action) {
        return makeButton(text, color, DEFAULT_BTN_SIZE, action);
    }

    private JButton makeButton(String text, Color color, Dimension maxDimension, ActionListener action) {
        JButton btn = Theme.styledButton(text, color);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(maxDimension);
        btn.addActionListener(action);
        return btn;
    }

    private JCheckBox checkbox(String text, boolean selected) {
        JCheckBox cb = new JCheckBox(text, selected);
        cb.setFont(Theme.FONT_BODY);
        cb.setForeground(Theme.TEXT_PRIMARY);
        cb.setOpaque(false);
        cb.setAlignmentX(Component.CENTER_ALIGNMENT);
        return cb;
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

    private void addWithStrut(JPanel panel, Component comp, int strutSize) {
        panel.add(comp);
        panel.add(Box.createVerticalStrut(strutSize));
    }

    private void showSetupDialog(String title, int width, int height, BiConsumer<JDialog, JPanel> dialogBuilder) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setLayout(new BorderLayout());
        Theme.styleFrame(dialog);
        dialog.setSize(width, height);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.BG_DARK);
        panel.setBorder(new EmptyBorder(24, 28, 24, 28));

        dialogBuilder.accept(dialog, panel);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showAiSetupDialog() {
        showSetupDialog("Setting up a game against AI", 380, 320, (dialog, panel) -> {
            JLabel heading = new JLabel("AI difficulty level");
            heading.setFont(Theme.FONT_HEADING);
            heading.setForeground(Theme.TEXT_PRIMARY);
            heading.setAlignmentX(Component.CENTER_ALIGNMENT);

            ButtonGroup group = new ButtonGroup();
            JRadioButton easy = radio("Easy - shoots almost at random", group, false);
            JRadioButton medium = radio("Medium - Finishes off a downed ship", group, true);
            JRadioButton hard = radio("Difficult - determines the direction of the ship", group, false);

            JCheckBox salvo = checkbox("\"Volley\" mode (shots = number of ships)", false);

            JButton start = makeButton("Start the game", Theme.ACCENT_DARK, DIALOG_BTN_SIZE, e -> {
                Difficulty difficulty = easy.isSelected() ? Difficulty.EASY
                        : hard.isSelected() ? Difficulty.HARD
                        : Difficulty.MEDIUM;
                dialog.dispose();
                startQuickGameVsAI(difficulty, salvo.isSelected());
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
        });
    }

    private void showPvpSetupDialog() {
        showSetupDialog("Setting up a game with a friend", 380, 220, (dialog, panel) -> {
            JCheckBox salvo = checkbox("\"Volley\" mode (shots = number of ships)", false);

            JButton start = makeButton("Start the game", Theme.ACCENT_DARK, DIALOG_BTN_SIZE, e -> {
                dialog.dispose();
                startQuickGameVsPlayer(salvo.isSelected());
            });

            panel.add(Box.createVerticalStrut(8));
            panel.add(salvo);
            panel.add(Box.createVerticalStrut(20));
            panel.add(start);
        });
    }

    private void startQuickGameVsAI(Difficulty difficulty, boolean salvo) {
        Player human = new Player("Player");
        AI ai = new AI("Computer (" + difficulty + ")", difficulty);
        ai.autoPlaceShips();

        ShipPlacementPanel placement = new ShipPlacementPanel(human, () -> {
            GameLogic logic = new GameLogic(human, ai, salvo);
            NavigationManager.get().showDynamic(new GamePanel(logic, true));
        });
        NavigationManager.get().showDynamic(placement);
    }

    private void startQuickGameVsPlayer(boolean salvo) {
        Player p1 = new Player("Player 1");
        Player p2 = new Player("Player 2");

        ShipPlacementPanel placement1 = new ShipPlacementPanel(p1, () -> {
            ShipPlacementPanel placement2 = new ShipPlacementPanel(p2, () -> {
                GameLogic logic = new GameLogic(p1, p2, salvo);
                NavigationManager.get().showDynamic(new GamePanel(logic, false));
            });
            NavigationManager.get().showDynamic(placement2);
        });
        NavigationManager.get().showDynamic(placement1);
    }
}