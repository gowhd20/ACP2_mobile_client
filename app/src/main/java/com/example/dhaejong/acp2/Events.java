package com.example.dhaejong.acp2;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Events extends FragmentActivity {

    Context context = this;

    private LinearLayout displayTitle(){
        int layoutId = getResources().getIdentifier("title", "id", getPackageName());

        LinearLayout titleLayout = new LinearLayout(this);
        TextView titleText = new TextView(this);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        titleParams.topMargin = 20;
        titleParams.gravity = Gravity.CENTER_HORIZONTAL;
        titleParams.gravity = Gravity.CENTER_VERTICAL;
        titleLayout.setOrientation(LinearLayout.VERTICAL);
        titleLayout.setLayoutParams(titleParams);
        titleLayout.setId(layoutId);
        titleLayout.addView(titleText);
        titleText.setText("this section is for title: ");

        return titleLayout;

    }

    private LinearLayout displayDate(){
        int layoutId = getResources().getIdentifier("date", "id", getPackageName());

        LinearLayout dateLayout = new LinearLayout(this);
        TextView dateText = new TextView(this);
        LinearLayout.LayoutParams dateParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dateParams.topMargin = 20;
        dateParams.gravity = Gravity.CENTER_HORIZONTAL;
        dateParams.gravity = Gravity.CENTER_VERTICAL;
        dateLayout.setOrientation(LinearLayout.VERTICAL);
        dateLayout.setLayoutParams(dateParams);
        dateLayout.setId(layoutId);
        dateLayout.addView(dateText);
        dateText.setText("this section is for date: ");

        return dateLayout;
    }

    private LinearLayout displayPrice(){
        int layoutId = getResources().getIdentifier("price", "id", getPackageName());

        LinearLayout priceLayout = new LinearLayout(this);
        TextView priceText = new TextView(this);
        LinearLayout.LayoutParams priceParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        priceParams.topMargin = 20;
        priceParams.gravity = Gravity.CENTER_HORIZONTAL;
        priceParams.gravity = Gravity.CENTER_VERTICAL;
        priceLayout.setOrientation(LinearLayout.VERTICAL);
        priceLayout.setLayoutParams(priceParams);
        priceLayout.setId(layoutId);
        priceLayout.addView(priceText);
        priceText.setText("this section is for price: ");

        return priceLayout;
    }

    private LinearLayout displayLocation(){
        int layoutId = getResources().getIdentifier("location", "id", getPackageName());

        LinearLayout locationLayout = new LinearLayout(this);
        TextView locationText = new TextView(this);
        LinearLayout.LayoutParams locationParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        locationParams.topMargin = 20;
        locationParams.gravity = Gravity.CENTER_HORIZONTAL;
        locationParams.gravity = Gravity.CENTER_VERTICAL;
        locationLayout.setOrientation(LinearLayout.VERTICAL);
        locationLayout.setLayoutParams(locationParams);
        locationLayout.addView(locationText);
        locationLayout.setId(layoutId);
        locationText.setText("this section is for location: ");

        return locationLayout;
    }

    public RelativeLayout addGoogleMapBtn(){
        RelativeLayout mapBtnLayout = new RelativeLayout(this);
        ImageButton googleMapBtn = new ImageButton(this);
        RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        btnParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        googleMapBtn.setImageResource(R.drawable.ic_place_black_24dp);
        googleMapBtn.setBackgroundColor(Color.WHITE);
        googleMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGoogleMapsInstalled()) {
                    // if this mobile app has google map application
                    Intent searchAddress = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + "syynimaa"));
                    startActivity(searchAddress);
                }else{
                    // if this mobile has not google map application, run my custom google map
                    // has to be implemented with key word of searching location as default
                    Intent googleMapIntent = new Intent(context, GoogleMapActivity.class);
                    startActivity(googleMapIntent);
                }
            }
        });

        mapBtnLayout.addView(googleMapBtn);
        mapBtnLayout.setLayoutParams(btnParams);

        return mapBtnLayout;
    }

    public boolean isGoogleMapsInstalled()
    {
        try
        {
            ApplicationInfo info = getPackageManager().getApplicationInfo("com.google.android.apps.maps", 0 );
            return true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        LinearLayout title = displayTitle();
        LinearLayout date = displayDate();
        LinearLayout price = displayPrice();
        LinearLayout location =  displayLocation();
        RelativeLayout mapBtnLayout = addGoogleMapBtn();
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.contentView);

        mainLayout.addView(title);
        mainLayout.addView(date);
        mainLayout.addView(price);
        mainLayout.addView(location);
        mainLayout.addView(mapBtnLayout);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_events, menu);
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
}
