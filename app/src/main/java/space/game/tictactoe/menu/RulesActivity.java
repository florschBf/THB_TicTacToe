package space.game.tictactoe.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import space.game.tictactoe.R;

public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        Button button_back = (Button) findViewById(R.id.button_back_rules);

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(RulesActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {

                }
            }
        });

    }

    // System-Button Back überschreiben
    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(RulesActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }catch (Exception e) {

        }
    }
}



