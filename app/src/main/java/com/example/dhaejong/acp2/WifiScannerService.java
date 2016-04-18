package com.example.dhaejong.acp2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by dhaejong on 14.4.2016.
 */
public class WifiScannerService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private WifiManager wifiManager;
    private Handler handler;
    private Runnable runnable;

    @Override
    public void onCreate() {
        super.onCreate();
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if(wifiManager.getWifiState()==WifiManager.WIFI_STATE_DISABLED || wifiManager.getWifiState()==WifiManager.WIFI_STATE_DISABLING)
                    Log.d("WIFIRAN", "Wifi off");
                else {
                    boolean scanStarted = wifiManager.startScan();
                    if(scanStarted)
                        Log.d("WIFIRAN", "WifiRan");
                    else
                        Log.d("WIFIRAN", "Wifi didn't run");
                }

                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,20000);
            }
        };

        handler.post(runnable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
