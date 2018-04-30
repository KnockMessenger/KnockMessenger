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
    public static final int PERIOD_TIME_IN_SECOND = 30;

    private AlarmManager alarmManager;
    private Context context;

    public ServiceAlarmManager(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.context = context;
    }

    public void createAlarmToStartService() {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent intent = PendingIntent.getBroadcast(context, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(setAlarmTime(), intent)/*AlarmManager.RTC_WAKEUP, setAlarmTime()*/, intent);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, setAlarmTime(), intent);
        Log.i("ALARM_MANAGER", "ALARM SET");
    }

    private long setAlarmTime() {
        Calendar alarmTime = Calendar.getInstance();
        DateTime now = new DateTime();
        now = now.plusSeconds(PERIOD_TIME_IN_SECOND);
        alarmTime.setTimeInMillis(now.getMillis());
        Log.i("ALARM_MANAGER", now.toString());

        return alarmTime.getTimeInMillis();
    }
}
