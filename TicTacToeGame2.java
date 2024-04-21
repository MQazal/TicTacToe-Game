import java.util.Scanner;
public class TicTacToeGame2 {
    private static final char EMPTY = ' ';
    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';

    private char[][] board;
    private char currentPlayer;

    // Constructor to initialize the game board and current player
    public TicTacToeGame2() {
        board = new char[3][3];
        currentPlayer = PLAYER_X;
        initializeBoard();
    }

    // Initialize the board with empty spaces
    private void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    // Print the current state of the board
    private void printBoard() {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println();
            System.out.println("-------------");
        }
    }

    // Check if a move is valid (within the board boundaries and the cell is empty)
    private boolean isMoveValid(int row, int col) {
        return row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == EMPTY;
    }

    // Check if the board is full, indicating a tie
    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    // Check if a player has won the game
    private boolean checkWin(char player) { // check for x values
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) {
                return true;
            }
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) {
                return true;
            }
        }

        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }

        return board[0][2] == player && board[1][1] == player && board[2][0] == player;
    }

    // Evaluate the current state of the board and return a score for the AI's move
    private int evaluate() {
        if (checkWin(PLAYER_X)) {
            return 10;
        } else if (checkWin(PLAYER_O)) {
            return -10;
        } else {
            return 0;
        }
    }

    // Implementation of the minimax algorithm to find the best move for the AI
    private int minimax(int depth, boolean isMax) {
        int score = evaluate();

        if (score == 10 || score == -10) {
            return score;
        }

        if (isBoardFull()) {
            return 0;
        }

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = PLAYER_X;
                        best = Math.max(best, minimax(depth + 1, !isMax));
                        board[i][j] = EMPTY;
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = PLAYER_O;
                        best = Math.min(best, minimax(depth + 1, !isMax));
                        board[i][j] = EMPTY;
                    }
                }
            }
            return best;
        }
    }
    // Easy level: Make random moves.
    private void makeRandomMove() {
        int row, col;
        do {
            row = (int) (Math.random() * 3);
            col = (int) (Math.random() * 3);
        } while (!isMoveValid(row, col));
        board[row][col] = PLAYER_X;
    }

    // Make a move for the AI based on the selected level of difficulty
    private void makeAIMove(boolean isHard) {
        if (isHard) {
            int bestMoveScore = Integer.MIN_VALUE;
            int bestRow = -1;
            int bestCol = -1;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = PLAYER_X;
                        int moveScore = minimax(0, false);
                        board[i][j] = EMPTY;

                        if (moveScore > bestMoveScore) {
                            bestMoveScore = moveScore;
                            bestRow = i;
                            bestCol = j;
                        }
                    }
                }
            }

            board[bestRow][bestCol] = PLAYER_X;
        } else {  // Easy level: Make random moves.
            makeRandomMove();
        }
    }

    // Main game loop for playing Tic Tac Toe
    public void playGame(boolean vsAI, boolean isHard) {
        Scanner scanner = new Scanner(System.in);
        int row, col;

        while (true) {
            printBoard();

            if (vsAI && currentPlayer == PLAYER_X){
                System.out.println("Computer's Turn (X):");
                makeAIMove(isHard);
                currentPlayer = PLAYER_O;
            } 
            
            else {
                System.out.println("Player " + currentPlayer + "'s Turn:");
                System.out.print("Enter row (1-3): ");
                row = scanner.nextInt()-1;
                System.out.print("Enter column (1-3): ");
                col = scanner.nextInt()-1;

                if (isMoveValid(row, col)) {
                    board[row][col] = currentPlayer;
                    currentPlayer = (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
                } else {
                    System.out.println("Invalid move! Try again.");
                }
            }
            
            if (checkWin(PLAYER_X)) {
                printBoard();
                if (vsAI) {
                    System.out.println("Computer Wins!");
                } else {
                    System.out.println("Player X Wins!");
                }
                break;
            } else if (checkWin(PLAYER_O)) {
                printBoard();
                System.out.println("Player O Wins!");
                break;
            } else if (isBoardFull()) {
                printBoard();
                System.out.println("It's a Tie!");
                break;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Tic Tac Toe!");

        System.out.println("Choose the mode:");
        System.out.println("1. Player vs Player");
        System.out.println("2. Player vs Computer");

        int mode = scanner.nextInt();
        boolean vsAI = (mode == 2);

        boolean isHard = false;
        if (vsAI) {
            System.out.println("Choose the Computer level:");
            System.out.println("1. Easy");
            System.out.println("2. Hard");

            int level = scanner.nextInt();
            isHard = (level == 2);
        }

        TicTacToeGame2 game = new TicTacToeGame2();
        game.playGame(vsAI, isHard);
    }
}
