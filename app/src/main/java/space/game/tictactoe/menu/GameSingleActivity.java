package space.game.tictactoe.menu;

import static space.game.tictactoe.models.Block.CROSS;
import static space.game.tictactoe.models.Block.EMPTY;
import static space.game.tictactoe.models.Block.NOUGHT;
import static space.game.tictactoe.R.id.icontransportsingle;

import android.app.AlertDialog;

import android.app.Dialog;

import android.content.DialogInterface;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import space.game.tictactoe.GameStatus;
import space.game.tictactoe.R;
import space.game.tictactoe.dialogs.DrawDialog;
import space.game.tictactoe.dialogs.LoseDialog;
import space.game.tictactoe.dialogs.WinDialog;
import space.game.tictactoe.menu.options.IconwahlActivity;
import space.game.tictactoe.models.Block;
import space.game.tictactoe.models.Player;
import space.game.tictactoe.models.Sound;


public class GameSingleActivity extends AppCompatActivity {


    /**für die Iconauswahl - for icon choosing
     *
     */
    private static final String TAG = "OnlineSpiel";
    private final Player player = Player.getPlayer();
    private int icon = player.getIcon();

    /** Drei Schwierigkeitsstufen: 0 -easy, 1 -medium, 2-hard - three difficulty levels, easy, medium, hard
     *
     */
    private int diffLevel;

    Dialog dialog;

    private GameSingleActivityLogic minimax;
    private GameSingleActivityLogic getWinnerRow;

    private ImageView mBoardImageView[];
    private CharSequence[] items = new CharSequence[]{"Ich", "Android"};
    private int first = 0; // 1 = beginnt Spieler, 2 beginnt Android

    public Block HUMAN = CROSS;
    public Block COMPUTER = NOUGHT;
    Random random = new Random();
    private boolean gameActiv = true;

    // Sound
    private MediaPlayer sound1, sound2, soundWin, soundLose, soundDraw;
    Button ton;
    int i = 1; // 1 = ton on, 0 = ton off



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        /** Sound-Icon für Ton wiedergabe referenzieren - sound icon to reference sound
         *
         */
        ton = (Button)findViewById(R.id.ton);
        if(player.getIsTonOn()) {
            ton.setBackgroundResource(R.drawable.ic_baseline_music_note_24); // Icon-Darstellung: Ton eingeschaltet
        } else {
            ton.setBackgroundResource(R.drawable.ic_baseline_music_off_24); // Icon-Darstellung: Ton ausgeschaltet
        }

