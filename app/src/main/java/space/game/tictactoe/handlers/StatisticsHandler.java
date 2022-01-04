package space.game.tictactoe.handlers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import space.game.tictactoe.models.Player;

//public class StatisticsHandler extends AppCompatActivity {
public class StatisticsHandler {


    Player player = Player.getPlayer();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    // private final DocumentReference playerDataReference = db.collection("users").document(player.getFirebaseId());

    public static StatisticsHandler statisticsHandler;

    // private int db_wins;
    private static Long db_wins;
    private static Long db_losses;
    private static Long db_draws;
    private static int db_icon;
    private static int totalGames;
    private Map<String, Object> playerFirestoreData = null;


    public StatisticsHandler() {
        statisticsHandler = this;
    }


    public static StatisticsHandler getStatisticsHandler(){
        return statisticsHandler;
    }

    /* @TODO cases
     * 0) not played but logged in
     * ==> show stored data (and added/updated by local data/settings)
     * 1) played w/o login --> called statistics
     * ==> show actual local playerdata and settings
     * 2) played w login --> called statistics
     * ==> show stored gameresults and local settings
     * 3) played w/o login --> login --> play --> statistics
     * ==> add local data to stored data and add local data again
     * 4) played w login --> called statistics --> play --> call statistic
     * ==> get stored data and show, add stored data to local, show updated data
     * How to do this all?:
     * when statistic called show/get local playerdata, which was updated with firestore-playerdata on login
     * only when when login then get stored data and add to local data
     * When to store playerdata (write in firestore)?: on logout when logged in before
     * */


    // this method adds a new document to firesstore each time called
    // actually it stores the selected actual Player-properties when called
    // not usefull to store the statistics refering to a certain player
//    public void addStatistics(){
//        System.out.println("updateStatistics called " + player);
//        CollectionReference dbStatistics = db.collection("UserStatistics");
//
//        Map<String, Object> userStatistics = new HashMap<>();
//        userStatistics.put("name", player.getName());
//        // firebaseId already given and used to connect with these data?
//        // userStatistics.put("firebaseId", player.getName());
//        userStatistics.put("icon", player.getIcon());
//        userStatistics.put("wins", player.getWins());
//        userStatistics.put("losses", player.getLosses());
//        userStatistics.put("draws", player.getDraws());
//        userStatistics.put("interrupted", player.getInterrupted());
//
//        CollectionReference dbUserStatistics = db.collection("userStatistics");
//
//        System.out.println("userStatistics: " + userStatistics);
//
//        dbUserStatistics.add(userStatistics).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                System.out.println("userStatistics stored: " + userStatistics);
////                 Toast.makeText(getApplicationContext(), "Statistics updated", Toast.LENGTH_LONG).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                System.out.println("userStatistics NOT stored! Error: " + e.getMessage());
//
////                 Toast.makeText(getApplicationContext(), "Statistics NOT updated. Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }


    // Only when when login then get stored data and add to local data -> that´s why check if a firebaseId is given
    // When to store playerdata (write in firestore)?: on logout when logged in before
    public void setPlayerData() throws Exception {
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
            throw new Exception("Player seems not to be logged in: FirebaseId is \"unknwon\". playerdata could not be stored at firebase.");
        }
    }

    public void updateLocalPlayerDataWithFbData() throws Exception {
        String firebaseUser = player.getFirebaseId();
        if (!firebaseUser.equals("unknown")) {
            System.out.println("Trying to update stored Playerdata from Firestore. Adding stored Data from Firestore to recent local PlayerData.");

            db.collection("users").document(player.getFirebaseId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        if (!player.getPlayerAlreadyUpdatedByFirestoreData()) {
                            player.updateWins(documentSnapshot.getLong("wins"));
                            player.updateLosses(documentSnapshot.getLong("losses"));
                            player.updateDraws(documentSnapshot.getLong("draws"));
                            player.updateInterrupted(documentSnapshot.getLong("interrupted"));
                            player.updateTotalGames(documentSnapshot.getLong("totalGames"));
                            player.setPlayerAlreadyUpdatedByFirestoreData(true);
                        } else {
                            System.out.println("Local PlayerData had been updated by FireStoreData before. Local Playerdata are ahead.");
                        }

                    } else {
                        System.out.println("Document " + documentSnapshot + " does not exist on FireStore");
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("Could not update local PlayerData (Statistics) by adding stored FirenbaseData. Got error: " + e.getMessage());
                }
            });
        } else {
            System.out.println("Local PlayerData could not be updated with Firestore PlayerData, because FireBaseId is \"unknown\". Probably because of user not logged in yet. User needs to login first!");
            throw new Exception("Local PlayerData could not be updated with Firestore PlayerData, because FireBaseId is \"unknown\". Probably because of user not logged in yet. User needs to login first!");
        }
    }

    //    public Map<String, Object> getPlayerData() throws Exception {
