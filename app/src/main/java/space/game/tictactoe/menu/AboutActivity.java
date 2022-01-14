package space.game.tictactoe.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import space.game.tictactoe.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void backToMenuActivity(View view) {
        Intent intent = new Intent(AboutActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    /** System-Button Back überschreiben
     * overwrite system-back-button
     */
    @Override
    public void onBackPressed() {
        try {
            Intent intent = new Intent(AboutActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }catch (Exception e) {

        }
    }
}