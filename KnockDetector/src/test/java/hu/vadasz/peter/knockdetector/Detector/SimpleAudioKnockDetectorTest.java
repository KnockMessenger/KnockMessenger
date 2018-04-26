package hu.vadasz.peter.knockdetector.Detector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.ConcurrentLinkedQueue;

import hu.vadasz.peter.knockdetector.Interfaces.DetectionVisualizer;
import hu.vadasz.peter.knockdetector.Interfaces.KnockDetector;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * This class it to test SimpleAudioKnockDetector's operations. The detected syllable can be tested
 * by the actual state of the decoder. See more in origin class documentation.
 */

@RunWith(MockitoJUnitRunner.class)
public class SimpleAudioKnockDetectorTest {

    public static final int SHORT_UNIT_TIME = 100;
    public static final int MAX_TIME = 1000;
    public static final boolean OK = true;

    @Mock
    private AudioRecorder audioRecorder;

    @Mock
    private DetectionVisualizer visualizer;

    private AbstractAudioKnockDetector detector;

    @Before
    public void init() {
        detector = new SimpleAudioKnockDetector(visualizer, new ConcurrentLinkedQueue<Integer>(), SHORT_UNIT_TIME);
        detector.setAudioRecorder(audioRecorder);
    }

    @Test(timeout = MAX_TIME)
    public void testStart_StateShouldBeWaiting() {
        detector.start();
        assertThat(detector.getDetectorState(), is(AbstractAudioKnockDetector.State.WAITING));
    }

    @Test(timeout = MAX_TIME)
    public void testStop_StateShouldBeWaiting() {
        detector.start();
        detector.stop();
        assertThat(detector.getDetectorState(), is(AbstractAudioKnockDetector.State.WAITING));
    }

    @Test(timeout = MAX_TIME)
    public void testChangeState_State_ShouldBeWaiting() {
        detector.start();
        detector.changeState(AudioRecorder.NONE);
        detector.changeState(AudioRecorder.NONE);
        assertThat(detector.getDetectorState(), is(AbstractAudioKnockDetector.State.WAITING));
    }

    @Test(timeout = MAX_TIME)
    public void testChangeState_StateShouldBeFirstPhase() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        assertThat(detector.getDetectorState(), is(AbstractAudioKnockDetector.State.FIRST_PHASE));
    }

    @Test(timeout = MAX_TIME)
    public void testChaneState_CounterShouldBeZero() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        assertEquals(KnockDetector.START_COUNTING, detector.getCounter());
    }

    @Test(timeout = MAX_TIME)
    public void testChangeState_ShortSyllableShouldBeDetected() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        for (int i = 0; i < KnockDetector.SHORT_SYLLABLE_LENGTH; ++i) {
            detector.changeState(AudioRecorder.NONE);
        }
        detector.changeState(AudioRecorder.KNOCK);
        assertThat(detector.getDetectorState(), is(AbstractAudioKnockDetector.State.FIRST_PHASE));
    }

    @Test(timeout = MAX_TIME)
    public void testChangeState_CounterShouldBeZeroAfterShortSyllableDetected() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        for (int i = 0; i < KnockDetector.SHORT_SYLLABLE_LENGTH; ++i) {
            detector.changeState(AudioRecorder.NONE);
        }
        detector.changeState(AudioRecorder.KNOCK);
        assertEquals(KnockDetector.START_COUNTING, detector.getCounter());
    }

    @Test(timeout = MAX_TIME)
    public void testChangeState_StateShouldBeSecondPhase() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        for (int i = 0; i <= KnockDetector.SHORT_SYLLABLE_LENGTH; ++i) {
            detector.changeState(AudioRecorder.NONE);
        }
        assertThat(detector.getDetectorState(), is(AbstractAudioKnockDetector.State.SECOND_PHASE));
    }

    @Test(timeout = MAX_TIME)
    public void testChangeState_CounterShouldBeLessOrEqualThanLongSyllableLength() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        for (int i = 0; i <= KnockDetector.SHORT_SYLLABLE_LENGTH; ++i) {
            detector.changeState(AudioRecorder.NONE);
        }
        assertEquals(OK, detector.getCounter() > KnockDetector.SHORT_SYLLABLE_LENGTH &&
            detector.getCounter() <= detector.getLongSyllableLength());
    }

    @Test(timeout = MAX_TIME)
    public void testChangeState_LongSyllableShouldBeDetected() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        for (int i = 0; i < detector.getLongSyllableLength(); ++i) {
            detector.changeState(AudioRecorder.NONE);
        }
        detector.changeState(AudioRecorder.KNOCK);
        assertThat(detector.getDetectorState(), is(AbstractAudioKnockDetector.State.FIRST_PHASE));
    }

    @Test(timeout = MAX_TIME)
    public void testChangeState_CounterShouldBeZeroAfterLongSyllableDetected() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        for (int i = 0; i < detector.getLongSyllableLength(); ++i) {
            detector.changeState(AudioRecorder.NONE);
        }
        detector.changeState(AudioRecorder.KNOCK);
        assertEquals(KnockDetector.START_COUNTING, detector.getCounter());
    }

    @Test(timeout = MAX_TIME)
    public void testChaneState_DetectionShouldBeTerminated() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        for (int i = 0; i <= KnockDetector.MAX_LENGTH; ++i) {
            detector.changeState(AudioRecorder.NONE);
        }
        assertThat(detector.getDetectorState(), is(AbstractAudioKnockDetector.State.WAITING));
    }
}