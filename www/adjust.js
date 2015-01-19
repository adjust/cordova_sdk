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


var adjustStarted = false;
var config = {
    appToken: '',
    environment: '',
    logLevel: '',
    enableEventBuffering: '',
};

var Adjust = {

    configure: function ( appToken, environment, logLevel, enableEventBuffering ) {

        config.appToken = appToken;
        config.environment = environment;
        config.logLevel = logLevel;
        config.enableEventBuffering = enableEventBuffering;

    },

    getConfiguration: function () {
        return config;
    },

    start: function() {

        if ( config.appToken === '' ) {
            console.error( 'Adjust not configured propperly: appToken is not set!' );
            return;
        }

        if ( !adjustStarted ) {
            callCordova('appDidLaunch', config.appToken, config.environment,
                config.logLevel, config.enableEventBuffering);
            console.log( 'Ajdust started' );
            adjustStarted = true;
        } else {
            console.error( 'Adjust has been started before.' );
        }

    },

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

function onPause () {
    callCordova('onPause');
}

function onResume () {
    callCordova('onResume');
}

document.addEventListener('resume', onResume, false);
document.addEventListener('pause', onPause, false);

module.exports = Adjust;
