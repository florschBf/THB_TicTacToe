package space.game.tictactoe.websocket.messageHandlers;

import com.google.gson.JsonObject;

import java.util.Objects;

import space.game.tictactoe.models.Player;

//TODO REFACTOR CmdHandlers TO MsgHandlers TO PREVENT CONFUSION WITH ACTUAL!! TttCommandHandler who does the commands
//These handle messages from the server
public class SignUpMsgHandler implements MsgHandler {


    @Override
    public String handle(JsonObject payload) {
        if (!Objects.equals(payload.get("topic").getAsString(), "signup")){
            System.out.println("shouldnt be here");
            return "Error";
        }
        else {
            //TODO Protokoll sauberer umsetzen
            System.out.println("decoding signup command answer");
            String cmd = payload.get("command").getAsString();
            switch (cmd){
                case ("list"):
                    try {
                        if (payload.get("players").getAsString().equals("all")){
                            return "playerList";
                        } else if (payload.get("player").getAsString().equals("self")) {
                            return "player";
                        }
                        else { return "Error, no valid register command found";}
                    } catch (Exception e){
                        //Wasn't players, dont know what this could be, not in protocol
                        return "Error, no valid player info found";
                    }
                case ("register"):
                    String playerUID = null;
                    try {
                        playerUID = payload.get("yourUID").getAsString();
                        Player.getPlayer().setUid(playerUID);
                        System.out.println("Player UID is now set to: " + Player.getPlayer().getServerId());
                        return "Set playerUID already";
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        return "Error with register command";
                    }
                default:
                    return "Error, not a proper signUp command";
            }
        }
    }
}
