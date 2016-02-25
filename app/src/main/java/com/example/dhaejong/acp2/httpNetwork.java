package com.example.dhaejong.acp2;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "httpNetwork";


    httpNetwork(Context context){
        this.context = context;
        this.client = new OkHttpClient();
    }

    public String getRequest(String url) throws IOException{
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
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

        RequestBody requestBody = RequestBody.create(JSON, new Gson().toJson(json));
        Request request = new Request.Builder()
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

    public void registerUserRequest(){
        SharedPref mSharedPref = new SharedPref(context);
        JsonMethods mJsonMethods = new JsonMethods();
        HttpCallback cb = new HttpCallback() {
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
            JsonObject userInfo = mJsonMethods.getUserInfoJson(mSharedPref.getDataForUserRegistration());
            postRequest(SystemPreferences.POST_REGISTER_USER_URL, userInfo, cb);

        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "failed");
        }
    }

    public void registerMacAddrRequest(){
        SharedPref mSharedPref = new SharedPref(context);
        JsonMethods mJsonMethods = new JsonMethods();
        HttpCallback cb = new HttpCallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO:
            }

            @Override
            public void onSuccess(Response response) {
                Log.d(TAG, response.toString()+" -> register mac_address response");
                // TODO: save user id in sharedpref
            }
        };

        try {
            //JsonObject userInfo = mJsonMethods.getUserMacAddrInfoJson(//user_id//, mSharedPref.getFromSP(SystemPreferences.MAC_ADDRESS))
            //postRequest(SystemPreferences.POST_REGISTER_USER, userInfo, cb);

        }catch(Exception e){
            e.printStackTrace();
            Log.e(TAG, "failed");
        }
    }
}
