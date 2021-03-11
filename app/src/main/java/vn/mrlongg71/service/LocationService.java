package vn.mrlongg71.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import vn.mrlongg71.service.core.AppDatabase;
import vn.mrlongg71.service.model.room.LogFollow;
import vn.mrlongg71.service.notify.AlarmUtils;
import vn.mrlongg71.service.service.Restart;

import static android.content.ContentValues.TAG;
import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

public class LocationService extends Service implements LocationListener {
    private Handler handler;
    private LocationManager locationManager;
    AppDatabase appDatabase;
    private Date date;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        date = new Date();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        appDatabase = AppDatabase.getInstance(this);
        AlarmUtils.create(getBaseContext());


        startForeground(110, pushNotify());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return START_STICKY;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this::onLocationChanged);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                } catch (Exception e) {
                    Log.d("eSalesLog", "run: " + e.getMessage());
                }
                handler.postDelayed(this, 5000);
            }
        }, 5000);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String getNotificationChannel(NotificationManager notificationManager) {
        String channelId = "channelid";
        String channelName = getResources().getString(R.string.app_name);
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }


    private Notification pushNotify() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? getNotificationChannel(notificationManager) : "";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        return notificationBuilder.setOngoing(true)
                .setWhen(System.currentTimeMillis())
                .setVibrate(new long[]{0, 500, 1000})
                .setContentTitle("")
                .setContentText("")
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setSmallIcon(R.drawable.heart)
                .setPriority(PRIORITY_MIN)
                .setLights(0xff0000ff, 300, 1000) // blue color
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restart.class);
        this.sendBroadcast(broadcastIntent);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");

        android.util.Log.d("dsd", "run: " + location.getLatitude());
        LogFollow logFollow = new LogFollow();
        logFollow.setLat(location.getLatitude());
        logFollow.setLng(location.getLatitude());
        logFollow.setCreateAt(format.format(Calendar.getInstance().getTime()));
        appDatabase.getLogDao().insertLog(logFollow);
    }
}
