package hu.vadasz.peter.knockdetector.Detector;

import android.media.MediaRecorder;
import android.util.Log;

import javax.inject.Inject;

import hu.vadasz.peter.knockdetector.Detector.Utils.MediaRecorderInitializer;
import hu.vadasz.peter.knockdetector.Exceptions.AudioRecorderInitException;
import hu.vadasz.peter.knockdetector.Interfaces.KnockDetector;
import lombok.Getter;
import lombok.Setter;

/**
 * This class receives information from the microphone through Android's MediaRecorder.
 * The strength of the recorded noise should reach the given lower bound to detect as knock.
 * Specified signal interval is used to detect knocks.
 * External storage is necessary for recording.
 */

public class AudioRecorder {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// INTERFACES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This interface is implemented by the detector class, which transforms raw signals into byte
     * series.
     */

    public interface RawSignalReceiver {

        /**
         *
         * @param signal the signal coming from Android's mediarecorder 1 if knock detected.
         */

        void rawSignalReceived(int signal);
    }

    /**
     * By using this interface the noise levels of each intervals can be received.
     */

    public interface NoiseLevelReceiver {

        void noiseLevelReceived(int level);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// INTERFACES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final int DEFAULT_MEASURE_TIME = 500;
    public static final int DEFAULT_AMPLITUDE_DIFF_LOWER_BOUND = 14500;
    public static final int SENSITIVITY_MULTIPLICATOR = 3500;

    public static final String TMP_FILE_NAME = "/KnockMessenger_tmp.3gp";
    public static final String AUDIORECORDER_INIT_ERROR = "Detector module can not be initialized!";

    private static final boolean IS_RECORDING = true;

    public static final int KNOCK = 1;
    public static final int NONE = 0;

    /// CONSTANTS -- END

    @Getter
    @Setter
    private int amplitudeThreshold;

    @Setter
    @Getter
    private RawSignalReceiver rawSignalReceiver;

    @Setter
    private NoiseLevelReceiver noiseLevelReceiver;

    @Inject
    @Setter
    protected MediaRecorder mediaRecorder;

    @Getter
    private String tmpAudioFile;

    @Setter
    @Getter
    private long shortUnitLength;

    @Getter
    private boolean recording;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public AudioRecorder(NoiseLevelReceiver noiseLevelReceiver) {
        this.tmpAudioFile = TMP_FILE_NAME;
        this.noiseLevelReceiver = noiseLevelReceiver;
    }

    public AudioRecorder() {
        KnockDetector.getInstance().getMainComponent().inject(this);
        this.shortUnitLength = DEFAULT_MEASURE_TIME;
        this.tmpAudioFile = TMP_FILE_NAME;
        this.amplitudeThreshold = DEFAULT_AMPLITUDE_DIFF_LOWER_BOUND;
    }

    public AudioRecorder(RawSignalReceiver rawSignalReceiver) {
        KnockDetector.getInstance().getMainComponent().inject(this);
        this.rawSignalReceiver = rawSignalReceiver;
        this.shortUnitLength = DEFAULT_MEASURE_TIME;
        this.tmpAudioFile = TMP_FILE_NAME;
        this.amplitudeThreshold = DEFAULT_AMPLITUDE_DIFF_LOWER_BOUND;
    }

    public AudioRecorder(RawSignalReceiver rawSignalReceiver, int shortUnitLength, int theresold) {
        KnockDetector.getInstance().getMainComponent().inject(this);
        this.rawSignalReceiver = rawSignalReceiver;
        this.shortUnitLength = shortUnitLength;
        this.tmpAudioFile = TMP_FILE_NAME;
        this.amplitudeThreshold = theresold;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Starts the recording.
     * @throws AudioRecorderInitException
     */

    public void start()  {
        MediaRecorderInitializer.initializeRecorder(mediaRecorder, tmpAudioFile);
        recordKnock();
    }

    /**
     * Stops  the recording.
     */

    public void stop() {
        recording = !IS_RECORDING;
    }

    /**
     * This method initializes and starts the MediaRecorder. This method should run in separate
     * thread. The main cycle of the method is running until the stop method is called.
     * @throws AudioRecorderInitException
     */

    private void recordKnock() {
        recording = IS_RECORDING;

        mediaRecorder.start();

        do {
            int amplitudeIntervalStart = mediaRecorder.getMaxAmplitude();
            waitIntervalLength();

            int amplitudeIntervalEnd = mediaRecorder.getMaxAmplitude();
            Log.i("AUDIO_RECORD", Integer.toString(amplitudeIntervalEnd));

            if (rawSignalReceiver != null && amplitudeIntervalEnd - amplitudeIntervalStart >= amplitudeThreshold) {
                rawSignalReceiver.rawSignalReceived(KNOCK);
            } else if (rawSignalReceiver != null) {
                rawSignalReceiver.rawSignalReceived(NONE);
            }

            if (noiseLevelReceiver != null) {
                noiseLevelReceiver.noiseLevelReceived(Math.abs(amplitudeIntervalEnd - amplitudeIntervalStart));
            }

        } while (recording);

        finish();
    }

    /**
     * Wait until the end of the signal interval.
     */

    private void waitIntervalLength() {
        try {
            Thread.sleep(shortUnitLength);
        } catch (InterruptedException e) {}
    }

    /**
     * Closes the MediaRecorder, stops the recording.
     */

    public void finish() {
        if (mediaRecorder != null)
        {
            mediaRecorder.stop();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
