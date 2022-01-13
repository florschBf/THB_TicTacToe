package space.game.tictactoe;

import static space.game.tictactoe.R.id.icontransport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import space.game.tictactoe.dialogs.AnnehmDialogFragment;
import space.game.tictactoe.dialogs.ChallengeDialogFragment;
import space.game.tictactoe.dialogs.DrawDialog;
import space.game.tictactoe.dialogs.InvitationOnlineGameDialog;
import space.game.tictactoe.dialogs.LoseDialog;
import space.game.tictactoe.dialogs.WaitingForOpponentDialogFragment;
import space.game.tictactoe.dialogs.WinDialog;
import space.game.tictactoe.handlers.GameBoardHandler;
import space.game.tictactoe.models.Player;
import space.game.tictactoe.models.Sound;
import space.game.tictactoe.websocket.TttWebsocketClient;
/* Liste der zu lösenden Schwierigkeiten im Online Spiel:
5. Wenn ein Spiel gestartet wurde, dürfen keine Optionen mehr anclickbar sein,
und auch keine Playerliste etc. → Möglichkeit Imageviews auszublenden oder auszugrauen? (Android prüfen)
7. Ist ein Spiel beendet, durch win, lose, draw, dann kann man die Buttons alle wieder nutzen.
Wir müssen verhindern, dass zu jedem Zeitpunkt, dauernd die Spielerliste neu geclickt werden kann,
 oder Icons während des Spiels getauscht werden, oder im Spiel zurück ins Menü gesprungen wird etc. */
public class OnlinespielActivity extends AppCompatActivity {


    private static final String TAG = "OnlineSpiel";
    private int icon;

    // private static int iconDefault = R.drawable.stern_90;
    private Map<String, String> headers = new HashMap<>();
    private TttWebsocketClient client = new TttWebsocketClient(new URI("ws://192.168.178.52:8080"), headers, this);
    private ImageView mBoardImageView[];
    private GameBoardHandler gameBoard;
    public FragmentManager fragMan = getSupportFragmentManager();

    private final Player player = Player.getPlayer();
    // Sound
    private MediaPlayer sound1, sound2, soundWin, soundLose, soundDraw;
    Button ton;
    int i = 1; // 1 = ton on, 0 = ton off

