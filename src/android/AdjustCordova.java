package com.adjust.sdk;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import android.net.Uri;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult.Status;
import static com.adjust.sdk.AdjustCordovaUtils.*;

public class AdjustCordova extends CordovaPlugin implements OnAttributionChangedListener, 
    OnEventTrackingSucceededListener,
    OnEventTrackingFailedListener,
    OnSessionTrackingSucceededListener,
    OnSessionTrackingFailedListener,
    OnDeeplinkResponseListener, 
    OnDeviceIdsRead {
    private boolean shouldLaunchDeeplink = true;
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
                JSONObject attributionJsonData = new JSONObject(getAttributionMap(attribution));
                PluginResult pluginResult = new PluginResult(Status.OK, attributionJsonData);
                pluginResult.setKeepCallback(true);
                getAttributionCallbackContext.sendPluginResult(pluginResult);
            }
        } else if (action.equals(COMMAND_GET_IDFA)) {
            getIdfaCallbackContext = callbackContext;
            final String idfa = "";
            PluginResult pluginResult = new PluginResult(Status.OK, idfa);
            pluginResult.setKeepCallback(true);
            getIdfaCallbackContext.sendPluginResult(pluginResult);
        } else if (action.equals(COMMAND_GET_SDK_VERSION)) {
            String sdkVersion = Adjust.getSdkVersion();
            if (sdkVersion == null) {
                sdkVersion = "";
            }
            PluginResult pluginResult = new PluginResult(Status.OK, sdkVersion);
            callbackContext.sendPluginResult(pluginResult);
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
        } else if (action.equals(COMMAND_DISABLE_THIRD_PARTY_SHARING)) {
            Adjust.disableThirdPartySharing(this.cordova.getActivity().getApplicationContext());
        } else if (action.equals(COMMAND_SET_REFERRER)) {
            final String referrer = args.getString(0);
            Adjust.setReferrer(referrer, this.cordova.getActivity().getApplicationContext());
        } else if (action.equals(COMMAND_TRACK_AD_REVENUE)) {
            try {
                JSONObject jsonPayload = new JSONObject(args.getString(1));
                Adjust.trackAdRevenue(args.getString(0), jsonPayload);
            } catch (JSONException err) {
                Logger logger = (Logger)AdjustFactory.getLogger();
                logger.error("Give ad revenue payload is not a valid JSON string");
            }
        } else if (action.equals(COMMAND_TRACK_APP_STORE_SUBSCRIPTION)) {
            // iOS method only
        } else if (action.equals(COMMAND_TRACK_PLAY_STORE_SUBSCRIPTION)) {
            executeTrackPlayStoreSubscription(args);
        } else if (action.equals(COMMAND_SET_TEST_OPTIONS)) {
            executeSetTestOptions(args);
        } else if (action.equals(COMMAND_TEARDOWN)) {
            attributionCallbackContext = null;
            eventTrackingSucceededCallbackContext = null;
            eventTrackingFailedCallbackContext = null;
            sessionTrackingSucceededCallbackContext = null;
            sessionTrackingFailedCallbackContext = null;
            deferredDeeplinkCallbackContext = null;
            getAdidCallbackContext = null;
            getIdfaCallbackContext = null;
            getGoogleAdIdCallbackContext = null;
            getAmazonAdidCallbackContext = null;
            getAttributionCallbackContext = null;
            shouldLaunchDeeplink = true;
        } else {
            Logger logger = (Logger)AdjustFactory.getLogger();
            logger.error(String.format("[AdjustCordova]: Invalid call (%s).", action));
            return false;    
        }

        return true;
    }

    private void executeCreate(final JSONArray args) throws JSONException {
        String params = args.getString(0);
        JSONArray jsonArrayParams = new JSONArray(params);
        JSONObject jsonParameters = jsonArrayParams.optJSONObject(0);
        Map<String, Object> parameters = jsonObjectToMap(jsonParameters);

        String appToken = null;
        String environment = null;
        String defaultTracker = null;
        String externalDeviceId = null;
        String urlStrategy = null;
        String processName = null;
        String delayStart = null;
        String logLevel = null;
        String userAgent = null;
        String sdkPrefix = null;
        String secretId = null;
        String info1 = null;
        String info2 = null;
        String info3 = null;
        String info4 = null;
        boolean isLogLevelSuppress = false;
        boolean eventBufferingEnabled = false;
        boolean isDeviceKnown = false;
        boolean sendInBackground = false;
        boolean shouldLaunchDeeplink = false;

        if (parameters.containsKey(KEY_APP_TOKEN)) {
            appToken = parameters.get(KEY_APP_TOKEN).toString();
        }
        if (parameters.containsKey(KEY_ENVIRONMENT)) {
            environment = parameters.get(KEY_ENVIRONMENT).toString();
        }
        if (parameters.containsKey(KEY_DEFAULT_TRACKER)) {
            defaultTracker = parameters.get(KEY_DEFAULT_TRACKER).toString();
        }
        if (parameters.containsKey(KEY_EXTERNAL_DEVICE_ID)) {
            externalDeviceId = parameters.get(KEY_EXTERNAL_DEVICE_ID).toString();
        }
        if (parameters.containsKey(KEY_URL_STRATEGY)) {
            urlStrategy = parameters.get(KEY_URL_STRATEGY).toString();
        }
        if (parameters.containsKey(KEY_PROCESS_NAME)) {
            processName = parameters.get(KEY_PROCESS_NAME).toString();
        }
        if (parameters.containsKey(KEY_DELAY_START)) {
            delayStart = parameters.get(KEY_DELAY_START).toString();
        }
        if (parameters.containsKey(KEY_LOG_LEVEL)) {
            logLevel = parameters.get(KEY_LOG_LEVEL).toString().toUpperCase();
        }
        if (parameters.containsKey(KEY_USER_AGENT)) {
            userAgent = parameters.get(KEY_USER_AGENT).toString();
        }
        if (parameters.containsKey(KEY_SECRET_ID)) {
            secretId = parameters.get(KEY_SECRET_ID).toString();
        }
        if (parameters.containsKey(KEY_SDK_PREFIX)) {
            sdkPrefix = parameters.get(KEY_SDK_PREFIX).toString();
        }
        if (parameters.containsKey(KEY_INFO_1)) {
            info1 = parameters.get(KEY_INFO_1).toString();
        }
        if (parameters.containsKey(KEY_INFO_2)) {
            info2 = parameters.get(KEY_INFO_2).toString();
        }
        if (parameters.containsKey(KEY_INFO_3)) {
            info3 = parameters.get(KEY_INFO_3).toString();
        }
        if (parameters.containsKey(KEY_INFO_4)) {
            info4 = parameters.get(KEY_INFO_4).toString();
        }
        if (parameters.containsKey(KEY_EVENT_BUFFERING_ENABLED)) {
            eventBufferingEnabled = parameters.get(KEY_EVENT_BUFFERING_ENABLED).toString() == "true" ? true : false;
        }
        if (parameters.containsKey(KEY_DEVICE_KNOWN)) {
            isDeviceKnown = parameters.get(KEY_DEVICE_KNOWN).toString() == "true" ? true : false;
        }
        if (parameters.containsKey(KEY_SEND_IN_BACKGROUND)) {
            sendInBackground = parameters.get(KEY_SEND_IN_BACKGROUND).toString() == "true" ? true : false;
        }
        if (parameters.containsKey(KEY_SHOULD_LAUNCH_DEEPLINK)) {
            shouldLaunchDeeplink = parameters.get(KEY_SHOULD_LAUNCH_DEEPLINK).toString() == "true" ? true : false;
        }

        if (isFieldValid(logLevel) && logLevel.equals("SUPPRESS")) {
            isLogLevelSuppress = true;
        }

        final AdjustConfig adjustConfig = new AdjustConfig(this.cordova.getActivity().getApplicationContext(), appToken, environment, isLogLevelSuppress);
        if (!adjustConfig.isValid()) {
            return;
        }

        // Log level.
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

        // SDK prefix.
        if (isFieldValid(sdkPrefix)) {
            adjustConfig.setSdkPrefix(sdkPrefix);
        }

        // Main process name.
        if (isFieldValid(processName)) {
            adjustConfig.setProcessName(processName);
        }

        // Default tracker.
        if (isFieldValid(defaultTracker)) {
            adjustConfig.setDefaultTracker(defaultTracker);
        }

        // External device ID.
        if (isFieldValid(externalDeviceId)) {
            adjustConfig.setExternalDeviceId(externalDeviceId);
        }

        // URL strategy.
        if (isFieldValid(urlStrategy)) {
            if (urlStrategy.equalsIgnoreCase("china")) {
                adjustConfig.setUrlStrategy(AdjustConfig.URL_STRATEGY_CHINA​);
            } else if (urlStrategy.equalsIgnoreCase("india")) {
                adjustConfig.setUrlStrategy(AdjustConfig.URL_STRATEGY_INDIA);
            }
        }

        // User agent.
        if (isFieldValid(userAgent)) {
            adjustConfig.setUserAgent(userAgent);
        }

        // App secret.
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

        // Deprecated.
        // Read mobile equipment identity.
        // adjustConfig.setReadMobileEquipmentIdentity(readMobileEquipmentIdentity);

        // Event buffering.
        adjustConfig.setEventBufferingEnabled(eventBufferingEnabled);

        // Is device known.
        adjustConfig.setDeviceKnown(isDeviceKnown);

        // Background tracking.
        adjustConfig.setSendInBackground(sendInBackground);

        // Launching deferred deep link.
        this.shouldLaunchDeeplink = shouldLaunchDeeplink;

        // Delayed start.
        if (isFieldValid(delayStart)) {
            try {
                double dDelayStart = Double.parseDouble(delayStart);
                adjustConfig.setDelayStart(dDelayStart);
            } catch(NumberFormatException ignored) {}
        }

        // Attribution callback.
        if (attributionCallbackContext != null) {
            adjustConfig.setOnAttributionChangedListener(this);
        }

        // Event tracking succeeded callback.
        if (eventTrackingSucceededCallbackContext != null) {
            adjustConfig.setOnEventTrackingSucceededListener(this);
        }

        // Event tracking failed callback.
        if (eventTrackingFailedCallbackContext != null) {
            adjustConfig.setOnEventTrackingFailedListener(this);
        }

        // Session tracking succeeded callback.
        if (sessionTrackingSucceededCallbackContext != null) {
            adjustConfig.setOnSessionTrackingSucceededListener(this);
        }

        // Session tracking failed callback.
        if (sessionTrackingFailedCallbackContext != null) {
            adjustConfig.setOnSessionTrackingFailedListener(this);
        }

        // Deferred deeplink callback listener.
        if (deferredDeeplinkCallbackContext != null) {
            adjustConfig.setOnDeeplinkResponseListener(this);
        }

        // Start SDK.
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

        String eventToken = null;
        String revenue = null;
        String currency = null;
        String transactionId = null;
        String callbackId = null;

        if (parameters.containsKey(KEY_EVENT_TOKEN)) {
            eventToken = parameters.get(KEY_EVENT_TOKEN).toString();
        }
        if (parameters.containsKey(KEY_REVENUE)) {
            revenue = parameters.get(KEY_REVENUE).toString();
        }
        if (parameters.containsKey(KEY_CURRENCY)) {
            currency = parameters.get(KEY_CURRENCY).toString();
        }
        if (parameters.containsKey(KEY_TRANSACTION_ID)) {
            transactionId = parameters.get(KEY_TRANSACTION_ID).toString();
        }
        if (parameters.containsKey(KEY_CALLBACK_ID)) {
            callbackId = parameters.get(KEY_CALLBACK_ID).toString();
        }

        JSONArray partnerParametersJson = (JSONArray)parameters.get(KEY_PARTNER_PARAMETERS);
        JSONArray callbackParametersJson = (JSONArray)parameters.get(KEY_CALLBACK_PARAMETERS);
        String[] partnerParameters = jsonArrayToArray(partnerParametersJson);
        String[] callbackParameters = jsonArrayToArray(callbackParametersJson);

        final AdjustEvent adjustEvent = new AdjustEvent(eventToken);
        if (!adjustEvent.isValid()) {
            return;
        }

        // Revenue and currency.
        if (isFieldValid(revenue) && isFieldValid(currency)) {
            try {
                double revenueValue = Double.parseDouble(revenue);
                adjustEvent.setRevenue(revenueValue, currency);
            } catch (Exception e) {
                ILogger logger = AdjustFactory.getLogger();
                logger.error("[AdjustCordova]: Unable to parse revenue.");
            }
        }

        // Callback parameters.
        for (int i = 0; i < callbackParameters.length; i +=2) {
            String key = callbackParameters[i];
            String value = callbackParameters[i+1];
            adjustEvent.addCallbackParameter(key, value);
        }

        // Partner parameters.
        for (int i = 0; i < partnerParameters.length; i += 2) {
            String key = partnerParameters[i];
            String value = partnerParameters[i+1];
            adjustEvent.addPartnerParameter(key, value);
        }

        // Transaction ID.
        if (isFieldValid(transactionId)) {
            adjustEvent.setOrderId(transactionId);
        }

        // Callback ID.
        if (isFieldValid(callbackId)) {
            adjustEvent.setCallbackId(callbackId);
        }

        // Track event.
        Adjust.trackEvent(adjustEvent);
    }

    private void executeTrackPlayStoreSubscription(final JSONArray args) throws JSONException {
        String params = args.getString(0);
        JSONArray jsonArrayParams = new JSONArray(params);
        JSONObject jsonParameters = jsonArrayParams.optJSONObject(0);
        Map<String, Object> parameters = jsonObjectToMap(jsonParameters);

        long price = -1;
        String currency = null;
        String sku = null;
        String orderId = null;
        String signature = null;
        String purchaseToken = null;

        // Price.
        if (parameters.containsKey(KEY_PRICE)) {
            try {
                price = Long.parseLong(parameters.get(KEY_PRICE).toString());
            } catch (NumberFormatException ignore) {}
        }
        // Currency.
        if (parameters.containsKey(KEY_CURRENCY)) {
            currency = parameters.get(KEY_CURRENCY).toString();
        }
        // SKU.
        if (parameters.containsKey(KEY_SKU)) {
            sku = parameters.get(KEY_SKU).toString();
        }
        // Order ID.
        if (parameters.containsKey(KEY_ORDER_ID)) {
            orderId = parameters.get(KEY_ORDER_ID).toString();
        }
        // Signature.
        if (parameters.containsKey(KEY_SIGNATURE)) {
            signature = parameters.get(KEY_SIGNATURE).toString();
        }
        // Purchase token.
        if (parameters.containsKey(KEY_PURCHASE_TOKEN)) {
            purchaseToken = parameters.get(KEY_PURCHASE_TOKEN).toString();
        }

        final AdjustPlayStoreSubscription subscription = new AdjustPlayStoreSubscription(
                price,
                currency,
                sku,
                orderId,
                signature,
                purchaseToken);

        // Purchase time.
        if (parameters.containsKey(KEY_PURCHASE_TIME)) {
            try {
                long purchaseTime = Long.parseLong(parameters.get(KEY_PURCHASE_TIME).toString());
                subscription.setPurchaseTime(purchaseTime);
            } catch (NumberFormatException ignore) {}
        }

        JSONArray partnerParametersJson = (JSONArray)parameters.get(KEY_PARTNER_PARAMETERS);
        JSONArray callbackParametersJson = (JSONArray)parameters.get(KEY_CALLBACK_PARAMETERS);
        String[] partnerParameters = jsonArrayToArray(partnerParametersJson);
        String[] callbackParameters = jsonArrayToArray(callbackParametersJson);

        // Callback parameters.
        for (int i = 0; i < callbackParameters.length; i +=2) {
            String key = callbackParameters[i];
            String value = callbackParameters[i+1];
            subscription.addCallbackParameter(key, value);
        }

        // Partner parameters.
        for (int i = 0; i < partnerParameters.length; i += 2) {
            String key = partnerParameters[i];
            String value = partnerParameters[i+1];
            subscription.addPartnerParameter(key, value);
        }

        // Track subscription.
        Adjust.trackPlayStoreSubscription(subscription);
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
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse context.");
            }
        }

        if (!jsonParameters.isNull(KEY_BASE_URL)) {
            try {
                String value = jsonParameters.getString(KEY_BASE_URL);
                testOptions.baseUrl = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse base URL.");
            }
        }

        if (!jsonParameters.isNull(KEY_GDPR_URL)) {
            try {
                String value = jsonParameters.getString(KEY_GDPR_URL);
                testOptions.gdprUrl = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse GDPR URL.");
            }
        }

        if (!jsonParameters.isNull(KEY_SUBSCRIPTION_URL)) {
            try {
                String value = jsonParameters.getString(KEY_SUBSCRIPTION_URL);
                testOptions.subscriptionUrl = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse subscription URL.");
            }
        }

        if (!jsonParameters.isNull(KEY_BASE_PATH)) {
            try {
                String value = jsonParameters.getString(KEY_BASE_PATH);
                testOptions.basePath = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse base path.");
            }
        }

        if (!jsonParameters.isNull(KEY_GDPR_PATH)) {
            try {
                String value = jsonParameters.getString(KEY_GDPR_PATH);
                testOptions.gdprPath = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse GDPR path.");
            }
        }

        if (!jsonParameters.isNull(KEY_SUBSCRIPTION_PATH)) {
            try {
                String value = jsonParameters.getString(KEY_SUBSCRIPTION_PATH);
                testOptions.subscriptionPath = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse subscription path.");
            }
        }

        if (!jsonParameters.isNull(KEY_USE_TEST_CONNECTION_OPTIONS)) {
            try {
                boolean value = jsonParameters.getBoolean(KEY_USE_TEST_CONNECTION_OPTIONS);
                testOptions.useTestConnectionOptions = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse use test connection options.");
            }
        }

        if (!jsonParameters.isNull(KEY_TIMER_INTERVAL)) {
            try {
                long value = jsonParameters.getLong(KEY_TIMER_INTERVAL);
                testOptions.timerIntervalInMilliseconds = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse timer interval.");
            }
        }

        if (!jsonParameters.isNull(KEY_TIMER_START)) {
            try {
                long value = jsonParameters.getLong(KEY_TIMER_START);
                testOptions.timerStartInMilliseconds = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse timer start.");
            }
        }

        if (!jsonParameters.isNull(KEY_SESSION_INTERVAL)) {
            try {
                long value = jsonParameters.getLong(KEY_SESSION_INTERVAL);
                testOptions.sessionIntervalInMilliseconds = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse session interval.");
            }
        } 

        if (!jsonParameters.isNull(KEY_SUBSESSION_INTERVAL)) {
            try {
                long value = jsonParameters.getLong(KEY_SUBSESSION_INTERVAL);
                testOptions.subsessionIntervalInMilliseconds = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse subsession interval.");
            }
        }

        if (!jsonParameters.isNull(KEY_TEARDOWN)) {
            try {
                boolean teardown = jsonParameters.getBoolean(KEY_TEARDOWN);
                testOptions.teardown = teardown;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse teardown.");
            }
        }

        if (!jsonParameters.isNull(KEY_NO_BACKOFF_WAIT)) {
            try {
                boolean noBackoffWait = jsonParameters.getBoolean(KEY_NO_BACKOFF_WAIT);
                testOptions.noBackoffWait = noBackoffWait;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse noBackoffWait.");
            }
        }

        Adjust.setTestOptions(testOptions);
    }

    @Override
    public void onAttributionChanged(AdjustAttribution attribution) {
        if (attributionCallbackContext == null) {
            return;
        }

        JSONObject attributionJsonData = new JSONObject(getAttributionMap(attribution));
        PluginResult pluginResult = new PluginResult(Status.OK, attributionJsonData);
        pluginResult.setKeepCallback(true);
        attributionCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onFinishedEventTrackingSucceeded(AdjustEventSuccess event) {
        if (eventTrackingSucceededCallbackContext == null) {
            return;
        }

        JSONObject jsonData = new JSONObject(getEventSuccessMap(event));
        PluginResult pluginResult = new PluginResult(Status.OK, jsonData);
        pluginResult.setKeepCallback(true);
        eventTrackingSucceededCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onFinishedEventTrackingFailed(AdjustEventFailure event) {
        if (eventTrackingFailedCallbackContext == null) {
            return;
        }

        JSONObject jsonData = new JSONObject(getEventFailureMap(event));
        PluginResult pluginResult = new PluginResult(Status.OK, jsonData);
        pluginResult.setKeepCallback(true);
        eventTrackingFailedCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onFinishedSessionTrackingSucceeded(AdjustSessionSuccess session) {
        if (sessionTrackingSucceededCallbackContext == null) {
            return;
        }

        JSONObject jsonData = new JSONObject(getSessionSuccessMap(session));
        PluginResult pluginResult = new PluginResult(Status.OK, jsonData);
        pluginResult.setKeepCallback(true);
        sessionTrackingSucceededCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onFinishedSessionTrackingFailed(AdjustSessionFailure session) {
        if (sessionTrackingFailedCallbackContext == null) {
            return;
        }

        JSONObject jsonData = new JSONObject(getSessionFailureMap(session));
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
}
