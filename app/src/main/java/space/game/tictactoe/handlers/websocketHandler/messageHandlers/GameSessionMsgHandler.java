package space.game.tictactoe.handlers.websocketHandler.messageHandlers;

import com.google.gson.JsonObject;

import java.util.Objects;

//TODO REFACTOR CmdHandlers TO MsgHandlers TO PREVENT CONFUSION WITH ACTUAL!! TttCommandHandler who does the commands
//These handle messages from the server
public class GameSessionMsgHandler implements MsgHandler {

    /**
     * Handle the payload sent from the managing server
     * Payload is a JSON-Object
     * @param payload received from server to client as JSON
     *                <li> {“topic”:”gameSession”,“command”:“startgame”,”state”:”challenged”,”opponent”:”<SenderName>”}</li>
     *                <li> {“topic”:”gameSession”,“command”:“startgame”,”state”:”confirmed”, “opponent”:”<playerID.name>”, “opponentIcon”:”<playerId.icon>”}</li>
     *                <li> {“topic”:”gameSession”,“command”:“startgame”,”state”:”confirmed”, “opponent”:”<Sender.name>”,“opponentIcon”:”<Sender.icon>”}</li>
     *                <li> {“topic”:”gameSession”,“command”:“startgame”,”state”:”denied”}</li>
     *                <li> {“topic”:”gameSession”,“command”:“gameState”,”info”:”whoseTurn”}</li>
     *                <li> {“topic”:”gameSession”,“command”:“gameState”,”info”:”yourTurn || opponentsTurn}</li>
     *                <li> {“topic”:”gameSession”,“command”:“command”:“gameState”,”info”:”boardState”}</li>
     *                <li> {“topic”:”gameSession”,“command”:“gameState”,”info”:”boardState”,”currentBoard”:”<gameBoardArray>”}</li>
     *                <li> {“topic”:”gameSession”,“command”:“quitgame”,”state”:”confirmed”}</li>
     *                <li> {“topic”:”gameSession”,“command”:“quitgame”,”state”:”now”,”reason”:”opponentQuit”}</li>
     *                <li> {“topic”:”gameSession”,“command”:“quitgame”,”state”:”now”,”reason”:”opponentDisco”}</li>
     *                <li> {“topic”:”gameSession”,“command”:”quitgame“,”state”:“youwin“}</li>
     *                <li> {“topic”:”gameSession”,“command”:”quitgame“,”state”:“youlose“}</li>
     *                <li> {“topic”:”gameSession”,“command”:”quitgame“,”state”:“draw“</li>
     * @return handling depending on message-parameters
     */
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
