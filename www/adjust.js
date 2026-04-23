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
        function(data) {
            // handle Cordova returning [null] for null values
            if (Array.isArray(data) && data.length === 1 && data[0] === null) {
                callback(null);
            } else {
                callback(data);
            }
        },
        function errorHandler(err) { },
        'Adjust',
        action,
        args
    );
}

function callCordovaStringifyCallback(action, data, callback) {
    var args = Array.prototype.slice.call(arguments, 1);

    cordova.exec(
        function(data) {
            // handle Cordova returning [null] for null values
            if (Array.isArray(data) && data.length === 1 && data[0] === null) {
                if (callback) {
                    callback(null);
                }
            } else {
                if (callback) {
                    callback(data);
                }
            }
        },
        function errorHandler(err) { },
        'Adjust',
        action,
        [JSON.stringify(args)]
    );
}

var Adjust = {
    // common
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
            callCordovaCallback('setSkanUpdatedCallback', adjustConfig.getSkanUpdatedCallback());
        }

        callCordovaStringify('initSdk', adjustConfig);
    },

    enable: function() {
        callCordova('enable');
    },

    disable: function() {
        callCordova('disable');
    },

    switchToOfflineMode: function() {
        callCordova('switchToOfflineMode');
    },

    switchBackToOnlineMode: function() {
        callCordova('switchBackToOnlineMode');
    },

    trackEvent: function(adjustEvent) {
        callCordovaStringify('trackEvent', adjustEvent);
    },

    trackAdRevenue: function(adjustAdrevenue) {
        callCordovaStringify('trackAdRevenue', adjustAdrevenue);
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

    gdprForgetMe: function() {
        callCordova('gdprForgetMe');
    },

    processDeeplink: function(adjustDeeplink) {
        callCordovaStringify('processDeeplink', adjustDeeplink);
    },

    processAndResolveDeeplink: function(adjustDeeplink, callback) {
        callCordovaStringifyCallback('processAndResolveDeeplink', adjustDeeplink, callback);
    },

    resolveLinkWithUrl: function(url, resolveUrlSuffixArray, callback) {
        if (typeof url !== 'string') {
            console.log("[Adjust] URL is not of type string");
            if (callback) {
                callback(null);
            }
            return;
        }
        var params = {
            url: url,
            resolveUrlSuffixArray: resolveUrlSuffixArray || []
        };
        callCordovaStringifyCallback('resolveLinkWithUrl', params, callback);
    },

    setPushToken: function(token) {
        if (typeof token !== 'string') {
            console.log("[Adjust] Push token is not of type string");
            return;
        }
        callCordova('setPushToken', token);
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

    endFirstSessionDelay: function() {
        callCordova('endFirstSessionDelay');
    },

    enableCoppaComplianceInDelay: function() {
        callCordova('enableCoppaComplianceInDelay');
    },

    disableCoppaComplianceInDelay: function() {
        callCordova('disableCoppaComplianceInDelay');
    },

    setExternalDeviceIdInDelay: function(externalDeviceId) {
        if (typeof externalDeviceId !== 'string') {
            console.log("[Adjust] External device ID is not of type string");
            return;
        }
        callCordova('setExternalDeviceIdInDelay', externalDeviceId);
    },

    isEnabled: function(callback) {
        callCordovaCallback('isEnabled', callback);
    },

    getAttribution: function(callback) {
        callCordovaCallback('getAttribution', callback);
    },

    getAttributionWithTimeout: function(timeoutInMilliseconds, callback) {
        if (!Number.isInteger(timeoutInMilliseconds)) {
            console.log("[Adjust] Timeout in milliseconds is not of type integer");
            if (callback) {
                callback(null);
            }
            return;
        }
        callCordovaStringifyCallback('getAttributionWithTimeout', {timeoutInMilliseconds: timeoutInMilliseconds}, function(attribution) {
            if (callback) {
                // convert empty object to null
                // when native SDK returns null attribution, we get an empty object {}
                if (attribution && typeof attribution === 'object') {
                    var hasAnyValue = false;
                    for (var key in attribution) {
                        if (attribution.hasOwnProperty(key)) {
                            var value = attribution[key];
                            // check if value is not empty (not null, undefined, or empty string)
                            if (value !== null && value !== undefined && value !== '') {
                                hasAnyValue = true;
                                break;
                            }
                        }
                    }
                    // if object has no meaningful values, return null
                    callback(hasAnyValue ? attribution : null);
                } else {
                    callback(attribution);
                }
            }
        });
    },

    getAdid: function(callback) {
        callCordovaCallback('getAdid', callback);
    },

    getAdidWithTimeout: function(timeoutInMilliseconds, callback) {
        if (!Number.isInteger(timeoutInMilliseconds)) {
            console.log("[Adjust] Timeout in milliseconds is not of type integer");
            if (callback) {
                callback(null);
            }
            return;
        }
        callCordovaStringifyCallback('getAdidWithTimeout', {timeoutInMilliseconds: timeoutInMilliseconds}, function(adid) {
            if (callback) {
                // convert null/empty string to null
                callback(adid === null || adid === undefined || adid === '' ? null : adid);
            }
        });
    },

    getLastDeeplink: function(callback) {
        callCordovaCallback('getLastDeeplink', callback);
    },

    getSdkVersion: function(callback) {
        var sdkPrefix = this.getSdkPrefix();
        callCordovaCallback('getSdkVersion', function(sdkVersion) {
            callback(sdkPrefix + "@" + sdkVersion);
        });
    },

    getSdkPrefix: function () {
        return 'cordova5.6.0';
    },

    // ios only

    trackAppStoreSubscription: function(adjustAppStoreSubscription) {
        callCordovaStringify('trackAppStoreSubscription', adjustAppStoreSubscription);
    },

    verifyAppStorePurchase: function(adjustAppStorePurchase, callback) {
        callCordovaStringifyCallback('verifyAppStorePurchase', adjustAppStorePurchase, callback);
    },

    verifyAndTrackAppStorePurchase: function(adjustEvent, callback) {
        callCordovaStringifyCallback('verifyAndTrackAppStorePurchase', adjustEvent, callback);
    },

    requestAppTrackingAuthorization: function(callback) {
        callCordovaCallback('requestAppTrackingAuthorization', callback);
    },

    getAppTrackingAuthorizationStatus: function(callback) {
        callCordovaCallback('getAppTrackingAuthorizationStatus', callback);
    },

    updateSkanConversionValue: function(conversionValue, coarseValue, lockWindow, callback) {
        if (!Number.isInteger(conversionValue) ||
            typeof coarseValue !== 'string' ||
            typeof lockWindow !== 'boolean') {
            console.log("[Adjust] SKAN parameters are not of a proper data types");
            return;
        }
        callCordovaCallback('updateSkanConversionValue', callback, conversionValue, coarseValue, lockWindow);
    },

    getIdfa: function(callback) {
        callCordovaCallback('getIdfa', callback);
    },

    getIdfv: function(callback) {
        callCordovaCallback('getIdfv', callback);
    },

    // android only

    trackPlayStoreSubscription: function(adjustPlayStoreSubscription) {
        callCordovaStringify('trackPlayStoreSubscription', adjustPlayStoreSubscription);
    },

    verifyPlayStorePurchase: function(adjustPlayStorePurchase, callback) {
        callCordovaStringifyCallback('verifyPlayStorePurchase', adjustPlayStorePurchase, callback);
    },

    verifyAndTrackPlayStorePurchase: function(adjustEvent, callback) {
        callCordovaStringifyCallback('verifyAndTrackPlayStorePurchase', adjustEvent, callback);
    },

    enablePlayStoreKidsComplianceInDelay: function() {
        callCordova('enablePlayStoreKidsComplianceInDelay');
    },

    disablePlayStoreKidsComplianceInDelay: function() {
        callCordova('disablePlayStoreKidsComplianceInDelay');
    },

    getGoogleAdId: function(callback) {
        callCordovaCallback('getGoogleAdId', callback);
    },

    getAmazonAdId: function(callback) {
        callCordovaCallback('getAmazonAdId', callback);
    },

    // testing only

    onResume: function(testParam) {
        if (testParam === null || testParam === undefined || testParam !== 'test') {
           return;
        }
        callCordova('onResume');
    },

    onPause: function(testParam) {
        if (testParam === null || testParam === undefined || testParam !== 'test') {
           return;
        }
        callCordova('onPause');
    },

    setTestOptions: function(testOptions) {
        callCordova('setTestOptions', testOptions);
    },

    teardown: function(testParam) {
        if (testParam === null || testParam === undefined || testParam !== 'test') {
           return;
        }
        callCordova('teardown');
    }
};

module.exports = Adjust;
