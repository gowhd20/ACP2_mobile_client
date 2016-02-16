package com.example.dhaejong.acp2;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

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
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Settings extends ActionBarActivity implements View.OnClickListener{

    private String TAG = "Settings";
    //public static List<Tags> tagsList;
    private Context context = this;
    private Tags mTags;
    private static final int GET_TAG_NAME = 0;
    SharedPref mSharedPreference;
    private AccessToken access_token;
    CallbackManager callbackManager;





    private void callDialog(String tagName, final int buttonId, final int textViewId){

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
        dialogButton.setTag(tagName);
        dialogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                removeTagsFromView(buttonId, textViewId);
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
                Intent intent = new Intent(context, CalendarService.class);
                context.startService(intent);
                // save checkbox state
                mSharedPreference.saveInSp("checkbox", true);

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
                /*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(context, SearchTags.class);
                startActivityForResult(intent, GET_TAG_NAME);

                Log.i(TAG, "action button clicked");
            }
        });
    }


    private int getTextViewIdByBtnId(int btnId){
        return btnId + Tags.TEXTVIEW_IDENTIFIER;
    }

    private void initSettingsActivity(){
        ArrayList<String> tagList;
        tagList = mTags.mLocalDB.getAllTagNames();  // get all tag names from local db
        if(!tagList.isEmpty()) {
            for (int i = 0; i < tagList.size(); i++) {
                int initBtnId = mTags.initAddTagToInterest(this, tagList.get(i));
                Log.i(TAG, "tag id: " + Integer.toString(initBtnId));
                ImageButton newButton = (ImageButton) findViewById(initBtnId);
                newButton.setOnClickListener(this);
            }
        }else{
            Log.i(TAG, "Local db is empty");
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
        Main2Activity.isSettingsActivityActive = true;
        mSharedPreference = new SharedPref(this);   // sharedpreference class
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

        mCheckbox.setChecked(mSharedPreference.getFromSP("checkbox"));                            // set checkbox by saved state
        mCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    callDialog();
                } else {
                    mSharedPreference.saveInSp("checkbox", false);
                    Intent intent = new Intent(context, CalendarService.class);
                    context.stopService(intent);
                    Log.i(TAG, "User remove connection with calendar");
                }

            }
        });

        // facebook login
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "public_profile", "user_friends", "user_posts");
        loginButton.setReadPermissions(permissionNeeds);


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                access_token = loginResult.getAccessToken();

                Log.i(TAG, "Content User ID: " + loginResult.getAccessToken().getUserId() + "\n" + "Auth Token: " + access_token.getToken());

            }

            @Override
            public void onCancel() {
                //"If login attempt canceled.";
            }

            @Override
            public void onError(FacebookException e) {
                //"If login attempt Failed.";
            }
        });


        mTags = new Tags(Settings.this, this);
        initSettingsActivity();         // init tag names of previously added

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Main2Activity.isSettingsActivityActive = false;
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

                if(!Main2Activity.isSettingsActivityActive) {
                    Intent intent = new Intent(this, Settings.class);
                    Main2Activity.isSettingsActivityActive = true;
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

        // resource exists
        if(checkExistence != 0){
            try {
                // delete item from view
                TextView mTxtView = (TextView) findViewById(getTextViewIdByBtnId(selectedResId));
                Log.i(TAG, "Resource exist and also textview " + mTxtView.getId() + " is clicked to delete");
                callDialog(mTxtView.getText().toString(), selectedResId, mTxtView.getId());

                // delete item from local db
                mTags.mLocalDB.deleteTag(selectedResId);
                Log.i(TAG, "selected item has deleted from local db");

            }catch(Exception e){
                Log.i(TAG, "something went wrong with getting resource");
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case GET_TAG_NAME:
                if (resultCode == RESULT_OK) {
                    try {
                        int newButtonId = mTags.addTagToInterest(this, data.getExtras().getString("tag_name"));
                        Log.i(TAG, "tag id: " + Integer.toString(newButtonId));
                        ImageButton newButton = (ImageButton) findViewById(newButtonId);
                        newButton.setOnClickListener(this);
                    }catch(Exception e){
                        Log.i(TAG, "can't find resource for the tag");
                    }

                }
                break;
        }
    }



}
