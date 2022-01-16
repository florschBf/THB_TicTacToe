package space.game.tictactoe.models;

import android.media.MediaPlayer;

/**
 * Klasse um bestimmte sounds während Spiel abspielern zu können
 *
 * @author in for help
 */
public class Sound {

    /**
     * Methode zum Abspielen bestimmtes Sounds
     * Method for playing specific sounds
     *
     * @param sound - Sound, der eingespielt werden soll
     */
    public static void soundPlay(MediaPlayer sound) {
        if(Player.getPlayer().getIsTonOn()) {
            sound.start();
        }
    }

    /**
     * Methode, um den Ton zu stoppen
     * Method to stop the sound
     * @param sound of the MediaPlayer to stop when is playing
     */
    public static void soundStop(MediaPlayer sound) {
        if(!sound.isPlaying()) {
            sound.release();
            sound = null;
        }
    }
}
