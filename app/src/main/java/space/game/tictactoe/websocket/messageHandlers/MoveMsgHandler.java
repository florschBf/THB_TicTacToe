package space.game.tictactoe.websocket.messageHandlers;

import com.google.gson.JsonObject;

import java.util.Objects;

//TODO REFACTOR CmdHandlers TO MsgHandlers TO PREVENT CONFUSION WITH ACTUAL!! TttCommandHandler who does the commands
//These handle messages from the server
public class MoveMsgHandler implements MsgHandler {

    @Override
    public String handle(JsonObject payload) {
        if (!Objects.equals(payload.get("topic").getAsString(), "gameMove")){
            System.out.println("shouldnt be here");
            return "Error";
        }
        else {
            System.out.println("decoding gameMove command answer");
            try {
                String cmd = payload.get("command").getAsString();
                switch (cmd){
                    case ("mark"):
                        if (payload.get("player").getAsString().equals("opponent")){
                            //Zug des Mitspielers
                            return payload.get("marked").getAsString();
                        }
                        else if (payload.get("player").getAsString().equals("you")){
                            //Mein Zug bestätigt
                            return "Zug bestätigt";
                        }

                    default:
                        System.out.println("didnt find a valid move command");
                        return "Error, not a valid move command";
                }
            } catch (Exception e){
                //Wasn't in protocol?
                System.out.println("protocol error on move?");
                return "Error, no valid command for gameMove found";
            }
        }
    }
}
