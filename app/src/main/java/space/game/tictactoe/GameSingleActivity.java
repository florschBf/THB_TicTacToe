package space.game.tictactoe;

import android.app.AlertDialog;

import android.app.Dialog;

import android.content.DialogInterface;

import android.content.Intent;
import android.media.MediaPlayer;
import android.graphics.Color;
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


public class GameSingleActivity extends AppCompatActivity {


    //für die Iconauswahl
    private static final String TAG = "OnlineSpiel";
    private int icon = Player.getPlayer().getIcon();

    // private static final int iconDefault = R.drawable.stern_90;


    // Schwierigkeitsgrad
    private int diffLevel; // 0 -easy, 1 -medium, 2-hard

    Dialog dialog;

    private GameSingleActivityLogic minimax;

    private ImageView mBoardImageView[];
    private CharSequence[] items = new CharSequence[]{"Ich", "Android"};
    private int first = 0; // 1 = beginnt Spieler, 2 beginnt Android

    public int HUMAN = 1;
    public int COMPUTER = 2;
    Random random = new Random();
    private boolean gameActiv = true;

    // Sound
    private MediaPlayer sound1, sound2, soundWin, soundLose, soundDraw;

    Button ton;
    boolean isTonOn = Player.getPlayer().getIsTonOn();
    int i = 1; // 1 = ton on, 0 = ton off


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Sound für Android ein- und ausschalten
        ton = (Button)findViewById(R.id.ton);
        if(Player.getPlayer().getIsTonOn() == true) {
            ton.setBackgroundResource(R.drawable.ic_baseline_music_note_24);
        } else {
            ton.setBackgroundResource(R.drawable.ic_baseline_music_off_24);
        }

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

        //Datatransfair from IconwahlActivity -> chosen Icon kommt in die OnlinespielActivity aus der Iconactivity woher auch immer diese aufgerufen wird
        final Intent intent = getIntent();
        //Test ob auch wirklich ein playericon geschickt wurde, just in case...sonst wird eines default gesetzt
        if(intent.hasExtra("playerIcon")){
            int playerIcon = intent.getIntExtra("playerIcon", R.drawable.chosenicon_dummy_90);
            Log.d(TAG, "player icon" + playerIcon);
            icon = playerIcon;
        }

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

        // Dialogfenster für Spielstatus: gewonnen, verloren, unentschieden

        dialog = new Dialog(this);

        mBoardImageView = new ImageView [9];
        for (int i = 0; i < mBoardImageView.length; i++) {
            mBoardImageView[i] = (ImageView) findViewById(getResources().getIdentifier("block" + i, "id", this.getPackageName()));
        }


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
        // false -> Dialog kann nicht mit der BACK-Taste abgebrochen werden.
        alertDialog.setCancelable(false);
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

                minimax = new GameSingleActivityLogic(GameSingleActivity.this);
                startNewGame();
            }
        });
        alertDialog.show();


        // Radiobutton Gruppe für 3 Schwierigkeitsgrade: easy, medium, hard
        RadioGroup difficultyLevel = (RadioGroup) this.findViewById(R.id.difficultyLevel);
        RadioButton radioButtonEasy = (RadioButton) this.findViewById(R.id.easy);
