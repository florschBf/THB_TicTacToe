package space.game.tictactoe.dialogs;

import static android.graphics.Color.TRANSPARENT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import space.game.tictactoe.GameSingleActivity;
import space.game.tictactoe.MenuActivity;
import space.game.tictactoe.OnlinespielActivity;
import space.game.tictactoe.OptionenActivity;
import space.game.tictactoe.R;

//IM MOMENT IST DAS ALLES noch kein DIALOG!! Ist nur zum Testen und klar werden was und wie

public class InvitationOnlineGameDialog extends AppCompatActivity {


    private View contentView;
    private View loadingView;
    private int shortAnimationDuration;
    private CrossfadeActivity myCrossfadeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invitation_online_game_dialog);
        this.myCrossfadeActivity = new CrossfadeActivity();

        this.contentView = (View)findViewById(R.id.content);
        this.loadingView = (View)findViewById(R.id.loading_spinner);
        /** Retrieve and cache the system's default "short" animation time.
         *
         */
        shortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        // Wenn ich den Player ablehne, möchte ich zurück in die Playerliste!! Muss sichergestellt werden TODO
        final Button button_ablehnen_onlineanfrage = findViewById(R.id.button_ablehnen_onlineanfrage);
        button_ablehnen_onlineanfrage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(InvitationOnlineGameDialog.this, OnlinespielActivity.class);
                    startActivity(intent);
                } catch (Exception e) {

                }
            }
        });
        /* hier möchte ich nicht in die Playerliste zurück! Sondern gleich zum Spiel TODO
        final Button button_annehmen_onlineanfrage = findViewById(R.id.button_annehmen_onlineanfrage);
        button_annehmen_onlineanfrage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(InvitationOnlineGameDialog.this, OnlinespielActivity.class);
                    startActivity(intent);
                } catch (Exception e) {

                }
            }
        });*/
    }
    // crossfade Klasse --------------------------------------------------------------------------------------------------------CROSSFADE--------------------
    public static class CrossfadeActivity extends Activity {

        private View contentView;
        private View loadingView;
        private int shortAnimationDuration;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_onlinespiel);

            contentView = findViewById(R.id.content);
            loadingView = findViewById(R.id.loading_spinner);

            /** Initially hide the content view.
             *
             */
            contentView.setVisibility(View.GONE);

            /** Retrieve and cache the system's default "short" animation time.
             *
             */
            shortAnimationDuration = getResources().getInteger(
                    android.R.integer.config_shortAnimTime);
        }


        private void crossfade() {

            /**Set the content view to 0% opacity but visible, so that it is visible
             * (but fully transparent) during the animation.
             */

            contentView.setAlpha(0f);
            contentView.setVisibility(View.VISIBLE);

            /** Animate the content view to 100% opacity, and clear any animation
             *  listener set on the view.
             */

            contentView.animate()
                    .alpha(1f)
                    .setDuration(shortAnimationDuration)
                    .setListener(null);

            /** Animate the loading view to 0% opacity. After the animation ends,
             * set its visibility to GONE as an optimization step (it won't
             * participate in layout passes, etc.)
             */

            loadingView.animate()
                    .alpha(0f)
                    .setDuration(shortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            loadingView.setVisibility(View.GONE);
                        }
                    });
        }
    }
    // crossfade Methode --------------------------------------------------------------------------------------------------------CROSSFADE--------------------
}

// Hier die an Iris singlePlayer angepasste Dialogvariante, aber die geht erst, wenn auch ein Game programmiert ist, das auf den Trigger PLay Button reagieren kan
/*package space.game.tictactoe.dialogs;

        import static android.graphics.Color.TRANSPARENT;

        import android.app.Dialog;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Bundle;
        import android.widget.Button;
        import android.widget.ImageView;

        import androidx.annotation.NonNull;


        import space.game.tictactoe.MenuActivity;
        import space.game.tictactoe.OnlinespielActivity;
        import space.game.tictactoe.R;

public class InvitationOnlineGameDialog extends Dialog {

    private static final int iconDefault = R.drawable.stern_90;

    private final OnlinespielActivity OnlinespielActivity;

    public InvitationOnlineGameDialog (@NonNull Context context, OnlinespielActivity OnlinespielActivity ) {
        super(context);
        this.OnlinespielActivity = OnlinespielActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invitation_online_game_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));

        final ImageView imageViewClose = findViewById(R.id.imageViewOnlineClose);
        final Button button_annehmen_onlineanfrage = findViewById(R.id.button_annehmen_onlineanfrage);
        final Button button_ablehnen_onlineanfrage = findViewById(R.id.button_ablehnen_onlineanfrage);

        //geht im Moment zurück ins Menü
        imageViewClose.setOnClickListener(v -> {
          Intent intent = new Intent(this.OnlinespielActivity, MenuActivity.class);
          OnlinespielActivity.startActivity(intent);
           dismiss();
        });

         button_annehmen_onlineanfrage.setOnClickListener(v -> {
          /* OnlinespielActivity.startOnlineGame(); ---TODO
            dismiss();
        });
            //dieser Button soll in die Playerliste zurück führen TODO
            button_ablehnen_onlineanfrage.setOnClickListener(v -> {
            Intent intent = new Intent(this.OnlinespielActivity, MenuActivity.class);
            OnlinespielActivity.startActivity(intent);
            dismiss();
        });

    }
}*/
