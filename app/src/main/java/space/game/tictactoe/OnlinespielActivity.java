package space.game.tictactoe;

import static space.game.tictactoe.R.id.icontransport;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.DialogFragmentNavigator;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import org.java_websocket.client.WebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;

import space.game.tictactoe.dialogs.InvitationOnlineGameDialog;
import space.game.tictactoe.dialogs.WaitingForOpponentDialogFragment;
import space.game.tictactoe.websocket.TttWebsocketClient;
/* Liste der zu lösenden Schwierigkeiten im Online Spiel (neben den Spielzügen):

1. Die Playerlist geht immer im oncreate auf, egal woher man kommt → sollte nur stattfinden,
 wenn man vom Hauptmenü kommt → kann man das irgendwie per Fallunterscheidungen lösen in Android?

2. Wenn der Playbutton gedrückt wurde, startet das Spiel.
5. Wenn ein Spiel gestartet wurde, dürfen keine Optionen mehr anclickbar sein,
und auch keine Playerliste etc. → Möglichkeit Imageviews auszublenden oder auszugrauen? (Android prüfen)
6. Wenn zu lange kein Zug vorgenommen wurde kann ein diconnect vorliegen,
 oder einer der Player hat sein Handy weggelegt….was dann? → Timeout einbauen? → Spieler wieder ins Matching schicken? → Fehlernachricht? Toast?
7. Ist ein Spiel beendet, durch win, lose, draw, dann kann man die Buttons alle wieder nutzen.
Wir müssen verhindern, dass zu jedem Zeitpunkt, dauernd die Spielerliste neu geclickt werden kann,
 oder Icons während des Spiels getauscht werden, oder im Spiel zurück ins Menü gesprungen wird etc. */
public class OnlinespielActivity extends AppCompatActivity {
    //booleans um Status im Blick zu behalten
    private boolean inRandomQueue = false;
    private boolean inGame = false;
    private boolean inChallengeOrChallenging = false;


    private static final String TAG = "OnlineSpiel";
    private int icon;

    private static final int iconDefault = R.drawable.stern_90;
    private TttWebsocketClient client = new TttWebsocketClient(new URI("ws://192.168.178.249:8088"), this);
    private ImageView mBoardImageView[];

    public OnlinespielActivity() throws URISyntaxException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_onlinespiel);

        //Activate websocket connection
        OnlinespielActivity.this.startConnection();
        View playerListOverlay = findViewById(R.id.overlay);

        //TODO Variable an Activity übergeben (bspw. von MenuActivity kommend), "showlist":true oder so ähnlich und hier prüfen
        //disabling playerList on load for now - favoring random matchmaking approach
