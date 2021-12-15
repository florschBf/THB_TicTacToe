package space.game.tictactoe.dialogs;

import static android.graphics.Color.TRANSPARENT;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import space.game.tictactoe.GameSingleActivity;
import space.game.tictactoe.MenuActivity;
import space.game.tictactoe.R;

public class LoseDialog extends Dialog {
    private final GameSingleActivity gameSingleActivity;

    public LoseDialog(@NonNull Context context, GameSingleActivity gameSingleActivity) {
        super(context);
        this.gameSingleActivity = gameSingleActivity;
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
//            gameSingleActivity.startNewGame();
            dismiss();
        });

        btnPlay.setOnClickListener(v -> {
            // Dialogfenstar schliessen, Spielfelder zurücksetzen
            gameSingleActivity.startNewGame();
            dismiss();
        });

        btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent(this.gameSingleActivity, MenuActivity.class);
            gameSingleActivity.startActivity(intent);
            dismiss();
        });

    }
}
