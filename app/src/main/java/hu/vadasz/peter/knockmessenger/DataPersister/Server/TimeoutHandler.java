package hu.vadasz.peter.knockmessenger.DataPersister.Server;

import android.os.CountDownTimer;

/**
 * Created by Peti on 2018. 04. 16..
 */

public class TimeoutHandler {

    public interface TimeoutListener {
        void timeout();
    }

    public static final int DEFAULT_SHORT_TIMEOUT = 10000;
    public static final int DEFAULT_MEDIUM_TIMEOUT = 30000;
    public static final int DEFAULT_LONG_TIME_OUT = 60000;
    public static final int DEFAULT_EXTRA_LONG_TIMEOUT = 90000;
    public static final int TICK_TIME = 1000;

    private CountDownTimer timer;

    private int timeout;

    private TimeoutListener listener;

    public TimeoutHandler(int timeout, final TimeoutListener listener) {
        this.timeout = timeout;
        this.listener = listener;

        timer = new CountDownTimer(timeout, TICK_TIME) {

            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                listener.timeout();
            }
        };
    }

    public TimeoutHandler(int timeout, int tickTime, final TimeoutListener listener) {
        this.timeout = timeout;
        this.listener = listener;

        timer = new CountDownTimer(timeout, tickTime) {

            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                listener.timeout();
            }
        };
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.cancel();
    }
}
