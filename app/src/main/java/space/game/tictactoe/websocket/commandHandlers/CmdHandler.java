package space.game.tictactoe.websocket.commandHandlers;

import com.google.gson.JsonObject;

/**
 * CmdHandlers extract the given input from a Client-Websocket-Message regarding their topic
 */
public interface CmdHandler {
    public String handle(JsonObject payload);
}
