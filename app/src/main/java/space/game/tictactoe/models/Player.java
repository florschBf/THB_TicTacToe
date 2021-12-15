package space.game.tictactoe.models;

public class Player {
    // keys of the JSON - analog to Player @Server
    //TODO diese Werte beim Start der App setzen oder durch den Nutzer setzen lassen
    //TODO diese Werte beim Anmelden am Websocketserver verwenden (Playerobjekt in OnlinespielActivity)
    private String name;
    private String firebaseId;
    private Long uid; // Denke wir brauchen hier keine uid. Eigentlich sollte es nur ein Player-Objekt in der App geben, oder?

    public Player(String name, String firebaseId, Long uid) {
        this.name = name;
        this.firebaseId = firebaseId;
        this.uid = uid;
    }

    public Player() {
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", firebaseId='" + firebaseId + '\'' +
                ", uid=" + uid +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public Long getUid() {
        return uid;
    }
}
