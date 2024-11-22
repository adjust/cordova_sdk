var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },

    // Bind Event Listeners
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },

    onDeviceReady: function() {
        app.receivedEvent('deviceready');

        var adjustConfig = new AdjustConfig("2fm9gkqubvpc", AdjustConfig.EnvironmentSandbox);
        adjustConfig.setLogLevel(AdjustConfig.LogLevelVerbose);

        adjustConfig.setAttributionCallback(function(attribution) {
            console.log("[AdjustExample]: Attribution callback received.");
            console.log("[AdjustExample]: Tracker token = " + attribution.trackerToken);
            console.log("[AdjustExample]: Tracker name = " + attribution.trackerName);
            console.log("[AdjustExample]: Network = " + attribution.network);
            console.log("[AdjustExample]: Campaign = " + attribution.campaign);
            console.log("[AdjustExample]: Adgroup = " + attribution.adgroup);
            console.log("[AdjustExample]: Creative = " + attribution.creative);
            console.log("[AdjustExample]: Click label = " + attribution.clickLabel);
            console.log("[AdjustExample]: Adid = " + attribution.adid);
        });

        adjustConfig.setEventTrackingSucceededCallback(function(eventSuccess) {
            console.log("[AdjustExample]: Event tracking succeeded callback received.");
            console.log("[AdjustExample]: Message: " + eventSuccess.message);
            console.log("[AdjustExample]: Timestamp: " + eventSuccess.timestamp);
            console.log("[AdjustExample]: Adid: " + eventSuccess.adid);
            console.log("[AdjustExample]: Event token: " + eventSuccess.eventToken);
            console.log("[AdjustExample]: Callback Id: " + eventSuccess.callbackId);
            console.log("[AdjustExample]: JSON response: " + eventSuccess.jsonResponse);
        });

        adjustConfig.setEventTrackingFailedCallback(function(eventFailed) {
            console.log("[AdjustExample]: Event tracking failed callback received.");
            console.log("[AdjustExample]: Message: " + eventFailed.message);
            console.log("[AdjustExample]: Timestamp: " + eventFailed.timestamp);
            console.log("[AdjustExample]: Adid: " + eventFailed.adid);
            console.log("[AdjustExample]: Event token: " + eventFailed.eventToken);
            console.log("[AdjustExample]: Will retry: " + eventFailed.willRetry);
            console.log("[AdjustExample]: Callback Id: " + eventFailed.callbackId);
            console.log("[AdjustExample]: JSON response: " + eventFailed.jsonResponse);
        });

        adjustConfig.setSessionTrackingSucceededCallback(function(sessionSuccess) {
            console.log("[AdjustExample]: Session tracking succeeded callback received.");
            console.log("[AdjustExample]: Message: " + sessionSuccess.message);
            console.log("[AdjustExample]: Timestamp: " + sessionSuccess.timestamp);
            console.log("[AdjustExample]: Adid: " + sessionSuccess.adid);
            console.log("[AdjustExample]: JSON response: " + sessionSuccess.jsonResponse);
        });

        adjustConfig.setSessionTrackingFailedCallback(function(sessionFailed) {
            console.log("[AdjustExample]: Session tracking failed callback received.");
            console.log("[AdjustExample]: Message: " + sessionFailed.message);
            console.log("[AdjustExample]: Timestamp: " + sessionFailed.timestamp);
            console.log("[AdjustExample]: Adid: " + sessionFailed.adid);
            console.log("[AdjustExample]: Will retry: " + sessionFailed.willRetry);
            console.log("[AdjustExample]: JSON response: " + sessionFailed.jsonResponse);
        });

        adjustConfig.setDeferredDeeplinkCallback(function(uri) {
            console.log("[AdjustExample]: Deferred Deeplink Callback received.");
            console.log("[AdjustExample]: URL: " + uri);
        });

        adjustConfig.setSkanUpdatedCallback(function(skanData) {
            console.log("[AdjustExample]: SKAdNetwork conversion data updated!");
            console.log("[AdjustExample]: Conversion Value = " + skanData.conversionValue);
            console.log("[AdjustExample]: Coarse Value = " + skanData.coarseValue);
            console.log("[AdjustExample]: Lock Window = " + skanData.lockWindow);
            console.log("[AdjustExample]: Error = " + skanData.error);
        });

        Adjust.addGlobalCallbackParameter("dummy_foo", "dummy_bar");
        Adjust.addGlobalCallbackParameter("dummy_foo_foo", "dummy_bar");

        Adjust.addGlobalPartnerParameter("dummy_foo", "dummy_bar");
        Adjust.addGlobalPartnerParameter("dummy_foo_foo", "dummy_bar");

        Adjust.removeGlobalCallbackParameter("dummy_foo");
        Adjust.removeGlobalPartnerParameter("dummy_foo");

        // Adjust.removeGlobalCallbackParameters();
        // Adjust.removeGlobalPartnerParameters();

        Adjust.initSdk(adjustConfig);
    },

    // Update DOM on a Received Event
    receivedEvent: function(id) {
        console.log('[AdjustExample]: Received Event: ' + id);

        var btnTrackSimpleEvent = document.getElementById("btnTrackSimpleEvent");
        var btnTrackSimpleEventWithCallbackId = document.getElementById("btnTrackSimpleEventWithCallbackId");
        var btnTrackRevenueEvent = document.getElementById("btnTrackRevenueEvent");
        var btnTrackCallbackEvent = document.getElementById("btnTrackCallbackEvent");
        var btnTrackPartnerEvent = document.getElementById("btnTrackPartnerEvent");
        var btnEnableDisableOfflineMode = document.getElementById("btnEnableDisableOfflineMode");
        var btnEnableDisableSdk = document.getElementById("btnEnableDisableSdk");
        var btnIsSdkEnabled = document.getElementById("btnIsSdkEnabled");
        var btnGetSdkVersion = document.getElementById("btnGetSdkVersion");

        btnTrackSimpleEvent.addEventListener('click', function() {
            var adjustEvent = new AdjustEvent("g3mfiw");
            Adjust.trackEvent(adjustEvent);
        }, false);

        btnTrackRevenueEvent.addEventListener('click',function() { 
            var adjustEvent = new AdjustEvent("a4fd35");
            adjustEvent.setRevenue(0.01, "USD");
            adjustEvent.setTransactionId("dummy_id");
            Adjust.trackEvent(adjustEvent);
        }, false);

        btnTrackCallbackEvent.addEventListener('click',function() {
            var adjustEvent = new AdjustEvent("34vgg9");
            adjustEvent.addCallbackParameter("key", "stuff");
            adjustEvent.addCallbackParameter("x", "y");
            adjustEvent.addCallbackParameter("key", "lock");
            Adjust.trackEvent(adjustEvent);
        }, false);

        btnTrackPartnerEvent.addEventListener('click',function() {
            var adjustEvent = new AdjustEvent("w788qs");
            adjustEvent.addPartnerParameter("foo", "bar");
            adjustEvent.addPartnerParameter("x", "y");
            adjustEvent.addPartnerParameter("foo", "foot");
            adjustEvent.addPartnerParameter("x", "z");
            Adjust.trackEvent(adjustEvent);
        }, false);

        btnEnableOfflineMode.addEventListener('click', function() {
            Adjust.switchToOfflineMode();
        }, false);

        btnDisableOfflineMode.addEventListener('click', function() {
            Adjust.switchBackToOnlineMode();
        }, false);

        btnEnableSdk.addEventListener('click', function() {
            Adjust.enable();
        }, false);

        btnDisableSdk.addEventListener('click', function() {
            Adjust.disable();
        }, false);

        btnIsSdkEnabled.addEventListener('click', function() {
            Adjust.isEnabled(function(isEnabled) {
                if (isEnabled) {
                    navigator.notification.alert('Yes, it is enabled.', null, 'Is SDK Enabled?', 'OK');
                } else {
                    navigator.notification.alert('No, it is not enabled.', null, 'Is SDK Enabled?', 'OK');
                }
            });
        }, false);

        btnGetIds.addEventListener('click', function() {
            Adjust.getIdfa(function(idfa) {
                console.log("[AdjustExample]: IDFA = " + idfa);
            });

            Adjust.getIdfv(function(idfv) {
                console.log("[AdjustExample]: IDFV = " + idfv);
            });

            Adjust.getGoogleAdId(function(gpsAdId) {
                console.log("[AdjustExample]: Google Ad Id = " + gpsAdId);
            });

            Adjust.getAmazonAdId(function(amazonAdId) {
                console.log("[AdjustExample]: Amazon Ad Id = " + amazonAdId);
            });

            Adjust.getAdid(function(adid) {
                console.log("[AdjustExample]: Adjust Id = " + adid);
            });

            Adjust.getAttribution(function(attribution) {
                console.log("[AdjustExample]: Tracker token = " + attribution.trackerToken);
                console.log("[AdjustExample]: Tracker name = " + attribution.trackerName);
                console.log("[AdjustExample]: Network = " + attribution.network);
                console.log("[AdjustExample]: Campaign = " + attribution.campaign);
                console.log("[AdjustExample]: Adgroup = " + attribution.adgroup);
                console.log("[AdjustExample]: Creative = " + attribution.creative);
                console.log("[AdjustExample]: Click label = " + attribution.clickLabel);
                console.log("[AdjustExample]: Cost Type = " + attribution.costType);
                console.log("[AdjustExample]: Cost Amount = " + attribution.costAmount);
                console.log("[AdjustExample]: Cost Currency = " + attribution.costCurrency);
            });

            Adjust.requestAppTrackingAuthorization(function(status) {
                console.log("[AdjustExample]: ATT status = " + status);
            });

            Adjust.getAppTrackingAuthorizationStatus(function(status) {
                console.log("[AdjustExample]: ATT status = " + status);
            });
        }, false);

        btnGetSdkVersion.addEventListener('click', function() {
            Adjust.getSdkVersion(function(sdkVersion) {
                navigator.notification.alert(sdkVersion, null, 'SDK Version', 'OK');
            });
        }, false);
    }
};

app.initialize();
