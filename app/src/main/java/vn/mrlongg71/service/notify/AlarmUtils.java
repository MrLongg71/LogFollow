package vn.mrlongg71.service.notify;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import vn.mrlongg71.service.R;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

public class AlarmUtils {
    private static int INDEX = 1;

    public static void create(Context context) {
        Intent intent = new Intent(context, SchedulingService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 280192, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + (1 * 1000), 10000
                , pendingIntent);
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//
//        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, SchedulingService.class);
//        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY, 00);
//        calendar.set(Calendar.MINUTE, 13);
//
//        Date date = calendar.getTime();
//
//        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                AlarmManager.INTERVAL_HOUR, alarmIntent);
//        Log.d("sd", "create: " +formatter.format(date));
        Toast.makeText(context, "Alarm will set in " + 1 + " seconds",
                Toast.LENGTH_LONG).show();
    }


}