/*        System.out.println("Setting playerList visible");
        try {
            playerListOverlay.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            System.out.println(e);
        }*/

        //Click listener to open Playerlist-View -> Fallunerscheidungen möglich? Je nachdem aus welcher Activity man kommt? TODO
        // Die Fallunterscheidung muss weiter oben stattfinden. Der Button hier dient ja nur dem Debugging, damit man die Liste jederzeit ein- u ausschalten kann
        TextView playerListToggle = (TextView) findViewById(R.id.listStatus);
        playerListToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    playerListOverlay.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        //Click listener to close Playerlist-View
        Button closeList = (Button) findViewById(R.id.closeList);
        closeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    playerListOverlay.setVisibility(View.GONE);

                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        //clicklistener for the playerList view
        ListView playerList = findViewById(R.id.playerList);
        playerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //intent
                System.out.println("clicked item" + parent + view + id);
                String opponentName = playerList.getItemAtPosition(position).toString();
                Toast selectedOpponent = Toast.makeText(getApplicationContext(), "Du kannst die Liste schließen.Du fragst ein Spiel an. Bitte warte auf Bestätigung von:  " + opponentName, Toast.LENGTH_SHORT);
                selectedOpponent.show();
                client.send(client.startGame(playerList.getItemAtPosition(position)));
            }
        });


        // Zufallsspiel Button
        Button restartGame = (Button)findViewById(R.id.restartGame);
        restartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Erstelle einen Dialog zum Warten auf den Gegner und den dazugehörigen Fragmentmanager
                    DialogFragment waitForOpponent = new WaitingForOpponentDialogFragment(client); //Dialog benötigt Client-Zugriff für Abbruch
                    FragmentManager fragMan = getSupportFragmentManager();
                    waitForOpponent.setCancelable(false);
                    waitForOpponent.show(fragMan, "waitOpponent");

                    //Sage dem Server, dass ich einen zufälligen Gegner möchte. Jetzt.
                    // Queue wird verlassen beim Schließen des Dialogs -> siehe WaitingForOpponentDialogFragment
                    client.randomGameQueue("start");

                } catch(Exception e) {
                    System.out.println("woah? Dialog fail: " + e);
                }
            }
        });
        // Imageview Zahnrad als Button anclickbar-> Optionen im Menü -> Weiterleitung zu Optionen->Icons->Statistiken
        ImageView zahnrad = findViewById(R.id.zahnrad_matchmaker);
        zahnrad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(OnlinespielActivity.this, OptionenActivity.class);
                    startActivity(intent);
                } catch (Exception e) {

                }
            }
        });
        // Imageview "Menu" in Online Activity-> anclickbar-> Weiterleitung ins Hauptmenü
        ImageView online_backtomenu = findViewById(R.id.online_backtomenu);
        online_backtomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(OnlinespielActivity.this, MenuActivity.class);
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
        } else{
            icon = iconDefault;
        }
        //overwrite default Icon in the ImageView of the onlinespielactivity with the chosen one from the IconWahlActivity, that was transfered above
        ImageView image = (ImageView) findViewById(icontransport);
        image.setImageResource(icon);



        //TAKEN FROM GAMEACTIVITY TO CONTROL THE BOARD
        mBoardImageView = new ImageView[9];
        for (int i = 0; i < mBoardImageView.length; i++) {
            mBoardImageView[i] = (ImageView) findViewById(getResources().getIdentifier("block" + i, "id", this.getPackageName()));
        }

        clearAllBlocks();
        unblockAllFields();


        //@TODO get selected Player
        // Intent intent = getIntent();

        //@TODO use selected Player
        // use some values from selected Player
    }
    // crossfade Methode --------------------------------------------------------------------------------------------------------CROSSFADE--------------------


    //start connection
    private void startConnection() {
        System.out.println("getting called");
        this.client.connect();
    }

    //disconnect on back button leaving the activity
    @Override
    public void onBackPressed() {
        //code
        super.onBackPressed();
        this.client.close();
    }

    //Use onResume to always check for connection when we come back
    //TODO implement reconnection if coming back from suspension - crashes the app like this
    @Override
    protected void onResume() {
        super.onResume();
        if (this.client.isClosed()) {
            System.out.println("reconnecting...");
            this.client.reconnect();
        }
    }

    //Overriding all System methods that disable the activity to also disconnect the websocket
/*    @Override
    protected void onPause() {
        super.onPause();
        this.client.close();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.client.close();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.client.close();
    }

    //TAKEN FROM GAME ACTIVITY TO CONTROL PLACING SIGNS
    public void clearAllBlocks()  {

        for (int i = 0; i < 9; i++) {
            mBoardImageView[i].setImageResource(0);
            mBoardImageView[i].setEnabled(true);
            mBoardImageView[i].setOnClickListener(new ButtonClickListener(i));
        }
    }
    private void unblockAllFields() {
        // alle Spielfelder für Mensch blockieren
        for (int i = 0; i < 9; i++) {
            mBoardImageView[i].setClickable(true);
        }
    }

    public void setMove(int x, int player) {
        //minimax.placeMove(x, player);
        if (player == 1) {
            System.out.println("Player did this " + player );

            mBoardImageView[x].setImageResource(icon);
        } else {
            // TODO implement this on websocket client
            new Runnable() {
                @Override
                public void run() {
                    mBoardImageView[x].setImageResource(R.drawable.zero);
                }
            };
        }
        mBoardImageView[x].setEnabled(false);
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
            if (mBoardImageView[x].isEnabled()) {

                System.out.println("getting clicks");
                System.out.println(x);
                System.out.println(mBoardImageView[x]);

                if (client.setMove(x)) { // if wartet auf Bestätigung des Moves durch Server - vielleicht nicht die beste Lösung UX wise
                    setMove(x, 1); // Mensch macht einen Schritt KREUZ
                }
                //blockAllFields();
            }
        }

    }
}