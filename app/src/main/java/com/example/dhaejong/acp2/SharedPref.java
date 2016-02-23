package com.example.dhaejong.acp2;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dhaejong on 13.2.2016.
 */
public class SharedPref {

    Context context;

    SharedPref(Context context){
        this.context = context;
    }

    public boolean getFromSP(String key){
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("Calendar", android.content.Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }
    public void saveInSp(String key, boolean value){
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("Calendar", android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public String getStringFromSP(String key){
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("Email", android.content.Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public void saveInSp(String key, String value){
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("Email", android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

}
