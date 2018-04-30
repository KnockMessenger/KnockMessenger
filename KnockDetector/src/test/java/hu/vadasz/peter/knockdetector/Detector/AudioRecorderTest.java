package hu.vadasz.peter.knockdetector.Detector;

import android.media.MediaRecorder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by Peti on 2018. 04. 30..
 */

@RunWith(MockitoJUnitRunner.class)
public class AudioRecorderTest {

    public static final int MAX_TIME = 2000;
    public static final int NOISE_LEVEL = 500;
    public static final int MEASURE_TIME = 10;

    public static final boolean OK = true;

    private AudioRecorder.RawSignalReceiver receiver;

    private AudioRecorder audioRecorder;

    @Mock
    private MediaRecorder mediaRecorder;

    @Test(timeout = MAX_TIME)
    public void testAudioRecorder_ShouldReturnWithNoKnocks() {
        when(mediaRecorder.getMaxAmplitude()).thenReturn(NOISE_LEVEL);
        receiver = new TestRawSignalReceiver();
        audioRecorder = new AudioRecorder(mediaRecorder, receiver);
        audioRecorder.setShortUnitLength(MEASURE_TIME);

        new Thread(new Runnable() {
            @Override
            public void run() {
                audioRecorder.start();
            }
        }).start();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        audioRecorder.stop();
        assertEquals(!OK, ((TestRawSignalReceiver) receiver).getSignals().contains(AudioRecorder.KNOCK));
    }

    @Test(timeout = MAX_TIME)
    public void testAudioRecorder_ShouldReturnWithKnocks() {
        MediaRecorder mediaRecorder = new TestMediaRecorder();
        receiver = new TestRawSignalReceiver();
        audioRecorder = new AudioRecorder(mediaRecorder, receiver);
        audioRecorder.setShortUnitLength(MEASURE_TIME);

        new Thread(new Runnable() {
            @Override
            public void run() {
                audioRecorder.start();
            }
        }).start();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        audioRecorder.stop();
        assertEquals(!OK, ((TestRawSignalReceiver) receiver).getSignals().contains(AudioRecorder.NONE));
    }

}