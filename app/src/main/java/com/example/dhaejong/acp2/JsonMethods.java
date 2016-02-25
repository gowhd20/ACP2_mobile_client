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
    JsonMethods(){}

    public JSONObject getEventJson(JSONArray eventJsonArray) {
        // wrong
        JSONObject eventObj = new JSONObject();

        try {
            eventObj.get("user_id");
            eventObj.get("event");
            eventObj.get("id");
            eventObj.get("title");
            eventObj.get("categories");
            eventObj.get("description");
            eventObj.get("address");
            eventObj.get("price");
            eventObj.get("image_url");
            eventObj.get("start_timestamp");
            eventObj.get("end_timestamp");
        }catch(JSONException e){
            e.printStackTrace();
        }

        return eventObj;
    }

    public JsonObject getUserInfoJson(ArrayList<String> dataArray) {
        JsonObject userInfoObj = new JsonObject();
        try {
            userInfoObj.addProperty("facebook_id", dataArray.get(0));
            userInfoObj.addProperty("email_address", dataArray.get(1));
            userInfoObj.addProperty("full_name", dataArray.get(2));
            userInfoObj.addProperty("short_token", dataArray.get(3));
            userInfoObj.addProperty("google_id", dataArray.get(4));
        }catch(JsonIOException e){
            e.printStackTrace();
        }

        return userInfoObj;
    }

    public JsonObject getUserMacAddrInfoJson(ArrayList<String> dataArray) {
        JsonObject macAddr = new JsonObject();
        try {
            macAddr.addProperty("user_id", dataArray.get(0));
            macAddr.addProperty("mac_address", dataArray.get(1));
        }catch(JsonIOException e){
            e.printStackTrace();
        }

        return macAddr;
    }
}