//        // FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        // assert firebaseUser != null;
//        String firebaseUser = player.getFirebaseId();
//
//        if (!firebaseUser.equals("unknown")) {
//            System.out.println("Trying to get stored Playerdata from Firestore");
//
//            db.collection("users").document(player.getFirebaseId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//
//                //private Long db_wins;
//
//                @Override
//                public void onSuccess(DocumentSnapshot documentSnapshot) {
//                    if (documentSnapshot.exists()){
//                        db_wins = documentSnapshot.getLong("wins");
//                        db_losses = documentSnapshot.getLong("losses");
//                        db_draws = documentSnapshot.getLong("draws");
//
//                        System.out.println("Got Userdata: " + "Wins: " + db_wins.toString()  + " Losses: " + db_losses.toString()  + " Draws: " + db_draws.toString()  + " Total Games: " + totalGames);
//                        playerFirestoreData = documentSnapshot.getData();
//
//                        try {
//                            updatePlayerData(playerFirestoreData);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//                        try {
//                            setPlayerData();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    System.out.println("Something went wrong by reading data from firestore. Go Failor: " + e.getMessage());
//                }
//            });
//            return playerFirestoreData;
//        } else {
//            System.out.println("Could not read Playerdata (UserData) from FireStore because Player has no valid FirebaseId");
//            throw new Exception("Player has no valid FirebaseId");
//        }
//    }

    /*@TODO Update stored firestore-data
     * how to workarround data-update?
     * 0) check, if there are Data found with the firebase-id - if not: set new Data (from client)
     * 1) read stored PlayerData from Firestore
     * 2) if there are values different then the recent values: substitute - but only settings, NOT gameresults
     * 3) add recent gameresults (wins, losses, draws) to stored data
     * */
//    public void updatePlayerData( Map<String, Object> firestorePlayerData) throws Exception {
//        String firebaseUser = player.getFirebaseId();
//        if (!firebaseUser.equals("unknown")) {
//            System.out.println("Trying to update stored Playerdata from Firestore. Adding recent sessiongameresults to stored data at firestore.");
//
////            Map<String, Object> firestorePlayerData = this.getPlayerData();
//            // Int updatedWins = firestorePlayerData.getLong("wins").toString(). + player.getWins();
//            if (firestorePlayerData != null && !firestorePlayerData.isEmpty()) {
//                // Map<String, Object> updatefirestorePlayerData = new HashMap<>();
////                System.out.println("Replace last values: wins: " + (long) firestorePlayerData.get("wins") + " with: " + ((long) firestorePlayerData.get("wins") + player.getWins()));
////
////                firestorePlayerData.replace("wins",  ((long) firestorePlayerData.get("wins") + player.getWins()));
//
//                System.out.println("Replace last values: losses: " + (long) firestorePlayerData.get("losses") + " with: " + ((long) firestorePlayerData.get("losses") + player.getLosses()));
//                firestorePlayerData.replace("losses",  ((long) firestorePlayerData.get("losses") + player.getLosses()));
//
//                System.out.println("Replace last values: draws: " + (long) firestorePlayerData.get("draws") + " with: " + ((long) firestorePlayerData.get("draws") + player.getDraws()));
//                firestorePlayerData.replace("draws",  ((long) firestorePlayerData.get("draws") + player.getDraws()));
//
//
//                firestorePlayerData.replace("icon",  (int) firestorePlayerData.get("icon") + player.getIcon());
//                firestorePlayerData.replace("totalGames",  ((int) firestorePlayerData.get("totalGames") + player.getTotalGames()));
//
////                db.collection("users").document(firebaseUser).update(firestorePlayerData).addOnCompleteListener(new OnCompleteListener<Void>(){
////                    @Override
////                    public void onComplete(@NonNull Task<Void> task) {
////                        System.out.println("Updating firestore completed.");
////                    }
////                });
//
//                db.collection("users").document(firebaseUser).update("wins", ((long) firestorePlayerData.get("wins") + player.getWins())).addOnCompleteListener(new OnCompleteListener<Void>(){
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        System.out.println("Updating \"wins\" at firestore completed.");
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        System.out.println("Updating \"wins\" at firestore succeded.");
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        System.out.println("Failure by updating \"wins\" at firestore.");
//                    }
//                });
//            } else {
//                this.setPlayerData();
//            }
//
//        }
//    }
}
