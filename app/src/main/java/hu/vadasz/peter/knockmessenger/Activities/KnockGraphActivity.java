package hu.vadasz.peter.knockmessenger.Activities;

import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ProgressBar;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.xml.sax.ext.DeclHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.vadasz.peter.knockdetector.Detector.AudioRecorder;
import hu.vadasz.peter.knockmessenger.Dialogs.MicrophoneSensitivityNumberPickerDialogPreference;
import hu.vadasz.peter.knockmessenger.Managers.SharedPreferenceManager;
import hu.vadasz.peter.knockmessenger.R;
import hu.vadasz.peter.knockmessenger.Tools.VibratorEngine;

/**
 * This class visualizes the strength of the knock with a graph based on the set values (short unit time, level).
 * The graph is animated.
 * Graph View: http://www.android-graphview.org
 */

public class KnockGraphActivity extends BaseActivity implements AudioRecorder.NoiseLevelReceiver {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final int SPACING = 20;
    public static final int PADDING = 100;
    public static final int LABEL_SPACE = 30;
    public static final int FONT_SIZE = 60;

    public static final int TIME_TO_END_BEFORE_NEXT_PERIOD = 60;

    public static final boolean X_AXIS_BOUNDS_MANUAL = true;
    public static final boolean Y_AXIS_BOUNDS_MANUAL = true;
    public static final boolean HORIZONTAL_LEVELS_VISIBILITY = true;

    public static final int MIN_X = 0;
    public static final int MAX_X = 2;
    public static final int X_POS = 1;
    public static final int MIN_Y = 0;

    public static final int LEVEL_ENOUGH_COLOR_RED = 42;
    public static final int LEVEL_ENOUGH_COLOR_GREEN = 177;
    public static final int LEVEL_ENOUGH_COLOR_BLUE = 85;
    public static final int LEVEL_NOT_ENOUGH_COLOR_RED = 124;
    public static final int LEVEL_NOT_ENOUGH_COLOR_GREEN = 10;
    public static final int LEVEL_NOT_ENOUGH_COLOR_BLUE = 10;
    public static final int ALPHA = 175;

    public static final int SHOW_PROGRESSBAR_TIME_UNDER_LIMIT = 300;

    /// CONSTANTS -- END

    @BindView(R.id.knockGraphActivity_graph)
    GraphView graph;

    @BindView(R.id.knockGraphActivity_progressBar)
    ProgressBar timeLeft;

    private DataPoint[] dataPoints;

    private CountDownTimer timer;

    private BarGraphSeries<DataPoint> series;

    private AudioRecorder audioRecorder;

    private Thread audioRecorderThread;

    private int limit;

    private float level;

    private int periodTime;

    private boolean vibratePreference;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knock_graph);
        ButterKnife.bind(this);

        limit = sharedPreferenceManager.getInt(SharedPreferenceManager.MIC_SENSITIVITY_PREFERENCE_KEY);
        periodTime = sharedPreferenceManager.getInt(SharedPreferenceManager.SHORT_UNIT_TIME_PREFERENCE_KEY);
        initAudioRecorder();
        initGraph();

        vibratePreference = sharedPreferenceManager.getBoolean(SharedPreferenceManager.VIBRATION_PREFERENCE_KEY);

        timer = new CountDownTimer(periodTime - TIME_TO_END_BEFORE_NEXT_PERIOD, 3) {
            @Override
            public void onTick(long millisUntilFinished) {
                interpol(millisUntilFinished);
                if (periodTime >= SHOW_PROGRESSBAR_TIME_UNDER_LIMIT) {
                    timeLeft.setProgress((int) (((float) millisUntilFinished / periodTime) * 100));
                }
            }

            @Override
            public void onFinish() {

            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        start();
    }

    @Override
    public void onPause() {
        super.onPause();
        stop();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method initializes the AudioRecorder with the chosen period time (short unit length).
     */

    private void initAudioRecorder() {
        audioRecorder = new AudioRecorder(this);
        audioRecorder.setMediaRecorder(new MediaRecorder());
        audioRecorder.setShortUnitLength(periodTime);
    }

    /**
     * This method initializes the graph and sets its style parameters.
     */

    private void initGraph() {
        dataPoints = new DataPoint[1];
        dataPoints[0] = new DataPoint(X_POS, MIN_Y);

        series = new BarGraphSeries<>(dataPoints);
        graph.addSeries(series);
        graph.getViewport().setXAxisBoundsManual(X_AXIS_BOUNDS_MANUAL);
        graph.getViewport().setYAxisBoundsManual(Y_AXIS_BOUNDS_MANUAL);
        graph.getViewport().setMinX(MIN_X);
        graph.getViewport().setMaxX(MAX_X);
        graph.getViewport().setMinY(MIN_Y);
        graph.getViewport().setMaxY(MicrophoneSensitivityNumberPickerDialogPreference.MAX_MIC_SENSITIVITY);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(!HORIZONTAL_LEVELS_VISIBILITY);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph.getGridLabelRenderer().setPadding(PADDING);
        graph.getGridLabelRenderer().setLabelsSpace(LABEL_SPACE);
        graph.getGridLabelRenderer().setTextSize(FONT_SIZE);

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {

            @Override
            public int get(DataPoint data) {
                return interpolateColor(data.getY());
            }
        });

        series.setSpacing(SPACING);
    }

    /**
     * This method starts the AudioRecorder in a new Thread.
     */

    private void start() {
        audioRecorderThread = new Thread(new Runnable() {

            @Override
            public void run() {
                audioRecorder.start();
            }
        });
        audioRecorderThread.start();
    }

    /**
     * This method stops the AudioRecorder and interrupts its thread.
     */

    private void stop() {
        audioRecorderThread.interrupt();
        audioRecorder.stop();
        timer.cancel();
    }

    /**
     * This method animates the graph using simple linear interpolation.
     * @param timeToEnd the time until the end of the animation (time to the next period).
     */

    private void interpol(long timeToEnd) {
        dataPoints[0] = new DataPoint(X_POS, ((float) timeToEnd / periodTime) * level);
        series.resetData(dataPoints);
    }

    private int interpolateColor(double level) {
        double rate = (level / limit);
        rate = rate > 1 ? 1.0 : rate;
        int red = (int)(((1 - rate) * LEVEL_NOT_ENOUGH_COLOR_RED) + rate * LEVEL_ENOUGH_COLOR_RED);
        int  green = (int)(rate * LEVEL_ENOUGH_COLOR_GREEN + (1 - rate) * LEVEL_NOT_ENOUGH_COLOR_GREEN);
        int blue = (int)((1- rate) * LEVEL_NOT_ENOUGH_COLOR_BLUE + rate * LEVEL_ENOUGH_COLOR_BLUE);

        return Color.argb(ALPHA, red, green, blue);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// AudioRecorder.NoiseLevelReceiver INTERFACE OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void noiseLevelReceived(final int actLevel) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                timer.cancel();
                dataPoints[0] = new DataPoint(X_POS, actLevel / AudioRecorder.SENSITIVITY_MULTIPLICATOR);
                KnockGraphActivity.this.level = actLevel / AudioRecorder.SENSITIVITY_MULTIPLICATOR;
                series.resetData(dataPoints);
                if (vibratePreference && actLevel / AudioRecorder.SENSITIVITY_MULTIPLICATOR >= limit) {
                    vibratorEngine.vibrate(VibratorEngine.SHORT_VIBRATION_TIME);
                }
                timer.start();
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// AudioRecorder.NoiseLevelReceiver INTERFACE OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
