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
 * Class to test ComplexAudioKnockDetector and some method from AbstractAudioKnockDetector. The detected
 * syllable can be tested by the actual state of the decoder. See more in origin class documentation.
 */

@RunWith(MockitoJUnitRunner.class)
public class ComplexAudioKnockDetectorTest {

    public static final int SHORT_UNIT_TIME = 100;
    public static final int MAX_TIME = 1000;
    public static final int PAUSE_TEST_TIME = 200;
    public static final int PAUSE_ERROR_INTERVAL = 50;
    public static final boolean OK = true;

    @Mock
    private AudioRecorder audioRecorder;

    @Mock
    private DetectionVisualizer visualizer;

    private AbstractAudioKnockDetector detector;

    @Before
    public void init() {
        detector = new ComplexAudioKnockDetector(visualizer, new ConcurrentLinkedQueue<Integer>(), SHORT_UNIT_TIME);
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
    public void testChangeState_StateShouldBeWaiting() {
        detector.start();
        detector.changeState(AudioRecorder.NONE);
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
    public void testChangeState_CounterShouldBeZero() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        assertEquals(KnockDetector.START_COUNTING, detector.getCounter());
    }

    @Test(timeout = MAX_TIME)
    public void testChangeState_StateShouldBeSecondPhase() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        for (int i = 0; i < KnockDetector.SHORT_SYLLABLE_LENGTH; ++i) {
            detector.changeState(AudioRecorder.NONE);
        }
        assertThat(detector.getDetectorState(), is(AbstractAudioKnockDetector.State.FIRST_PHASE));
    }

    @Test(timeout = MAX_TIME)
    public void testChangeState_CounterShouldBeLessOrEqualThenShortSyllableLength() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        for (int i = 0; i < KnockDetector.SHORT_SYLLABLE_LENGTH; ++i) {
            detector.changeState(AudioRecorder.NONE);
        }

        assertEquals(OK, detector.getCounter() <= KnockDetector.SHORT_SYLLABLE_LENGTH);
    }

    @Test(timeout = MAX_TIME)
    public void testChangeState_StateShouldBeFirstPhaseAfterShortSyllableDetected() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        detector.changeState(AudioRecorder.NONE);
        detector.changeState(AudioRecorder.KNOCK);
        assertThat(detector.getDetectorState(), is(AbstractAudioKnockDetector.State.FIRST_PHASE));
    }

    @Test(timeout = MAX_TIME)
    public void testChangeState_CounterShouldBeZeroAfterShortSyllableDetected() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        detector.changeState(AudioRecorder.NONE);
        detector.changeState(AudioRecorder.KNOCK);
        assertEquals(KnockDetector.START_COUNTING, detector.getCounter());
    }

    @Test(timeout = MAX_TIME)
    public void testChangeState_StateShouldBeSecondState() {
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
        assertEquals(OK, detector.getCounter() > AbstractAudioKnockDetector.SHORT_SYLLABLE_LENGTH &&
            detector.getCounter() <= detector.getLongSyllableLength());
    }

    @Test(timeout = MAX_TIME)
    public void testChangeState_StateShouldBeThirdPhase() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        detector.changeState(AudioRecorder.KNOCK);
        for (int i = 0; i <= detector.getLongSyllableLength(); ++i) {
            detector.changeState(AudioRecorder.NONE);
        }
        assertThat(detector.getDetectorState(), is(AbstractAudioKnockDetector.State.THIRD_PHASE));
    }

    @Test(timeout = MAX_TIME)
    public void testChangeState_CounterShouldBeLessOrEqualThanMaxLength() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        detector.changeState(AudioRecorder.KNOCK);
        for (int i = 0; i <= detector.getLongSyllableLength(); ++i) {
            detector.changeState(AudioRecorder.NONE);
        }
        assertEquals(OK, detector.getCounter() > detector.getLongSyllableLength() &&
            detector.getCounter() <= KnockDetector.MAX_LENGTH);
    }

    @Test(timeout = MAX_TIME)
    public void testChangeState_shortSyllableShouldBeDetected() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        detector.changeState(AudioRecorder.NONE);
        detector.changeState(AudioRecorder.KNOCK);
        assertThat(detector.getDetectorState(), is(AbstractAudioKnockDetector.State.FIRST_PHASE));
    }

    @Test
    public void testChangeState_longSyllableShouldBeDetected() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        for (int i = 0; i < detector.getLongSyllableLength(); ++i) {
            detector.changeState(AudioRecorder.NONE);
        }
        detector.changeState(AudioRecorder.KNOCK);
        assertThat(detector.getDetectorState(), is(AbstractAudioKnockDetector.State.FIRST_PHASE));
    }

    @Test
    public void testChangeState_gapShouldBeDetected() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        for (int i = 0; i < KnockDetector.MAX_LENGTH; ++i) {
            detector.changeState(AudioRecorder.NONE);
        }
        detector.changeState(AudioRecorder.KNOCK);
        assertThat(detector.getDetectorState(), is(AbstractAudioKnockDetector.State.FIRST_PHASE));
    }

    @Test(timeout = MAX_TIME)
    public void testChangeState_detectionShouldBeTerminated() {
        detector.start();
        for (int i = 0; i <= KnockDetector.MAX_LENGTH; ++i) {
            detector.changeState(AudioRecorder.NONE);
        }

        assertThat(detector.getDetectorState(), is(AbstractAudioKnockDetector.State.WAITING));
    }

    @Test(timeout = MAX_TIME)
    public void testAdministrateSyllableDetection_StateShouldBeFirstPhase() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        detector.changeState(AudioRecorder.NONE);
        detector.administrateSyllableDetection(KnockDetector.SHORT_SYLLABLE);
        assertThat(detector.getDetectorState(), is(AbstractAudioKnockDetector.State.FIRST_PHASE));
    }

    @Test(timeout = MAX_TIME)
    public void testAdministrateSyllableDetection_CounterShouldBeZero() {
        detector.start();
        detector.changeState(AudioRecorder.KNOCK);
        detector.changeState(AudioRecorder.NONE);
        detector.administrateSyllableDetection(KnockDetector.SHORT_SYLLABLE);
        assertEquals(KnockDetector.START_COUNTING, detector.getCounter());
    }

    @Test(timeout = PAUSE_TEST_TIME + PAUSE_ERROR_INTERVAL)
    public void testPause() {
        detector.pause(PAUSE_TEST_TIME);
    }
}