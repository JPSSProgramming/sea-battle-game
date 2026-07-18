package org.sea.battle.game.model;

import java.util.List;

public class LevelCatalog {

    public static final List<Level> LEVELS = List.of(
            new Level(1, "Розвідка",
                    "Слабкий супротивник, ідеально для розминки.",
                    Difficulty.EASY, false, false, 0, 50),

            new Level(2, "Прибережний бій",
                    "Той самий супротивник, але тепер стріляє одразу залпом.",
                    Difficulty.EASY, true, false, 0, 60),

            new Level(3, "Ворожий флот",
                    "Супротивник розумніший — добиває підбитий корабель.",
                    Difficulty.MEDIUM, false, false, 0, 80),

            new Level(4, "Штормовий залп",
                    "Середній рівень складності та залповий вогонь одночасно.",
                    Difficulty.MEDIUM, true, false, 0, 100),

            new Level(5, "Нерівні сили",
                    "У вас на 2 катери менше, ніж зазвичай — доведеться грати обережніше.",
                    Difficulty.MEDIUM, false, false, 2, 130),

            new Level(6, "Досвідчений супротивник",
                    "ШІ вираховує напрямок корабля вже після другого влучання.",
                    Difficulty.HARD, false, false, 0, 160),

            new Level(7, "Блискавичний залп",
                    "Складний ШІ, що ще й стріляє одразу кількома снарядами за хід.",
                    Difficulty.HARD, true, false, 0, 200),

            new Level(8, "Флагман ворога",
                    "У супротивника є додатковий важкий корабель. Фінальний бій кампанії.",
                    Difficulty.HARD, false, true, 0, 280)
    );
}