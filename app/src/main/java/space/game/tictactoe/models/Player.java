package space.game.tictactoe.models;

import java.util.Random;

import space.game.tictactoe.R;

public class Player {

    /**
     * Declaration and in intialization of membervariables
     * keys of the JSON - analog to Player @Server
     * Set on start of app or triggered by user
     * Used on signIn to Websocketserver (Playerobject in OnlinespielActivity)
     */
    private String name = "unknown";
    private String email = "unknown";
    private String firebaseId = "unknown";
    private String serverId = "unknown"; // Wird vom Server aus der Connection gehashed

    public int icon = R.drawable.stern_90; // set icon as default from fireBase
    public int board = R.drawable.play_board; // default play board selected at start


    /**
     * Declaration and in itialization of membervariable sound
     */
    private static boolean isTonOn = true;

    /**
     * Declaration and in itialization of membervariables
     * statistics counted on the actual session
     */
    private long wins = 0;
    private long losses = 0;
    private long draws  = 0;
    private long interrupted = 0;
    private long totalGames = 0;

    /**
     * Declaration and in itialization of membervariable isPlayerAlreadyUpdatedByFirestoreData
     */
    private Boolean isPlayerAlreadyUpdatedByFirestoreData = false;

    /**
     * Declaration of Obejectvariable
     */
    public static Player player;

    /**
     * Constructor of class player
     */
    public Player() {
        this.name = randomName();
        player = this;
        System.out.println("New Player-Instanciated: " + player);
    }


    /**
     *  Returns the player-object as a string, including member variables:
     *  name
     *  firebaseId
     *  serverId
     * @return String player-info
     */
    @Override
    public String toString() {
        return "Player{'icon=' " + icon + '\'' +
                "name='" + name + '\'' +
                ", firebaseId='" + firebaseId + '\'' +
                ", serverId=" + serverId +
                '}';
    }

    /**
     * update default player-name by passed name
     * @param name The name of the player, retrieved from database after login
     */
    public void setName(String name) { this.name=name; }

    /**
     * update default player-email by passed email
     * @param email The e-mail of the player, retrieved from database after login
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * update default player-firebaseId by passed firebaseId
     * @param firebaseId The firebaseId of the player, given by firebase and retrieved from there after login
     */
    public void setFirebaseId(String firebaseId) {
        this.firebaseId=firebaseId;
    }

    /**
     * @param uid Unique Player Id given from server to identify this player
     */
    public void setServerId(String uid) {
        this.serverId = uid;
    }

    /**
     * update default player-icon by passed icon-number
     * @param icon The selected icon shown in game-display of the player
     */
    public void setIcon(int icon) {
        this.icon = icon;
    }

    /**
     * update of membervariable isTonOn
     * needed for implementation of sound-features
     * @param isTonOn To toggle sound on/off
     */
    public void setIsTonOn(boolean isTonOn) {this.isTonOn = isTonOn; }


    /**
     * get local value of isTonOn
     * @return player-sound is on or off
     *         <code>true</code> if Ton is switched on (default)
     *         <code>false</code> if Ton is switched off
     */
    public boolean getIsTonOn() {return isTonOn; }

    /**
     * get the player-object
     * @return The player-object with all local attribut-values
     */
    public static Player getPlayer(){
        return player;
    }

    /**
     * get local value of player-name
     * @return player-name
     *         name is a random name by default
     *         if player has logged in it returns the local name updated by firebase-data
     */
    public String getName() {
        return name;
    }

    /**
     * get local E-mail
     * @return player-E-mail
     *         if player has logged in it returns the local E-Mail updated by firebase-data
     *         otherwise "unknown"
     */
    public String getEmail() {
        return email;
    }

    /**
     * get the firebaseId
     * @return player-firebaseId
     *         if player has logged in it returns the local firebaseId updated by firebase-data
     *         otherwise "unknown"
     */
    public String getFirebaseId() {
        return firebaseId;
    }

    /**
     * get Id of the server
     * @return server-Id given by the server
     */
    public String getServerId() {
        return serverId;
    }

    /**
     *
     * @return build a default-Playername
     *         get a random letter
     *         Player-<letter>
     */
    public String randomName() {
        Random rnd = new Random();
        char letter = (char) ('A' + rnd.nextInt(26));
        String randomName = "Player-" + letter;;
        return randomName;
    }

