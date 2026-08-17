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

public class MainMenuPanel extends JPanel {
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

        JLabel modes = sectionLabel("= PLAYER MODES =");

        JButton campaign = makeButton("Campaign", Theme.ACCENT_DARK,
                e -> NavigationManager.get().showCampaign());
        JButton shop = makeButton("Ship shop", Theme.WARNING.darker(),
                e -> NavigationManager.get().showShop());
        JButton stats = makeButton("Statistics", Theme.BG_PANEL_LIGHT,
                e -> NavigationManager.get().showStats());

        JLabel special = sectionLabel("= SPECIAL MODES =");

        JButton timeAttack = makeButton("Time Hunt (5 min)", new Color(220, 100, 100),
                e -> NavigationManager.get().showTimeAttack());
        JButton arena = makeButton("Arena (3 matches)", new Color(100, 150, 220),
                e -> NavigationManager.get().showArena());
        JButton daily = makeButton("Daily Challenge", new Color(200, 150, 50),
                e -> NavigationManager.get().showDailyChallenge());

        JLabel classic = sectionLabel("= CLASSIC MODES =");

        JButton playAI = makeButton("Playing against the computer", Theme.ACCENT_DARK,
                e -> showAiSetupDialog());
        JButton playFriend = makeButton("Playing with a friend", Theme.BG_PANEL_LIGHT,
                e -> showPvpSetupDialog());

        JCheckBox sound = new JCheckBox("Sounds are on");
        sound.setFont(Theme.FONT_BODY);
        sound.setForeground(Theme.TEXT_PRIMARY);
        sound.setOpaque(false);
        sound.setSelected(SoundManager.get().isEnabled());
        sound.addActionListener(e -> SoundManager.get().setEnabled(sound.isSelected()));
        sound.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton exit = makeButton("Exit", new Color(90, 30, 30),
                e -> System.exit(0));

        content.add(title);
        content.add(subtitle);
        content.add(coinsLabel);
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

    private JButton makeButton(String text, Color color, java.awt.event.ActionListener action) {
        JButton btn = Theme.styledButton(text, color);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(340, 46));
        btn.addActionListener(action);
        return btn;
    }

    private void showAiSetupDialog() {
        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Setting up a game against AI",
                true);
        dialog.setLayout(new BorderLayout());
        Theme.styleFrame(dialog);
        dialog.setSize(380, 320);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.BG_DARK);
        panel.setBorder(new EmptyBorder(24, 28, 24, 28));

        JLabel heading = new JLabel("AI difficulty level");
        heading.setFont(Theme.FONT_HEADING);
        heading.setForeground(Theme.TEXT_PRIMARY);
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        ButtonGroup group = new ButtonGroup();
        JRadioButton easy = radio("Easy - shoots almost at random", group, false);
        JRadioButton medium = radio("Medium - Finishes off a downed ship", group, true);
        JRadioButton hard = radio("Difficult - determines the direction of the ship", group, false);

        JCheckBox salvo = new JCheckBox("\"Volley\" mode (shots = number of ships)");
        salvo.setFont(Theme.FONT_BODY);
        salvo.setForeground(Theme.TEXT_PRIMARY);
        salvo.setOpaque(false);
        salvo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton start = Theme.styledButton("Start the game", Theme.ACCENT_DARK);
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.setMaximumSize(new Dimension(220, 46));
        start.addActionListener(e -> {
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

        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void showPvpSetupDialog() {
        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Setting up a game with a friend",
                true);
        dialog.setLayout(new BorderLayout());
        Theme.styleFrame(dialog);
        dialog.setSize(380, 220);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.BG_DARK);
        panel.setBorder(new EmptyBorder(24, 28, 24, 28));

        JCheckBox salvo = new JCheckBox("\"Volley\" mode (shots = number of ships)");
        salvo.setFont(Theme.FONT_BODY);
        salvo.setForeground(Theme.TEXT_PRIMARY);
        salvo.setOpaque(false);
        salvo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton start = Theme.styledButton("Start the game", Theme.ACCENT_DARK);
        start.setAlignmentX(Component.CENTER_ALIGNMENT);
        start.setMaximumSize(new Dimension(220, 46));
        start.addActionListener(e -> {
            dialog.dispose();
            startQuickGameVsPlayer(salvo.isSelected());
        });

        panel.add(Box.createVerticalStrut(8));
        panel.add(salvo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(start);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.setVisible(true);
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