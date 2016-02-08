package com.example.dhaejong.acp2;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quinny898.library.persistentsearch.SearchResult;


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
        floatingBtn();

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

        int checkExistence = this.getResources().getIdentifier(Integer.toString(selectedResId), "id", this.getPackageName());

        // resource exists
        if(checkExistence != 0){
            try {
                TextView mTxtView = (TextView) findViewById(getTextViewIdByBtnId(selectedResId));
                Log.i(TAG, "also textview " + mTxtView.getId() + " is clicked");
                callDialog(mTxtView.getText().toString(), selectedResId, getTextViewIdByBtnId(selectedResId));
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
                    ImageButton newButton;
                    newButton = mTags.addTagToInterest(this, data.getExtras().getString("tag_name"));
                    newButton.setOnClickListener(this);
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
                removeTags(buttonId, textViewId);
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
        return Integer.valueOf(Integer.toString(btnId)+Integer.toString(btnId)+Integer.toString(btnId));
    }

    protected boolean removeTags(int btnId, int textViewId){
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


}
