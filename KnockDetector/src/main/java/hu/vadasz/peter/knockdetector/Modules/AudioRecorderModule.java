package hu.vadasz.peter.knockdetector.Modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hu.vadasz.peter.knockdetector.Detector.AudioRecorder;

/**
 * This class provides singleton AudioRecorder.
 */

@Module
public class AudioRecorderModule {

    @Provides
    @Singleton
    public AudioRecorder provideAudioRecorder() {
        return new AudioRecorder();
    }
}
