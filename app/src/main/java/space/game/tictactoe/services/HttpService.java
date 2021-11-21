package space.game.tictactoe.services;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * implement a Singleton-Class to ensure, that there is only one Requestqueue
 *
 * */
public class HttpService {
    private static HttpService INSTANCE = null;
    // substitute here baseUrl by url of server
    public static final String BASE_URL = "http://192.168.0.100:8080";

    private Retrofit httpRetrofitRequestQueue;
    // private Retrofit httpRetrofitRequestQueue;
    private static Context cntxt;

    // @TODO implement more Services in buildRetrofit-method later
    private PlayerService playerService;


    // Constructor-Method
    private HttpService(Context context) {
        cntxt = context;
        // httpRetrofitRequestQueue = getRetrofitRequestQueue();
        buildRetrofit(BASE_URL);
    }

    /**
     * Keypart of Singleton-Implementation
     * only allow one class of HttpService to be instantiated
     * */
    public static synchronized HttpService getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HttpService(context);
        }
        return INSTANCE;
    }
    // Request a Retrofit Response from the provided URL (baseUrl has to be the URL of the App-backend-server)
    // Instantiate the Request
    public Retrofit getRetrofitRequestQueue() {
        // only instantiate a new RequestQueue, if there is not already another queue
        if (httpRetrofitRequestQueue == null) {
            httpRetrofitRequestQueue = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return httpRetrofitRequestQueue;
    }

    public void buildRetrofit(String baseURL) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.playerService = retrofit.create(PlayerService.class);
    }

    public PlayerService getPlayerService() {
        return this.playerService;
    }
//    public void addToRequestQueue(Retrofit req) {
//        getRetrofitRequestQueue().add ..
//    }

}
