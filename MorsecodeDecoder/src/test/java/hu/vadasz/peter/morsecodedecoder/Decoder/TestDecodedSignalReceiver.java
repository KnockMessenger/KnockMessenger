package hu.vadasz.peter.morsecodedecoder.Decoder;

import java.util.ArrayList;
import java.util.List;

import hu.vadasz.peter.morsecodedecoder.Code.Code;

/**
 * Created by Peti on 2018. 05. 06..
 */

public class TestDecodedSignalReceiver implements MorseCodeDecoder.DecodedSignalReceiver {

    public static final boolean ERROR_CALLED = true;
    public static final boolean TIME_IS_OVER = true;

    private List<Code> buffer;

    private boolean errorCalled;

    private boolean timeIsOver;

    public TestDecodedSignalReceiver() {
        buffer = new ArrayList<>();
    }

    @Override
    public void signalReceived(Code code) {
        buffer.add(code);
    }

    @Override
    public void error(String message) {
        errorCalled = ERROR_CALLED;
    }

    @Override
    public void timeToLive(long maxTime, long remainingTime) {}

    @Override
    public void pause() {
        timeIsOver = TIME_IS_OVER;
    }

    public List<Code> getBuffer() {
        return buffer;
    }

    public boolean isErrorCalled() {
        return errorCalled;
    }

    public boolean isTimeOver() {
        return timeIsOver;
    }
}
