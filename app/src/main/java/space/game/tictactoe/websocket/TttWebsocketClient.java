package space.game.tictactoe.websocket;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.net.URI;
import java.nio.ByteBuffer;
import java.text.ParseException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;

import space.game.tictactoe.R;
import space.game.tictactoe.handlers.GameBoardHandler;
import space.game.tictactoe.handlers.GameSessionHandler;
import space.game.tictactoe.models.Player;


public class TttWebsocketClient extends WebSocketClient{
    private Context context;
    private final TttMessageHandler msgHandler = new TttMessageHandler();
    private final TttCommandHandler cmdHandler = new TttCommandHandler();
    private PlayerListHandler listHandler;
    private GameBoardHandler gameBoard;
    private GameSessionHandler session;
    public Player player;

    public void setGameBoard(GameBoardHandler gameBoard) {
        this.gameBoard = gameBoard;
    }

    //booleans um Status im Blick zu behalten
    private boolean inRandomQueue = false;
    private boolean inGame = false;
    private boolean inChallengeOrChallenging = false;

    public boolean isInRandomQueue() {
        return inRandomQueue;
    }

    public void setInRandomQueue(boolean inRandomQueue) {
        this.inRandomQueue = inRandomQueue;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public boolean isInChallengeOrChallenging() {
        return inChallengeOrChallenging;
    }

    public void setInChallengeOrChallenging(boolean inChallengeOrChallenging) {
        this.inChallengeOrChallenging = inChallengeOrChallenging;
    }


    public TttWebsocketClient(URI serverURI, Context context) {
        super(serverURI);
        this.context = context;
        this.listHandler = new PlayerListHandler(context);

    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        /*
         * @TODO add this player and firebase-ID
         * Player: randomname/ defaultname+nr
         **/
        this.player = Player.getPlayer();


        send("{\"topic\":\"signup\",\"command\":\"register\",\"player\":\"" + player.getName() +"\",\"firebaseId\":\""+ player.getFirebaseId() +"\"}");
        System.out.println("new connection opened");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
    }


    /*  HIER BEI ON MESSAGE FINDET DER DATENAUSTAUSCH MIT DEM SERVER STATT
    *   KOMMT EINE MESSAGE VOM SERVER AN, WIRD SIE ANHAND DES PROTOKOLLS GEPRÜFT UND AUSGEWERTET
    *   EIGENE KLASSE FÜR DIE PRÜFUNG: TttMessageHandler
     */
    @Override
    public void onMessage(String message) {
        System.out.println("received message: " + message);
        String handledMessage = null;
        Integer x = null;

        try{
            handledMessage = this.msgHandler.handle(message);
        } catch (ParseException | JSONException e){
            e.printStackTrace();
        }

        switch (handledMessage){
            case ("playerList"):
                this.listHandler.renderList(message);
                break;
            case ("gameStarted!"):
                setInRandomQueue(false);
                setInGame(true);
                //need to disable dialog from here if there is one, kinda messy..
                try{
                    AppCompatActivity here = (AppCompatActivity)context;
                    FragmentManager myManager = here.getSupportFragmentManager();
                    DialogFragment queueDialog = (DialogFragment) myManager.getFragments().get(0);
                    queueDialog.dismiss();
                }
                catch (Exception e){
                    System.out.print("no dialog present after all: " + e);
                }

                //get a GameSessionHandler going, set turn false for now, GameSessionHandler knows what to do with the board
                this.session = new GameSessionHandler(gameBoard);
                this.session.setMyTurn(false);
                break;
            case ("turnInfo"):
                //my turn? let's unblock the fields, else wait
                try {
                    System.out.println("setting turn");
                    this.session.setMyTurn(session.findTurnInfo(message));
                } catch (ParseException e) {
                    System.out.println("sth wrong with turn message");
                    e.printStackTrace();
                }
                break;
            case ("0"):
            case ("1"):
            case ("2"):
            case ("3"):
            case ("4"):
            case ("5"):
            case ("6"):
            case ("7"):
            case ("8"):
                x = Integer.valueOf(handledMessage);
                System.out.println(x);
                gameBoard.renderMove(x);
                session.setMyTurn(true);
                break;
            case ("Zug bestätigt"):
                session.setMyTurn(false);
                break;
            default:
                System.out.println("Error, message handling failed");
        }
    }

    @Override
    public void onMessage(ByteBuffer message) {
        System.out.println("received ByteBuffer");
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }

    public String startGame(Object selectedPlayer) {
        return cmdHandler.startGame(selectedPlayer);
    }

    public boolean sendMoveToServer(Integer feld){
        //TODO implement server validation
        boolean moveValid = true;
        String command = cmdHandler.sendMove(feld.toString());
        send(command);
        return moveValid;
    }


    /**
     * Methode um in die Liste für Zufallsspiele auf dem Server ein- bzw ausgetragen zu werden
     * @param todo String start|stop
     */
    public void randomGameQueue(String todo) {
        switch (todo){
            case "start":
                send(this.cmdHandler.startRandom());
                this.setInRandomQueue(true);
                break;
            case "stop":
                send(this.cmdHandler.stopRandom());
                this.setInRandomQueue(false);
                break;
            default:
                System.out.println("Error, random queue implementation is Start|Stop, nothing else");
        }

    }
}
