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
    public static Player player;


    /*
     * How to store the results of a game at firestore and make it accessable for Statistics
     * */
    // easy way
    // statistics counted on the actual session
    private int wins = 0;
    private int losses = 0;
    private int draws  = 0;
    private int interrupted = 0;

    // more komplex way
    private ArrayList<Game> gameResults;

//    public Player(String name, String firebaseId, String serverId) {
//        this.name = name;
//        this.firebaseId = firebaseId;
//        this.serverId = serverId;
//        player = this;
//
//        System.out.println("New Player-Instanciated: " + player);
//    }

    public Player() {
        this.name = randomName();
        player = this;
        System.out.println("New Player-Instanciated: " + player);
    }


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

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }

    public int getInterrupted() {
        return interrupted;
    }


    public void setWins() {
        this.wins++;
    }

    public void setLosses() {
        this.losses++;
    }

    public void setDraws() {
        this.draws++;
    }

    public void setInterrupted() {
        this.interrupted++;
    }



}
