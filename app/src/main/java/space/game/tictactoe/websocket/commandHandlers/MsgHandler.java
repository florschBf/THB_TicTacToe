package space.game.tictactoe.websocket.commandHandlers;

import com.google.gson.JsonObject;

/**
 * CmdHandlers extract the given input from a Client-Websocket-Message regarding their topic
 */
//TODO REFACTOR CmdHandlers TO MsgHandlers TO PREVENT CONFUSION WITH ACTUAL!! TttCommandHandler who does the commands
//These handle messages from the server
public interface MsgHandler {
    public String handle(JsonObject payload);
}
