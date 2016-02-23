package com.example.dhaejong.acp2;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Main2Activity extends ActionBarActivity {

    private String TAG = "Main2Activity";
    public static boolean isSettingsActivityActive = false;
    private Tags mTags;

    public ArrayList<String> ids = new ArrayList<>();
    protected ArrayList<String> titles = new ArrayList<>();

    Context context;

    private void initListView(){
        // query list of titles and ids from local db
        final ArrayList<String> contentTitleList = mTags.mLocalDB.getAllEventTitles();

        ArrayList<MetaDataForEvents> tempArray;
        tempArray = mTags.mLocalDB.getMetaDataForEvents();

        // store ids and titles locally, to avoid frequent query to database
        for(MetaDataForEvents mMetaDataForEvents : tempArray ){
            ids.add(mMetaDataForEvents.id);
            titles.add(mMetaDataForEvents.title);
        }

        final ListView listview = (ListView) findViewById(R.id.listview);
        final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, contentTitleList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Log.d(TAG, view.toString());
                Object test = parent.getItemAtPosition(position);
                String testTxt = test.toString();

                for(int i=0; i<ids.size(); i++){
                    if(titles.get(i).equals(testTxt)){
                        Intent contentIntent = new Intent(context, Events.class);
                        contentIntent.putExtra("id", ids.get(i));
                        contentIntent.putExtra("title", titles.get(i));

                        startActivity(contentIntent);
                        return;
                    }
                }


                //Log.d(TAG, "selected id is " + selectedItem.);
                //t = mTags.mLocalDB.getAllItemsById(view.getId(), LocalDB.DATABASE_TABLE_NAME_EVENTS);
                //Log.d(TAG, returnVal);
                //selectedItem.setTag();


               // }catch(Exception e){
               //     Log.e(TAG, "failed to find item according to the item's id");
               // }

                /*final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                //list.remove(item);
                                //adapter.notifyDataSetChanged();
                                //view.setAlpha(1);
                                Intent viewContents = new Intent(context, Events.class);
                                startActivity(viewContents);

                            }
                        });*/

            }

        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mTags = new Tags(Main2Activity.this, this);

        this.context = this;


        initListView();

    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);

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

                if(!isSettingsActivityActive) {
                    Intent intent = new Intent(this, Settings.class);
                    Main2Activity.isSettingsActivityActive = true;
                    Log.i(TAG, "Settings.java is starting");
                    startActivity(intent);
                    return true;
                }else{
                    Toast.makeText(Main2Activity.this, " Settings.java is already running", Toast.LENGTH_LONG).show();
                    return false;
                }

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
