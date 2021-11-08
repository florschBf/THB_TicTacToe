package space.game.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Matchmaking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchmaking);


        //Button 1 -> Weiterleitung nach Matchmaking zum Online Spiel
        Button button_playerchosen = (Button)findViewById(R.id.button_playerchosen);
        button_playerchosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Matchmaking.this, OnlinespielActivity.class);
                    startActivity(intent);
                } catch(Exception e) {

                }
            }
        });
    }
}