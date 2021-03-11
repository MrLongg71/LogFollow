package vn.mrlongg71.service.notify;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import vn.mrlongg71.service.core.AppDatabase;
import vn.mrlongg71.service.helper.Helper;
import vn.mrlongg71.service.model.room.LogFollow;

public class SchedulingService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        if (intent != null) {
            if (intent.getAction().equalsIgnoreCase("syncData")) {
                Log.d("dsm", "onReceive: " + formatter.format(new Date()));
                Toast.makeText(context, "Time Up... Now Vibrating !!!" + formatter.format(new Date()),
                        Toast.LENGTH_LONG).show();
                syncData(context);
                NotificationScheduler.showNotification(context, "Test", "Time" + formatter.format(new Date()));
            }
        }


    }

    private void syncData(Context context) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(Helper.getDeviceName());
        myRef.child(format.format(new Date())).setValue(getData(context)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("eSalesLog", "onComplete: ");
                }
            }
        });


    }


    private List<LogFollow> getData(Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;

        AppDatabase appDatabase = AppDatabase.getInstance(context);
        List<LogFollow> logList = appDatabase.getLogDao().getListLog();
        for (LogFollow log : logList) {
                try {
                    addresses = geocoder.getFromLocation(log.getLat(), log.getLng(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null && addresses.size() > 0) {
                    log.setAddress(addresses.get(0).getAddressLine(0));
                }
        }

        return logList;
    }
}