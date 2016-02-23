package com.example.dhaejong.acp2;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;

import org.json.JSONObject;

/**
 * Created by dhaejong on 13.2.2016.
 */
public class FacebookMethods {

    private static final String TAG = "FacebookMethods";
    private GraphRequestBatch batch;
    Context context;

    FacebookMethods(Context context){
        this.context = context;
    }

    public void queryFbData(){

        getBatches();
        GraphRequest request_me = batch.get(0);
        Bundle readmeParams = new Bundle();
        readmeParams.putString("fields", "id, name, email, birthday");
        request_me.setParameters(readmeParams);

    }

    private void getBatches() {
        batch = new GraphRequestBatch(
                GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                                try {
                                    Log.d(TAG, response.toString());
                                    Object obj = jsonObject.get("email");
                                    Log.d(TAG, obj.toString());
                                    SharedPref mSharedPref = new SharedPref(context);
                                    mSharedPref.saveInSp("user_email", obj.toString());             // store user email to sharedpreference

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // Application code for user
                            }
                        })

        );
        batch.addCallback(new GraphRequestBatch.Callback() {
            @Override
            public void onBatchCompleted(GraphRequestBatch graphRequests) {

            }

        });
        batch.executeAsync();

    }




}
