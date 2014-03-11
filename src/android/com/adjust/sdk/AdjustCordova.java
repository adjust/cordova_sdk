package com.adjust.sdk;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdjustCordova extends CordovaPlugin implements OnFinishedListener {

	private static String callbackId;
	private static CordovaWebView cordovaWebView;
	
	@Override
	public boolean execute(String action, JSONArray args,
			CallbackContext callbackContext) throws JSONException {
		if (action.equals("echo")) {
            callbackContext.success(args.getString(0)); 
            return true;
        } else if (action.equals("appDidLaunch")) {
        	Adjust.onResume(this.cordova.getActivity());
        	return true;
        } else if (action.equals("trackEvent")) {
        	String eventToken = args.getString(0);
        	if (args.length() == 1) {
        		Adjust.trackEvent(eventToken);        	
        	} else {
            	JSONObject jsonParameters = args.getJSONObject(1);
            	Map<String, String> parameters = jsonObjectToMap(jsonParameters);
            	Adjust.trackEvent(eventToken, parameters);
        	}
        	return true;
        } else if (action.equals("trackRevenue")) {
        	double amountInCents = args.getDouble(0);
        	if (args.length() == 1) {
        		Adjust.trackRevenue(amountInCents);
        	} else if (args.length() == 2) {
        		String eventToken = args.getString(1);
        		Adjust.trackRevenue(amountInCents, eventToken);
        	} else {
        		String eventToken = args.getString(1);
            	JSONObject jsonParameters = args.getJSONObject(2);
            	Map<String, String> parameters = jsonObjectToMap(jsonParameters);
            	Adjust.trackRevenue(amountInCents, eventToken, parameters);
        	}
        	return true;
        } else if (action.equals("setFinishedTrackingCallback")) {
        	AdjustCordova.callbackId = callbackContext.getCallbackId();
        	AdjustCordova.cordovaWebView = this.webView;
        	Adjust.setOnFinishedListener(this);
        	
        	return true;
        } 
		return false;
	}

	@Override
	public void onFinishedTracking(ResponseData responseData) {
		// TODO Auto-generated method stub
    	PluginResult pluginResult = new PluginResult(Status.OK);
    	pluginResult.setKeepCallback(true);
    	
    	CallbackContext callbackResponseData = new CallbackContext(AdjustCordova.callbackId, AdjustCordova.cordovaWebView);
    	callbackResponseData.sendPluginResult(pluginResult);
	}

	private Map<String, String> jsonObjectToMap(JSONObject jsonObject) throws JSONException {
    	Map<String, String> map = new HashMap<String, String>(jsonObject.length());
    	@SuppressWarnings("unchecked")
		Iterator<String> jsonObjectIterator = jsonObject.keys();
    	while(jsonObjectIterator.hasNext()) {
    		String key = jsonObjectIterator.next();
    		map.put(key, jsonObject.getString(key));
    	}
    	return map;
	}
}