//        RadioButton radioButtonMedium = (RadioButton) this.findViewById(R.id.medium);
//        RadioButton radioButtonHard = (RadioButton) this.findViewById(R.id.hard);
        // Defaultwert für Schwierigkeitsgrad - easy
        radioButtonEasy.setChecked(true);

        // Ermöglicht Schwierigkeitsgrad zu ändern
        difficultyLevel.setOnCheckedChangeListener((group, checkedId) -> doOnDifficultyLevelChanged(group, checkedId));

    }

    // Schwierigkeitsgrad wurde geändert
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

    public void startNewGame() {
        resetGame();
        // wenn Android geht zuert, macht erster Schritt random
        if (first == 2) {  // Android geht zuerst
            setMove(random.nextInt(9), COMPUTER);
        }
        gameActiv = true;
    }

    public void setMove(int x, int player) {
        minimax.placeMove(x, player);
        if (player == 1) {
            mBoardImageView[x].setImageResource(icon);
            soundPlay(sound1);
        } else {
            soundPlay(sound2);
            // Zeitverzug für Android Schritte
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBoardImageView[x].setImageResource(R.drawable.herz_90);
                    unblockAllFields();
                }
            }, 450);
        }
        mBoardImageView[x].setEnabled(false);
    }

    public void soundPlay(MediaPlayer sound) {
        if(Player.getPlayer().getIsTonOn()) { // wenn Sound unter Optionen aktiviert
            sound.start();
        }
    }

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

    public void clearAllBlocks() {
        minimax.resetBoard();
        for (int i = 0; i < 9; i++) {
            mBoardImageView[i].setImageResource(0);
            mBoardImageView[i].setEnabled(true);
            mBoardImageView[i].setOnClickListener(new ButtonClickListener(i));
            mBoardImageView[i].setBackgroundColor(Color.argb(100, 11, 11, 59 ));
        }
    }
    private void unblockAllFields() {
        // alle Spielfelder für Mensch blockieren
        for (int i = 0; i < 9; i++) {
            mBoardImageView[i].setClickable(true);
        }
    }

    private class ButtonClickListener implements View.OnClickListener {
        int x;
        public ButtonClickListener(int i) {
            this.x = i;
        }

        private void blockAllFields() {
            // alle Spielfelder für Mensch blockieren
            for (int i = 0; i < 9; i++) {
                mBoardImageView[i].setClickable(false);
            }
        }

        // verwendet den Schwierigkeitsgrad, um zu bestimmen, welchen Algorithmus der Computer verwenden soll
        @Override
        public void onClick(View v) {
            // easy level, Spiel aktiv, Felder frei
            if (diffLevel == 0 && gameActiv && mBoardImageView[x].isEnabled()) {
                setMove(x, HUMAN); // Mensch macht einen Schritt KREUZ
                blockAllFields();
                int winner = minimax.checkGameStatus();
                if (winner == minimax.PLAYING) { // immer noch spielen
                    int[] result = minimax.easyMove();
                    setMove(result[0], COMPUTER);
                    winner = minimax.checkGameStatus();
                }
                checkWinner();
            }
            // medium
            if (diffLevel == 1 && gameActiv && mBoardImageView[x].isEnabled()) {
                setMove(x, HUMAN); // Mensch macht einen Schritt KREUZ
                blockAllFields(); // Spielfeld blockieren
                int winner = minimax.checkGameStatus();
                if (winner == minimax.PLAYING) { // immer noch spielen
                    int[] result = minimax.mediumMove();
                    setMove(result[0], COMPUTER);
                    winner = minimax.checkGameStatus();
                }
                checkWinner();
            }
            // hard
            if (diffLevel == 2 && gameActiv && mBoardImageView[x].isEnabled()) {
                setMove(x, HUMAN); // Mensch macht einen Schritt KREUZ
                blockAllFields();
                int winner = minimax.checkGameStatus();
                if (winner == minimax.PLAYING) { // immer noch spielen
                    int[] result = minimax.hardMove();
                    setMove(result[0], COMPUTER);
                    winner = minimax.checkGameStatus();
                }
                checkWinner();
            }
        }



        // prüft Spielergebnis, stellt etsprechenden Dialogfenster dar
        private void checkWinner() {
            int winner = minimax.checkGameStatus();
            if (winner == minimax.DRAW) { // unentschieden
                soundPlay(soundDraw);
                gameActiv = false;
                new Handler().postDelayed(new Runnable() { // Zeitverzug
                    @Override
                    public void run() {
                        showDrawDialog();
                    }
                }, 700);

            } else if (winner == minimax.CROSS_WON) { // X gewonnen
                gameActiv = false;
                soundPlay(soundWin);
                new Handler().postDelayed(new Runnable() { // Zeitverzug
                    @Override
                    public void run() {
                        showWinDialog();
                    }
                }, 700);

            } else if (winner == minimax.NOUGHT_WON) { // O gewonnen, X verloren
                gameActiv = false;
                soundPlay(soundLose);
                new Handler().postDelayed(new Runnable() { // Zeitverzug
                    @Override
                    public void run() {
                        showLoseDialog();
                    }
                }, 700);
            }
        }

        // Dialogfenster für Spielergebniss
        private void showLoseDialog() {
            LoseDialog loseDialog = new LoseDialog(GameSingleActivity.this, GameSingleActivity.this);
            loseDialog.show();
        }
        private void showDrawDialog() {
            DrawDialog drawDialog = new DrawDialog(GameSingleActivity.this, GameSingleActivity.this);
            drawDialog.show();
        }

        private void showWinDialog() {
            WinDialog winDialog = new WinDialog(GameSingleActivity.this, GameSingleActivity.this);
            winDialog.show();
        }

    }
}
