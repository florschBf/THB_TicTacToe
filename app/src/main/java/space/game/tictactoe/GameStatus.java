package space.game.tictactoe;

/** eine Klasse um den Spielstatus zu definieren, nach " noch am spielen", Spielresultat, und zwecks der Gewinnreihe
 *
 */
public class GameStatus {
    private final boolean playing;
    private final GameResult result;
    private final int[] winningRow;

    public GameStatus(boolean playing, GameResult result, int[] winningRow) {
        this.playing = playing;
        this.result = result;
        this.winningRow = winningRow;
    }

    public enum GameResult {
        CROSS_WON, NOUGHT_WON, DRAW
    }

    public boolean isPlaying() {
        return playing;
    }

    public GameResult getResult() {
        return result;
    }

    public int[] getWinningRow() {
        return winningRow;
    }

    @Override
    public String toString() {
        return "GameStatus(playing = " + playing + ", result = " + result + ", winningRow = " + winningRow + ")";
    }
}
