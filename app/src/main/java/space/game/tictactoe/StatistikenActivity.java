package space.game.tictactoe;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.Toast;

import space.game.tictactoe.handlers.StatisticsHandler;
import space.game.tictactoe.models.Player;


public class StatistikenActivity extends AppCompatActivity {

    Player player = Player.getPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiken);
        System.out.println("Statistics onCreate called");

        /*@TODO show selected playerdata as textview */
        Toast.makeText(this, "Wins: " + player.getWins() + " Losses: " + player.getLosses() + " Draws: " + player.getDraws(), Toast.LENGTH_LONG).show();
    }
}