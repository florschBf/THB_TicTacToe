package space.game.tictactoe.handlers;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import space.game.tictactoe.models.Player;

public class FirebaseHandler extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    Player player = Player.getPlayer();

    public static FirebaseHandler firebaseHandler;

    public FirebaseHandler() {
        firebaseHandler = this;
    }

    public void updateStatistics(){
        System.out.println("updateStatistics called " + player);
        CollectionReference dbStatistics = db.collection("UserStatistics");

        Map<String, Object> userStatistics = new HashMap<>();
        userStatistics.put("name", player.getName());
        // firebaseId already given and used to connect with these data?
        // userStatistics.put("firebaseId", player.getName());
        userStatistics.put("icon", player.getIcon());
        userStatistics.put("wins", player.getWins());
        userStatistics.put("losses", player.getLosses());
        userStatistics.put("draws", player.getDraws());
        userStatistics.put("interrupted", player.getInterrupted());

        CollectionReference dbUserStatistics = db.collection("userStatistics");

        System.out.println("userStatistics: " + userStatistics);

        dbUserStatistics.add(userStatistics).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                System.out.println("userStatistics stored: " + userStatistics);
//                 Toast.makeText(getApplicationContext(), "Statistics updated", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("userStatistics NOT stored! Error: " + e.getMessage());

//                 Toast.makeText(getApplicationContext(), "Statistics NOT updated. Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static FirebaseHandler getFirebaseHandler(){
        return firebaseHandler;
    }

}
