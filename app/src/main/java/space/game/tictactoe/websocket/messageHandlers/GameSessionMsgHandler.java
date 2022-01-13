package space.game.tictactoe.websocket.messageHandlers;

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
                        if (payload.get("state").getAsString().equals("confirmed")){
                            handledMsg = "gameStarted!";
                        }
                        else if (payload.get("state").getAsString().equals("challenged")){
                            handledMsg = "challenged!";
                        }
                        else if (payload.get("state").getAsString().equals("denied")){
                            handledMsg = "game denied";
                        }
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
                    case ("quitgame"):
                        if (payload.get("state").getAsString().equals("confirmed")){
                            //server just confirmed me ending the game, all is fine
                            handledMsg = "gameEndedOnServer";
                            break;
                        }
                        else if (payload.get("state").getAsString().equals("youwin")){
                            //Server says I won!
                            handledMsg = "youwin";
                            break;
                        }
                        else if (payload.get("state").getAsString().equals("youlose")){
                            // Server say I lost!
                            handledMsg = "youlose";
                            break;
                        }
                        else if (payload.get("state").getAsString().equals("draw")){
                            // Server says it's a draw!
                            handledMsg = "draw";
                            break;
                        }
                        else if (payload.get("state").getAsString().equals("now") && payload.get("reason").getAsString().equals("opponentDisco")){
                            handledMsg = "gameTerminatedDisco";
                            break;
                        }
                        else if (payload.get("state").getAsString().equals("now") && payload.get("reason").getAsString().equals("opponentQuit")){
                            handledMsg = "gameTerminatedQuit";
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
