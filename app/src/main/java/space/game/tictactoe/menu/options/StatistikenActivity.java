package space.game.tictactoe.menu.options;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import space.game.tictactoe.R;
import space.game.tictactoe.handlers.StatisticsHandler;
import space.game.tictactoe.menu.AboutActivity;
import space.game.tictactoe.menu.MenuActivity;
import space.game.tictactoe.menu.OptionenActivity;
import space.game.tictactoe.models.Player;

/** statistics activity to show a status list for wins, losses or draws
 *
 */
public class StatistikenActivity extends AppCompatActivity {

    Player player = Player.getPlayer();

    /**
     * show gamestatistics in StatistikenActivity with (updated) playerdata
     * @param savedInstanceState saved state of instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiken);
        System.out.println("Statistics onCreate called");

        //Toast.makeText(this, "Wins: " + player.getWins() + " Losses: " + player.getLosses() + " Draws: " + player.getDraws(), Toast.LENGTH_LONG).show();

        TextView textViewTotalGames = (TextView) findViewById(R.id.statistiken_table_totalgames_value);
        textViewTotalGames.setText(Long.toString(player.getTotalGames()));

        TextView texViewTitle = (TextView) findViewById(R.id.statistiken_title_id);
        texViewTitle.setText("See your statistics, " + player.getName() + "!");

        TextView textViewWins = (TextView) findViewById(R.id.statistiken_table_wins_value);
        textViewWins.setText(Long.toString(player.getWins()));

        TextView textViewLosses = (TextView) findViewById(R.id.statistiken_table_losses_value);
        textViewLosses.setText(Long.toString(player.getLosses()));

        TextView textViewDraws = (TextView) findViewById(R.id.statistiken_table_draws_value);
        textViewDraws.setText(Long.toString(player.getDraws()));

    }

    /**
     * Method to guaranty or define where to go when device-back-button is clicked
     * otherwise it could happen, that you have to click back 2 times from statistics, when you returned
     * to options by "zurück"-button.
     * @param view A Android view - could be the Statistics View or Optionen-View
     */
    public void backToOpionenActivity(View view) {
        Intent intent = new Intent(StatistikenActivity.this, OptionenActivity.class);
        startActivity(intent);
        finish();
    }


}