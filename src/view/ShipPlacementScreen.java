package src.view;

import src.model.Cell;
import src.model.Player;
import src.model.Ship;
import src.utils.Utils;

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
        super("Розстановка кораблів — " + player.getName());
        this.player = player;
        this.board = player.getBoard();
        this.placedShips = new ArrayList<>();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        reservePanel = new JPanel();
        reservePanel.setLayout(new BoxLayout(reservePanel, BoxLayout.Y_AXIS));
        reservePanel.setBorder(BorderFactory.createTitledBorder("Резерв кораблів"));

        for (int sz : Utils.SHIP_SIZES) {
            JPanel p = createReserveShipComponent(sz);
            reservePanel.add(p);
        }

        add(reservePanel, BorderLayout.WEST);

        JPanel center = new JPanel(new BorderLayout());
        center.add(board, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JLabel instr = new JLabel("Виберіть корабель зліва, рухайте мишкою по полю, R - обертання, D - видалити обраний");
        add(instr, BorderLayout.SOUTH);

        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

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
                        player.getBoard().setShowShips(false);
                        dispose();
                        onFinished.run();
                    }
                }
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    if (currentPlacing != null) {
                        placingHorizontal = !placingHorizontal;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    if (!placedShips.isEmpty()) {
                        Ship s = placedShips.remove(placedShips.size() - 1);
                        board.removeShip(s);
                        player.getShips().remove(s);
                        addReserveShipComponent(s.cells().size());
                    }
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();
    }

    private JPanel createReserveShipComponent(int size) {
        JPanel comp = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = size * Utils.CELL_SIZE;
                int h = Utils.CELL_SIZE;
                g.setColor(new Color(100, 120, 160));
                g.fillRect(2, 2, w - 4, h - 4);
                g.setColor(Color.BLACK);
                g.drawRect(2, 2, w - 4, h - 4);
            }
        };
        comp.setPreferredSize(new Dimension(Math.max(80, size * Utils.CELL_SIZE + 10), Utils.CELL_SIZE + 8));
        comp.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        comp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                currentSize = size;
                currentPlacing = new Ship(new ArrayList<>());
                placingHorizontal = true;
            }
        });

        return comp;
    }

    private void addReserveShipComponent(int size) {
        JPanel p = createReserveShipComponent(size);
        reservePanel.add(p);
        reservePanel.revalidate();
        reservePanel.repaint();
    }

    private void removeOneFromReserve(int size) {
        for (Component c : reservePanel.getComponents()) {
            if (c instanceof JPanel) {
                Dimension d = ((JPanel) c).getPreferredSize();
                if (d.width >= size * Utils.CELL_SIZE) {
                    reservePanel.remove(c);
                    reservePanel.revalidate();
                    reservePanel.repaint();
                    return;
                }
            }
        }
    }

    private boolean isAllPlaced() {
        return placedShips.size() >= Utils.SHIP_SIZES.length;
    }

    private List<Cell> candidateCellsForPlacement(int gx, int gy, int size, boolean horizontal) {
        if (!Utils.inBounds(gx, gy)) return null;
        List<Cell> cand = new ArrayList<>();
        if (horizontal) {
            int startX = gx;
            if (startX + size - 1 >= Utils.BOARD_SIZE) startX = Utils.BOARD_SIZE - size;
            for (int i = 0; i < size; i++) {
                int nx = startX + i;
                int ny = gy;
                cand.add(board.getCell(nx, ny));
            }
        } else {
            int startY = gy;
            if (startY + size - 1 >= Utils.BOARD_SIZE) startY = Utils.BOARD_SIZE - size;
            for (int i = 0; i < size; i++) {
                int nx = gx;
                int ny = startY + i;
                cand.add(board.getCell(nx, ny));
            }
        }
        return cand;
    }
}