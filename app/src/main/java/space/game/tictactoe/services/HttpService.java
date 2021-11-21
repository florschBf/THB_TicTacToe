package space.game.tictactoe.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpService {

    // substitute baseUrl by url of server
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.0.100:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

}
