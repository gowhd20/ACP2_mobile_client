package com.example.dhaejong.acp2;

import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.CalendarContract;

import android.os.Handler;
import android.util.Log;

/**
 * Created by dhaejong on 26.2.2016.
 */
public class CalendarUpdatedService extends Service {
    static final String TAG = "CalendarUpdatedService";
    Context context = this;
    HttpRequests mHttpRequests;
    CalendarMethods mCalendar = new CalendarMethods(context);
    httpNetwork mHttpNet;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int returnValue = super.onStartCommand(intent, flags, startId);
        getContentResolver().registerContentObserver(CalendarContract.Events.CONTENT_URI, true, observer);

        // post calendar event once when checkbox is checked
        mHttpNet = new httpNetwork(context);
        mHttpNet.updateCalendarReq(mCalendar.queryEvents(context));

        return returnValue;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    ContentObserver observer = new ContentObserver(new Handler()) {

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange);
            Log.i(TAG, "calendar state updated");
            mHttpNet = new httpNetwork(context);
            mHttpNet.updateCalendarReq(mCalendar.queryEvents(context));

        }
    };

}
