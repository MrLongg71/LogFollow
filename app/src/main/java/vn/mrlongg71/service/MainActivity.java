package vn.mrlongg71.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import vn.mrlongg71.service.notify.AlarmUtils;
import vn.mrlongg71.service.service.Restart;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        AlarmUtils.create(getBaseContext());
        //Intent intent = new Intent(this, AlertDetails.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                // mBuilder.build();
                showNotification(MainActivity.this, "dsds", "sds");
                Intent intent = new Intent(MainActivity.this, LocationService.class);
                startService(intent);
            }
        });


//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//
//        List<Address> addresses = null;
//
//        AppDatabase appDatabase = AppDatabase.getInstance(this);
//        List<Log> logList = appDatabase.getLogDao().getListLog();
//        for (Log log : logList) {
//            if(log.id != 1){
//                try {
//                    addresses = geocoder.getFromLocation(log.lat, log.lng, 1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (addresses != null && addresses.size() > 0) {
//                    log.setAddress(addresses.get(0).getAddressLine(0));
//                }
//            }
//        }


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

    @Override
    protected void onDestroy() {
        Log.d("sd", "onDestroyView: ");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));

        broadcastIntent.setClass(this, Restart.class);
        this.sendBroadcast(broadcastIntent);


        super.onDestroy();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = ("channel_name");
            String description = ("channel_description");
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("32", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showNotification(Context context, String title, String body) {
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