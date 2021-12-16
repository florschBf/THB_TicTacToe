package space.game.tictactoe.websocket.commandHandlers;

import com.google.gson.JsonObject;

//TODO REFACTOR CmdHandlers TO MsgHandlers TO PREVENT CONFUSION WITH ACTUAL!! TttCommandHandler who does the commands
//These handle messages from the server
public class GameSessionCmdHandler implements CmdHandler{
    @Override
    public String handle(JsonObject payload) {
        return null;
    }
}
