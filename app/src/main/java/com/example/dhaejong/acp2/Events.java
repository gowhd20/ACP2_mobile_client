package com.example.dhaejong.acp2;

import android.content.Context;
import android.content.Intent;
import android.content.SyncAdapterType;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Events extends ActionBarActivity {

    private static final String TAG = "Events";

    Context context = this;
    LocalDB mLocalDB;
    GPSListener mGPSListener;
    // TODO: clear history needs to be implemented

    private List<String> getData(){
        Bundle metaDataBundle;
        metaDataBundle = getIntent().getExtras();
        return Arrays.asList(metaDataBundle.getString("id"), metaDataBundle.getString("title"));   // is more efficient than "new ArrayList<Integer>(Arrays.asList(ia))"
    }

    private LinearLayout addTitle(String title){
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
        titleText.setText("\n" + title);
        titleText.setTextSize(20);
        titleText.setTextColor(Color.RED);
        titleText.setTypeface(Typeface.DEFAULT_BOLD);

        return titleLayout;

    }

    private LinearLayout addDescription(String description){
        int layoutId = getResources().getIdentifier("description", "id", getPackageName());

        LinearLayout descriptionLayout = new LinearLayout(this);
        TextView descriptionText = new TextView(this);
        LinearLayout.LayoutParams descriptionParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        descriptionParams.topMargin = 20;
        descriptionParams.gravity = Gravity.CENTER_HORIZONTAL;
        descriptionParams.gravity = Gravity.CENTER_VERTICAL;
        descriptionLayout.setOrientation(LinearLayout.VERTICAL);
        descriptionLayout.setLayoutParams(descriptionParams);
        descriptionLayout.setId(layoutId);
        descriptionLayout.addView(descriptionText);
        descriptionText.setLinksClickable(true);
        descriptionText.setAutoLinkMask(Linkify.ALL);
        descriptionText.setTextSize(15);
        descriptionText.setTypeface(Typeface.DEFAULT_BOLD);
        descriptionText.setText("\n" + Html.fromHtml(description));

        return descriptionLayout;

    }

    private LinearLayout addEndTime(Long endTime){
        int layoutId = getResources().getIdentifier("end_time", "id", getPackageName());

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
        dateText.setText("End: \n" + mLocalDB.timestampToDatetime(endTime));
        dateText.setTextSize(15);
        dateText.setTypeface(Typeface.DEFAULT_BOLD);

        return dateLayout;
    }

    private LinearLayout addStartTime(Long startTime){
        int layoutId = getResources().getIdentifier("start_time", "id", getPackageName());

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
        dateText.setText("Start: \n" + mLocalDB.timestampToDatetime(startTime));
        dateText.setTextSize(15);
        dateText.setTypeface(Typeface.DEFAULT_BOLD);

        return dateLayout;
    }

    private LinearLayout addPrice(String price){
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
        priceText.setText("Price: \n" + Html.fromHtml(price));
        priceText.setTextSize(15);
        priceText.setTypeface(Typeface.DEFAULT_BOLD);

        return priceLayout;
    }

    private LinearLayout addLocation(String address){
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
        locationText.setText("Location: \n" + address);
        locationText.setTextSize(15);
        locationText.setTypeface(Typeface.DEFAULT_BOLD);

        return locationLayout;
    }

    private LinearLayout addUrl(String link){
        int layoutId = getResources().getIdentifier("url", "id", getPackageName());

        LinearLayout urlLayout = new LinearLayout(this);
        //TextView urlText = new TextView(this);
        ImageView urlImageView = new ImageView(this);
        LinearLayout.LayoutParams urlParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        urlParams.topMargin = 20;
        urlParams.gravity = Gravity.CENTER_HORIZONTAL;
        urlParams.gravity = Gravity.CENTER_VERTICAL;
        urlLayout.setOrientation(LinearLayout.VERTICAL);
        urlLayout.setLayoutParams(urlParams);
        //urlLayout.addView(urlText);
        urlLayout.addView(urlImageView);
        urlLayout.setId(layoutId);
        //urlText.setClickable(true);
        //urlText.setMovementMethod(LinkMovementMethod.getInstance());
        //String text = "<a href='"+link+"'> Link URL </a>";
        //urlText.setText(Html.fromHtml(text));
        //urlText.setTextSize(15);
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        Picasso.with(context).load(link).resize(screenWidth,screenHeight/3)
                .centerCrop().into(urlImageView);

        return urlLayout;
    }


    private RelativeLayout addGoogleMapBtn(String address, final Hashtable hash){
        RelativeLayout mapBtnLayout = new RelativeLayout(this);
        ImageButton googleMapBtn = new ImageButton(this);
        RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        btnParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        googleMapBtn.setTag(address);
        googleMapBtn.setImageResource(R.drawable.ic_google_map_icon);
        googleMapBtn.setBackgroundColor(Color.TRANSPARENT);

        googleMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Object city = "";
                Object country = "";
                if(hash.containsKey("country_name") && hash.get("country_name") != null) {
                    city = hash.get("country_name");
                }

                if(hash.containsKey("locality") && hash.get("locality") != null) {
                    country = hash.get("locality");
                }

                if (isGoogleMapsInstalled()) {
                    // if this mobile app has google map application
                    Object obj = v.getTag();
                    Intent searchAddress = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" +
                            obj.toString() + " " +
                            city.toString() + " " +
                            country.toString()));

                    startActivity(searchAddress);
                } else {
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

    public ArrayList<String> searchWebUrl(String text){
        ArrayList<String> links = new ArrayList<>();
        String regex = "\\(?\\b(https://|http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);
        while(m.find()) {
            String urlStr = m.group();
            if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
                urlStr = urlStr.substring(1, urlStr.length() - 1);
            }
            links.add(urlStr);
        }
        return links;
    }

    public Hashtable getAddress(double lat, double lon) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            Hashtable mhash = new Hashtable();
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
                Address obj = addresses.get(0);
                try{
                    mhash.put("address", obj.getAddressLine(0));
                }catch(NullPointerException e){
                    Log.d(TAG, "address is not obtained");
                }
                try {
                    mhash.put("country_name", obj.getCountryName());
                }catch(NullPointerException e){
                    Log.d(TAG, "country name is not obtained");
                }
                try{
                    mhash.put("locality", obj.getLocality());
                }catch(NullPointerException e){
                    Log.d(TAG, "locality is not obtained");
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return mhash;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        mLocalDB = new LocalDB(this);
        List<String> selectedInfo = getData();  // init selected item info
        ArrayList<String> selectedAllInfo;
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.contentView);
        selectedAllInfo = mLocalDB.getAllItemsById(Integer.valueOf(selectedInfo.get(0)), LocalDB.DATABASE_TABLE_NAME_EVENTS);

        Log.d(TAG, selectedAllInfo.toString());

        // get locality from previously stored lat, lon
        Location location;
        mGPSListener = new GPSListener(this);
        location = mGPSListener.getLocation();

        Hashtable mHash = new Hashtable();
        try {
            mHash = getAddress(location.getLatitude(),
                    location.getLongitude());
        }catch(NullPointerException e){
            Log.i(TAG, "known gps info not exist");
        }

        // title
        if(!selectedAllInfo.get(7).isEmpty()){
            mainLayout.addView(addTitle(selectedAllInfo.get(7)));
        }
        // description
        if(!selectedAllInfo.get(5).isEmpty()){
            mainLayout.addView(addDescription(selectedAllInfo.get(5)));
        }
        // price
        if(!selectedAllInfo.get(3).isEmpty()){
            mainLayout.addView(addPrice(selectedAllInfo.get(3)));
        }
        // url
        if(!selectedAllInfo.get(2).isEmpty()){
            mainLayout.addView(addUrl(selectedAllInfo.get(2)));
        }
        // start time
        if(!selectedAllInfo.get(1).isEmpty()){
            mainLayout.addView(addStartTime(Long.valueOf(selectedAllInfo.get(1))));
        }
        // end time
        if(!selectedAllInfo.get(0).isEmpty()){
            mainLayout.addView(addEndTime(Long.valueOf(selectedAllInfo.get(0))));
        }
        // address
        if(!selectedAllInfo.get(4).isEmpty()){
            mainLayout.addView(addLocation(selectedAllInfo.get(4)));
        }
        // location map
        if(!selectedAllInfo.get(4).isEmpty()){
            mainLayout.addView(addGoogleMapBtn(selectedAllInfo.get(4), mHash));
        }
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
                    Toast.makeText(Events.this, " Settings.java is already running", Toast.LENGTH_LONG).show();
                    return false;
                }

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "square()"; }
    }
}
