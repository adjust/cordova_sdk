package com.adjust.sdk;

import android.os.Bundle;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.adjust.sdk.AdjustCordovaUtils.COMMAND_CREATE;
import static com.adjust.sdk.AdjustCordovaUtils.COMMAND_SET_ATTRIBUTION_CALLBACK;
import static com.adjust.sdk.AdjustCordovaUtils.COMMAND_SET_DEFERRED_DEEPLINK_CALLBACK;
import static com.adjust.sdk.AdjustCordovaUtils.COMMAND_SET_EVENT_TRACKING_FAILED_CALLBACK;
import static com.adjust.sdk.AdjustCordovaUtils.COMMAND_SET_EVENT_TRACKING_SUCCEEDED_CALLBACK;
import static com.adjust.sdk.AdjustCordovaUtils.COMMAND_SET_SESSION_TRACKING_FAILED_CALLBACK;
import static com.adjust.sdk.AdjustCordovaUtils.COMMAND_SET_SESSION_TRACKING_SUCCEEDED_CALLBACK;
import static com.adjust.sdk.AdjustCordovaUtils.KEY_APP_TOKEN;
import static com.adjust.sdk.AdjustCordovaUtils.KEY_ENVIRONMENT;

public class EagerAdjustCordova extends AdjustCordova {

    // DO NOT CHANGE (Used in MainActivity)
    private final String AdjustTokenKey = "adjust_token";
    private final String EnvironmentKey = "environment";

    // Adjust event callbacks to set
    private final List<String> callbacks = Arrays.asList(
            COMMAND_SET_ATTRIBUTION_CALLBACK,
            COMMAND_SET_EVENT_TRACKING_SUCCEEDED_CALLBACK,
            COMMAND_SET_EVENT_TRACKING_FAILED_CALLBACK,
            COMMAND_SET_SESSION_TRACKING_SUCCEEDED_CALLBACK,
            COMMAND_SET_SESSION_TRACKING_FAILED_CALLBACK,
            COMMAND_SET_DEFERRED_DEEPLINK_CALLBACK);

    private Map<String, BufferedCallbackContext> callbackContextMap = new HashMap<>();

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);

        for (String callback : callbacks) {
            setBufferedCallback(callback);
        }
        initializeSdk();
    }

    /**
     * Sets `COMMAND_SET_*_CALLBACK` callback as a placeholder until we call
     * setCallback from JS.
     * This enables us to buffer the result of the callback incase
     * JS is slow in calling setCallback.
     */
    private void setBufferedCallback(String action) {
        BufferedCallbackContext bufferedCallbackContext = new BufferedCallbackContext(action, webView);
        JSONArray empty = new JSONArray();
        try {
            this.execute(action, empty, bufferedCallbackContext);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        handleCallback(action, callbackContext);
        return super.execute(action, args, callbackContext);
    }

    /**
     * Stores BufferedCallbacks to the Map as a placeholder callback,
     * for when the JS is slower in setting the callback for each action.
     * BufferedCallback stores the result (if invoked) and passes the
     * result via the `sendPluginResult` call to the callback to JS.
     *
     * @param action
     * @param callbackContext
     */
    private void handleCallback(String action, CallbackContext callbackContext) {
        // Only handle callbacks for specified commands
        if (!callbacks.contains(action)) return;

        BufferedCallbackContext bufferedCallback = callbackContextMap.remove(action);
        if (bufferedCallback != null) {
            bufferedCallback.replayResults(callbackContext);
        } else if (callbackContext instanceof BufferedCallbackContext) {
            callbackContextMap.put(action, (BufferedCallbackContext) callbackContext);
        }
    }

    // Initializer function to call the `execute` method with the required arguments for init.
    private void initializeSdk() {
        
        // Getting data from extras
        Bundle extras = this.cordova.getActivity().getIntent().getExtras();

        // If no eager initialization params were set, e.g. in Debug UI, don't do initialize eagerly.
        if (extras == null) return;

        try {
            String appToken = extras.getString(AdjustTokenKey);
            boolean isDebug = extras.getBoolean(EnvironmentKey, false);

            JSONObject data = new JSONObject();
            data.put(KEY_APP_TOKEN, appToken);
            data.put(
                    KEY_ENVIRONMENT,
                    isDebug
                            ? AdjustConfig.ENVIRONMENT_SANDBOX
                            : AdjustConfig.ENVIRONMENT_PRODUCTION
            );

            JSONArray dataArray = new JSONArray();
            dataArray.put(data);

            JSONArray jsonArray = new JSONArray();
            jsonArray.put(dataArray);

            execute(COMMAND_CREATE, jsonArray, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
