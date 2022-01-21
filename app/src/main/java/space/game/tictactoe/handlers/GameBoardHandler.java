package space.game.tictactoe.handlers;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import space.game.tictactoe.menu.OnlinespielActivity;
import space.game.tictactoe.R;
import space.game.tictactoe.dialogs.WaitingForOpponentDialogFragment;
import space.game.tictactoe.handlers.websocketHandler.TttWebsocketClient;
import space.game.tictactoe.models.Player;

public class GameBoardHandler {
    //TAKEN AND MODIFIED FROM GAME ACTIVITY TO CONTROL PLACING SIGNS
    /**
     * Declaration and in itialization of membervariables
     */
    private ImageView[] mBoardImageView;
    private int icon;
    private String opponentName;
    private int opponentIcon;
    private TttWebsocketClient client;
    private Context context;


    /**
     * constructor of class GameBoardHandler
     * initialize membervariables with params
     * @param mBoardImageView draws the gameboard where the icons are show
     * @param icon icon to display in the board
     * @param client websocketconnection of the client to the server
     * @param context show the board in this context
     */
    public GameBoardHandler(ImageView[] mBoardImageView, int icon, TttWebsocketClient client, Context context){
        this.mBoardImageView = mBoardImageView;
        this.context = context;
        this.icon = icon;
        this.client = client;
    }

