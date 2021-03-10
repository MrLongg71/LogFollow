package vn.mrlongg71.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import vn.mrlongg71.service.core.AppDatabase;
import vn.mrlongg71.service.model.room.Log;
import vn.mrlongg71.service.notify.AlarmUtils;
import vn.mrlongg71.service.service.Restart;

import static android.content.ContentValues.TAG;
import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

public class LocationService extends Service {
    private Handler handler;
    private LocationManager locationManager;
    LocationListener locationListener;
    AppDatabase appDatabase;
    private Date date;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
        date = new Date();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        appDatabase = AppDatabase.getInstance(this);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log log = new Log();
                log.lat = location.getLatitude();
                log.lng = location.getLatitude();
                log.createAt = format.format(Calendar.getInstance().getTime());
                appDatabase.getLogDao().insertLog(log);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };

        startForeground(110, pushNotify("ABC"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmUtils.create(getBaseContext());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    android.util.Log.d("dsd", "run: ");
                    //checkPermission();
                    //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                } catch (Exception e) {

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

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }

    private Notification pushNotify(String title) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? getNotificationChannel(notificationManager) : "";
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setWhen(System.currentTimeMillis())
                .setVibrate(new long[]{0, 500, 1000})
                .setContentTitle(title)
                .setContentText("DEF")
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/" + R.raw.apple))
                .setSmallIcon(R.drawable.heart)
                .setPriority(PRIORITY_MIN)
                .setLights(0xff0000ff, 300, 1000) // blue color
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .build();
        return notification;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        android.util.Log.d(TAG, "onDestroy: ");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restart.class);
        this.sendBroadcast(broadcastIntent);
    }
}
