package hu.vadasz.peter.knockdetector.Detector;

import android.media.MediaRecorder;

/**
 * Created by Peti on 2018. 05. 01..
 */

public class TestMediaRecorder extends MediaRecorder {

    public static final int LOW_AMPLITUDE = 1000;
    public static final int HIGH_AMPLITUDE = 30000;

    int call = 0;

    @Override
    public int getMaxAmplitude() {
        if (call % 2 == 1) {
            call++;
            return HIGH_AMPLITUDE;
        }

        call++;
        return LOW_AMPLITUDE;
    }

}
