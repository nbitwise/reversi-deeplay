package io.deeplay;

public class Board {
    private final Disk[][] board;
    private int deskSize = 0;


    public Board(int BOARD_SIZE) { //создание доски.. здесь ли делать проверки на корректность данных? или отдельно, аля в "контроллере"
        deskSize = BOARD_SIZE;
        board = new Disk[BOARD_SIZE][BOARD_SIZE];

        int center = BOARD_SIZE / 2;
        board[center - 1][center - 1] = Disk.WHITE;
        board[center][center] = Disk.WHITE;
        board[center - 1][center] = Disk.BLACK;
        board[center][center - 1] = Disk.BLACK;
    }

    public void setOnPlace(int i, int j, Disk disk) { //установка диска в ячейку
        board[i][j] = disk;
    }

    public Disk whatIsInside(int i, int j) { //проверка(возврат), что в ячейке
        return board[i][j];
    }

    public int countDisk(Disk disk) { //подсчет дисков указанного цвета
        int count = 0;
        for (Disk[] disks : board) {
            for (int j = 0; j < deskSize; j++) {
                if (disks[j] == disk) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isValidMove(int row, int col, Player player) { //проверка, является ли клетка подходящей для хода указанного игрока(здесь ли реализовывать?)
        if (row < 0 || row >= deskSize || col < 0 || col >= deskSize || board[row][col] != null) {
            return false;
        }

        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }

                int r = row + dr;
                int c = col + dc;
                boolean isValidDirection = false;

                while (r >= 0 && r < deskSize && c >= 0 && c < deskSize && board[r][c] == player.getOpponent()) {
                    r += dr;
                    c += dc;
                    isValidDirection = true;
                }

                if (isValidDirection && r >= 0 && r < deskSize && c >= 0 && c < deskSize && board[r][c] == player.getOpponent()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasAvailableMoves(Player player) {
        for (int i = 0; i < deskSize; i++) {
            for (int j = 0; j < deskSize; j++) {
                if (isValidMove(i, j, player)) {
                    return true;
                }
            }
        }
        return false;
    }


    //еще нужно сам мув добавить или типа установку. или мув использует метод "устанвока", но где он?

    //зачем нужен копибоард?
}


