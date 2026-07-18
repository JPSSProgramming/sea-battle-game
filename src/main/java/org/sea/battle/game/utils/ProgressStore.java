package org.sea.battle.game.utils;

import java.io.*;
import java.util.*;

public class ProgressStore {
    private static final String DIR = System.getProperty("user.home") + File.separator + ".seabattle";
    private static final String FILE = DIR + File.separator + "progress.properties";

    private static ProgressStore instance;

    private int coins;
    private int maxLevelUnlocked;
    private final Set<String> ownedSkins = new HashSet<>();
    private String selectedSkin;

    public static ProgressStore get() {
        if (instance == null) {
            instance = new ProgressStore();
            instance.load();
        }
        return instance;
    }

    private ProgressStore() {
        coins = 0;
        maxLevelUnlocked = 1;
        selectedSkin = "CLASSIC";
        ownedSkins.add("CLASSIC");
    }

    public int getCoins() { return coins; }

    public void addCoins(int amount) {
        coins += amount;
        save();
    }

    public boolean spendCoins(int amount) {
        if (coins < amount) return false;
        coins -= amount;
        save();
        return true;
    }

    public int getMaxLevelUnlocked() { return maxLevelUnlocked; }

    public void unlockLevel(int levelIndex) {
        if (levelIndex > maxLevelUnlocked) {
            maxLevelUnlocked = levelIndex;
            save();
        }
    }

    public boolean ownsSkin(String id) { return ownedSkins.contains(id); }

    public void buySkin(String id) {
        ownedSkins.add(id);
        save();
    }

    public String getSelectedSkin() { return selectedSkin; }

    public void selectSkin(String id) {
        selectedSkin = id;
        save();
    }

    private void load() {
        File f = new File(FILE);
        if (!f.exists()) return;
        Properties p = new Properties();
        try (InputStream in = new FileInputStream(f)) {
            p.load(in);
            coins = Integer.parseInt(p.getProperty("coins", "0"));
            maxLevelUnlocked = Integer.parseInt(p.getProperty("maxLevelUnlocked", "1"));
            selectedSkin = p.getProperty("selectedSkin", "CLASSIC");
            String owned = p.getProperty("ownedSkins", "CLASSIC");
            ownedSkins.clear();
            ownedSkins.addAll(Arrays.asList(owned.split(",")));
        } catch (IOException | NumberFormatException ignored) {
        }
    }

    private void save() {
        new File(DIR).mkdirs();
        Properties p = new Properties();
        p.setProperty("coins", String.valueOf(coins));
        p.setProperty("maxLevelUnlocked", String.valueOf(maxLevelUnlocked));
        p.setProperty("selectedSkin", selectedSkin);
        p.setProperty("ownedSkins", String.join(",", ownedSkins));
        try (OutputStream out = new FileOutputStream(FILE)) {
            p.store(out, "Sea Battle progress");
        } catch (IOException ignored) {
        }
    }
}