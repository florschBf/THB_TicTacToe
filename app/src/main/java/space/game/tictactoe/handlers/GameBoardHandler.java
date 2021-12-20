package space.game.tictactoe.handlers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import space.game.tictactoe.R;
import space.game.tictactoe.websocket.TttWebsocketClient;

public class GameBoardHandler {
    //TAKEN AND MODIFIED FROM GAME ACTIVITY TO CONTROL PLACING SIGNS
    private ImageView[] mBoardImageView;
    private int icon;
    private TttWebsocketClient client;
    private Context context;

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
        } else {
            System.out.println("Remote move received!");
            // TODO get opponent icon
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("runnable running mark of tile!");
                    mBoardImageView[x].setImageResource(R.drawable.zero);
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
}
