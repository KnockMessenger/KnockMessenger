package hu.vadasz.peter.knockmessenger.Managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.Calendar;

import hu.vadasz.peter.knockmessenger.BroadcastReceivers.AlarmReceiver;

/**
 * Created by Peti on 2018. 04. 24..
 */

public class ServiceAlarmManager {

    public static final int PERIOD_TIME_IN_MINUTE = 1;

    private AlarmManager alarmManager;
    private Context context;

    public ServiceAlarmManager(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.context = context;
    }

    public void createAlarmToStartService() {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent intent = PendingIntent.getBroadcast(context, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, setAlarmTime(), intent);
        Log.i("ALARM_MANAGER", "ALARM SET");
    }

    private long setAlarmTime() {
        Calendar alarmTime = Calendar.getInstance();
        DateTime now = new DateTime();
        alarmTime.setTimeInMillis(System.currentTimeMillis());
        alarmTime.set(Calendar.YEAR, now.getYear());
        alarmTime.set(Calendar.MONTH, now.getMonthOfYear()-1);
        alarmTime.set(Calendar.DAY_OF_MONTH, now.getDayOfMonth());
        alarmTime.set(Calendar.HOUR_OF_DAY, now.getHourOfDay());
        alarmTime.set(Calendar.MINUTE, now.getMinuteOfHour() + PERIOD_TIME_IN_MINUTE);
        alarmTime.set(Calendar.SECOND, 0);

        return alarmTime.getTimeInMillis();
    }
}
