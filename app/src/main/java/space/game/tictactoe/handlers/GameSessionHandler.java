package space.game.tictactoe.handlers;

import android.app.Activity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.ParseException;

public class GameSessionHandler {
    private GameBoardHandler gameBoard;
    private boolean myTurn = false;
    private boolean gameOver = false;

    public boolean isGameOver() {
        return gameOver;
    }

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
                //TODO show win dialog
                gameBoard.showNotification("youWin");
                hardReset();
                break;
            case ("youLose"):
                //TODO show lose dialog
                gameBoard.showNotification("youLose");
                hardReset();
                break;
            case ("draw"):
                //TODO show draw dialog
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
        }
        else {
            System.out.println("should unblock fields");
            this.gameBoard.unblockAllFields();
        }
    }

    public GameSessionHandler (GameBoardHandler gameBoard){
        this.gameBoard = gameBoard;
        System.out.println(gameBoard);
    }

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

    private void hardReset(){
        System.out.println("hard resetting boardstate");
        gameBoard.clearAllBlocks();
        gameBoard.blockAllFields();
        setMyTurn(false);
    }
}
