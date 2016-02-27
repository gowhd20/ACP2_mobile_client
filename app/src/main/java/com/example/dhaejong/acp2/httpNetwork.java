package com.example.dhaejong.acp2;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;

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

    public void postRequest(String url, JsonObject json, final HttpCallback cb) throws IOException {
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
            //JsonObject categoryObj = mJsonMethods.getUserCategoryJson(SystemPreferences.USER_ID, id);
            //postRequest(SystemPreferences.POST_REGISTER_USER_URL, categoryObj, cb);

        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "delete user failed");
        }
    }

    public void updateCalendarReq(JsonArray calEvents){
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
        JsonElement test = calEvents.get(0);
        JsonObject tes2 = test.getAsJsonObject();

        Log.d(TAG, tes2.toString());
        try {
            //postRequest(SystemPreferences.POST_REGISTER_USER_URL, calEvents.getAsJsonObject(), cb);

        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "calendar update failed");
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
                Log.d(TAG, response.toString()+" -> register user category response");
            }
        };
        try {
            //JsonObject categoryObj = mJsonMethods.getUserCategoryJson(mSharedPref.getDataForUserRegistration());
            //postRequest(SystemPreferences.POST_REGISTER_USER_URL, categoryObj, cb);

        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "register user category failed");
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
                Log.d(TAG, response.toString()+" -> register user response");
                // TODO: save user id in sharedpref
            }
        };
        try {
            // actual http post request happens here
            JsonObject userInfo = mJsonMethods.getUserInfoJson(mSharedPref.getDataForUserRegistration());
            postRequest(SystemPreferences.POST_REGISTER_USER_URL, userInfo, cb);

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
                Log.d(TAG, response.toString()+" -> register mac_address response");
            }
        };

        try {
            //JsonObject userInfo = mJsonMethods.getUserMacAddrInfoJson(//user_id//, mSharedPref.getFromSP(SystemPreferences.MAC_ADDRESS))
            //postRequest(SystemPreferences.POST_REGISTER_USER, userInfo, cb);

        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "register mac address failed");
        }
    }
}
