package hu.vadasz.peter.knockmessenger.Controllers;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import hu.vadasz.peter.morsecodedecoder.Code.Code;

import static org.junit.Assert.*;

public class MessageDetectingControllerTest {

    public static final boolean OK = true;
    public static final int MAX_TIME = 1000;

    private MessageDetectingController messageDetectingController;

    @Before
    public void init() {
        messageDetectingController = new MessageDetectingController(
                new KnockDetectorActivityTest()
                ,100
                , new ArrayList<Code>()
                , false
                , 100);
    }

    @Test(timeout = MAX_TIME)
    public void testStartDecoder_decoderShouldBeRunning() {
        messageDetectingController.startDecoder();
        boolean running = messageDetectingController.isDecoderRunning();
        messageDetectingController.stopDecoder();
        assertEquals(OK, running);
    }

    @Test(timeout = MAX_TIME)
    public void testStopDecoder_decoderShouldNotBeRunning() {
        messageDetectingController.startDecoder();
        messageDetectingController.stopDecoder();
        boolean running = messageDetectingController.isDecoderRunning();
        assertEquals(!OK, running);
    }
}