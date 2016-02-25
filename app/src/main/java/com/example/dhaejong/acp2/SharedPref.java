package com.example.dhaejong.acp2;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by dhaejong on 13.2.2016.
 */
public class SharedPref {
    Context context;
    private static final String TAG = "SharedPref";

    SharedPref(Context context){
        this.context = context;
    }

    public boolean getFromSP(String key){
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("General", android.content.Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }
    public void saveInSp(String key, boolean value){
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("General", android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getStringFromSP(String key){
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("General", android.content.Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public void saveInSp(String key, String value){
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("General", android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void saveInSp(String key, int value){
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("General", android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getIntFromSP(String key){
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("General", android.content.Context.MODE_PRIVATE);
        return Integer.valueOf(preferences.getString(key, ""));
    }

    public ArrayList<String> getDataForUserRegistration() throws Exception{
        ArrayList<String> registerDataArray = new ArrayList<>();
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("General", android.content.Context.MODE_PRIVATE);
        registerDataArray.add(preferences.getString(SystemPreferences.FACEBOOK_ID, ""));
        registerDataArray.add(preferences.getString(SystemPreferences.EMAIL, ""));
        registerDataArray.add(preferences.getString(SystemPreferences.USER_NAME, ""));
        registerDataArray.add(preferences.getString(SystemPreferences.FACEBOOK_TOKEN, ""));
        registerDataArray.add(preferences.getString("gcm_id", SystemPreferences.GCM_ID));
        return registerDataArray;
    }

}
