package space.game.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import space.game.tictactoe.models.Player;
import space.game.tictactoe.services.HttpService;
import space.game.tictactoe.services.PlayersService;

public class Matchmaking extends AppCompatActivity {

    private TextView textViewPlayerlistResponse;
    private HttpService httpService;
//    private ListView listView_players;



//    public Matchmaking(@Inject HttpService httpService) {
//        this.httpService = httpService;
//        //this.textViewPlayerlistResponse = textViewPlayerlistResponse;
//    }

    String[] items = new String [] {"Apple", "Orange", "Grapes", "Pinapple"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_matchmaking);

        ListView listView_players = (ListView) findViewById(R.id.playerList_ListView);

        //        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
//        listView_players.setAdapter(adapter);


        //TextView textViewPlayerlistResponse = findViewById(R.id.playerlist_response);


        PlayersService playersService = new PlayersService(this);

        /**
         * Async Callback - call this Method, when there are Results!
         * otherwise playerlist would be null, because of async Response by Server
         * */
        playersService.getPlayerList(new PlayersService.RetrofitResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<Player> playerListResponse) {
                // put entire List into the lisview control

                Toast.makeText(getApplicationContext(), "Playerlist:" + playerListResponse, Toast.LENGTH_SHORT).show();

                ArrayAdapter<Player> arrayAdapter = new ArrayAdapter<Player>(getApplicationContext(), android.R.layout.simple_list_item_1, playerListResponse);

                listView_players.setAdapter(arrayAdapter);

//                for (Player player : playerListResponse) {
//                    String content = "";
//                    content += "firebaseId: " + player.getFirebaseId() + "\n";
//                    content += "name: " + player.getName() + "\n";
//
//                    Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
//                    //  textViewPlayerlistResponse.setText(content);
//                }
            }
        });

//

        //@TODO offer playerList
        //@TODO be able to select an ListItem (Player) and pass it where needed - e.g. OnlinespielActivity

        //RadioButton playerRadioButton = (RadioButton)findViewById(R.id.matchmaker_radiodummy);

        //@TODO implement a ListView or something alike to build dynamically a List with fetched Players at activity_matchmaking.xml
//        playerRadioButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // test - show a message, if button works
//                textViewPlayerlistResponse.setText("Player selected");
//            }
//        });



        //Button 1 -> Weiterleitung nach Matchmaking zum Online Spiel
//        Button button_playerchosen = (Button)findViewById(R.id.button_playerchosen);
//
//
//        button_playerchosen.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // Test, if
//
//                try {
//                    Intent intent = new Intent(Matchmaking.this, OnlinespielActivity.class);
//
//                    // @TODO add here something like Id of selected item (player of playerlist)
//
//                    startActivity(intent);
//                } catch(Exception e) {
//
//                }
//            }
//        });





        // Imageview Zahnrad als Button anclickbar-> Optionen im Menü -> Weiterleitung zu Optionen->Icons->Statistiken
//        ImageView zahnrad= findViewById(R.id.zahnrad_matchmaker);
//        zahnrad.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    Intent intent = new Intent(Matchmaking.this, OptionenActivity.class);
//                    startActivity(intent);
//                } catch(Exception e) {
//
//                }
//            }
//        });
    }
}