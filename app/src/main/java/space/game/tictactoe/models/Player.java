package space.game.tictactoe.models;

public class Player {
    // keys of the JSON - analog to Player @Server
    private String name;
    private String firebaseId;
    private Long uid;

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
