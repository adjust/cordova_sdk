package com.adjust.sdk.purchase;

import android.net.Uri;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult.Status;

public class AdjustPurchaseCordova extends CordovaPlugin implements OnADJPVerificationFinished {
    private static final String KEY_APP_TOKEN                       = "appToken";
    private static final String KEY_ENVIRONMENT                     = "environment";
    private static final String KEY_LOG_LEVEL                       = "logLevel";
    private static final String KEY_SDK_PREFIX                      = "sdkPrefix";

    private static final String COMMAND_INIT                        = "init";
    private static final String COMMAND_VERIFY_PURCHASE_IOS         = "verifyPurchaseiOS";
    private static final String COMMAND_VERIFY_PURCHASE_ANDROID     = "verifyPurchaseAndroid";
    private static final String COMMAND_SET_VERIFICATION_CALLBACK   = "setVerificationCallback";

    private static final String VERIFICATION_MESSAGE                = "message";
    private static final String VERIFICATION_STATUS_CODE            = "statusCode";
    private static final String VERIFICATION_STATE                  = "verificationState";

    private static CallbackContext verificationCallbackContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals(COMMAND_INIT)) {
            JSONObject jsonParameters = args.optJSONObject(0);
            Map<String, Object> parameters = jsonObjectToMap(jsonParameters);

            String appToken = parameters.get(KEY_APP_TOKEN).toString();
            String environment = parameters.get(KEY_ENVIRONMENT).toString();
            String sdkPrefix = parameters.get(KEY_SDK_PREFIX).toString();
            String logLevel = parameters.get(KEY_LOG_LEVEL).toString();

            ADJPConfig adjustConfig = new ADJPConfig(appToken, environment);

            if (adjustConfig == null) {
                return true;
            }

            // Log level
            if (isFieldValid(logLevel)) {
                if (logLevel.equals("VERBOSE")) {
                    adjustConfig.setLogLevel(ADJPLogLevel.VERBOSE);
                } else if (logLevel.equals("DEBUG")) {
                    adjustConfig.setLogLevel(ADJPLogLevel.DEBUG);
                } else if (logLevel.equals("INFO")) {
                    adjustConfig.setLogLevel(ADJPLogLevel.INFO);
                } else if (logLevel.equals("WARN")) {
                    adjustConfig.setLogLevel(ADJPLogLevel.WARN);
                } else if (logLevel.equals("ERROR")) {
                    adjustConfig.setLogLevel(ADJPLogLevel.ERROR);
                } else if (logLevel.equals("ASSERT")) {
                    adjustConfig.setLogLevel(ADJPLogLevel.ASSERT);
                } else {
                    adjustConfig.setLogLevel(ADJPLogLevel.INFO);
                }
            }

            // SDK prefix
            if (isFieldValid(sdkPrefix)) {
                adjustConfig.setSdkPrefix(sdkPrefix);
            }

            AdjustPurchase.init(adjustConfig);

            return true;
        } else if (action.equals(COMMAND_SET_VERIFICATION_CALLBACK)) {
            AdjustPurchaseCordova.verificationCallbackContext = callbackContext;

            return true;
        } else if (action.equals(COMMAND_VERIFY_PURCHASE_ANDROID)) {
            String itemSku = args.getString(0);
            String purchaseToken = args.getString(1);
            String developerPayload = args.getString(2);

            AdjustPurchase.verifyPurchase(itemSku, purchaseToken, developerPayload, this);

            return true;
        } else if (action.equals(COMMAND_VERIFY_PURCHASE_IOS)) {
            return true;
        }

        String errorMessage = String.format("Invalid call (%s)", action);
        ADJPLogger.getInstance().error(errorMessage);

        return false;
    }

    @Override
    public void onVerificationFinished(ADJPVerificationInfo info) {
        JSONObject verificationInfoJsonData = new JSONObject(getVerificationInfoDictionary(info));
        PluginResult pluginResult = new PluginResult(Status.OK, verificationInfoJsonData);
        pluginResult.setKeepCallback(true);

        verificationCallbackContext.sendPluginResult(pluginResult);
        verificationCallbackContext = null;
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

    private boolean isFieldValid(String field) {
        if (field != null) {
            if (!field.equals("") && !field.equals("null")) {
                return true;
            }
        }

        return false;
    }

    private Map<String, String> getVerificationInfoDictionary(ADJPVerificationInfo info) {
        Map<String, String> verificationInfoDictionary = new HashMap<String, String>();

        // Verification state
        if (info.getVerificationState() != null) {
            verificationInfoDictionary.put(VERIFICATION_STATE, info.getVerificationState().toString());
        } else {
            verificationInfoDictionary.put(VERIFICATION_STATE, "");
        }

        // Message
        if (info.getMessage() != null) {
            verificationInfoDictionary.put(VERIFICATION_MESSAGE, info.getMessage().toString());
        } else {
            verificationInfoDictionary.put(VERIFICATION_MESSAGE, "");
        }

        // Status code
        verificationInfoDictionary.put(VERIFICATION_STATUS_CODE, info.getStatusCode().toString());

        return verificationInfoDictionary;
    }
}
