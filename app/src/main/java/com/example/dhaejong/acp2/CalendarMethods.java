package com.example.dhaejong.acp2;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.MissingResourceException;

/**
 * Created by dhaejong on 26.2.2016.
 */
public class CalendarMethods {
    Context context;
    private static final String TAG = "CalendarMethods";

    protected final String[] INSTANCE_PROJECTION = new String[]{
            CalendarContract.Instances.EVENT_ID,        // 0
            CalendarContract.Instances.BEGIN,           // 1
            CalendarContract.Instances.TITLE,           // 2
            CalendarContract.Instances.END,             // 3
            CalendarContract.Instances.DESCRIPTION,     // 4
            CalendarContract.Instances.EVENT_LOCATION   // 5
    };

    // The indices for the projection array above.

    protected final int PROJECTION_ID_INDEX = 0;
    protected final int PROJECTION_BEGIN_INDEX = 1;
    protected final int PROJECTION_TITLE_INDEX = 2;
    protected final int PROJECTION_END_INDEX = 3;
    protected final int PROJECTION_DESCRIPTION_INDEX = 4;
    protected final int PROJECTION_LOCATION_INDEX = 5;

    CalendarMethods(Context context){
        this.context = context;
    }

    public String queryEvents(Context context) {

        ArrayList<String> calendarArr = new ArrayList<>();
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
        Cursor cur = cr.query(builder.build(),
                INSTANCE_PROJECTION,
                null,
                null,
                null);
        cur.moveToFirst();

        JsonMethods mJson = new JsonMethods();
        JsonArray calJsonArr = new JsonArray();

        while (cur.moveToNext()) {
            // can also add condition if isEventOn == false;
            // Get the field values

            try{
                calendarArr.add(String.valueOf(cur.getLong(PROJECTION_ID_INDEX)));
            } catch(MissingResourceException e) {
                e.printStackTrace();
                calendarArr.add("");
            }
            try{
                calendarArr.add(cur.getString(PROJECTION_TITLE_INDEX));
            } catch(MissingResourceException e) {
                e.printStackTrace();
                calendarArr.add("");
            }
            try{
                calendarArr.add(cur.getString(PROJECTION_DESCRIPTION_INDEX));
            } catch(MissingResourceException e) {
                e.printStackTrace();
                calendarArr.add("");
            }
            try{
                calendarArr.add(String.valueOf(cur.getLong(PROJECTION_BEGIN_INDEX)).substring(0, 10));
            } catch(MissingResourceException e) {
                e.printStackTrace();
                calendarArr.add("");
            }
            try{
                calendarArr.add(String.valueOf(cur.getLong(PROJECTION_END_INDEX)).substring(0, 10));
            } catch(MissingResourceException e) {
                e.printStackTrace();
                calendarArr.add("");
            }
            try{
                calendarArr.add(cur.getString(PROJECTION_LOCATION_INDEX));
            } catch(MissingResourceException e) {
                e.printStackTrace();
                calendarArr.add("");
            }
            calJsonArr.add(mJson.getCalendarInfoJson(calendarArr));
            calendarArr.clear();

        }
        JsonObject calJsonObj = new JsonObject();
        SharedPref mSharedPref = new SharedPref(context);

        calJsonObj.addProperty("id", mSharedPref.getIntFromSP(SystemPreferences.USER_ID));
        calJsonObj.addProperty("events", calJsonArr.toString());
        String temp = calJsonObj.toString().replaceAll("\\\\", "");
        Log.d(TAG, temp);

        cur.close();
        return temp;
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
