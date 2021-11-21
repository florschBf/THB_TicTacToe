package space.game.tictactoe.services;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import space.game.tictactoe.gameObjects.Player;

public interface JsonPlaceholderApi {
    //relative Url
    @GET("/playerList/")
    Call<List<Player>> getPlayerList();
}
