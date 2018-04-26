package hu.vadasz.peter.knockmessenger.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import javax.inject.Inject;

import hu.vadasz.peter.knockmessenger.Application.BaseApplication;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.UserDataManager;
import hu.vadasz.peter.knockmessenger.Managers.ServiceAlarmManager;
import hu.vadasz.peter.knockmessenger.Services.MessageReceiverService;
import hu.vadasz.peter.knockmessenger.Tools.VibratorEngine;

/**
 * Created by Peti on 2018. 04. 22..
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Inject
    protected ServiceAlarmManager serviceAlarmManager;

    @Inject
    protected UserDataManager userDataManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("ALARM_RECEIVER", "RECEIVED");
        BaseApplication.getInstance().getmMainComponent().inject(this);

        userDataManager.loadUser();
        userDataManager.loadFriends();

        context.stopService(new Intent(context, MessageReceiverService.class));

        if (userDataManager.isLoggedIn()) {
            context.startService(new Intent(context, MessageReceiverService.class));
            serviceAlarmManager.createAlarmToStartService();
        }
    }
}
