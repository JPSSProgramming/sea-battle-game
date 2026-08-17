package org.sea.battle.game.utils;

import java.io.*;
import java.util.*;

public class GameStats {
    private static GameStats instance;

    private int totalGames;
    private int totalWins;
    private int totalShipsSunk;
    private int longestWinStreak;
    private int currentWinStreak;
    private long totalPlayTimeSeconds;
    private int accuracy;

    private static final String STATS_FILE = System.getProperty("user.home") + File.separator
            + ".seabattle" + File.separator + "stats.properties";

    public static GameStats get() {
        if (instance == null) {
            instance = new GameStats();
            instance.load();
        }
        return instance;
    }

    private GameStats() {
        totalGames = 0;
        totalWins = 0;
        totalShipsSunk = 0;
        longestWinStreak = 0;
        currentWinStreak = 0;
        totalPlayTimeSeconds = 0;
        accuracy = 0;
    }

    public void recordGame(boolean won, int shipsSunk, int accuracyValue) {
        totalGames++;
        totalShipsSunk += shipsSunk;
        this.accuracy = (this.accuracy + accuracyValue) / 2;
        if (won) {
            totalWins++;
            currentWinStreak++;
            if (currentWinStreak > longestWinStreak) {
                longestWinStreak = currentWinStreak;
            }
        } else {
            currentWinStreak = 0;
        }
        save();
    }

    public void addPlayTime(long seconds) {
        totalPlayTimeSeconds += seconds;
        save();
    }

    public int getTotalGames() {
        return totalGames;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getTotalShipsSunk() {
        return totalShipsSunk;
    }

    public int getLongestWinStreak() {
        return longestWinStreak;
    }

    public int getCurrentWinStreak() {
        return currentWinStreak;
    }

    public long getTotalPlayTimeSeconds() {
        return totalPlayTimeSeconds;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public double getWinRate() {
        return totalGames == 0 ? 0 : (double) totalWins * 100 / totalGames;
    }

    private void load() {
        File f = new File(STATS_FILE);
        if (!f.exists()) return;
        Properties p = new Properties();
        try (InputStream in = new FileInputStream(f)) {
            p.load(in);
            totalGames = Integer.parseInt(p.getProperty("totalGames", "0"));
            totalWins = Integer.parseInt(p.getProperty("totalWins", "0"));
            totalShipsSunk = Integer.parseInt(p.getProperty("totalShipsSunk", "0"));
            longestWinStreak = Integer.parseInt(p.getProperty("longestWinStreak", "0"));
            currentWinStreak = Integer.parseInt(p.getProperty("currentWinStreak", "0"));
            totalPlayTimeSeconds = Long.parseLong(p.getProperty("totalPlayTimeSeconds", "0"));
            accuracy = Integer.parseInt(p.getProperty("accuracy", "0"));
        } catch (IOException | NumberFormatException ignored) {
        }
    }

    private void save() {
        new File(STATS_FILE).getParentFile().mkdirs();
        Properties p = new Properties();
        p.setProperty("totalGames", String.valueOf(totalGames));
        p.setProperty("totalWins", String.valueOf(totalWins));
        p.setProperty("totalShipsSunk", String.valueOf(totalShipsSunk));
        p.setProperty("longestWinStreak", String.valueOf(longestWinStreak));
        p.setProperty("currentWinStreak", String.valueOf(currentWinStreak));
        p.setProperty("totalPlayTimeSeconds", String.valueOf(totalPlayTimeSeconds));
        p.setProperty("accuracy", String.valueOf(accuracy));
        try (OutputStream out = new FileOutputStream(STATS_FILE)) {
            p.store(out, "Sea Battle Stats");
        } catch (IOException ignored) {
        }
    }
}