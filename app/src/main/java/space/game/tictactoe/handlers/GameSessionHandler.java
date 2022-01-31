package space.game.tictactoe.handlers;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.ParseException;

/**
 * Klasse zum Verwalten einer Spielesession
 * Wer ist dran, wer gewinnt, Spielfeld aufräumen
 */
public class GameSessionHandler {
    /**
     * Declaration and in itialization of membervariables
     */
    private GameBoardHandler gameBoard;
    private boolean myTurn = false;
    private boolean gameOver = false;

    /**
     * constructor of class GameBoardHandler
     * updates membervariables
     * @param gameBoard uses this gameboard, including icons, where the gamesession takes place
     */
    public GameSessionHandler (GameBoardHandler gameBoard){
        this.gameBoard = gameBoard;
        this.gameBoard.renderOpponentName();
        System.out.println(gameBoard);
    }

    /**
     * Methode die das laufende Spiel beendet und den Spieler informiert
     * @param reason String der den Grund für das Spielende beinhaltet
     *               Valide: disconnect|oppoQuit|youWin|youLose|draw|endForNoReason
     */
    public void setGameOver(String reason) {
        //Game's over...
        this.gameOver = true;
        switch (reason){
            case("disconnect"):
                System.out.println("disconnect handling...");
                gameBoard.showNotification("disconnect");
                hardReset();
                break;
            case ("oppoQuit"):
                gameBoard.showNotification("oppoQuit");
                hardReset();
                break;
            case ("youWin"):
                gameBoard.showNotification("youWin");
                hardReset();
                break;
            case ("youLose"):
                gameBoard.showNotification("youLose");
                hardReset();
                break;
            case ("draw"):
                gameBoard.showNotification("draw");
                hardReset();
                break;
            case ("endForNoReason"):
                gameBoard.showNotification("endForNoReason");
                hardReset();
                break;
        }
    }

    /**
     * find out the state of the gamesession
     * @return <code>true</code> if gamesession is over
     *         <code>false</code> if gamesession is not over yet
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * find out who´s turn to set an draw it is
     * @return <code>true</code> if it´s the turn of the local player
     *         <code>false</code> if it´s the opponent´s turn
     */
    public boolean isMyTurn() {
        return myTurn;
    }

    /**
     * Methode zur Zug-Verwaltung auf dem Spielfeld im Online-Modus
     * @param yourTurn boolean - bin ich dran oder nicht
     */
    public void setMyTurn(boolean yourTurn) {
        System.out.println("switching turns");
        this.myTurn = yourTurn;
        if (!isMyTurn()){
            System.out.println("should block fields");
            this.gameBoard.blockAllFields();
            this.gameBoard.setTurnInfo(2);
        }
        else {
            System.out.println("should unblock fields");
            this.gameBoard.unblockAllFields();
            this.gameBoard.setTurnInfo(1);
        }
    }



    /**
     * Methode die Informationen zur Zugreihenfolge aus der Servernachricht ausliest
     * @param message String, die Nachricht vom TTT-Server
     * @return true = Spieler am Zug, false = warten auf Gegenspieler
     * @throws ParseException --> malformed JSON, sollte nicht passieren...
     */
    public boolean findTurnInfo(String message) throws ParseException {
        JsonParser parser = new JsonParser();
        JsonObject payload = (JsonObject) parser.parse(message);
        System.out.println("I parsed this");
        if (payload.get("info").getAsString().equals("yourTurn")) {
            System.out.println("its my turn");
            return true;
        }
        else{
            System.out.println("its not my turn");
            return false;
        }
    }

    /**
     * Methode zum Zurücksetzen des Spielfelds nach Spielende
     */
    public void hardReset(){
        System.out.println("hard resetting boardstate");
        setMyTurn(false);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("calling new handler");
                gameBoard.clearAllBlocks();
                gameBoard.blockAllFields();
                gameBoard.clearOppoName();
            }
        }, 250);
    }
}
