package com.adjust.sdktesting;

import com.adjust.testlibrary.TestLibrary;

import android.net.Uri;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
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

public class AdjustTestingCordova extends CordovaPlugin {
    private static final String TAG = "AdjustTestingCordova";

    private static final String COMMAND_START_TEST_SESSION  = "startTestSession";
    private static final String COMMAND_ADD_INFO_TO_SEND    = "addInfoToSend";
    private static final String COMMAND_SEND_INFO_TO_SERVER = "sendInfoToServer";
    private static final String COMMAND_ADD_TEST            = "addTest";
    private static final String COMMAND_ADD_TEST_DIRECTORY  = "addTestDirectory";

    private CallbackContext commandCallbackContext;
    private TestLibrary testLibrary;
    private List<String> selectedTests = new ArrayList<String>();
    private List<String> selectedTestDirs = new ArrayList<String>();

    @Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals(COMMAND_START_TEST_SESSION)) {
            final String baseUrl = args.getString(0);
            //Log.d(TAG, "startTestSession() with baseUrl[" + baseUrl + "]");

            this.commandCallbackContext = callbackContext;

            testLibrary = new TestLibrary(baseUrl, 
                    new CommandListener(
                        this.cordova.getActivity().getApplicationContext(), 
                        this.commandCallbackContext)
                    );

            for(int i = 0; i < selectedTests.size(); i++) {
                testLibrary.addTest(selectedTests.get(i));
            }

            for(int i = 0; i < selectedTestDirs.size(); i++) {
                testLibrary.addTestDirectory(selectedTestDirs.get(i));
            }

            testLibrary.startTestSession("cordova4.12.5@android4.12.4");
            return true;
        } else if (action.equals(COMMAND_ADD_INFO_TO_SEND)) {
            final String key = args.getString(0);
            final String value = args.getString(1);
            //Log.d(TAG, "AddInfoToSend() with key[" + key + "] and value[" + value + "]");

            if(testLibrary != null) {
                testLibrary.addInfoToSend(key, value);
            }

            return true;
        } else if (action.equals(COMMAND_SEND_INFO_TO_SERVER)) {
            final String basePath = args.getString(0);
            //Log.d(TAG, "sendInfoToServer(): " + basePath);

            if(testLibrary != null) {
                testLibrary.sendInfoToServer(basePath);
            }

            return true;
        } else if (action.equals(COMMAND_ADD_TEST)) {
            this.selectedTests.add(args.getString(0));

            return true;
        } else if (action.equals(COMMAND_ADD_TEST_DIRECTORY)) {
            this.selectedTestDirs.add(args.getString(0));

            return true;
        }

        Log.e(TAG, "Invalid call: " + action);
        return false;
    }
}
