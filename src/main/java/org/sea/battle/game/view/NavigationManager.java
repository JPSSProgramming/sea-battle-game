package org.sea.battle.game.view;

import javax.swing.*;
import java.awt.*;

public class NavigationManager {
    private static final String DYNAMIC_CARD = "DYNAMIC";

    private static NavigationManager instance;

    private JPanel container;
    private CardLayout cardLayout;
    private MainWindow mainWindow;
    private JPanel currentDynamicPanel;

    public static NavigationManager get() {
        if (instance == null) instance = new NavigationManager();
        return instance;
    }

    public void init(MainWindow mainWindow, JPanel container, CardLayout cardLayout) {
        this.mainWindow = mainWindow;
        this.container = container;
        this.cardLayout = cardLayout;
    }

    private void stopCurrentDynamicIfNeeded() {
        if (currentDynamicPanel instanceof Stoppable stoppable) {
            stoppable.stopTimers();
        }
    }

    private void navigateToStatic(String screen) {
        stopCurrentDynamicIfNeeded();
        cardLayout.show(container, screen);
        container.requestFocusInWindow();
    }

    public void showDynamic(JPanel panel) {
        stopCurrentDynamicIfNeeded();
        if (currentDynamicPanel != null) {
            container.remove(currentDynamicPanel);
        }
        currentDynamicPanel = panel;
        container.add(panel, DYNAMIC_CARD);
        cardLayout.show(container, DYNAMIC_CARD);
        container.revalidate();
        container.repaint();
        panel.requestFocusInWindow();
    }

    public void showMainMenu() { navigateToStatic("MAIN_MENU"); }
    public void showCampaign() { navigateToStatic("CAMPAIGN"); }
    public void showShop() { navigateToStatic("SHOP"); }
    public void showStats() { navigateToStatic("STATS"); }
    public void showTimeAttack() { navigateToStatic("TIME_ATTACK"); }
    public void showArena() { navigateToStatic("ARENA"); }
    public void showDailyChallenge() { navigateToStatic("DAILY_CHALLENGE"); }

    public Window getWindow() { return mainWindow; }
}