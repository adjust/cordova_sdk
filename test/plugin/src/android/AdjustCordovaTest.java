package com.adjust.test;

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
import com.adjust.test.TestLibrary;

public class AdjustCordovaTest extends CordovaPlugin {
    private static final String TAG = "AdjustCordovaTest";
    private static final String COMMAND_START_TEST_SESSION = "startTestSession";
    private static final String COMMAND_ADD_INFO_TO_SEND = "addInfoToSend";
    private static final String COMMAND_SEND_INFO_TO_SERVER = "sendInfoToServer";
    private static final String COMMAND_ADD_TEST = "addTest";
    private static final String COMMAND_ADD_TEST_DIRECTORY = "addTestDirectory";

    private TestLibrary testLibrary;
    private CallbackContext commandCallbackContext;
    private List<String> selectedTests = new ArrayList<String>();
    private List<String> selectedTestDirs = new ArrayList<String>();

    @Override
    public boolean execute(String action, final JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals(COMMAND_START_TEST_SESSION)) {
            final String baseUrl = args.getString(0);
            final String sdkVersion = args.getString(1);
            this.commandCallbackContext = callbackContext;
            testLibrary = new TestLibrary(
                baseUrl, 
                new CommandListener(
                    this.cordova.getActivity().getApplicationContext(),
                    this.commandCallbackContext));

            for (int i = 0; i < selectedTests.size(); i++) {
                testLibrary.addTest(selectedTests.get(i));
            }
            for(int i = 0; i < selectedTestDirs.size(); i++) {
                testLibrary.addTestDirectory(selectedTestDirs.get(i));
            }

            testLibrary.startTestSession(sdkVersion);
            return true;
        } else if (action.equals(COMMAND_ADD_INFO_TO_SEND)) {
            final String key = args.getString(0);
            final String value = args.getString(1);
            if (testLibrary != null) {
                testLibrary.addInfoToSend(key, value);
            }
            return true;
        } else if (action.equals(COMMAND_SEND_INFO_TO_SERVER)) {
            final String basePath = args.getString(0);
            if (testLibrary != null) {
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
