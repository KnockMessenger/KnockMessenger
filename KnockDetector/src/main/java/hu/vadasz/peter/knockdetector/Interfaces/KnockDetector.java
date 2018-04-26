package hu.vadasz.peter.knockdetector.Interfaces;

import hu.vadasz.peter.knockdetector.Components.DaggerMainComponent;
import hu.vadasz.peter.knockdetector.Components.MainComponent;
import hu.vadasz.peter.knockdetector.Exceptions.UnrecognizedSyllableException;
import hu.vadasz.peter.knockdetector.Modules.AudioRecorderModule;
import hu.vadasz.peter.knockdetector.Modules.MediaRecorderModule;
import lombok.Getter;

/**
 * This interface provides interface for knock detection. Every detector class should be a subclass
 * of this class.
 */

public abstract class KnockDetector {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final int SHORT_SYLLABLE = 0;
    public static final int LONG_SYLLABLE = 1;
    public static final int GAP = 2;
    public static final int SPACE = 3;
    public static final int START_COUNTING = 0;

    public static final int LONG_SYLLABLE_MULTIPLICATOR = 3;
    public static final int GAP_MULTIPLICATOR = 3;

    public static final int SHORT_SYLLABLE_LENGTH = 1;
    public static final int MAX_LENGTH = 7;

    public static final boolean PAUSE_BETWEEN_SYLLABLES = true;

    public static final String SIGNAL_RECEIVER_NOT_FOUND_ERROR_MESSAGE = "Internal error: signal receiver not found!";

    protected static final boolean DETECTOR_IS_DETECTING = true;

    /// CONSTANTS -- END

    private static KnockDetector instance;

    public static KnockDetector getInstance() {
        return instance;
    }

    @Getter
    protected MainComponent mainComponent;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public KnockDetector() {
        instance = this;

        mainComponent = DaggerMainComponent.builder()
                .mediaRecorderModule(new MediaRecorderModule())
                .audioRecorderModule(new AudioRecorderModule())
                .build();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public abstract void start();
    public abstract void stop();

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ABSTRACT METHODS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method operates in an other way, and represents experiments.
     * Detects a short or a long syllable, depends on the time between two knocks.
     * @param rawSignal signal from audio recorder.
     * @throws UnrecognizedSyllableException
     */

    @Deprecated
    protected void detectSyllable(int rawSignal) throws UnrecognizedSyllableException {

        /*if (detectorState != State.SYLLABLE_DETECTING && rawSignal == AudioRecorder.KNOCK) {
            detectorState = State.SYLLABLE_DETECTING;
            counter = 0;
        } else if (detectorState == State.SYLLABLE_DETECTING && counter <= longSyllableLength && rawSignal == AudioRecorder.NONE) {
            counter++;
        } else if (detectorState == State.SYLLABLE_DETECTING && rawSignal == AudioRecorder.KNOCK) {
            if (counter >= 0 && counter <= SHORT_SYLLABLE_LENGTH) {
                administrateSyllableDetection(SHORT_SYLLABLE);
            } else if (counter > SHORT_SYLLABLE_LENGTH && counter <= longSyllableLength) {
                administrateSyllableDetection(LONG_SYLLABLE);
            }
        } else if (syllableDetecting && counter > longSyllableLength) {
            detectorState = State.WAITING;
            throw new UnrecognizedSyllableException(UNRECOGNIZED_SYLLABLE_ERROR);
        }
        if (syllableDetecting) {
            visualizer.actualState(counter);
        }*/
    }

}
