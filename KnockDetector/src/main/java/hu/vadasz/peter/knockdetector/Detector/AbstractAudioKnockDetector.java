package hu.vadasz.peter.knockdetector.Detector;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.inject.Inject;
import javax.inject.Singleton;

import hu.vadasz.peter.knockdetector.Exceptions.SignalReceiverNotFoundException;
import hu.vadasz.peter.knockdetector.Interfaces.DetectionVisualizer;
import hu.vadasz.peter.knockdetector.Interfaces.KnockDetector;
import lombok.Getter;
import lombok.Setter;

/**
 * This class is the super of the knock detectors which are base on audio detecting. The result of
 * the detection depends of the current state of the decoder. Before knocking the detector is in
 * WAITING state, after one knock the detector starts listening. The state changing is made by subclasses.
 */

public abstract class AbstractAudioKnockDetector extends KnockDetector implements AudioRecorder.RawSignalReceiver {

    public enum State {WAITING, FIRST_PHASE, SECOND_PHASE, THIRD_PHASE}

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Setter
    @Inject
    protected AudioRecorder audioRecorder;

    protected DetectionVisualizer visualizer;

    protected ConcurrentLinkedQueue<Integer> signalReceiverBuffer;

    protected int shortUnitTime;

    @Getter
    protected int longSyllableLength;

    protected boolean syllableDetecting;

    @Getter
    protected int counter;

    @Getter
    protected State detectorState;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public AbstractAudioKnockDetector() {
        mainComponent.inject(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ABSTRACT METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method changes the state of the detector based on time and detection mode.
     * @param rawSignal
     */

    protected abstract void changeState(int rawSignal);

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ABSTRACT METHODS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Adds the signal to the buffer, sends the signal to the visualizer and waits until the
     * end of the specified pause.
     * @param signal the arriving signal.
     */

    protected void administrateSyllableDetection(int signal) {
        sendDetectedSignal(signal);
        visualizer.syllableDetected(signal);
        detectorState = State.FIRST_PHASE;
        counter = START_COUNTING;
        pause(shortUnitTime);
    }

    /**
     * Sends the detected signal to the DetectedSignalReceiver (decoder) by pushing to it's buffer.
     * @param signal the detected signal.
     */

    protected void sendDetectedSignal(int signal) {
        signalReceiverBuffer.add(signal);
    }

    /**
     * Waits between two signal interval.
     * @param time the specified time to wait in millis.
     */

    protected void pause(int time) {
        try {
            visualizer.pauseBetweenSyllables(PAUSE_BETWEEN_SYLLABLES);
            Thread.sleep(time);
            visualizer.pauseBetweenSyllables(!PAUSE_BETWEEN_SYLLABLES);
        } catch (InterruptedException e) {}
    }

    public void setSensitivity(int sensitivity) {
        audioRecorder.setAmplitudeThreshold(sensitivity * AudioRecorder.SENSITIVITY_MULTIPLICATOR);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// AudioRecorder.RawSignalReceiver INTERFACE OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void rawSignalReceived(int signal) {
        changeState(signal);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// AudioRecorder.RawSignalReceiver INTERFACE OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
