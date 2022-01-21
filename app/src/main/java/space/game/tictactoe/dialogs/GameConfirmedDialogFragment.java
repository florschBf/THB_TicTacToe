package space.game.tictactoe.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import space.game.tictactoe.R;
import space.game.tictactoe.handlers.websocketHandler.TttWebsocketClient;

public class GameConfirmedDialogFragment extends DialogFragment {
    /**
     * declaration and initialation of the member variable client
     */
    private String oppoName;
    private int oppoIcon;

    /**
     * constructor - tell the fragment who we are playing against so name and icon can be displayed
     * @param opponentName Name of the confirmed opponent
     */
    public GameConfirmedDialogFragment(String opponentName, String iconId){
        this.oppoName = opponentName;
        this.oppoIcon = Integer.parseInt(iconId);
    }

    /**
     * create and show a dialog to inform the player about the waiting status when called online-game
     * @param savedInstanceState saved state of a instance
     * @return reate the AlertDialog object and return it
     * @see Dialog
     * @see android.app.DialogFragment
     * @see WaitingForOpponentDialogFragment
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.layout_dialog_game_confirmed, null);
        builder.setView(v);
        TextView confirmation = v.findViewById(R.id.dialog_game_confirmed);
        confirmation.setText(v.getResources().getText(R.string.dialog_game_confirmed) + " " + oppoName);
        ImageView icon = v.findViewById(R.id.image_dialog_oppoIcon);
        icon.setImageResource(this.oppoIcon);

        v.findViewById(R.id.button_letsgo_game).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println("ending dialog");
                    FragmentManager myManager = getActivity().getSupportFragmentManager();
                    DialogFragment whoAmI = (DialogFragment) myManager.getFragments().get(0);
                    whoAmI.dismiss();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
