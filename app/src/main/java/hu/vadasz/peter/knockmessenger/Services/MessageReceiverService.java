package hu.vadasz.peter.knockmessenger.Services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import hu.vadasz.peter.knockmessenger.Application.BaseApplication;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Message;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.MessageDataManager;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.UserDataManager;
import hu.vadasz.peter.knockmessenger.DataPersister.Server.ServerDataChangeHandler;
import hu.vadasz.peter.knockmessenger.Managers.NotificationManager;
import hu.vadasz.peter.knockmessenger.R;
import hu.vadasz.peter.knockmessenger.Tools.VibratorEngine;

/**
 * This class is responsible for the message receiving in the background.
 */

public class MessageReceiverService extends IntentService implements ValueEventListener {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    public static final String WORKER_THREAD_NAME = "km_message_receiver";
    public static final boolean RUNNING = true;
    public static final int SERVICE_SLEEP_TIME = 2000;

    /// CONSTANTS -- END

    @Inject
    protected MessageDataManager messageDataManager;

    @Inject
    protected UserDataManager userDataManager;

    @Inject
    protected NotificationManager notificationManager;

    @Inject
    protected ServerDataChangeHandler serverDataChangeHandler;

    private boolean running;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public MessageReceiverService() {
        super(WORKER_THREAD_NAME);
        BaseApplication.getInstance().getmMainComponent().inject(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONSTRUCTION -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * This method decides which messages are new.
     * @param messages the messages coming from the server.
     */

    private void handleMessages(ArrayList<Message> messages) {
        List<Message> cachedMessages = new ArrayList<>(messageDataManager.getMessages());
        int numOfNewMessages = 0;
        for (Message message : messages) {
            if (!cachedMessages.contains(message)) {
                numOfNewMessages++;
                messageDataManager.newMessage(message);
                Friend from = userDataManager.getFriendByTelephone(message.getFromTelephone());
                notificationManager.createMessageNotification(from == null ? message.getFromTelephone() : from.getName(),
                        message.getMessage(), message.getFromTelephone());
            }
        }
        if (numOfNewMessages > 1) {
            notificationManager.createMoreMessageNotification();
        }
        serverDataChangeHandler.messageReceived();
    }

    /**
     * This method provides pause between querying messages from the server.
     */

    private void pause() {
        try {
            Thread.sleep(SERVICE_SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// CONTENT UTILS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// IntentService OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("SERVICE", "STARTED");
        running = RUNNING;
        while (running) {
            pause();
            if (userDataManager.getUser() != null) {
                messageDataManager.requestAllMessages(userDataManager.getUser().getTelephone(), MessageReceiverService.this);
            }
            Log.i("SERVICE", "REFRESH REQUEST");
        }

        //sendBroadcast(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        running = !RUNNING;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// IntentService OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ValueEventListener OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            ArrayList<Message> messages = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                messages.add(snapshot.getValue(Message.class));
            }
            handleMessages(messages);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {}

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// ValueEventListener OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
