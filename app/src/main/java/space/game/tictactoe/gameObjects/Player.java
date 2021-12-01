package space.game.tictactoe.gameObjects;

public class Player {
    // keys of the JSON - analog to Player @Server
    private String name;
    private String firebaseId;
    private Long uid;

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
