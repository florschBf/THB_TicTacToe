package space.game.tictactoe.handlers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.ParseException;

/**
 * Klasse zum Verwalten einer Spielesession
 * Wer ist dran, wer gewinnt, Spielfeld aufräumen
 */
public class GameSessionHandler {
    private GameBoardHandler gameBoard;
    private boolean myTurn = false;
    private boolean gameOver = false;

    public boolean isGameOver() {
        return gameOver;
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
                //TODO show an alert dialog to explain things
                gameBoard.showNotification("disconnect");
                hardReset();
                break;
            case ("oppoQuit"):
                //TODO show an alert dialog to explain things
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
                //TODO remove completely or combine disco and quit
                gameBoard.showNotification("endForNoReason");
                hardReset();
                break;
        }
    }

    public boolean isMyTurn() {
        return myTurn;
    }
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

    public GameSessionHandler (GameBoardHandler gameBoard){
        this.gameBoard = gameBoard;
        this.gameBoard.renderOpponentName();
        System.out.println(gameBoard);
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
    private void hardReset(){
        System.out.println("hard resetting boardstate");
        setMyTurn(false);
        gameBoard.clearAllBlocks();
        gameBoard.blockAllFields();
        gameBoard.clearOppoName();

    }
}
