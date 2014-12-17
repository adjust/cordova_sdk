//
//  PackageBuilder.java
//  Adjust
//
//  Created by Christian Wellenbrock on 2013-06-25.
//  Copyright (c) 2013 adjust GmbH. All rights reserved.
//  See the file MIT-LICENSE for copying permission.
//

package com.adjust.sdk;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

public class PackageBuilder {

    private Context context;

    // general
    private String appToken;
    private String macSha1;
    private String macShortMd5;
    private String androidId;
    private String fbAttributionId;
    private String userAgent;
    private String clientSdk;
    private String uuid;
    private String environment;

    // sessions
    private int    sessionCount;
    private int    subsessionCount;
    private long   createdAt;
    private long   sessionLength;
    private long   timeSpent;
    private long   lastInterval;
    private String defaultTracker;
    private String referrer;

    // events
    private int                 eventCount;
    private String              eventToken;
    private double              amountInCents;
    private Map<String, String> callbackParameters;

    // reattributions
    private Map<String, String> deepLinkParameters;

    public PackageBuilder(Context context)
    {
        this.context = context;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public void setMacSha1(String macSha1) {
        this.macSha1 = macSha1;
    }

    public void setMacShortMd5(String macShortMd5) {
        this.macShortMd5 = macShortMd5;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setFbAttributionId(String fbAttributionId) {
        this.fbAttributionId = fbAttributionId;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setClientSdk(String clientSdk) {
        this.clientSdk = clientSdk;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }

    public void setSubsessionCount(int subsessionCount) {
        this.subsessionCount = subsessionCount;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public void setSessionLength(long sessionLength) {
        this.sessionLength = sessionLength;
    }

    public void setTimeSpent(long timeSpent) {
        this.timeSpent = timeSpent;
    }

    public void setLastInterval(long lastInterval) {
        this.lastInterval = lastInterval;
    }

    public void setDefaultTracker(String defaultTracker) {
        this.defaultTracker = defaultTracker;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public String getEventToken() {
        return eventToken;
    }

    public void setEventToken(String eventToken) {
        this.eventToken = eventToken;
    }

    public double getAmountInCents() {
        return amountInCents;
    }

    public void setAmountInCents(double amountInCents) {
        this.amountInCents = amountInCents;
    }

    public void setCallbackParameters(Map<String, String> callbackParameters) {
        this.callbackParameters = callbackParameters;
    }

    public void setDeepLinkParameters(Map<String, String> deepLinkParameters) {
        this.deepLinkParameters = deepLinkParameters;
    }

    public boolean isValidForEvent() {
        if (null == eventToken) {
            Logger logger = AdjustFactory.getLogger();
            logger.error("Missing Event Token");
            return false; // non revenue events need event tokens
        }
        return isEventTokenValid(); // and they must be valid
    }

    public boolean isValidForRevenue() {
        if (amountInCents < 0.0) {
            Logger logger = AdjustFactory.getLogger();
            logger.error("Invalid amount %f", amountInCents);
            return false;
        }
        if (eventToken == null) {
            return true; // revenue events don't need event tokens
        }
        return isEventTokenValid(); // but if they have one, it must be valid
    }

    public ActivityPackage buildSessionPackage() {
        Map<String, String> parameters = getDefaultParameters();
        addDuration(parameters, "last_interval", lastInterval);
        addString(parameters, "default_tracker", defaultTracker);
        addString(parameters, Constants.REFERRER, referrer);

        ActivityPackage sessionPackage = getDefaultActivityPackage();
        sessionPackage.setPath("/startup");
        sessionPackage.setActivityKind(ActivityKind.SESSION);
        sessionPackage.setSuffix("");
        sessionPackage.setParameters(parameters);

        return sessionPackage;
    }

    public ActivityPackage buildEventPackage() {
        Map<String, String> parameters = getDefaultParameters();
        injectEventParameters(parameters);

        ActivityPackage eventPackage = getDefaultActivityPackage();
        eventPackage.setPath("/event");
        eventPackage.setActivityKind(ActivityKind.EVENT);
        eventPackage.setSuffix(getEventSuffix());
        eventPackage.setParameters(parameters);

        return eventPackage;
    }

    public ActivityPackage buildRevenuePackage() {
        Map<String, String> parameters = getDefaultParameters();
        injectEventParameters(parameters);
        addString(parameters, "amount", getAmountString());

        ActivityPackage revenuePackage = getDefaultActivityPackage();
        revenuePackage.setPath("/revenue");
        revenuePackage.setActivityKind(ActivityKind.REVENUE);
        revenuePackage.setSuffix(getRevenueSuffix());
        revenuePackage.setParameters(parameters);

        return revenuePackage;
    }

    public ActivityPackage buildReattributionPackage() {
        Map<String, String> parameters = getDefaultParameters();
        addMapJson(parameters, "deeplink_parameters", deepLinkParameters);

        ActivityPackage reattributionPackage = getDefaultActivityPackage();
        reattributionPackage.setPath("/reattribute");
        reattributionPackage.setActivityKind(ActivityKind.REATTRIBUTION);
        reattributionPackage.setSuffix("");
        reattributionPackage.setParameters(parameters);

        return reattributionPackage;
    }

    private boolean isEventTokenValid() {
        if (6 != eventToken.length()) {
            Logger logger = AdjustFactory.getLogger();
            logger.error("Malformed Event Token '%s'", eventToken);
            return false;
        }
        return true;
    }

    private ActivityPackage getDefaultActivityPackage() {
        ActivityPackage activityPackage = new ActivityPackage();
        activityPackage.setUserAgent(userAgent);
        activityPackage.setClientSdk(clientSdk);
        return activityPackage;
    }

    private Map<String, String> getDefaultParameters() {
        Map<String, String> parameters = new HashMap<String, String>();

        // general
        addDate(parameters, "created_at", createdAt);
        addString(parameters, "app_token", appToken);
        addString(parameters, "mac_sha1", macSha1);
        addString(parameters, "mac_md5", macShortMd5);
        addString(parameters, "android_id", androidId);
        addString(parameters, "android_uuid", uuid);
        addString(parameters, "fb_id", fbAttributionId);
        addString(parameters, "environment", environment);
        String playAdId = Util.getPlayAdId(context);
        addString(parameters, "gps_adid", playAdId);
        Boolean isTrackingEnabled = Util.isPlayTrackingEnabled(context);
        addBoolean(parameters, "tracking_enabled", isTrackingEnabled);

        // session related (used for events as well)
        addInt(parameters, "session_count", sessionCount);
        addInt(parameters, "subsession_count", subsessionCount);
        addDuration(parameters, "session_length", sessionLength);
        addDuration(parameters, "time_spent", timeSpent);

        return parameters;
    }

    private void injectEventParameters(Map<String, String> parameters) {
        addInt(parameters, "event_count", eventCount);
        addString(parameters, "event_token", eventToken);
        addMapBase64(parameters, "params", callbackParameters);
    }

    private String getAmountString() {
        long amountInMillis = Math.round(10 * amountInCents);
        amountInCents = amountInMillis / 10.0; // now rounded to one decimal point
        return Long.toString(amountInMillis);
    }

    private String getEventSuffix() {
        return String.format(" '%s'", eventToken);
    }

    private String getRevenueSuffix() {
        if (eventToken != null) {
            return String.format(Locale.US, " (%.1f cent, '%s')", amountInCents, eventToken);
        } else {
            return String.format(Locale.US, " (%.1f cent)", amountInCents);
        }
    }

    private void addString(Map<String, String> parameters, String key, String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }

        parameters.put(key, value);
    }

    private void addInt(Map<String, String> parameters, String key, long value) {
        if (value < 0) {
            return;
        }

        String valueString = Long.toString(value);
        addString(parameters, key, valueString);
    }

    private void addDate(Map<String, String> parameters, String key, long value) {
        if (value < 0) {
            return;
        }

        String dateString = Util.dateFormat(value);
        addString(parameters, key, dateString);
    }

    private void addDuration(Map<String, String> parameters, String key, long durationInMilliSeconds) {
        if (durationInMilliSeconds < 0) {
            return;
        }

        long durationInSeconds = (durationInMilliSeconds + 500) / 1000;
        addInt(parameters, key, durationInSeconds);
    }

    private void addMapBase64(Map<String, String> parameters, String key, Map<String, String> map) {
        if (null == map) {
            return;
        }

        JSONObject jsonObject = new JSONObject(map);
        byte[] jsonBytes = jsonObject.toString().getBytes();
        String encodedMap = Base64.encodeToString(jsonBytes, Base64.NO_WRAP);

        addString(parameters, key, encodedMap);
    }

    private void addMapJson(Map<String, String> parameters, String key, Map<String, String> map) {
        if (null == map) {
            return;
        }

        JSONObject jsonObject = new JSONObject(map);
        String jsonString = jsonObject.toString();

        addString(parameters, key, jsonString);
    }

    private void addBoolean(Map<String, String> parameters, String key, Boolean value) {
        if (value == null) {
            return;
        }

        int intValue = value? 1 : 0;

        addInt(parameters, key, intValue);
    }
}
