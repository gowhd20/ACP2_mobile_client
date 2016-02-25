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

/**
 * Created by dhaejong on 13.2.2016.
 */
public class CalendarService extends Service {

    private static final String TAG = "Calendar";

    public static final String[] INSTANCE_PROJECTION = new String[]{
            CalendarContract.Instances.EVENT_ID,        // 0
            CalendarContract.Instances.BEGIN,           // 1
            CalendarContract.Instances.TITLE,           // 2
            CalendarContract.Instances.END,             // 3
            CalendarContract.Instances.DESCRIPTION,     // 4
            CalendarContract.Instances.EVENT_LOCATION   // 5
    };

    // The indices for the projection array above.

    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_BEGIN_INDEX = 1;
    private static final int PROJECTION_TITLE_INDEX = 2;
    private static final int PROJECTION_END_INDEX = 3;
    private static final int PROJECTION_DESCRIPTION_INDEX = 4;
    private static final int PROJECTION_LOCATION_INDEX = 5;

    private BroadcastReceiver myBroadcastReceiver;
    private static final String CALENDAR_ACTION = "android.intent.action.CALENDAR_ACTION";

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
        Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Alarm set");
    }


    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(CALENDAR_ACTION)) {

                PowerManager pm = (PowerManager) context.getSystemService(POWER_SERVICE);
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");

                wl.acquire();
                Toast.makeText(context, "Checking calendar", Toast.LENGTH_LONG).show(); // For example
                queryEvents(context);
                Log.i(TAG, " Checking calendar"); // For example
                wl.release();
            }
        }

        public void queryEvents(Context context) {

            Cursor cur;
            java.util.Calendar beginTime = java.util.Calendar.getInstance();
            long startMillis = beginTime.getTimeInMillis();
            java.util.Calendar endTime = java.util.Calendar.getInstance();

            endTime.add(java.util.Calendar.MONTH, 1);
            long endMillis = endTime.getTimeInMillis();

            ContentResolver cr = context.getContentResolver();

            Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
            ContentUris.appendId(builder, startMillis);
            ContentUris.appendId(builder, endMillis);

            // Submit the query
            cur = cr.query(builder.build(),
                    INSTANCE_PROJECTION,
                    null,
                    null,
                    null);
            cur.moveToFirst();
            while (cur.moveToNext()) {   // can also add condition if isEventOn == false;
                String eventTitle;
                long eventID;
                long eventStartTime;
                long eventEndTime;
                String eventDescription;
                String eventLocation;


                // Get the field values
                eventID = cur.getLong(PROJECTION_ID_INDEX);
                eventStartTime = cur.getLong(PROJECTION_BEGIN_INDEX);
                eventTitle = cur.getString(PROJECTION_TITLE_INDEX);
                eventEndTime = cur.getLong(PROJECTION_END_INDEX);
                eventDescription = cur.getString(PROJECTION_DESCRIPTION_INDEX);
                eventLocation = cur.getString(PROJECTION_LOCATION_INDEX);

                Log.d(TAG, Long.toString(eventID));
                Log.d(TAG, Long.toString(eventStartTime));
                Log.d(TAG, eventTitle);
                Log.d(TAG, Long.toString(eventEndTime));
                Log.d(TAG, eventDescription);
                Log.d(TAG, eventLocation);

            }
        }

        // checking length of the detected event and return if it's current or not
        private boolean onEvent(long begin, long end) {

            java.util.Calendar getTime = java.util.Calendar.getInstance();
            long currentTime = getTime.getTimeInMillis();

            if (currentTime >= begin && currentTime <= end) {
                return true;
            } else {
                return false;
            }
        }


    }
}
