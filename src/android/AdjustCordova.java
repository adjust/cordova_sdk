package com.adjust.sdk;

import java.lang.reflect.*;
import java.util.Map;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult.Status;
import static com.adjust.sdk.AdjustCordovaUtils.*;

public class AdjustCordova extends CordovaPlugin implements
        OnAttributionChangedListener,
        OnEventTrackingSucceededListener,
        OnEventTrackingFailedListener,
        OnSessionTrackingSucceededListener,
        OnSessionTrackingFailedListener,
        OnDeferredDeeplinkResponseListener {
    private boolean shouldLaunchDeeplink = true;
    private CallbackContext attributionChangedCallbackContext;
    private CallbackContext eventTrackingSucceededCallbackContext;
    private CallbackContext eventTrackingFailedCallbackContext;
    private CallbackContext sessionTrackingSucceededCallbackContext;
    private CallbackContext sessionTrackingFailedCallbackContext;
    private CallbackContext deferredDeeplinkReceivedCallbackContext;

    @Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals(COMMAND_CREATE)) {
            executeCreate(args);
        } else if (action.equals(COMMAND_SET_ATTRIBUTION_CHANGED_CALLBACK)) {
            attributionChangedCallbackContext = callbackContext;
        } else if (action.equals(COMMAND_SET_EVENT_TRACKING_SUCCEEDED_CALLBACK)) {
            eventTrackingSucceededCallbackContext = callbackContext;
        } else if (action.equals(COMMAND_SET_EVENT_TRACKING_FAILED_CALLBACK)) {
            eventTrackingFailedCallbackContext = callbackContext;
        } else if (action.equals(COMMAND_SET_SESSION_TRACKING_SUCCEEDED_CALLBACK)) {
            sessionTrackingSucceededCallbackContext = callbackContext;
        } else if (action.equals(COMMAND_SET_SESSION_TRACKING_FAILED_CALLBACK)) {
            sessionTrackingFailedCallbackContext = callbackContext;
        } else if (action.equals(COMMAND_SET_DEFERRED_DEEPLINK_RECEIVED_CALLBACK)) {
            deferredDeeplinkReceivedCallbackContext = callbackContext;
        } else if (action.equals(COMMAND_SET_SKAN_CONVERSION_DATA_UPDATED_CALLBACK)) {
        } else if (action.equals(COMMAND_SET_PUSH_TOKEN)) {
            final String token = args.getString(0);
            Adjust.setPushToken(token, this.cordova.getActivity().getApplicationContext());
        } else if (action.equals(COMMAND_SET_REFERRER)) {
            final String referrer = args.getString(0);
            Adjust.setReferrer(referrer, this.cordova.getActivity().getApplicationContext());
        } else if (action.equals(COMMAND_GET_ATTRIBUTION)) {
            executeGetAttribution(callbackContext);
        } else if (action.equals(COMMAND_GET_ADID)) {
            executeGetAdId(callbackContext);
        } else if (action.equals(COMMAND_GET_GOOGLE_AD_ID)) {
            executeGetGoogleAdid(callbackContext);
        } else if (action.equals(COMMAND_GET_AMAZON_AD_ID)) {
            executeGetAmazonAdid(callbackContext);
        } else if (action.equals(COMMAND_GET_SDK_VERSION)) {
            executeGetSdkVersion(callbackContext);
        } else if (action.equals(COMMAND_GET_GOOGLE_PLAY_INSTALL_REFERRER)) {
            executeGetGooglePlayInstallReferrer(callbackContext);
        } else if (action.equals(COMMAND_ADD_GLOBAL_CALLBACK_PARAMETER)) {
            final String key = args.getString(0);
            final String value = args.getString(1);
            Adjust.addGlobalCallbackParameter(key, value);
        } else if (action.equals(COMMAND_REMOVE_GLOBAL_CALLBACK_PARAMETER)) {
            final String key = args.getString(0);
            Adjust.removeGlobalCallbackParameter(key);
        } else if (action.equals(COMMAND_RESET_GLOBAL_CALLBACK_PARAMETERS)) {
            Adjust.removeGlobalCallbackParameters();
        } else if (action.equals(COMMAND_ADD_GLOBAL_PARTNER_PARAMETER)) {
            final String key = args.getString(0);
            final String value = args.getString(1);
            Adjust.addGlobalPartnerParameter(key, value);
        } else if (action.equals(COMMAND_REMOVE_GLOBAL_PARTNER_PARAMETER)) {
            final String key = args.getString(0);
            Adjust.removeGlobalPartnerParameter(key);
        } else if (action.equals(COMMAND_RESET_GLOBAL_PARTNER_PARAMETERS)) {
            Adjust.removeGlobalPartnerParameters();
        } else if (action.equals(COMMAND_SWITCH_TO_OFFLINE_MODE)) {
            Adjust.switchToOfflineMode();
        } else if (action.equals(COMMAND_SWITCH_BACK_TO_ONLINE_MODE)) {
            Adjust.switchBackToOnlineMode();
        } else if (action.equals(COMMAND_ENABLE)) {
            Adjust.enable();
        } else if (action.equals(COMMAND_DISABLE)) {
            Adjust.disable();
        } else if (action.equals(COMMAND_IS_ENABLED)) {
            executeIsAdjustSdkEnabled(callbackContext);
        } else if (action.equals(COMMAND_GDPR_FORGET_ME)) {
            Adjust.gdprForgetMe(this.cordova.getActivity().getApplicationContext());
        } else if (action.equals(COMMAND_ON_PAUSE)) {
            Adjust.onPause();
        } else if (action.equals(COMMAND_ON_RESUME)) {
            Adjust.onResume();
        } else if (action.equals(COMMAND_TRACK_EVENT)) {
                executeTrackEvent(args);
        } else if (action.equals(COMMAND_TRACK_AD_REVENUE)) {
            if (args.length() != 1) {
                AdjustFactory.getLogger().error(String.format("[AdjustCordova]: Invalid call (%s). Only one parameter is supported!", action));
                return false;
            }
            executeTrackAdRevenue(args);
        } else if (action.equals(COMMAND_TRACK_PLAY_STORE_SUBSCRIPTION)) {
            executeTrackPlayStoreSubscription(args);
        } else if (action.equals(COMMAND_VERIFY_PLAY_STORE_PURCHASE)) {
            executeVerifyPlayStorePurchase(args, callbackContext);
        } else if (action.equals(COMMAND_VERIFY_AND_TRACK_PLAY_STORE_PURCHASE)) {
            executeVerifyAndTrackPlayStorePurchase(args, callbackContext);
        } else if (action.equals(COMMAND_TRACK_THIRD_PARTY_SHARING)) {
            executeTrackThirdPartySharing(args);
        } else if (action.equals(COMMAND_TRACK_MEASUREMENT_CONSENT)) {
            final Boolean isEnabled = args.getBoolean(0);
            if (isEnabled != null) {
                Adjust.trackMeasurementConsent(isEnabled);
            }
        } else if (action.equals(COMMAND_PROCESS_DEEPLINK)) {
            String url = args.getString(0);
            AdjustDeeplink adjustDeeplink = new AdjustDeeplink(Uri.parse(url));
            Adjust.processDeeplink(adjustDeeplink, this.cordova.getActivity().getApplicationContext());
        } else if (action.equals(COMMAND_PROCESS_AND_RESOLVE_DEEPLINK)) {
            executeProcessAndResolveDeeplink(args, callbackContext);
        } else if (action.equals(COMMAND_GET_LAST_DEEPLINK)) {
            executeGetLastDeeplink(callbackContext);
        } else if (action.equals(COMMAND_TRACK_APP_STORE_SUBSCRIPTION)) {
            // iOS method only
        } else if (action.equals(COMMAND_VERIFY_APP_STORE_PURCHASE)) {
            // iOS method only
        } else if (action.equals(COMMAND_VERIFY_AND_TRACK_APP_STORE_PURCHASE)) {
            // iOS method only
        } else if (action.equals(COMMAND_REQUEST_TRACKING_AUTHORIZATION_WITH_COMPLETION_HANDLER)) {
            // iOS method only
        } else if (action.equals(COMMAND_GET_APP_TRACKING_AUTHORIZATION_STATUS)) {
            // iOS method only
        } else if (action.equals(COMMAND_UPDATE_SKAN_CONVERSION_VALUE_WITH_ERROR_CALLBACK)) {
            // iOS method only
        } else if (action.equals(COMMAND_GET_IDFA)) {
            // iOS method only
            final String idfa = "";
            PluginResult pluginResult = new PluginResult(Status.OK, idfa);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        } else if (action.equals(COMMAND_GET_IDFV)) {
            // iOS method only
            final String idfv = "";
            PluginResult pluginResult = new PluginResult(Status.OK, idfv);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        }  else if (action.equals(COMMAND_SET_TEST_OPTIONS)) {
            executeSetTestOptions(args);
        } else if (action.equals(COMMAND_TEARDOWN)) {
            attributionChangedCallbackContext = null;
            eventTrackingSucceededCallbackContext = null;
            eventTrackingFailedCallbackContext = null;
            sessionTrackingSucceededCallbackContext = null;
            sessionTrackingFailedCallbackContext = null;
            deferredDeeplinkReceivedCallbackContext = null;
            shouldLaunchDeeplink = true;
        } else {
            Logger logger = (Logger) AdjustFactory.getLogger();
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
        String processName = null;
        String logLevel = null;
        String sdkPrefix = null;
        String preinstallFilePath = null;
        String fbAppId = null;
        Integer eventDeduplicationIdsMaxSize = -1;
        boolean shouldLaunchDeeplink = true;
        boolean preinstallTrackingEnabled = false;
        boolean oaidReadingEnabled = false;
        boolean isLogLevelSuppress = false;
        boolean isSendingInBackgroundEnabled = false;
        boolean isCoppaComplianceEnabled = false;
        boolean isCostDataInAttributionEnabled = false;
        boolean isDeviceIdsReadingOnceEnabled = false;
        boolean playStoreKidsAppEnabled = false;

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
        if (parameters.containsKey(KEY_PROCESS_NAME)) {
            processName = parameters.get(KEY_PROCESS_NAME).toString();
        }
        if (parameters.containsKey(KEY_LOG_LEVEL)) {
            logLevel = parameters.get(KEY_LOG_LEVEL).toString().toUpperCase();
        }
        if (parameters.containsKey(KEY_SDK_PREFIX)) {
            sdkPrefix = parameters.get(KEY_SDK_PREFIX).toString();
        }
        if (parameters.containsKey(KEY_PREINSTALL_FILE_PATH)) {
            preinstallFilePath = parameters.get(KEY_PREINSTALL_FILE_PATH).toString();
        }
        if (parameters.containsKey(KEY_FB_APP_ID)) {
            fbAppId = parameters.get(KEY_FB_APP_ID).toString();
        }
        if (parameters.containsKey(KEY_SHOULD_LAUNCH_DEEPLINK)) {
            shouldLaunchDeeplink = parameters.get(KEY_SHOULD_LAUNCH_DEEPLINK).toString() == "true" ? true : false;
        }
        if (parameters.containsKey(KEY_PREINSTALL_TRACKING_ENABLED)) {
            preinstallTrackingEnabled = parameters.get(KEY_PREINSTALL_TRACKING_ENABLED).toString() == "true" ? true : false;
        }
        if (parameters.containsKey(KEY_OAID_READING_ENABLED)) {
            oaidReadingEnabled = parameters.get(KEY_OAID_READING_ENABLED).toString() == "true" ? true : false;
        }
        if (parameters.containsKey(KEY_IS_SENDING_IN_BACKGROUND_ENABLED)) {
            isSendingInBackgroundEnabled = parameters.get(KEY_IS_SENDING_IN_BACKGROUND_ENABLED).toString() == "true" ? true : false;
        }
        if (parameters.containsKey(KEY_IS_COST_DATA_IN_ATTRIBUTION_ENABLED)) {
            isCostDataInAttributionEnabled = parameters.get(KEY_IS_COST_DATA_IN_ATTRIBUTION_ENABLED).toString() == "true" ? true : false;
        }
        if (parameters.containsKey(KEY_IS_COPPA_COMPLIANCE_ENABLED)) {
            isCoppaComplianceEnabled = parameters.get(KEY_IS_COPPA_COMPLIANCE_ENABLED).toString() == "true" ? true : false;
        }
        if (parameters.containsKey(KEY_IS_DEVICE_IDS_READING_ONCE_ENABLED)) {
            isDeviceIdsReadingOnceEnabled = parameters.get(KEY_IS_DEVICE_IDS_READING_ONCE_ENABLED).toString() == "true" ? true : false;
        }
        if (parameters.containsKey(KEY_PLAY_STORE_KIDS_APP_ENABLED)) {
            playStoreKidsAppEnabled = parameters.get(KEY_PLAY_STORE_KIDS_APP_ENABLED).toString() == "true" ? true : false;
        }

        if(parameters.containsKey(KEY_EVENT_DEDUPLICATION_IDS_MAX_SIZE)) {
            Object obj = parameters.get(KEY_EVENT_DEDUPLICATION_IDS_MAX_SIZE);
            if (obj instanceof Integer) {
                eventDeduplicationIdsMaxSize = (Integer) obj;
            }
        }

        if (isFieldValid(logLevel) && logLevel.equals("SUPPRESS")) {
            isLogLevelSuppress = true;
        }

        final AdjustConfig adjustConfig = new AdjustConfig(this.cordova.getActivity().getApplicationContext(),
                appToken,
                environment,
                isLogLevelSuppress);
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
        // TODO: GENA - Implement the following parameters when the requirements are clear
        // KEY_URL_STRATEGY_DOMAINS, KEY_USE_SUBDOMAINS, KEY_IS_DATA_RESIDENCY

        // Preinstall file path.
        if (isFieldValid(preinstallFilePath)) {
            adjustConfig.setPreinstallFilePath(preinstallFilePath);
        }

        // FB app ID (meta install referrer).
        if (isFieldValid(fbAppId)) {
            adjustConfig.setFbAppId(fbAppId);
        }

        // COPPA compliant.
        if (isCoppaComplianceEnabled) {
            adjustConfig.enableCoppaCompliance();
        }

        // Play Store Kids App.
        if (playStoreKidsAppEnabled) {
            adjustConfig.enablePlayStoreKidsCompliance();
        }

        // Read device info just once.
        if (isDeviceIdsReadingOnceEnabled) {
            adjustConfig.enableDeviceIdsReadingOnce();
        }

        // Background tracking.
        if (isSendingInBackgroundEnabled) {
            adjustConfig.enableSendingInBackground();
        }

        // Launching deferred deep link.
        this.shouldLaunchDeeplink = shouldLaunchDeeplink;

        // Cost data.
        if (isCostDataInAttributionEnabled) {
            adjustConfig.enableCostDataInAttribution();
        }

        // Preinstall tracking enabled.
        if (preinstallTrackingEnabled) {
            adjustConfig.enablePreinstallTracking();
        }

        // Event deduplication IDs max size
        if (eventDeduplicationIdsMaxSize != null) {
            adjustConfig.setEventDeduplicationIdsMaxSize(eventDeduplicationIdsMaxSize);
        }
        // Attribution callback.
        if (attributionChangedCallbackContext != null) {
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
        if (deferredDeeplinkReceivedCallbackContext != null) {
            adjustConfig.setOnDeferredDeeplinkResponseListener(this);
        }

        // PLUGIN
        // Check if OAID reading is enabled to potentially ping OAID plugin if added natively.
        if (oaidReadingEnabled) {
            Logger logger = (Logger) AdjustFactory.getLogger();
            try {
                Class clazz = Class.forName("com.adjust.sdk.oaid.AdjustOaid");
                if (clazz != null) {
                    Method method = clazz.getMethod("readOaid", (Class<?>[]) null);
                    if (method != null) {
                        method.invoke(null, (Object[]) null);
                        logger.info(String.format("[AdjustCordova]: Adjust OAID plugin successfully found in the app"));
                        logger.info(String.format("[AdjustCordova]: OAID reading enabled"));
                    }
                }
            } catch (Exception e) {
                logger.error(String.format("[AdjustCordova]: OAID reading failed"));
                e.printStackTrace();
            }
        }

        // Start SDK.
        Adjust.initSdk(adjustConfig);
        // Needed because Cordova doesn't launch 'resume' event on app start.
        // It initializes it only when app comes back from the background.
        Adjust.onResume();
    }

    private void executeGetGoogleAdid(final CallbackContext callbackContext) throws JSONException {
        Adjust.getGoogleAdId(this.cordova.getActivity().getApplicationContext(), new OnGoogleAdIdReadListener() {
            @Override
            public void onGoogleAdIdRead(String googleAdId) {
                PluginResult pluginResult = new PluginResult(Status.OK, googleAdId);
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
            }
        });
    }

    private void executeGetAmazonAdid(final CallbackContext callbackContext) throws JSONException {
        Adjust.getAmazonAdId(this.cordova.getActivity().getApplicationContext(), new OnAmazonAdIdReadListener() {
            @Override
            public void onAmazonAdIdRead(String amazonAdId) {
                amazonAdId = (amazonAdId != null) ? amazonAdId : "";
                PluginResult pluginResult = new PluginResult(Status.OK, amazonAdId);
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
            }
        });
    }

    private void executeGetAdId(final CallbackContext callbackContext) throws JSONException {
        Adjust.getAdid(new OnAdidReadListener() {
            @Override
            public void onAdidRead(String adid) {
                adid = (adid != null) ? adid : "";
                PluginResult pluginResult = new PluginResult(Status.OK, adid);
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
            }
        });
    }

    private void executeGetAttribution(final CallbackContext callbackContext) throws JSONException {
        Adjust.getAttribution(new OnAttributionReadListener() {
            @Override
            public void onAttributionRead(AdjustAttribution adjustAttribution) {
                JSONObject attributionJsonData = new JSONObject(getAttributionMap(adjustAttribution));
                PluginResult pluginResult = new PluginResult(Status.OK, attributionJsonData);
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
            }
        });
    }

    private void executeGetSdkVersion(final CallbackContext callbackContext) throws JSONException {
        Adjust.getSdkVersion(new OnSdkVersionReadListener() {
            @Override
            public void onSdkVersionRead(String sdkVersion) {
                sdkVersion = (sdkVersion != null) ? sdkVersion : "";
                PluginResult pluginResult = new PluginResult(Status.OK, sdkVersion);
                callbackContext.sendPluginResult(pluginResult);
            }
        });
    }

    private void executeIsAdjustSdkEnabled(final CallbackContext callbackContext) throws JSONException {
        Adjust.isEnabled(this.cordova.getActivity().getApplicationContext(), new OnIsEnabledListener() {
            @Override
            public void onIsEnabledRead(boolean isEnabled) {
                PluginResult pluginResult = new PluginResult(Status.OK, isEnabled);
                callbackContext.sendPluginResult(pluginResult);
            }
        });
    }

    private void executeGetLastDeeplink(final CallbackContext callbackContext) throws JSONException {
        Adjust.getLastDeeplink(this.cordova.getActivity().getApplicationContext(), new OnLastDeeplinkReadListener() {
            @Override
            public void onLastDeeplinkRead(Uri uri) {
                String uriS = (uri != null) ? uri.toString() : "";
                PluginResult pluginResult = new PluginResult(Status.OK, uriS);
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
            }
        });
    }

    private void executeGetGooglePlayInstallReferrer(final CallbackContext callbackContext) throws JSONException {
        Adjust.getGooglePlayInstallReferrer(this.cordova.getActivity().getApplicationContext(), new OnGooglePlayInstallReferrerReadListener() {
            @Override
            public void onInstallReferrerRead(GooglePlayInstallReferrerDetails googlePlayInstallReferrerDetails) {
                JSONObject jsonData = new JSONObject(getGooglePlayInstallReferrerMap(googlePlayInstallReferrerDetails));
                PluginResult pluginResult = new PluginResult(Status.OK, jsonData);
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
            }

            @Override
            public void onFail(String message) {
                JSONObject jsonData = new JSONObject(getGooglePlayInstallReferrerFailureMap(message));
                PluginResult pluginResult = new PluginResult(Status.OK, jsonData);
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
            }
        });
    }

    private void executeTrackEvent(final JSONArray args) throws JSONException {

        AdjustEvent adjustEvent = serializeAdjustEventFromJson(args);
        if (adjustEvent == null) {
            return;
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
            } catch (NumberFormatException ignore) {
            }
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
            } catch (NumberFormatException ignore) {
            }
        }

        JSONArray partnerParametersJson = (JSONArray) parameters.get(KEY_PARTNER_PARAMETERS);
        JSONArray callbackParametersJson = (JSONArray) parameters.get(KEY_CALLBACK_PARAMETERS);
        String[] partnerParameters = jsonArrayToArray(partnerParametersJson);
        String[] callbackParameters = jsonArrayToArray(callbackParametersJson);

        // Callback parameters.
        for (int i = 0; i < callbackParameters.length; i += 2) {
            String key = callbackParameters[i];
            String value = callbackParameters[i + 1];
            subscription.addCallbackParameter(key, value);
        }

        // Partner parameters.
        for (int i = 0; i < partnerParameters.length; i += 2) {
            String key = partnerParameters[i];
            String value = partnerParameters[i + 1];
            subscription.addPartnerParameter(key, value);
        }

        // Track subscription.
        Adjust.trackPlayStoreSubscription(subscription);
    }

    private void executeVerifyPlayStorePurchase(
            final JSONArray args,
            final CallbackContext callbackContext) throws JSONException {

        String params = args.getString(0);
        JSONArray jsonArrayParams = new JSONArray(params);
        JSONObject jsonParameters = jsonArrayParams.optJSONObject(0);
        Map<String, Object> parameters = jsonObjectToMap(jsonParameters);

        String productId = null;
        String purchaseToken = null;

        // Product ID.
        if (parameters.containsKey(KEY_PRODUCT_ID)) {
            productId = parameters.get(KEY_PRODUCT_ID).toString();
        }
        // Purchase token.
        if (parameters.containsKey(KEY_PURCHASE_TOKEN)) {
            purchaseToken = parameters.get(KEY_PURCHASE_TOKEN).toString();
        }

        // Create purchase instance.
        final AdjustPlayStorePurchase playStorePurchase = new AdjustPlayStorePurchase(productId, purchaseToken);
        // Verify purchase.
        Adjust.verifyPlayStorePurchase(playStorePurchase, new OnPurchaseVerificationFinishedListener() {
            @Override
            public void onVerificationFinished(AdjustPurchaseVerificationResult verificationResult) {
                Map<String, String> resultMap = getPurchaseVerificationResultMap(verificationResult);
                JSONObject verificationResultJsonData = new JSONObject(resultMap);
                PluginResult pluginResult = new PluginResult(Status.OK, verificationResultJsonData);
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
            }
        });
    }

    private void executeVerifyAndTrackPlayStorePurchase(
            final JSONArray args,
            final CallbackContext callbackContext) throws JSONException {

        AdjustEvent adjustEvent = serializeAdjustEventFromJson(args);
        if (adjustEvent == null) {
            return;
        }
        Adjust.verifyAndTrackPlayStorePurchase(adjustEvent, new OnPurchaseVerificationFinishedListener() {
            @Override
            public void onVerificationFinished(AdjustPurchaseVerificationResult verificationResult) {
                Map<String, String> resultMap = getPurchaseVerificationResultMap(verificationResult);
                JSONObject verificationResultJsonData = new JSONObject(resultMap);
                PluginResult pluginResult = new PluginResult(Status.OK, verificationResultJsonData);
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
            }
        });
    }

    private void executeProcessAndResolveDeeplink(
            final JSONArray args,
            final CallbackContext callbackContext) throws JSONException {
        String url = args.getString(0);
        AdjustDeeplink adjustDeeplink = new AdjustDeeplink(Uri.parse(url));

        Adjust.processAndResolveDeeplink(adjustDeeplink, this.cordova.getActivity().getApplicationContext(), new OnDeeplinkResolvedListener() {
            @Override
            public void onDeeplinkResolved(String resolvedLink) {
                PluginResult pluginResult = new PluginResult(Status.OK, resolvedLink);
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
            }
        });
    }

    private void executeTrackThirdPartySharing(final JSONArray args) throws JSONException {
        String params = args.getString(0);
        JSONArray jsonArrayParams = new JSONArray(params);
        JSONObject jsonParameters = jsonArrayParams.optJSONObject(0);
        Map<String, Object> parameters = jsonObjectToMap(jsonParameters);

        Boolean isEnabled = null;

        // Is enabled.
        if (parameters.containsKey(KEY_IS_ENABLED)) {
            if (parameters.get(KEY_IS_ENABLED) != null) {
                isEnabled = Boolean.valueOf(parameters.get(KEY_IS_ENABLED).toString());
            }
        }

        final AdjustThirdPartySharing adjustThirdPartySharing = new AdjustThirdPartySharing(isEnabled);

        JSONArray granularOptionsJson = (JSONArray) parameters.get(KEY_GRANULAR_OPTIONS);
        String[] granularOptions = jsonArrayToArray(granularOptionsJson);

        // Granular options.
        for (int i = 0; i < granularOptions.length; i += 3) {
            adjustThirdPartySharing.addGranularOption(
                    granularOptions[i],
                    granularOptions[i + 1],
                    granularOptions[i + 2]);
        }

        JSONArray partnerSharingSettingsJson = (JSONArray) parameters.get(KEY_PARTNER_SHARING_SETTINGS);
        String[] partnerSharingSettings = jsonArrayToArray(partnerSharingSettingsJson);

        // Partner sharing settings.
        for (int i = 0; i < partnerSharingSettings.length; i += 3) {
            adjustThirdPartySharing.addPartnerSharingSetting(
                    partnerSharingSettings[i],
                    partnerSharingSettings[i + 1],
                    Boolean.parseBoolean(partnerSharingSettings[i + 2]));
        }

        // Track third party sharing.
        Adjust.trackThirdPartySharing(adjustThirdPartySharing);
    }

    private void executeTrackAdRevenue(final JSONArray args) throws JSONException {
        String params = args.getString(0);
        JSONArray jsonArrayParams = new JSONArray(params);
        JSONObject jsonParameters = jsonArrayParams.optJSONObject(0);
        Map<String, Object> parameters = jsonObjectToMap(jsonParameters);

        String revenue = null;
        String adImpressionsCount = null;
        String source = null;
        String currency = null;
        String adRevenueNetwork = null;
        String adRevenueUnit = null;
        String adRevenuePlacement = null;

        if (parameters.containsKey(KEY_SOURCE)) {
            source = parameters.get(KEY_SOURCE).toString();
        }
        if (parameters.containsKey(KEY_REVENUE)) {
            revenue = parameters.get(KEY_REVENUE).toString();
        }
        if (parameters.containsKey(KEY_CURRENCY)) {
            currency = parameters.get(KEY_CURRENCY).toString();
        }
        if (parameters.containsKey(KEY_AD_IMPRESSIONS_COUNT)) {
            adImpressionsCount = parameters.get(KEY_AD_IMPRESSIONS_COUNT).toString();
        }
        if (parameters.containsKey(KEY_AD_REVENUE_NETWORK)) {
            adRevenueNetwork = parameters.get(KEY_AD_REVENUE_NETWORK).toString();
        }
        if (parameters.containsKey(KEY_AD_REVENUE_UNIT)) {
            adRevenueUnit = parameters.get(KEY_AD_REVENUE_UNIT).toString();
        }
        if (parameters.containsKey(KEY_AD_REVENUE_PLACEMENT)) {
            adRevenuePlacement = parameters.get(KEY_AD_REVENUE_PLACEMENT).toString();
        }

        JSONArray partnerParametersJson = (JSONArray) parameters.get(KEY_PARTNER_PARAMETERS);
        JSONArray callbackParametersJson = (JSONArray) parameters.get(KEY_CALLBACK_PARAMETERS);
        String[] partnerParameters = jsonArrayToArray(partnerParametersJson);
        String[] callbackParameters = jsonArrayToArray(callbackParametersJson);

        final AdjustAdRevenue adjustAdRevenue = new AdjustAdRevenue(source);

        // Revenue and currency.
        if (isFieldValid(revenue) && isFieldValid(currency)) {
            try {
                double revenueValue = Double.parseDouble(revenue);
                adjustAdRevenue.setRevenue(revenueValue, currency);
            } catch (Exception e) {
                ILogger logger = AdjustFactory.getLogger();
                logger.error("[AdjustCordova]: Unable to parse revenue.");
            }
        }

        // Callback parameters.
        for (int i = 0; i < callbackParameters.length; i += 2) {
            String key = callbackParameters[i];
            String value = callbackParameters[i + 1];
            adjustAdRevenue.addCallbackParameter(key, value);
        }

        // Partner parameters.
        for (int i = 0; i < partnerParameters.length; i += 2) {
            String key = partnerParameters[i];
            String value = partnerParameters[i + 1];
            adjustAdRevenue.addPartnerParameter(key, value);
        }

        // Ad impressions count.
        if (isFieldValid(adImpressionsCount)) {
            try {
                int adImpressionsCountValue = Integer.parseInt(adImpressionsCount);
                adjustAdRevenue.setAdImpressionsCount(adImpressionsCountValue);
            } catch (Exception e) {
                ILogger logger = AdjustFactory.getLogger();
                logger.error("[AdjustCordova]: Unable to parse ad impressions count.");
            }
        }

        // Ad revenue network.
        if (isFieldValid(adRevenueNetwork)) {
            adjustAdRevenue.setAdRevenueNetwork(adRevenueNetwork);
        }

        // Ad revenue unit.
        if (isFieldValid(adRevenueUnit)) {
            adjustAdRevenue.setAdRevenueUnit(adRevenueUnit);
        }

        // Ad revenue placement.
        if (isFieldValid(adRevenuePlacement)) {
            adjustAdRevenue.setAdRevenuePlacement(adRevenuePlacement);
        }

        // Track ad revenue.
        Adjust.trackAdRevenue(adjustAdRevenue);
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

        if (!jsonParameters.isNull(KEY_PURCHASE_VERIFICATION_URL)) {
            try {
                String value = jsonParameters.getString(KEY_PURCHASE_VERIFICATION_URL);
                testOptions.purchaseVerificationUrl = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse purchase verification URL.");
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

        if (!jsonParameters.isNull(KEY_PURCHASE_VERIFICATION_PATH)) {
            try {
                String value = jsonParameters.getString(KEY_PURCHASE_VERIFICATION_PATH);
                testOptions.purchaseVerificationPath = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse purchase verification path.");
            }
        }

        // Keep this for the record. Not needed anymore since extraction of test-options.
        // if (!jsonParameters.isNull(KEY_USE_TEST_CONNECTION_OPTIONS)) {
        //     try {
        //         boolean value = jsonParameters.getBoolean(KEY_USE_TEST_CONNECTION_OPTIONS);
        //         testOptions.useTestConnectionOptions = value;
        //     } catch (JSONException e) {
        //         AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse use test connection options.");
        //     }
        // }

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
        if (attributionChangedCallbackContext == null) {
            return;
        }

        JSONObject attributionJsonData = new JSONObject(getAttributionMap(attribution));
        PluginResult pluginResult = new PluginResult(Status.OK, attributionJsonData);
        pluginResult.setKeepCallback(true);
        attributionChangedCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onEventTrackingSucceeded(AdjustEventSuccess event) {
        if (eventTrackingSucceededCallbackContext == null) {
            return;
        }

        JSONObject jsonData = new JSONObject(getEventSuccessMap(event));
        PluginResult pluginResult = new PluginResult(Status.OK, jsonData);
        pluginResult.setKeepCallback(true);
        eventTrackingSucceededCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onEventTrackingFailed(AdjustEventFailure event) {
        if (eventTrackingFailedCallbackContext == null) {
            return;
        }

        JSONObject jsonData = new JSONObject(getEventFailureMap(event));
        PluginResult pluginResult = new PluginResult(Status.OK, jsonData);
        pluginResult.setKeepCallback(true);
        eventTrackingFailedCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onSessionTrackingSucceeded(AdjustSessionSuccess session) {
        if (sessionTrackingSucceededCallbackContext == null) {
            return;
        }

        JSONObject jsonData = new JSONObject(getSessionSuccessMap(session));
        PluginResult pluginResult = new PluginResult(Status.OK, jsonData);
        pluginResult.setKeepCallback(true);
        sessionTrackingSucceededCallbackContext.sendPluginResult(pluginResult);
    }

    @Override
    public void onSessionTrackingFailed(AdjustSessionFailure session) {
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
        if (deferredDeeplinkReceivedCallbackContext != null) {
            PluginResult pluginResult = new PluginResult(Status.OK, deeplink.toString());
            pluginResult.setKeepCallback(true);
            deferredDeeplinkReceivedCallbackContext.sendPluginResult(pluginResult);
        }

        return this.shouldLaunchDeeplink;
    }

    private AdjustEvent serializeAdjustEventFromJson(final JSONArray args) throws JSONException {
        String params = args.getString(0);
        JSONArray jsonArrayParams = new JSONArray(params);
        JSONObject jsonParameters = jsonArrayParams.optJSONObject(0);
        Map<String, Object> parameters = jsonObjectToMap(jsonParameters);

        String eventToken = null;
        String revenue = null;
        String currency = null;
        String transactionId = null;
        String callbackId = null;
        String productId = null;
        String purchaseToken = null;
        String deduplicationId = null;

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
        if (parameters.containsKey(KEY_PRODUCT_ID)) {
            productId = parameters.get(KEY_PRODUCT_ID).toString();
        }
        if (parameters.containsKey(KEY_PURCHASE_TOKEN)) {
            purchaseToken = parameters.get(KEY_PURCHASE_TOKEN).toString();
        }

        if (parameters.containsKey(KEY_DEDUPLICATION_ID)) {
            deduplicationId = parameters.get(KEY_DEDUPLICATION_ID).toString();
        }

        JSONArray partnerParametersJson = (JSONArray) parameters.get(KEY_PARTNER_PARAMETERS);
        JSONArray callbackParametersJson = (JSONArray) parameters.get(KEY_CALLBACK_PARAMETERS);
        String[] partnerParameters = jsonArrayToArray(partnerParametersJson);
        String[] callbackParameters = jsonArrayToArray(callbackParametersJson);

        final AdjustEvent adjustEvent = new AdjustEvent(eventToken);
        if (!adjustEvent.isValid()) {
            return null;
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
        for (int i = 0; i < callbackParameters.length; i += 2) {
            String key = callbackParameters[i];
            String value = callbackParameters[i + 1];
            adjustEvent.addCallbackParameter(key, value);
        }

        // Partner parameters.
        for (int i = 0; i < partnerParameters.length; i += 2) {
            String key = partnerParameters[i];
            String value = partnerParameters[i + 1];
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

        // Product ID.
        if (isFieldValid(productId)) {
            adjustEvent.setProductId(productId);
        }

        // Purchase token.
        if (isFieldValid(purchaseToken)) {
            adjustEvent.setPurchaseToken(purchaseToken);
        }

        // Deduplication ID.
        if (isFieldValid(deduplicationId)) {
            adjustEvent.setDeduplicationId(deduplicationId);
        }
        return adjustEvent;
    }
}
