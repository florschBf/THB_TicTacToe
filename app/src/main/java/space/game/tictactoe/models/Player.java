package space.game.tictactoe.models;

import java.util.ArrayList;
import java.util.Random;

import space.game.tictactoe.R;

public class Player {

    // keys of the JSON - analog to Player @Server
    //TODO diese Werte beim Start der App setzen oder durch den Nutzer setzen lassen
    //TODO diese Werte beim Anmelden am Websocketserver verwenden (Playerobjekt in OnlinespielActivity)
    private String name = "unknown";
    private String firebaseId = "unknown";
    private String serverId = "unknown"; // Wird vom Server aus der Connection gehashed
    private int icon = R.drawable.stern_90;;

    //sound
    private static boolean isTonOn = true;
    public static Player player;

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

    // statistics

}
