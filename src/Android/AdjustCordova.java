package com.adjust.sdk;

import android.net.Uri;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;


public class AdjustCordova extends CordovaPlugin 
    implements OnAttributionChangedListener, 
               OnEventTrackingSucceededListener,
               OnEventTrackingFailedListener,
               OnSessionTrackingSucceededListener,
               OnSessionTrackingFailedListener,
               OnDeeplinkResponseListener, 
               OnDeviceIdsRead {
    private static final String KEY_APP_TOKEN               = "appToken";
    private static final String KEY_ENVIRONMENT             = "environment";
    private static final String KEY_LOG_LEVEL               = "logLevel";
    private static final String KEY_SDK_PREFIX              = "sdkPrefix";
    private static final String KEY_PROCESS_NAME            = "processName";
    private static final String KEY_DEFAULT_TRACKER         = "defaultTracker";
    private static final String KEY_EVENT_BUFFERING_ENABLED = "eventBufferingEnabled";
    private static final String KEY_EVENT_TOKEN             = "eventToken";
    private static final String KEY_REVENUE                 = "revenue";
    private static final String KEY_CURRENCY                = "currency";
    private static final String KEY_TRANSACTION_ID          = "transactionId";
    private static final String KEY_CALLBACK_PARAMETERS     = "callbackParameters";
    private static final String KEY_PARTNER_PARAMETERS      = "partnerParameters";
    private static final String KEY_SEND_IN_BACKGROUND      = "sendInBackground";
    private static final String KEY_SHOULD_LAUNCH_DEEPLINK  = "shouldLaunchDeeplink";
    private static final String KEY_USER_AGENT              = "userAgent";
    private static final String KEY_DELAY_START             = "delayStart";

    private static final String COMMAND_CREATE                                   = "create";
    private static final String COMMAND_SET_ATTRIBUTION_CALLBACK                 = "setAttributionCallback";
    private static final String COMMAND_SET_EVENT_TRACKING_SUCCEEDED_CALLBACK    = "setEventTrackingSucceededCallback";
    private static final String COMMAND_SET_EVENT_TRACKING_FAILED_CALLBACK       = "setEventTrackingFailedCallback";
    private static final String COMMAND_SET_SESSION_TRACKING_SUCCEEDED_CALLBACK  = "setSessionTrackingSucceededCallback";
    private static final String COMMAND_SET_SESSION_TRACKING_FAILED_CALLBACK     = "setSessionTrackingFailedCallback";
    private static final String COMMAND_SET_DEFERRED_DEEPLINK_CALLBACK           = "setDeferredDeeplinkCallback";
    private static final String COMMAND_SET_PUSH_TOKEN                           = "setPushToken";
    private static final String COMMAND_TRACK_EVENT                              = "trackEvent";
    private static final String COMMAND_SET_OFFLINE_MODE                         = "setOfflineMode";
    private static final String COMMAND_ON_RESUME                                = "onResume";
    private static final String COMMAND_ON_PAUSE                                 = "onPause";
    private static final String COMMAND_IS_ENABLED                               = "isEnabled";
    private static final String COMMAND_SET_ENABLED                              = "setEnabled";
    private static final String COMMAND_APP_WILL_OPEN_URL                        = "appWillOpenUrl";
    private static final String COMMAND_GET_IDFA                                 = "getIdfa";
    private static final String COMMAND_GET_GOOGLE_AD_ID                         = "getGoogleAdId";
    private static final String COMMAND_ADD_SESSION_CALLBACK_PARAMETER           = "addSessionCallbackParameter";
    private static final String COMMAND_REMOVE_SESSION_CALLBACK_PARAMETER        = "removeSessionCallbackParameter";
    private static final String COMMAND_RESET_SESSION_CALLBACK_PARAMETERS        = "resetSessionCallbackParameters";
    private static final String COMMAND_ADD_SESSION_PARTNER_PARAMETER            = "addSessionPartnerParameter";
    private static final String COMMAND_REMOVE_SESSION_PARTNER_PARAMETER         = "removeSessionPartnerParameter";
    private static final String COMMAND_RESET_SESSION_PARTNER_PARAMETERS         = "resetSessionPartnerParameters";
    private static final String COMMAND_SEND_FIRST_PACKAGES                      = "sendFirstPackages";
    private static final String COMMAND_REFERRER                                 = "setReferrer";

    private static final String ATTRIBUTION_TRACKER_TOKEN   = "trackerToken";
    private static final String ATTRIBUTION_TRACKER_NAME    = "trackerName";
    private static final String ATTRIBUTION_NETWORK         = "network";
    private static final String ATTRIBUTION_CAMPAIGN        = "campaign";
    private static final String ATTRIBUTION_ADGROUP         = "adgroup";
    private static final String ATTRIBUTION_CREATIVE        = "creative";
    private static final String ATTRIBUTION_CLICK_LABEL     = "clickLabel";

    private static final String EVENT_SUCCESS_MESSAGE       = "message";
    private static final String EVENT_SUCCESS_TIMESTAMP     = "timeStamp";
    private static final String EVENT_SUCCESS_ADID          = "adid";
    private static final String EVENT_SUCCESS_EVENT_TOKEN   = "eventToken";
    private static final String EVENT_SUCCESS_JSON_RESPONSE = "jsonResponse";

    private static final String EVENT_FAILED_MESSAGE        = "message";
    private static final String EVENT_FAILED_TIMESTAMP      = "timeStamp";
    private static final String EVENT_FAILED_ADID           = "adid";
    private static final String EVENT_FAILED_EVENT_TOKEN    = "eventToken";
    private static final String EVENT_FAILED_WILL_RETRY     = "willRetry";
    private static final String EVENT_FAILED_JSON_RESPONSE  = "jsonResponse";

    private static final String SESSION_SUCCESS_MESSAGE         = "message";
    private static final String SESSION_SUCCESS_TIMESTAMP       = "timeStamp";
    private static final String SESSION_SUCCESS_ADID            = "adid";
    private static final String SESSION_SUCCESS_JSON_RESPONSE   = "jsonResponse";

    private static final String SESSION_FAILED_MESSAGE          = "message";
    private static final String SESSION_FAILED_TIMESTAMP        = "timeStamp";
    private static final String SESSION_FAILED_ADID             = "adid";
    private static final String SESSION_FAILED_WILL_RETRY       = "willRetry";
    private static final String SESSION_FAILED_JSON_RESPONSE    = "jsonResponse";

    private static CallbackContext googleAdIdCallbackContext;
    private static CallbackContext attributionCallbackContext;
    private static CallbackContext eventTrackingSucceededCallbackContext;
    private static CallbackContext eventTrackingFailedCallbackContext;
    private static CallbackContext sessionTrackingSucceededCallbackContext;
    private static CallbackContext sessionTrackingFailedCallbackContext;
    private static CallbackContext deferredDeeplinkCallbackContext;

    private boolean shouldLaunchDeeplink = false;

    @Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals(COMMAND_CREATE)) {
            final JSONObject jsonParameters = args.optJSONObject(0);
            Map<String, Object> parameters = jsonObjectToMap(jsonParameters);

            String appToken = parameters.get(KEY_APP_TOKEN).toString();
            String environment = parameters.get(KEY_ENVIRONMENT).toString();
            String defaultTracker = parameters.get(KEY_DEFAULT_TRACKER).toString();
            String processName = parameters.get(KEY_PROCESS_NAME).toString();
            String sdkPrefix = parameters.get(KEY_SDK_PREFIX).toString();

            String logLevel = parameters.get(KEY_LOG_LEVEL).toString();
            String userAgent = parameters.get(KEY_USER_AGENT).toString();
            String eventBufferingEnabled = parameters.get(KEY_EVENT_BUFFERING_ENABLED).toString();
            
            boolean isLogLevelSuppress = false;
            boolean sendInBackground = parameters.get(KEY_SEND_IN_BACKGROUND).toString() == "true" ? true : false;
            boolean shouldLaunchDeeplink = parameters.get(KEY_SHOULD_LAUNCH_DEEPLINK).toString() == "true" ? true : false;

            double delayStart = Double.parseDouble(parameters.get(KEY_DELAY_START).toString());

            if (isFieldValid(logLevel) && logLevel.equals("SUPPRESS")) {
                isLogLevelSuppress = true;
            }

            final AdjustConfig adjustConfig = new AdjustConfig(this.cordova.getActivity().getApplicationContext(), appToken, environment, isLogLevelSuppress);

            if (adjustConfig.isValid()) {
                // Log level
                if (isFieldValid(logLevel)) {
                    if (logLevel.equals("VERBOSE")) {
                        adjustConfig.setLogLevel(LogLevel.VERBOSE);
                    } else if (logLevel.equals("DEBUG")) {
                        adjustConfig.setLogLevel(LogLevel.DEBUG);
                    } else if (logLevel.equals("INFO")) {
                        adjustConfig.setLogLevel(LogLevel.INFO);
                    } else if (logLevel.equals("WARN")) {
                        adjustConfig.setLogLevel(LogLevel.WARN);
                    } else if (logLevel.equals("ERROR")) {
                        adjustConfig.setLogLevel(LogLevel.ERROR);
                    } else if (logLevel.equals("ASSERT")) {
                        adjustConfig.setLogLevel(LogLevel.ASSERT);
                    } else if (logLevel.equals("SUPPRESS")) {
                        adjustConfig.setLogLevel(LogLevel.SUPRESS);
                    } else {
                        adjustConfig.setLogLevel(LogLevel.INFO);
                    }
                }

                // Event buffering
                if (isFieldValid(eventBufferingEnabled)) {
                    if (eventBufferingEnabled.equalsIgnoreCase("true") || eventBufferingEnabled.equalsIgnoreCase("false")) {
                        adjustConfig.setEventBufferingEnabled(Boolean.valueOf(eventBufferingEnabled));
                    }
                }

                // SDK prefix
                if (isFieldValid(sdkPrefix)) {
                    adjustConfig.setSdkPrefix(sdkPrefix);
                }

                // Main process name
                if (isFieldValid(processName)) {
                    adjustConfig.setProcessName(processName);
                }

                // Default tracker
                if (isFieldValid(defaultTracker)) {
                    adjustConfig.setDefaultTracker(defaultTracker);
                }

                // User agent
                if (isFieldValid(userAgent)) {
                    adjustConfig.setUserAgent(userAgent);
                }

                // Background tracking
                adjustConfig.setSendInBackground(sendInBackground);

                // Launching deferred deep link
                this.shouldLaunchDeeplink = shouldLaunchDeeplink;

                // Delayed start
                adjustConfig.setDelayStart(delayStart);

                // Attribution callback
                if (attributionCallbackContext != null) {
                    adjustConfig.setOnAttributionChangedListener(this);
                }

                // Event tracking succeeded callback
                if (eventTrackingSucceededCallbackContext != null) {
                    adjustConfig.setOnEventTrackingSucceededListener(this);
                }

                // Event tracking failed callback
                if (eventTrackingFailedCallbackContext != null) {
                    adjustConfig.setOnEventTrackingFailedListener(this);
                }

                // Session tracking succeeded callback
                if (sessionTrackingSucceededCallbackContext != null) {
                    adjustConfig.setOnSessionTrackingSucceededListener(this);
                }

                // Session tracking failed callback
                if (sessionTrackingFailedCallbackContext != null) {
                    adjustConfig.setOnSessionTrackingFailedListener(this);
                }

                // Deferred deeplink callback listener
                if (deferredDeeplinkCallbackContext != null) {
                    adjustConfig.setOnDeeplinkResponseListener(this);
                }

                Adjust.onCreate(adjustConfig);

                // Needed because Cordova doesn't launch 'resume' event on app start.
                // It initializes it only when app comes back from the background.
                Adjust.onResume();
            }

            return true;
        } else if (action.equals(COMMAND_SET_ATTRIBUTION_CALLBACK)) {
            AdjustCordova.attributionCallbackContext = callbackContext;

            return true;
        } else if (action.equals(COMMAND_SET_EVENT_TRACKING_SUCCEEDED_CALLBACK)) {
            AdjustCordova.eventTrackingSucceededCallbackContext = callbackContext;

            return true;
        } else if (action.equals(COMMAND_SET_EVENT_TRACKING_FAILED_CALLBACK)) {
            AdjustCordova.eventTrackingFailedCallbackContext = callbackContext;

            return true;
        } else if (action.equals(COMMAND_SET_SESSION_TRACKING_SUCCEEDED_CALLBACK)) {
            AdjustCordova.sessionTrackingSucceededCallbackContext = callbackContext;

            return true;
        } else if (action.equals(COMMAND_SET_SESSION_TRACKING_FAILED_CALLBACK)) {
            AdjustCordova.sessionTrackingFailedCallbackContext = callbackContext;

            return true;
        } else if (action.equals(COMMAND_SET_DEFERRED_DEEPLINK_CALLBACK)) {
            AdjustCordova.deferredDeeplinkCallbackContext = callbackContext;

            return true;
        } else if (action.equals(COMMAND_GET_GOOGLE_AD_ID)) {
            AdjustCordova.googleAdIdCallbackContext = callbackContext;

            // Google ad id callback
            if (googleAdIdCallbackContext != null) {
                Adjust.getGoogleAdId(this.cordova.getActivity().getApplicationContext(), this);
            }

            return true;
        } else if (action.equals(COMMAND_TRACK_EVENT)) {
            JSONObject jsonParameters = args.optJSONObject(0);
            Map<String, Object> parameters = jsonObjectToMap(jsonParameters);

            String eventToken = parameters.get(KEY_EVENT_TOKEN).toString();
            String revenue = parameters.get(KEY_REVENUE).toString();
            String currency = parameters.get(KEY_CURRENCY).toString();
            String transactionId = parameters.get(KEY_TRANSACTION_ID).toString();

            JSONArray partnerParametersJson = (JSONArray)parameters.get(KEY_PARTNER_PARAMETERS);
            JSONArray callbackParametersJson = (JSONArray)parameters.get(KEY_CALLBACK_PARAMETERS);
            String[] partnerParameters = jsonArrayToArray(partnerParametersJson);
            String[] callbackParameters = jsonArrayToArray(callbackParametersJson);

            final AdjustEvent adjustEvent = new AdjustEvent(eventToken);

            if (adjustEvent.isValid()) {
                if (isFieldValid(revenue)) {
                    try {
                        double revenueValue = Double.parseDouble(revenue);

                        adjustEvent.setRevenue(revenueValue, currency);
                    } catch (Exception e) {
                        ILogger logger = AdjustFactory.getLogger();
                        logger.error("Unable to parse revenue");
                    }
                }

                for (int i = 0; i < callbackParameters.length; i +=2) {
                    String key = callbackParameters[i];
                    String value = callbackParameters[i+1];

                    adjustEvent.addCallbackParameter(key, value);
                }

                for (int i = 0; i < partnerParameters.length; i += 2) {
                    String key = partnerParameters[i];
                    String value = partnerParameters[i+1];

                    adjustEvent.addPartnerParameter(key, value);
                }

                if (isFieldValid(transactionId)) {
                    adjustEvent.setOrderId(transactionId);
                }

                Adjust.trackEvent(adjustEvent);
            }

            return true;
        } else if (action.equals(COMMAND_SET_OFFLINE_MODE)) {
            final Boolean enabled = args.getBoolean(0);
            Adjust.setOfflineMode(enabled);
            
            return true;
        } else if (action.equals(COMMAND_SET_PUSH_TOKEN)) {
            final String token = args.getString(0);
            Adjust.setPushToken(token);
            
            return true;
        } else if (action.equals(COMMAND_ON_PAUSE)) {
            Adjust.onPause();
            
            return true;
        } else if (action.equals(COMMAND_ON_RESUME)) {
            Adjust.onResume();
            
            return true;
        } else if (action.equals(COMMAND_SET_ENABLED)) {
            final Boolean enabled = args.getBoolean(0);
            Adjust.setEnabled(enabled);
            
            return true;
        } else if (action.equals(COMMAND_IS_ENABLED)) {
            Boolean isEnabled = Adjust.isEnabled();
            PluginResult pluginResult = new PluginResult(Status.OK, isEnabled);
            
            callbackContext.sendPluginResult(pluginResult);

            return true;
        } else if (action.equals(COMMAND_APP_WILL_OPEN_URL)) {
            String url = args.getString(0);
            final Uri uri = Uri.parse(url);
            
            Adjust.appWillOpenUrl(uri);
            
            return true;
        } else if (action.equals(COMMAND_GET_IDFA)) {
            return true;
        } else if (action.equals(COMMAND_ADD_SESSION_CALLBACK_PARAMETER)) {
            final String key = args.getString(0);
            final String value = args.getString(1);
            
            Adjust.addSessionCallbackParameter(key, value);
            
            return true;
        } else if (action.equals(COMMAND_REMOVE_SESSION_CALLBACK_PARAMETER)) {
            final String key = args.getString(0);
            
            Adjust.removeSessionCallbackParameter(key);
            
            return true;
        } else if (action.equals(COMMAND_RESET_SESSION_CALLBACK_PARAMETERS)) {
            Adjust.resetSessionCallbackParameters();
            
            return true;
        } else if (action.equals(COMMAND_ADD_SESSION_PARTNER_PARAMETER)) {
            final String key = args.getString(0);
            final String value = args.getString(1);
            
            Adjust.addSessionPartnerParameter(key, value);
            
            return true;
        } else if (action.equals(COMMAND_REMOVE_SESSION_PARTNER_PARAMETER)) {
            final String key = args.getString(0);
            
            Adjust.removeSessionPartnerParameter(key);
            
            return true;
        } else if (action.equals(COMMAND_RESET_SESSION_PARTNER_PARAMETERS)) {
            Adjust.resetSessionPartnerParameters();
            
            return true;
        } else if (action.equals(COMMAND_SEND_FIRST_PACKAGES)) {
            Adjust.sendFirstPackages();
            
            return true;
        } else if (action.equals(COMMAND_REFERRER)) {
            final String referrer = args.getString(0);
            
            Adjust.setReferrer(referrer);
            
            return true;
        }

        String errorMessage = String.format("Invalid call (%s)", action);
        Logger logger = (Logger)AdjustFactory.getLogger();
        
        logger.error(errorMessage);

        return false;
    }

    @Override
    public void onGoogleAdIdRead(String playAdId) {
        PluginResult pluginResult = new PluginResult(Status.OK, playAdId);
        pluginResult.setKeepCallback(true);

        googleAdIdCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onAttributionChanged(AdjustAttribution attribution) {
        JSONObject attributionJsonData = new JSONObject(getAttributionDictionary(attribution));
        PluginResult pluginResult = new PluginResult(Status.OK, attributionJsonData);
        pluginResult.setKeepCallback(true);

        attributionCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onFinishedEventTrackingSucceeded(AdjustEventSuccess event) {
        JSONObject jsonData = new JSONObject(getEventTrackingSucceededDictionary(event));
        PluginResult pluginResult = new PluginResult(Status.OK, jsonData);
        pluginResult.setKeepCallback(true);

        eventTrackingSucceededCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onFinishedEventTrackingFailed(AdjustEventFailure event) {
        JSONObject jsonData = new JSONObject(getEventTrackingFailedDictionary(event));
        PluginResult pluginResult = new PluginResult(Status.OK, jsonData);
        pluginResult.setKeepCallback(true);

        eventTrackingFailedCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onFinishedSessionTrackingSucceeded(AdjustSessionSuccess session) {
        JSONObject jsonData = new JSONObject(getSessionTrackingSucceededDictionary(session));
        PluginResult pluginResult = new PluginResult(Status.OK, jsonData);
        pluginResult.setKeepCallback(true);

        sessionTrackingSucceededCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onFinishedSessionTrackingFailed(AdjustSessionFailure session) {
        JSONObject jsonData = new JSONObject(getSessionTrackingFailedDictionary(session));
        PluginResult pluginResult = new PluginResult(Status.OK, jsonData);
        pluginResult.setKeepCallback(true);

        sessionTrackingFailedCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public boolean launchReceivedDeeplink(Uri deeplink) {
        PluginResult pluginResult = new PluginResult(Status.OK, deeplink.toString());
        pluginResult.setKeepCallback(true);

        deferredDeeplinkCallbackContext.sendPluginResult(pluginResult);

        return this.shouldLaunchDeeplink;
    }

    boolean isFieldValid(String field) {
        if (field != null) {
            if (!field.equals("") && !field.equals("null")) {
                return true;
            }
        }

        return false;
    }

    private String[] jsonArrayToArray(JSONArray jsonArray) throws JSONException {
        if (jsonArray != null) { 
            String[] array = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                array[i] = jsonArray.get(i).toString();
            }

            return array;
        }

        return null;
    }

    private Map<String, Object> jsonObjectToMap(JSONObject jsonObject) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>(jsonObject.length());

        @SuppressWarnings("unchecked")
        Iterator<String> jsonObjectIterator = jsonObject.keys();

        while (jsonObjectIterator.hasNext()) {
            String key = jsonObjectIterator.next();
            map.put(key, jsonObject.get(key));
        }

        return map;
    }

    private Map<String, String> getEventTrackingSucceededDictionary(AdjustEventSuccess event) {
        Map<String, String> dict = new HashMap<String, String>();

        // Message
        if (event.message != null) {
            dict.put(EVENT_SUCCESS_MESSAGE, event.message);
        } else {
            dict.put(EVENT_SUCCESS_MESSAGE, "");
        }

        // Timestamp
        if (event.timestamp != null) {
            dict.put(EVENT_SUCCESS_TIMESTAMP, event.timestamp);
        } else {
            dict.put(EVENT_SUCCESS_TIMESTAMP, "");
        }

        // Adid
        if (event.adid != null) {
            dict.put(EVENT_SUCCESS_ADID, event.adid);
        } else {
            dict.put(EVENT_SUCCESS_ADID, "");
        }

        // Event token
        if (event.eventToken != null) {
            dict.put(EVENT_SUCCESS_EVENT_TOKEN, event.eventToken);
        } else {
            dict.put(EVENT_SUCCESS_EVENT_TOKEN, "");
        }

        // JSON response
        if (event.jsonResponse != null) {
            dict.put(EVENT_SUCCESS_JSON_RESPONSE, event.jsonResponse.toString());
        } else {
            dict.put(EVENT_SUCCESS_JSON_RESPONSE, "");
        }

        return dict;
    }

    private Map<String, String> getEventTrackingFailedDictionary(AdjustEventFailure event) {
        Map<String, String> dict = new HashMap<String, String>();

        // Message
        if (event.message != null) {
            dict.put(EVENT_FAILED_MESSAGE, event.message);
        } else {
            dict.put(EVENT_FAILED_MESSAGE, "");
        }

        // Timestamp
        if (event.timestamp != null) {
            dict.put(EVENT_FAILED_TIMESTAMP, event.timestamp);
        } else {
            dict.put(EVENT_FAILED_TIMESTAMP, "");
        }

        // Adid
        if (event.adid != null) {
            dict.put(EVENT_FAILED_ADID, event.adid);
        } else {
            dict.put(EVENT_FAILED_ADID, "");
        }

        // Event Token
        if (event.eventToken != null) {
            dict.put(EVENT_FAILED_EVENT_TOKEN, event.eventToken);
        } else {
            dict.put(EVENT_FAILED_EVENT_TOKEN, "");
        }

        // Will retry
        if (event.willRetry) {
            dict.put(EVENT_FAILED_WILL_RETRY, "true");
        } else {
            dict.put(EVENT_FAILED_WILL_RETRY, "false");
        }

        // JSON response
        if (event.jsonResponse != null) {
            dict.put(EVENT_FAILED_JSON_RESPONSE, event.jsonResponse.toString());
        } else {
            dict.put(EVENT_FAILED_JSON_RESPONSE, "");
        }

        return dict;
    }

    private Map<String, String> getAttributionDictionary(AdjustAttribution attribution) {
        Map<String, String> dict = new HashMap<String, String>();

        // Tracker token
        if (attribution.trackerToken != null) {
            dict.put(ATTRIBUTION_TRACKER_TOKEN, attribution.trackerToken);
        } else {
            dict.put(ATTRIBUTION_TRACKER_TOKEN, "");
        }

        // Tracker name
        if (attribution.trackerName != null) {
            dict.put(ATTRIBUTION_TRACKER_NAME, attribution.trackerName);
        } else {
            dict.put(ATTRIBUTION_TRACKER_NAME, "");
        }

        // Network
        if (attribution.network != null) {
            dict.put(ATTRIBUTION_NETWORK, attribution.network);
        } else {
            dict.put(ATTRIBUTION_NETWORK, "");
        }

        // Campaign
        if (attribution.campaign != null) {
            dict.put(ATTRIBUTION_CAMPAIGN, attribution.campaign);
        } else {
            dict.put(ATTRIBUTION_CAMPAIGN, "");
        }

        // Adgroup
        if (attribution.adgroup != null) {
            dict.put(ATTRIBUTION_ADGROUP, attribution.adgroup);
        } else {
            dict.put(ATTRIBUTION_ADGROUP, "");
        }

        // Creative
        if (attribution.creative != null) {
            dict.put(ATTRIBUTION_CREATIVE, attribution.creative);
        } else {
            dict.put(ATTRIBUTION_CREATIVE, "");
        }

        // Click label
        if (attribution.clickLabel != null) {
            dict.put(ATTRIBUTION_CLICK_LABEL, attribution.clickLabel);
        } else {
            dict.put(ATTRIBUTION_CLICK_LABEL, "");
        }

        return dict;
    }

    private Map<String, String> getSessionTrackingSucceededDictionary(AdjustSessionSuccess session) {
        Map<String, String> dict = new HashMap<String, String>();

        // Message
        if (session.message != null) {
            dict.put(SESSION_SUCCESS_MESSAGE, session.message);
        } else {
            dict.put(SESSION_SUCCESS_MESSAGE, "");
        }

        // Timestamp
        if (session.timestamp != null) {
            dict.put(SESSION_SUCCESS_TIMESTAMP, session.timestamp);
        } else {
            dict.put(SESSION_SUCCESS_TIMESTAMP, "");
        }

        // Adid
        if (session.adid != null) {
            dict.put(SESSION_SUCCESS_ADID, session.adid);
        } else {
            dict.put(SESSION_SUCCESS_ADID, "");
        }

        // JSON response
        if (session.jsonResponse != null) {
            dict.put(SESSION_SUCCESS_JSON_RESPONSE, session.jsonResponse.toString());
        } else {
            dict.put(SESSION_SUCCESS_JSON_RESPONSE, "");
        }

        return dict;
    }

    private Map<String, String> getSessionTrackingFailedDictionary(AdjustSessionFailure session) {
        Map<String, String> dict = new HashMap<String, String>();

        // Message
        if (session.message != null) {
            dict.put(SESSION_FAILED_MESSAGE, session.message);
        } else {
            dict.put(SESSION_FAILED_MESSAGE, "");
        }

        // Timestamp
        if (session.timestamp != null) {
            dict.put(SESSION_FAILED_TIMESTAMP, session.timestamp);
        } else {
            dict.put(SESSION_FAILED_TIMESTAMP, "");
        }

        // Adid
        if (session.adid != null) {
            dict.put(SESSION_FAILED_ADID, session.adid);
        } else {
            dict.put(SESSION_FAILED_ADID, "");
        }

        // Will retry
        if (session.willRetry) {
            dict.put(SESSION_FAILED_WILL_RETRY, "true");
        } else {
            dict.put(SESSION_FAILED_WILL_RETRY, "false");
        }

        // JSON response
        if (session.jsonResponse != null) {
            dict.put(SESSION_FAILED_JSON_RESPONSE, session.jsonResponse.toString());
        } else {
            dict.put(SESSION_FAILED_JSON_RESPONSE, "");
        }

        return dict;
    }
}
