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
    create: function(adjustConfig) {
        if (adjustConfig) {
            adjustConfig.sdkPrefix = this.getSdkPrefix();
        }

        if (adjustConfig.hasAttributionChangedListener()) {
            callCordovaCallback('setAttributionChangedCallback', adjustConfig.getAttributionChangedCallback());
        }
        if (adjustConfig.hasEventTrackingSucceededListener()) {
            callCordovaCallback('setEventTrackingSucceededCallback', adjustConfig.getEventTrackingSucceededCallback());
        }
        if (adjustConfig.hasEventTrackingFailedListener()) {
            callCordovaCallback('setEventTrackingFailedCallback', adjustConfig.getEventTrackingFailedCallback());
        }
        if (adjustConfig.hasSessionTrackingSucceededListener()) {
            callCordovaCallback('setSessionTrackingSucceededCallback', adjustConfig.getSessionTrackingSucceededCallback());
        }
        if (adjustConfig.hasSessionTrackingFailedListener()) {
            callCordovaCallback('setSessionTrackingFailedCallback', adjustConfig.getSessionTrackingFailedCallback());
        }
        if (adjustConfig.hasDeferredDeeplinkReceivedCallbackListener()) {
            callCordovaCallback('setDeferredDeeplinkReceivedCallback', adjustConfig.getDeferredDeeplinkReceivedCallback());
        }

        if (adjustConfig.hasSkanConversionDataUpdatedCallbackListener()) {
            callCordovaCallback('setSkanConversionDataUpdatedCallback', adjustConfig.getSkanConversionDataUpdatedCallback());
        }

        callCordovaStringify('create', adjustConfig);
    },

    trackEvent: function(adjustEvent) {
        callCordovaStringify('trackEvent', adjustEvent);
    },

    switchToOfflineMode: function() {
        callCordova('switchToOfflineMode');
    },

    switchBackToOnlineMode: function() {
        callCordova('switchBackToOnlineMode');
    },

    setPushTokenAsString: function(pushToken) {
        callCordova('setPushTokenAsString', pushToken);
    },

    processDeeplink: function(url) {
        callCordova('processDeeplink', url);
    },

    processAndResolveDeeplink: function(deeplink, callback) {
        callCordovaCallback('processAndResolveDeeplink', callback, deeplink);
    },

    getIdfa: function(callback) {
        callCordovaCallback('getIdfa', callback);
    },

    getIdfv: function(callback) {
        callCordovaCallback('getIdfv', callback);
    },

    getAdid: function(callback) {
        callCordovaCallback('getAdid', callback);
    },

    getAttribution: function(callback) {
        callCordovaCallback('getAttribution', callback);
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

    trackAdRevenue: function(adRevenue) {
        callCordovaStringify('trackAdRevenue', adRevenue);
    },

    trackAppStoreSubscription: function(subscription) {
        callCordovaStringify('trackAppStoreSubscription', subscription);
    },

    addGlobalCallbackParameter: function(key, value) {
        callCordova('addGlobalCallbackParameter', key, value);
    },

    removeGlobalCallbackParameter: function(key) {
        callCordova('removeGlobalCallbackParameter', key);
    },

    removeGlobalCallbackParameters: function() {
        callCordova('removeGlobalCallbackParameters');
    },

    addGlobalPartnerParameter: function(key, value) {
        callCordova('addGlobalPartnerParameter', key, value);
    },

    removeGlobalPartnerParameter: function(key) {
        callCordova('removeGlobalPartnerParameter', key);
    },

    removeGlobalPartnerParameters: function() {
        callCordova('removeGlobalPartnerParameters');
    },

    requestAppTrackingAuthorization: function(callback) {
        callCordovaCallback('requestAppTrackingAuthorization', callback);
    },

    getAppTrackingAuthorizationStatus: function(callback) {
        callCordovaCallback('getAppTrackingAuthorizationStatus', callback);
    },

    updateSkanConversionValueWithErrorCallback: function(callback, fineValue, coarseValue, lockWindow) {
        callCordovaCallback('updateSkanConversionValueWithErrorCallback', callback, fineValue, coarseValue, lockWindow);
    },

    trackThirdPartySharing: function(adjustThirdPartySharing) {
        callCordovaStringify('trackThirdPartySharing', adjustThirdPartySharing);
    },

    trackMeasurementConsent: function(measurementConsent) {
        callCordova('trackMeasurementConsent', measurementConsent);
    },

    getLastDeeplink: function(callback) {
        callCordovaCallback('getLastDeeplink', callback);
    },

    verifyAppStorePurchase: function(purchase, callback) {
        callCordovaStringifyCallback('verifyAppStorePurchase', purchase, callback);
    },

    verifyAndTrackAppStorePurchase: function(adjustEvent, callback) {
        callCordovaStringifyCallback('verifyAndTrackAppStorePurchase', adjustEvent, callback);
    },

    onPause: function(testParam) {
        if(testParam === null || testParam === undefined || testParam !== 'test') {
           return;
        }
        callCordova('onPause');
    },
    onResume: function(testParam) {
        if(testParam === null || testParam === undefined || testParam !== 'test') {
           return;
        }
        callCordova('onResume');
    },

    setReferrer: function(referrer) {
        callCordova('setReferrer', referrer);
    },

    getGoogleAdId: function(callback) {
        callCordovaCallback('getGoogleAdId', callback);
    },

    getAmazonAdId: function(callback) {
        callCordovaCallback('getAmazonAdId', callback);
    },

    trackPlayStoreSubscription: function(subscription) {
        callCordovaStringify('trackPlayStoreSubscription', subscription);
    },

    verifyPlayStorePurchase: function(purchase, callback) {
        callCordovaStringifyCallback('verifyPlayStorePurchase', purchase, callback);
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

function onPause() {
    callCordova('onPause');
}

function onResume() {
    callCordova('onResume');
}

document.addEventListener('resume', onResume, false);
document.addEventListener('pause', onPause, false);

module.exports = Adjust;