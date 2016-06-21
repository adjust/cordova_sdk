/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

 function handleOpenURL(url) {
   setTimeout(function () {
       Adjust.appWillOpenUrl(url);
   }, 300);
};

var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function() {
        app.receivedEvent('deviceready');

        if (device.platform == "iOS") {
            universalLinks.subscribe('adjustDeepLinking', app.didLaunchAppFromLink);
        }

        var adjustConfig = new AdjustConfig("{YourAppToken}", AdjustConfig.EnvironmentSandbox);
        adjustConfig.setLogLevel(AdjustConfig.LogLevelVerbose);
        adjustConfig.setCallbackListener(function(attribution) {
            console.log("Tracker token = " + attribution.trackerToken);
            console.log("Tracker name = " + attribution.trackerName);
            console.log("Network = " + attribution.network);
            console.log("Campaign = " + attribution.campaign);
            console.log("Adgroup = " + attribution.adgroup);
            console.log("Creative = " + attribution.creative);
            console.log("Click label = " + attribution.clickLabel);
        });

        Adjust.create(adjustConfig);
    },

    didLaunchAppFromLink: function(eventData) {
        Adjust.appWillOpenUrl(eventData.url);
    },

    // Update DOM on a Received Event
    receivedEvent: function(id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);

        var btnTrackSimpleEvent = document.getElementById("btnTrackSimpleEvent");
        var btnTrackRevenueEvent = document.getElementById("btnTrackRevenueEvent");
        var btnTrackCallbackEvent = document.getElementById("btnTrackCallbackEvent");
        var btnTrackPartnerEvent = document.getElementById("btnTrackPartnerEvent");
        var btnEnableDisableOfflineMode = document.getElementById("btnEnableDisableOfflineMode");
        var btnEnableDisableSdk = document.getElementById("btnEnableDisableSdk");
        var btnIsSdkEnabled = document.getElementById("btnIsSdkEnabled");

        btnTrackSimpleEvent.addEventListener('click',function() { 
            Adjust.isEnabled(function(isEnabled) {
                if (isEnabled) {
                    var adjustEvent = new AdjustEvent("{YourEventToken}");
                    Adjust.trackEvent(adjustEvent);
                } else {
                    navigator.notification.alert('SDK is disabled.', null, 'Notification', 'OK');
                }
            });
        }, false);

        btnTrackRevenueEvent.addEventListener('click',function() { 
            Adjust.isEnabled(function(isEnabled) {
                if (isEnabled) {
                    var adjustEvent = new AdjustEvent("{YourEventToken}");
                    adjustEvent.setRevenue(0.01, "EUR");
                    Adjust.trackEvent(adjustEvent);
                } else {
                    navigator.notification.alert('SDK is disabled.', null, 'Notification', 'OK');
                }
            });
        }, false);

        btnTrackCallbackEvent.addEventListener('click',function() {
            Adjust.isEnabled(function(isEnabled) {
                if (isEnabled) {
                    var adjustEvent = new AdjustEvent("{YourEventToken}");
                    adjustEvent.addCallbackParameter("key", "value");
                    adjustEvent.addCallbackParameter("x", "y");
                    adjustEvent.addCallbackParameter("key", "lock");
                    Adjust.trackEvent(adjustEvent);
                } else {
                    navigator.notification.alert('SDK is disabled.', null, 'Notification', 'OK');
                }
            });
        }, false);

        btnTrackPartnerEvent.addEventListener('click',function() {
            Adjust.isEnabled(function(isEnabled) {
                if (isEnabled) {
                    var adjustEvent = new AdjustEvent("{YourEventToken}");
                    adjustEvent.addPartnerParameter("foo", "bar");
                    adjustEvent.addPartnerParameter("x", "y");
                    adjustEvent.addPartnerParameter("foo", "foot");
                    adjustEvent.addPartnerParameter("x", "z");
                    Adjust.trackEvent(adjustEvent);
                } else {
                    navigator.notification.alert('SDK is disabled.', null, 'Notification', 'OK');
                }
            });
        }, false);

        btnEnableDisableOfflineMode.addEventListener('click',function() {
            Adjust.isEnabled(function(isEnabled) {
                if (isEnabled) {
                    Adjust.setOfflineMode(true);
                } else {
                    navigator.notification.alert('SDK is disabled.', null, 'Notification', 'OK');
                }
            });
        }, false);

        btnEnableDisableSdk.addEventListener('click',function() {
            Adjust.isEnabled(function(isEnabled) {
                if (isEnabled) {
                    Adjust.setEnabled(false);
                } else {
                    Adjust.setEnabled(true);
                    Adjust.setOfflineMode(false);
                }
            });
        }, false);

        btnIsSdkEnabled.addEventListener('click',function() {
            Adjust.isEnabled(function(isEnabled) {
                if (isEnabled) {
                    navigator.notification.alert('Yes, it is enabled.', null, 'Is SDK Enabled?', 'OK');
                } else {
                    navigator.notification.alert('No, it is not enabled.', null, 'Is SDK Enabled?', 'OK');
                }
            });
        }, false);
    }
};

app.initialize();