    public OnlinespielActivity() throws URISyntaxException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updatePlayerIcon();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_onlinespiel);

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

        soundWin = MediaPlayer.create(this, R.raw.win);
        soundLose = MediaPlayer.create(this, R.raw.lose);
        soundDraw = MediaPlayer.create(this, R.raw.draw);

        //Activate websocket connection

        this.startConnection();
        View playerListOverlay = findViewById(R.id.overlay);

        //Click listener to open Playerlist-View
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
                if(!client.isInRandomQueue() && !client.isInGame() && !client.isInChallengeOrChallenging()) {
                    System.out.println("clicked item" + parent + view + id);
                    System.out.println("Position is: " + position + ", getting that opponent");
                    String firebaseId = client.getPlayerFromList(position);
                    System.out.println("Oppos firebase & server ID: " + firebaseId);
                    String opponentName = playerList.getItemAtPosition(position).toString();

                    //sende Spielanfrage, schließe Spielerliste
                    client.send(client.startGame(firebaseId));
                    playerListOverlay.setVisibility(View.GONE);

                    //Erstelle einen Dialog zum Warten auf den Gegner und den dazugehörigen Fragmentmanager
                    DialogFragment waitForOpponent = new WaitingForOpponentDialogFragment(client); //Dialog benötigt Client-Zugriff für Abbruch
                    FragmentManager fragMan = getSupportFragmentManager();
                    waitForOpponent.setCancelable(false);
                    waitForOpponent.show(fragMan, "waitOpponent");
                }
                else {
                    Toast cantChallenge = Toast.makeText(getApplicationContext(), "Du kannst gerade keine Challenge schicken. Es läuft bereits etwas mit Dir", Toast.LENGTH_SHORT);
                    cantChallenge.show();
                }
            }
        });


        // Zufallsspiel Button
        Button restartGame = (Button)findViewById(R.id.restartGame);
        restartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!client.isInRandomQueue() && !client.isInGame() && !client.isInChallengeOrChallenging()){
                    try {
                        //Erstelle einen Dialog zum Warten auf den Gegner und den dazugehörigen Fragmentmanager
                        DialogFragment waitForOpponent = new WaitingForOpponentDialogFragment(client); //Dialog benötigt Client-Zugriff für Abbruch
                        waitForOpponent.setCancelable(false);
                        waitForOpponent.show(fragMan, "waitOpponent");

                        //Sage dem Server, dass ich einen zufälligen Gegner möchte. Jetzt.
                        // Queue wird verlassen beim Schließen des Dialogs -> siehe WaitingForOpponentDialogFragment
                        if(!client.isInRandomQueue() && !client.isInGame() && !client.isInChallengeOrChallenging()) {
                            client.randomGameQueue("start");
                        }
                        else {
                            System.out.println("already in queue... weird, but not a problem");
                        }

                    } catch(Exception e) {
                        System.out.println("woah? Dialog fail: " + e);
                    }
                }
                else{
                    System.out.println("already gaming");
                    Toast errorPlay = Toast.makeText(v.getContext(), "Du bist bereits in einem Spiel.", Toast.LENGTH_SHORT);
                    errorPlay.show();
                }

            }
        });
        // Imageview Zahnrad als Button anclickbar-> Optionen im Menü -> Weiterleitung zu Optionen->Icons->Statistiken
        /*ImageView zahnrad = findViewById(R.id.zahnrad_matchmaker);
        zahnrad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(OnlinespielActivity.this, OptionenActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                }
            }
        });*/
        ImageView imagechange = findViewById(R.id.icontransport);
        imagechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    client.endGameNow();
                    client.cleanSlate();
                    Intent intent = new Intent(OnlinespielActivity.this, IconwahlActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
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
        }
        //overwrite default Icon in the ImageView of the onlinespielactivity with the chosen one from the IconWahlActivity, that was transfered above
        ImageView image = (ImageView) findViewById(icontransport);
        image.setImageResource(icon);

        //TAKEN AND MODIFIED FROM GAMEACTIVITY TO CONTROL THE BOARD
        mBoardImageView = new ImageView[9];
        for (int i = 0; i < mBoardImageView.length; i++) {
            System.out.println("populating ImageView Array " + i);
            mBoardImageView[i] = (ImageView) findViewById(getResources().getIdentifier("block" + i, "id", this.getPackageName()));
        }
        //hand over the ImageView[] to the GameBoardHandler
        this.gameBoard = new GameBoardHandler(mBoardImageView, icon, client, this);
        this.gameBoard.clearAllBlocks();
        this.gameBoard.blockAllFields();
        client.setGameBoard(this.gameBoard);

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
    @Override
    protected void onResume() {
        super.onResume();
        if (this.client.isClosed()) {
            System.out.println("reconnecting...");
            this.client.reconnect();
        }
        updatePlayerIcon();
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

    // Dialogfenster für Spielergebniss
    public void showLoseDialog() {
        LoseDialog loseDialog = new LoseDialog(this, OnlinespielActivity.this);
        Sound.soundPlay(soundLose);
        loseDialog.show();
    }
    public void showDrawDialog() {
        DrawDialog drawDialog = new DrawDialog(this, OnlinespielActivity.this);
        Sound.soundPlay(soundDraw);
        drawDialog.show();
    }

    public void showWinDialog() {
        WinDialog winDialog = new WinDialog(this, OnlinespielActivity.this);
        Sound.soundPlay(soundWin);
        winDialog.show();
    }

    public void startChallengeProcess(String oppoName){
        System.out.println("challenge started!");
        AnnehmDialogFragment challenged = new AnnehmDialogFragment(client, oppoName);
        challenged.setCancelable(false);
        challenged.show(fragMan, "challenge");
    }

    private void updatePlayerIcon(){
        try {
            this.icon = player.getIcon();
        }
        catch (Exception e){
            e.printStackTrace();
            this.icon = R.drawable.stern_90;
        }
    }

}