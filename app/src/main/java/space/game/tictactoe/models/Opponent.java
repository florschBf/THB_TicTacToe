package space.game.tictactoe.models;

public class Opponent {

    /**
     * Declaration and in intialization of membervariables
     */
    private String name;
    private String firebaseId;
    private String serverId;
    private int listPosition;
    private boolean isBusy = false;

    /**
     * constructor of class Opponent
     * initializing decared membervariables
     * @param name Name of the opponent - could be a name given on login or a random-name
     * @param firebaseId Id at firebase to identify the firebase-data-object of the opponent
     */
    public Opponent(String name, String firebaseId, String serverId){
        this.name = name;
        this.firebaseId = firebaseId;
        this.serverId = serverId;
    }

    /**
     * @return name of opponent at an onlinegame
     */
    public String getName() {
        return name;
    }

    /**
     * initialize name
     * @param name set a opponent´s name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    /**
     * @return Id of the firebase-data-object
     */
    public String getFirebaseId() {
        return firebaseId;
    }

    /**
     * initialize firebaseId
     * @param firebaseId to identify the firebase-data-object containing all stored opponents data
     */
    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    /**
     * Find out the position of opponent in a list,
     * probably important to identify the opponent in a list when selected
     * @return number as position in a Playerlist
     */
    public int getListPosition() {
        return listPosition;
    }

    /**
     * Set a position of the opponent in a list of players
     * @param listPosition number as position in a Playerlist
     */
    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    /**
     * find out if opponent is in a game or otherwise occupied
     * @return <code>true</code> if opponent is busy with a game or something else and can´t play a onlinegame
     *         <code>false</code> if opponent isn´t busy and is ready to get invited for a onlinegame
     */
    public boolean isBusy() {
        return isBusy;
    }

    /**
     * Set or update the state of the opponent to show, if it can be invited for a onlinegame or not
     * @param busy <code>true</code> opponent is set busy
     *             <code>false</code> opponent is set ready/not busy
     */
    public void setBusy(boolean busy) {
        isBusy = busy;
    }
}
