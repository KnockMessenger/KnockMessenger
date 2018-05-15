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
 * This class receives the reboot state of the device. This is planned for later use.
 */

public class RebootReceiver extends BroadcastReceiver {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /// CONSTANTS

    private static final String REQUESTED_REBOOT_ACTION = "android.intent.action.BOOT_COMPLETED";

    /// CONSTANTS -- END

    @Inject
    protected UserDataManager userDataManager;

    @Inject
    protected ServiceAlarmManager serviceAlarmManager;

    @Inject
    protected VibratorEngine vibratorEngine;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// FIELDS -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// BroadcastReceiver OVERRIDES
    ////////////////////////////////////////////////////////////////////////////////////////////////

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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /// BroadcastReceiver OVERRIDES -- END
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
