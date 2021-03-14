package vn.mrlongg71.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import vn.mrlongg71.service.notify.SchedulingService;
import vn.mrlongg71.service.service.Restart;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        checkAdmin();
    }

    private void checkAdmin() {
        Button btn = findViewById(R.id.btn);
        EditText edt = findViewById(R.id.edt);
        btn.setOnClickListener(v -> {
            if (edt.getText().length() > 0) {

            }

            AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, SchedulingService.class);
            intent.setAction("syncData");
            PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, 1000,
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        });
    }

    private void checkPermission() {
        int checkPermissionCOARSE = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int checkPermissionFINE = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int checkPermissionREAD = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);

        if (checkPermissionCOARSE != PackageManager.PERMISSION_GRANTED &&
                checkPermissionFINE != PackageManager.PERMISSION_GRANTED && checkPermissionREAD != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_SMS}, 101);
        } else {
            startService();

        }
    }

    @Override
    protected void onDestroy() {
        Log.d("Mrlongg71", "onDestroy: View");
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
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_SMS}, 101);
            }
        }
    }

    private void startService() {
        Intent intent = new Intent(MainActivity.this, LocationService.class);
        startService(intent);

    }

}