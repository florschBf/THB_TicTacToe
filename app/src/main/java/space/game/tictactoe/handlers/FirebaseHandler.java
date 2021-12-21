package space.game.tictactoe.handlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import space.game.tictactoe.models.Player;
import space.game.tictactoe.models.UserStatistics;

public class FirebaseHandler extends AppCompatActivity {



    Player player = Player.getPlayer();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    // private final DocumentReference playerDataReference = db.collection("users").document(player.getFirebaseId());

    public static FirebaseHandler firebaseHandler;

    // private int db_wins;
    private static Long db_wins;
    private static Long db_losses;
    private static Long db_draws;
    private Map<String, Object> playerFirestoreData;


    public FirebaseHandler() {
        firebaseHandler = this;
    }


    public static FirebaseHandler getFirebaseHandler(){
        return firebaseHandler;
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


    public void addPlayerData() throws Exception {
        // FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // assert firebaseUser != null;
        String firebaseUser = player.getFirebaseId();
        if (!firebaseUser.equals("unknown")) {
            db.collection("users").document(player.getFirebaseId()).set(player).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    System.out.println("Playerdata added to FireStore");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Playerdata not added to FireStore. Got Failure: " + e.getMessage());
                }
            });
        } else {
            System.out.println("Playerdata not added to FireStore because Player has no valid FirebaseId");
            throw new Exception("Player has no valid FirebaseId");
        }
    }

    public void getPlayerData() throws Exception {
        // FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // assert firebaseUser != null;
        String firebaseUser = player.getFirebaseId();
        if (!firebaseUser.equals("unknown")) {
            System.out.println("Trying to get stored Playerdata from Firestore");

            db.collection("users").document(player.getFirebaseId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                //private Long db_wins;

                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        db_wins = documentSnapshot.getLong("wins");
                        db_losses = documentSnapshot.getLong("losses");
                        db_draws = documentSnapshot.getLong("draws");

                        System.out.println("Got Userdata: " + "Wins: " + db_wins.toString()  + "Losses: " + db_losses.toString()  + "Draws: " + db_draws.toString() );
                        playerFirestoreData = documentSnapshot.getData();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Something went wrong by reading data from firestore. Go Failor: " + e.getMessage());
                }
            });

        } else {
            System.out.println("Could not read Playerdata (UserData) from FireStore because Player has no valid FirebaseId");
            throw new Exception("Player has no valid FirebaseId");
        }
    }

    /*@TODO Update stored firestore-data
     * how to workarround data-update?
     * 0) check, if there are Data found with the firebase-id - if not: set new Data (from client)
     * 1) read stored PlayerData from Firestore
     * 2) if there are values different then the recent values: substitute - but only settings, NOT gameresults
     * 3) add recent gameresults (wins, losses, draws) to stored data
     * */

}
