package space.game.tictactoe.websocket;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;

import java.text.ParseException;

import space.game.tictactoe.websocket.commandHandlers.GameSessionCmdHandler;
import space.game.tictactoe.websocket.commandHandlers.MoveCmdHandler;
import space.game.tictactoe.websocket.commandHandlers.SignUpCmdHandler;

public class TttMessageHandler {
    private SignUpCmdHandler signUp = new SignUpCmdHandler();
    private GameSessionCmdHandler sessionCmd = new GameSessionCmdHandler();
    private MoveCmdHandler moveCmd = new MoveCmdHandler();

    public String handle(String message) throws ParseException, JSONException {


        //Annahme: TTT-Protokoll V 1.0 (Stand 01.12.'21)
        //Parse entsprechend nach den bekannten Topics und Commands.
        //Websockets lässt sonst automatisch die Connection fallen
        JsonObject payload = parseJSONString(message);
        String topic = payload.get("topic").getAsString();
        switch (topic) {
            case "signup":
                System.out.println("signup topic -> calling SignUpCmdHandler");
                return this.signUp.handle(payload);
            case "gameSession":
                System.out.println("gameSession topic -> calling GameSessionCmdHandler");
                return this.sessionCmd.handle(payload);
            case "gameMove":
                System.out.println("gameMove topic -> calling MoveCmdHandler");
                return this.moveCmd.handle(payload);
            default:
                System.out.println("found no useful message..");
                return "You said" + payload;
        }
    }

    private JsonObject parseJSONString(String message) throws ParseException{
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(message);
        JsonObject payload = (JsonObject) obj;
        return payload;
    }
}
