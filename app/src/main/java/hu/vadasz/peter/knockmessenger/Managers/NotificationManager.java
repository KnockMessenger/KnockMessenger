package hu.vadasz.peter.knockmessenger.Managers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import javax.inject.Inject;

import hu.vadasz.peter.knockmessenger.Activities.FriendsActivity;
import hu.vadasz.peter.knockmessenger.Activities.MainScreenActivity;
import hu.vadasz.peter.knockmessenger.Activities.MessageSendingActivity;
import hu.vadasz.peter.knockmessenger.Application.BaseApplication;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Friend;
import hu.vadasz.peter.knockmessenger.DataPersister.Entities.Message;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.MessageDataManager;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.UserDataManager;
import hu.vadasz.peter.knockmessenger.R;
import hu.vadasz.peter.knockmessenger.Tools.SongPlayer;
import hu.vadasz.peter.knockmessenger.Tools.VibratorEngine;

/**
 * Created by Peti on 2018. 04. 21..
 */

public class NotificationManager {

    public static final String CHANNEL_ID = "1";
    public static final int NOTIFICATION_NOT_FOUND = -1;

    public static final int MESSAGE_NOTIFICATION_ID = 1;
    public static final int MORE_MESSAGE_NOTIFICATION = 2;
    public static final int SYSTEM_NOTIFICATION_ID = 3;

    public static boolean NOTIFICATION_WITH_MEDIA = true;

    private Context context;

    @Inject
    protected MessageDataManager messageDataManager;

    @Inject
    protected UserDataManager userDataManager;

    @Inject
    protected VibratorEngine vibratorEngine;

    public NotificationManager(Context context) {
        this.context = context;
        BaseApplication.getInstance().getmMainComponent().inject(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// NOTIFICATION OPERATIONS
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = context.getString(R.string.notificationChannel_id);
            String description = context.getString(R.string.notificationChannel_description);
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, android.app.NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            //notificationManager.createNotificationChannel(channel);
        }
    }

    public void createNotification(String title, String message, int id, boolean media, String from) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message);

        Intent notificationIntent = null;
        if (from == null) {
            notificationIntent = new Intent(context, MainScreenActivity.class);
        } else {
            if (userDataManager.isFriend(from)) {
                notificationIntent = new Intent(context, MessageSendingActivity.class);
                notificationIntent.putExtra(MessageSendingActivity.EXTRA_FRIEND_TELEPHONE_KEY, from);
            } else {
                notificationIntent = new Intent(context, MainScreenActivity.class);
            }
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(notificationIntent);

        PendingIntent notPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(notPendingIntent);

        createNotificationChannel();

        showNotification(notificationBuilder.build(), id, media);
    }

    public void createMessageNotification(String from, String message, String fromTel) {
        createNotification(from + " - " + context.getString(R.string.notificationTitle), message, MESSAGE_NOTIFICATION_ID,
                NOTIFICATION_WITH_MEDIA, fromTel);
    }

    public void createMoreMessageNotification() {
        createNotification(context.getString(R.string.notificationTitle), context.getString(R.string.moreMessage_notification_title)
                , MORE_MESSAGE_NOTIFICATION, NOTIFICATION_WITH_MEDIA, null);
    }

    private void showNotification(Notification notification, int notificationId, boolean media) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (notificationId != NOTIFICATION_NOT_FOUND) {
            notificationManager.notify(notificationId, notification);
            if (media) {
                SongPlayer songPlayer = new SongPlayer(context, R.raw.knock);
                songPlayer.playSong();
                vibratorEngine.vibrate(VibratorEngine.LONG_VIBRATION_TIME);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// NOTIFICATION OPERATIONS - - END
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
