package com.example.dhaejong.acp2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;


/**
 * Created by dhaejong on 12.2.2016.
 */
public class BroadcastListener extends BroadcastReceiver {

    private static final String TAG = "BroadcastListener";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.i(TAG, "detect a reboot in BroadcastListener.java");

            // restart get request service
            Intent httpRequestIntent = new Intent(context, httpService.class);
            context.startService(httpRequestIntent);

            // restart calendar service
            SharedPref mSharedPreference = new SharedPref(context);
            //mSharedPreference.saveInSp("checkbox", true);
            if(mSharedPreference.getFromSP(SystemPreferences.CHECKBOX)){
                //Intent calIntent = new Intent(context, CalendarService.class);
                //context.startService(calIntent);

                Intent calObserverIntent = new Intent(context, CalendarUpdatedService.class);
                context.startService(calObserverIntent);
                Log.i(TAG, "restarted CalendarService");
            }else{
            }


        }
    }

}
