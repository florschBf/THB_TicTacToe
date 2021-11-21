package space.game.tictactoe.services;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import space.game.tictactoe.gameObjects.Player;
import retrofit2.Response;

public class PlayersService {

    public Player player;
    List<Player> playerList;
    Context context;

    public PlayersService(Context context) {
        this.context = context;
    }

    public interface RetrofitResponseListener {
        void onError(String message);

        void onResponse(List<Player> playerListResponse);
    }


    // private PlayerServiceApi playerServiceApi;
    // call a PlayerList as Response
    public void getPlayerList(RetrofitResponseListener retrofitResponseListener) {

        PlayersServiceApi playersServiceApi = HttpService.getInstance(context).getPlayerService();
        Call<List<Player>> request = playersServiceApi.getPlayerList();

        // all Requests are treated one by another, so a new Request has to enque to previous Requests
        // when Request proceeded, execute this Callback-Function
        request.enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(@NonNull Call<List<Player>> request, @NonNull Response<List<Player>> response) {
                // the Request was executed (onResponse)
                try {
                    if (!response.isSuccessful()) {
                        // No successfull Response on Request-Call
                        // textViewPlayerlistResponse.setText("No succes: " + response.code());
                        retrofitResponseListener.onError("Something went wrong");
                    } else if (response.body() != null) {
                        // Successfull Response on Request-Call and Response-Body not empty

                        playerList = response.body();

                        retrofitResponseListener.onResponse(playerList);

                        for (Player player : playerList) {
                            String content = "";
                            content += "firebaseId: " + player.getFirebaseId() + "\n";
                            content += "name: " + player.getName() + "\n";

                            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
                            //  textViewPlayerlistResponse.setText(content);
                        }
                    } else {
                        Toast.makeText(context, "No Online-Players found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            // Something after executed Request went wrong
            @Override
            public void onFailure(@NonNull Call<List<Player>> call, @NonNull Throwable t) {
                retrofitResponseListener.onError("Something went wrong");
                Toast.makeText(context, "Error occured: \" + t.getMessage()", Toast.LENGTH_SHORT).show();
//                textViewPlayerlistResponse.setText("Failure: " + t.getMessage());
            }
        });

        //return playerList;
        //return null;
    }
}
