package space.game.tictactoe.handlers.websocketHandler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.health.SystemHealthManager;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.net.URI;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;

import space.game.tictactoe.R;
import space.game.tictactoe.dialogs.GameConfirmedDialogFragment;
import space.game.tictactoe.menu.OnlinespielActivity;
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

    // Setters & Getters collection
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
    public GameSessionHandler getSession() {
        return session;
    }

    /**
     * Constructor for websocket client
     * @param serverURI The server URL, either in format "wss://google-cloud-run-url" or as "ws://yourlocal_IP:8080"
     * @param headers Possible headers to pass along to server, currently not used
     * @param context The calling activity context to have it on hand. Client does a lot of things where it's needed.
     */
    public TttWebsocketClient(URI serverURI, Map<String, String> headers, Context context) {
        super(serverURI, headers);
        this.context = context;
        this.listHandler = new PlayerListHandler(context);

    }

    /**
     * Methode wird bei Öffnung der Websocket-Verbindung ausgeführt, meldet sich mit TTT-Protokoll beim Server an
     * @param handshakedata
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        this.player = Player.getPlayer();
        send("{\"topic\":\"signup\",\"command\":\"register\",\"player\":\"" + player.getName() +"\",\"firebaseId\":\""+ player.getFirebaseId() +"\"}");
        System.out.println("new connection opened");
    }

    /**
     * Methode wird beim Schließen der Websocket-Verbindung ausgeführt
     * @param code
     * @param reason
     * @param remote
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
        this.cleanSlate();
        this.gameBoard.clearOppoName();
        this.gameBoard.clearAllBlocks();
        this.gameBoard.blockAllFields();
    }


    /**
     * Hier bei der onMessage findet der Datenaustausch mit dem Server statt
     * Kommt eine Message vom Server an, wird sie anhand des Protokolls geprüft und ausgewertet
     * Eigene Klasse für die Prüfung ist der TttMessageHandler mit seinen Unter-Handlern
     * @param message Der vom Server empfangene String im JSON-Format, TTT-Protokoll V2.0
     */
    @Override
    public void onMessage(String message) {
        this.player = Player.getPlayer();
        System.out.println("received message: " + message);
        String handledMessage = null;
        Integer x;

        try{
            //Hier findet die Auswertung der Nachricht durch den Handler statt
            handledMessage = this.msgHandler.handle(message);
        } catch (ParseException | JSONException e){
            e.printStackTrace();
        }

        //In diesem switch statement wird das Resultat umgesetzt
        switch (handledMessage){
            case ("playerList"):
                this.listHandler.renderList(message);
                break;
            case ("Set playerUID already"):
                //nothing more to do here, UID is set through messageHandler already
                break;
            case ("challenged!"):
                setInChallengeOrChallenging(true);
                OnlinespielActivity gameActivity = (OnlinespielActivity)context;
                System.out.println("starting challenge process");
                gameActivity.startChallengeProcess(msgHandler.getOpponentNameFromMessage(message));
                break;
            case ("gameStarted!"):
                setInRandomQueue(false);
                setInChallengeOrChallenging(false);
                setInGame(true);
                String oppoName = msgHandler.getOpponentNameFromMessage(message);
                String oppoIconId = msgHandler.getOpponentIconIdFromMessage(message);

                if (Integer.parseInt(oppoIconId) == player.getIcon() || oppoIconId == "0") {
                    //sth wrong with oppo icon ID, getting the default heart
                    oppoIconId = Integer.toString(R.drawable.zero); //Integer.toString(context.getResources().getInteger(R.drawable.));
                }

                //need to disable dialog from here if there is one, kinda messy..
                killDialog();
                showGameConfirmDialog(oppoName, oppoIconId);


                //get a GameSessionHandler going, set turn false for now, GameSessionHandler knows what to do with the board
                this.gameBoard.setOpponentIcon(oppoIconId);
                this.gameBoard.setOpponentName(oppoName);
                this.session = new GameSessionHandler(gameBoard);
                this.session.setMyTurn(false);
                this.send(cmdHandler.readyForGame());
                break;
            case ("game denied"):
                cleanSlate();
                killDialog();
                gameBoard.showNotification("oppoQuit");
                break;
            case ("opponentState"):
                listHandler.toggleOppoBusyState(listHandler.getOppoFromId(msgHandler.getOpponentIdFromMessage(message)));
                break;
            case ("youwin"):
                this.player.increaseWins();
                System.out.println("I won, I won");
                session.setGameOver("youWin");
                cleanSlate();
                break;
            case ("youlose"):
                this.player.increaseLosses();
                System.out.println("I lost, oh no");
                session.setGameOver("youLose");
                cleanSlate();
                break;
            case ("draw"):
                this.player.increaseDraws();
                System.out.println("It's a draw, how exciting");
                session.setGameOver("draw");
                cleanSlate();
                break;
            case ("gameTerminatedDisco"):
                this.player.increaseInterrupted();
                //opponent disconnected, ending game session, resetting activity
                System.out.println("My opponent disconnected, bummer");
                System.out.println("do we have a gamesession already?");
                try {
                    System.out.println("disconnecting gamesession");
                    session.setGameOver("disconnect");
                }
                catch (Exception e){
                    System.out.println("no session, assuming we are being challenged and oppo bailed");
                    killDialog();
                }
                cleanSlate();
                confirmQuit(); // need to be freed from server queue
                break;
            case ("gameTerminatedQuit"):
                this.player.increaseInterrupted();
                //opponent quit, ending game session, resetting activity
                System.out.println("My opponent quit with proper protocol... bummer");
                session.setGameOver("oppoQuit");
                cleanSlate();
                break;
            case ("gameEndedOnServer"):
                this.player.increaseInterrupted();
                //Server confirms my quit... well I already cleaned everything when quitting so I don't care.
                System.out.println("Game termination confirmed");
                session.setGameOver("endForNoReason");
                cleanSlate();
                break;
            case ("gameTerminated"):
                this.player.increaseInterrupted();
                //Wird momentan nicht zurückgegeben, stattdessen Unterscheidung in quit und disconnect. Nötig?
                //ending game session, resetting activity
                System.out.println("Game terminated, bummer");
                session.setGameOver("endForNoReason");
                cleanSlate();
                session.hardReset();
                killDialog();
                confirmQuit();
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

    public String startGame(String selectedOppoId) {
        return cmdHandler.startGame(selectedOppoId);
    }

    public boolean sendMoveToServer(Integer feld){

        boolean moveValid = true;
        String command = cmdHandler.sendMove(feld.toString());
        send(command);
        return moveValid;
    }

    /**
     * Method to clear all player game flags
     */
    public void cleanSlate(){
        System.out.println("removing all ingame, inqueue, inchallenge flags");
        setInRandomQueue(false);
        setInGame(false);
        setInChallengeOrChallenging(false);
        killDialog();
    }

    /**
     * Method to kill the first dialog on Activity
     */
    public void killDialog(){
        try{
            AppCompatActivity here = (AppCompatActivity)context;
            FragmentManager myManager = here.getSupportFragmentManager();
            DialogFragment dialog = (DialogFragment) myManager.getFragments().get(0);
            dialog.dismiss();
        }
        catch (Exception e){
            System.out.print("no dialog present after all: " + e);
        }
    }

    public void showGameConfirmDialog(String oppoName, String oppoIcon){
        try{
            System.out.println("rendering game confirmation dialog");
            AppCompatActivity here = (AppCompatActivity)context;
            FragmentManager myManager = here.getSupportFragmentManager();
            DialogFragment gameConfirmed = new GameConfirmedDialogFragment(oppoName, oppoIcon);
            gameConfirmed.setCancelable(true);
            gameConfirmed.show(myManager, "Game started");
        }
        catch (Exception e){
            System.out.println("failed to render game confirmation dialog");
            e.printStackTrace();
        }
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

    /**
     * Method to get opponent IDs after click on list items
     * @param listPos the position in the list that was clicked
     * @return String of the firebase/server UID of the player to challenge
     */
    public String getPlayerFromList(int listPos){
        return listHandler.getOppoFromListPos(listPos);
    }

    /**
     * Method to answer game challenges
     * @param answer String desired answer for server&opponent
     */
    public void answerChallenge(String answer){
        switch(answer){
            case "accept":
                send(this.cmdHandler.acceptGame());
                break;
            case "deny":
                send(this.cmdHandler.denyGame());
                break;
        }
    }

    public void endGameNow(){
        send(this.cmdHandler.endGame());
    }

    public void confirmQuit() {
        System.out.println("confirming the quit message");
        send(this.cmdHandler.confirmQuit());
    }

    public boolean getOppoBusyState(String oppoId){
        if (listHandler.getOppoFromId(oppoId).isBusy()){
            return true;
        }
        else { return false;}
    }
}
