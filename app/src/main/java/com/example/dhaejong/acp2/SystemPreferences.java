package com.example.dhaejong.acp2;

/**
 * Created by dhaejong on 24.2.2016.
 */
public class SystemPreferences {

    // systemwise valuables
    public static final String CATEGORY_IN_USE = "job";
    public static final int CALENDAR_QUERY_INTERVAL = 10000;
    public static final String CURRENT_CITY_OF_USERS = "Oulu"; // TODO: save current city where user is staying, search with pre-defined interval
    public static boolean IS_SETTINGS_ACTIVITY_ACTIVE = false;
    public static final int TEXTVIEW_IDENTIFIER = 111111;
    public static final String GCM_ID = "624829381259";             // not sure if it needs to be dynamic?
    public static final String GCM_TOKEN = "gcm_token";
    public static boolean IS_YOUR_FIRST_PLAY = true;

    // sharedpreference
    public static final String USER_ID = "user_id";
    public static final String EMAIL = "user_email";
    public static final String FACEBOOK_ID = "facebook_id";
    public static final String USER_NAME = "user_name";
    public static final String MAC_ADDRESS = "mac_address";
    public static final String ANDROID_ID = "android_id";
    public static final String DEVICE_ID = "device_id";
    public static final String CATEGORY_ID_IN_USE = "category_id";
    public static final String CHECKBOX = "checkbox";
    public static final String FACEBOOK_TOKEN = "fb_access_token";
    public static final String SERVER_TOKEN = "server_access_token";

    // server url
    public static final String GET_CATEGORIES_URL = "https://acp.velho.xyz/categories";
    public static final String POST_REGISTER_USER_URL = "https://acp.velho.xyz/client/adduser";
    public static final String POST_USER_MAC_ADDRESS = "https://acp.velho.xyz/client/user/addmac";

    public static final String DATA_NOT_COLLAPSED = "do_not_collapse";

    // http request intervals
    public static final int GET_CATEGORY_REQUEST_INTERVAL = 100000;
    public static final int POST_USER_REGISTER_REQUEST_INTERVAL = 100000;



}
