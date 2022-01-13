package space.game.tictactoe.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import space.game.tictactoe.menu.options.IconwahlActivity;
import space.game.tictactoe.R;
import space.game.tictactoe.menu.options.StatistikenActivity;

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

        //Button 2 -> Weiterleitung zu den Statistiken
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

}