package space.game.tictactoe;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.net.URI;
import java.net.URISyntaxException;

import space.game.tictactoe.websocket.TttWebsocketClient;

public class OnlinespielActivity extends AppCompatActivity {
    private TttWebsocketClient client = new TttWebsocketClient(new URI("ws://192.168.178.52:8088"), this);;

    public OnlinespielActivity() throws URISyntaxException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlinespiel);

        //Activate websocket connection
        OnlinespielActivity.this.startConnection();
        View playerListOverlay = findViewById(R.id.overlay);
        System.out.println("Setting visible");
        try {
            playerListOverlay.setVisibility(View.VISIBLE);
        } catch (Exception e){
            System.out.println(e);
        }

        //Click listener to open Playerlist-View
        TextView playerListToggle = (TextView) findViewById(R.id.listStatus);
        playerListToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    playerListOverlay.setVisibility(View.VISIBLE);
                } catch(Exception e) {
                    System.out.println(e);
                }
            }
        });

        //Click listener to close Playerlist-View
        Button closeList = (Button) findViewById(R.id.closeList);
        closeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    playerListOverlay.setVisibility(View.GONE);
                } catch(Exception e) {
                    System.out.println(e);
                }
            }
        });

        //clicklistener for the playerList view
        ListView playerList = findViewById(R.id.playerList);
        playerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                //intent
                System.out.println("clicked item" + parent + view + id);
                String opponentName = playerList.getItemAtPosition(position).toString();
                Toast selectedOpponent = Toast.makeText(getApplicationContext(), "Du startest ein Spiel gegen " + opponentName, Toast.LENGTH_SHORT);
                selectedOpponent.show();
            }
        });
        //@TODO get selected Player
        // Intent intent = getIntent();

        //@TODO use selected Player
        // use some values from selected Player
    }

    //start connection
    private void startConnection(){
        System.out.println("getting called");
        this.client.connect();
    }

    //disconnect on back button leaving the activity
    @Override
    public void onBackPressed() {
        //code
        super.onBackPressed();
        this.client.close();
    }

   //Use onResume to always check for connection when we come back
    //TODO implement reconnection if coming back from suspension - crashes the app like this
    @Override
    protected void onResume(){
        super.onResume();
        if(this.client.isClosed()){
            System.out.println("reconnecting...");
            this.client.connect();
        }

    }

    //Overriding all System methods that disable the activity to also disconnect the websocket
/*    @Override
    protected void onPause() {
        super.onPause();
        this.client.close();
    }*/

    @Override
    protected void onDestroy(){
        super.onDestroy();
        this.client.close();
    }

    @Override
    protected void onStop(){
        super.onStop();
        this.client.close();
    }






}