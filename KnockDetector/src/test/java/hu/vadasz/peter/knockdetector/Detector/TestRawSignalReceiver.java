package hu.vadasz.peter.knockdetector.Detector;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Peti on 2018. 04. 30..
 */

public class TestRawSignalReceiver implements AudioRecorder.RawSignalReceiver {

    private List<Integer> signals;

    public TestRawSignalReceiver() {
        signals = new ArrayList<>();
    }

    public List<Integer> getSignals() {
        return signals;
    }

    @Override
    public void rawSignalReceived(int signal) {
        signals.add(signal);
    }
}
