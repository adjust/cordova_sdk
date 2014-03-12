var AdjustCordova = {
    callCordova: function (action) {
        var args = Array.prototype.slice.call(arguments, 1);
        cordova.exec(function callback(data) { alert(action + ' ' + data); },
                     function errorHandler(err) { },
                     'AdjustCordova',
                     action,
                     args
                     );
    },
    appDidLaunch: function (appToken) {
        this.callCordova('appDidLaunch', appToken);
    },
    trackEvent: function (eventToken, parameters) {
        this.callCordova('trackEvent', eventToken, parameters);
    },
    trackRevenue: function (amountInCents, eventToken, parameters) {
        this.callCordova('trackRevenue', amountInCents, eventToken, parameters);
    },
    setLogLevel: function (logLevel) {
        this.callCordova('setLogLevel', logLevel);
    },
    setEnvironment: function (environment) {
        this.callCordova('setEnvironment', environment);
    },
    setEventBufferingEnabled: function (enabled) {
        this.callCordova('setEventBufferingEnabled', enabled);
    },
    setMacMd5TrackingEnabled: function (enabled) {
        this.callCordova('setMacMd5TrackingEnabled', enabled);
    },
    setFinishedTrackingCallback: function (callback) {
        cordova.exec(callback,
                     function errorHandler(err) { },
                     'AdjustCordova',
                     'setFinishedTrackingCallback',
                     []
                     );
    },
    onPause: function() {
        this.callCordova('onPause');
    },
    onResume: function() {
        this.callCordova('onResume');
    }
};

module.exports = AdjustCordova;
