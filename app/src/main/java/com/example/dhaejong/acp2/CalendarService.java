package com.example.dhaejong.acp2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.MissingResourceException;

/**
 * Created by dhaejong on 13.2.2016.
 */
public class CalendarService extends Service {

    private static final String TAG = "CalendarService";
    private static final String CALENDAR_ACTION = "android.intent.action.CALENDAR_ACTION";

    BroadcastReceiver myBroadcastReceiver;
    PendingIntent pendingIntent;
    IntentFilter intentFilter;
    Context context = this;

    @Override
    public void onCreate() {

        intentFilter = new IntentFilter(CALENDAR_ACTION);
        myBroadcastReceiver = new MyBroadcastReceiver();

        registerReceiver(myBroadcastReceiver, intentFilter);
        alarmStart(this);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful

        return Service.START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }


    public void alarmStart(Context context) {

        Intent alarmIntent = new Intent(CALENDAR_ACTION);
        pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // alarm checking time interval 10 sec
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), SystemPreferences.CALENDAR_QUERY_INTERVAL, pendingIntent);
        Toast.makeText(context, "Querying calendar initiated", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Alarm set");
    }


    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(CALENDAR_ACTION)) {

                PowerManager pm = (PowerManager) context.getSystemService(POWER_SERVICE);
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");

                wl.acquire();
                CalendarMethods mCalendar = new CalendarMethods(context);
                Toast.makeText(context, "Checking calendar", Toast.LENGTH_LONG).show(); // For example
                mCalendar.queryEvents(context);
                Log.i(TAG, " Checking calendar"); // For example
                wl.release();
            }
        }



    }
}
