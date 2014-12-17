package com.adjust.sdk;

import org.json.JSONObject;

public interface IPackageHandler {
    public void addPackage(ActivityPackage pack);

    public void sendFirstPackage();

    public void sendNextPackage();

    public void closeFirstPackage();

    public void pauseSending();

    public void resumeSending();

    public String getFailureMessage();

    public boolean dropsOfflineActivities();

    public void finishedTrackingActivity(ActivityPackage activityPackage, ResponseData responseData, JSONObject jsonResponse);
}