        /**
         * TouchListener-Methode, um Sound ein- und -ausschalten - touch listener method to switch on and of sound
         */
        ton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(i == 1) {
                        ton.setBackgroundResource(R.drawable.ic_baseline_music_note_24);
                        Player.getPlayer().setIsTonOn(true);
                        i = 0;
                    } else if (i == 0){
                        ton.setBackgroundResource(R.drawable.ic_baseline_music_off_24);
                        Player.getPlayer().setIsTonOn(false);
                        i = 1;
                    }
                }
                return false;
            }
        });

        sound1 = MediaPlayer.create(this, R.raw.one);
        sound2 = MediaPlayer.create(this, R.raw.two);
        soundWin = MediaPlayer.create(this, R.raw.win);
        soundLose = MediaPlayer.create(this, R.raw.lose);
        soundDraw = MediaPlayer.create(this, R.raw.draw);


        ImageView imagechange = findViewById(R.id.icontransportsingle);
        imagechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(GameSingleActivity.this, IconwahlActivity.class);
                    startActivity(intent);
                } catch (Exception e) {

                }
            }
        });

        /**Datatransfair from IconwahlActivity -> chosen Icon kommt in die OnlinespielActivity
         * aus der Iconactivity woher auch immer diese aufgerufen wird
         * datatransfair from IconwahlActivity -> chosen Icon goes to OnlinespielActivity
         * from Iconactivity, not minding, from where this is being called up
         */

        final Intent intent = getIntent();
        /**Test ob auch wirklich ein playericon geschickt wurde, just in case...sonst wird eines default gesetzt
         * test ir player icon was really set
         *
         */
        if(intent.hasExtra("playerIcon")){
            int playerIcon = intent.getIntExtra("playerIcon", R.drawable.chosenicon_dummy_90);
            Log.d(TAG, "player icon" + playerIcon);
            icon = playerIcon;
        }
        /**overwrite default Icon in the ImageView of the onlinespielactivity
         * with the chosen one from the IconWahlActivity, that was transfered above
         */
        ImageView image = (ImageView) findViewById(icontransportsingle);
        image.setImageResource(icon);

        /** Dialogfenster für Spielstatus: gewonnen, verloren, unentschieden - dialog window for game statusses won, lost, draw
         *
         */
        dialog = new Dialog(this);

        mBoardImageView = new ImageView [9];
        for (int i = 0; i < mBoardImageView.length; i++) {
            mBoardImageView[i] = (ImageView) findViewById(getResources().getIdentifier("block" + i, "id", this.getPackageName()));
        }

        /** AlertDialog zur Abfrage, wer soll das Spiel als erster beginnen - alert dialog, to estimate who starts with the first move
         *
         */
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
        alertDialog.setCancelable(false); // false -> Dialog kann nicht mit der BACK-Taste abgebrochen werden
        alertDialog.setTitle("Wer geht zuerst?");
        alertDialog.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item] == "Ich") {
                    first = 1; // beginnt Spieler
                } else if (items[item] == "Android") {
                    first = 2; // beginnt Android
                }
                dialog.dismiss();

                minimax = new GameSingleActivityLogic();
                startNewGame();
            }
        });
        alertDialog.show();


        /** Radiobutton Gruppe erzeugt 3 Radiobuttons für 3 Schwierigkeitsgrade: easy, medium, hard
         * radiobutton group creates three radio buttons for three difficulty levels - easy, medium, hard
         */
        RadioGroup difficultyLevel = (RadioGroup) this.findViewById(R.id.difficultyLevel);
        RadioButton radioButtonEasy = (RadioButton) this.findViewById(R.id.easy);
        radioButtonEasy.setChecked(true); // Defaultwert für Schwierigkeitsgrad - easy

        /**
         * Ermöglicht auf Änderungen (Schwierigkeitsstufe ändern) zu reagieren
         * allows to react at changed difficulty level
         */
        difficultyLevel.setOnCheckedChangeListener((group, checkedId) -> doOnDifficultyLevelChanged(group, checkedId));

    }

    /**
     * Methode um Schwierigkeitsstufe zu ändern - method to change difficulty
     * @param group Radio-Button Gruppe für drei Schwierigkeitsgrade - radio buttons for three difficulty levels
     * @param checkedId Radio-Button Id, der in der Radiogruppe aktiviert
     */
    private void doOnDifficultyLevelChanged(RadioGroup group, int checkedId) {
        int checkedRadioId = group.getCheckedRadioButtonId();
        if(checkedRadioId == R.id.easy) {
            resetGame();
            diffLevel = 0;
            startNewGame();
            Toast.makeText(this, "Sie haben den Schwierigkeitsgrad easy ausgewählt", Toast.LENGTH_SHORT).show();
        } else if (checkedRadioId == R.id.medium) {
            resetGame();
            diffLevel = 1;
            startNewGame();
            Toast.makeText(this, "Sie haben den Schwierigkeitsgrad medium ausgewählt", Toast.LENGTH_SHORT).show();
        } else if (checkedRadioId == R.id.hard) {
            resetGame();
            diffLevel = 2;
            startNewGame();
            Toast.makeText(this, "Sie haben den Schwierigkeitsgrad hard ausgewählt", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Methode setzt den Spielstatus auf aktiv - method that sets game status to active
     * Setzt ersten Schritt random, falls Android zuerst geht - sets first move to random, if android starts
     */
    public void startNewGame() {
        resetGame();
        if (first == 2) {
            setMove(random.nextInt(9), COMPUTER);
        }
        gameActiv = true;
    }

    /**
     * Methode um Felder mit Icons zu markieren - method to mark blocks with icons
     * Sorgt dafür, dass die Spielzüge nur in noch freie Felder gesetzt werden können - secures that moves only can be applied to empty blocks
     * @param x Nummer des Feldes
     * @param player 1 für den Spieler, != 1 Android
     */
    public void setMove(int x, Block player) {
        minimax.placeMove(x, player);
        if (player == CROSS) {
            mBoardImageView[x].setImageResource(icon);
            Sound.soundPlay(sound1);
        } else {
            Sound.soundPlay(sound2);
            // Zeitverzug für Android Schritte
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBoardImageView[x].setImageResource(R.drawable.herz_90);
                    unblockAllFields();
                }
            }, 550);
        }
        mBoardImageView[x].setEnabled(false);
    }

    /**
     * Methode erlaubt Spiel neu anfangen - method allows to restart game anew
     * @param view - Offline-Game Ansicht
     */
    public void restartGame(View view) {
        Toast restart = Toast.makeText(this, "Spiel neu starten", Toast.LENGTH_SHORT);
        restart.setGravity(Gravity.CENTER, 0, 0);
        restart.show();
        resetGame();
        gameActiv = true;
    }

    public void resetGame() {
        clearAllBlocks();
    }

    /**
     * Methode um alle Spielfelder auf leer und klickbar zu setzen - method to set all game blocks to empty and clickable
     */
    public void clearAllBlocks() {
        minimax.resetBoard();
        for (int i = 0; i < 9; i++) {
            mBoardImageView[i].setImageResource(0);
            mBoardImageView[i].setEnabled(true);
            mBoardImageView[i].setOnClickListener(new ButtonClickListener(i));
            //setze alle Feldfarben wieder auf blau zurück - reset all blocks to blue
           mBoardImageView[i].setBackgroundColor(Color.argb(100, 11, 11, 59 ));
        }
    }

    /**
     * Methode setzt alle Spielfelder in anklickbarem Status
     * set all board fields in clickable mode
     */
    private void unblockAllFields() {
        for (int i = 0; i < 9; i++) {
            mBoardImageView[i].setClickable(true);
        }
    }

    private class ButtonClickListener implements View.OnClickListener {
        int x;
        public ButtonClickListener(int i) {
            this.x = i;
        }

        /**
         *  Methode um alle Spieldfelder nicht anklickbar machen
         *  method to block all game fields /blocks from being clicked
         */
        private void blockAllFields() {
            for (int i = 0; i < 9; i++) {
                mBoardImageView[i].setClickable(false);
            }
        }


        @Override
        public void onClick(View v) {
            // easy level, Spiel aktiv, Felder frei
            if (diffLevel == 0 && gameActiv && mBoardImageView[x].isEnabled()) {
                setMove(x, HUMAN); // Mensch macht einen Schritt KREUZ
                blockAllFields();
                if (minimax.checkGameStatus().isPlaying()) {
                    int[] result = minimax.easyMove();
                    setMove(result[0], COMPUTER);
                }
                checkWinner();
            }
            // medium
            if (diffLevel == 1 && gameActiv && mBoardImageView[x].isEnabled()) {
                setMove(x, HUMAN); // Mensch macht einen Schritt KREUZ
                blockAllFields(); // Spielfeld blockieren
                if (minimax.checkGameStatus().isPlaying()) { // immer noch spielen
                    int[] result = minimax.mediumMove();
                    setMove(result[0], COMPUTER);
                }
                checkWinner();
            }
            // hard
            if (diffLevel == 2 && gameActiv && mBoardImageView[x].isEnabled()) {
                setMove(x, HUMAN); // Mensch macht einen Schritt KREUZ
                blockAllFields();
                if (minimax.checkGameStatus().isPlaying()) { // immer noch spielen
                    int[] result = minimax.hardMove();
                    setMove(result[0], COMPUTER);
                }
                checkWinner();
            }
        }

        /**
         * Methode prüft Spielergebnis und ruft etsprechende Methode mit Dialogfenster auf
         * - method  checks gamestatus and calls method that calls a dialog window
         */
        private void checkWinner() {
            GameStatus status = minimax.checkGameStatus();
            System.out.println(status);
            if (status.isPlaying()) {
                return;
            }
            if (status.getResult() == GameStatus.GameResult.DRAW) {
                Sound.soundPlay(soundDraw);
                gameActiv = false;
                new Handler().postDelayed(new Runnable() { // Zeitverzug
                    @Override
                    public void run() {
                        showDrawDialog();
                    }
                }, 700);

            } else if (status.getResult() == GameStatus.GameResult.CROSS_WON) {
                gameActiv = false;
                colorWinningRow(status.getWinningRow());
                Sound.soundPlay(soundWin);
                new Handler().postDelayed(new Runnable() { // Zeitverzug
                    @Override
                    public void run() {
                        showWinDialog();
                    }
                }, 700);

            } else if (status.getResult() == GameStatus.GameResult.NOUGHT_WON) {
                gameActiv = false;
                colorWinningRow(status.getWinningRow());
                Sound.soundPlay(soundLose);
                new Handler().postDelayed(new Runnable() { // Zeitverzug
                    @Override
                    public void run() {
                        showLoseDialog();
                    }
                }, 700);
            }
        }

        /**
         * Methode stellt "Loose" Dialogfenster dar
         */
        private void showLoseDialog() {
            player.increaseLosses();
            LoseDialog loseDialog = new LoseDialog(GameSingleActivity.this, GameSingleActivity.this);
            loseDialog.show();
        }

        /**
         * Methode stellt "Draw" Dialogfenster dar - method shows "draw" dialog window
         */
        private void showDrawDialog() {
            player.increaseDraws();
            DrawDialog drawDialog = new DrawDialog(GameSingleActivity.this, GameSingleActivity.this);
            drawDialog.show();
        }

        /**
         * Methode stellt "win" Dialogfenster dar - method shows "win" dialog window
         */
        private void showWinDialog() {
            player.increaseWins();
            WinDialog winDialog = new WinDialog(GameSingleActivity.this, GameSingleActivity.this);
            winDialog.show();
        }

    }

    /** function to color the winning row
     *
     * @param winningRow
     */
    private void colorWinningRow(int[] winningRow) {
        int[] blockIDs = {
                R.id.block0, R.id.block1, R.id.block2, R.id.block3, R.id.block4,
                R.id.block5, R.id.block6, R.id.block7, R.id.block8
        };

        for (int field: winningRow) {
            int blockID = blockIDs[field];
            ImageView imageView = (ImageView) findViewById(blockID);
            imageView.setBackgroundColor(Color.argb(50,0,229,230));
        }
    }

    /** define the possible rows in the class
     *
     */
    public static class GameSingleActivityLogic {
        private static final int[][] ROWS = {
                { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
                { 0, 4, 8 }, { 2, 4, 6 }
        };

        /**Namenskonstanten zur Darstellung der verschiedenen Spielzustände - name constants to show different game conditions
         * obsolete as this is handled in Enums now
         */
        public static final int PLAYING = 0; // Spiel läuft
        public static final int CROSS_WON = 1; // Kreuz (Spieler) hat gewonnen
        public static  final int NOUGHT_WON = 2; // Zero (Android) hat gewonnen
        public static final int DRAW = 3; // Unentschieden

        /**Das Spielbrett
         * board
         */
        private static final int BOARDSIZE = 9; // Anzahl der Blocks
        private Block[] board = new Block[BOARDSIZE]; // Spielbrett in Array-Anordnung

        public void resetBoard() {
            for (int i = 0; i < BOARDSIZE; ++i) {
                board[i] = EMPTY;
            }
        }

        /** Gibt den nächsten besten Zug für den Computer zurück. - gives back next best move for computer
         *
         * @return
         */
        public int[] hardMove() {
            int[] result = minimax(2, NOUGHT); // depth - depth - gewuenschteTiefe, gibt Max (für 0) zurück
            return new int[] {result[1]};   // Blockposition
        }

        /** Gibt den nächsten freien Zug für den Computer zurück. - gives back next free move for the computer
         *
         * @return
         */
        public int[] easyMove() {
            int[] result = findEasyMove(2, NOUGHT); // depth - gewuenschteTiefe, gibt Max (für 0) zurück
            return new int[] {result[1]};   // Blockposition
        }

        public int[] mediumMove() {
            int[] result = alternatelyMove(2, NOUGHT); // depth - gewuenschteTiefe, gibt Max (für 0) zurück
            return new int[] {result[1]};   // Blockposition
        }


        /** Führt abwechselnd easy und medium Schritte aus - in turns easy or medium moves
         *
         */
        int count = 1;
        public int[] alternatelyMove(int depth, Block player){
            Log.d("count", String.valueOf(count));

            /** speichert mögliche nächste Züge in der Liste - saves possible next moves in a list
             *
             */
            List<int[]> nextMoves = generateMoves();

            int bestScore = (player == NOUGHT) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            int currentScore;
            int bestBlock = -1;
            /** Easy - wählt erste von Ende position
             *
             */
            if (count % 2 == 0) {
                for (int[] move : nextMoves){
                    board[move[0]] = player;
                    bestBlock = move[0];
                    board[move[0]] = EMPTY;
                }
            }
            /**Hard - Minimax-Algorithmus
             *
             */
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
            /**speichert mögliche nächste Züge in der Liste - saves possible next moves in a list
             *
             */
            List<int[]> nextMoves = generateMoves();
            /**wählt ein zufälliges Element aus Liste aus - chooses random element from list
             *
             */
            int[] move = nextMoves.get(new Random().nextInt(nextMoves.size()));
            int bestScore = (player == NOUGHT) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            int bestBlock = move[0];
            return new int[] {bestScore, bestBlock};
        }


        /** Minimiere Gewinnmöglichkeiten für den Gegner - minimize win possibilities
         *
         * @param depth
         * @param player
         * @return
         */
        /**Maximiere eigene Gewinnmöglichkeiten - maximize win possibilities
         *
         * @param depth
         * @param player
         * @return
         */
        public int[] minimax(int depth, Block player){
            /** Generiert mögliche nächste Züge in einer Liste. - generates possible next turn in a list
             *
             */
            List<int[]> nextMoves = generateMoves();

            /**Android (0 NOUGHT) ist maximizing; Gegner (X CROSS) ist minimizing
             *
             */
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

        /** Wertung für jede der 8 Linien auswerten (3 Zeilen, 3 Spalten, 2 Diagonalen) - evaluate all 8 possible winner lines
         *
         * @return
         */
        private int evaluate() {
            int score = 0;
            /** Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
             *
             */
            for (int[] row : ROWS) {
                score += evaluateLine(row);  // zeile 0
            }
            return score;
        }

        /** function to evaluate the gamestatus - we estimate its a draw and set the winning row to null, as a draw has no winning row
         * then we check if we have a winner or a looser and give back the winning result and a winning row if there is
         * one - it can be also the winnig row of the opponent, as we want to color the winners rows later
         */

        public GameStatus checkGameStatus() {
            GameStatus.GameResult result = GameStatus.GameResult.DRAW;
            int[] winningRow = null;
            for (int[] row : ROWS) {
                int eval = evaluateLine(row);
                if (eval == 100) { // nought has won
                    result = GameStatus.GameResult.NOUGHT_WON;
                    winningRow = row;
                    break;
                } else if (eval == -100) { //cross has won
                    result = GameStatus.GameResult.CROSS_WON;
                    winningRow = row;
                    break;
                }
            }

            return new GameStatus(!isBoardFull() && result == GameStatus.GameResult.DRAW, result, winningRow);
        }

        /** function to check if board is full
         *
         * @return
         */
        private boolean isBoardFull() {
            for (Block block: board) {
                if (block == EMPTY) {
                    return false;
                }
            }
            return true;
        }

        /** Heuristische Funktion zur Bewertung der Nützlichkeit des Spielzustands - heuristic function to evaluate the gamestatus
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

        /**  gibt mögliche nächste Züge in der Liste - returns next possible moves in the list
         *
         * @return
         */
        private List<int[]> generateMoves() {
            List<int[]> nextMoves = new ArrayList<int[]>();

            if (checkGameStatus().isPlaying()) {
                // Sucht nach leeren Blocks und fügt diese der Liste hinzu -  checks for empty blocks and adds them to the list

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
}
