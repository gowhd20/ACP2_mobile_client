package com.example.dhaejong.acp2;

import android.content.Context;
import android.content.Intent;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private String TAG = "MainActivity";
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View mainView = findViewById(R.id.mainActivity);
        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "screen Clicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, Settings.class);
                startActivity(intent);

            }
        });

        // TODO: show a notification checking if it's right person when mobile receives events info

        Tags mTags = new Tags(MainActivity.this, this);
        EnableGcm mGcm = new EnableGcm(context, MainActivity.this);
        mGcm.initGcm();                                                      // set all services of gcm


        if(mTags.countTagsInList != 0 ){
            // not first time use
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
        }


        /*
        //mTags.mLocalDB.deleteTable("Tags");
        ArrayList<String> list = new ArrayList<>();
        list.add("9");
        list.add("tivoli hot bitch party");
        list.add("2");
        list.add("");
        list.add("");
        list.add("As the weather is getting warmer, the streets are icy and the snow is falling from the roofs on your head, you know: " +
                "Summer is almost here! To accelerate the process a bit, we are already throwing you a beach party. So bring your beach toys, " +
                "leave your winter clothes at home and be ready to feel the heat on your bum and the sand between your toes. As always, " +
                "with more than student friendly prices, wink wink! ");
        list.add("Tivoli");
        list.add("2€ in advance, 4€ from the door, free entry with ESN Card");
        list.add("https://www.facebook.com/events/1662196667386875/");
        list.add("2016-02-18 22:00:00");
        list.add("");

        boolean result = mTags.mLocalDB.addNewEvent(list);
        if(result){
            list = mTags.mLocalDB.getAllItems(9, LocalDB.DATABASE_TABLE_NAME_EVENTS);
            Log.d(TAG, list.toString());

        }else{
            Log.e(TAG, "wrong");
        }*/

        //list = mTags.mLocalDB.getAllItems(10, LocalDB.DATABASE_TABLE_NAME_EVENTS);
        //Log.d(TAG, list.toString());



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                if(!Main2Activity.isSettingsActivityActive) {
                    Intent intent = new Intent(this, Settings.class);
                    Main2Activity.isSettingsActivityActive = true;
                    Log.i(TAG, "Settings.java is starting");
                    startActivity(intent);
                    return true;
                }else{
                    Toast.makeText(MainActivity.this," Settings.java is already running", Toast.LENGTH_LONG).show();
                    return false;
                }
            case R.id.title_activity_main2:
                Intent intent = new Intent(this, Main2Activity.class);
                startActivity(intent);
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    public void onResume(){
        super.onResume();
    }



}
