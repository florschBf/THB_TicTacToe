package space.game.tictactoe.menu;

import static space.game.tictactoe.R.id.icontransport;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import space.game.tictactoe.R;
import space.game.tictactoe.dialogs.AnnehmDialogFragment;
import space.game.tictactoe.dialogs.DrawDialog;
import space.game.tictactoe.dialogs.LoseDialog;
import space.game.tictactoe.dialogs.WaitingForOpponentDialogFragment;
import space.game.tictactoe.dialogs.WinDialog;
import space.game.tictactoe.handlers.GameBoardHandler;
import space.game.tictactoe.handlers.websocketHandler.TttWebsocketClient;
import space.game.tictactoe.menu.options.IconwahlActivity;
import space.game.tictactoe.models.Player;
import space.game.tictactoe.models.Sound;

/** activity for online player mode
 *
 */
public class OnlinespielActivity extends AppCompatActivity {


    private static final String TAG = "OnlineSpiel";
    private int icon;

    private Map<String, String> headers = new HashMap<>();
    private TttWebsocketClient client = new TttWebsocketClient(new URI("wss://ttt-server-gizejztnta-ew.a.run.app/"), headers, this);
    private ImageView mBoardImageView[];
    private GameBoardHandler gameBoard;
    public FragmentManager fragMan = getSupportFragmentManager();

    private final Player player = Player.getPlayer();
    /** sounds for win, lose, draw
     *
     */
    private MediaPlayer sound1, sound2, soundWin, soundLose, soundDraw;
    Button ton;
    int i = 1; // 1 = ton on, 0 = ton off

    public OnlinespielActivity() throws URISyntaxException {
    }


    public TttWebsocketClient getClient() {
        return client;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updatePlayerIcon();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_onlinespiel);
        //check for custom play_board selection
        int board = player.getBoard();
        if (board != 0){
            this.findViewById(R.id.tableLayout).setBackgroundResource(board);
        }

        //Sound-Icon für Ton wiedergabe referenzieren
         //sound icon to reference sound reproduction

        ton = (Button)findViewById(R.id.ton);
        if(player.getIsTonOn()) {
            ton.setBackgroundResource(R.drawable.ic_baseline_music_note_24); // Icon-Darstellung: Ton eingeschaltet
        } else {
            ton.setBackgroundResource(R.drawable.ic_baseline_music_off_24); // Icon-Darstellung: Ton ausgeschaltet
        }


         // TouchListener-Methode, um Sound ein- und -ausschalten
         // touch listender method to switch sound on and off

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
                     // send game request, close player list

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


        //Zufallsspiel Button
         // button for random game

        Button restartGame = (Button)findViewById(R.id.restartGame);
        restartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!client.isInRandomQueue() && !client.isInGame() && !client.isInChallengeOrChallenging()){
                    try {
                        //Erstelle einen Dialog zum Warten auf den Gegner und den dazugehörigen Fragmentmanager
                        //dialog to wait for the opponent and the associated fragment manager

                        DialogFragment waitForOpponent = new WaitingForOpponentDialogFragment(client); //Dialog benötigt Client-Zugriff für Abbruch
                        waitForOpponent.setCancelable(false);
                        waitForOpponent.show(fragMan, "waitOpponent");

                        //Sage dem Server, dass ich einen zufälligen Gegner möchte. Jetzt.
                         // tell the server, that a random opponent is needed, now.

                        // Queue wird verlassen beim Schließen des Dialogs -> siehe WaitingForOpponentDialogFragment
                         // queue will be left when dialog is closed -> reference WaitingForOpponentDialogFragment

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

        ImageView imagechange = findViewById(R.id.icontransport);
        imagechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(OnlinespielActivity.this, IconwahlActivity.class);
                    startActivity(intent);
                    client.endGameNow();
                    client.cleanSlate();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        // Imageview "Menu" in Online Activity-> anclickbar-> Weiterleitung ins Hauptmenü
         // imageview menu, in online activity -> can be clicked -> forwarding to main menu

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
        //Datatransfair from IconwahlActivity ->
         // chosen Icon gets transported into OnlinespielActivity from  Iconactivity


        final Intent intent = getIntent();
        //Test ob auch wirklich ein playericon geschickt wurde, just in case...sonst wird eines default gesetzt
         // test if really a playericon was set, if not, a default icon is being set

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


    }

    /**start connection
     *
     */
    private void startConnection() {
        System.out.println("getting called");
        this.client.connect();
    }

    /**disconnect on back button leaving the activity
     *
     */
    @Override
    public void onBackPressed() {
        //code
        super.onBackPressed();
        this.client.close();
    }

    /**Use onResume to always check for connection when we come back
     *
     */
    @Override
    protected void onResume() {
        System.out.println("reconnecting...");
        super.onResume();
        if (this.client.isClosed()){
            theHardestResetGo();
            this.client.reconnect();
        }

    }

    /**Overriding all System methods that disable the activity to also disconnect the websocket
     *
     */
       @Override
    protected void onPause() {
        super.onPause();
        System.out.println("triggering onPause...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.client.close();
    }

    @Override
    protected void onStop() {
        System.out.println("stopping...");
        super.onStop();
        theHardestResetGo();
    }

    /** Dialogfenster für Spielergebnis
     * dialog window for game results
     */
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

    private void theHardestResetGo(){
        System.out.println("reset activity state called");
        this.client.close();
        this.client.cleanSlate();
        //this.client.setGameBoard(new GameBoardHandler(mBoardImageView, icon, client, this));
/*        fragMan.getFragments().clear();*/
    }

}