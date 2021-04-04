package com.adjust.sdk;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;

import java.util.ArrayList;

class BufferedCallbackContext extends CallbackContext {
    private ArrayList<PluginResult> results = new ArrayList();

    public BufferedCallbackContext(String callbackId, CordovaWebView webView) {
        super(callbackId, webView);
    }

    @Override
    public void sendPluginResult(PluginResult pluginResult) {
        super.sendPluginResult(pluginResult);
        results.add(pluginResult);
    }

    /**
     * Calls `sendPluginResult` on callbackContext instance, with all buffered PluginResults.
     *
     * @param callbackContext
     */
    public void replayResults(CallbackContext callbackContext) {
        for (PluginResult result : results) {
            callbackContext.sendPluginResult(result);
        }
    }
}