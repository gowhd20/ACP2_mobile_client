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
    public static final String USER_REGISTERED = "user_registered";
    public static final String MAC_ADDR_REGISTERED = "mac_addr_registered";
    public static final String USER_INFO_UPDATED = "user_info_updated";
    public static final String GCM_TOKEN_REGISTERED = "gcm_token_registered";

    // sharedpreference
    public static final String USER_ID = "user_id";
    public static final String EMAIL = "email_address";
    public static final String FACEBOOK_ID = "facebook_id";
    public static final String USER_NAME = "full_name";
    public static final String MAC_ADDRESS = "mac_address";
    public static final String ANDROID_ID = "android_id";
    public static final String DEVICE_ID = "device_id";
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_LIST = "categories";    // stored locally
    public static final String CHECKBOX = "checkbox";
    public static final String FACEBOOK_TOKEN = "short_token";
    public static final String SERVER_TOKEN = "server_access_token";

    // calendar data set
    public static final String CALENDAR_EVENT_ID = "calendar_event_id";
    public static final String CALENDAR_EVENT_TITLE = "title";
    public static final String CALENDAR_EVENT_START_TIME = "start_timestamp";
    public static final String CALENDAR_EVENT_END_TIME = "end_timestamp";
    public static final String CALENDAR_EVENT_LOCATION = "location";
    public static final String CALENDAR_EVENT_DESCRIPTION = "description";

    // server url
    public static final String GET_CATEGORIES_URL = "https://acp.velho.xyz/categories";
    public static final String POST_REGISTER_USER_URL = "https://acp.velho.xyz/client/adduser";
    public static final String POST_USER_MAC_ADDRESS = "https://acp.velho.xyz/client/user/addmac";
    public static final String POST_TAG_ADDED = "https://acp.velho.xyz/client/user/topic/register";
    public static final String POST_TAG_DELETED = "https://acp.velho.xyz/client/user/topic/unregister";
    public static final String UPDATE_USER_INFO = "https://acp.velho.xyz/client/user/facebook";
    public static final String UPDATE_GCM_TOKEN = "https://acp.velho.xyz//client/user/google/token";

    public static final String DATA_NOT_COLLAPSED = "do_not_collapse";

    // http request intervals
    public static final int GET_CATEGORY_REQUEST_INTERVAL = 100000;
    public static final int POST_USER_REGISTER_REQUEST_INTERVAL = 100000;



}
