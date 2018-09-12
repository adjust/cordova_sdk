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

import static com.adjust.sdk.AdjustCordovaUtils.*;

public class AdjustCordova extends CordovaPlugin 
    implements OnAttributionChangedListener, 
               OnEventTrackingSucceededListener,
               OnEventTrackingFailedListener,
               OnSessionTrackingSucceededListener,
               OnSessionTrackingFailedListener,
               OnDeeplinkResponseListener, 
               OnDeviceIdsRead {

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
        } else if (action.equals(COMMAND_SET_ATTRIBUTION_CALLBACK)) {
            attributionCallbackContext = callbackContext;
        } else if (action.equals(COMMAND_SET_EVENT_TRACKING_SUCCEEDED_CALLBACK)) {
            eventTrackingSucceededCallbackContext = callbackContext;
        } else if (action.equals(COMMAND_SET_EVENT_TRACKING_FAILED_CALLBACK)) {
            eventTrackingFailedCallbackContext = callbackContext;
        } else if (action.equals(COMMAND_SET_SESSION_TRACKING_SUCCEEDED_CALLBACK)) {
            sessionTrackingSucceededCallbackContext = callbackContext;
        } else if (action.equals(COMMAND_SET_SESSION_TRACKING_FAILED_CALLBACK)) {
            sessionTrackingFailedCallbackContext = callbackContext;
        } else if (action.equals(COMMAND_SET_DEFERRED_DEEPLINK_CALLBACK)) {
            deferredDeeplinkCallbackContext = callbackContext;
        } else if (action.equals(COMMAND_GET_GOOGLE_AD_ID)) {
            getGoogleAdIdCallbackContext = callbackContext;

            // Google Ad Id callback
            if (getGoogleAdIdCallbackContext != null) {
                Adjust.getGoogleAdId(this.cordova.getActivity().getApplicationContext(), this);
            }
        } else if (action.equals(COMMAND_GET_AMAZON_AD_ID)) {
            getAmazonAdidCallbackContext = callbackContext;

            if (getAmazonAdidCallbackContext != null) {
                String amazonAdId = Adjust.getAmazonAdId(this.cordova.getActivity().getApplicationContext());

                if (amazonAdId == null) {
                    amazonAdId = "";
                }

                PluginResult pluginResult = new PluginResult(Status.OK, amazonAdId);
                pluginResult.setKeepCallback(true);

                getAmazonAdidCallbackContext.sendPluginResult(pluginResult);
            }
        } else if (action.equals(COMMAND_GET_ADID)) {
            getAdidCallbackContext = callbackContext;

            if (getAdidCallbackContext != null) {
                final String adid = Adjust.getAdid();

                PluginResult pluginResult = new PluginResult(Status.OK, adid);
                pluginResult.setKeepCallback(true);

                getAdidCallbackContext.sendPluginResult(pluginResult);
            }
        } else if (action.equals(COMMAND_GET_ATTRIBUTION)) {
            getAttributionCallbackContext = callbackContext;

            if (getAttributionCallbackContext != null) {
                final AdjustAttribution attribution = Adjust.getAttribution();

                JSONObject attributionJsonData = new JSONObject(getAttributionDictionary(attribution));
                PluginResult pluginResult = new PluginResult(Status.OK, attributionJsonData);
                pluginResult.setKeepCallback(true);

                getAttributionCallbackContext.sendPluginResult(pluginResult);
            }
        } else if (action.equals(COMMAND_GET_IDFA)) {
            getIdfaCallbackContext = callbackContext;

            // Send empty string for IDFA
            final String idfa = "";

            PluginResult pluginResult = new PluginResult(Status.OK, idfa);
            pluginResult.setKeepCallback(true);

            getIdfaCallbackContext.sendPluginResult(pluginResult);
        } else if (action.equals(COMMAND_TRACK_EVENT)) {
            executeTrackEvent(args);
        } else if (action.equals(COMMAND_SET_OFFLINE_MODE)) {
            final Boolean enabled = args.getBoolean(0);
            Adjust.setOfflineMode(enabled);
        } else if (action.equals(COMMAND_SET_PUSH_TOKEN)) {
            final String token = args.getString(0);
            Adjust.setPushToken(token, this.cordova.getActivity().getApplicationContext());
        } else if (action.equals(COMMAND_ON_PAUSE)) {
            Adjust.onPause();
        } else if (action.equals(COMMAND_ON_RESUME)) {
            Adjust.onResume();
        } else if (action.equals(COMMAND_SET_ENABLED)) {
            final Boolean enabled = args.getBoolean(0);
            Adjust.setEnabled(enabled);
        } else if (action.equals(COMMAND_IS_ENABLED)) {
            final Boolean isEnabled = Adjust.isEnabled();
            PluginResult pluginResult = new PluginResult(Status.OK, isEnabled);
            callbackContext.sendPluginResult(pluginResult);
        } else if (action.equals(COMMAND_APP_WILL_OPEN_URL)) {
            String url = args.getString(0);
            final Uri uri = Uri.parse(url);
            Adjust.appWillOpenUrl(uri, this.cordova.getActivity().getApplicationContext());
        } else if (action.equals(COMMAND_ADD_SESSION_CALLBACK_PARAMETER)) {
            final String key = args.getString(0);
            final String value = args.getString(1);
            Adjust.addSessionCallbackParameter(key, value);
        } else if (action.equals(COMMAND_REMOVE_SESSION_CALLBACK_PARAMETER)) {
            final String key = args.getString(0);
            Adjust.removeSessionCallbackParameter(key);
        } else if (action.equals(COMMAND_RESET_SESSION_CALLBACK_PARAMETERS)) {
            Adjust.resetSessionCallbackParameters();
        } else if (action.equals(COMMAND_ADD_SESSION_PARTNER_PARAMETER)) {
            final String key = args.getString(0);
            final String value = args.getString(1);
            Adjust.addSessionPartnerParameter(key, value);
        } else if (action.equals(COMMAND_REMOVE_SESSION_PARTNER_PARAMETER)) {
            final String key = args.getString(0);
            Adjust.removeSessionPartnerParameter(key);
        } else if (action.equals(COMMAND_RESET_SESSION_PARTNER_PARAMETERS)) {
            Adjust.resetSessionPartnerParameters();
        } else if (action.equals(COMMAND_SEND_FIRST_PACKAGES)) {
            Adjust.sendFirstPackages();
        } else if (action.equals(COMMAND_GDPR_FORGET_ME)) {
            Adjust.gdprForgetMe(this.cordova.getActivity().getApplicationContext());
        } else if (action.equals(COMMAND_SET_REFERRER)) {
            final String referrer = args.getString(0);
            Adjust.setReferrer(referrer, this.cordova.getActivity().getApplicationContext());
        } else if (action.equals(COMMAND_SET_TEST_OPTIONS)) {
            executeSetTestOptions(args);
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
        } else {
            Logger logger = (Logger)AdjustFactory.getLogger();
            logger.error(String.format("Invalid call (%s)", action));
            return false;    
        }

        return true;
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

        if (!jsonParameters.isNull(KEY_GDPR_URL)) {
            try {
                String value = jsonParameters.getString(KEY_GDPR_URL);
                testOptions.gdprUrl = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("Unable to parse gdpr url");
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

        if (!jsonParameters.isNull(KEY_GDPR_PATH)) {
            try {
                String value = jsonParameters.getString(KEY_GDPR_PATH);
                testOptions.gdprPath = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("Unable to parse gdpr path");
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

        if (!jsonParameters.isNull(KEY_NO_BACKOFF_WAIT)) {
            try {
                boolean noBackoffWait = jsonParameters.getBoolean(KEY_NO_BACKOFF_WAIT);
                testOptions.noBackoffWait = noBackoffWait;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("Unable to parse noBackoffWait");
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

    private Map<String, String> getEventTrackingSucceededDictionary(AdjustEventSuccess event) {
        Map<String, String> dict = new HashMap<String, String>();
        addValueOrEmpty(dict, KEY_MESSAGE, event.message);
        addValueOrEmpty(dict, KEY_TIMESTAMP, event.timestamp);
        addValueOrEmpty(dict, KEY_ADID, event.adid);
        addValueOrEmpty(dict, KEY_EVENT_TOKEN, event.eventToken);
        addValueOrEmpty(dict, KEY_JSON_RESPONSE, event.jsonResponse);
        return dict;
    }

    private Map<String, String> getEventTrackingFailedDictionary(AdjustEventFailure event) {
        Map<String, String> dict = new HashMap<String, String>();
        addValueOrEmpty(dict, KEY_MESSAGE, event.message);
        addValueOrEmpty(dict, KEY_TIMESTAMP, event.timestamp);
        addValueOrEmpty(dict, KEY_ADID, event.adid);
        addValueOrEmpty(dict, KEY_EVENT_TOKEN, event.eventToken);
        addValueOrEmpty(dict, KEY_WILL_RETRY, event.willRetry ? "true" : "false");;
        addValueOrEmpty(dict, KEY_JSON_RESPONSE, event.jsonResponse);
        return dict;
    }

    private Map<String, String> getAttributionDictionary(AdjustAttribution attribution) {
        Map<String, String> dict = new HashMap<String, String>();
        addValueOrEmpty(dict, KEY_TRACKER_TOKEN, attribution.trackerToken);
        addValueOrEmpty(dict, KEY_TRACKER_NAME, attribution.trackerName);
        addValueOrEmpty(dict, KEY_NETWORK, attribution.network);
        addValueOrEmpty(dict, KEY_CAMPAIGN, attribution.campaign);
        addValueOrEmpty(dict, KEY_ADGROUP, attribution.adgroup);
        addValueOrEmpty(dict, KEY_CREATIVE, attribution.creative);
        addValueOrEmpty(dict, KEY_CLICK_LABEL, attribution.clickLabel);
        addValueOrEmpty(dict, KEY_ADID, attribution.adid);
        return dict;
    }

    private Map<String, String> getSessionTrackingSucceededDictionary(AdjustSessionSuccess session) {
        Map<String, String> dict = new HashMap<String, String>();
        addValueOrEmpty(dict, KEY_MESSAGE, session.message);
        addValueOrEmpty(dict, KEY_TIMESTAMP, session.timestamp);
        addValueOrEmpty(dict, KEY_ADID, session.adid);
        addValueOrEmpty(dict, KEY_JSON_RESPONSE, session.jsonResponse);
        return dict;
    }

    private Map<String, String> getSessionTrackingFailedDictionary(AdjustSessionFailure session) {
        Map<String, String> dict = new HashMap<String, String>();
        addValueOrEmpty(dict, KEY_MESSAGE, session.message);
        addValueOrEmpty(dict, KEY_TIMESTAMP, session.timestamp);
        addValueOrEmpty(dict, KEY_ADID, session.adid);
        addValueOrEmpty(dict, KEY_WILL_RETRY, session.willRetry ? "true" : "false");;
        addValueOrEmpty(dict, KEY_JSON_RESPONSE, session.jsonResponse);
        return dict;
    }
}
