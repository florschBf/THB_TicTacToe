package space.game.tictactoe;

import static space.game.tictactoe.Block.CROSS;
import static space.game.tictactoe.Block.NOUGHT;
import static space.game.tictactoe.R.id.icontransport;
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

import java.util.Random;

import space.game.tictactoe.dialogs.DrawDialog;
import space.game.tictactoe.dialogs.LoseDialog;
import space.game.tictactoe.dialogs.WinDialog;
import space.game.tictactoe.models.Player;
import space.game.tictactoe.models.Sound;


public class GameSingleActivity extends AppCompatActivity {


    //für die Iconauswahl
    private static final String TAG = "OnlineSpiel";
    private final Player player = Player.getPlayer();
    private int icon = player.getIcon();

    // private static final int iconDefault = R.drawable.stern_90;


    private int diffLevel; // Drei Schwierigkeitsstufen: 0 -easy, 1 -medium, 2-hard

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


    //board imageviews für die erleucheten Felder

    // Dies sind die ImageViews vom Board
   /* ImageView block0 = findViewById(R.id.block0);
    ImageView block1 = findViewById(R.id.block1);
    ImageView block2 = findViewById(R.id.block2);
    ImageView block3 = findViewById(R.id.block3);
    ImageView block4 = findViewById(R.id.block4);
    ImageView block5 = findViewById(R.id.block5);
    ImageView block6 = findViewById(R.id.block6);
    ImageView block7 = findViewById(R.id.block7);
    ImageView block8 = findViewById(R.id.block8);
    // das gameboard abstrakt um die Felder markieren zu können, Feldzuordnung dann über die blöcke = block0 etc
    private Integer[] gameboard = {0,0,0,0,0,0,0,0,0};*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Sound-Icon für Ton wiedergabe referenzieren
        ton = (Button)findViewById(R.id.ton);
        if(player.getIsTonOn()) {
            ton.setBackgroundResource(R.drawable.ic_baseline_music_note_24); // Icon-Darstellung: Ton eingeschaltet
        } else {
            ton.setBackgroundResource(R.drawable.ic_baseline_music_off_24); // Icon-Darstellung: Ton ausgeschaltet
        }

        /**
         * TouchListener-Methode, um Sound ein- und -ausschalten
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

        //Datatransfair from IconwahlActivity -> chosen Icon kommt in die OnlinespielActivity aus der Iconactivity woher auch immer diese aufgerufen wird
        final Intent intent = getIntent();
        //Test ob auch wirklich ein playericon geschickt wurde, just in case...sonst wird eines default gesetzt
        if(intent.hasExtra("playerIcon")){
            int playerIcon = intent.getIntExtra("playerIcon", R.drawable.chosenicon_dummy_90);
            Log.d(TAG, "player icon" + playerIcon);
            icon = playerIcon;
        }
        //overwrite default Icon in the ImageView of the onlinespielactivity with the chosen one from the IconWahlActivity, that was transfered above
        ImageView image = (ImageView) findViewById(icontransportsingle);
        image.setImageResource(icon);

/* ZAHNRAD
        // Imageview Zahnrad als Button anclickbar-> Optionen im Menü -> Weiterleitung zu Optionen->Icons->Statistiken
        ImageView zahnrad= findViewById(R.id.zahnrad_pcgame);
        zahnrad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(GameSingleActivity.this, OptionenActivity.class);
                    startActivity(intent);
                } catch(Exception e) {

                }
            }
        });

*/



        dialog = new Dialog(this);         // Dialogfenster für Spielstatus: gewonnen, verloren, unentschieden

        mBoardImageView = new ImageView [9];
        for (int i = 0; i < mBoardImageView.length; i++) {
            mBoardImageView[i] = (ImageView) findViewById(getResources().getIdentifier("block" + i, "id", this.getPackageName()));
        }

        // AlertDialog zur Abfrage, wer soll das Spiel als erster beginnen
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


        // Radiobutton Gruppe erzeugt 3 Radiobuttons für 3 Schwierigkeitsgrade: easy, medium, hard
        RadioGroup difficultyLevel = (RadioGroup) this.findViewById(R.id.difficultyLevel);
        RadioButton radioButtonEasy = (RadioButton) this.findViewById(R.id.easy);
        radioButtonEasy.setChecked(true); // Defaultwert für Schwierigkeitsgrad - easy

        /**
         * // Ermöglicht auf Änderungen (Schwierigkeitsstufe ändern) zu reagieren
         */
        difficultyLevel.setOnCheckedChangeListener((group, checkedId) -> doOnDifficultyLevelChanged(group, checkedId));

    }

    /**
     * Methode um Schwierigkeitsstufe zu ändern
     * @param group Radio-Button Gruppe für drei Schwierigkeitsgrade
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
     * Methode setzt der Spielstatus auf aktiv
     * Setzt ersten Schritt random, falls Android zuerst geht
     */
    public void startNewGame() {
        resetGame();
        if (first == 2) {
            setMove(random.nextInt(9), COMPUTER);
        }
        gameActiv = true;
    }

    /**
     * Methode um Felder mit Icons zu markieren
     * Sorgt dafür, dass die Spielzüge nur in noch freie Felder gesetzt werden können
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
     * Methode erlaubt Spiel neu anfangen
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
     * Methode um alle Spielfelder auf leer und klickbar zu setzen
     */
    public void clearAllBlocks() {
        minimax.resetBoard();
        for (int i = 0; i < 9; i++) {
            mBoardImageView[i].setImageResource(0);
            mBoardImageView[i].setEnabled(true);
            mBoardImageView[i].setOnClickListener(new ButtonClickListener(i));
            //setze alle Feldfarben wieder auf blau zurück
           mBoardImageView[i].setBackgroundColor(Color.argb(100, 11, 11, 59 ));
        }
    }

    /**
     * Methode setzt alle Spielfelder in anklickbarem Status
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
         */
        private void blockAllFields() {
            for (int i = 0; i < 9; i++) {
                mBoardImageView[i].setClickable(false);
            }
        }

        // verwendet den Schwierigkeitsgrad, um zu bestimmen, welchen Algorithmus der Computer verwenden soll

        // aber was genau macht die Funktion?
        // Der Name "onClick" ist nicht aussagekräftig, Die Funktion sieht so aus, als täte sie folgendes:
        // sie stellt sicher, dass das board gerade aktiv ist, setzt einen Zug, und blockiert dann die Restfelder,
        // prüft auf einen Gewinner anhand der Funktionen minimax und checkGameStatus, wenn noch kein Gewinner gefunden
        // wurde, wird der Spielstatus auf playing gesetzt und ein Zug des Computers wird initiert, am ende wird nochmal auf den winner geprüft
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
         * Methode stellt "Lose" Dialogfenster dar
         */
        private void showLoseDialog() {
            player.increaseLosses();
            LoseDialog loseDialog = new LoseDialog(GameSingleActivity.this, GameSingleActivity.this);
            loseDialog.show();
        }

        /**
         * Methode stellt "Draw" Dialogfenster dar
         */
        private void showDrawDialog() {
            player.increaseDraws();
            DrawDialog drawDialog = new DrawDialog(GameSingleActivity.this, GameSingleActivity.this);
            drawDialog.show();
        }

        /**
         * Methode stellt "win" Dialogfenster dar
         */
        private void showWinDialog() {
            player.increaseWins();
            WinDialog winDialog = new WinDialog(GameSingleActivity.this, GameSingleActivity.this);
            winDialog.show();
        }

    }

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
}
