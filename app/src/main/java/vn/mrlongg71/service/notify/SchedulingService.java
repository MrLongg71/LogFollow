package vn.mrlongg71.service.notify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import vn.mrlongg71.service.MainActivity;
import vn.mrlongg71.service.R;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

public class SchedulingService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Log.d("dsm", "onReceive: " + formatter.format(new Date()));

        Toast.makeText(context, "Time Up... Now Vibrating !!!" + new Date().getTime(),
                Toast.LENGTH_LONG).show();
        pushNotify("ALO " +formatter.format(new Date()) , context);
       // pushNotify("asd", context);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String getNotificationChannel(NotificationManager notificationManager, Context context) {
        String channelId = "channelid";
        String channelName = context.getResources().getString(R.string.app_name);
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }

    private  void pushNotify(String title, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;
//
//        Uri soundUri = Uri.parse(
//                "android.resource://" +
//                        getApplicationContext().getPackageName() +
//                        "/" +
//                        R.raw.ringvngo);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            //   mChannel.setSound(soundUri, audioAttributes);
            notificationManager.createNotificationChannel(mChannel);
        }


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setContentTitle("VNGo")
                .setSmallIcon(R.drawable.heart)
//                .setSound(soundUri)
                .setContentText("You just got a new pickup!!");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // stackBuilder.addNextIntent(intent);
//        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
//                0,
//                PendingIntent.FLAG_UPDATE_CURRENT
//        );
//        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
//        mBuilder.sound = Uri.parse("android.resource://"
//                + context.getPackageName() + "/" + R.raw.ringvngo.mp3);
    }

}