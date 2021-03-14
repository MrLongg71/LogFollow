package vn.mrlongg71.service.notify;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import vn.mrlongg71.service.core.helper.DateHelper;

public class AlarmUtils {

    public static void create(Context context) {
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, SchedulingService.class);
        intent.setAction("syncData");
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 00);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        Log.d("Mrlongg71", "onCreate AlarmManager Sync: " + DateHelper.getString(calendar.getTime(),null));

        intent.setAction("goodNight");
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 00);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        Log.d("Mrlongg71", "onCreate AlarmManager GoodNight: " + DateHelper.getString(calendar.getTime(), null));

    }


}

