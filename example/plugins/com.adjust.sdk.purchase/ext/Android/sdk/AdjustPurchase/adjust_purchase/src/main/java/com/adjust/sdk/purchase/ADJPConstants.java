package com.adjust.sdk.purchase;

/**
 * Created by uerceg on 04/12/15.
 */
public class ADJPConstants {
    public static final int TIMEOUT = 5000;
    public static final int APP_TOKEN_SIZE = 12;
    public static final int STATUS_CODE_ERROR = -1;

    public static final String ENCODING = "UTF-8";
    public static final String TAG = "AdjustPurchase";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'Z";

    public static final String ENVIRONMENT_SANDBOX = "sandbox";
    public static final String ENVIRONMENT_PRODUCTION = "production";

    public static final String SDK_VERSION = "android_purchase1.1.2";
    public static final String BASE_URL = "https://ssrv.adjust.com/verify";

    public static final String KEY_APP_TOKEN = "app_token";
    public static final String KEY_SDK_VERSION = "sdk_version";
    public static final String KEY_ENVIRONMENT = "environment";
    public static final String KEY_GPS_PRODUCT_ID = "gps_product_id";
    public static final String KEY_GPS_PURCHASE_TOKEN = "gps_token";
    public static final String KEY_GPS_DEVELOPER_PAYLOAD = "gps_developer_payload";

    public static final String KEY_ADJUST_STATE = "adjust_state";
    public static final String KEY_ADJUST_MESSAGE = "adjust_message";
    public static final String KEY_ADJUST_STATUS_CODE = "adjust_status_code";
}
