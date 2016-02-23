package com.example.dhaejong.acp2;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dhaejong on 2.2.2016.
 */
public class Tags {

    private String TAG = "Tags";
    private ArrayList<String> tag_names;
    private Context context;
    private Activity activity;
    public final static int TEXTVIEW_IDENTIFIER = 111111;

    public int countTagsInList;      // count of tags in the interest list
    public String[] tags;
    public String[] tempAddedTags;
    public LocalDB mLocalDB;

    Tags(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        this.mLocalDB = new LocalDB(context);
        this.countTagsInList = mLocalDB.numberOfRows(LocalDB.DATABASE_TABLE_NAME_TAGS);
        //mLocalDB.emptyTables();           // for test purpose *******************************//


        this.tags = new String[]{"science", "party", "education", "job", "course", "music"};
        this.tempAddedTags = new String[]{};
        this.tag_names = new ArrayList<>();
        tag_names.addAll(Arrays.asList(tags));

    }


    public ArrayList<String> addTags(String name){
        this.tag_names.add(name);
        return this.tag_names;
    }


    public ArrayList<String> getTagList(){
        return this.tag_names;
    }

    public String getTagItem(String name){
        String foundTag="";

        return foundTag;
    }

    protected int getTextViewIdByBtnId(int btnId){
        return Integer.valueOf(Integer.toString(btnId)+Integer.toString(btnId)+Integer.toString(btnId));
    }

    protected void addTagInfo(String name, Integer id){

    }

    public int getNumberOfTags(){
        return this.tags.length;
    }

    public boolean isExistingTag(String tagName){
        int checkExistence = context.getResources().getIdentifier(tagName, "id", context.getPackageName());

        if(checkExistence != 0){
            Log.i(TAG, tagName+" is already in the list");
            // tag name already exist
            return true;
        }else{
            // tag name not exist
            return false;
        }
    }

    private boolean addTagToLocalDB(int btnId, String tagName){
        // store in local db
        if (mLocalDB.addNewTag(Integer.toString(btnId), tagName)) {
            // store the tag in local database
            countTagsInList = mLocalDB.numberOfRows(LocalDB.DATABASE_TABLE_NAME_TAGS);
            Log.i(TAG, "tag stored in local db");
            Log.i(TAG, "number of tags: " + Integer.toString(countTagsInList));
            return true;
        }else{
            Log.i(TAG, "failed to store tag name into local db");
        }
        return false;
    }


    public int addTagToInterest(Context context, String tagName){
        // 000 for button id cannot be assigned                 //
        // button id = (ex)222222                               //
        // textView id = (ex)333333    , button id +111111      //
        // to avoid possible conflict in indexing between them when looking for resource

        // checking first if user has added this tag before
        // if tag is already added, return 0 or return tag id

        ArrayList<String> tempArrayList;
        int btnId = context.getResources().getIdentifier(tagName, "id", context.getPackageName());
        int txtId = btnId + TEXTVIEW_IDENTIFIER;

        if(mLocalDB.numberOfRows(LocalDB.DATABASE_TABLE_NAME_TAGS) != 0) {
            // do not query for exist tag name when it's very first time
            tempArrayList = mLocalDB.getAllItemsById(btnId, LocalDB.DATABASE_TABLE_NAME_TAGS);


            if (tempArrayList.isEmpty()) {
                // not first time execution
                addTagToView(context, tagName, btnId, txtId);

                // store in local db
                addTagToLocalDB(btnId, tagName);
                return btnId;

            } else {

                // tag name already registered
                Log.i(TAG, "tag name conflicted in local database");
                return 0;

            }
        }else{
            // first execution of the app
            addTagToView(context, tagName, btnId, txtId);

            // store in local db
            addTagToLocalDB(btnId, tagName);
            return btnId;
        }

    }

    private void addTagToView(Context context, String tagName, int btnId, int txtId){
        Log.i(TAG, "btn id: " + btnId);
        Log.i(TAG, "txt id: " + txtId);

        LinearLayout outerLayout = (LinearLayout) activity.findViewById(R.id.ButtonsLayout);
        TextView mTextView = new TextView(context);
        RelativeLayout innerLayout = new RelativeLayout(context);
        ImageButton mImageBtn = new ImageButton(context);


        RelativeLayout.LayoutParams btnParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        btnParam.addRule(RelativeLayout.CENTER_VERTICAL);
        btnParam.addRule(RelativeLayout.END_OF, mTextView.getId());
        mImageBtn.setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.ic_clear_black_24dp));
        mImageBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
        mImageBtn.setId(btnId);


        mTextView.setId(txtId);
        RelativeLayout.LayoutParams txtParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        txtParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        txtParam.addRule(RelativeLayout.CENTER_VERTICAL);
        mTextView.setLayoutParams(txtParam);
        mTextView.setTextSize(25);
        mTextView.setText(tagName);

        RelativeLayout.LayoutParams innerLayoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        innerLayout.setLayoutParams(innerLayoutParam);

        innerLayout.addView(mTextView);
        innerLayout.addView(mImageBtn);
        outerLayout.addView(innerLayout);
    }

    public int initAddTagToInterest(Context context, String tagName){
        int btnId = context.getResources().getIdentifier(tagName, "id", context.getPackageName());
        int txtId = btnId + TEXTVIEW_IDENTIFIER;

        addTagToView(context, tagName, btnId, txtId);

        return btnId;
    }

}
