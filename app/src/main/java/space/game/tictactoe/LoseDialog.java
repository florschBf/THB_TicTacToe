package space.game.tictactoe;

import static android.graphics.Color.TRANSPARENT;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class LoseDialog extends Dialog {
    private final GameActivity gameActivity;

    public LoseDialog(@NonNull Context context, GameActivity gameActivity) {
        super(context);
        this.gameActivity = gameActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView((R.layout.layout_dialog_lost));
        getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));

        final ImageView imageViewClose = findViewById(R.id.imageViewClose);
        final Button btnPlay = findViewById(R.id.btn_play);
        final Button btnMenu = findViewById(R.id.btn_menu);

        imageViewClose.setOnClickListener(v -> {
            //Dialogfenster schliessen, Felder inkl Zuege bleiben sichtbar
//            gameActivity.startNewGame();
            dismiss();
        });

        btnPlay.setOnClickListener(v -> {
            // Dialogfenstar schliessen, Spielfelder zurücksetzen
            gameActivity.startNewGame();
            dismiss();
        });

        btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent(this.gameActivity, MenuActivity.class);
            gameActivity.startActivity(intent);
            dismiss();
        });

    }
}
