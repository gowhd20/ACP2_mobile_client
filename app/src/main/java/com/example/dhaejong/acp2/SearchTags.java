package com.example.dhaejong.acp2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.quinny898.library.persistentsearch.SearchBox.SearchListener;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;


public class SearchTags extends ActionBarActivity {

    private SearchBox search;
    private Context context;
    private LocalDB mLocalDB;
    private Tags mTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tags);

        this.mLocalDB = new LocalDB(this);
        this.mTags = new Tags(this, SearchTags.this);
        this.context = this;


        setSearchBar(mTags.getNumberOfTags());
    }
    

    public void setSearchBar(int countTags){

        this.search = (SearchBox) findViewById(R.id.searchbox);
        search.enableVoiceRecognition(this);

        // set list of preview items
        for(int i = 0; i < countTags; i++){
            SearchResult option = new SearchResult(mTags.tags[i], ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.ic_image_black_24dp));
            search.addSearchable(option);
        }
        // menu listener for one at the left top corner
        search.setMenuListener(new SearchBox.MenuListener() {

            @Override
            public void onMenuClick() {
                //Hamburger has been clicked
                Toast.makeText(context, "Menu click", Toast.LENGTH_LONG).show();
            }

        });

        // item listener
        search.setSearchListener(new SearchListener(){

            @Override
            public void onSearchOpened() {
                //Use this to tint the screen
            }

            @Override
            public void onSearchClosed() {
                //Use this to un-tint the screen
            }

            @Override
            public void onSearchTermChanged(String term) {
                //React to the search term changing
                //Called after it has updated results
            }

            @Override
            public void onSearch(String searchTerm) {
                Toast.makeText(SearchTags.this, searchTerm +" Searched", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResultClick(SearchResult result) {
                //React to a result being clicked
                Toast.makeText(SearchTags.this, result +" what the fuck", Toast.LENGTH_LONG).show();

                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.item_selected_popup_window);
                dialog.setTitle("Title...");

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Android custom dialog example!");
                ImageView image = (ImageView) dialog.findViewById(R.id.image);
                image.setImageResource(R.drawable.ic_image_black_24dp);

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }

            @Override
            public void onSearchCleared() {
                //Called when the clear button is clicked
            }

        });

        search.setOverflowMenu(R.menu.overflow_menu);
        search.setOverflowMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            // menu listener at the right top corner
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.test_menu_item:
                        Toast.makeText(SearchTags.this, "Clicked!", Toast.LENGTH_SHORT).show();
                        return true;
                }
                return false;
            }
        });


    }

    public void setSearchListner(){

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
        if (requestCode == SearchBox.VOICE_RECOGNITION_CODE && resultCode == SearchTags.this.RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            for(int i=0; i<matches.size(); i++) {
                search.populateEditText(matches.get(i));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
