package com.example.dhaejong.acp2;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.ArrayList;
import java.util.MissingResourceException;

/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private static final String REFRESH_ACTION = "android.intent.action.REFRESH_ACTION";

    ArrayList<String> eventArr = new ArrayList<>();
    LocalDB mLocalDB;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.d(TAG, data.toString());

        String message = data.getString("message");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        if(data.getString("collapse_key").equals(SystemPreferences.DATA_NOT_COLLAPSED)){
            /** Respect order of inserting data into db!!
            *  id
            *  title
            *  categories
            *  description
            *  address
            *  price
            *  image url
            *  start time
            *  end time         **/

            try{
                eventArr.add(data.getString("id"));
            } catch(MissingResourceException e) {
                e.printStackTrace();
                eventArr.add("null");
            }
            try{
                eventArr.add(data.getString("title"));
            } catch(MissingResourceException e) {
                e.printStackTrace();
                eventArr.add("null");
            }
            try{
                eventArr.add(data.getString("categories"));
            } catch(MissingResourceException e) {
                e.printStackTrace();
                eventArr.add("null");
            }
            try{
                eventArr.add(data.getString("description"));
            } catch(MissingResourceException e) {
                e.printStackTrace();
                eventArr.add("null");
            }
            try{
                eventArr.add(data.getString("address"));
            } catch(MissingResourceException e) {
                e.printStackTrace();
                eventArr.add("null");
            }
            try{
                eventArr.add(data.getString("price"));
            } catch(MissingResourceException e) {
                e.printStackTrace();
                eventArr.add("null");
            }
            try{
                eventArr.add(data.getString("image_url"));
            } catch(MissingResourceException e) {
                e.printStackTrace();
                eventArr.add("null");
            }
            try{
                eventArr.add(data.getString("start_timestamp"));
            } catch(MissingResourceException e) {
                e.printStackTrace();
                eventArr.add("null");
            }
            try{
                eventArr.add(data.getString("end_timestamp"));
            } catch(MissingResourceException e) {
                e.printStackTrace();
                eventArr.add("null");
            }

            Log.d(TAG, eventArr.toString());
            // store into local db
            storeReceivedEvent();

        }else{
            Log.e(TAG, "data server pushed has collapsed");
        }

        // send refresh action to event listview activity
        Intent refreshIntent = new Intent(REFRESH_ACTION);
        sendBroadcast(refreshIntent);

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        try {
            sendNotification(message, data.getString("title"));
        }catch(Exception e){
            e.printStackTrace();
        }
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message, String title) {
        Intent intent = new Intent(this, Main2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void storeReceivedEvent(){
        mLocalDB = new LocalDB(this);
        // check possible duplication by event id
        if(!mLocalDB.checkDuplicated(eventArr.get(0), LocalDB.DATABASE_TABLE_NAME_EVENTS)){
            mLocalDB.addNewEvent(eventArr);
        }else{
            sendNotification("This item is already in your history!!", eventArr.get(1));
        }

    }


}
