package space.game.tictactoe.menu.options;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import space.game.tictactoe.R;
import space.game.tictactoe.handlers.StatisticsHandler;
import space.game.tictactoe.models.Player;

/** statistics activity to show a status list for wins, losses or draws
 *
 */
public class StatistikenActivity extends AppCompatActivity {

    Player player = Player.getPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiken);
        System.out.println("Statistics onCreate called");

        /*@TODO show selected playerdata as textview */
        Toast.makeText(this, "Wins: " + player.getWins() + " Losses: " + player.getLosses() + " Draws: " + player.getDraws(), Toast.LENGTH_LONG).show();

        TextView textViewTotalGames = (TextView) findViewById(R.id.statistiken_table_totalgames_value);
        textViewTotalGames.setText(Long.toString(player.getTotalGames()));

        TextView textViewWins = (TextView) findViewById(R.id.statistiken_table_wins_value);
        textViewWins.setText(Long.toString(player.getWins()));

        TextView textViewLosses = (TextView) findViewById(R.id.statistiken_table_losses_value);
        textViewLosses.setText(Long.toString(player.getLosses()));

        TextView textViewDraws = (TextView) findViewById(R.id.statistiken_table_draws_value);
        textViewDraws.setText(Long.toString(player.getDraws()));

    }
}