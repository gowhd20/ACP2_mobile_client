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
        return preferences.getString(key, null);
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
        return preferences.getInt(key, 0);  // default 0
    }

    public ArrayList<String> getDataForUserRegistration() throws Exception{
        ArrayList<String> registerDataArray = new ArrayList<>();
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("General", android.content.Context.MODE_PRIVATE);
        registerDataArray.add(preferences.getString(SystemPreferences.FACEBOOK_ID, null));
        registerDataArray.add(preferences.getString(SystemPreferences.EMAIL, null));
        registerDataArray.add(preferences.getString(SystemPreferences.USER_NAME, null));
        registerDataArray.add(preferences.getString(SystemPreferences.FACEBOOK_TOKEN, null));
        registerDataArray.add(preferences.getString(SystemPreferences.GCM_TOKEN, null));
        return registerDataArray;
    }

    public ArrayList<String> getDataForUserInfoUpdate() throws Exception{
        ArrayList<String> updateDataArr = new ArrayList<>();
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("General", android.content.Context.MODE_PRIVATE);
        int userId = preferences.getInt(SystemPreferences.USER_ID, 0);
        updateDataArr.add(String.valueOf(userId));
        updateDataArr.add(preferences.getString(SystemPreferences.FACEBOOK_ID, null));
        updateDataArr.add(preferences.getString(SystemPreferences.EMAIL, null));
        updateDataArr.add(preferences.getString(SystemPreferences.USER_NAME, null));
        updateDataArr.add(preferences.getString(SystemPreferences.FACEBOOK_TOKEN, null));
        return updateDataArr;
    }

}
