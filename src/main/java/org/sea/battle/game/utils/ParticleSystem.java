package org.sea.battle.game.utils;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class ParticleSystem {
    public static class Particle {
        public double x, y;
        public double vx, vy;
        public Color color;
        public int life; // 0-255
        public int maxLife;

        public Particle(double x, double y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.life = 255;
            this.maxLife = 255;
            Random r = new Random();
            this.vx = (r.nextDouble() - 0.5) * 4;
            this.vy = (r.nextDouble() - 0.5) * 4;
        }

        public void update() {
            x += vx;
            y += vy;
            vy += 0.1;
            life -= 5;
        }

        public void draw(Graphics2D g) {
            int alpha = (int) (255 * life / (double) maxLife);
            g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
            g.fillOval((int) x, (int) y, 4, 4);
        }

        public boolean isAlive() { return life > 0; }
    }

    private List<Particle> particles = new ArrayList<>();

    public void burst(int x, int y, int count, Color color) {
        for (int i = 0; i < count; i++) {
            particles.add(new Particle(x + Math.random() * 20 - 10, y + Math.random() * 20 - 10, color));
        }
    }

    public void update() {
        particles.removeIf(p -> !p.isAlive());
        for (Particle p : particles) p.update();
    }

    public void draw(Graphics2D g) {
        for (Particle p : particles) p.draw(g);
    }

    public boolean hasParticles() { return !particles.isEmpty(); }
}