package space.game.tictactoe.websocket.commandHandlers;

import com.google.gson.JsonObject;

import java.util.Objects;

//TODO REFACTOR CmdHandlers TO MsgHandlers TO PREVENT CONFUSION WITH ACTUAL!! TttCommandHandler who does the commands
//These handle messages from the server
public class GameSessionMsgHandler implements MsgHandler {

    @Override
    public String handle(JsonObject payload) {
        String handledMsg = null;
        String cmd = null;
        if (!Objects.equals(payload.get("topic").getAsString(), "gameSession")){
            System.out.println("shouldnt be here");
            return "Error, not a gameSession message";
        }
        else {
            System.out.println("decoding gameSession command answer");
            try {
                cmd = payload.get("command").getAsString();
                switch (cmd){
                    case ("startgame"):
                        if (payload.get("state").getAsString().equals("confirmed"))
                        handledMsg = "gameStarted!";
                        break;
                    case ("gameState"):
                        String info = payload.get("info").getAsString();
                        if (info.equals("yourTurn") || info.equals("opponentsTurn")) {
                        handledMsg = "turnInfo";
                        break;
                        } else if (info.equals("boardState")){
                            handledMsg = "boardState";
                            break;
                        }
                    default:
                        System.out.println("Error, no valid gameSession command");
                        handledMsg = "Error, no valid gameSession command";
                }
            } catch (Exception e){
                System.out.println("Protocol error? Cant find a command");
                return "Error, issues finding a proper command";
            }
        }
        return handledMsg;
    }
}
