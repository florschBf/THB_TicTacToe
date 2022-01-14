package space.game.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import space.game.tictactoe.handlers.StatisticsHandler;
import space.game.tictactoe.menu.MenuActivity;
import space.game.tictactoe.models.Player;

/** main activity of the project
 *
 */
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonStart = (Button)findViewById(R.id.buttonStart);

        buttonStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // instantiate a player with default-values when app is started
                Player player = new Player();
                StatisticsHandler statisticsHandler = new StatisticsHandler();

                try {
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);finish();
                } catch (Exception e) {

                }
            }
        });


    }
}