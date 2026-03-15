package org.sea.battle.game.view;

import org.sea.battle.game.model.*;
import org.sea.battle.game.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ShipPlacementScreen extends JFrame {
    private final Player player;
    private final GameBoard board;
    private final List<Ship> placedShips;
    private final JPanel reservePanel;
    private Ship currentPlacing;
    private int currentSize;
    private boolean placingHorizontal = true;

    public ShipPlacementScreen(Player player, Runnable onFinished) {
        super("Розстановка — " + player.getName());
        this.player = player;
        this.board = player.getBoard();
        this.placedShips = new ArrayList<>();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        reservePanel = new JPanel();
        reservePanel.setLayout(new BoxLayout(reservePanel, BoxLayout.Y_AXIS));
        reservePanel.setBorder(BorderFactory.createTitledBorder("Резерв"));

        for (int sz : Utils.SHIP_SIZES) {
            reservePanel.add(createReserveShipComponent(sz));
        }

        add(reservePanel, BorderLayout.WEST);
        add(board, BorderLayout.CENTER);

        JLabel instr = new JLabel("R - повернути, Клік - поставити. Потрібно: " + Utils.SHIP_SIZES.length, SwingConstants.CENTER);
        add(instr, BorderLayout.SOUTH);

        board.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (currentPlacing == null) return;
                int gx = e.getX() / Utils.CELL_SIZE;
                int gy = e.getY() / Utils.CELL_SIZE;
                List<Cell> cand = candidateCellsForPlacement(gx, gy, currentSize, placingHorizontal);
                if (cand != null && ShipPlacementValidator.canPlaceShip(board, cand)) {
                    board.setPreviewCells(cand);
                } else {
                    board.setPreviewCells(null);
                }
            }
        });

        board.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (currentPlacing == null) return;
                int gx = e.getX() / Utils.CELL_SIZE;
                int gy = e.getY() / Utils.CELL_SIZE;
                List<Cell> cand = candidateCellsForPlacement(gx, gy, currentSize, placingHorizontal);

                if (cand != null && ShipPlacementValidator.canPlaceShip(board, cand)) {
                    Ship s = new Ship(new ArrayList<>(cand));
                    board.addShip(s);
                    player.addShip(s);
                    placedShips.add(s);

                    removeOneFromReserve(currentSize);
                    currentPlacing = null;
                    board.clearPreview();

                    if (isAllPlaced()) {
                        board.setShowShips(false);
                        dispose();
                        onFinished.run();
                    }
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) placingHorizontal = !placingHorizontal;
            }
        });

        setFocusable(true);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createReserveShipComponent(int size) {
        JPanel comp = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.GRAY);
                g.fillRect(2, 2, size * Utils.CELL_SIZE - 4, Utils.CELL_SIZE - 4);
            }
        };
        comp.setPreferredSize(new Dimension(size * Utils.CELL_SIZE + 10, Utils.CELL_SIZE + 10));
        comp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentSize = size;
                currentPlacing = new Ship(new ArrayList<>());
            }
        });
        return comp;
    }

    private void removeOneFromReserve(int size) {
        for (Component c : reservePanel.getComponents()) {
            if (c.getPreferredSize().width >= size * Utils.CELL_SIZE) {
                reservePanel.remove(c);
                reservePanel.revalidate();
                reservePanel.repaint();
                break;
            }
        }
    }

    private boolean isAllPlaced() {
        return placedShips.size() == Utils.SHIP_SIZES.length;
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
}