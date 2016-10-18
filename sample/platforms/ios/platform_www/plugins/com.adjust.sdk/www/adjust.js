cordova.define("com.adjust.sdk.Adjust", function(require, exports, module) {
function callCordova (action) {
    var args = Array.prototype.slice.call(arguments, 1);

    cordova.exec(function callback(data) { },
        function errorHandler(err) { },
        'Adjust',
        action,
        args
    );
}

function callCordovaCallback(action, callback) {
    var args = Array.prototype.slice.call(arguments, 2);

    cordova.exec(callback,
        function errorHandler(err) { },
        'Adjust',
        action,
        args
    );
}

var Adjust = {
    create: function(adjustConfig) {
        if (adjustConfig.hasAttributionListener()) {
            callCordovaCallback('setAttributionCallback', adjustConfig.getAttributionCallback());
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

        if (adjustConfig.hasDeferredDeeplinkCallbackListener()) {
            callCordovaCallback('setDeferredDeeplinkCallback', adjustConfig.getDeferredDeeplinkCallback());
        }

        callCordova('create', adjustConfig);
    },

    trackEvent: function(adjustEvent) {
        callCordova('trackEvent', adjustEvent);
    },

    setOfflineMode: function(enabled) {
        callCordova('setOfflineMode', enabled);
    },

    appWillOpenUrl: function(url) {
        callCordova('appWillOpenUrl', url);
    },

    setEnabled: function(enabled) {
        callCordova('setEnabled', enabled);
    },

    setPushToken: function(pushToken) {
        callCordova('setPushToken', pushToken);
    },

    isEnabled: function(callback) {
        callCordovaCallback('isEnabled', callback);
    },

    getGoogleAdId: function(callback) {
        callCordovaCallback('getGoogleAdId', callback);
    },

    getIdfa: function(callback) {
        callCordovaCallback('getIdfa', callback);
    },

    addSessionCallbackParameter: function(key, value) {
        callCordova('addSessionCallbackParameter', key, value);
    },

    removeSessionCallbackParameter: function(key) {
        callCordova('removeSessionCallbackParameter', key);
    },

    resetSessionCallbackParameters: function() {
        callCordova('resetSessionCallbackParameters');
    },

    addSessionPartnerParameter: function(key, value) {
        callCordova('addSessionPartnerParameter', key, value);
    },

    removeSessionPartnerParameter: function(key) {
        callCordova('removeSessionPartnerParameter', key);
    },

    resetSessionPartnerParameters: function() {
        callCordova('resetSessionPartnerParameters');
    },

    sendFirstPackages: function() {
        callCordova('sendFirstPackages');
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

});
