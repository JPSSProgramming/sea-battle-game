package org.sea.battle.game.utils;

import java.awt.Color;

public enum ShipSkin {
    CLASSIC("CLASSIC", "Класичний", 0, new Color(70, 80, 96), new Color(210, 215, 225)),
    STEEL("STEEL", "Сталевий", 80, new Color(140, 150, 165), new Color(230, 235, 240)),
    STEALTH("STEALTH", "Стелс", 150, new Color(20, 20, 25), new Color(90, 200, 190)),
    NEON("NEON", "Неоновий", 200, new Color(20, 40, 60), new Color(60, 240, 220)),
    GOLD("GOLD", "Золотий", 250, new Color(180, 140, 30), new Color(255, 225, 120)),
    CRIMSON("CRIMSON", "Багряний флот", 300, new Color(120, 20, 30), new Color(255, 120, 120));

    public final String id;
    public final String label;
    public final int price;
    public final Color fill;
    public final Color border;

    ShipSkin(String id, String label, int price, Color fill, Color border) {
        this.id = id;
        this.label = label;
        this.price = price;
        this.fill = fill;
        this.border = border;
    }

    public static ShipSkin byId(String id) {
        for (ShipSkin s : values()) {
            if (s.id.equals(id)) return s;
        }
        return CLASSIC;
    }
}