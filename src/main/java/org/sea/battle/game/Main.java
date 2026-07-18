package org.sea.battle.game;

import org.sea.battle.game.view.MainMenu;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainMenu::new);
    }
}
