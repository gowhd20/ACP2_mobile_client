package com.example.dhaejong.acp2;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by dhaejong on 2.2.2016.
 */
public class Tags {

    private ArrayList<String> tag_names;
    private Context context;
    private String[] tags;
    public int countTags = 0;
    private int txtId = 0;
    private int btnId = 0;
    private Activity activity;

    public void Tags(Context context, Activity activity){
        this.context = context;
        this.activity = activity;

        tags = new String[]{"science", "party", "education"};
        tag_names = new ArrayList<>();
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

    protected void addTagInfo(String name, Integer id){

    }

    public ImageButton addTag(Context context, String tagName){

        LinearLayout outerLayout = (LinearLayout)this.activity.findViewById(R.id.ButtonsLayout);
        TextView mTextView = new TextView(context);
        RelativeLayout innerLayout = new RelativeLayout(context);
        ImageButton mImageBtn = new ImageButton(context);


        mTextView.setId(this.txtId);
        RelativeLayout.LayoutParams txtParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        txtParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        txtParam.addRule(RelativeLayout.CENTER_VERTICAL);
        mTextView.setLayoutParams(txtParam);
        mTextView.setTextSize(25);
        mTextView.setText(tagName);


        RelativeLayout.LayoutParams btnParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        btnParam.addRule(RelativeLayout.CENTER_VERTICAL);
        btnParam.addRule(RelativeLayout.END_OF, mTextView.getId());
        mImageBtn.setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.ic_clear_black_24dp));
        mImageBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
        mImageBtn.setId(this.btnId);

        this.countTags = this.countTags+1;
        this.txtId = this.txtId+1;
        this.btnId = this.btnId+1;


        RelativeLayout.LayoutParams innerLayoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        innerLayout.setLayoutParams(innerLayoutParam);

        innerLayout.addView(mTextView);
        innerLayout.addView(mImageBtn);
        outerLayout.addView(innerLayout);

        return mImageBtn;
    }

}
