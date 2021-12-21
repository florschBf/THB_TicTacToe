package space.game.tictactoe;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import space.game.tictactoe.handlers.FirebaseHandler;
import space.game.tictactoe.models.Player;

public class StatistikenActivity extends AppCompatActivity {


    FirebaseHandler firebaseHandler = FirebaseHandler.getFirebaseHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistiken);
        System.out.println("Statistics onCreate called");
        // update the FirebaseDate:
        // call updateStatistics fetches actual Player properties and writes them into the Firestore
        firebaseHandler.updateStatistics();
        try {
            firebaseHandler.addUserData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            firebaseHandler.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}