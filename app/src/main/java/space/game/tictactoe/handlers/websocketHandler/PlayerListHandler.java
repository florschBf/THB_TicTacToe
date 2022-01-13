package space.game.tictactoe.handlers.websocketHandler;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import space.game.tictactoe.R;
import space.game.tictactoe.models.Opponent;
import space.game.tictactoe.models.Player;

public class PlayerListHandler {
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    //who knew it could be so complicated to add a string to a list...
    private ArrayAdapter<String> adapter;
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    private final ArrayList<String> playerListItems = new ArrayList<String>();
    private final ArrayList<Opponent> opponents = new ArrayList<Opponent>();
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
                opponents.clear();

                adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, playerListItems);
                playerList = ((Activity) context).findViewById(R.id.playerList);
                playerList.setAdapter(adapter);
                int i = 0;
                //using key list to get all player objects with their names and uids - putting them into playerList ListView with adapter
                for (String key : keys) {
                    if (!key.equals("topic") && !key.equals("players") &&!key.equals("command")) { //excluding keys we know are not player strings
                        System.out.println("running for key " + key);

                        try {
                            JsonObject listPlayer = (JsonObject) payload.get(key);
                            String playerName = listPlayer.get("name").getAsString();
                            String playerUID = listPlayer.get("playerUID").getAsString();
                            Opponent listEntry = new Opponent(playerName, playerUID);


                            //filtering for own serverID here
                            if (!playerUID.equals(player.getServerId())){
                                adapter.add(playerName);
                                opponents.add(listEntry); // saving opponents to find later
                                listEntry.setListPosition(i);
                                i++;
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
            }
        });
    }

    public String getOppoFromListPos(int listPosition){
        for (Opponent oppo : opponents){
            if (oppo.getListPosition() == listPosition){
                return oppo.getFirebaseId();
            }
        }
        return null;
    }

    private JsonObject parseMessage(String message){
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(message);
        JsonObject payload = (JsonObject) obj;
        return payload;
    }

}
