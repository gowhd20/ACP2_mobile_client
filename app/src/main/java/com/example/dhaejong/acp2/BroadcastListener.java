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

            SharedPref mSharedPreference = new SharedPref(context);
            mSharedPreference.saveInSp("checkbox", true);
            if(mSharedPreference.getFromSP("checkbox")){
                Intent calIntent = new Intent(context, CalendarService.class);
                context.startService(calIntent);
                Log.i(TAG, "re started CalendarService");
            }else{
            }


        }
    }

}
