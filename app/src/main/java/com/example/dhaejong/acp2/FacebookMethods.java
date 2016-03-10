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
                                    Object nameObj = jsonObject.get("name");
                                    Object emailObj = jsonObject.get("email");
                                    Object facebookIdObj = jsonObject.get("id");
                                    Log.d(TAG, emailObj.toString());
                                    SharedPref mSharedPref = new SharedPref(context);
                                    mSharedPref.saveInSp(SystemPreferences.EMAIL, emailObj.toString());             // store user email to sharedpreference
                                    mSharedPref.saveInSp(SystemPreferences.FACEBOOK_ID, facebookIdObj.toString());  // store user facebook id ...
                                    mSharedPref.saveInSp(SystemPreferences.USER_NAME, nameObj.toString());          // store user name ...

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
                //TODO: need to make smarter user info update!!!!!!!!!!!!!! ex) if any changes in user info
                HttpRequests mHttpReq = new HttpRequests(context, 0, SystemPreferences.UPDATE_USER);        // flag 5 -> update user info request
                mHttpReq.run();
            }

        });
        batch.executeAsync();

    }




}
