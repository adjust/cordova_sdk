package com.adjust.sdktesting;

import android.content.Context;
import android.util.Log;

import com.adjust.testlibrary.ICommandRawJsonListener;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONException;

import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult.Status;

import java.util.concurrent.atomic.AtomicInteger;

public class CommandListener implements ICommandRawJsonListener {
    private Context mContext;
    private CallbackContext mCommandCallbackContext;
    private AtomicInteger orderCounter = null;

    public CommandListener(Context context, CallbackContext commandCallbackContext) {
        mContext = context;
        mCommandCallbackContext = commandCallbackContext;
        orderCounter = new AtomicInteger(0);
    }

    @Override
    public void executeCommand(String jsonStr) {
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            // Order of packages sent through PluginResult is not reliable, this is solved
            //  through a scheduling mechanism in command_executor.js#scheduleCommand() side.
            // The 'order' entry is used to schedule commands
            jsonObj.put("order", orderCounter.getAndIncrement());

            PluginResult pluginResult = new PluginResult(Status.OK, jsonObj.toString());
            pluginResult.setKeepCallback(true);

            CommandListener.this.mCommandCallbackContext.sendPluginResult(pluginResult);
        } catch(JSONException ex) {
            ex.printStackTrace();
        }
    }
}
