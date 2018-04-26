package hu.vadasz.peter.knockmessenger.Tools;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * This class is responsible for playing noises / effects.
 */

public class SongPlayer {

    private  MediaPlayer mediaPlayer;

    public SongPlayer(Context context, int songResourceId) {
        mediaPlayer = MediaPlayer.create(context, songResourceId);
    }

    public void playSong() {
        mediaPlayer.start();
    }

    public void stop() {
        mediaPlayer.stop();
    }
}
