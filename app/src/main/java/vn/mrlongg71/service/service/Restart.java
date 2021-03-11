package vn.mrlongg71.service.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import vn.mrlongg71.service.LocationService;
import vn.mrlongg71.service.core.AppDatabase;

public class Restart extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Broadcast Listened", "Service tried to stop" + intent.getAction());
        Toast.makeText(context, "Service restarted" , Toast.LENGTH_SHORT).show();
        if (intent.getAction().equalsIgnoreCase("restartservice")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, LocationService.class));
            } else {
                context.startService(new Intent(context, LocationService.class));
            }
        }
    }


}
