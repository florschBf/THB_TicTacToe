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

import space.game.tictactoe.models.Player;
import space.game.tictactoe.models.UserStatistics;

public class FirebaseHandler extends AppCompatActivity {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    Player player = Player.getPlayer();

    public static FirebaseHandler firebaseHandler;

    List<Object> dbContentList;

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

//    public List readStatistics(){
//
//        db.collection("userStatistics").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                List<DocumentSnapshot> dbContentSnapshotList = null;
//                if(!queryDocumentSnapshots.isEmpty()){
//                    dbContentSnapshotList = queryDocumentSnapshots.getDocuments();
//                    for (DocumentSnapshot document : dbContentSnapshotList){
//                        UserStatistics userStatistics = document.toObject(UserStatistics.class);
//                    }
//                } else {
//                    dbContentSnapshotList = null;
//                }
//            }
//        });
//        return dbContentList;
//    }

    public void addUserData() throws Exception {
        // FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // assert firebaseUser != null;
        String firebaseUser = player.getFirebaseId();
        if (!firebaseUser.equals("unknown")) {
            FirebaseFirestore.getInstance().collection("users").document(player.getFirebaseId()).set(player);
        } else {
            System.out.println("Playerdata not added to FireStore because Player has no valid FirebaseId");
            throw new Exception("Player has no valid FirebaseId");
        }
    }

    public void getUserData() throws Exception {
        // FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // assert firebaseUser != null;
        String firebaseUser = player.getFirebaseId();
        if (!firebaseUser.equals("unknown")) {
            Object userData = FirebaseFirestore.getInstance().collection("users").document(player.getFirebaseId()).get();
            System.out.println("Got Userdata: " + userData);
        } else {
            System.out.println("Could not read Playerdata (UserData) from FireStore because Player has no valid FirebaseId");
            throw new Exception("Player has no valid FirebaseId");
        }
    }

}
