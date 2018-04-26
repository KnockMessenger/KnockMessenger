package hu.vadasz.peter.knockdetector.Components;

import javax.inject.Singleton;

import dagger.Component;
import hu.vadasz.peter.knockdetector.Detector.AbstractAudioKnockDetector;
import hu.vadasz.peter.knockdetector.Detector.AudioRecorder;
import hu.vadasz.peter.knockdetector.Modules.AudioRecorderModule;
import hu.vadasz.peter.knockdetector.Modules.MediaRecorderModule;

/**
 * This interface is implemented by Dagger's main component, and indicates in which class can be
 * dependencies injected.
 */

@Singleton
@Component (modules = {MediaRecorderModule.class, AudioRecorderModule.class})
public interface MainComponent {

    void inject(AudioRecorder audioRecorder);
    void inject(AbstractAudioKnockDetector abstractAudioKnockDetector);
}
