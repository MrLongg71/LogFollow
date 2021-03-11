package vn.mrlongg71.service;

import android.Manifest;
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
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import vn.mrlongg71.service.core.AppDatabase;
import vn.mrlongg71.service.core.helper.DateHelper;
import vn.mrlongg71.service.model.database.LogFollow;
import vn.mrlongg71.service.notify.AlarmUtils;
import vn.mrlongg71.service.service.Restart;

import static androidx.core.app.NotificationCompat.PRIORITY_MIN;

public class LocationService extends Service implements LocationListener {
    private LocationManager locationManager;
    AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        appDatabase = AppDatabase.getInstance(this);
        AlarmUtils.create(getBaseContext());
        startForeground(110, pushNotify());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return START_STICKY;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, this);


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
        Log.d("Mrlongg71", "onLocationChanged: ");
        LogFollow logFollow = new LogFollow();
        logFollow.setLat(location.getLatitude());
        logFollow.setLng(location.getLongitude());
        logFollow.setCreateAt(DateHelper.getString(null));
        appDatabase.getLogDao().insertLog(logFollow);
    }
}
