package vn.mrlongg71.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import vn.mrlongg71.service.service.Restart;

public class MainActivity extends AppCompatActivity implements LocationListener {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

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
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    private void checkPermission() {
        int checkPermissionCOARSE = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int checkPermissionFINE = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int checkPermissionREAD = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        if (checkPermissionCOARSE != PackageManager.PERMISSION_GRANTED &&
                checkPermissionFINE != PackageManager.PERMISSION_GRANTED && checkPermissionREAD != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        } else {
            startService();

        }
    }

    @Override
    protected void onDestroy() {
        Log.d("sd", "onDestroyView: ");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restart.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startService();
            } else {
                Toast.makeText(this, "Bạn chưa cấp quyền", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_SMS}, 101);

            }
        }
    }

    private void startService() {
        Intent intent = new Intent(MainActivity.this, LocationService.class);
        //startService(intent);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncData(MainActivity.this);
            }
        });


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
        Log.d("eSalesLog", "getData: " + logList.size());
        for (LogFollow log : logList) {
                try {
                    addresses = geocoder.getFromLocation(log.getLat(), log.getLng(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null && addresses.size() > 0) {
                    log.setAddress(addresses.get(0).getAddressLine(0));
                }
            Log.d("eSalesLog", "getData: " + log.getId() + " - " + log.getAddress() + " - " + log.getCreateAt());

        }

        return logList;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("eSalesLog", "onLocationChanged: " + location.getLatitude());

    }
}