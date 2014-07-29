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

var Adjust= {
    trackEvent: function (eventToken, parameters) {
        callCordova('trackEvent', eventToken, parameters);
    },
    trackRevenue: function (amountInCents, eventToken, parameters) {
        callCordova('trackRevenue', amountInCents, eventToken, parameters);
    },
    setFinishedTrackingCallback: function (callback) {
        callCordovaCallback('setFinishedTrackingCallback',callback);
    },
    setEnabled: function (enabled) {
        callCordova('setEnabled', enabled);
    },
    isEnabled: function (callback) {
        callCordovaCallback('isEnabled', callback);
    }
};

function startAdjust() {
    var appToken = '{adjust_appToken}';
    var environment = '{adjust_environment}';
    var logLevel = '{adjust_logLevel}';
    var enableEventBuffering = '{adjust_enableEventBuffering}';
    callCordova('appDidLaunch', appToken, environment,
            logLevel, enableEventBuffering);
}
function onPause () {
    callCordova('onPause');
}
function onResume () {
    callCordova('onResume');
}

document.addEventListener('deviceready', startAdjust ,false);
document.addEventListener('resume', onResume, false);
document.addEventListener('pause', onPause, false);

module.exports = Adjust;
