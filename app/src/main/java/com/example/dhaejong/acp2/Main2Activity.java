package com.example.dhaejong.acp2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Main2Activity extends ActionBarActivity {

    private String TAG = "Main2Activity";
    private Tags mTags;

    protected ArrayList<String> ids = new ArrayList<>();
    protected ArrayList<String> titles = new ArrayList<>();

    Context context;

    private void initListView(){
        // query list of titles and ids from local db
        final ArrayList<String> contentTitleList = mTags.mLocalDB.getAllEventTitles();

        ArrayList<Metadata> tempArray;
        tempArray = mTags.mLocalDB.getMetaDataForEvents();

        // store ids and titles locally, to avoid frequent query to database
        for(Metadata mMeta : tempArray ){
            ids.add(mMeta.id);
            titles.add(mMeta.title);
            Log.d(TAG, ids.toString());
            Log.d(TAG, titles.toString());
        }

        final ListView listview = (ListView) findViewById(R.id.listview);
        final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, contentTitleList);
        listview.setAdapter(adapter);




        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                Log.i(TAG, view.toString());
                Object obj = parent.getItemAtPosition(position);
                final String selectedItem = obj.toString();

                for(int i=0; i<ids.size(); i++){
                    if(titles.get(i).equals(selectedItem)){
                        try {
                            // remove item from the db
                            mTags.mLocalDB.deleteEventItem(ids.get(i));
                            view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.remove(selectedItem);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            /*view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
                                        @Override
                                        public void run() {
                                            //list.remove(item);
                                            //adapter.notifyDataSetChanged();
                                            //view.setAlpha(1);
                                        }
                                    }*/



                        }catch(Exception e){
                            e.printStackTrace();
                            Log.d(TAG, "failed to delete the event item");
                        }
                        return true;
                    }
                }

                return false;
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                //Log.i(TAG, view.toString());
                Object obj = parent.getItemAtPosition(position);
                String selectedItem = obj.toString();

                for(int i=0; i<ids.size(); i++){
                    if(titles.get(i).equals(selectedItem)){
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

                if(!SystemPreferences.IS_SETTINGS_ACTIVITY_ACTIVE) {
                    Intent intent = new Intent(this, Settings.class);
                    SystemPreferences.IS_SETTINGS_ACTIVITY_ACTIVE = true;
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