    /**
     * Methode um alle Spielfelder auf leer und klickbar zu setzen
     */
    public void clearAllBlocks()  {
        //HAS TO RUN ON UITHREAD OR WE CANT KEEP UPDATING
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("clearing all blocks");
                for (int i = 0; i < 9; i++) {
                    mBoardImageView[i].setImageResource(0);
                    mBoardImageView[i].setEnabled(true);
                    mBoardImageView[i].setOnClickListener(new ButtonClickListener(i));
                }
            }
        });
    }

    /**
     * Methode um alle Spielfelder, die noch nicht markiert sind, klickbar zu schalten
     */
    public void unblockAllFields() {
        System.out.println("entsperre");
        // alle Spielfelder für Mensch freischalten im UI Thread
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 9; i++) {
                    mBoardImageView[i].setClickable(true);
                }
            }
        });
    }

    /**
     * Methode um alle Spielfelder für Klicks zu sperren
     */
    public void blockAllFields(){
        System.out.println("blocking");
        //alle Spielfelder für Mensch blockieren im UI Thread
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 9; i++) {
                    mBoardImageView[i].setClickable(false);
                }
            }
        });
    }

    /**
     * Methode um Felder mit Icons zu markieren
     * @param x Nummer des Feldes von links oben nach rechts unten
     * @param player 1 für den lokalen Spieler, != 1 Gegenspieler
     */
    public void setMove(int x, int player) {

        if (player == 1) {
            System.out.println("Player did this " + player );
            mBoardImageView[x].setImageResource(icon);
            if(Player.getPlayer().getIsTonOn()) {
                MediaPlayer.create(((Activity) context), R.raw.tapeone).start();
            }
        } else {
            System.out.println("Remote move received!");
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("runnable running mark of tile!");
                    System.out.println(opponentIcon);
                    try{
                        if (opponentIcon != icon && opponentIcon != 0){
                            mBoardImageView[x].setImageResource(opponentIcon);
                        }
                        else {
                            mBoardImageView[x].setImageResource(R.drawable.zero);
                        }
                        if(Player.getPlayer().getIsTonOn()) {
                            MediaPlayer.create(((Activity) context), R.raw.tapetwo).start();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        mBoardImageView[x].setImageResource(R.drawable.zero);
                    }
                }
            });
        }
        mBoardImageView[x].setEnabled(false);
    }
    /**
     * Method to render received opponent moves
     * @param feld Feld auf dem Spielfeld, 0-8, oben links bis unten rechts
     */
    public void renderMove(Integer feld){
        //find field and mark it for opponent
        System.out.println("renderMove called");
        setMove(feld, 2);
    }

    /**
     * Methode zum Darstellen von Benachrichtigungen in der Onlinespiel-Activity
     * @param reason String zur Auswahl der Benachrichtigung
     */
    public void showNotification(String reason) {
        OnlinespielActivity here = (OnlinespielActivity) context;

        here.runOnUiThread(new Runnable() {
            public void run() {
                if (client.isInChallengeOrChallenging()){
                    try{
                        System.out.println("dismissing dialog..");
                        WaitingForOpponentDialogFragment dialog = (WaitingForOpponentDialogFragment) here.fragMan.getFragments().get(0);
                        dialog.dismiss();
                    }
                    catch (Exception e){
                        System.out.println("no dialog to dismiss after all..");
                    }
                }
                System.out.println("being called to display notification toast");
                switch (reason) {
                    case ("disconnect"):
                        Toast oppoDisco = Toast.makeText(context, "Die Verbindung deines Mitspielers ist abgebrochen... sorry. Spiel beendet.", Toast.LENGTH_SHORT);
                        oppoDisco.show();
                        break;
                    case ("oppoQuit"):
                        Toast oppoQuit = Toast.makeText(context, "Dein Mitspieler hat das Spiel abgebrochen... sorry. Spiel beendet.", Toast.LENGTH_SHORT);
                        oppoQuit.show();
                        break;
                    case ("youWin"):
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                here.showWinDialog();
                            }}, 250);
                        Toast youWon = Toast.makeText(context, "Du hast gewonnen! Resette Activity.", Toast.LENGTH_SHORT);
                        youWon.show();
                        break;
                    case ("youLose"):
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                here.showLoseDialog();                    }
                        }, 250);
                        Toast youLose = Toast.makeText(context, "Du hast verloren! Resette Activity.", Toast.LENGTH_SHORT);
                        youLose.show();
                        break;
                    case ("draw"):
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                here.showDrawDialog(); }
                        }, 250);
                        Toast draw = Toast.makeText(context, "Unentschieden! Resette Activity.", Toast.LENGTH_SHORT);
                        draw.show();
                        break;
                    case ("endForNoReason"):
                        Toast end = Toast.makeText(context, "Ich musste dein Spiel beenden, sorry. Resette Activity.", Toast.LENGTH_SHORT);
                        end.show();
                        break;
                    default:
                        Toast err = Toast.makeText(context, "Dunno what to say... sorry.", Toast.LENGTH_SHORT);
                        err.show();
                }
            }
        });
    }

    /**
     * Methode zum Setzen des Gegenspieler-Namens
     */
    public void renderOpponentName() {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("setting oppo name: " +  opponentName);
                TextView oppoName = ((Activity) context).findViewById(R.id.oppo_name);
                try{
                    oppoName.setText(opponentName);
                }
                catch (Exception e){
                    e.printStackTrace();
                    oppoName.setText("Freund-X");
                }
            }
        });
    }

    /**
     * Methode zum Zurücksetzen der Spielinfos
     */
    public void clearOppoName(){
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("clearing opponent name");
                TextView oppoName = ((Activity)context).findViewById(R.id.oppo_name);
                oppoName.setText(R.string.no_game_yet);
                System.out.println("resetting turn info");
                TextView turnInfo = ((Activity)context).findViewById(R.id.turn_info);
                turnInfo.setText(R.string.placeholder_turn);
            }
        });
    }

    /**
     * Method to set turn info on gameboard
     * @param whoseTurn int to signify whose turn it is
     *                  0 -> reset, no game
     *                  1 -> player turn
     *                  2 -> opponent turn
     */
    public void setTurnInfo(int whoseTurn){
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("setting turn info");
                TextView turnInfo = ((Activity)context).findViewById(R.id.turn_info);
                switch (whoseTurn){
                    case (0):
                        turnInfo.setText(R.string.placeholder_turn);
                        break;
                    case(1):
                        turnInfo.setText(R.string.your_turn);
                        break;
                    case(2):
                        turnInfo.setText(R.string.oppo_turn);
                        break;
                }
            }
        });
    }


    private class ButtonClickListener implements View.OnClickListener {
        int x;

        public ButtonClickListener(int i) {
            this.x = i;
        }

        @Override
        public void onClick(View v) {
            if (mBoardImageView[x].isEnabled() && mBoardImageView[x].getDrawable() == null) {
                blockAllFields();
                System.out.println("getting clicks");

                System.out.println(mBoardImageView[x].getResources());
                setMove(x, 1); // Mensch macht einen Schritt KREUZ
                System.out.println(mBoardImageView[x].getResources());

                if (client.sendMoveToServer(x)) { // if wartet auf Bestätigung des Moves durch Server
                    System.out.println("clicked field was blocked");
                    Toast waitForOppo = Toast.makeText(v.getContext(), "Dein Mitspieler ist jetzt dran. Einen Moment Geduld...", Toast.LENGTH_SHORT);
                    waitForOppo.show();
                }
                else{
                    System.out.println("clicked field was blocked");
                    Toast errorMarking = Toast.makeText(v.getContext(), "Da ist was schief gegangen mit dem Zug am Server, synchronisiere... Versuch es nochmal.", Toast.LENGTH_SHORT);
                    errorMarking.show();
                    unblockAllFields();
                }
            }
            else {
                //
            }
        }
    }
    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;

    }

    public int getOpponentIcon() {
        return opponentIcon;
    }

    public void setOpponentIcon(String opponentIcon) {
        System.out.println("Setting opponent icon: " + Integer.parseInt(opponentIcon));
        this.opponentIcon = Integer.parseInt(opponentIcon);
    }
}
