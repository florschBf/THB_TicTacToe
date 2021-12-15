package space.game.tictactoe.handlers;

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

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
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
}
