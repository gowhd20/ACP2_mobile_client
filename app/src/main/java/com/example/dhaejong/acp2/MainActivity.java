package com.example.dhaejong.acp2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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
    Tags mTags;
    EnableGcm mGcm;

    private Intent wifiIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiIntent = new Intent(this, WifiScannerService.class);
        startService(wifiIntent);

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
        mTags = new Tags(MainActivity.this, this);
        mGcm = new EnableGcm(context, MainActivity.this);
        mGcm.initGcm();                                               // set all services of gcm

        //mTags.mLocalDB.dropDB();    // drop db everytime running *********** test purpose ****************
        // if user uses this app first time
        // open mainActivity and register user to the server
        int tagCount = mTags.countTagsInList;
        SharedPref mSharedPref = new SharedPref(this);

        // configure calendar query service as saved state
        SharedPref mSharedPreference = new SharedPref(this);
        if(mSharedPreference.getFromSP(SystemPreferences.CHECKBOX)){
            //Intent calIntent = new Intent(this, CalendarService.class);
            //startService(calIntent);

            Intent calObserverIntent = new Intent(this, CalendarUpdatedService.class);
            startService(calObserverIntent);
        }

        try {
            // get ids of this device for credential issue
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            String macAddress = wInfo.getMacAddress();
            Log.d(TAG, String.valueOf(macAddress));
            mSharedPref.saveInSp(SystemPreferences.MAC_ADDRESS, macAddress);
        }catch(Exception e){
            e.printStackTrace();
        }
        try {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
            Log.d("ID", "Android ID: " + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));
            Log.d("ID", "Device ID : " + tm.getDeviceId());
            mSharedPref.saveInSp(SystemPreferences.ANDROID_ID, android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));
            mSharedPref.saveInSp(SystemPreferences.DEVICE_ID, tm.getDeviceId());
        }catch(Exception e){
            e.printStackTrace();
        }

        //if(!mSharedPref.getFromSP(SystemPreferences.USER_REGISTERED)){
            // or first time user
            // user must register their info to the server
            // possibly register here without facebook id and token
        //    mSharedPref.saveInSp(SystemPreferences.USER_REGISTERED, true);        // set true that user is registered in the server

        //TODO: get google token is called later than user register
         //   HttpRequests mHttpRequests = new HttpRequests(this,0,4);         // flag 4 -> register user to the server
         //   mHttpRequests.run();


        //}else{
            // if user uses this app not first time but has no tags added
            // open mainActivity to give more introduction
            if(tagCount != 0 ){
                ProgressBar mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
                mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                Intent intent = new Intent(this, Main2Activity.class);
                startActivity(intent);
            }
        //}

        // TODO: this won't need as get query take place everytime user interact with app interface which requires server data
        // TODO: however this need some adjustment in code otherwise we found error messages that attempted searchbar search without category contents, so i will leave this for now
        //Intent intent = new Intent(this, httpService.class);
        //startService(intent);
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
    protected void onResume() {
        super.onResume();
        mGcm.registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mGcm.mRegistrationBroadcastReceiver);
        mGcm.isReceiverRegistered = false;
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
