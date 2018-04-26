package hu.vadasz.peter.knockmessenger.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import hu.vadasz.peter.knockmessenger.DataPersister.Managers.UserDataManager;
import hu.vadasz.peter.knockmessenger.Managers.ServiceAlarmManager;
import hu.vadasz.peter.knockmessenger.Services.MessageReceiverService;
import hu.vadasz.peter.knockmessenger.Tools.VibratorEngine;

/**
 * Created by Peti on 2018. 04. 26..
 */

public class RebootReceiver extends BroadcastReceiver {

    private static final String REQUESTED_REBOOT_ACTION = "android.intent.action.BOOT_COMPLETED";

    @Inject
    protected UserDataManager userDataManager;

    @Inject
    protected ServiceAlarmManager serviceAlarmManager;

    @Inject
    protected VibratorEngine vibratorEngine;

    @Override
    public void onReceive(Context context, Intent intent) {
        vibratorEngine.vibrate(VibratorEngine.LONG_VIBRATION_TIME);
        if (REQUESTED_REBOOT_ACTION.equals(intent.getAction() )) {
            userDataManager.loadUser();

            context.stopService(new Intent(context, MessageReceiverService.class));
            if (userDataManager.isLoggedIn()) {
                context.startService(new Intent(context, MessageReceiverService.class));
                serviceAlarmManager.createAlarmToStartService();
            }
        }
    }
}
