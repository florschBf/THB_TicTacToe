package space.game.tictactoe;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameSingleActivityLogic {

    // Namenskonstanten zur Darstellung der Blockinhalt
    public final int EMPTY = 0; // Leer
    public final int CROSS = 1; // Kreuz
    public final int NOUGHT = 2; // Zero

    // Namenskonstanten zur Darstellung der verschiedenen Spielzustände
    public final int PLAYING = 0; // Spiel läuft
    public final int CROSS_WON = 1; // Kreuz (Spieler) hat gewonnen
    public final int NOUGHT_WON = 2; // Zero (Android) hat gewonnen
    public final int DRAW = 3; // Unentschieden

    // Das Spielbrett und der Spielstatus
    public static final int BOARSDIZE = 9; // Anzahl der Blocks
    public static int[] board = new int[BOARSDIZE]; // Spielbrett in Array-Anordnung

    GameSingleActivity gameActivitySingleActivity;

    public GameSingleActivityLogic(GameSingleActivity gameActivitySingleActivity) {
        this.gameActivitySingleActivity = gameActivitySingleActivity;
    }

    public void resetBoard() {
        for (int i = 0; i < BOARSDIZE; ++i) {
            board[i] = 0;
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
    public int[] alternatelyMove(int depth, int player){
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


    public int[] findEasyMove(int depth, int player){
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
    public int[] minimax(int depth, int player){
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
        score += evaluateLine(0, 1, 2);  // zeile 0
        score += evaluateLine(3, 4, 5);  // zeile 1
        score += evaluateLine(6, 7, 8);  // zeile 2
        score += evaluateLine(0, 3, 6);  // spalte 0
        score += evaluateLine(1, 4, 7);  // spalte 1
        score += evaluateLine(2, 5, 8);  // spalte 2
        score += evaluateLine(0, 4, 8);  // diagonal 1
        score += evaluateLine(2, 4, 6);  // diagonal 1
        return score;
    }


    /** Heuristische Funktion zur Bewertung der Nützlichkeit des Spielzustands
     @Return +100, +10, +1 für 3-, 2-, 1 -in jeder Linie für Android.
     @Return -100, -10, -1 for 3-, 2-, 1 -in jeder Linie für Opponent.
     @Return 0 sonst bzw. wenn Linie X und 0 enthält
     */
    private int evaluateLine(int row, int row1, int row2) {
        int score = 0;

        // Nach Linien für X- oder O-Sieg suchen.

        if (board[row] == NOUGHT) {
            score = 1;
        } else if (board[row] == CROSS) {
            score = -1;
        }

        if (board[row1] == NOUGHT) {
            if (score == 1) {   // android 0
                score = 10;
            } else if (score == -1) {  // gegner X
                return 0;
            } else {  // leer
                score = 1;
            }
        } else if (board[row1] == CROSS) {
            if (score == -1) { // gegner X
                score = -10;
            } else if (score == 1) { // android 0
                return 0;
            } else {  // ist leer
                score = -1;
            }
        }

        if (board[row2] == NOUGHT) {
            if (score > 0) {  // einmal und/oder zweimal android 0
                score *= 10;
            } else if (score < 0) {  // einmal und/oder zweimal gegner X
                return 0;
            } else {  // einmal und/oder zweimal leer
                score = 1;
            }
        } else if (board[row2] == CROSS) {
            if (score < 0) {  // einmal und/oder zweimal gegner X
                score *= 10;
            } else if (score > 1) {  // einmal und/oder zweimal android 0
                return 0;
            } else {  // einmal und/oder zweimal leer
                score = -1;
            }
        }
        return score;
    }

    //  gibt mögliche nächste Züge in der Liste
    private List<int[]> generateMoves() {
        List<int[]> nextMoves = new ArrayList<int[]>();

        int state = checkGameStatus(); // 1= X gewonnen, 2= 0 gewonnen, 3= unentschieden
        if (state == 1 || state == 2 || state == 3) {
            return nextMoves;   // gibt leere Liste zurück
        }

        // Sucht nach leeren Blocks und fügt diese der Liste hinzu
        for (int i = 0; i < BOARSDIZE; ++i) {
            if (board[i] == EMPTY) {
                nextMoves.add(new int[] {i});
            }
        }
        return nextMoves;
    }

    public void placeMove(int x, int player) {
        board[x] = player;
    }


    public int checkGameStatus() {
        /** CROSS_WON - X (Person) hat gewonnen
         *  NOUGHT_WON -  0 (Android) hat gewonnen
         *  DRAW - Unentschieden
         *  PLAYING - Spiel leuft
         */

        // Zeilen prüfen
        if ((board[0] == CROSS && board[1] == CROSS && board[2] == CROSS) ||
                (board[3] == CROSS && board[4] == CROSS && board[5] == CROSS) ||
                (board[6] == CROSS && board[7] == CROSS && board[8] == CROSS)) {
            return CROSS_WON;
        }
        if ((board[0] == NOUGHT && board[1] == NOUGHT && board[2] == NOUGHT) ||
                (board[3] == NOUGHT && board[4] == NOUGHT && board[5] == NOUGHT) ||
                (board[6] == NOUGHT && board[7] == NOUGHT && board[8] == NOUGHT)) {
            return NOUGHT_WON;
        }

        // Spalten prüfen
        if ((board[0] == CROSS && board[3] == CROSS && board[6] == CROSS) ||
                (board[1] == CROSS && board[4] == CROSS && board[7] == CROSS) ||
                (board[2] == CROSS && board[5] == CROSS && board[8] == CROSS)) {
            return CROSS_WON;
        }
        if ((board[0] == NOUGHT && board[3] == NOUGHT && board[6] == NOUGHT) ||
                (board[1] == NOUGHT && board[4] == NOUGHT && board[7] == NOUGHT) ||
                (board[2] == NOUGHT && board[5] == NOUGHT && board[8] == NOUGHT)) {
            return NOUGHT_WON;
        }

        // Diagonale prüfen
        if ((board[0] == CROSS && board[4] == CROSS && board[8] == CROSS) ||
                (board[2] == CROSS && board[4] == CROSS && board[6] == CROSS)) {
            return CROSS_WON;
        }
        if ((board[0] == NOUGHT && board[4] == NOUGHT && board[8] == NOUGHT) ||
                (board[2] == NOUGHT && board[4] == NOUGHT && board[6] == NOUGHT)) {
            return NOUGHT_WON;
        }

        // Spiel läuft
        for (int i = 0; i < BOARSDIZE; i++) {
            if (board[i] != CROSS && board[i] != NOUGHT) {
                return PLAYING;
            }
        }
        return DRAW;
    }
}