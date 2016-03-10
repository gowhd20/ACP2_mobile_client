package com.example.dhaejong.acp2;

import android.app.Service;

import android.content.Context;
import android.content.Intent;

import android.os.IBinder;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by dhaejong on 24.2.2016.
 */
public class httpService extends Service {

    private static final String TAG = "httpService";
    httpNetwork mHttpNetwork;
    Context context = this;
    boolean exit = false;
    public static Boolean NETWORK_AVAILABLE = true;
    protected ArrayList<CategoryList> tags;
    private void getRequest(){

        mHttpNetwork = new httpNetwork(this);

        try {
            String response = mHttpNetwork.getRequest(SystemPreferences.GET_CATEGORIES_URL);
            int categoryId = mHttpNetwork.getIdOfCategory(response);
            Log.d(TAG, Integer.toString(categoryId));
            Log.d(TAG, response);

            // store category names
            CategoryList mCategory = new CategoryList();
            mCategory.setCategories(response);
            NETWORK_AVAILABLE = true;
            // save id of category in sharedpreference for future post
            SharedPref mSharedPref = new SharedPref(this);
            mSharedPref.saveInSp(SystemPreferences.CATEGORY_LIST, categoryId);

            //Log.d(TAG, response);
        }catch(IOException e){
            e.printStackTrace();
            Log.e(TAG, "get failed");
            NETWORK_AVAILABLE = false;
        }
    }

    Thread getRequestThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while(!exit) {
                getRequest();
                try {
                    Thread.sleep(SystemPreferences.GET_CATEGORY_REQUEST_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    });

    @Override
    public void onCreate() {
        this.tags = new ArrayList<>();
        // init request with server
        mHttpNetwork = new httpNetwork(this);

        // TODO: need a condition to check whether the user is available of Internet connection,
        // TODO: ckeck out https://github.com/novoda/merlin

        getRequestThread.start();


    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        exit = true;
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

}
