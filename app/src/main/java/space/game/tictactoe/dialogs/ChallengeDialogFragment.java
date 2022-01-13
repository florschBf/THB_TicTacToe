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


public class ChallengeDialogFragment extends DialogFragment {
    /**
     * Declaration and in itialization of membervariable client
     */
    private TttWebsocketClient client = null;

    /**
     * Constructor of class ChallengeDialogFragment
     * @param client the websocket-connection of this client
     */
    public ChallengeDialogFragment(TttWebsocketClient client){
        this.client = client;
    }

    /**
     * call a dialog to deny an invitation for a online-game
     * but not used in project
     * @param savedInstanceState a state of a passed instance
     * @return Create the AlertDialog object and return it
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.layout_dialog_challenge, null);
        builder.setView(v);

        v.findViewById(R.id.button_ablehnen_sent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println("ending dialog");
                    FragmentManager myManager = getActivity().getSupportFragmentManager();
                    DialogFragment whoAmI = (DialogFragment) myManager.getFragments().get(0);
                    whoAmI.dismiss();
                    client.randomGameQueue("stop");
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
        v.findViewById(R.id.button_challenge_senden).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println("ending dialog");
                    FragmentManager myManager = getActivity().getSupportFragmentManager();
                    DialogFragment whoAmI = (DialogFragment) myManager.getFragments().get(0);
                    whoAmI.dismiss();
                    client.randomGameQueue("stop");
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}

