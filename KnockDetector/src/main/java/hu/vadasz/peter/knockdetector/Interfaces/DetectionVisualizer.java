package hu.vadasz.peter.knockdetector.Interfaces;

/**
 * This interface should be implemented by the activity which uses the detector to show
 * when is detector ready for detecting knocking and show the detected syllable.
 */

public interface DetectionVisualizer {

    /**
     * By implementing this method the activity can show the detected signal.
     * @param signal from Detector
     */

    void syllableDetected(int signal);

    /**
     * By implementing this method it can be shown when there is pause between 2 syllables.
     */

    void pauseBetweenSyllables(boolean isPause);

    /**
     * By implementing this method the state of the detector can be shown.
     * @param num
     */

    void actualState(int num);

    /**
     * Shows if detector is ready for listening.
     */

    void detectorIsWaiting();

    /**
     * This method is called by the detector when the detection ended.
     */

    void detectionFinished();

    /**
     * This method is called if there is an undefined signal.
     */

    void undefinedSyllable(String message);
}
