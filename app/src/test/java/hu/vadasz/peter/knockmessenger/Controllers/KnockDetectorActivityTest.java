package hu.vadasz.peter.knockmessenger.Controllers;

import hu.vadasz.peter.knockmessenger.Activities.BaseActivityWithKnockDecoder;
import hu.vadasz.peter.morsecodedecoder.Code.Code;

public class KnockDetectorActivityTest extends BaseActivityWithKnockDecoder {
    @Override
    public void syllableDetected(int signal) {

    }

    @Override
    public void pauseBetweenSyllables(boolean isPause) {

    }

    @Override
    public void actualState(int num) {

    }

    @Override
    public void detectorIsWaiting() {

    }

    @Override
    public void detectionFinished() {

    }

    @Override
    public void undefinedSyllable(String message) {

    }

    @Override
    public void signalReceived(Code code) {

    }

    @Override
    public void error(String message) {

    }

    @Override
    public void timeToLive(long maxTime, long remainingTime) {

    }

    @Override
    public void pause() {

    }
}
