package space.game.tictactoe.models;

import android.media.MediaPlayer;

public class Sound {

    /**
     * Methode zum Abspielen bestimmtes Sounds
     * @param sound - Sound, der eingespielt werden soll
     */
    public static void soundPlay(MediaPlayer sound) {
        if(Player.getPlayer().getIsTonOn()) {
            sound.start();
        }
    }

    /**
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
