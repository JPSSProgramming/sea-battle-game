package org.sea.battle.game.view;

import org.sea.battle.game.controller.DragAndDropManager;
import org.sea.battle.game.controller.ShipRotationHandler;
import org.sea.battle.game.model.*;
import org.sea.battle.game.utils.Theme;
import org.sea.battle.game.utils.Utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ShipPlacementScreen extends JFrame {
    private final Player player;
    private final GameBoard board;
    private final List<Ship> placedShips;
    private final JPanel reservePanel;
    private final JLabel instr;

    private final Map<Integer, Integer> remainingBySize = new LinkedHashMap<>();
    private final Random random = new Random();
    private final int fleetSize;

    private int currentSize = -1;
    private boolean placingHorizontal = true;

    public ShipPlacementScreen(Player player, Runnable onFinished) {
        this(player, Utils.SHIP_SIZES, onFinished);
    }

    public ShipPlacementScreen(Player player, int[] fleet, Runnable onFinished) {
        super("Розстановка — " + player.getName());
        this.player = player;
        this.board = player.getBoard();
        this.placedShips = new ArrayList<>();
        this.fleetSize = fleet.length;

        for (int sz : fleet) {
            remainingBySize.merge(sz, 1, Integer::sum);
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        Theme.styleFrame(this);
        getContentPane().setBackground(Theme.BG_DARK);

        reservePanel = new JPanel();
        reservePanel.setLayout(new BoxLayout(reservePanel, BoxLayout.Y_AXIS));
        reservePanel.setBackground(Theme.BG_PANEL);
        reservePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(8, 8, 8, 8), "Резерв кораблів"));
        rebuildReservePanel();

        JPanel boardWrap = new JPanel(new GridBagLayout());
        boardWrap.setBackground(Theme.BG_DARK);
        boardWrap.setBorder(new EmptyBorder(16, 16, 16, 16));
        boardWrap.add(board);

        add(reservePanel, BorderLayout.WEST);
        add(boardWrap, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setLayout(new BoxLayout(south, BoxLayout.Y_AXIS));
        south.setBackground(Theme.BG_PANEL);
        south.setBorder(new EmptyBorder(10, 10, 10, 10));

        instr = new JLabel("", SwingConstants.CENTER);
        instr.setFont(Theme.FONT_BODY);
        instr.setForeground(Theme.TEXT_PRIMARY);
        instr.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateInstructions();

        JLabel help = new JLabel(
                "R — повернути перед постановкою · Клік ПКМ по кораблю — повернути · Перетягніть корабель мишею, щоб переставити",
                SwingConstants.CENTER);
        help.setFont(Theme.FONT_MONO);
        help.setForeground(Theme.TEXT_MUTED);
        help.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton randomize = Theme.styledButton("Розставити випадково", Theme.BG_PANEL_LIGHT);
        randomize.setAlignmentX(Component.CENTER_ALIGNMENT);
        randomize.addActionListener(e -> randomizeRemaining(onFinished));

        south.add(instr);
        south.add(Box.createVerticalStrut(4));
        south.add(help);
        south.add(Box.createVerticalStrut(8));
        south.add(randomize);
        add(south, BorderLayout.SOUTH);

        new DragAndDropManager(board, placedShips);
        ShipRotationHandler rotationHandler = new ShipRotationHandler(board);

        board.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (currentSize <= 0) return;
                int gx = e.getX() / Utils.CELL_SIZE;
                int gy = e.getY() / Utils.CELL_SIZE;
                List<Cell> cand = candidateCellsForPlacement(gx, gy, currentSize, placingHorizontal);
                board.setPreviewCells(cand);
            }
        });

        board.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int gx = e.getX() / Utils.CELL_SIZE;
                    int gy = e.getY() / Utils.CELL_SIZE;
                    Ship existing = board.findShipAt(gx, gy);
                    if (existing != null) rotationHandler.rotate(existing);
                    return;
                }

                if (currentSize <= 0) return;
                int gx = e.getX() / Utils.CELL_SIZE;
                int gy = e.getY() / Utils.CELL_SIZE;
                List<Cell> cand = candidateCellsForPlacement(gx, gy, currentSize, placingHorizontal);

                if (cand != null && ShipPlacementValidator.canPlaceShip(board, cand)) {
                    Ship s = new Ship(new ArrayList<>(cand));
                    board.addShip(s);
                    player.addShip(s);
                    placedShips.add(s);

                    consumeOneFromReserve(currentSize);
                    currentSize = -1;
                    board.clearPreview();
                    updateInstructions();

                    if (isAllPlaced()) {
                        finish(onFinished);
                    }
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    placingHorizontal = !placingHorizontal;
                    board.repaint();
                }
            }
        });

        setFocusable(true);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void rebuildReservePanel() {
        reservePanel.removeAll();
        for (Map.Entry<Integer, Integer> entry : remainingBySize.entrySet()) {
            int size = entry.getKey();
            int count = entry.getValue();
            if (count <= 0) continue;
            reservePanel.add(createReserveShipComponent(size, count));
            reservePanel.add(Box.createVerticalStrut(6));
        }
        reservePanel.revalidate();
        reservePanel.repaint();
    }

    private JPanel createReserveShipComponent(int size, int count) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(200, Utils.CELL_SIZE + 16));

        JPanel ship = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(currentSize == size ? Theme.ACCENT : Theme.SHIP_COLOR);
                g2.fillRoundRect(2, 2, size * (Utils.CELL_SIZE - 4) - 4, Utils.CELL_SIZE - 8, 8, 8);
                g2.dispose();
            }
        };
        ship.setOpaque(false);
        ship.setPreferredSize(new Dimension(size * (Utils.CELL_SIZE - 4), Utils.CELL_SIZE));
        ship.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ship.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentSize = size;
                board.repaint();
                reservePanel.repaint();
                updateInstructions();
            }
        });

        JLabel label = new JLabel(Utils.shipTypeName(size) + " x" + count);
        label.setFont(Theme.FONT_BODY);
        label.setForeground(Theme.TEXT_PRIMARY);

        row.add(ship, BorderLayout.WEST);
        row.add(label, BorderLayout.CENTER);
        return row;
    }

    private void consumeOneFromReserve(int size) {
        remainingBySize.merge(size, -1, Integer::sum);
        rebuildReservePanel();
    }

    private boolean isAllPlaced() {
        for (int remaining : remainingBySize.values()) {
            if (remaining > 0) return false;
        }
        return true;
    }

    private void updateInstructions() {
        int total = fleetSize;
        instr.setText(currentSize > 0
                ? "Розміщується: " + Utils.shipTypeName(currentSize) + " (розмір " + currentSize + "). Розставлено кораблів: "
                + placedShips.size() + " з " + total
                : "Оберіть корабель у резерві. Розставлено кораблів: " + placedShips.size() + " з " + total);
    }

    private List<Cell> candidateCellsForPlacement(int gx, int gy, int size, boolean horizontal) {
        List<Cell> cand = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int nx = horizontal ? gx + i : gx;
            int ny = horizontal ? gy : gy + i;
            if (!Utils.inBounds(nx, ny)) return null;
            cand.add(board.getCell(nx, ny));
        }
        return cand;
    }

    private void randomizeRemaining(Runnable onFinished) {
        for (Map.Entry<Integer, Integer> entry : new LinkedHashMap<>(remainingBySize).entrySet()) {
            int size = entry.getKey();
            int count = entry.getValue();
            for (int i = 0; i < count; i++) {
                placeRandomly(size);
            }
        }
        currentSize = -1;
        board.clearPreview();
        rebuildReservePanel();
        updateInstructions();
        if (isAllPlaced()) {
            finish(onFinished);
        }
    }

    private void placeRandomly(int size) {
        int attempts = 0;
        while (attempts < 10000) {
            attempts++;
            int x = random.nextInt(Utils.BOARD_SIZE);
            int y = random.nextInt(Utils.BOARD_SIZE);
            boolean horizontal = random.nextBoolean();
            List<Cell> cand = candidateCellsForPlacement(x, y, size, horizontal);
            if (cand != null && ShipPlacementValidator.canPlaceShip(board, cand)) {
                Ship s = new Ship(new ArrayList<>(cand));
                board.addShip(s);
                player.addShip(s);
                placedShips.add(s);
                remainingBySize.merge(size, -1, Integer::sum);
                return;
            }
        }
    }

    private void finish(Runnable onFinished) {
        board.setShowShips(false);
        dispose();
        onFinished.run();
    }
}