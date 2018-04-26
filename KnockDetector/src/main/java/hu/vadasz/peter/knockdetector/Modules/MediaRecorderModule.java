package hu.vadasz.peter.knockdetector.Modules;

import android.media.MediaRecorder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * This class provides singleton MediaRecorder from Android.
 */

@Module
public class MediaRecorderModule {

    @Provides
    @Singleton
    public MediaRecorder provideMediaRecorder() {
        return new MediaRecorder();
    }
}
