package vn.mrlongg71.service.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import vn.mrlongg71.service.core.AppDatabase;
import vn.mrlongg71.service.core.helper.DateHelper;
import vn.mrlongg71.service.core.helper.Helper;
import vn.mrlongg71.service.model.database.LogFollow;

public class SchedulingService extends BroadcastReceiver {

    private AppDatabase appDatabase;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        appDatabase = AppDatabase.getInstance(context);

        if (intent != null) {
            if (intent.getAction().equalsIgnoreCase("syncData")) {
                syncData(context);
                Log.d("Mrlongg71", "onReceive Sync: " + DateHelper.getString(null));
            }

            if (intent.getAction().equalsIgnoreCase("goodNight")) {
                NotificationScheduler.showNotification(context, "Good Night", "Time " + DateHelper.getString(null));
                Log.d("Mrlongg71", "onReceive Good night: " + DateHelper.getString(null));
            }
        }

    }

    private void syncData(Context context) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Helper.getDeviceName());
        myRef.child(DateHelper.getString("ddMMyyyy")).setValue(getData(context)).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                appDatabase.getLogDao().updateLogFollow();
                Log.d("Mrlongg71", "syncData: success");
            }
        });
    }

    private List<LogFollow> getData(Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        List<LogFollow> logList = appDatabase.getLogDao().getListLogFollow();
        if (logList.size() > 0) {
            for (LogFollow log : logList) {
                try {
                    addresses = geocoder.getFromLocation(log.getLat(), log.getLng(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null && addresses.size() > 0) {
                    log.setAddress(addresses.get(0).getAddressLine(0));
                    log.setSync(true);
                }
            }
        }
        return logList;
    }
}