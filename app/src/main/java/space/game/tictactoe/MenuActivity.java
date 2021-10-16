package space.game.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Spiel im Vollbild ausführen
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);



        // Button 1 btn_singleGame -> Weiterleitung zum Spiel mit PC GameActivity
        Button btn_singleGame = (Button)findViewById(R.id.btn_singleGame);
        btn_singleGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MenuActivity.this, GameActivity.class);
                    startActivity(intent);
                } catch(Exception e) {

                }
            }
        });


        // Button3 button_rules -> Weiterleitung zur Spielbeschreibung, Spielregel
        Button button_rules = (Button)findViewById(R.id.button_rules);
        button_rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MenuActivity.this, RulesActivity.class);
                    startActivity(intent);
                } catch(Exception e) {

                }
            }
        });

        // Button 4 button_showAbout -> Weiterleitung zur Projektbeschreibung
        Button button_showAbout = (Button)findViewById(R.id.button_showAbout);
        button_showAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MenuActivity.this, AboutActivity.class);
                    startActivity(intent);
                } catch(Exception e) {

                }
            }
        });


    }

}