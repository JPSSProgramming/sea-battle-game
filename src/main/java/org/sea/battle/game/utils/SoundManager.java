package org.sea.battle.game.utils;

import javax.sound.sampled.*;

public class SoundManager {
    private static SoundManager instance;
    private boolean enabled = true;

    public static SoundManager get() {
        if (instance == null) instance = new SoundManager();
        return instance;
    }

    public void setEnabled(boolean v) {
        enabled = v;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void playHit() {
        if (!enabled) return;
        playTone(800, 100);
    }

    public void playMiss() {
        if (!enabled) return;
        playTone(300, 150);
    }

    public void playSunk() {
        if (!enabled) return;
        playTone(600, 80);
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {
        }
        playTone(700, 80);
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {
        }
        playTone(800, 150);
    }

    public void playVictory() {
        if (!enabled) return;
        playTone(800, 100);
        try {
            Thread.sleep(60);
        } catch (InterruptedException ignored) {
        }
        playTone(1000, 100);
        try {
            Thread.sleep(60);
        } catch (InterruptedException ignored) {
        }
        playTone(1200, 200);
    }

    public void playDefeat() {
        if (!enabled) return;
        playTone(400, 100);
        try {
            Thread.sleep(60);
        } catch (InterruptedException ignored) {
        }
        playTone(300, 100);
        try {
            Thread.sleep(60);
        } catch (InterruptedException ignored) {
        }
        playTone(200, 200);
    }

    public void playButtonClick() {
        if (!enabled) return;
        playTone(500, 50);
    }

    private void playTone(int frequency, int durationMs) {
        new Thread(() -> {
            try {
                AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
                int samples = (int) (44100L * durationMs / 1000);
                byte[] data = new byte[samples * 2];
                for (int i = 0; i < samples; i++) {
                    double angle = 2.0 * Math.PI * frequency * i / 44100;
                    short sample = (short) (Short.MAX_VALUE * 0.5 * Math.sin(angle));
                    data[i * 2] = (byte) sample;
                    data[i * 2 + 1] = (byte) (sample >> 8);
                }
                SourceDataLine line = AudioSystem.getSourceDataLine(format);
                line.open(format);
                line.start();
                line.write(data, 0, data.length);
                line.drain();
                line.close();
            } catch (Exception ignored) {
            }
        }).start();
    }
}