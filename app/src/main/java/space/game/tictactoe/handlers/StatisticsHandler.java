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

/**
 *  This Class handles following cases:
 *  0) not played but logged in
 *     ==> show stored data (and added/updated by local data/settings)
 *  1) played w/o login --> called statistics
 *     ==> show actual local playerdata and settings
 *  2) played w login --> called statistics
 *     ==> show stored gameresults and local settings
 *  3) played w/o login --> login --> play --> statistics
 *     ==> add local data to stored data and add local data again
 *  4) played w login --> called statistics --> play --> call statistic
 *     ==> get stored data and show, add stored data to local, show updated data
 *  How is this handled?
 *     when statistic called show/get local playerdata, which was updated with firestore-playerdata on login
 *     only when when login then get stored data and add to local data
 *     When to store playerdata (write in firestore)?: on logout when logged in before
 */
public class StatisticsHandler {


    /**
     * Declaration and in intialization of membervariables
     *
     * declare db and initialize db with an instance of FirebaseFirestore-object
     */
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    // private final DocumentReference playerDataReference = db.collection("users").document(player.getFirebaseId());

    /**
     * declare and initialize a player with the only instance of the player-object
     */
    Player player = Player.getPlayer();

    public static StatisticsHandler statisticsHandler;

    private static Long db_wins;
    private static Long db_losses;
    private static Long db_draws;
    private static int db_icon;
    private static int totalGames;

    /**
     * Constructor of Class StatisticsHandler
     * writes state of all attributes in a static member-variable called statisticsHandler
     */
    public StatisticsHandler() {
        statisticsHandler = this;
    }


    /**
     * @return Object of instanciated Class StatisicsHandler
     */
    public static StatisticsHandler getStatisticsHandler(){
        return statisticsHandler;
    }



    /**
     * Store local playerdata in firebase database if the player is logged in.
     * If the Player is logged a firebase-Id is given by firebase and overrides the default-value "unknown"
     * @throws Exception "Player seems not to be logged in: FirebaseId is \"unknwon\". playerdata could not be stored at firebase."
     */
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

    /**
     * Update der Attribute der Player-Klasse mit den entsprechenden in Firebase gespeicherten Daten
     * Only on login get stored playerdata and add them to local playerdata -> that´s why check if a firebaseId is given
     * When to store playerdata (write in firestore)?: on logout when logged in before
     * Aktualisierte Attribute:
     * wins
     * losses
     * draws
     * interrupted
     * totalGames
     * isPlayerAlreadyUpdatedByFirestoreData
     * @throws Exception
     */
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


}
