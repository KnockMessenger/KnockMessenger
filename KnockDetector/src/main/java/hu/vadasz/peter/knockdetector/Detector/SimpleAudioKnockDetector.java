package hu.vadasz.peter.knockdetector.Detector;

import java.util.concurrent.ConcurrentLinkedQueue;

import hu.vadasz.peter.knockdetector.Interfaces.DetectionVisualizer;

/**
 * This class is based on Morse-coding and prefix (Huffman) coding. This class detects the rythm of the raw signal coming
 * from AudioRecorder and transforms to byte series, which represents short, long syllables.
 * The detector can have three states, depending on the time between two knocks.
 * Depending on time between two knocks the detector changes its state. In FIRST_PHASE the detector
 * detect short syllables, in SECOND_PHASE long syllables can be detected. If there was knock until the
 * and of the SECOND_PHASE the detection terminates.
 */

public class SimpleAudioKnockDetector extends AbstractAudioKnockDetector {

    public SimpleAudioKnockDetector(DetectionVisualizer visualizer, ConcurrentLinkedQueue<Integer> signalReceiverBuffer,
                                    int shortUnitTime) {
        audioRecorder.setRawSignalReceiver(this);
        audioRecorder.setShortUnitLength(shortUnitTime);
        audioRecorder.setAmplitudeThreshold(AudioRecorder.DEFAULT_AMPLITUDE_DIFF_LOWER_BOUND);
        this.visualizer = visualizer;
        this.signalReceiverBuffer = signalReceiverBuffer;
        this.shortUnitTime = shortUnitTime;
        longSyllableLength = SHORT_SYLLABLE_LENGTH * LONG_SYLLABLE_MULTIPLICATOR;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// AbstractAudioKnockDetector OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method changes the state of the detector based on time.
     * @param rawSignal
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
                        detectorState = State.WAITING;
                    }
                } else {
                    administrateSyllableDetection(LONG_SYLLABLE);
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

    @Override
    public void start() {
        detectorState = State.WAITING;
        audioRecorder.start();
    }

    @Override
    public void stop() {
        audioRecorder.stop();
        visualizer.detectionFinished();
        detectorState = State.WAITING;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// KnockDetector OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
