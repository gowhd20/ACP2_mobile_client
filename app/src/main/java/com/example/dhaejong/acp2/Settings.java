package com.example.dhaejong.acp2;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


public class Settings extends ActionBarActivity implements View.OnClickListener{

    private String TAG = "Settings";
    //public static List<Tags> tagsList;
    private Context context;
    private int countTags = 0;
    private Tags mTags;
    private ImageButton newButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        floatingBtn();
        this.context = this;

        mTags = new Tags();
        mTags.Tags(Settings.this, this);
        newButton = mTags.addTag(this, "Science");
        newButton.setOnClickListener(this);
        int a = newButton.getId();
        newButton = mTags.addTag(this, "Party");
        newButton.setOnClickListener(this);
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

                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void floatingBtn(){
        android.support.design.widget.FloatingActionButton fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(context, SearchTags.class);
                startActivity(intent);
                Log.i(TAG, "action button clicked");
            }
        });
    }

    // tag add button clicked

    @Override
    public void onClick(View view){
        Log.i(TAG, String.valueOf(view.getId()));
        switch(view.getId()){
        }
        Log.i(TAG, "button clicked");

    }

}
