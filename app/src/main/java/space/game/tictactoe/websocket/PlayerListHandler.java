package space.game.tictactoe.websocket;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import space.game.tictactoe.R;
import space.game.tictactoe.models.Player;

public class PlayerListHandler {
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    //who knew it could be so complicated to add a string to a list...
    private ArrayAdapter<String> adapter;
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    private final ArrayList<String> playerListItems=new ArrayList<String>();
    private ListView playerList;
    private final Context context;

    private Player player;

    public PlayerListHandler(Context context) {
        this.context = context;
    }
    /**
     * Method to render the received playerList to the given context (OnlinespielActivity)
     * @param message String from websocket server including playerlist objects
     */
    public void renderList(String message) {
        this.player = Player.getPlayer();

        //TODO render the list in message into context
        System.out.println("rendering list somewhere...");

        //parsing playerlist
        JsonObject payload = parseMessage(message);

        //getting keys out of the json...
        List<String> keys = payload.entrySet()
                .stream()
                .map(i -> i.getKey())
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("Payload keys: " + keys);

        //HAS TO RUN ON UITHREAD OR WE CANT KEEP UPDATING
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //empty playerlist to refill from data later
                playerListItems.clear();

                /** @Todo get player self */



                adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, playerListItems);
                playerList = ((Activity) context).findViewById(R.id.playerList);
                playerList.setAdapter(adapter);
                //using key list to get all player objects with their names and uids - putting them into playerList ListView with adapter
                for (String key : keys) {
                    if (!key.equals("topic") && !key.equals("players") &&!key.equals("command")) { //excluding keys we know are not player strings
                        System.out.println("running for key " + key);

                        try {
                            JsonObject listPlayer = (JsonObject) payload.get(key);
                            String playerName = listPlayer.get("name").getAsString();
                            String playerUID = listPlayer.get("playerUID").getAsString();
                            //String listEntry = playerName; // + " " + playerUID;
                            //@TODO playerUIDs müssen gespeichert werden für spätere Nutzung, ohne in der Liste sichtbar zu sein
                            //Vorschlag wäre eine Klasse 'listitem' oder auch 'listplayer', die hier in der ArrayList gespeichert wird
                            //von der wird dann hier nur die Methode "getName" auf die Liste gerendert

                            //filtering for own serverID here
                            if (!playerUID.equals(player.getServerId())){
                                adapter.add(playerName);
                            }

                        } catch (Exception e) {
                            System.out.println(e);
                        }

                    }
                }
            }
        });
    }

    private JsonObject parseMessage(String message){
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(message);
        JsonObject payload = (JsonObject) obj;
        return payload;
    }

}
