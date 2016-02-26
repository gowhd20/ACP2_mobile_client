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

    public JsonObject getUserInfoJson(ArrayList<String> dataArray) {
        datasetObj = new JsonObject();
        try {
            datasetObj.addProperty("facebook_id", dataArray.get(0));
            datasetObj.addProperty("email_address", dataArray.get(1));
            datasetObj.addProperty("full_name", dataArray.get(2));
            datasetObj.addProperty("short_token", dataArray.get(3));
            datasetObj.addProperty("google_id", dataArray.get(4));
        }catch(JsonIOException e){
            e.printStackTrace();
        }

        return datasetObj;
    }

    public JsonObject getUserCategoryJson(String userId, String categoryId) {
        datasetObj = new JsonObject();
        try {
            datasetObj.addProperty("user_id", userId);
            datasetObj.addProperty("category_id", categoryId);

        }catch(JsonIOException e){
            e.printStackTrace();
        }

        return datasetObj;
    }

    public JsonObject getUserMacAddrInfoJson(String userId, String mac) {
        datasetObj = new JsonObject();
        try {
            datasetObj.addProperty("user_id", userId);
            datasetObj.addProperty("mac_address", mac);
        }catch(JsonIOException e){
            e.printStackTrace();
        }

        return datasetObj;
    }

    public JsonObject getCalendarInfoJson(ArrayList<String> dataArray) {
        datasetObj = new JsonObject();
        try {
            datasetObj.addProperty("event_id", dataArray.get(0));
            datasetObj.addProperty("title", dataArray.get(1));
            datasetObj.addProperty("description", dataArray.get(2));
            datasetObj.addProperty("start_timestamp", dataArray.get(3));
            datasetObj.addProperty("end_timestamp", dataArray.get(4));
            datasetObj.addProperty("location", dataArray.get(5));
        }catch(JsonIOException e){
            e.printStackTrace();
        }

        return datasetObj;
    }

}
