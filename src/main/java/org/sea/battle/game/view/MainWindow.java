package org.sea.battle.game.view;

import org.sea.battle.game.utils.Theme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Морський бій");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        setUndecorated(true);
        setLayout(new BorderLayout());
        Theme.styleFrame(this);
        if (device.isFullScreenSupported()) {
            device.setFullScreenWindow(this);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        CardLayout cardLayout = new CardLayout();
        JPanel container = new JPanel(cardLayout);
        container.setBackground(Theme.BG_DARK);

        NavigationManager.get().init(this, container, cardLayout);

        container.add(new MainMenuPanel(), "MAIN_MENU");
        container.add(new CampaignScreenPanel(), "CAMPAIGN");
        container.add(new ShopScreenPanel(), "SHOP");
        container.add(new StatsScreenPanel(), "STATS");
        container.add(new TimeAttackScreenPanel(), "TIME_ATTACK");
        container.add(new ArenaScreenPanel(), "ARENA");
        container.add(new DailyChallengeScreenPanel(), "DAILY_CHALLENGE");

        add(container, BorderLayout.CENTER);

        getRootPane().registerKeyboardAction(
                e -> NavigationManager.get().showMainMenu(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        NavigationManager.get().showMainMenu();
        setVisible(true);
    }
}