package hu.vadasz.peter.knockdetector.Detector;

import java.util.concurrent.ConcurrentLinkedQueue;

import hu.vadasz.peter.knockdetector.Interfaces.DetectionVisualizer;

/**
 * This class is based on Morse-coding methods. This class detects the rythm of the raw signal coming
 * from AudioRecorder and transforms to byte series, which represents short, long syllables, gaps
 * and spaces. The detector can have for states, depending on the time between two knocks.
 * Depending on time between two knocks the detector changes its state. In FIRST_PHASE the
 * detector detect short syllables, in SECOND_PHASE long syllables, in THIRD_PHASE gaps and spaces
 * can be detected. At the end of the detection the detector is also in WAITING state. If there was
 * no knock until the end of the THIRD_PHASE space is detected and the detection terminates.
 */

public class ComplexAudioKnockDetector extends AbstractAudioKnockDetector {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public ComplexAudioKnockDetector(DetectionVisualizer visualizer, ConcurrentLinkedQueue<Integer> signalReceiverBuffer, int shortUnitTime) {
        audioRecorder.setRawSignalReceiver(this);
        audioRecorder.setShortUnitLength(shortUnitTime);
        audioRecorder.setAmplitudeThreshold(AudioRecorder.DEFAULT_AMPLITUDE_DIFF_LOWER_BOUND);
        this.visualizer = visualizer;
        this.shortUnitTime = shortUnitTime;
        longSyllableLength = SHORT_SYLLABLE_LENGTH * LONG_SYLLABLE_MULTIPLICATOR;
        this.signalReceiverBuffer = signalReceiverBuffer;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method is called at the at of the detection.
     */

    private void terminateDetection() {
        sendDetectedSignal(SPACE);
        detectorState = State.WAITING;
        counter = START_COUNTING;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// AbstractAudioKnockDetector OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method changes the state of the detector based on the current state and time.
     * @param rawSignal signal coming from audio recorder, 1 = Knock, 0 = None
     */

    @Override
    protected void changeState(int rawSignal) {
        switch (detectorState) {
            case WAITING:
                if (rawSignal == AudioRecorder.KNOCK) {
                    detectorState = State.FIRST_PHASE;
                    counter = START_COUNTING;
                }
                break;
            case FIRST_PHASE:
                if (rawSignal == AudioRecorder.NONE) {
                    counter++;
                    if (counter > SHORT_SYLLABLE_LENGTH) {
                        detectorState = State.SECOND_PHASE;
                    }
                } else {
                    administrateSyllableDetection(SHORT_SYLLABLE);
                }
                break;
            case SECOND_PHASE:
                if (rawSignal == AudioRecorder.NONE) {
                    counter++;
                    if (counter > longSyllableLength) {
                        detectorState = State.THIRD_PHASE;
                    }
                } else {
                    administrateSyllableDetection(LONG_SYLLABLE);
                }
                break;
            case THIRD_PHASE:
                if (rawSignal == AudioRecorder.NONE) {
                    counter++;
                    if (counter >= MAX_LENGTH) {
                        detectorState = State.WAITING;
                        terminateDetection();
                    }
                } else {
                    if (counter < MAX_LENGTH) {
                        administrateSyllableDetection(GAP);
                    }
                }
                break;
        }
        visualizer.actualState(detectorState.ordinal());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// AbstractAudioKnockDetector OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// KnockDetector OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method starts the AudioRecorder which sends raw signals from microphone.
     */

    @Override
    public void start() {
        visualizer.detectorIsWaiting();
        detectorState = State.WAITING;
        audioRecorder.start();
    }

    /**
     * This method stops the AudioRecorder.
     */

    @Override
    public void stop() {
        syllableDetecting = !DETECTOR_IS_DETECTING;
        audioRecorder.stop();
        visualizer.detectionFinished();
        detectorState = State.WAITING;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// KnockDetector OVERRIDES-- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
