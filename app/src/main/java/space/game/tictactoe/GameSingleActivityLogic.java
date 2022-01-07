package space.game.tictactoe;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static space.game.tictactoe.Block.*;

public class GameSingleActivityLogic {
    private static final int[][] ROWS = {
            { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
            { 0, 4, 8 }, { 2, 4, 6 }
    };

    // Namenskonstanten zur Darstellung der verschiedenen Spielzustände
    public static final int PLAYING = 0; // Spiel läuft
    public static final int CROSS_WON = 1; // Kreuz (Spieler) hat gewonnen
    public static  final int NOUGHT_WON = 2; // Zero (Android) hat gewonnen
    public static final int DRAW = 3; // Unentschieden

    // Das Spielbrett und der Spielstatus
    private static final int BOARDSIZE = 9; // Anzahl der Blocks
    private Block[] board = new Block[BOARDSIZE]; // Spielbrett in Array-Anordnung

    public void resetBoard() {
        for (int i = 0; i < BOARDSIZE; ++i) {
            board[i] = EMPTY;
        }
    }

    // Gibt den nächsten besten Zug für den Computer zurück.
    public int[] hardMove() {
        int[] result = minimax(2, NOUGHT); // depth - depth - gewuenschteTiefe, gibt Max (für 0) zurück
        return new int[] {result[1]};   // Blockposition
    }

    // Gibt den nächsten freien Zug für den Computer zurück.
    public int[] easyMove() {
        int[] result = findEasyMove(2, NOUGHT); // depth - gewuenschteTiefe, gibt Max (für 0) zurück
        return new int[] {result[1]};   // Blockposition
    }

    public int[] mediumMove() {
        int[] result = alternatelyMove(2, NOUGHT); // depth - gewuenschteTiefe, gibt Max (für 0) zurück
        return new int[] {result[1]};   // Blockposition
    }


    // Führt abwechselnd easy und medium Schritte aus
    int count = 1;
    public int[] alternatelyMove(int depth, Block player){
        Log.d("count", String.valueOf(count));

        // speichert mögliche nächste Züge in der Liste
        List<int[]> nextMoves = generateMoves();

        int bestScore = (player == NOUGHT) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestBlock = -1;
        // Easy - wählt erste von Ende position
        if (count % 2 == 0) {
            for (int[] move : nextMoves){
                board[move[0]] = player;
                bestBlock = move[0];
                board[move[0]] = EMPTY;
            }
        }
        //Hard - Minimax-Algorithmus
        if (count % 2 == 1) {
            if (depth == 0 || nextMoves.isEmpty()){
                bestScore = evaluate();
            } else {
                for (int[] move : nextMoves){
                    board[move[0]] = player;
                    if (player == NOUGHT) {
                        currentScore = minimax(depth - 1, CROSS)[0];
                        if (currentScore > bestScore) {
                            bestScore = currentScore;
                            bestBlock = move[0];
                        }
                    } else {
                        currentScore = minimax(depth - 1, NOUGHT)[0];
                        if (currentScore < bestScore) {
                            bestScore = currentScore;
                            bestBlock = move[0];
                        }
                    }
                    board[move[0]] = EMPTY;
                }
            }

        }

        count++;
        return new int[] {bestScore, bestBlock};

    }


    public int[] findEasyMove(int depth, Block player){
        //  speichert mögliche nächste Züge in der Liste
        List<int[]> nextMoves = generateMoves();
        // wählt ein zufälliges Element aus Liste aus
        int[] move = nextMoves.get(new Random().nextInt(nextMoves.size()));
        int bestScore = (player == NOUGHT) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int bestBlock = move[0];
        return new int[] {bestScore, bestBlock};
    }


    // Minimiere Gewinnmöglichkeiten für den Gegner
    // Maximiere eigene Gewinnmöglichkeiten
    public int[] minimax(int depth, Block player){
        // Generiert mögliche nächste Züge in einer Liste.
        List<int[]> nextMoves = generateMoves();

        // Android (0 NOUGHT) ist maximizing; Gegner (X CROSS) ist minimizing
        int bestScore = (player == NOUGHT) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        int bestBlock = -1;

        if (depth == 0 || nextMoves.isEmpty()){
            bestScore = evaluate();
        } else {
            for (int[] move : nextMoves){
                board[move[0]] = player;
                if (player == NOUGHT) { // Android (0 NOUGHT) is Maximizing Player
                    currentScore = minimax(depth - 1, CROSS)[0];
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestBlock = move[0];
                    }
                } else { // Spieler (X CROSS) is Minimizing Player
                    currentScore = minimax(depth - 1, NOUGHT)[0];
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestBlock = move[0];
                    }
                }
                board[move[0]] = EMPTY;
            }
        }
        return new int[] {bestScore, bestBlock};
    }

    //  Wertung für jede der 8 Linien auswerten (3 Zeilen, 3 Spalten, 2 Diagonalen)
    private int evaluate() {
        int score = 0;
        // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
        for (int[] row : ROWS) {
            score += evaluateLine(row);  // zeile 0
        }
        return score;
    }

    public GameStatus checkGameStatus() {
        GameStatus.GameResult result = GameStatus.GameResult.DRAW;
        int[] winningRow = null;
        for (int[] row : ROWS) {
            int eval = evaluateLine(row);
            if (eval == 100) { // nought has won
                result = GameStatus.GameResult.NOUGHT_WON;
                winningRow = row;
                break;
            } else if (eval == -100) {
                result = GameStatus.GameResult.CROSS_WON;
                winningRow = row;
                break;
            }
        }

        return new GameStatus(!isBoardFull() && result == GameStatus.GameResult.DRAW, result, winningRow);
    }

    private boolean isBoardFull() {
        for (Block block: board) {
            if (block == EMPTY) {
                return false;
            }
        }
        return true;
    }

    /** Heuristische Funktion zur Bewertung der Nützlichkeit des Spielzustands
     @Return +100, +10, +1 für 3-, 2-, 1 -in jeder Linie für Android.
     @Return -100, -10, -1 for 3-, 2-, 1 -in jeder Linie für Opponent.
     @Return 0 sonst bzw. wenn Linie X und 0 enthält
     */
    private int evaluateLine(int[] row) {
        final int[] scores =  { 0, 1, 10, 100 };

        int crosses = 0;
        int noughts = 0;
        for (int field: row) {
            if (board[field] == CROSS) {
                crosses++;
            } else if (board[field] == NOUGHT) {
                noughts++;
            }
        }
        if (crosses > 0 && noughts > 0) {
            return 0;
        } else {
            return scores[noughts] - scores[crosses];
        }
    }

    //  gibt mögliche nächste Züge in der Liste
    private List<int[]> generateMoves() {
        List<int[]> nextMoves = new ArrayList<int[]>();

        if (checkGameStatus().isPlaying()) {
            // Sucht nach leeren Blocks und fügt diese der Liste hinzu
            for (int i = 0; i < BOARDSIZE; ++i) {
                if (board[i] == EMPTY) {
                    nextMoves.add(new int[]{i});
                }
            }
        }

        return nextMoves;
    }

    public void placeMove(int x, Block player) {
        board[x] = player;
    }
}