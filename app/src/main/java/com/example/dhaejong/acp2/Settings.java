package com.example.dhaejong.acp2;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Settings extends ActionBarActivity implements View.OnClickListener{

    private static final String TAG = "Settings";
    private Tags mTags;
    private static final int GET_TAG_NAME = 0;
    private AccessToken access_token;

    Context context = this;
    SharedPref mSharedPreference;
    CallbackManager callbackManager;
    FacebookMethods mFacebookMethods;
    HttpRequests mHttpReq;

    private void callDialog(final String tagName, final int buttonId, final int textViewId){

        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.item_selected_popup_window);
        dialog.setTitle("Removing " + tagName + " tag");

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Would you like to remove '" + tagName + "' Tag from your tag list?");
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_image_black_24dp);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNo);
        dialogButton.setTag(tagName);
        dialogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //removeTagsFromView(buttonId, textViewId);
                //deleteCategoryItemFromDb(buttonId, tagName);
                BackgroundTask deleteItemTask =
                        new BackgroundTask(Settings.this, buttonId, textViewId, tagName);
                deleteItemTask.execute();
                dialog.dismiss();
            }
        });

        dialogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // no selected
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void callDialog(){

        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.item_selected_popup_window);
        dialog.setTitle("Calendar data");

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Would you like to connect your schedule in the Calendar?");
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_image_black_24dp);

        Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
        Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNo);

        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // call connecting calender methods
                //Intent calQueryIntent = new Intent(context, CalendarService.class);
                //context.startService(calQueryIntent);
                // service to observe changes in calendar
                Intent calObserverIntent = new Intent(context, CalendarUpdatedService.class);
                context.startService(calObserverIntent);

                // save checkbox state
                mSharedPreference.saveInSp(SystemPreferences.CHECKBOX, true);

                dialog.dismiss();

            }
        });

        dialogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // no selected
                int checkboxId = context.getResources().getIdentifier("checkBoxForCalendar", "id", context.getPackageName());
                if(checkboxId != 0){
                    CheckBox mCheckbox = (CheckBox)findViewById(checkboxId);
                    mCheckbox.setChecked(false);
                }else{
                    Log.i(TAG, "checkbox doesn't exist");
                }
                dialog.dismiss();

            }
        });

        dialog.show();
    }

    // tag add button clicked
    private void floatingBtn(){
        android.support.design.widget.FloatingActionButton fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread networkChecking = new Thread() {
                    @Override
                    public void run() {
                        checkNetworkState();
                    }
                };
                networkChecking.start();
                if(httpService.NETWORK_AVAILABLE){
                    Intent intent = new Intent(context, SearchTags.class);
                    startActivityForResult(intent, GET_TAG_NAME);
                }else{
                    Toast.makeText(context, "No internet network is available", Toast.LENGTH_SHORT).show();
                }

                /*if(mTags.tagNames.isEmpty()){
                    Toast.makeText(context, "No internet network is available", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(context, SearchTags.class);
                    startActivityForResult(intent, GET_TAG_NAME);

                    Log.i(TAG, "action button clicked");
                }*/
            }
        });
    }

    private int getTextViewIdByBtnId(int btnId){
        return btnId + SystemPreferences.TEXTVIEW_IDENTIFIER;
    }

    private void initSettingsActivity(){
        // get all tag ids from db
        ArrayList<String> ids = mTags.initAddTagToInterest(this);

        for (int i=0; i < ids.size(); i++) {
            Log.i(TAG, "tag id: " + ids.get(i));
            ImageButton newButton = (ImageButton) findViewById(Integer.valueOf(ids.get(i)));
            newButton.setOnClickListener(this);
        }
    }

    private boolean removeTagsFromView(int btnId, int textViewId){
        try {
            ImageButton mBtn = (ImageButton) findViewById(btnId);
            TextView mTxtView = (TextView) findViewById(textViewId);
            ViewGroup layout = (ViewGroup) mBtn.getParent();
            layout.removeView(mBtn);
            layout.removeView(mTxtView);
            // reduce the count of tags in the list
            mTags.countTagsInList--;
            return true;
        }catch(Exception e){
            Log.i(TAG, "removing item failed");
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());    // this should come before setContentView
        setContentView(R.layout.activity_settings);
        SystemPreferences.IS_SETTINGS_ACTIVITY_ACTIVE = true;
        mSharedPreference = new SharedPref(this);                   // sharedpreference class
        mFacebookMethods = new FacebookMethods(this);

        //    mSharedPreference.saveInSp(SystemPreferences.MAC_ADDR_REGISTERED, true);
        //    mHttpReq = new HttpRequests(this, 0, SystemPreferences.REGISTER_MAC_ADDRESS); // flag 3 -> register mac address
        //    mHttpReq.run();

        floatingBtn();  // call the adding tag button

        final CheckBox mCheckbox=(CheckBox)findViewById(R.id.checkBoxForCalendar);    // create checkbox
        mCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "Check box " + arg0.getText().toString() + " is " + String.valueOf(arg1), Toast.LENGTH_LONG).show();

                switch (String.valueOf(arg1)) {
                    case "false":
                        break;
                    case "true":
                        // check box clicked, connect calendar

                        break;
                }

            }
        });

        mCheckbox.setChecked(mSharedPreference.getFromSP(SystemPreferences.CHECKBOX));           // init checkbox by saved state
        mCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    callDialog();
                } else {
                    mSharedPreference.saveInSp(SystemPreferences.CHECKBOX, false);
                    // unchecked checkbox, stop the calendar related services
                    Intent calQueryIntent = new Intent(context, CalendarService.class);
                    context.stopService(calQueryIntent);
                    Intent calObserverIntent = new Intent(context, CalendarUpdatedService.class);
                    context.stopService(calObserverIntent);
                    Log.i(TAG, "User remove connection with calendar");
                }

            }
        });

        // facebook login
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        List<String> permissionNeeds = Arrays.asList("basic_info", "user_photos", "email", "user_birthday", "public_profile", "user_friends", "user_posts");
        loginButton.setReadPermissions(permissionNeeds);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            // TODO: need to do something if user disconnect facebook
            @Override
            public void onSuccess(LoginResult loginResult) {
                access_token = loginResult.getAccessToken();
                SharedPref mSharedPref = new SharedPref(context);
                mSharedPref.saveInSp(SystemPreferences.FACEBOOK_TOKEN, access_token.getToken());    //  save/update facebook token
                mFacebookMethods.queryFbData();             // query facebook data that will be stored in shared preference
                Log.d(TAG, "Content User ID: " + loginResult.getAccessToken().getUserId() + "\n" + "Auth Token: " + access_token.getToken());

            }

            @Override
            public void onCancel() {
                //"If login attempt canceled.";
            }

            @Override
            public void onError(FacebookException e) {
                //"If login attempt Failed.";
                Toast.makeText(context, "No internet network is available", Toast.LENGTH_SHORT).show();
            }
        });

        mTags = new Tags(Settings.this, this);
        initSettingsActivity();         // init tag names of previously added

    }

    protected boolean checkNetworkState(){
        httpNetwork mHttpNetwork = new httpNetwork(this);

        try {
            String response = mHttpNetwork.getRequest(SystemPreferences.GET_CATEGORIES_URL);
            if(response != null) {
                int categoryId = mHttpNetwork.getIdOfCategory(response);
                Log.d(TAG, Integer.toString(categoryId));
                Log.d(TAG, response);

                // store category names
                CategoryList mCategory = new CategoryList();
                mCategory.setCategories(response);
                httpService.NETWORK_AVAILABLE = true;
                // save id of category in sharedpreference for future post
                mSharedPreference = new SharedPref(this);
                mSharedPreference.saveInSp(SystemPreferences.CATEGORY_LIST, categoryId);
                return true;
            }else{
                httpService.NETWORK_AVAILABLE = false;
                return false;
            }
            //Log.d(TAG, response);
        }catch(IOException e){
            e.printStackTrace();
            Log.e(TAG, "get failed");
            httpService.NETWORK_AVAILABLE = false;
            return false;
        }
    }

    public void deleteCategoryItemFromDb(int resId, String tagName){
        // delete item from local db
        mTags.mLocalDB.deleteTag(resId);
        // notify server delete of user's category item
        int selectedId = mTags.getIdOfCategory(mTags.mCategory.getCategories(), tagName);
        mHttpReq = new HttpRequests(this, selectedId, SystemPreferences.DELETE_CATEGORY); // flag 2 -> deleted tag request
        mHttpReq.run();

        Log.i(TAG, "selected item has deleted from local db");
        mTags.mLocalDB.close();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        SystemPreferences.IS_SETTINGS_ACTIVITY_ACTIVE = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...

                if(!SystemPreferences.IS_SETTINGS_ACTIVITY_ACTIVE) {
                    Intent intent = new Intent(this, Settings.class);
                    SystemPreferences.IS_SETTINGS_ACTIVITY_ACTIVE = true;
                    Log.i(TAG, "Settings.java is starting");
                    startActivity(intent);
                    return true;
                }else{
                    Toast.makeText(Settings.this, " Settings.java is already running", Toast.LENGTH_LONG).show();
                    return false;
                }

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onClick(View view){
        int selectedResId = view.getId();
        Log.i(TAG, Integer.toString(view.getId()));
        int checkExistence = this.getResources().getIdentifier(Integer.toString(selectedResId), "id", this.getPackageName());

        // TODO: this is temporal method to prevent user from removing tags without internet connection however
        // TODO: this will need fundamental solution ex) remove only response from server is true

        if (checkExistence != 0) {
            try {
                // delete item from view
                TextView mTxtView = (TextView) findViewById(getTextViewIdByBtnId(selectedResId));
                Log.i(TAG, "Resource exists and with textview " + mTxtView.getId() + " are clicked to delete");
                callDialog(mTxtView.getText().toString(), selectedResId, mTxtView.getId());

            } catch (Exception e) {
                Log.i(TAG, "something went wrong with getting resource");
            }
        }else{
            Toast.makeText(context, "System couldn't find selected item in the local space", Toast.LENGTH_SHORT).show();
        }
       /* if(httpService.NETWORK_AVAILABLE){

        //if(mTags.ifHasCategories()) {
            if (checkExistence != 0) {  // resource exists
                try {
                    // delete item from view
                    TextView mTxtView = (TextView) findViewById(getTextViewIdByBtnId(selectedResId));
                    Log.i(TAG, "Resource exists and with textview " + mTxtView.getId() + " are clicked to delete");
                    callDialog(mTxtView.getText().toString(), selectedResId, mTxtView.getId());

                } catch (Exception e) {
                    Log.i(TAG, "something went wrong with getting resource");
                }
            }
        }else{
            Toast.makeText(context, "No internet network is available", Toast.LENGTH_SHORT).show();
        }*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case GET_TAG_NAME:
                // item from search list selected
                if (resultCode == RESULT_OK) {
                    int newButtonId=0;
                    // TODO: if server fails to add user's tag, should remove from the view
                    try {
                        newButtonId = mTags.addTagToInterest(this, data.getExtras().getString("tag_name"));
                        Log.i(TAG, "tag id: " + Integer.toString(newButtonId));
                        ImageButton newButton = (ImageButton) findViewById(newButtonId);
                        newButton.setOnClickListener(this);
                    }catch(Exception e) {
                        Log.i(TAG, "can't find resource for the tag");
                    }

                    if(newButtonId != 0) {
                        mHttpReq = new HttpRequests(this, mTags.getIdOfCategory(mTags.mCategory.getCategories(),
                                data.getExtras().getString("tag_name")), SystemPreferences.REGISTER_CATEOGORY);        // flag 1 -> tag added request
                        mHttpReq.run();
                    }else{
                        Log.e(TAG, "failed to get the id of new added button");
                    }
                }
                break;
        }
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        int buttonId;
        int textViewId;
        String tagName;

        public BackgroundTask(Settings activity, int btnId, int txtViewId, String name) {
            dialog = new ProgressDialog(activity);
            buttonId = btnId;
            textViewId = txtViewId;
            tagName = name;
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Sending request...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(httpService.NETWORK_AVAILABLE){
                removeTagsFromView(buttonId, textViewId);
                deleteCategoryItemFromDb(buttonId, tagName);
            }else{
                Toast.makeText(context, "Network is not available or server is not responding", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            Thread networkChecking = new Thread() {
                @Override
                public void run() {
                    checkNetworkState();
                }
            };
            networkChecking.start();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

    }


}
