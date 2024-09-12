function callCordova(action) {
    var args = Array.prototype.slice.call(arguments, 1);

    cordova.exec(
        function callback(data) { },
        function errorHandler(err) { },
        'Adjust',
        action,
        args
    );
}

function callCordovaStringify(action) {
    console.log(action);
    var args = Array.prototype.slice.call(arguments, 1);

    cordova.exec(
        function callback(data) { },
        function errorHandler(err) { },
        'Adjust',
        action,
        [JSON.stringify(args)]
    );
}

function callCordovaCallback(action, callback) {
    var args = Array.prototype.slice.call(arguments, 2);

    cordova.exec(
        callback,
        function errorHandler(err) { },
        'Adjust',
        action,
        args
    );
}

function callCordovaStringifyCallback(action, data, callback) {
    var args = Array.prototype.slice.call(arguments, 1);

    cordova.exec(
        callback,
        function errorHandler(err) { },
        'Adjust',
        action,
        [JSON.stringify(args)]
    );
}

var Adjust = {
    initSdk: function(adjustConfig) {
        if (adjustConfig) {
            adjustConfig.sdkPrefix = this.getSdkPrefix();
        }
        if (adjustConfig.hasAttributionCallback()) {
            callCordovaCallback('setAttributionCallback', adjustConfig.getAttributionCallback());
        }
        if (adjustConfig.hasEventTrackingSucceededCallback()) {
            callCordovaCallback('setEventTrackingSucceededCallback', adjustConfig.getEventTrackingSucceededCallback());
        }
        if (adjustConfig.hasEventTrackingFailedCallback()) {
            callCordovaCallback('setEventTrackingFailedCallback', adjustConfig.getEventTrackingFailedCallback());
        }
        if (adjustConfig.hasSessionTrackingSucceededCallback()) {
            callCordovaCallback('setSessionTrackingSucceededCallback', adjustConfig.getSessionTrackingSucceededCallback());
        }
        if (adjustConfig.hasSessionTrackingFailedCallback()) {
            callCordovaCallback('setSessionTrackingFailedCallback', adjustConfig.getSessionTrackingFailedCallback());
        }
        if (adjustConfig.hasDeferredDeeplinkCallback()) {
            callCordovaCallback('setDeferredDeeplinkCallback', adjustConfig.getDeferredDeeplinkCallback());
        }
        if (adjustConfig.hasSkanUpdatedCallback()) {
            if (device.platform === 'iOS') {
                callCordovaCallback('setSkanUpdatedCallback', adjustConfig.getSkanUpdatedCallback());
            }
        }

        callCordovaStringify('initSdk', adjustConfig);
    },

    setPushToken: function(token) {
        if (typeof token !== 'string') {
            console.log("[Adjust] Push token is not of type string");
            return;
        }
        callCordova('setPushToken', token);
    },

    getAttribution: function(callback) {
        callCordovaCallback('getAttribution', callback);
    },

    getAdid: function(callback) {
        callCordovaCallback('getAdid', callback);
    },

    getGoogleAdId: function(callback) {
        if (device.platform === 'Android') {
            callCordovaCallback('getGoogleAdId', callback);
        }
    },

    getAmazonAdId: function(callback) {
        if (device.platform === 'Android') {
            callCordovaCallback('getAmazonAdId', callback);
        }
    },

    getSdkVersion: function(callback) {
        var sdkPrefix = this.getSdkPrefix();
        callCordovaCallback('getSdkVersion', function(sdkVersion) {
            callback(sdkPrefix + "@" + sdkVersion);
        });
    },

    getSdkPrefix: function () {
        return 'cordova5.0.0';
    },

    addGlobalCallbackParameter: function(key, value) {
        if (typeof key !== 'string' || typeof value !== 'string') {
            console.log("[Adjust] Global callback parameter key or value is not of type string");
            return;
        }
        callCordova('addGlobalCallbackParameter', key, value);
    },

    removeGlobalCallbackParameter: function(key) {
        if (typeof key !== 'string') {
            console.log("[Adjust] Global callback parameter key is not of type string");
            return;
        }
        callCordova('removeGlobalCallbackParameter', key);
    },

    removeGlobalCallbackParameters: function() {
        callCordova('removeGlobalCallbackParameters');
    },

    addGlobalPartnerParameter: function(key, value) {
        if (typeof key !== 'string' || typeof value !== 'string') {
            console.log("[Adjust] Global partner parameter key or value is not of type string");
            return;
        }
        callCordova('addGlobalPartnerParameter', key, value);
    },

    removeGlobalPartnerParameter: function(key) {
        if (typeof key !== 'string') {
            console.log("[Adjust] Global partner parameter key is not of type string");
            return;
        }
        callCordova('removeGlobalPartnerParameter', key);
    },

    removeGlobalPartnerParameters: function() {
        callCordova('removeGlobalPartnerParameters');
    },

    switchToOfflineMode: function() {
        callCordova('switchToOfflineMode');
    },

    switchBackToOnlineMode: function() {
        callCordova('switchBackToOnlineMode');
    },

    enable: function() {
        callCordova('enable');
    },

    disable: function() {
        callCordova('disable');
    },

    isEnabled: function(callback) {
        callCordovaCallback('isEnabled', callback);
    },

    gdprForgetMe: function() {
        callCordova('gdprForgetMe');
    },

    onPause: function(testParam) {
        if (testParam === null || testParam === undefined || testParam !== 'test') {
           return;
        }
        callCordova('onPause');
    },
    onResume: function(testParam) {
        if (testParam === null || testParam === undefined || testParam !== 'test') {
           return;
        }
        callCordova('onResume');
    },

    trackEvent: function(adjustEvent) {
        callCordovaStringify('trackEvent', adjustEvent);
    },

    trackAdRevenue: function(adjustAdrevenue) {
        callCordovaStringify('trackAdRevenue', adjustAdrevenue);
    },

    trackPlayStoreSubscription: function(adjustPlayStoreSubscription) {
        if (device.platform === 'Android') {
            callCordovaStringify('trackPlayStoreSubscription', adjustPlayStoreSubscription);
        }
    },

    verifyPlayStorePurchase: function(adjustPlayStorePurchase, callback) {
        if (device.platform === 'Android') {
            callCordovaStringifyCallback('verifyPlayStorePurchase', adjustPlayStorePurchase, callback);
        }
    },

    verifyAndTrackPlayStorePurchase: function(adjustEvent, callback) {
        if (device.platform === 'Android') {
            callCordovaStringifyCallback('verifyAndTrackPlayStorePurchase', adjustEvent, callback);
        }
    },

    trackThirdPartySharing: function(adjustThirdPartySharing) {
        callCordovaStringify('trackThirdPartySharing', adjustThirdPartySharing);
    },

    trackMeasurementConsent: function(measurementConsent) {
        if (typeof measurementConsent !== 'boolean') {
            console.log("[Adjust] Measurement consent is not of type boolean");
            return;
        }
        callCordova('trackMeasurementConsent', measurementConsent);
    },

    processDeeplink: function(adjustDeeplink) {
        callCordovaStringify('processDeeplink', adjustDeeplink);
    },

    processAndResolveDeeplink: function(adjustDeeplink, callback) {
        callCordovaStringifyCallback('processAndResolveDeeplink', adjustDeeplink, callback);
    },

    getLastDeeplink: function(callback) {
        callCordovaCallback('getLastDeeplink', callback);
    },

   trackAppStoreSubscription: function(adjustAppStoreSubscription) {
        if (device.platform === 'iOS') {
            callCordovaStringify('trackAppStoreSubscription', adjustAppStoreSubscription);
        }
    },

    verifyAppStorePurchase: function(adjustAppStorePurchase, callback) {
        if (device.platform === 'iOS') {
            callCordovaStringifyCallback('verifyAppStorePurchase', adjustAppStorePurchase, callback);
        }
    },

    verifyAndTrackAppStorePurchase: function(adjustEvent, callback) {
        if (device.platform === 'iOS') {
            callCordovaStringifyCallback('verifyAndTrackAppStorePurchase', adjustEvent, callback);
        }
    },

   requestAppTrackingAuthorization: function(callback) {
        if (device.platform === 'iOS') {
            callCordovaCallback('requestAppTrackingAuthorization', callback);
        }
    },

    getAppTrackingAuthorizationStatus: function(callback) {
        if (device.platform === 'iOS') {
            callCordovaCallback('getAppTrackingAuthorizationStatus', callback);
        }
    },

    updateSkanConversionValue: function(conversionValue, coarseValue, lockWindow, callback) {
        if (device.platform === 'iOS') {
            if (!Number.isInteger(conversionValue) ||
                typeof coarseValue !== 'string' ||
                typeof lockWindow !== 'boolean') {
                console.log("[Adjust] SKAN parameters are not of a proper data types");
                return;
            }
            callCordovaCallback('updateSkanConversionValue', callback, conversionValue, coarseValue, lockWindow);
        }
    },

    getIdfa: function(callback) {
        if (device.platform === 'iOS') {
            callCordovaCallback('getIdfa', callback);
        }
    },

    getIdfv: function(callback) {
        if (device.platform === 'iOS') {
            callCordovaCallback('getIdfv', callback);
        }
    },

    setTestOptions: function(testOptions) {
        callCordova('setTestOptions', testOptions);
    },

    teardown: function(testParam) {
        if(testParam === null || testParam === undefined || testParam !== 'test') {
           return;
        }
        callCordova('teardown');
    }
};

module.exports = Adjust;