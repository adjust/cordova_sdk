package com.adjust.sdk;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class AdjustCordovaUtils {
	public static final String KEY_APP_TOKEN = "appToken";
    public static final String KEY_ENVIRONMENT = "environment";
    public static final String KEY_LOG_LEVEL = "logLevel";
    public static final String KEY_SDK_PREFIX = "sdkPrefix";
    public static final String KEY_PROCESS_NAME = "processName";
    public static final String KEY_DEFAULT_TRACKER = "defaultTracker";
    public static final String KEY_EXTERNAL_DEVICE_ID = "externalDeviceId";
    public static final String KEY_URL_STRATEGY = "urlStrategy";
    public static final String KEY_EVENT_BUFFERING_ENABLED = "eventBufferingEnabled";
    public static final String KEY_EVENT_TOKEN = "eventToken";
    public static final String KEY_REVENUE = "revenue";
    public static final String KEY_CURRENCY = "currency";
    public static final String KEY_TRANSACTION_ID = "transactionId";
    public static final String KEY_CALLBACK_ID = "callbackId";
    public static final String KEY_CALLBACK_PARAMETERS = "callbackParameters";
    public static final String KEY_PARTNER_PARAMETERS = "partnerParameters";
    public static final String KEY_SEND_IN_BACKGROUND = "sendInBackground";
    public static final String KEY_SHOULD_LAUNCH_DEEPLINK = "shouldLaunchDeeplink";
    public static final String KEY_USER_AGENT = "userAgent";
    public static final String KEY_DELAY_START = "delayStart";
    public static final String KEY_SECRET_ID = "secretId";
    public static final String KEY_INFO_1 = "info1";
    public static final String KEY_INFO_2 = "info2";
    public static final String KEY_INFO_3 = "info3";
    public static final String KEY_INFO_4 = "info4";
    public static final String KEY_DEVICE_KNOWN = "isDeviceKnown";
    // public static final String KEY_READ_MOBILE_EQUIPMENT_IDENTITY = "readMobileEquipmentIdentity";
    public static final String KEY_BASE_URL = "baseUrl";
    public static final String KEY_GDPR_URL = "gdprUrl";
    public static final String KEY_SUBSCRIPTION_URL = "subscriptionUrl";
    public static final String KEY_BASE_PATH = "basePath";
    public static final String KEY_GDPR_PATH = "gdprPath";
    public static final String KEY_SUBSCRIPTION_PATH = "subscriptionPath";
    public static final String KEY_USE_TEST_CONNECTION_OPTIONS = "useTestConnectionOptions";
    public static final String KEY_TIMER_INTERVAL = "timerIntervalInMilliseconds";
    public static final String KEY_TIMER_START = "timerStartInMilliseconds";
    public static final String KEY_SESSION_INTERVAL = "sessionIntervalInMilliseconds";
    public static final String KEY_SUBSESSION_INTERVAL = "subsessionIntervalInMilliseconds";
    public static final String KEY_TEARDOWN = "teardown";
    public static final String KEY_NO_BACKOFF_WAIT = "noBackoffWait";
    public static final String KEY_HAS_CONTEXT = "hasContext";
    public static final String KEY_ADID = "adid";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_NETWORK = "network";
    public static final String KEY_ADGROUP = "adgroup";
    public static final String KEY_CAMPAIGN = "campaign";
    public static final String KEY_CREATIVE = "creative";
    public static final String KEY_WILL_RETRY = "willRetry";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_CLICK_LABEL = "clickLabel";
    public static final String KEY_TRACKER_NAME = "trackerName";
    public static final String KEY_TRACKER_TOKEN = "trackerToken";
    public static final String KEY_JSON_RESPONSE = "jsonResponse";
    public static final String KEY_PRICE = "price";
    public static final String KEY_SKU = "sku";
    public static final String KEY_ORDER_ID = "orderId";
    public static final String KEY_SIGNATURE = "signature";
    public static final String KEY_PURCHASE_TOKEN = "purchaseToken";
    public static final String KEY_PURCHASE_TIME = "purchaseTime";

    public static final String COMMAND_CREATE = "create";
    public static final String COMMAND_SET_ATTRIBUTION_CALLBACK = "setAttributionCallback";
    public static final String COMMAND_SET_EVENT_TRACKING_SUCCEEDED_CALLBACK = "setEventTrackingSucceededCallback";
    public static final String COMMAND_SET_EVENT_TRACKING_FAILED_CALLBACK = "setEventTrackingFailedCallback";
    public static final String COMMAND_SET_SESSION_TRACKING_SUCCEEDED_CALLBACK = "setSessionTrackingSucceededCallback";
    public static final String COMMAND_SET_SESSION_TRACKING_FAILED_CALLBACK = "setSessionTrackingFailedCallback";
    public static final String COMMAND_SET_DEFERRED_DEEPLINK_CALLBACK = "setDeferredDeeplinkCallback";
    public static final String COMMAND_SET_PUSH_TOKEN = "setPushToken";
    public static final String COMMAND_TRACK_EVENT = "trackEvent";
    public static final String COMMAND_SET_OFFLINE_MODE = "setOfflineMode";
    public static final String COMMAND_ON_RESUME = "onResume";
    public static final String COMMAND_ON_PAUSE = "onPause";
    public static final String COMMAND_IS_ENABLED = "isEnabled";
    public static final String COMMAND_SET_ENABLED = "setEnabled";
    public static final String COMMAND_APP_WILL_OPEN_URL = "appWillOpenUrl";
    public static final String COMMAND_GDPR_FORGET_ME = "gdprForgetMe";
    public static final String COMMAND_DISABLE_THIRD_PARTY_SHARING = "disableThirdPartySharing";
    public static final String COMMAND_TRACK_AD_REVENUE = "trackAdRevenue";
    public static final String COMMAND_TRACK_APP_STORE_SUBSCRIPTION = "trackAppStoreSubscription";
    public static final String COMMAND_TRACK_PLAY_STORE_SUBSCRIPTION = "trackPlayStoreSubscription";
    public static final String COMMAND_GET_IDFA = "getIdfa";
    public static final String COMMAND_GET_ADID = "getAdid";
    public static final String COMMAND_GET_ATTRIBUTION = "getAttribution";
    public static final String COMMAND_GET_GOOGLE_AD_ID = "getGoogleAdId";
    public static final String COMMAND_GET_AMAZON_AD_ID = "getAmazonAdId";
    public static final String COMMAND_GET_SDK_VERSION = "getSdkVersion";
    public static final String COMMAND_ADD_SESSION_CALLBACK_PARAMETER = "addSessionCallbackParameter";
    public static final String COMMAND_REMOVE_SESSION_CALLBACK_PARAMETER = "removeSessionCallbackParameter";
    public static final String COMMAND_RESET_SESSION_CALLBACK_PARAMETERS = "resetSessionCallbackParameters";
    public static final String COMMAND_ADD_SESSION_PARTNER_PARAMETER = "addSessionPartnerParameter";
    public static final String COMMAND_REMOVE_SESSION_PARTNER_PARAMETER = "removeSessionPartnerParameter";
    public static final String COMMAND_RESET_SESSION_PARTNER_PARAMETERS = "resetSessionPartnerParameters";
    public static final String COMMAND_SEND_FIRST_PACKAGES = "sendFirstPackages";
    public static final String COMMAND_SET_REFERRER = "setReferrer";
    public static final String COMMAND_SET_TEST_OPTIONS = "setTestOptions";
    public static final String COMMAND_TEARDOWN = "teardown";

    public static void addValueOrEmpty(Map<String, String> map, String key, Object value){
        if (value != null) {
            map.put(key, value.toString());
        } else {
            map.put(key, "");
        }
    }

    public static boolean isFieldValid(String field) {
        return field != null && !field.equals("") && !field.equals("null");
    }

    public static String[] jsonArrayToArray(JSONArray jsonArray) throws JSONException {
        if (jsonArray == null) {
            return null;
        }

        String[] array = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            array[i] = jsonArray.get(i).toString();
        }

        return array;
    }

    public static Map<String, Object> jsonObjectToMap(JSONObject jsonObject) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>(jsonObject.length());
        @SuppressWarnings("unchecked")
        Iterator<String> jsonObjectIterator = jsonObject.keys();

        while (jsonObjectIterator.hasNext()) {
            String key = jsonObjectIterator.next();
            map.put(key, jsonObject.get(key));
        }

        return map;
    }

    public static Map<String, String> getAttributionMap(AdjustAttribution attribution) {
        Map<String, String> map = new HashMap<String, String>();
        addValueOrEmpty(map, KEY_TRACKER_TOKEN, attribution.trackerToken);
        addValueOrEmpty(map, KEY_TRACKER_NAME, attribution.trackerName);
        addValueOrEmpty(map, KEY_NETWORK, attribution.network);
        addValueOrEmpty(map, KEY_CAMPAIGN, attribution.campaign);
        addValueOrEmpty(map, KEY_ADGROUP, attribution.adgroup);
        addValueOrEmpty(map, KEY_CREATIVE, attribution.creative);
        addValueOrEmpty(map, KEY_CLICK_LABEL, attribution.clickLabel);
        addValueOrEmpty(map, KEY_ADID, attribution.adid);
        return map;
    }

    public static Map<String, String> getEventSuccessMap(AdjustEventSuccess event) {
        Map<String, String> map = new HashMap<String, String>();
        addValueOrEmpty(map, KEY_MESSAGE, event.message);
        addValueOrEmpty(map, KEY_TIMESTAMP, event.timestamp);
        addValueOrEmpty(map, KEY_ADID, event.adid);
        addValueOrEmpty(map, KEY_EVENT_TOKEN, event.eventToken);
        addValueOrEmpty(map, KEY_CALLBACK_ID, event.callbackId);
        addValueOrEmpty(map, KEY_JSON_RESPONSE, event.jsonResponse);
        return map;
    }

    public static Map<String, String> getEventFailureMap(AdjustEventFailure event) {
        Map<String, String> map = new HashMap<String, String>();
        addValueOrEmpty(map, KEY_MESSAGE, event.message);
        addValueOrEmpty(map, KEY_TIMESTAMP, event.timestamp);
        addValueOrEmpty(map, KEY_ADID, event.adid);
        addValueOrEmpty(map, KEY_EVENT_TOKEN, event.eventToken);
        addValueOrEmpty(map, KEY_CALLBACK_ID, event.callbackId);
        addValueOrEmpty(map, KEY_WILL_RETRY, event.willRetry ? "true" : "false");;
        addValueOrEmpty(map, KEY_JSON_RESPONSE, event.jsonResponse);
        return map;
    }

    public static Map<String, String> getSessionSuccessMap(AdjustSessionSuccess session) {
        Map<String, String> map = new HashMap<String, String>();
        addValueOrEmpty(map, KEY_MESSAGE, session.message);
        addValueOrEmpty(map, KEY_TIMESTAMP, session.timestamp);
        addValueOrEmpty(map, KEY_ADID, session.adid);
        addValueOrEmpty(map, KEY_JSON_RESPONSE, session.jsonResponse);
        return map;
    }

    public static Map<String, String> getSessionFailureMap(AdjustSessionFailure session) {
        Map<String, String> map = new HashMap<String, String>();
        addValueOrEmpty(map, KEY_MESSAGE, session.message);
        addValueOrEmpty(map, KEY_TIMESTAMP, session.timestamp);
        addValueOrEmpty(map, KEY_ADID, session.adid);
        addValueOrEmpty(map, KEY_WILL_RETRY, session.willRetry ? "true" : "false");;
        addValueOrEmpty(map, KEY_JSON_RESPONSE, session.jsonResponse);
        return map;
    }
}
