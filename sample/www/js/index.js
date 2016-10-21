var isOffline = false;

function handleOpenURL(url) {
    setTimeout(function() {
        //navigator.notification.alert('received url: ' + url, null, 'Notification', 'OK');
        Adjust.appWillOpenUrl(url);
    }, 0);
}

var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },

    // Bind Event Listeners
    //
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //

    onDeviceReady: function() {
        app.receivedEvent('deviceready');

        //Register for universal links
        if(device.platform == 'iOS') {
            universalLinks.subscribe('adjustDeepLinking', app.didLaunchAppFromLink);
        }

        var adjustConfig = new AdjustConfig("rb4g27fje5ej", AdjustConfig.EnvironmentSandbox);

        adjustConfig.setAttributionCallbackListener(function(attribution) {
            console.log(">>> attribution callback received");

            console.log("Tracker token = " + attribution.trackerToken);
            console.log("Tracker name = " + attribution.trackerName);
            console.log("Network = " + attribution.network);
            console.log("Campaign = " + attribution.campaign);
            console.log("Adgroup = " + attribution.adgroup);
            console.log("Creative = " + attribution.creative);
            console.log("Click label = " + attribution.clickLabel);
        });

        adjustConfig.setEventTrackingSucceededCallbackListener(function(eventSuccess) {
            console.log(">>> event tracking succeeded callback received");

            console.log("message: " + eventSuccess.message);
            console.log("timestamp: " + eventSuccess.timeStamp);
            console.log("adid: " + eventSuccess.adid);
            console.log("eventToken: " + eventSuccess.eventToken);
            console.log("json response: " + eventSuccess.jsonResponse);
        });

        adjustConfig.setEventTrackingFailedCallbackListener(function(eventFailed) {
            console.log(">>> event tracking failed callback received");

            console.log("message: " + eventFailed.message);
            console.log("timestamp: " + eventFailed.timeStamp);
            console.log("adid: " + eventFailed.adid);
            console.log("eventToken: " + eventFailed.eventToken);
            console.log("will retry: " + eventFailed.willRetry);
            console.log("json response: " + eventFailed.jsonResponse);
        });

        adjustConfig.setSessionTrackingSucceededCallbackListener(function(sessionSuccess) {
            console.log(">>> session tracking succeeded callback received");

            console.log("message: " + sessionSuccess.message);
            console.log("timestamp: " + sessionSuccess.timeStamp);
            console.log("adid: " + sessionSuccess.adid);
            console.log("json response: " + sessionSuccess.jsonResponse);
        });

        adjustConfig.setSessionTrackingFailedCallbackListener(function(sessionFailed) {
            console.log(">>> session tracking failed callback received");

            console.log("message: " + sessionFailed.message);
            console.log("timestamp: " + sessionFailed.timeStamp);
            console.log("adid: " + sessionFailed.adid);
            console.log("will retry: " + sessionFailed.willRetry);
            console.log("json response: " + sessionFailed.jsonResponse);
        });

        adjustConfig.setDeferredDeeplinkCallbackListener(function(uri) {
            console.log(">>> Deferred Deeplink Callback received");

            console.log("uri: " + uri);
        });

        adjustConfig.setShouldLaunchDeeplink(true);
        //adjustConfig.setEventBufferingEnabled(true);

        Adjust.addSessionCallbackParameter("dummy_foo", "dummy_bar");
        Adjust.addSessionCallbackParameter("dummy_foo_foo", "dummy_bar");

        Adjust.addSessionPartnerParameter("dummy_foo", "dummy_bar");
        Adjust.addSessionPartnerParameter("dummy_foo_foo", "dummy_bar");

        Adjust.removeSessionCallbackParameter("dummy_foo");
        Adjust.removeSessionPartnerParameter("dummy_foo");

        //Adjust.resetSessionCallbackParameters();
        //Adjust.resetSessionPartnerParameters();

        adjustConfig.setLogLevel(AdjustConfig.LogLevelVerbose);

        adjustConfig.setDelayStart(3.0);
        adjustConfig.setUserAgent("little_bunny_foo_foo");

        Adjust.create(adjustConfig);

        Adjust.setPushToken("bunny_foo_foo");
        //Adjust.sendFirstPackages();
    },

    didLaunchAppFromLink: function(eventData) {
        //navigator.notification.alert('received url from universal link: ' + eventData.url, null, 'Notification', 'OK');
        Adjust.appWillOpenUrl(eventData.url);
    },

    // Update DOM on a Received Event
    receivedEvent: function(id) {
        console.log('Received Event: ' + id);

        var btnTrackSimpleEvent = document.getElementById("btnTrackSimpleEvent"); 
        var btnTrackRevenueEvent = document.getElementById("btnTrackRevenueEvent");
        var btnTrackCallbackEvent = document.getElementById("btnTrackCallbackEvent");
        var btnTrackPartnerEvent = document.getElementById("btnTrackPartnerEvent");
        var btnEnableDisableOfflineMode = document.getElementById("btnEnableDisableOfflineMode");
        var btnEnableDisableSdk = document.getElementById("btnEnableDisableSdk");
        var btnIsSdkEnabled = document.getElementById("btnIsSdkEnabled");

        btnTrackSimpleEvent.addEventListener('click', function() {
            console.log("trackSimpleEvent()");
            Adjust.isEnabled(function(isEnabled) {
                if (isEnabled) {
                    var adjustEvent = new AdjustEvent("uqg17r");
                    Adjust.trackEvent(adjustEvent);
                } else {
                    navigator.notification.alert('SDK is disabled.', null, 'Notification', 'OK');
                }
            });
        }, false);

        btnTrackRevenueEvent.addEventListener('click',function() { 
            Adjust.isEnabled(function(isEnabled) {
                if (isEnabled) {
                    var adjustEvent = new AdjustEvent("71iltz");
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
                    var adjustEvent = new AdjustEvent("1ziip1");
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
                    var adjustEvent = new AdjustEvent("9s4lqn");
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
                    if(!isOffline) {
                        isOffline = true;
                        Adjust.setOfflineMode(isOffline);
                        navigator.notification.alert('SDK is offline', null, 'SDK', 'OK');
                    } else {
                        isOffline = false;
                        Adjust.setOfflineMode(isOffline);
                        navigator.notification.alert('SDK is online', null, 'SDK', 'OK');
                    }
                } else {
                    navigator.notification.alert('SDK is disabled.', null, 'Notification', 'OK');
                }
            });
        }, false);

        btnEnableDisableSdk.addEventListener('click',function() {
            Adjust.isEnabled(function(isEnabled) {
                if (isEnabled) {
                    Adjust.setEnabled(false);
                    navigator.notification.alert('SDK is disabled', null, 'SDK', 'OK');
                } else {
                    Adjust.setEnabled(true);
                    Adjust.setOfflineMode(false);
                    navigator.notification.alert('SDK is enabled', null, 'SDK', 'OK');
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
