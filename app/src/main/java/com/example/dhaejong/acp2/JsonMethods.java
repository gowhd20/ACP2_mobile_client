package com.example.dhaejong.acp2;

import android.util.Log;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dhaejong on 24.2.2016.
 */
public class JsonMethods {

    private static final String TAG = "JsonMethods";
    JsonObject datasetObj;
    JsonMethods(){}

    public JsonObject getGCMRefreshJson(int id, String token) {
        datasetObj = new JsonObject();
        try {
            datasetObj.addProperty(SystemPreferences.USER_ID, id);
            datasetObj.addProperty(SystemPreferences.GCM_TOKEN, token);
        }catch(JsonIOException e){
            e.printStackTrace();
        }

        return datasetObj;
    }

    public JsonObject getUserInfoJson(ArrayList<String> dataArray){
        datasetObj = new JsonObject();
        try {
            datasetObj.addProperty(SystemPreferences.FACEBOOK_ID, dataArray.get(0));
            datasetObj.addProperty(SystemPreferences.EMAIL, dataArray.get(1));
            datasetObj.addProperty(SystemPreferences.USER_NAME, dataArray.get(2));
            datasetObj.addProperty(SystemPreferences.FACEBOOK_TOKEN, dataArray.get(3));
            datasetObj.addProperty(SystemPreferences.GCM_TOKEN, dataArray.get(4));
        }catch(JsonIOException e){
            e.printStackTrace();
        }

        return datasetObj;
    }

    public JsonObject getUserInfoJsonToUpdate(ArrayList<String> dataArray){
        datasetObj = new JsonObject();
        try {
            datasetObj.addProperty(SystemPreferences.USER_ID, Integer.valueOf(dataArray.get(0)));
            datasetObj.addProperty(SystemPreferences.FACEBOOK_ID, dataArray.get(1));
            datasetObj.addProperty(SystemPreferences.EMAIL, dataArray.get(2));
            datasetObj.addProperty(SystemPreferences.USER_NAME, dataArray.get(3));
            datasetObj.addProperty(SystemPreferences.FACEBOOK_TOKEN, dataArray.get(4));
        }catch(JsonIOException e){
            e.printStackTrace();
        }

        return datasetObj;
    }

    public JsonObject getUserCategoryJson(int userId, int categoryId){
        datasetObj = new JsonObject();
        try {
            datasetObj.addProperty(SystemPreferences.USER_ID, userId);
            datasetObj.addProperty(SystemPreferences.CATEGORY_ID, categoryId);

        }catch(JsonIOException e){
            e.printStackTrace();
        }

        return datasetObj;
    }

    public JsonObject getUserMacAddrInfoJson(int userId, String mac) {
        datasetObj = new JsonObject();
        try {
            datasetObj.addProperty(SystemPreferences.USER_ID, userId);
            datasetObj.addProperty(SystemPreferences.MAC_ADDRESS, mac);
        }catch(JsonIOException e){
            e.printStackTrace();
        }

        return datasetObj;
    }

    public JsonObject getCalendarInfoJson(ArrayList<String> dataArray){
        datasetObj = new JsonObject();
        try {
            datasetObj.addProperty(SystemPreferences.CALENDAR_EVENT_ID, dataArray.get(0));
            datasetObj.addProperty(SystemPreferences.CALENDAR_EVENT_TITLE, dataArray.get(1));
            datasetObj.addProperty(SystemPreferences.CALENDAR_EVENT_DESCRIPTION, dataArray.get(2));
            datasetObj.addProperty(SystemPreferences.CALENDAR_EVENT_START_TIME, Integer.valueOf(dataArray.get(3)));
            datasetObj.addProperty(SystemPreferences.CALENDAR_EVENT_END_TIME, Integer.valueOf(dataArray.get(4)));
            datasetObj.addProperty(SystemPreferences.CALENDAR_EVENT_LOCATION, dataArray.get(5));
        }catch(JsonIOException e){
            e.printStackTrace();
        }

        return datasetObj;
    }

}
