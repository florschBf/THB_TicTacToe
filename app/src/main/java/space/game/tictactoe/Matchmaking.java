package space.game.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import space.game.tictactoe.gameObjects.Player;
import space.game.tictactoe.services.HttpService;
import space.game.tictactoe.services.JsonPlaceholderApi;
import space.game.tictactoe.services.PlayerService;

public class Matchmaking extends AppCompatActivity {

    private TextView textViewPlayerlistResponse;
    private HttpService httpService;

    private ListView lv_playerList;

//    public Matchmaking(@Inject HttpService httpService) {
//        this.httpService = httpService;
//        //this.textViewPlayerlistResponse = textViewPlayerlistResponse;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_matchmaking);

        // assign List of Players
        // lv_playerList = findViewById(R.id.playerList_ListView);

        //TextView textViewPlayerlistResponse = findViewById(R.id.playerlist_response);

        // substitute baseUrl by url of server
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.0.100:8080")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();


        // Instantiate Request
        PlayerService playerService = HttpService.getInstance(this).getPlayerService();
        //JsonPlaceholderApi jsonPlaceholderApi = HttpServce.retrofit.create(JsonPlaceholderApi.class);

        // call a PlayerList as Response
        Call<List<Player>> call = playerService.getPlayerList();

        // all Requests are treated one by another, so a new Request has to enque to previous Requests
        // when Request proceeded, execute this Callback-Function
        call.enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(@NonNull Call<List<Player>> call, @NonNull Response<List<Player>> response) {
                // the Request was executed (onResponse)
                try {
                    if (!response.isSuccessful()) {
                        // No successfull Response on Request-Call
                        //                    textViewPlayerlistResponse.setText("No succes: " + response.code());
                    } else if (response.body() != null) {
                        // Successfull Response on Request-Call and Response-Body not empty

                        List<Player> playerList = response.body();
                        for (Player player : playerList) {
                            String content = "";
                            content += "firebaseId: " + player.getFirebaseId() + "\n";
                            content += "name: " + player.getName() + "\n";

                            Toast.makeText(Matchmaking.this, content, Toast.LENGTH_SHORT).show();
                            //  textViewPlayerlistResponse.setText(content);
                        }
                    } else {
                        Toast.makeText(Matchmaking.this, "No Online-Players found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }


            }

            // Something after executed Request went wrong
            @Override
            public void onFailure(@NonNull Call<List<Player>> call, @NonNull Throwable t) {
                Toast.makeText(Matchmaking.this, "Error occured: \" + t.getMessage()", Toast.LENGTH_SHORT).show();
//                textViewPlayerlistResponse.setText("Failure: " + t.getMessage());
            }
        });


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
        ImageView zahnrad= findViewById(R.id.zahnrad_matchmaker);
        zahnrad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Matchmaking.this, OptionenActivity.class);
                    startActivity(intent);
                } catch(Exception e) {

                }
            }
        });
    }
}