package space.game.tictactoe.services;

import java.util.List;

import retrofit2.Call;
import space.game.tictactoe.gameObjects.Player;
import space.game.tictactoe.services.JsonPlaceholderApi;

public class PlayerService {

    public static JsonPlaceholderApi jsonPlaceholderApi = HttpService.retrofit.create(JsonPlaceholderApi.class);

    public static Call<List<Player>> call = jsonPlaceholderApi.getPlayerList();

}
