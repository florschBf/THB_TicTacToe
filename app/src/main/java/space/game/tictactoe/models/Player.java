package space.game.tictactoe.models;

import java.util.ArrayList;
import java.util.Random;

import space.game.tictactoe.R;

public class Player {

    // keys of the JSON - analog to Player @Server
    //TODO diese Werte beim Start der App setzen oder durch den Nutzer setzen lassen
    //TODO diese Werte beim Anmelden am Websocketserver verwenden (Playerobjekt in OnlinespielActivity)
    private String name = "unknown";
    private String email = "unknown";
    private String firebaseId = "unknown";
    private String serverId = "unknown"; // Wird vom Server aus der Connection gehashed
    private int icon = R.drawable.stern_90; // set icon as default from fireBase

    //sound
    private static boolean isTonOn = true;


    /*
     * How to store the results of a game at firestore and make it accessable for Statistics
     * */
    // easy way
    // statistics counted on the actual session
    private long wins = 0;
    private long losses = 0;
    private long draws  = 0;
    private long interrupted = 0;
    private long totalGames = 0;



    // more komplex way
    private ArrayList<Game> gameResults;

    public static Player player;

    public Player() {
        this.name = randomName();
        player = this;
        System.out.println("New Player-Instanciated: " + player);
    }

    private Boolean isPlayerAlreadyUpdatedByFirestoreData = false;


    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", firebaseId='" + firebaseId + '\'' +
                ", serverId=" + serverId +
                '}';
    }

    public void setName(String name) { this.name=name; }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId=firebaseId;
    }

    public void setUid(String uid) {
        this.serverId=uid;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setIsTonOn(boolean isTonOn) {this.isTonOn = isTonOn; }
    public boolean getIsTonOn() {return isTonOn; }


    public static Player getPlayer(){
        return player;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


    public String getFirebaseId() {
        return firebaseId;
    }

    public String getServerId() {
        return serverId;
    }

    private String randomName() {
        Random rnd = new Random();
        char letter = (char) ('A' + rnd.nextInt(26));
        String randomName = "Player-" + letter;;
        return randomName;
    }

    public int getIcon() {
        return icon;
    }

    /*
     * statistics
     */

    public long getWins() {
        return wins;
    }

    public long getLosses() {
        return losses;
    }

    public long getDraws() {
        return draws;
    }

    public long getInterrupted() {
        return interrupted;
    }

    public long getTotalGames() {
        return wins + losses + draws + interrupted;
    }


    public void increaseWins() {
        this.wins++;
    }

    public void increaseLosses() {
        this.losses++;
    }

    public void increaseDraws() {
        this.draws++;
    }

    public void increaseInterrupted() { this.interrupted++; }

    public void increasetotalGames() { this.totalGames++; }

    public void updateWins(long winsFromFireStore) {
        this.wins += winsFromFireStore;
    }

    public void updateLosses(long lossesFromFireStore) {
        this.losses += lossesFromFireStore;
    }

    public void updateDraws(long drawsFromFireStore) {
        this.draws += drawsFromFireStore;
    }

    public void updateInterrupted(long interruptedFromFireStore) {
        this.draws += interruptedFromFireStore;
    }

    public Boolean getPlayerAlreadyUpdatedByFirestoreData() {
        return isPlayerAlreadyUpdatedByFirestoreData;
    }

    public void setPlayerAlreadyUpdatedByFirestoreData(Boolean playerAlreadyUpdatedByFirestoreData) {
        isPlayerAlreadyUpdatedByFirestoreData = playerAlreadyUpdatedByFirestoreData;
    }


    public void updateTotalGames(long totalGamesFromFireStore) {
        this.totalGames += totalGamesFromFireStore;
    }








    }
