import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TicTacToeFrame extends JFrame {

    private String[][] board = new String[3][3];
    private TicTacToeTile[][] tiles = new TicTacToeTile[3][3];
    private JLabel statusLabel;
    private String player = "X";
    private int moveCount = 0;
    private boolean gameOver = false;

    public TicTacToeFrame() {
        setTitle("Tic Tac Toe");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        statusLabel = new JLabel("Player X's turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(statusLabel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 5, 5));
        ActionListener tileListener = new TileListener();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                tiles[row][col] = new TicTacToeTile(row, col);
                tiles[row][col].setFont(
                        new Font("SansSerif", Font.BOLD, 60));
                tiles[row][col].addActionListener(tileListener);
                boardPanel.add(tiles[row][col]);
            }
        }

        add(boardPanel, BorderLayout.CENTER);

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> quitGame());
        add(quitButton, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quitGame();
            }
        });

        clearBoard();
        setLocationRelativeTo(null);
    }

    private class TileListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (gameOver) {
                return;
            }

            TicTacToeTile tile = (TicTacToeTile) e.getSource();
            int row = tile.getRow();
            int col = tile.getCol();

            if (!isValidMove(row, col)) {
                JOptionPane.showMessageDialog(
                        TicTacToeFrame.this,
                        "That square is taken. Pick an empty square.",
                        "Invalid Move",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            board[row][col] = player;
            tile.setText(player);
            moveCount++;

            if (moveCount >= 5 && isWin(player)) {
                endGame("Player " + player + " wins!");
                return;
            }

            if (moveCount >= 7 && isTie()) {
                if (moveCount == 9) {
                    endGame("It's a tie! The board is full.");
                } else {
                    endGame("It's a tie! No winning moves remain.");
                }
                return;
            }

            if (player.equals("X")) {
                player = "O";
            } else {
                player = "X";
            }

            statusLabel.setText("Player " + player + "'s turn");
        }
    }

    private void clearBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                board[row][col] = " ";
                tiles[row][col].setText(" ");
            }
        }

        player = "X";
        moveCount = 0;
        gameOver = false;
        statusLabel.setText("Player X's turn");
    }

    private boolean isValidMove(int row, int col) {
        return board[row][col].equals(" ");
    }

    private boolean isWin(String player) {
        return isRowWin(player)
                || isColWin(player)
                || isDiagonalWin(player);
    }

    private boolean isRowWin(String player) {
        for (int row = 0; row < 3; row++) {
            if (board[row][0].equals(player)
                    && board[row][1].equals(player)
                    && board[row][2].equals(player)) {
                return true;
            }
        }

        return false;
    }

    private boolean isColWin(String player) {
        for (int col = 0; col < 3; col++) {
            if (board[0][col].equals(player)
                    && board[1][col].equals(player)
                    && board[2][col].equals(player)) {
                return true;
            }
        }

        return false;
    }

    private boolean isDiagonalWin(String player) {
        if (board[0][0].equals(player)
                && board[1][1].equals(player)
                && board[2][2].equals(player)) {
            return true;
        }

        return board[0][2].equals(player)
                && board[1][1].equals(player)
                && board[2][0].equals(player);
    }

    private boolean isTie() {
        for (int row = 0; row < 3; row++) {
            if (!isBlockedLine(
                    board[row][0], board[row][1], board[row][2])) {
                return false;
            }
        }

        for (int col = 0; col < 3; col++) {
            if (!isBlockedLine(
                    board[0][col], board[1][col], board[2][col])) {
                return false;
            }
        }

        return isBlockedLine(board[0][0], board[1][1], board[2][2])
                && isBlockedLine(board[0][2], board[1][1], board[2][0]);
    }

    private boolean isBlockedLine(String first, String second, String third) {
        boolean hasX = first.equals("X")
                || second.equals("X") || third.equals("X");

        boolean hasO = first.equals("O")
                || second.equals("O") || third.equals("O");

        return hasX && hasO;
    }

    private void endGame(String message) {
        gameOver = true;
        statusLabel.setText(message);

        JOptionPane.showMessageDialog(this, message);

        int answer = JOptionPane.showConfirmDialog(
                this,
                "Would you like to play again?",
                "Play Again",
                JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            clearBoard();
        } else {
            dispose();
        }
    }

    private void quitGame() {
        int answer = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to quit?",
                "Quit Game",
                JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
}