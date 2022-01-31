package space.game.tictactoe.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import space.game.tictactoe.R;
import space.game.tictactoe.handlers.websocketHandler.TttWebsocketClient;

public class WaitingForOpponentDialogFragment extends DialogFragment {
    /**
     * declaration and initialation of the member variable client
     */
    private TttWebsocketClient client = null;

    /**
     * constructor of class WaitingForOpponentDialogFragment
     * @param client the websocket-connection of the client to the server
     */
    public WaitingForOpponentDialogFragment(TttWebsocketClient client){
        this.client = client;
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
        View v = inflater.inflate(R.layout.layout_dialog_wait_for_game, null);
        builder.setView(v);

        v.findViewById(R.id.button_abbrechen_randomgame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println("ending dialog");
                    FragmentManager myManager = getActivity().getSupportFragmentManager();
                    DialogFragment whoAmI = (DialogFragment) myManager.getFragments().get(0);
                    whoAmI.dismiss();
                    client.randomGameQueue("stop");
                    client.endGameNow();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
