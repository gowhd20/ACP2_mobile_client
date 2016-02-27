package com.example.dhaejong.acp2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;
import com.quinny898.library.persistentsearch.SearchBox.SearchListener;


public class SearchTags extends ActionBarActivity {

    SearchBox search;
    Tags mTags;

    Context context;
    private static final String TAG = "SearchTags";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tags);
        mTags = new Tags(this, SearchTags.this);
        context = this;

        setSearchBar(mTags.getNumberOfTags());
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
            if(!SystemPreferences.IS_SETTINGS_ACTIVITY_ACTIVE) {
                Intent intent = new Intent(this, Settings.class);
                SystemPreferences.IS_SETTINGS_ACTIVITY_ACTIVE = true;
                Log.i(TAG, "Settings.java is starting");
                startActivity(intent);
                return true;
            }else{
                Toast.makeText(SearchTags.this," Settings.java is already running", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    protected void callDialog(SearchResult tagName){

        // custom dialog
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.item_selected_popup_window);
        dialog.setTitle("Adding " + tagName + " tag");

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Would you like to add '" + tagName + "' Tag to your tag list?");
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.drawable.ic_image_black_24dp);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNo);
        dialogButton.setTag(tagName);
        dialogButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // params to send back to Settings activity
                String selectedItem = v.getTag().toString();
                Log.d(TAG,mTags.mCategory.toString());
                int selectedId = mTags.getIdOfCategory(mTags.mCategory.getCategories(), selectedItem);
                if(selectedId != 0){
                    sendBackTagName(selectedItem, selectedId);
                }else{
                    Log.e(TAG, "failed to find id of selected tag");
                }
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

    public void setSearchBar(int countTags){

        this.search = (SearchBox) findViewById(R.id.searchbox);
        search.enableVoiceRecognition(this);
        // if network is available to fetch the category list
        if(!mTags.tagNames.isEmpty()) {
            // set list of preview items
            for (int i = 0; i < countTags; i++) {
                SearchResult option = new SearchResult(mTags.tagNames.get(i),          //categoriesJsonArr.get(i).getAsJsonObject().get("category").toString(),
                        ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.ic_image_black_24dp));
                search.addSearchable(option);
            }
        }else{
            SearchResult option = new SearchResult(null,          //categoriesJsonArr.get(i).getAsJsonObject().get("category").toString(),
                    ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.ic_image_black_24dp));
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
        search.setSearchListener(new SearchListener() {

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
                Toast.makeText(SearchTags.this, searchTerm + " Searched", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResultClick(SearchResult result) {
                callDialog(result);

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

    protected void sendBackTagName(String tagName, int tagId){
        Bundle tag = new Bundle();
        tag.putString("tag_name", tagName);
        tag.putInt("tag_id", tagId);
        Intent intent = new Intent();
        intent.putExtras(tag);

        setResult(RESULT_OK, intent);
        finish();
    }


}