    /**
     * get the local player-icon
     * @return The local icon
     */
    public int getIcon() {
        return icon;
    }


    public int getBoard() {
        return board;
    }

    public void setBoard(int board) {
        this.board = board;
    }

    // statistics

    /**
     * @return the number of local counted wins
     */
    public long getWins() {
        return wins;
    }

    /**
     * Get
     * @return the  number of local counted losses
     */
    public long getLosses() {
        return losses;
    }

    /**
     * @return the number of local counted draws
     */
    public long getDraws() {
        return draws;
    }

    /**
     * @return the number of local counted interruptions, caused by not finished games
     */
    public long getInterrupted() {
        return interrupted;
    }

    /**
     * Get number of all games of a player
     * @return If player has logged in the total number of games are once added to local number of games
     *         Sum of all counted values of wins, losses, draws, interrupted
     */
    public long getTotalGames() {
        return wins + losses + draws + interrupted;
    }


    /**
     * when called increase local wins +1
     */
    public void increaseWins() {
        this.wins++;
    }

    /**
     * when called increase local losses +1
     */
    public void increaseLosses() {
        this.losses++;
    }

    /**
     * when called increase local draws +1
     */
    public void increaseDraws() {
        this.draws++;
    }

    /**
     * when called increase interrupted +1
     */
    public void increaseInterrupted() { this.interrupted++; }

    /**
     * when called increase totalGames +1
     */
    public void increasetotalGames() { this.totalGames++; }

    /**
     * Update local wins by adding wins retrieved from firestore on login
     * If player had logged in remote wins are only once added to local wins
     * @param winsFromFireStore The wins stored at firestore
     */
    public void updateWins(long winsFromFireStore) {
        this.wins += winsFromFireStore;
    }

    /**
     * Update local losses by adding losses retrieved from firestore on login
     * If player had logged in remote losses are only once added to local losses
     * @param lossesFromFireStore The losses stored at firestore
     */
    public void updateLosses(long lossesFromFireStore) {
        this.losses += lossesFromFireStore;
    }

    /**
     * Update local draws by adding draws retrieved from firestore on login
     * If player had logged in remote draws are only once added to local draws
     * @param drawsFromFireStore The draws stored at firestore
     */
    public void updateDraws(long drawsFromFireStore) {
        this.draws += drawsFromFireStore;
    }

    /**
     * Update local interruption-counter by adding interruptions retrieved from firestore on login
     * If player had logged in interruptions are only once added to local interruptions
     * @param interruptedFromFireStore The interruptions stored at firestore
     */
    public void updateInterrupted(long interruptedFromFireStore) {
        this.draws += interruptedFromFireStore;
    }

    /**
     * Check if the local player-data has been updated already by remote firestore-data
     * to avoid multiple counting of firestore-values at statistics
     * @return is Player Already Updated By Firestore Data
     *      *         <code>true</code>  updated - firestore-data already had been added to player-statistics
     *      *         <code>false</code> not updated yet (default)
     */
    public Boolean getPlayerAlreadyUpdatedByFirestoreData() {
        return isPlayerAlreadyUpdatedByFirestoreData;
    }

    /**
     * Set a value to check, if the firestoredata had been already retrieved
     * to avoid multiple counting of firestore-values at statistics
     * @param playerAlreadyUpdatedByFirestoreData
     *        <code>true</code> is set if firestroedata had been already updated by firestoredata
     *        <code>false</code> is set if by default
     */
    public void setPlayerAlreadyUpdatedByFirestoreData(Boolean playerAlreadyUpdatedByFirestoreData) {
        isPlayerAlreadyUpdatedByFirestoreData = playerAlreadyUpdatedByFirestoreData;
    }

    /**
     * add stored firestore games to number of local games
     * @param totalGamesFromFireStore number of all games
     */
    public void updateTotalGames(long totalGamesFromFireStore) {
        this.totalGames += totalGamesFromFireStore;
    }

    /*
     * setter for wins, losses, draws - e. g. when logged out
     *
     */

    public void setWins(long wins) {
        this.wins = wins;
    }

    public void setLosses(long losses) {
        this.losses = losses;
    }

    public void setDraws(long draws) {
        this.draws = draws;
    }

    public void setInterrupted(long interrupted) {
        this.interrupted = interrupted;
    }

    public void setTotalGames(long totalGames) {
        this.totalGames = totalGames;
    }
}
