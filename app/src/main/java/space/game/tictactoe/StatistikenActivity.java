package space.game.tictactoe;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import space.game.tictactoe.handlers.FirebaseHandler;

public class StatistikenActivity extends AppCompatActivity {


    FirebaseHandler firebaseHandler = FirebaseHandler.getFirebaseHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiken);
        System.out.println("Statistics onCreate called");
        // update the FirebaseDate:
        // call updateStatistics fetches actual Player properties and writes them into the Firestore
        // firebaseHandler.updateStatistics();
        try {
            firebaseHandler.addPlayerData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            firebaseHandler.getPlayerData();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error happend by getting stored playerdata from firebase. Got error: " + e.getMessage());
        }

    }
}