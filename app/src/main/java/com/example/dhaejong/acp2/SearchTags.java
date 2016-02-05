package com.example.dhaejong.acp2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;


public class SearchTags extends ActionBarActivity {

    SearchBox search;
    Activity mSearchTags;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tags);


        mSearchTags = SearchTags.this;
        this.context = this;

        search = (SearchBox) findViewById(R.id.searchbox);
        search.enableVoiceRecognition(this);


        for(int x = 0; x < 10; x++){
            SearchResult option = new SearchResult("Result " + Integer.toString(x), ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.ic_image_black_24dp));
            search.addSearchable(option);
        }
        search.setMenuListener(new SearchBox.MenuListener(){

            @Override
            public void onMenuClick() {
                //Hamburger has been clicked
                Toast.makeText(mSearchTags, "Menu click", Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_tags, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchBox.VOICE_RECOGNITION_CODE && resultCode == mSearchTags.RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for(int i=0; i<matches.size(); i++) {
                search.populateEditText(matches.get(i));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
