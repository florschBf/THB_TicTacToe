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

public class DrawDialog extends Dialog {
    private final GameActivity gameactivity;

    public DrawDialog(@NonNull Context context, GameActivity gameActivity) {
        super(context);
        this.gameactivity = gameActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_dialog_draw);
        getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));

        final ImageView imageViewClose = findViewById(R.id.imageViewClose);
        final Button btnPlay = findViewById(R.id.btn_play);
        final Button btnMenu = findViewById(R.id.btn_menu);

        imageViewClose.setOnClickListener(v -> {
//            gameactivity.ResetGame();
            dismiss();
        });

        btnPlay.setOnClickListener(v -> {
            gameactivity.ResetGame();
            dismiss();
        });

        btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent(this.gameactivity, MenuActivity.class);
            gameactivity.startActivity(intent);
            dismiss();
        });

    }
}
