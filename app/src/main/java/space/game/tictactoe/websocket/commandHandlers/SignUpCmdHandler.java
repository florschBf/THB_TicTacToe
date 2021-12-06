package space.game.tictactoe.websocket.commandHandlers;

import android.app.LauncherActivity;

import com.google.gson.JsonObject;

import java.util.Objects;

//TODO REFACTOR CmdHandlers TO MsgHandlers TO PREVENT CONFUSION WITH ACTUAL!! TttCommandHandler who does the commands
//These handle messages from the server
public class SignUpCmdHandler implements CmdHandler{

    @Override
    public String handle(JsonObject payload) {
        if (!Objects.equals(payload.get("topic").getAsString(), "signup")){
            System.out.println("shouldnt be here");
            return "Error";
        }
        else {
            System.out.println("decoding signup command answer");
            try {
                if (payload.get("players").getAsString().equals("all")){
                    return "playerList";
                }
                else { return "Error, no valid register command found";}
            } catch (Exception e){
                //Wasn't players, dont know what this could be, not in protocol
                return "Error, no valid player info found";
            }
        }
    }
}
