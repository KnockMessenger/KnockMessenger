package hu.vadasz.peter.knockmessenger.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ValueEventListener;

import hu.vadasz.peter.knockdetector.Detector.AbstractAudioKnockDetector;
import hu.vadasz.peter.knockdetector.Detector.ComplexAudioKnockDetector;
import hu.vadasz.peter.knockmessenger.Adapters.MessageAdapter;
import hu.vadasz.peter.knockmessenger.Controllers.Exceptions.*;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.vadasz.peter.knockmessenger.Controllers.MessageDetectingController;
import hu.vadasz.peter.knockmessenger.Controllers.MessageSendingController;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.User;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.ServerDataChangeHandler;
import hu.vadasz.peter.knockmessenger.Managers.SharedPreferenceManager;
import hu.vadasz.peter.knockmessenger.Models.MessageSender;
import hu.vadasz.peter.knockmessenger.R;
import hu.vadasz.peter.knockmessenger.Tools.SongPlayer;
import hu.vadasz.peter.knockmessenger.Tools.VibratorEngine;
import hu.vadasz.peter.morsecodedecoder.Code.Code;

/**
 *  Activity class to send messages. This class is responsible for request permissions for microphone
 *  and external storage.
 */

public class MessageSendingActivity extends BaseActivityWithKnockDecoder implements MessageSender.MessageSendingVisualizer,
        MessageSendingController.MessageSendingControllerListener, ServerDataChangeHandler.FriendChangeListener,
        MessageAdapter.MessageAdapterListener, ServerDataChangeHandler.MessageReceivedListener {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final String EXTRA_FRIEND_TELEPHONE_KEY = "EXTRA_FRIEND_TELEPHONE_KEY";

    private static final boolean INPUT_MODE = true;
    public static final boolean START_DETECTING_ENABLED = true;

    /// CONSTANTS -- END

    /// VIEW ITEMS

    @BindView(R.id.KnockDetector_startButton)
    ImageButton knockDetectorStartButton;

    @BindView(R.id.Message_sendButton)
    ImageButton messageSendingButton;

    @BindView(R.id.messageSendingActivity_modeButton)
    ImageView modeIcon;

    @BindView(R.id.DetectedMessage_text)
    TextView detectedMessageText;

    @BindView(R.id.detectorIsListening_icon)
    ImageView detectorIsListeningIcon;

    @BindView(R.id.detected_Syllable_text)
    TextView detectedSyllableText;

    @BindView(R.id.messageSendingActivity_progressbar)
    ProgressBar timeToLive;

    @BindView(R.id.messageSendingActivity_sending_inProgress)
    ProgressBar sendingInProgress;

    @BindView(R.id.messageSendingActivity_messages)
    RecyclerView messagesRecyclerView;

    /// VIEW ITEMS -- END

    private MessageDetectingController messageDetectingController;
    private MessageSendingController messageSendingController;

    private SongPlayer shortBeepSongPlayer;
    private SongPlayer longBeepSongPlayer;

    private boolean vibratePreference;
    private boolean soundPreference;

    private boolean inputMode;

    private Friend friend;

    private MessageAdapter adapter;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_sending);

        keepDeviceAwake();

        ButterKnife.bind(this);

        int measureTime = sharedPreferenceManager.getInt(SharedPreferenceManager.SHORT_UNIT_TIME_PREFERENCE_KEY);
        if (measureTime == 0) {
            measureTime = SharedPreferenceManager.DEFAULT_MEASURE_TIME;
        }

        int micSensitivity = sharedPreferenceManager.getInt(SharedPreferenceManager.MIC_SENSITIVITY_PREFERENCE_KEY);
        if (micSensitivity == 0) {
            micSensitivity = SharedPreferenceManager.DEFAULT_MIC_SENSITIVITY;
        }

        messageDetectingController = new MessageDetectingController(this,
                measureTime,
                codeDataManager.getActualCodes(),
                sharedPreferenceManager.getBoolean(SharedPreferenceManager.MODE_PREFERENCE_KEY),
                micSensitivity);

        messageSendingController = new MessageSendingController(this, messageDataManager, this);

        shortBeepSongPlayer = new SongPlayer(this, R.raw.short_beep);
        longBeepSongPlayer = new SongPlayer(this, R.raw.long_beep);

        inputMode = INPUT_MODE;

        sendingInProgress.setVisibility(View.GONE);

        initOnClickListeners();
        initPreferenceVariables();

        Intent intent = getIntent();
        String telephone = intent.getStringExtra(EXTRA_FRIEND_TELEPHONE_KEY);
        friend = new Friend(userDataManager.getFriendByTelephone(telephone));
        setTitle(friend.getName());

        initRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        serverDataChangeHandler.addFriendDataChangeListener(this);
        serverDataChangeHandler.addMessageReceivedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        serverDataChangeHandler.removeFriendDataChangeListener(this);
        serverDataChangeHandler.removeMessageReceivedListener(this);
        messageDetectingController.stopDecoder();
        knockDetectorStartButton.setImageResource(R.mipmap.ic_mic_white_48dp);
    }

    @Override
    public void showErrorMessage(String message) {
        super.showErrorMessage(message);
        detectedSyllableText.setText(EMPTY_TEXT);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ACTIVITY OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method initializes the onClickListeners of the buttons on the activity's view.
     */

    private void initOnClickListeners() {
        knockDetectorStartButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!messageDetectingController.isDecoderRunning()) {
                    messageDetectingController.startDecoder();
                    knockDetectorStartButton.setImageResource(R.mipmap.ic_mic_off_white_48dp);
                    knockDetectorStartButton.setEnabled(!START_DETECTING_ENABLED);
                } else {
                    messageDetectingController.stopDecoder();
                    knockDetectorStartButton.setImageResource(R.mipmap.ic_mic_white_48dp);
                    timeToLive.setProgress(0);
                }
            }
        });

        messageSendingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                send();
            }
        });
    }

    /**
     * This method initializes the custom preference variables.
     */

    private void initPreferenceVariables() {
        vibratePreference = sharedPreferenceManager.getBoolean(SharedPreferenceManager.VIBRATION_PREFERENCE_KEY);
        soundPreference = sharedPreferenceManager.getBoolean(SharedPreferenceManager.SOUNDS_PREFERENCE_KEY);
    }

    private void initRecyclerView() {
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessageAdapter(messageDataManager.getMessages(), this);
        messagesRecyclerView.setAdapter(adapter);
        messagesRecyclerView.setHasFixedSize(RECYCLER_VIEW_HAS_FIXED_SIZE);
    }

    /**
     * This method shows the coming syllable from ComplexAudioKnockDetector.
     * @param syllable syllable to show
     */

    private void showSyllable(int syllable) {

        if (syllable == ComplexAudioKnockDetector.SHORT_SYLLABLE) {
            this.detectedSyllableText.setText(detectedSyllableText.getText() + "*");
            visualizeDetectedSyllable(syllable);
        } else {
            this.detectedSyllableText.setText(detectedSyllableText.getText() + "-");
            visualizeDetectedSyllable(syllable);
        }
    }

    /**
     * This method is responsible for sending the message.
     */

    private void send() {
        try {
            messageSendingController.send(userDataManager.getUser().getTelephone(), friend.getTel());
        } catch (DeviceIsOfflineException | EmptyMessageException e) {
            showErrorMessage(e.getMessage());
        }
    }

    private void visualizeDetectedSyllable(int syllable) {
        if (vibratePreference) {
            if (syllable == ComplexAudioKnockDetector.SHORT_SYLLABLE) {
                vibratorEngine.vibrate(VibratorEngine.SHORT_VIBRATION_TIME);
            } else {
                vibratorEngine.vibrate(VibratorEngine.LONG_VIBRATION_TIME);
            }
        }
    }

    private void clear() {
        detectedSyllableText.setText(EMPTY_TEXT);
    }

    private void changeModeIcon() {
        if (inputMode) {
            modeIcon.setImageResource(R.mipmap.ic_mode_edit_black_48dp);
        } else {
            modeIcon.setImageResource(R.mipmap.ic_format_textdirection_l_to_r_black_48dp);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// METHODS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// DecodedSignalReceiver OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void signalReceived(final Code symbol) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (symbol.getType() == Code.Type.BACK_SPACE_SYMBOL) {
                    try {
                        messageSendingController.delete();
                    } catch (CanNotDeleteCharacterException e) {
                        showErrorMessage(e.getMessage());
                    }
                } else if (symbol.getType() == Code.Type.HOME_SYMBOL) {
                    messageSendingController.home();
                } else if (symbol.getType() == Code.Type.END_SYMBOL) {
                    messageSendingController.end();
                } else if (symbol.getType() == Code.Type.CHANGE_MODE_SYMBOL) {
                    clear();
                    inputMode = !inputMode;
                    if (soundPreference) {
                        longBeepSongPlayer.playSong();
                    }
                    changeModeIcon();
                } else {
                    messageSendingController.add(symbol.getText());
                }
            }
        });
    }

    @Override
    public void error(final String message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                showErrorMessage(message);
                detectedSyllableText.setText(EMPTY_TEXT);
            }
        });
    }

    @Override
    public void timeToLive(final long maxTime, final long remainingTime) {
        int progressState = (int) ((remainingTime / (float) maxTime) * 100);
        timeToLive.setProgress(progressState);
    }

    @Override
    public void pause() {
        timeToLive.setProgress(0);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// DecodedSignalReceiver OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// DetectionVisualizer OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void syllableDetected(final int signal) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showSyllable(signal);
            }
        });
    }

    @Override
    public void pauseBetweenSyllables(final boolean isPause) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isPause == ComplexAudioKnockDetector.PAUSE_BETWEEN_SYLLABLES) {
                    detectorIsListeningIcon.setImageResource(R.mipmap.ic_report_problem_black_48dp);
                } else {
                    detectorIsListeningIcon.setImageResource(R.mipmap.ic_hearing_black_48dp);
                }
            }
        });
    }

    @Override
    public void detectorIsWaiting() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

            }
        });
    }

    @Override
    public void detectionFinished() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                detectedSyllableText.setText(EMPTY_TEXT);
            }
        });
    }

    @Override
    public void undefinedSyllable(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showErrorMessage(message);
            }
        });
    }

    @Override
    public void actualState(final int num) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (num == AbstractAudioKnockDetector.State.WAITING.ordinal()) {
                    detectorIsListeningIcon.setImageResource(R.mipmap.ic_hearing_black_48dp);
                } else if (num == AbstractAudioKnockDetector.State.FIRST_PHASE.ordinal()) {
                    detectorIsListeningIcon.setImageResource(R.mipmap.ic_fiber_manual_record_black_48dp);
                } else if (num == AbstractAudioKnockDetector.State.SECOND_PHASE.ordinal()) {
                    detectorIsListeningIcon.setImageResource(R.mipmap.ic_stop_black_48dp);
                } else {
                    detectorIsListeningIcon.setImageResource(R.mipmap.ic_drag_handle_black_48dp);
                }

                if (!knockDetectorStartButton.isEnabled()) {
                    knockDetectorStartButton.setEnabled(START_DETECTING_ENABLED);
                }
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// DetectionVisualizer OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// MessageSender.MessageSendingVisualizer INTERFACE OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void print(String message) {
        detectedMessageText.setText(message);
        clear();

        if (soundPreference) {
            shortBeepSongPlayer.playSong();
        }
    }

    @Override
    public void clearMessage() {
        detectedMessageText.setText(EMPTY_TEXT);
        detectedSyllableText.setText(EMPTY_TEXT);
    }

    @Override
    public void messageSendingInProgress() {
        sendingInProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void messageSent() {
        sendingInProgress.setVisibility(View.GONE);
        adapter.dataSetChanged();
    }

    @Override
    public void connectionTimeout() {
        sendingInProgress.setVisibility(View.GONE);
        showErrorMessage(getString(R.string.connection_timeout_error));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// MessageSender.MessageSendingVisualizer INTERFACE OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void sendingStarted() {
        sendingInProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void userNotExists() {
        sendingInProgress.setVisibility(View.GONE);
        showErrorMessage(getString(R.string.user_notExists));
    }

    @Override
    public void timeout() {
        sendingInProgress.setVisibility(View.GONE);
        showErrorMessage(getString(R.string.connection_timeout_error));
    }

    @Override
    public void requestUser(ValueEventListener listener) {
        userDataManager.findUser(friend, listener);
    }

    @Override
    public void friendChanged(Friend friend) {
        if (friend.equals(this.friend)) {
            this.friend.setName(friend.getName());
            setTitle(friend.getName());
        }
    }

    @Override
    public void friendAdded(Friend friend) {

    }

    @Override
    public void friendRemoved(Friend friend) {
        setTitle(friend.getName() + getString(R.string.messageSendingActivity_userLeft));
        //this.friend = null;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Friend getActualFriend() {
        return friend;
    }

    @Override
    public void messageReceived() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.dataSetChanged();
            }
        });

    }

    @Override
    public boolean isFriend(String tel) {
        return false;
    }

    @Override
    public String getUserTel() {
        return null;
    }

    @Override
    public User getUser() {
        return userDataManager.getUser();
    }

    @Override
    public void loading() {

    }

    @Override
    public void dataLoaded() {

    }

    @Override
    public void noMessages() {

    }
}
