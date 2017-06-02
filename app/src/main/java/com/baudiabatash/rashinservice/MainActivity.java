package com.baudiabatash.rashinservice;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvLatLong;
    private Button btnStartservice,btnStopService;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

       if(!run_time_permission()){
           enable_button();
       }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(broadcastReceiver== null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    tvLatLong.append("\n"+intent.getExtras().get("coordinates"));

                }
            };

            registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(broadcastReceiver!= null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    private void enable_button() {
        btnStartservice.setOnClickListener(this);
        btnStopService.setOnClickListener(this);
    }

    private boolean run_time_permission() {
        if(Build.VERSION.SDK_INT>23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return true;
        }
        return false;
    }

    private void initView() {
        tvLatLong = (TextView) findViewById(R.id.lat_lng);
        btnStartservice = (Button) findViewById(R.id.start_service);
        btnStopService = (Button) findViewById(R.id.stop_service);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                enable_button();
            }else {
                run_time_permission();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.start_service:
                Intent intent = new Intent(getApplicationContext(),GPS_SERVICE.class);
                startService(intent);
                break;

            case R.id.stop_service:
                Intent intent2 = new Intent(getApplicationContext(),GPS_SERVICE.class);
                stopService(intent2);
                break;
        }
    }
}
