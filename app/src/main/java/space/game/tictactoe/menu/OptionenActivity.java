package space.game.tictactoe.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import space.game.tictactoe.menu.options.IconwahlActivity;
import space.game.tictactoe.R;
import space.game.tictactoe.menu.options.SpielbrettwahlActivity;
import space.game.tictactoe.menu.options.StatistikenActivity;

/** options activity for options in main menu
 * There is two submenues: 1. Icon choosing
 * 2. statistics about your wins, losses or draws
  */

public class OptionenActivity extends AppCompatActivity {

//    Switch ton;
//    public static boolean tonOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optionen);

        //Button 1 -> Weiterleitung zur Icon Auswahl
        Button button_icon_waehlen = (Button)findViewById(R.id.button_icon_waehlen);
        button_icon_waehlen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(OptionenActivity.this, IconwahlActivity.class);
                    startActivity(intent);
                } catch(Exception e) {

                }
            }
        });

        //Button 2 -> Weiterleitung zur Spielbrett Auswahl
        Button button_spielbrett_waehlen = (Button)findViewById(R.id.button_spielbrett_waehlen);
        button_spielbrett_waehlen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(OptionenActivity.this, SpielbrettwahlActivity.class);
                    startActivity(intent);
                } catch(Exception e) {

                }
            }
        });

        //Button 3 -> Weiterleitung zu den Statistiken
        Button button_statistiken = (Button)findViewById(R.id.button_statistiken);
        button_statistiken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(OptionenActivity.this, StatistikenActivity.class);
                    startActivity(intent);
                } catch(Exception e) {

                }
            }
        });

    }

    // System-Button Back überschreiben
    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(OptionenActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }catch (Exception e) {

        }
    }

}