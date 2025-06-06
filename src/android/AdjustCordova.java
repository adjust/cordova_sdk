package com.adjust.sdk;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    private boolean isDeferredDeeplinkOpeningEnabled = true;
    private CallbackContext attributionCallbackContext;
    private CallbackContext eventTrackingSucceededCallbackContext;
    private CallbackContext eventTrackingFailedCallbackContext;
    private CallbackContext sessionTrackingSucceededCallbackContext;
    private CallbackContext sessionTrackingFailedCallbackContext;
    private CallbackContext deferredDeeplinkCallbackContext;

    @Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals(COMMAND_INIT_SDK)) {
            executeInitSdk(args);
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
        } else if (action.equals(COMMAND_SET_PUSH_TOKEN)) {
            final String token = args.getString(0);
            Adjust.setPushToken(token, this.cordova.getActivity().getApplicationContext());
        } else if (action.equals(COMMAND_GET_ATTRIBUTION)) {
            executeGetAttribution(callbackContext);
        } else if (action.equals(COMMAND_GET_ADID)) {
            executeGetAdid(callbackContext);
        } else if (action.equals(COMMAND_GET_GOOGLE_AD_ID)) {
            executeGetGoogleAdid(callbackContext);
        } else if (action.equals(COMMAND_GET_AMAZON_AD_ID)) {
            executeGetAmazonAdid(callbackContext);
        } else if (action.equals(COMMAND_GET_SDK_VERSION)) {
            executeGetSdkVersion(callbackContext);
        } else if (action.equals(COMMAND_ADD_GLOBAL_CALLBACK_PARAMETER)) {
            final String key = args.getString(0);
            final String value = args.getString(1);
            Adjust.addGlobalCallbackParameter(key, value);
        } else if (action.equals(COMMAND_REMOVE_GLOBAL_CALLBACK_PARAMETER)) {
            final String key = args.getString(0);
            Adjust.removeGlobalCallbackParameter(key);
        } else if (action.equals(COMMAND_REMOVE_GLOBAL_CALLBACK_PARAMETERS)) {
            Adjust.removeGlobalCallbackParameters();
        } else if (action.equals(COMMAND_ADD_GLOBAL_PARTNER_PARAMETER)) {
            final String key = args.getString(0);
            final String value = args.getString(1);
            Adjust.addGlobalPartnerParameter(key, value);
        } else if (action.equals(COMMAND_REMOVE_GLOBAL_PARTNER_PARAMETER)) {
            final String key = args.getString(0);
            Adjust.removeGlobalPartnerParameter(key);
        } else if (action.equals(COMMAND_REMOVE_GLOBAL_PARTNER_PARAMETERS)) {
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
            if (executeProcessDeeplink(args) == false) {
                return false;
            }
        } else if (action.equals(COMMAND_PROCESS_AND_RESOLVE_DEEPLINK)) {
            if (executeProcessAndResolveDeeplink(args, callbackContext) == false) {
                return false;
            }
        } else if (action.equals(COMMAND_GET_LAST_DEEPLINK)) {
            executeGetLastDeeplink(callbackContext);
        } else if (action.equals(COMMAND_END_FIRST_SESSION_DELAY)) {
            Adjust.endFirstSessionDelay();
        } else if (action.equals(COMMAND_ENABLE_COPPA_COMPLIANCE_IN_DELAY)) {
            Adjust.enableCoppaComplianceInDelay();
        } else if (action.equals(COMMAND_DISABLE_COPPA_COMPLIANCE_IN_DELAY)) {
            Adjust.disableCoppaComplianceInDelay();
        } else if (action.equals(COMMAND_ENABLE_PLAY_STORE_KIDS_COMPLIANCE_IN_DELAY)) {
            Adjust.enablePlayStoreKidsComplianceInDelay();
        } else if (action.equals(COMMAND_DISABLE_PLAY_STORE_KIDS_COMPLIANCE_IN_DELAY)) {
            Adjust.disablePlayStoreKidsComplianceInDelay();
        } else if (action.equals(COMMAND_SET_EXTERNAL_DEVICE_ID_IN_DELAY)) {
            final String externalDeviceId = args.getString(0);
            Adjust.setExternalDeviceIdInDelay(externalDeviceId);
        } else if (action.equals(COMMAND_SET_TEST_OPTIONS)) {
            executeSetTestOptions(args);
        } else if (action.equals(COMMAND_TEARDOWN)) {
            attributionCallbackContext = null;
            eventTrackingSucceededCallbackContext = null;
            eventTrackingFailedCallbackContext = null;
            sessionTrackingSucceededCallbackContext = null;
            sessionTrackingFailedCallbackContext = null;
            deferredDeeplinkCallbackContext = null;
            isDeferredDeeplinkOpeningEnabled = true;
        } else if (action.equals(COMMAND_SET_SKAN_UPDATED_CALLBACK)) {
            // ignore on android
        } else if (action.equals(COMMAND_VERIFY_AND_TRACK_APP_STORE_PURCHASE)) {
            // ignore on android
            HashMap<String, String> mapResponse = new HashMap<>();
            JSONObject response = new JSONObject(mapResponse);
            PluginResult pluginResult = new PluginResult(Status.OK, response);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        } else if (action.equals(COMMAND_VERIFY_APP_STORE_PURCHASE)) {
            // ignore on android
            HashMap<String, String> mapResponse = new HashMap<>();
            JSONObject response = new JSONObject(mapResponse);
            PluginResult pluginResult = new PluginResult(Status.OK, response);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        } else if (action.equals(COMMAND_REQUEST_APP_TRACKING_AUTHORIZATION)) {
            // ignore on android
            PluginResult pluginResult = new PluginResult(Status.OK, -1);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        } else if (action.equals(COMMAND_GET_APP_TRACKING_AUTHORIZATION_STATUS)) {
            // ignore on android
            PluginResult pluginResult = new PluginResult(Status.OK, -1);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        } else if (action.equals(COMMAND_UPDATE_SKAN_CONVERSION_VALUE)) {
            // ignore on android
            HashMap<String, String> skanResponse = new HashMap<>();
            skanResponse.put("error", "SKAdNetwork functionality not available on Android platform");
            JSONObject response = new JSONObject(skanResponse);
            PluginResult pluginResult = new PluginResult(Status.OK, response);
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        } else if (action.equals(COMMAND_GET_IDFA)) {
            // ignore on android
            PluginResult pluginResult = new PluginResult(Status.OK, "");
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        } else if (action.equals(COMMAND_GET_IDFV)) {
            // ignore on android
            PluginResult pluginResult = new PluginResult(Status.OK, "");
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        } else {
            Logger logger = (Logger) AdjustFactory.getLogger();
            logger.error(String.format("[AdjustCordova]: Invalid call (%s).", action));
            return false;
        }
        return true;
    }

    private void executeInitSdk(final JSONArray args) throws JSONException {
        String params = args.getString(0);
        JSONArray jsonArrayParams = new JSONArray(params);
        JSONObject jsonParameters = jsonArrayParams.optJSONObject(0);
        Map<String, Object> parameters = jsonObjectToMap(jsonParameters);

        String appToken = null;
        String environment = null;
        String logLevel = null;
        boolean isLogLevelSuppress = false;

        // app token
        if (parameters.containsKey(KEY_APP_TOKEN)) {
            appToken = parameters.get(KEY_APP_TOKEN).toString();
        }
        // environment
        if (parameters.containsKey(KEY_ENVIRONMENT)) {
            environment = parameters.get(KEY_ENVIRONMENT).toString();
        }
        // suppress log level
        if (parameters.containsKey(KEY_LOG_LEVEL)) {
            logLevel = parameters.get(KEY_LOG_LEVEL).toString().toUpperCase();
            if (isFieldValid(logLevel) && logLevel.equals("SUPPRESS")) {
                isLogLevelSuppress = true;
            }
        }

        final AdjustConfig adjustConfig = new AdjustConfig(
                this.cordova.getActivity().getApplicationContext(),
                appToken,
                environment,
                isLogLevelSuppress);
        if (!adjustConfig.isValid()) {
            return;
        }

        // SDK prefix
        if (parameters.containsKey(KEY_SDK_PREFIX)) {
            String sdkPrefix = parameters.get(KEY_SDK_PREFIX).toString();
            if (isFieldValid(sdkPrefix)) {
                adjustConfig.setSdkPrefix(sdkPrefix);
            }
        }

        // log level
        if (isFieldValid(logLevel)) {
            switch (logLevel) {
                case "VERBOSE":
                    adjustConfig.setLogLevel(LogLevel.VERBOSE);
                    break;
                case "DEBUG":
                    adjustConfig.setLogLevel(LogLevel.DEBUG);
                    break;
                case "WARN":
                    adjustConfig.setLogLevel(LogLevel.WARN);
                    break;
                case "ERROR":
                    adjustConfig.setLogLevel(LogLevel.ERROR);
                    break;
                case "ASSERT":
                    adjustConfig.setLogLevel(LogLevel.ASSERT);
                    break;
                case "SUPPRESS":
                    adjustConfig.setLogLevel(LogLevel.SUPPRESS);
                    break;
                case "INFO":
                default:
                    adjustConfig.setLogLevel(LogLevel.INFO);
                    break;
            }
        }

        // COPPA compliance
        if (parameters.containsKey(KEY_IS_COPPA_COMPLIANCE_ENABLED)) {
            String strIsCoppaComplianceEnabled = parameters.get(KEY_IS_COPPA_COMPLIANCE_ENABLED).toString();
            boolean isCoppaComplianceEnabled = Boolean.parseBoolean(strIsCoppaComplianceEnabled);
            if (isCoppaComplianceEnabled) {
                adjustConfig.enableCoppaCompliance();
            }
        }

        // Google Play Store kids compliance
        if (parameters.containsKey(KEY_IS_PLAY_STORE_KIDS_COMPLIANCE_ENABLED)) {
            String strIsPlayStoreKidsComplianceEnabled = parameters.get(KEY_IS_PLAY_STORE_KIDS_COMPLIANCE_ENABLED).toString();
            boolean isPlayStoreKidsComplianceEnabled = Boolean.parseBoolean(strIsPlayStoreKidsComplianceEnabled);
            if (isPlayStoreKidsComplianceEnabled) {
                adjustConfig.enablePlayStoreKidsCompliance();
            }
        }

        // read device info only once
        if (parameters.containsKey(KEY_IS_DEVICE_IDS_READING_ONCE_ENABLED)) {
            String strIsDeviceIdsReadingOnceEnabled = parameters.get(KEY_IS_DEVICE_IDS_READING_ONCE_ENABLED).toString();
            boolean isDeviceIdsReadingOnceEnabled = Boolean.parseBoolean(strIsDeviceIdsReadingOnceEnabled);
            if (isDeviceIdsReadingOnceEnabled) {
                adjustConfig.enableDeviceIdsReadingOnce();
            }
        }

        // event deduplication buffer size
        if (parameters.containsKey(KEY_EVENT_DEDUPLICATION_IDS_MAX_SIZE)) {
            String strEventDeduplicationIdsMaxSize = parameters.get(KEY_EVENT_DEDUPLICATION_IDS_MAX_SIZE).toString();
            try {
                int eventDeduplicationIdsMaxSize = Integer.valueOf(strEventDeduplicationIdsMaxSize);
                adjustConfig.setEventDeduplicationIdsMaxSize(eventDeduplicationIdsMaxSize);
            } catch (Exception e) {}
        }

        // URL strategy
        if (parameters.containsKey(KEY_URL_STRATEGY_DOMAINS) &&
                parameters.containsKey(KEY_USE_SUBDOMAINS) &&
                parameters.containsKey(KEY_IS_DATA_RESIDENCY)) {
            String strUrlStrategyDomains = parameters.get(KEY_URL_STRATEGY_DOMAINS).toString();
            try {
                JSONArray jsonArray = new JSONArray(strUrlStrategyDomains);
                ArrayList<String> urlStrategyDomainsArray = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i += 1) {
                    urlStrategyDomainsArray.add((String) jsonArray.get(i));
                }
                String strShouldUseSubdomains = parameters.get(KEY_USE_SUBDOMAINS).toString();
                boolean useSubdomains = Boolean.parseBoolean(strShouldUseSubdomains);

                String strIsDataResidency = parameters.get(KEY_IS_DATA_RESIDENCY).toString();
                boolean isDataResidency = Boolean.parseBoolean(strIsDataResidency);

                adjustConfig.setUrlStrategy(urlStrategyDomainsArray, useSubdomains, isDataResidency);
            } catch (JSONException ignored) {}
        }

        // main process name
        if (parameters.containsKey(KEY_PROCESS_NAME)) {
            String processName = parameters.get(KEY_PROCESS_NAME).toString();
            if (isFieldValid(processName)) {
                adjustConfig.setProcessName(processName);
            }
        }

        // default tracker
        if (parameters.containsKey(KEY_DEFAULT_TRACKER)) {
            String defaultTracker = parameters.get(KEY_DEFAULT_TRACKER).toString();
            if (isFieldValid(defaultTracker)) {
                adjustConfig.setDefaultTracker(defaultTracker);
            }
        }

        // external device ID
        if (parameters.containsKey(KEY_EXTERNAL_DEVICE_ID)) {
            String externalDeviceId = parameters.get(KEY_EXTERNAL_DEVICE_ID).toString();
            if (isFieldValid(externalDeviceId)) {
                adjustConfig.setExternalDeviceId(externalDeviceId);
            }
        }

        // custom preinstall file path
        if (parameters.containsKey(KEY_PREINSTALL_FILE_PATH)) {
            String preinstallFilePath = parameters.get(KEY_PREINSTALL_FILE_PATH).toString();
            if (isFieldValid(preinstallFilePath)) {
                adjustConfig.setPreinstallFilePath(preinstallFilePath);
            }
        }

        // FB app ID (META install referrer)
        if (parameters.containsKey(KEY_FB_APP_ID)) {
            String fbAppId = parameters.get(KEY_FB_APP_ID).toString();
            if (isFieldValid(fbAppId)) {
                adjustConfig.setFbAppId(fbAppId);
            }
        }

        // sending in background
        if (parameters.containsKey(KEY_IS_SENDING_IN_BACKGROUND_ENABLED)) {
            String strIsSendingInBackgroundEnabled = parameters.get(KEY_IS_SENDING_IN_BACKGROUND_ENABLED).toString();
            boolean isSendingInBackgroundEnabled = Boolean.parseBoolean(strIsSendingInBackgroundEnabled);
            if (isSendingInBackgroundEnabled) {
                adjustConfig.enableSendingInBackground();
            }
        }

        // cost data in attribution callback
        if (parameters.containsKey(KEY_IS_COST_DATA_IN_ATTRIBUTION_ENABLED)) {
            String strIsCostDataInAttributionEnabled = parameters.get(KEY_IS_COST_DATA_IN_ATTRIBUTION_ENABLED).toString();
            boolean isCostDataInAttributionEnabled = Boolean.parseBoolean(strIsCostDataInAttributionEnabled);
            if (isCostDataInAttributionEnabled) {
                adjustConfig.enableCostDataInAttribution();
            }
        }

        // preinstall tracking
        if (parameters.containsKey(KEY_IS_PREINSTALL_TRACKING_ENABLED)) {
            String strIsPreinstallTrackingEnabled = parameters.get(KEY_IS_PREINSTALL_TRACKING_ENABLED).toString();
            boolean isPreinstallTrackingEnabled = Boolean.parseBoolean(strIsPreinstallTrackingEnabled);
            if (isPreinstallTrackingEnabled) {
                adjustConfig.enablePreinstallTracking();
            }
        }

        // first session delay
        if (parameters.containsKey(KEY_IS_FIRST_SESSION_DELAY_ENABLED)) {
            String strIsFirstSessionDelayEnabled = parameters.get(KEY_IS_FIRST_SESSION_DELAY_ENABLED).toString();
            boolean isFirstSessionDelayEnabled = Boolean.parseBoolean(strIsFirstSessionDelayEnabled);
            if (isFirstSessionDelayEnabled) {
                adjustConfig.enableFirstSessionDelay();
            }
        }

        // store info
        if (parameters.containsKey(KEY_STORE_INFO)) {
            String strStoreInfo = parameters.get(KEY_STORE_INFO).toString();
            try {
                JSONObject storeInfoJson = new JSONObject(strStoreInfo);
                String storeName = storeInfoJson.optString(KEY_STORE_NAME, null);
                if (isFieldValid(storeName)) {
                    AdjustStoreInfo adjustStoreInfo = new AdjustStoreInfo(storeName);
                    String storeAppId = storeInfoJson.optString(KEY_STORE_APP_ID, null);
                    if (isFieldValid(storeAppId)) {
                        adjustStoreInfo.setStoreAppId(storeAppId);
                    }
                    adjustConfig.setStoreInfo(adjustStoreInfo);
                }
            } catch (JSONException e) {}
        }

        // launching deferred deep link
        if (parameters.containsKey(KEY_IS_DEFERRED_DEEP_LINK_OPENING_ENABLED)) {
            String strIsDeferredDeeplinkOpeningEnabled = parameters.get(KEY_IS_DEFERRED_DEEP_LINK_OPENING_ENABLED).toString();
            isDeferredDeeplinkOpeningEnabled = strIsDeferredDeeplinkOpeningEnabled.equals("true");
        }

        // attribution callback
        if (attributionCallbackContext != null) {
            adjustConfig.setOnAttributionChangedListener(this);
        }

        // event tracking success callback
        if (eventTrackingSucceededCallbackContext != null) {
            adjustConfig.setOnEventTrackingSucceededListener(this);
        }

        // event tracking failure callback
        if (eventTrackingFailedCallbackContext != null) {
            adjustConfig.setOnEventTrackingFailedListener(this);
        }

        // session tracking success callback
        if (sessionTrackingSucceededCallbackContext != null) {
            adjustConfig.setOnSessionTrackingSucceededListener(this);
        }

        // session tracking failure callback
        if (sessionTrackingFailedCallbackContext != null) {
            adjustConfig.setOnSessionTrackingFailedListener(this);
        }

        // deferred deep link callback
        if (deferredDeeplinkCallbackContext != null) {
            adjustConfig.setOnDeferredDeeplinkResponseListener(this);
        }

        // initialize SDK
        Adjust.initSdk(adjustConfig);
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

    private void executeGetAdid(final CallbackContext callbackContext) throws JSONException {
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

    private void executeTrackEvent(final JSONArray args) throws JSONException {
        AdjustEvent adjustEvent = serializeAdjustEventFromJson(args);
        if (adjustEvent == null) {
            return;
        }
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

        // price
        if (parameters.containsKey(KEY_PRICE)) {
            try {
                price = Long.parseLong(parameters.get(KEY_PRICE).toString());
            } catch (NumberFormatException ignore) {
            }
        }

        // currency
        if (parameters.containsKey(KEY_CURRENCY)) {
            currency = parameters.get(KEY_CURRENCY).toString();
        }

        // SKU
        if (parameters.containsKey(KEY_SKU)) {
            sku = parameters.get(KEY_SKU).toString();
        }

        // order ID
        if (parameters.containsKey(KEY_ORDER_ID)) {
            orderId = parameters.get(KEY_ORDER_ID).toString();
        }

        // signature
        if (parameters.containsKey(KEY_SIGNATURE)) {
            signature = parameters.get(KEY_SIGNATURE).toString();
        }

        // purchase token
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

        // purchase time
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

        // callback parameters
        for (int i = 0; i < callbackParameters.length; i += 2) {
            String key = callbackParameters[i];
            String value = callbackParameters[i + 1];
            subscription.addCallbackParameter(key, value);
        }

        // partner parameters
        for (int i = 0; i < partnerParameters.length; i += 2) {
            String key = partnerParameters[i];
            String value = partnerParameters[i + 1];
            subscription.addPartnerParameter(key, value);
        }

        // track subscription
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

        // product ID
        if (parameters.containsKey(KEY_PRODUCT_ID)) {
            productId = parameters.get(KEY_PRODUCT_ID).toString();
        }

        // purchase token
        if (parameters.containsKey(KEY_PURCHASE_TOKEN)) {
            purchaseToken = parameters.get(KEY_PURCHASE_TOKEN).toString();
        }

        final AdjustPlayStorePurchase playStorePurchase = new AdjustPlayStorePurchase(productId, purchaseToken);

        // verify purchase
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

    private Boolean executeProcessDeeplink(final JSONArray args) throws JSONException {
        String params = args.getString(0);
        JSONArray jsonArrayParams = new JSONArray(params);
        JSONObject jsonParameters = jsonArrayParams.optJSONObject(0);
        Map<String, Object> parameters = jsonObjectToMap(jsonParameters);

        String deeplink = null;
        if (parameters.containsKey(KEY_DEEPLINK)) {
            deeplink = parameters.get(KEY_DEEPLINK).toString();
        } else {
            return false;
        }
        AdjustDeeplink adjustDeeplink = new AdjustDeeplink(Uri.parse(deeplink));
        if (parameters.containsKey(KEY_REFERRER)) {
            String referrer = parameters.get(KEY_REFERRER).toString();
            adjustDeeplink.setReferrer(Uri.parse(referrer));
        }

        Adjust.processDeeplink(adjustDeeplink, this.cordova.getActivity().getApplicationContext());
        return true;
    }

    private Boolean executeProcessAndResolveDeeplink(
            final JSONArray args,
            final CallbackContext callbackContext) throws JSONException {
        String params = args.getString(0);
        JSONArray jsonArrayParams = new JSONArray(params);
        JSONObject jsonParameters = jsonArrayParams.optJSONObject(0);
        Map<String, Object> parameters = jsonObjectToMap(jsonParameters);

        String deeplink = null;
        if (parameters.containsKey(KEY_DEEPLINK)) {
            deeplink = parameters.get(KEY_DEEPLINK).toString();
        } else {
            return false;
        }
        AdjustDeeplink adjustDeeplink = new AdjustDeeplink(Uri.parse(deeplink));
        if (parameters.containsKey(KEY_REFERRER)) {
            String referrer = parameters.get(KEY_REFERRER).toString();
            adjustDeeplink.setReferrer(Uri.parse(referrer));
        }

        Adjust.processAndResolveDeeplink(adjustDeeplink, this.cordova.getActivity().getApplicationContext(), new OnDeeplinkResolvedListener() {
            @Override
            public void onDeeplinkResolved(String resolvedLink) {
                PluginResult pluginResult = new PluginResult(Status.OK, resolvedLink);
                pluginResult.setKeepCallback(true);
                callbackContext.sendPluginResult(pluginResult);
            }
        });
        return true;
    }

    private void executeTrackThirdPartySharing(final JSONArray args) throws JSONException {
        String params = args.getString(0);
        JSONArray jsonArrayParams = new JSONArray(params);
        JSONObject jsonParameters = jsonArrayParams.optJSONObject(0);
        Map<String, Object> parameters = jsonObjectToMap(jsonParameters);

        Boolean isEnabled = null;

        // is sharing enabled
        if (parameters.containsKey(KEY_IS_ENABLED)) {
            if (parameters.get(KEY_IS_ENABLED) != null) {
                isEnabled = Boolean.valueOf(parameters.get(KEY_IS_ENABLED).toString());
            }
        }

        final AdjustThirdPartySharing adjustThirdPartySharing = new AdjustThirdPartySharing(isEnabled);

        JSONArray granularOptionsJson = (JSONArray) parameters.get(KEY_GRANULAR_OPTIONS);
        String[] granularOptions = jsonArrayToArray(granularOptionsJson);

        // granular options
        for (int i = 0; i < granularOptions.length; i += 3) {
            adjustThirdPartySharing.addGranularOption(
                    granularOptions[i],
                    granularOptions[i + 1],
                    granularOptions[i + 2]);
        }

        JSONArray partnerSharingSettingsJson = (JSONArray) parameters.get(KEY_PARTNER_SHARING_SETTINGS);
        String[] partnerSharingSettings = jsonArrayToArray(partnerSharingSettingsJson);

        // partner sharing settings
        for (int i = 0; i < partnerSharingSettings.length; i += 3) {
            adjustThirdPartySharing.addPartnerSharingSetting(
                    partnerSharingSettings[i],
                    partnerSharingSettings[i + 1],
                    Boolean.parseBoolean(partnerSharingSettings[i + 2]));
        }

        // track third party sharing
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

        // revenue and currency
        if (isFieldValid(revenue) && isFieldValid(currency)) {
            try {
                double revenueValue = Double.parseDouble(revenue);
                adjustAdRevenue.setRevenue(revenueValue, currency);
            } catch (Exception e) {
                ILogger logger = AdjustFactory.getLogger();
                logger.error("[AdjustCordova]: Unable to parse revenue.");
            }
        }

        // callback parameters
        for (int i = 0; i < callbackParameters.length; i += 2) {
            String key = callbackParameters[i];
            String value = callbackParameters[i + 1];
            adjustAdRevenue.addCallbackParameter(key, value);
        }

        // partner parameters
        for (int i = 0; i < partnerParameters.length; i += 2) {
            String key = partnerParameters[i];
            String value = partnerParameters[i + 1];
            adjustAdRevenue.addPartnerParameter(key, value);
        }

        // ad impressions count
        if (isFieldValid(adImpressionsCount)) {
            try {
                int adImpressionsCountValue = Integer.parseInt(adImpressionsCount);
                adjustAdRevenue.setAdImpressionsCount(adImpressionsCountValue);
            } catch (Exception e) {
                ILogger logger = AdjustFactory.getLogger();
                logger.error("[AdjustCordova]: Unable to parse ad impressions count.");
            }
        }

        // ad revenue network
        if (isFieldValid(adRevenueNetwork)) {
            adjustAdRevenue.setAdRevenueNetwork(adRevenueNetwork);
        }

        // ad revenue unit
        if (isFieldValid(adRevenueUnit)) {
            adjustAdRevenue.setAdRevenueUnit(adRevenueUnit);
        }

        // ad revenue placement
        if (isFieldValid(adRevenuePlacement)) {
            adjustAdRevenue.setAdRevenuePlacement(adRevenuePlacement);
        }

        // track ad revenue
        Adjust.trackAdRevenue(adjustAdRevenue);
    }

    private void executeSetTestOptions(final JSONArray args) throws JSONException {
        JSONObject jsonParameters = args.optJSONObject(0);
        Map<String, Object> parameters = jsonObjectToMap(jsonParameters);
        final AdjustTestOptions testOptions = new AdjustTestOptions();

        if (!jsonParameters.isNull(KEY_DELETE_STATE)) {
            try {
                boolean value = jsonParameters.getBoolean(KEY_DELETE_STATE);
                if (value) {
                    testOptions.context = this.cordova.getActivity().getApplicationContext();
                }
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse context.");
            }
        }

        if (!jsonParameters.isNull(KEY_TEST_URL_OVERWRITE)) {
            try {
                String value = jsonParameters.getString(KEY_TEST_URL_OVERWRITE);
                testOptions.baseUrl = value;
                testOptions.gdprUrl = value;
                testOptions.subscriptionUrl = value;
                testOptions.purchaseVerificationUrl = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse testUrlOverwrite.");
            }
        }

        if (!jsonParameters.isNull(KEY_EXTRA_PATH)) {
            try {
                String value = jsonParameters.getString(KEY_EXTRA_PATH);
                testOptions.basePath = value;
                testOptions.gdprPath = value;
                testOptions.subscriptionPath = value;
                testOptions.purchaseVerificationPath = value;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse extra path.");
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

        if (!jsonParameters.isNull(KEY_IGNORE_SYSTEM_LIFECYCLE_BOOTSTRAP)) {
            try {
                boolean ignoreSystemLifecycleBootstrap = jsonParameters.getBoolean(KEY_IGNORE_SYSTEM_LIFECYCLE_BOOTSTRAP);
                testOptions.ignoreSystemLifecycleBootstrap = ignoreSystemLifecycleBootstrap;
            } catch (JSONException e) {
                AdjustFactory.getLogger().error("[AdjustCordova]: Unable to parse ignoreSystemLifecycleBootstrap.");
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
        if (deferredDeeplinkCallbackContext != null) {
            PluginResult pluginResult = new PluginResult(Status.OK, deeplink.toString());
            pluginResult.setKeepCallback(true);
            deferredDeeplinkCallbackContext.sendPluginResult(pluginResult);
        }

        return this.isDeferredDeeplinkOpeningEnabled;
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

        // revenue and currency
        if (isFieldValid(revenue) && isFieldValid(currency)) {
            try {
                double revenueValue = Double.parseDouble(revenue);
                adjustEvent.setRevenue(revenueValue, currency);
            } catch (Exception e) {
                ILogger logger = AdjustFactory.getLogger();
                logger.error("[AdjustCordova]: Unable to parse revenue.");
            }
        }

        // callback parameters
        for (int i = 0; i < callbackParameters.length; i += 2) {
            String key = callbackParameters[i];
            String value = callbackParameters[i + 1];
            adjustEvent.addCallbackParameter(key, value);
        }

        // partner parameters
        for (int i = 0; i < partnerParameters.length; i += 2) {
            String key = partnerParameters[i];
            String value = partnerParameters[i + 1];
            adjustEvent.addPartnerParameter(key, value);
        }

        // transaction ID
        if (isFieldValid(transactionId)) {
            adjustEvent.setOrderId(transactionId);
        }

        // callback ID
        if (isFieldValid(callbackId)) {
            adjustEvent.setCallbackId(callbackId);
        }

        // product ID
        if (isFieldValid(productId)) {
            adjustEvent.setProductId(productId);
        }

        // purchase token
        if (isFieldValid(purchaseToken)) {
            adjustEvent.setPurchaseToken(purchaseToken);
        }

        // deduplication ID
        if (isFieldValid(deduplicationId)) {
            adjustEvent.setDeduplicationId(deduplicationId);
        }

        return adjustEvent;
    }
}
