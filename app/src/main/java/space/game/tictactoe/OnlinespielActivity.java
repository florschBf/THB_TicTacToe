package space.game.tictactoe;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.net.URI;
import java.net.URISyntaxException;

import space.game.tictactoe.websocket.TttWebsocketClient;

public class OnlinespielActivity extends AppCompatActivity {
    private TttWebsocketClient client = new TttWebsocketClient(new URI("ws://192.168.178.52:8088"), this);

    public OnlinespielActivity() throws URISyntaxException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlinespiel);

        //Activate websocket connection
        OnlinespielActivity.this.startConnection();
        View playerList = findViewById(R.id.playerList);
        System.out.println("Setting visible");
        try {
            playerList.setVisibility(View.VISIBLE);
        } catch (Exception e){
            System.out.println(e);
        }






        //@TODO get selected Player
        // Intent intent = getIntent();

        //@TODO use selected Player
        // use some values from selected Player
    }


    private void startConnection(){
        System.out.println("getting called");
        this.client.connect();
    }






}