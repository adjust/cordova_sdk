function callCordova (action) {
    var args = Array.prototype.slice.call(arguments, 1);

    cordova.exec(function callback(data) { },
        function errorHandler(err) { },
        'Adjust',
        action,
        args
    );
}

function callCordovaCallback (action, callback) {
    var args = Array.prototype.slice.call(arguments, 2);

    cordova.exec(callback,
        function errorHandler(err) { },
        'Adjust',
        action,
        args
    );
}

var Adjust = {
    create: function (adjustConfig) {
        if (adjustConfig.hasAttributionListener()) {
            callCordovaCallback('setAttributionCallback', 
                adjustConfig.getAttributionCallback());
        }

        if (adjustConfig.hasEventTrackingSuccessfulListener()) {
            callCordovaCallback('setEventTrackingSuccessfulCallback', 
                adjustConfig.getEventTrackingSuccessfulCallback());
        }

        if (adjustConfig.hasEventTrackingFailedListener()) {
            callCordovaCallback('setEventTrackingFailedlCallback', 
                adjustConfig.getEventTrackingFailedCallback());
        }

        if (adjustConfig.hasSessionTrackingSuccessfulListener()) {
            callCordovaCallback('setSessionTrackingSuccessfulCallback', 
                adjustConfig.getSessionTrackingSuccessfulCallback());
        }

        if (adjustConfig.hasSessionTrackingFailedListener()) {
            callCordovaCallback('setSessionTrackingFailedlCallback', 
                adjustConfig.getSessionTrackingFailedCallback());
        }

        if (adjustConfig.hasDeeplinkCallbackListener()) {
            callCordovaCallback('setDeeplinklCallback', 
                adjustConfig.getDeeplinkCallback());
        }

        callCordova('create', adjustConfig);
    },

    trackEvent: function (adjustEvent) {
        callCordova('trackEvent', adjustEvent);
    },

    setOfflineMode: function(enabled) {
        callCordova('setOfflineMode', enabled);
    },

    appWillOpenUrl: function(url) {
        callCordova('appWillOpenUrl', url);
    },

    setEnabled: function (enabled) {
        callCordova('setEnabled', enabled);
    },

    setPushToken: function (pushToken) {
        callCordova('setPushToken', pushToken);
    },

    isEnabled: function (callback) {
        callCordovaCallback('isEnabled', callback);
    },

    getGoogleAdId: function (callback) {
        callCordovaCallback('getGoogleAdId', callback);
    },

    getIdfa: function (callback) {
        callCordovaCallback('getIdfa', callback);
    }
};

function onPause () {
    callCordova('onPause');
}

function onResume () {
    callCordova('onResume');
}

document.addEventListener('resume', onResume, false);
document.addEventListener('pause', onPause, false);

module.exports = Adjust;
