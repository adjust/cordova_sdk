package com.adjust.sdk;

import android.net.Uri;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult.Status;


public class AdjustCordova extends CordovaPlugin 
    implements OnAttributionChangedListener, 
               OnEventTrackingSucceededListener,
               OnEventTrackingFailedListener,
               OnSessionTrackingSucceededListener,
               OnSessionTrackingFailedListener,
               OnDeeplinkResponseListener, 
               OnDeviceIdsRead {
    private static final String KEY_APP_TOKEN                      = "appToken";
    private static final String KEY_ENVIRONMENT                    = "environment";
    private static final String KEY_LOG_LEVEL                      = "logLevel";
    private static final String KEY_SDK_PREFIX                     = "sdkPrefix";
    private static final String KEY_PROCESS_NAME                   = "processName";
    private static final String KEY_DEFAULT_TRACKER                = "defaultTracker";
    private static final String KEY_EVENT_BUFFERING_ENABLED        = "eventBufferingEnabled";
    private static final String KEY_EVENT_TOKEN                    = "eventToken";
    private static final String KEY_REVENUE                        = "revenue";
    private static final String KEY_CURRENCY                       = "currency";
    private static final String KEY_TRANSACTION_ID                 = "transactionId";
    private static final String KEY_CALLBACK_PARAMETERS            = "callbackParameters";
    private static final String KEY_PARTNER_PARAMETERS             = "partnerParameters";
    private static final String KEY_SEND_IN_BACKGROUND             = "sendInBackground";
    private static final String KEY_SHOULD_LAUNCH_DEEPLINK         = "shouldLaunchDeeplink";
    private static final String KEY_USER_AGENT                     = "userAgent";
    private static final String KEY_DELAY_START                    = "delayStart";
    private static final String KEY_SECRET_ID                      = "secretId";
    private static final String KEY_INFO_1                         = "info1";
    private static final String KEY_INFO_2                         = "info2";
    private static final String KEY_INFO_3                         = "info3";
    private static final String KEY_INFO_4                         = "info4";
    private static final String KEY_DEVICE_KNOWN                   = "isDeviceKnown";
    private static final String KEY_READ_MOBILE_EQUIPMENT_IDENTITY = "readMobileEquipmentIdentity";
    private static final String KEY_BASE_URL                       = "baseUrl";
    private static final String KEY_BASE_PATH                      = "basePath";
    private static final String KEY_USE_TEST_CONNECTION_OPTIONS    = "useTestConnectionOptions";
    private static final String KEY_TIMER_INTERVAL                 = "timerIntervalInMilliseconds";
    private static final String KEY_TIMER_START                    = "timerStartInMilliseconds";
    private static final String KEY_SESSION_INTERVAL               = "sessionIntervalInMilliseconds";
    private static final String KEY_SUBSESSION_INTERVAL            = "subsessionIntervalInMilliseconds";
    private static final String KEY_TEARDOWN                       = "teardown";
    private static final String KEY_HAS_CONTEXT                    = "hasContext";

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
    private static final String COMMAND_GET_ADID                                 = "getAdid";
    private static final String COMMAND_GET_ATTRIBUTION                          = "getAttribution";
    private static final String COMMAND_GET_GOOGLE_AD_ID                         = "getGoogleAdId";
    private static final String COMMAND_GET_AMAZON_AD_ID                         = "getAmazonAdId";
    private static final String COMMAND_ADD_SESSION_CALLBACK_PARAMETER           = "addSessionCallbackParameter";
    private static final String COMMAND_REMOVE_SESSION_CALLBACK_PARAMETER        = "removeSessionCallbackParameter";
    private static final String COMMAND_RESET_SESSION_CALLBACK_PARAMETERS        = "resetSessionCallbackParameters";
    private static final String COMMAND_ADD_SESSION_PARTNER_PARAMETER            = "addSessionPartnerParameter";
    private static final String COMMAND_REMOVE_SESSION_PARTNER_PARAMETER         = "removeSessionPartnerParameter";
    private static final String COMMAND_RESET_SESSION_PARTNER_PARAMETERS         = "resetSessionPartnerParameters";
    private static final String COMMAND_SEND_FIRST_PACKAGES                      = "sendFirstPackages";
    private static final String COMMAND_SET_REFERRER                             = "setReferrer";
    private static final String COMMAND_SET_TEST_OPTIONS                         = "setTestOptions";
    private static final String COMMAND_TEARDOWN                                 = "teardown";

    private static final String ATTRIBUTION_TRACKER_TOKEN   = "trackerToken";
    private static final String ATTRIBUTION_TRACKER_NAME    = "trackerName";
    private static final String ATTRIBUTION_NETWORK         = "network";
    private static final String ATTRIBUTION_CAMPAIGN        = "campaign";
    private static final String ATTRIBUTION_ADGROUP         = "adgroup";
    private static final String ATTRIBUTION_CREATIVE        = "creative";
    private static final String ATTRIBUTION_CLICK_LABEL     = "clickLabel";
    private static final String ATTRIBUTION_ADID            = "adid";

    private static final String EVENT_SUCCESS_MESSAGE       = "message";
    private static final String EVENT_SUCCESS_TIMESTAMP     = "timestamp";
    private static final String EVENT_SUCCESS_ADID          = "adid";
    private static final String EVENT_SUCCESS_EVENT_TOKEN   = "eventToken";
    private static final String EVENT_SUCCESS_JSON_RESPONSE = "jsonResponse";

    private static final String EVENT_FAILED_MESSAGE        = "message";
    private static final String EVENT_FAILED_TIMESTAMP      = "timestamp";
    private static final String EVENT_FAILED_ADID           = "adid";
    private static final String EVENT_FAILED_EVENT_TOKEN    = "eventToken";
    private static final String EVENT_FAILED_WILL_RETRY     = "willRetry";
    private static final String EVENT_FAILED_JSON_RESPONSE  = "jsonResponse";

    private static final String SESSION_SUCCESS_MESSAGE         = "message";
    private static final String SESSION_SUCCESS_TIMESTAMP       = "timestamp";
    private static final String SESSION_SUCCESS_ADID            = "adid";
    private static final String SESSION_SUCCESS_JSON_RESPONSE   = "jsonResponse";

    private static final String SESSION_FAILED_MESSAGE          = "message";
    private static final String SESSION_FAILED_TIMESTAMP        = "timestamp";
    private static final String SESSION_FAILED_ADID             = "adid";
    private static final String SESSION_FAILED_WILL_RETRY       = "willRetry";
    private static final String SESSION_FAILED_JSON_RESPONSE    = "jsonResponse";

    private CallbackContext attributionCallbackContext;
    private CallbackContext eventTrackingSucceededCallbackContext;
    private CallbackContext eventTrackingFailedCallbackContext;
    private CallbackContext sessionTrackingSucceededCallbackContext;
    private CallbackContext sessionTrackingFailedCallbackContext;
    private CallbackContext deferredDeeplinkCallbackContext;
    private CallbackContext getAdidCallbackContext;
    private CallbackContext getIdfaCallbackContext;
    private CallbackContext getGoogleAdIdCallbackContext;
    private CallbackContext getAmazonAdidCallbackContext;
    private CallbackContext getAttributionCallbackContext;

    private boolean shouldLaunchDeeplink = false;

    @Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals(COMMAND_CREATE)) {
            executeCreate(args);

            return true;
        } else if (action.equals(COMMAND_SET_ATTRIBUTION_CALLBACK)) {
            attributionCallbackContext = callbackContext;

            return true;
        } else if (action.equals(COMMAND_SET_EVENT_TRACKING_SUCCEEDED_CALLBACK)) {
            eventTrackingSucceededCallbackContext = callbackContext;

            return true;
        } else if (action.equals(COMMAND_SET_EVENT_TRACKING_FAILED_CALLBACK)) {
            eventTrackingFailedCallbackContext = callbackContext;

            return true;
        } else if (action.equals(COMMAND_SET_SESSION_TRACKING_SUCCEEDED_CALLBACK)) {
            sessionTrackingSucceededCallbackContext = callbackContext;

            return true;
        } else if (action.equals(COMMAND_SET_SESSION_TRACKING_FAILED_CALLBACK)) {
            sessionTrackingFailedCallbackContext = callbackContext;

            return true;
        } else if (action.equals(COMMAND_SET_DEFERRED_DEEPLINK_CALLBACK)) {
            deferredDeeplinkCallbackContext = callbackContext;

            return true;
        } else if (action.equals(COMMAND_GET_GOOGLE_AD_ID)) {
            getGoogleAdIdCallbackContext = callbackContext;

            // Google Ad Id callback
            if (null != getGoogleAdIdCallbackContext) {
                Adjust.getGoogleAdId(this.cordova.getActivity().getApplicationContext(), this);
            }

            return true;
        } else if (action.equals(COMMAND_GET_AMAZON_AD_ID)) {
            getAmazonAdidCallbackContext = callbackContext;

            if (null != getAmazonAdidCallbackContext) {
                String amazonAdId = Adjust.getAmazonAdId(this.cordova.getActivity().getApplicationContext());

                if (amazonAdId == null) {
                    amazonAdId = "";
                }

                PluginResult pluginResult = new PluginResult(Status.OK, amazonAdId);
                pluginResult.setKeepCallback(true);

                getAmazonAdidCallbackContext.sendPluginResult(pluginResult);
            }

            return true;
        } else if (action.equals(COMMAND_GET_ADID)) {
            getAdidCallbackContext = callbackContext;

            if (null != getAdidCallbackContext) {
                final String adid = Adjust.getAdid();

                PluginResult pluginResult = new PluginResult(Status.OK, adid);
                pluginResult.setKeepCallback(true);

                getAdidCallbackContext.sendPluginResult(pluginResult);
            }

            return true;
        } else if (action.equals(COMMAND_GET_ATTRIBUTION)) {
            getAttributionCallbackContext = callbackContext;

            if (null != getAttributionCallbackContext) {
                final AdjustAttribution attribution = Adjust.getAttribution();

                JSONObject attributionJsonData = new JSONObject(getAttributionDictionary(attribution));
                PluginResult pluginResult = new PluginResult(Status.OK, attributionJsonData);
                pluginResult.setKeepCallback(true);

                getAttributionCallbackContext.sendPluginResult(pluginResult);
            }

            return true;
        } else if (action.equals(COMMAND_GET_IDFA)) {
            getIdfaCallbackContext = callbackContext;

            // Send empty string for IDFA
            final String idfa = "";

            PluginResult pluginResult = new PluginResult(Status.OK, idfa);
            pluginResult.setKeepCallback(true);

            getIdfaCallbackContext.sendPluginResult(pluginResult);

            return true;
        } else if (action.equals(COMMAND_TRACK_EVENT)) {
            executeTrackEvent(args);

            return true;
        } else if (action.equals(COMMAND_SET_OFFLINE_MODE)) {
            final Boolean enabled = args.getBoolean(0);
            Adjust.setOfflineMode(enabled);
            
            return true;
        } else if (action.equals(COMMAND_SET_PUSH_TOKEN)) {
            final String token = args.getString(0);
            Adjust.setPushToken(token, this.cordova.getActivity().getApplicationContext());
            
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
            final Boolean isEnabled = Adjust.isEnabled();
            PluginResult pluginResult = new PluginResult(Status.OK, isEnabled);
            
            callbackContext.sendPluginResult(pluginResult);

            return true;
        } else if (action.equals(COMMAND_APP_WILL_OPEN_URL)) {
            String url = args.getString(0);
            final Uri uri = Uri.parse(url);
            
            Adjust.appWillOpenUrl(uri);
            
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
        } else if (action.equals(COMMAND_SET_REFERRER)) {
            final String referrer = args.getString(0);
            
            Log.wtf("AdjustCordova", "setReferrer: with " + referrer);
            Adjust.setReferrer(referrer, this.cordova.getActivity().getApplicationContext());
            
            return true;
        } else if (action.equals(COMMAND_SET_TEST_OPTIONS)) {
            executeSetTestOptions(args);
            
            return true;
        } else if (action.equals(COMMAND_TEARDOWN)) {
            attributionCallbackContext              = null;
            eventTrackingSucceededCallbackContext   = null;
            eventTrackingFailedCallbackContext      = null;
            sessionTrackingSucceededCallbackContext = null;
            sessionTrackingFailedCallbackContext    = null;
            deferredDeeplinkCallbackContext         = null;
            getAdidCallbackContext                  = null;
            getIdfaCallbackContext                  = null;
            getGoogleAdIdCallbackContext            = null;
            getAmazonAdidCallbackContext            = null;
            getAttributionCallbackContext           = null;
            shouldLaunchDeeplink                    = false;
            
            return true;
        }

        String errorMessage = String.format("Invalid call (%s)", action);
        Logger logger = (Logger)AdjustFactory.getLogger();

        logger.error(errorMessage);

        return false;
    }

    private void executeCreate(final JSONArray args) throws JSONException {
        String params = args.getString(0);
        JSONArray jsonArrayParams = new JSONArray(params);
        JSONObject jsonParameters = jsonArrayParams.optJSONObject(0);
        Map<String, Object> parameters = jsonObjectToMap(jsonParameters);

        String appToken = parameters.get(KEY_APP_TOKEN).toString();
        String environment = parameters.get(KEY_ENVIRONMENT).toString();
        String defaultTracker = parameters.get(KEY_DEFAULT_TRACKER).toString();
        String processName = parameters.get(KEY_PROCESS_NAME).toString();
        String sdkPrefix = parameters.get(KEY_SDK_PREFIX).toString();
        String delayStart = parameters.get(KEY_DELAY_START).toString();

        String logLevel = parameters.get(KEY_LOG_LEVEL).toString();
        String userAgent = parameters.get(KEY_USER_AGENT).toString();

        String secretId = parameters.get(KEY_SECRET_ID).toString();
        String info1 = parameters.get(KEY_INFO_1).toString();
        String info2 = parameters.get(KEY_INFO_2).toString();
        String info3 = parameters.get(KEY_INFO_3).toString();
        String info4 = parameters.get(KEY_INFO_4).toString();

        boolean isLogLevelSuppress = false;
        boolean eventBufferingEnabled = parameters.get(KEY_EVENT_BUFFERING_ENABLED).toString() == "true" ? true : false;
        boolean isDeviceKnown = parameters.get(KEY_DEVICE_KNOWN).toString() == "true" ? true : false;
        boolean sendInBackground = parameters.get(KEY_SEND_IN_BACKGROUND).toString() == "true" ? true : false;
        boolean shouldLaunchDeeplink = parameters.get(KEY_SHOULD_LAUNCH_DEEPLINK).toString() == "true" ? true : false;
        boolean readMobileEquipmentIdentity = parameters.get(KEY_READ_MOBILE_EQUIPMENT_IDENTITY).toString() == "true" ? true : false;

        if (isFieldValid(logLevel) && logLevel.equals("SUPPRESS")) {
            isLogLevelSuppress = true;
        }

        final AdjustConfig adjustConfig = new AdjustConfig(this.cordova.getActivity().getApplicationContext(), appToken, environment, isLogLevelSuppress);

        if (!adjustConfig.isValid()) {
            return;
        }

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

        // App secret
        if (isFieldValid(secretId) && isFieldValid(info1) && isFieldValid(info2) && isFieldValid(info3) && isFieldValid(info4)) {
            try {
                long lSecretId = Long.parseLong(secretId, 10);
                long lInfo1 = Long.parseLong(info1, 10);
                long lInfo2 = Long.parseLong(info2, 10);
                long lInfo3 = Long.parseLong(info3, 10);
                long lInfo4 = Long.parseLong(info4, 10);

                adjustConfig.setAppSecret(lSecretId, lInfo1, lInfo2, lInfo3, lInfo4);
            } catch(NumberFormatException ignored) {}
        }
        
        // Read mobile equipment identity
        adjustConfig.setReadMobileEquipmentIdentity(readMobileEquipmentIdentity);

        // Event buffering
        adjustConfig.setEventBufferingEnabled(eventBufferingEnabled);

        // Set Device Known
        adjustConfig.setDeviceKnown(isDeviceKnown);

        // Background tracking
        adjustConfig.setSendInBackground(sendInBackground);

        // Launching deferred deep link
        this.shouldLaunchDeeplink = shouldLaunchDeeplink;

        // Delayed start
        if (isFieldValid(delayStart)) {
            try {
                double dDelayStart = Double.parseDouble(delayStart);
                adjustConfig.setDelayStart(dDelayStart);
            } catch(NumberFormatException ignored) {}
        }

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

    private void executeTrackEvent(final JSONArray args) throws JSONException {
        String params = args.getString(0);
        JSONArray jsonArrayParams = new JSONArray(params);
        JSONObject jsonParameters = jsonArrayParams.optJSONObject(0);
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
        
        if (!adjustEvent.isValid()) {
            return;
        }

        // Revenue and currency
        if (isFieldValid(revenue) && isFieldValid(currency)) {
            try {
                double revenueValue = Double.parseDouble(revenue);
                adjustEvent.setRevenue(revenueValue, currency);
            } catch (Exception e) {
                ILogger logger = AdjustFactory.getLogger();
                logger.error("Unable to parse revenue");
            }
        }

        // Callback parameters
        for (int i = 0; i < callbackParameters.length; i +=2) {
            String key = callbackParameters[i];
            String value = callbackParameters[i+1];

            adjustEvent.addCallbackParameter(key, value);
        }

        // Partner parameters
        for (int i = 0; i < partnerParameters.length; i += 2) {
            String key = partnerParameters[i];
            String value = partnerParameters[i+1];

            adjustEvent.addPartnerParameter(key, value);
        }

        // Order ID
        if (isFieldValid(transactionId)) {
            adjustEvent.setOrderId(transactionId);
        }

        Log.wtf("AdjustCordova", "executeTrackEvent: " + adjustEvent);
        Adjust.trackEvent(adjustEvent);
    }

    private void executeSetTestOptions(final JSONArray args) throws JSONException {
        JSONObject jsonParameters = args.optJSONObject(0);
        Map<String, Object> parameters = jsonObjectToMap(jsonParameters);

        final AdjustTestOptions testOptions = new AdjustTestOptions();

        if (!jsonParameters.isNull(KEY_HAS_CONTEXT)) {
            try {
                boolean value = jsonParameters.getBoolean(KEY_HAS_CONTEXT);
                if (value) {
                    testOptions.context = this.cordova.getActivity().getApplicationContext();
                }
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("Unable to parse has context");
            }
        }

        if (!jsonParameters.isNull(KEY_BASE_URL)) {
            try {
                String value = jsonParameters.getString(KEY_BASE_URL);
                testOptions.baseUrl = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("Unable to parse base url");
            }
        }

        if (!jsonParameters.isNull(KEY_BASE_PATH)) {
            try {
                String value = jsonParameters.getString(KEY_BASE_PATH);
                testOptions.basePath = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("Unable to parse base path");
            }
        }

        if (!jsonParameters.isNull(KEY_USE_TEST_CONNECTION_OPTIONS)) {
            try {
                boolean value = jsonParameters.getBoolean(KEY_USE_TEST_CONNECTION_OPTIONS);
                testOptions.useTestConnectionOptions = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("Unable to parse use test connection options");
            }
        }

        if (!jsonParameters.isNull(KEY_TIMER_INTERVAL)) {
            try {
                long value = jsonParameters.getLong(KEY_TIMER_INTERVAL);
                testOptions.timerIntervalInMilliseconds = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("Unable to parse timer interval");
            }
        }

        if (!jsonParameters.isNull(KEY_TIMER_START)) {
            try {
                long value = jsonParameters.getLong(KEY_TIMER_START);
                testOptions.timerStartInMilliseconds = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("Unable to parse timer start");
            }
        }

        if (!jsonParameters.isNull(KEY_SESSION_INTERVAL)) {
            try {
                long value = jsonParameters.getLong(KEY_SESSION_INTERVAL);
                testOptions.sessionIntervalInMilliseconds = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("Unable to parse session interval");
            }
        } 

        if (!jsonParameters.isNull(KEY_SUBSESSION_INTERVAL)) {
            try {
                long value = jsonParameters.getLong(KEY_SUBSESSION_INTERVAL);
                testOptions.subsessionIntervalInMilliseconds = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("Unable to parse subsession interval");
            }
        }

        if (!jsonParameters.isNull(KEY_TEARDOWN)) {
            try {
                boolean teardown = jsonParameters.getBoolean(KEY_TEARDOWN);
                testOptions.teardown = teardown;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("Unable to parse teardown");
            }
        }

        Adjust.setTestOptions(testOptions);
    }

    @Override
    public void onAttributionChanged(AdjustAttribution attribution) {
        if (attributionCallbackContext == null) {
            return;
        }

        JSONObject attributionJsonData = new JSONObject(getAttributionDictionary(attribution));
        PluginResult pluginResult = new PluginResult(Status.OK, attributionJsonData);
        pluginResult.setKeepCallback(true);

        attributionCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onFinishedEventTrackingSucceeded(AdjustEventSuccess event) {
        if (eventTrackingSucceededCallbackContext == null) {
            return;
        }

        JSONObject jsonData = new JSONObject(getEventTrackingSucceededDictionary(event));
        PluginResult pluginResult = new PluginResult(Status.OK, jsonData);
        pluginResult.setKeepCallback(true);

        eventTrackingSucceededCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onFinishedEventTrackingFailed(AdjustEventFailure event) {
        if (eventTrackingFailedCallbackContext == null) {
            return;
        }

        JSONObject jsonData = new JSONObject(getEventTrackingFailedDictionary(event));
        PluginResult pluginResult = new PluginResult(Status.OK, jsonData);
        pluginResult.setKeepCallback(true);

        eventTrackingFailedCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onFinishedSessionTrackingSucceeded(AdjustSessionSuccess session) {
        if (sessionTrackingSucceededCallbackContext == null) {
            return;
        }

        JSONObject jsonData = new JSONObject(getSessionTrackingSucceededDictionary(session));
        PluginResult pluginResult = new PluginResult(Status.OK, jsonData);
        pluginResult.setKeepCallback(true);

        sessionTrackingSucceededCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onFinishedSessionTrackingFailed(AdjustSessionFailure session) {
        if (sessionTrackingFailedCallbackContext == null) {
            return;
        }

        JSONObject jsonData = new JSONObject(getSessionTrackingFailedDictionary(session));
        PluginResult pluginResult = new PluginResult(Status.OK, jsonData);
        pluginResult.setKeepCallback(true);

        sessionTrackingFailedCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public boolean launchReceivedDeeplink(Uri deeplink) {
        if (deferredDeeplinkCallbackContext != null) {
            PluginResult pluginResult = new PluginResult(Status.OK, deeplink.toString());
            pluginResult.setKeepCallback(true);

            deferredDeeplinkCallbackContext.sendPluginResult(pluginResult);
        }

        return this.shouldLaunchDeeplink;
    }

    @Override
    public void onGoogleAdIdRead(String playAdId) {
        if (getGoogleAdIdCallbackContext == null) {
            return;
        }

        PluginResult pluginResult = new PluginResult(Status.OK, playAdId);
        pluginResult.setKeepCallback(true);

        getGoogleAdIdCallbackContext.sendPluginResult(pluginResult);
    }

    boolean isFieldValid(String field) {
        return field != null && !field.equals("") && !field.equals("null");
    }

    private String[] jsonArrayToArray(JSONArray jsonArray) throws JSONException {
        if (jsonArray == null) {
            return null;
        }

        String[] array = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            array[i] = jsonArray.get(i).toString();
        }

        return array;
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

        addValueOrEmpty(dict, EVENT_SUCCESS_MESSAGE, event.message);
        addValueOrEmpty(dict, EVENT_SUCCESS_TIMESTAMP, event.timestamp);
        addValueOrEmpty(dict, EVENT_SUCCESS_ADID, event.adid);
        addValueOrEmpty(dict, EVENT_SUCCESS_EVENT_TOKEN, event.eventToken);
        addValueOrEmpty(dict, EVENT_SUCCESS_JSON_RESPONSE, event.jsonResponse);

        return dict;
    }

    private Map<String, String> getEventTrackingFailedDictionary(AdjustEventFailure event) {
        Map<String, String> dict = new HashMap<String, String>();

        addValueOrEmpty(dict, EVENT_FAILED_MESSAGE, event.message);
        addValueOrEmpty(dict, EVENT_FAILED_TIMESTAMP, event.timestamp);
        addValueOrEmpty(dict, EVENT_FAILED_ADID, event.adid);
        addValueOrEmpty(dict, EVENT_FAILED_EVENT_TOKEN, event.eventToken);
        addValueOrEmpty(dict, EVENT_FAILED_WILL_RETRY, event.willRetry ? "true" : "false");;
        addValueOrEmpty(dict, EVENT_FAILED_JSON_RESPONSE, event.jsonResponse);

        return dict;
    }

    private Map<String, String> getAttributionDictionary(AdjustAttribution attribution) {
        Map<String, String> dict = new HashMap<String, String>();

        addValueOrEmpty(dict, ATTRIBUTION_TRACKER_TOKEN, attribution.trackerToken);
        addValueOrEmpty(dict, ATTRIBUTION_TRACKER_NAME, attribution.trackerName);
        addValueOrEmpty(dict, ATTRIBUTION_NETWORK, attribution.network);
        addValueOrEmpty(dict, ATTRIBUTION_CAMPAIGN, attribution.campaign);
        addValueOrEmpty(dict, ATTRIBUTION_ADGROUP, attribution.adgroup);
        addValueOrEmpty(dict, ATTRIBUTION_CREATIVE, attribution.creative);
        addValueOrEmpty(dict, ATTRIBUTION_CLICK_LABEL, attribution.clickLabel);
        addValueOrEmpty(dict, ATTRIBUTION_ADID, attribution.adid);

        return dict;
    }

    private Map<String, String> getSessionTrackingSucceededDictionary(AdjustSessionSuccess session) {
        Map<String, String> dict = new HashMap<String, String>();

        addValueOrEmpty(dict, SESSION_SUCCESS_MESSAGE, session.message);
        addValueOrEmpty(dict, SESSION_SUCCESS_TIMESTAMP, session.timestamp);
        addValueOrEmpty(dict, SESSION_SUCCESS_ADID, session.adid);
        addValueOrEmpty(dict, SESSION_SUCCESS_JSON_RESPONSE, session.jsonResponse);

        return dict;
    }

    private Map<String, String> getSessionTrackingFailedDictionary(AdjustSessionFailure session) {
        Map<String, String> dict = new HashMap<String, String>();

        addValueOrEmpty(dict, SESSION_FAILED_MESSAGE, session.message);
        addValueOrEmpty(dict, SESSION_FAILED_TIMESTAMP, session.timestamp);
        addValueOrEmpty(dict, SESSION_FAILED_ADID, session.adid);
        addValueOrEmpty(dict, SESSION_FAILED_WILL_RETRY, session.willRetry ? "true" : "false");;
        addValueOrEmpty(dict, SESSION_FAILED_JSON_RESPONSE, session.jsonResponse);

        return dict;
    }

    private void addValueOrEmpty(Map<String, String> dict, String key, Object value){
        if (value != null) {
            dict.put(key, value.toString());
        } else {
            dict.put(key, "");
        }
    }
}
