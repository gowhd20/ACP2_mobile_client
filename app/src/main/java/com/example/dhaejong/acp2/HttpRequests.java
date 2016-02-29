package com.example.dhaejong.acp2;

import android.content.Context;
import android.util.Log;

/**
 * Created by haejong on 29/02/2016.
 */
public class HttpRequests implements Runnable {

    private static final String TAG = "HttpRequests";
    Object data;
    int flag;
    Context context;
    HttpRequests(Context context, Object data, int flag){this.data=data; this.flag=flag; this.context=context;}
    public void run(){
        httpNetwork mHttpNet;
        switch(flag) {
            case 1:
                try{
                    mHttpNet = new httpNetwork(context);
                    mHttpNet.registerUserCategoryReq((int)data);
                }catch(Exception e){
                    e.printStackTrace();
                    Log.e(TAG, "notifying server of added item failed");
                }
                break;
            case 2:
                try{
                    mHttpNet = new httpNetwork(context);
                    mHttpNet.deleteUserCategoryReq((int)data);
                }catch(Exception e){
                    e.printStackTrace();
                    Log.e(TAG, "notifying server of deleted item failed");
                }
                break;
            case 3:
                try {
                    mHttpNet = new httpNetwork(context);
                    mHttpNet.registerMacAddrRequest();
                }catch(Exception e){
                    e.printStackTrace();
                    Log.e(TAG, "failed calling registerMacAddrRequest()");
                }
                break;
            case 4:
                try {
                    // register user info to the server
                    mHttpNet = new httpNetwork(context);
                    mHttpNet.registerUserRequest();
                }catch(Exception e){
                    e.printStackTrace();
                    Log.e(TAG, "failed calling registerUserRequest()");
                }

                break;
            case 5:
                try{
                    mHttpNet = new httpNetwork(context);
                    mHttpNet.updateUserInfo();
                }catch(Exception e){
                    e.printStackTrace();
                    Log.e(TAG, "update user info has failed");
                }
                break;
            case 6:
                try{
                    mHttpNet = new httpNetwork(context);
                    mHttpNet.updateGCMReq(data.toString());
                }catch(Exception e){
                    e.printStackTrace();
                    Log.e(TAG, "update gcm token has failed");
                }
            default:
                break;
        }


    }


}
