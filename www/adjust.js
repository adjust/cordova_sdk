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
    EnvironmentSandbox      : "sandbox",
    EnvironmentProduction   : "production",

    LogLevelVerbose         : "VERBOSE",
    LogLevelDebug           : "DEBUG",
    LogLevelInfo            : "INFO",
    LogLevelWarn            : "WARN",
    LogLevelError           : "ERROR",
    LogLevelAssert          : "ASSERT",

    create: function (adjustConfig) {
        if (adjustConfig.hasListener()) {
            callCordovaCallback('setAttributionCallback', adjustConfig.getAttributionCallback());
        }

        callCordova('create', adjustConfig);
    },

    trackEvent: function (adjustEvent) {
        callCordova('trackEvent', adjustEvent);
    },

    setOfflineMode: function(enabled) {
        callCordova('setOfflineMode', enabled);
    },

    setEnabled: function (enabled) {
        callCordova('setEnabled', enabled);
    },

    isEnabled: function (callback) {
        callCordovaCallback('isEnabled', callback);
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