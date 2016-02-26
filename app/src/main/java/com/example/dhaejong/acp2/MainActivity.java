package com.example.dhaejong.acp2;

import android.content.Context;
import android.content.Intent;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    Context context = this;
    Runnable postRegisterUserReq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View mainView = findViewById(R.id.mainActivity);
        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "screen Clicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, Settings.class);
                startActivity(intent);

            }
        });

        // TODO: show a notification checking if it's right person when mobile receives events info

        Tags mTags = new Tags(MainActivity.this, this);
        EnableGcm mGcm = new EnableGcm(context, MainActivity.this);
        mGcm.initGcm();                                               // set all services of gcm

        //mTags.mLocalDB.dropDB();    // drop db everytime running *********** test purpose ****************
        // if user uses this app first time
        // open mainActivity and register user to the server
        int tagCount = mTags.countTagsInList;
        if(SystemPreferences.IS_YOUR_FIRST_PLAY){
            // or first time user
            // user must register their info to the server
            // possibly register here without facebook id and token
           /* postRequestThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    httpNetwork mHttpNet = new httpNetwork(context);
                    mHttpNet.registerUserRequest();
                }
            });*/

            postRegisterUserReq = new Runnable() {
                @Override
                public void run() {
                    httpNetwork mHttpNet = new httpNetwork(context);
                    mHttpNet.registerUserRequest();
                }
            };
            postRegisterUserReq.run();
            //postRequestThread.start();
            SystemPreferences.IS_YOUR_FIRST_PLAY = false;

        }else{
            // if user uses this app not first time but has no tags added
            // open mainActivity to give more introduction
            if(tagCount != 0 ){
                Intent intent = new Intent(this, Main2Activity.class);
                startActivity(intent);
            }
        }

        // configure calendar query service as saved state
        SharedPref mSharedPreference = new SharedPref(this);
        if(mSharedPreference.getFromSP(SystemPreferences.CHECKBOX)){
            Intent calIntent = new Intent(this, CalendarService.class);
            startService(calIntent);
        }

        // get ids of this device for credential issue
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress = wInfo.getMacAddress();
        Log.d(TAG, macAddress);
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        Log.d("ID", "Android ID: " + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));
        Log.d("ID", "Device ID : " + tm.getDeviceId());

        SharedPref mSharedPref = new SharedPref(this);
        mSharedPref.saveInSp(SystemPreferences.MAC_ADDRESS, macAddress);
        mSharedPref.saveInSp(SystemPreferences.ANDROID_ID, android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));
        mSharedPref.saveInSp(SystemPreferences.DEVICE_ID, tm.getDeviceId());

        Intent intent = new Intent(this, httpService.class);
        startService(intent);

/*

        ArrayList<String> list = new ArrayList<>();
        list.add("9");
        list.add("tivoli hot bitch party");
        list.add("bubble");
        list.add("As the weather is getting warmer, the streets are icy and the snow is falling from the roofs on your head, you know: " +
                "Summer is almost here! To accelerate the process a bit, we are already throwing you a beach party. So bring your beach toys, " +
                "leave your winter clothes at home and be ready to feel the heat on your bum and the sand between your toes. As always, " +
                "with more than student friendly prices, wink wink! ");
        list.add("Tivoli");
        list.add("2 in advance, 4 from the door, free entry with ESN Card");
        list.add("https://www.facebook.com/events/1662196667386875/");
        list.add("2016-02-18 22:00:00");
        list.add("");

        boolean result = mTags.mLocalDB.addNewEvent(list);
        if(result){
            list = mTags.mLocalDB.getAllItemsById(9, LocalDB.DATABASE_TABLE_NAME_EVENTS);
            Log.d(TAG, list.toString());

        }else{
            Log.e(TAG, "wrong");
        }
        */

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                if(!SystemPreferences.IS_SETTINGS_ACTIVITY_ACTIVE) {
                    Intent intent = new Intent(this, Settings.class);
                    SystemPreferences.IS_SETTINGS_ACTIVITY_ACTIVE = true;
                    Log.i(TAG, "Settings.java is starting");
                    startActivity(intent);
                    return true;
                }else{
                    Toast.makeText(MainActivity.this," Settings.java is already running", Toast.LENGTH_LONG).show();
                    return false;
                }
            case R.id.title_activity_main2:
                Intent intent = new Intent(this, Main2Activity.class);
                startActivity(intent);
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }



}
