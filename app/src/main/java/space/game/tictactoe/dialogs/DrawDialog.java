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

import space.game.tictactoe.menu.GameSingleActivity;
import space.game.tictactoe.menu.MenuActivity;
import space.game.tictactoe.menu.OnlinespielActivity;
import space.game.tictactoe.R;

/**
 * Klasse wird genutzt um Unentschieden-Spielstatus in Form einer Dialogfenster erkennbar machen
 * Class is used to reveal lost game statuses in the form of a dialog window
 *
 * @author in, fs for help
 */

public class DrawDialog extends Dialog {

    /**
     *  Declaration and in itialization of membervariables
     */
    private GameSingleActivity gameSingleActivity = null;
    private OnlinespielActivity onlinespielActivity = null;

    /**
     * Constructor to display a draw-alert-dialog for singleplayer-mode
     * @param context Show the dialog in the context given
     * @param gameSingleActivity if selected in the dialog go to a new activity for a single-player-game called GameSingleActivity
     */
    public DrawDialog(@NonNull Context context, GameSingleActivity gameSingleActivity) {
        super(context);
        this.gameSingleActivity = gameSingleActivity;
    }

    /**
     * constructor to display a draw-alert-dialog for online-game
     * @param context Show the dialog in the context given
     * @param onlinespielActivity if selected in the dialog go to a new activity for a online-player-game called OnlinespielActivity
     */
    public DrawDialog(@NonNull Context context, OnlinespielActivity onlinespielActivity) {
        super(context);
        this.onlinespielActivity = onlinespielActivity;
    }

    /**
     * create and show the draw-dialog with options how to continue
     * @param savedInstanceState saved state of instance
     * @see Dialog
     * @see DrawDialog
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_dialog_draw);
        getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));

        final ImageView imageViewClose = findViewById(R.id.imageViewClose);
        final Button btnPlay = findViewById(R.id.btn_play);
        final Button btnMenu = findViewById(R.id.btn_menu);


        if (gameSingleActivity != null){
            imageViewClose.setOnClickListener(v -> {
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
        else if (onlinespielActivity != null){
            this.setCancelable(false); // weggeklickte Dialoge werden in OnlinespielActivity nicht ordentlich verarbeitet, deshalb disabled
            imageViewClose.setOnClickListener(v -> {
                //TODO properly close and handle dialogs and dialog buttons
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
