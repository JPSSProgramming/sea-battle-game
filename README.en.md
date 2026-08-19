<!-- HEADER AND BADGES -->
<div align="center">

<h1>
SEA-BATTLE-GAME</h1>
<p>Next-generation naval combat — Java Swing, campaign, store, statistics and custom AI</p>

<p>
<img src="https://img.shields.io/badge/License-MIT-green.svg" alt="License">
<img src="https://img.shields.io/badge/Java-21+-blue.svg" alt="Java Version">
<img src="https://img.shields.io/badge/GUI-Java%20Swing-orange.svg" alt="Java Swing">
<img src="https://img.shields.io/badge/Platform-Windows%20%7C%20macOS%20%7C%20Linux-lightgrey.svg" alt="Platform">
<img src="https://img.shields.io/badge/status-completed-success.svg" alt="Status">
</p>

</div>

---

## 🎯 About the project

The classic board game "Sea Battle", reimagined as a full-fledged desktop application: an 8-level campaign, a coin-based cosmetics store, three levels of AI difficulty (including one that "calculates" the direction of your ship), and four additional modes for those who have already completed everything else.

The entire game is **one full-screen window**. No separate pop-ups when switching between menus, campaign or combat - everything switches instantly and smoothly.

Language used in the game - English (for convenience)

---

## ✨ Main features

* 8-level campaign - increasing difficulty, handicaps for the player, enemy flagship bosses and rewards in coins for passing.
* Smart AI of three levels - from pure random to an algorithm that after two hits calculates the direction of the ship and "fires along the line".
* "Volley" mode - the number of shots per turn is equal to the number of ships left in the fleet.
* Skin store - 6 ship color options, purchased with campaign coins, are applied immediately in all modes.
* 5 game modes - classic vs. AI/friend, campaign, time hunt (5 min), arena (3 battles in a row without defeats), daily challenge.
* Personal statistics - victories, winning streaks, accuracy, total time in the game.
* Full screen mode in one window — all screens on CardLayout, without opening new JFrames.
* Animations and particles — flash and scatter of particles when sinking a ship, programmatically synthesized sound effects without external audio files.
* ️ Drag & Drop arrangement — drag and rotate ships with the mouse when preparing for battle.

---

## 🛠 Technology stack

| Category | Technologies |
| :--- | :--- |
| **Language** | Java 21 (records, pattern matching in `switch`) |
| **GUI** | Java Swing (custom theme, custom component rendering) |
| **Sound** | `javax.sound.sampled` — tones are synthesized programmatically at runtime |
| **Data storage** | Local `.properties` files (without database) |
| **Tools** | Git, IntelliJ IDEA / Eclipse, `javac` |

> The project is deliberately without a backend and without a database — it is an offline game, all progress is saved locally on the player's disk.

---

## 🎮 Game modes

| Mode | Description |
| :--- | :--- |
| **Play against the computer** | Classic 1x1 battle, 3 AI difficulty levels to choose from |
| **Play with a friend** | Local PvP on one device, fleets are placed alternately |
| **Campaign** | 8 levels with unique conditions: handicap, salvo, boss flagship |
| **Time Hunt** | 5 minutes — destroy the maximum number of enemy ships |
| **Arena** | 3 fights in a row without a single defeat — risk for reward |
| **Daily Challenge** | One battle per day with fixed conditions |

### AI Difficulty Levels

| Level | Behavior |
| :--- | :--- |
| 🟢 Easy | Shoots completely randomly across the board |
| 🟡 Medium | Paired cells + kills neighboring cells after hitting |
| 🔴 Hard | After 2 hits, calculates direction and fires along the ship's line |

---

## 🚀 Quick Start

Run these commands in the terminal to run the project locally:

### 1. Clone the repository
```bash
git clone https://github.com/JPSSProgramming/sea-battle-game.git
cd sea-battle-game
```

### 2. Compile
```bash
javac -encoding UTF-8 -d out $(find src -name "*.java")
```

### 3. Run
```bash
java -cp out org.sea.battle.game.Main
```

> Requires **JDK 21+**. Check version: `java -version`.

### Via IDE
Open the folder as a Java project in IntelliJ IDEA / Eclipse, make sure JDK 21 is selected, and run `org.sea.battle.game.Main`.

---

## ⌨️ Management

| Action | Key / Mouse Action |
| :--- | :--- |
| Select / Place Ship | LMB on Reserve → LMB on Board |
| Rotate Ship Before Placement | `R` |
| Rotate Already Placed Ship | RMB on Ship |
| Move Placed Ship | Hold LMB and Drag |
| Shot at Opponent | LMB on Enemy Board Cell |
| Exit to Main Menu from Any Screen | `Esc` |

---

## 🗂️ Project structure

```
src/org/sea/battle/game/
├── Main.java
├── controller/ # Drag&Drop, ship rotation
├── model/ # AI, GameLogic, Player, Ship, Level, ...
├── utils/ # Theme, ProgressStore, SoundManager, ParticleSystem, ...
└── view/ # MainWindow, NavigationManager, all screens-panels
```
A detailed description of the architecture (single window + `CardLayout` + `NavigationManager`) is in the repository Wiki.

---

## 💾 Saving progress

Progress is saved locally, without cloud and accounts:

```
~/.seabattle/progress.properties # coins, unlocked levels, skins
~/.seabattle/stats.properties # game statistics
```

---

## 📄 License

Distributed under the MIT license. Details in the `LICENSE` file.