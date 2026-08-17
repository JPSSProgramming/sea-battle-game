package org.sea.battle.game.utils;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ParticleSystem {

    private static class Particle {
        double x, y, vx, vy;
        Color color;
        int life = 255;
        final int maxLife = 255;

        Particle(double x, double y, Color color, Random r) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.vx = (r.nextDouble() - 0.5) * 4;
            this.vy = (r.nextDouble() - 0.5) * 4 - 1;
        }

        void update() {
            x += vx;
            y += vy;
            vy += 0.12;
            life -= 6;
        }

        void draw(Graphics2D g) {
            int alpha = Math.max(0, Math.min(255, 255 * life / maxLife));
            g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
            g.fillOval((int) x, (int) y, 5, 5);
        }

        boolean isAlive() {
            return life > 0;
        }
    }

    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();

    public void burst(int x, int y, int count, Color color) {
        for (int i = 0; i < count; i++) {
            particles.add(new Particle(
                    x + random.nextDouble() * 24 - 12,
                    y + random.nextDouble() * 24 - 12,
                    color, random));
        }
    }

    public void update() {
        particles.removeIf(p -> !p.isAlive());
        for (Particle p : particles) p.update();
    }

    public void draw(Graphics2D g) {
        for (Particle p : particles) p.draw(g);
    }

    public boolean hasParticles() {
        return !particles.isEmpty();
    }
}