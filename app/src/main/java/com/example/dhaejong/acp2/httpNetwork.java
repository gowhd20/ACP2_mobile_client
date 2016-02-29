package com.example.dhaejong.acp2;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dhaejong on 15.2.2016.
 */
public class httpNetwork {

    Context context;
    OkHttpClient client;
    SharedPref mSharedPref;
    JsonMethods mJsonMethods;
    Request request;
    Response response;
    RequestBody requestBody;
    HttpCallback cb;
    JsonObject jsonObj;
    public static Boolean REQ_ITEM_ADD_SUCCEED = false;
    public static Boolean REQ_ITEM_DELETE_SUCCEED = false;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "httpNetwork";


    httpNetwork(Context context){
        this.context = context;
        this.client = new OkHttpClient();

        mSharedPref = new SharedPref(context);
        mJsonMethods = new JsonMethods();
    }

    public String getRequest(String url) throws IOException{
        request = new Request.Builder()
                .url(url)
                .build();

        response = client.newCall(request).execute();
        String res = response.body().string();
        response.body().close();
        return res;

    }

    public interface HttpCallback{
        void onFailure(Call call, IOException e);

        void onSuccess(Response response);
    }

    private Request addBasicAuthHeaders(Request request) {
        String credential = Credentials.basic("id", "pwd");
        return request.newBuilder().header("Authorization", credential).build();
    }

    public void postRequest(String url, Object json, final HttpCallback cb) throws IOException {
        Log.i(TAG, "what do i post to server? : "+ json.toString());
        requestBody = RequestBody.create(JSON, new Gson().toJson(json));
        request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader("Accept", "application/json")
                .addHeader("token", "test")
                .build();
        //request = addBasicAuthHeaders(request);       // add pwd and id

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                cb.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                cb.onSuccess(response);

            }
        });
    }

    public void updateGCMReq(String token){
        cb = new HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO:
            }

            @Override
            public void onSuccess(Response response) {
                Log.d(TAG, response.toString()+" -> update gcm token response");
            }
        };

        try {
            jsonObj = mJsonMethods.getGCMRefreshJson(mSharedPref.getIntFromSP(SystemPreferences.USER_ID), token);
            postRequest(SystemPreferences.UPDATE_GCM_TOKEN, jsonObj, cb);

        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "calendar update failed");
        }
    }

    public void updateCalendarReq(String calEvents){
        cb = new HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO:
            }

            @Override
            public void onSuccess(Response response) {
                Log.d(TAG, response.toString()+" -> update calendar event response");
            }
        };
        try {
            postRequest(SystemPreferences.POST_REGISTER_USER_URL, calEvents, cb);

        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "calendar update failed");
        }
    }

    public void deleteUserCategoryReq(int id){

        cb = new HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO:
            }

            @Override
            public void onSuccess(Response response) {
                Log.d(TAG, response.toString()+" -> delete user category response");

            }
        };
        try {
            jsonObj = mJsonMethods.getUserCategoryJson(mSharedPref.getIntFromSP(SystemPreferences.USER_ID), id);
            postRequest(SystemPreferences.POST_TAG_DELETED, jsonObj, cb);

        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "delete user failed");
        }
    }

    public void registerUserCategoryReq(int id){
        cb = new HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO:
            }

            @Override
            public void onSuccess(Response response) {
                Log.d(TAG, response.body().toString()+" -> register user category response");
                REQ_ITEM_ADD_SUCCEED = true;
            }
        };
        try {
            jsonObj = mJsonMethods.getUserCategoryJson(mSharedPref.getIntFromSP(SystemPreferences.USER_ID), id);
            postRequest(SystemPreferences.POST_TAG_ADDED, jsonObj, cb);

        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "register user category failed");
        }
    }

    public void updateUserInfo(){
        cb = new HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO:
            }

            @Override
            public void onSuccess(Response response) {
                try {

                    Log.d(TAG, response.body().string() + " -> update user response");

                }catch(IOException e){
                    e.printStackTrace();
                }

            }
        };
        try {
            // actual http post request happens here
            jsonObj = mJsonMethods.getUserInfoJsonToUpdate(mSharedPref.getDataForUserInfoUpdate());
            postRequest(SystemPreferences.UPDATE_USER_INFO, jsonObj, cb);

        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "update user failed");
        }
    }

    public void registerUserRequest(){
        cb = new HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO:
            }

            @Override
            public void onSuccess(Response response) {
            try {
                try {
                    String userId = response.body().string();
                    Log.d(TAG, userId + " -> register user response");
                    JSONObject idObj = new JSONObject(userId);
                    Log.i(TAG, idObj.getString("id") + " look at this");
                    mSharedPref.saveInSp(SystemPreferences.USER_ID, Integer.valueOf(idObj.getString("id")));    // store user id
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }catch(IOException e){
                e.printStackTrace();
            }

            }
        };
        try {
            // actual http post request happens here
            jsonObj = mJsonMethods.getUserInfoJson(mSharedPref.getDataForUserRegistration());
            postRequest(SystemPreferences.POST_REGISTER_USER_URL, jsonObj, cb);

        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "register user failed");
        }
    }

    public void registerMacAddrRequest(){
        cb = new HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO:
            }

            @Override
            public void onSuccess(Response response) {
                try {
                    Log.d(TAG, response.body().string() + " -> register mac_address response");

                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        };

        try {
            jsonObj = mJsonMethods.getUserMacAddrInfoJson(mSharedPref.getIntFromSP(SystemPreferences.USER_ID), mSharedPref.getStringFromSP(SystemPreferences.MAC_ADDRESS));
            postRequest(SystemPreferences.POST_USER_MAC_ADDRESS, jsonObj, cb);

        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "register mac address failed");
        }
    }
}
