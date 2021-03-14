package vn.mrlongg71.service.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Objects;

import vn.mrlongg71.service.LocationService;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)){
            Log.d("Mrlongg71", "onReceive: ACTION_BOOT_COMPLETED");
            context.startActivity(new Intent(context, LocationService.class));
        }
    }
}
