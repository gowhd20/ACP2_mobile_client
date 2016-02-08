package com.example.dhaejong.acp2;
import android.app.Activity;
import android.content.Context;
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

/**
 * Created by dhaejong on 2.2.2016.
 */
public class Tags {

    private String TAG = "Tags";
    private ArrayList<String> tag_names;
    private Context context;
    private Activity activity;

    public int countTagsInList;      // count of tags in the interest list
    public String[] tags;

    Tags(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
        this.countTagsInList = 0;

        this.tags = new String[]{"science", "party", "education", "job", "course", "music"};
        this.tag_names = new ArrayList<>();
        tag_names.addAll(Arrays.asList(tags));

    }


    public ArrayList<String> addTags(String name){
        this.tag_names.add(name);
        return this.tag_names;
    }

/*    public boolean isExistingTag(String tagName){
        TextView tempView;// = (TextView) this.activity.findViewById(textViewId)
        int count = this.countTagsInList;
        namingIndex
        while(count > 0){
            tempView = (TextView) this.activity.findViewById(textViewId)

            --count;
        }

    }*/

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

    public int getAvailableId(Context context){
        int idCandidate = this.countTagsInList;

        while(idCandidate>0) {
            if(!isExistingId(idCandidate, context)){
                Log.i(TAG, "im selected "+ Integer.toString(idCandidate));
                return idCandidate;
            }else {
                idCandidate--;
            }
        }
        return ++idCandidate;
    }

    public int getNumberOfTags(){
        return this.tags.length;
    }

    public boolean isExistingId(int id, Context context){
        int checkExistence = context.getResources().getIdentifier(Integer.toString(id), "id", context.getPackageName());
        Log.i(TAG, Integer.toString(checkExistence));

        if(checkExistence != 0){
            // id is taken
            return true;
        }else{
            // id is available
            return false;
        }
    }


    public ImageButton addTagToInterest(Context context, String tagName){
        // Button and TextView naming                               //
        // starting from 1 as 000 for button id cannot be assigned  //
        // button id = 1,2,3,4,5 ... same as countTagsInterested  //
        // textView id = 111,222,333 ... ,101010,111111               //
        // to avoid possible conflict in indexing between them when looking for resource

        this.countTagsInList++;

        //int btnId = getAvailableId(context);
        if(this.countTagsInList == 1) {
            int checkExistence = context.getResources().getIdentifier(Integer.toString(1), "id", context.getPackageName());
            Log.i(TAG, "checkExisnstance before=  "+Integer.toString(checkExistence));
        }
        if(this.countTagsInList == 2) {


            int checkExistence = context.getResources().getIdentifier(Integer.toString(1), "id", context.getPackageName());
            Log.i(TAG, "checkExisnstance after=  "+Integer.toString(checkExistence));
        }
        int btnId = this.countTagsInList;
        String temp = Integer.toString(btnId)+Integer.toString(btnId)+Integer.toString(btnId);
        int txtId = Integer.valueOf(temp);

        LinearLayout outerLayout = (LinearLayout)this.activity.findViewById(R.id.ButtonsLayout);
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


        Log.i(TAG, Integer.toString(this.countTagsInList));
        Log.i(TAG, "id: "+Integer.toString(btnId));
        RelativeLayout.LayoutParams innerLayoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        innerLayout.setLayoutParams(innerLayoutParam);

        innerLayout.addView(mTextView);
        innerLayout.addView(mImageBtn);
        outerLayout.addView(innerLayout);


        return mImageBtn;
    }

}
