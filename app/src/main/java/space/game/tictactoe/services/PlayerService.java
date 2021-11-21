package space.game.tictactoe.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import space.game.tictactoe.gameObjects.Player;
import space.game.tictactoe.services.JsonPlaceholderApi;


public interface PlayerService {
    //relative Url
    @GET("/playerList/")
    Call<List<Player>> getPlayerList();
}