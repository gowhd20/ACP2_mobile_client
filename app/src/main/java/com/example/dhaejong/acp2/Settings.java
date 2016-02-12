package com.example.dhaejong.acp2;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quinny898.library.persistentsearch.SearchResult;

import java.util.*;
import java.util.Calendar;


public class Settings extends ActionBarActivity implements View.OnClickListener{

    private String TAG = "Settings";
    //public static List<Tags> tagsList;
    private Context context = this;
    private Tags mTags;
    private static final int GET_TAG_NAME = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Main2Activity.isSettingsActivityActive = true;
        floatingBtn();  // call the adding tag button

        final CheckBox mCheckbox=(CheckBox)findViewById(R.id.checkBoxForCalendar);    // create checkbox
        mCheckbox.setChecked(getFromSP("checkbox"));                            // set checkbox by saved state
        mCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    callDialog();
                } else {
                    saveInSp("checkbox", false);
                    Log.i(TAG, "User remove connection with calendar");
                }

            }
        });
        mTags = new Tags(Settings.this, this);

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

        switch(requestCode) {
            case GET_TAG_NAME:
                if (resultCode == RESULT_OK) {
                    try {
                        int newButtonId = mTags.addTagToInterest(this, data.getExtras().getString("tag_name"));
                        //ImageButton newButton = mTags.addTagToInterest(this, data.getExtras().getString("tag_name"));
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

    protected void callDialog(String tagName, final int buttonId, final int textViewId){

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

    protected void callDialog(){

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
                Intent intent = new Intent(context, Calendar.class);
                startService(intent);
                Log.i(TAG, "am i?");
                // save checkbox state
                saveInSp("checkbox", true);

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
    protected void floatingBtn(){
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

    protected int getTextViewIdByBtnId(int btnId){
        return btnId + Tags.TEXTVIEW_IDENTIFIER;
    }

    protected boolean removeTagsFromView(int btnId, int textViewId){
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

    private boolean getFromSP(String key){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Calendar", android.content.Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }
    private void saveInSp(String key, boolean value){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Calendar", android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


}
