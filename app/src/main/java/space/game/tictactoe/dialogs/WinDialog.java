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
import space.game.tictactoe.OnlinespielActivity;
import space.game.tictactoe.R;

public class WinDialog extends Dialog {

    private GameSingleActivity gameSingleActivity = null;
    private OnlinespielActivity onlinespielActivity = null;

    public WinDialog(@NonNull Context context, GameSingleActivity gameSingleActivity) {
        super(context);
        this.gameSingleActivity = gameSingleActivity;
    }

    public WinDialog(@NonNull Context context, OnlinespielActivity onlinespielActivity) {
        super(context);
        this.onlinespielActivity = onlinespielActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView((R.layout.layout_dialog_win));
        getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));

        final ImageView imageViewClose = findViewById(R.id.imageViewClose);
        final Button btnPlay = findViewById(R.id.btn_play);
        final Button btnMenu = findViewById(R.id.btn_menu);

        if (gameSingleActivity != null){
            imageViewClose.setOnClickListener(v -> {
//          gameSingleActivity.startNewGame();
                dismiss();
            });

            btnPlay.setOnClickListener(v -> {
                gameSingleActivity.startNewGame();
                dismiss();

            });
            btnMenu.setOnClickListener(v -> {
                Intent intent = new Intent(this.gameSingleActivity, MenuActivity.class);
                gameSingleActivity.startActivity(intent);
                dismiss();
            });
        }
        /**weggeklickte Dialoge werden in OnlinespielActivity nicht ordentlich verarbeitet, deshalb disabled
         *
         */
        else if (onlinespielActivity != null){
            //TODO properly close and handle dialogs and dialog buttons
            this.setCancelable(false);
            imageViewClose.setOnClickListener(v -> {
//          gameSingleActivity.startNewGame();
                dismiss();
            });

            btnPlay.setOnClickListener(v -> {
                //gameSingleActivity.startNewGame();
                dismiss();
            });
            btnMenu.setOnClickListener(v -> {
                Intent intent = new Intent(this.onlinespielActivity, MenuActivity.class);
                onlinespielActivity.startActivity(intent);
                dismiss();
            });
        }


    }
}