var Adjust= {
    callCordova: function (action) {
        var args = Array.prototype.slice.call(arguments, 1);
        cordova.exec(function callback(data) { },
                     function errorHandler(err) { },
                     'Adjust',
                     action,
                     args
                     );
    },
    appDidLaunch: function (appToken, environment, logLevel, eventBuffering) {
        this.callCordova('appDidLaunch', appToken, environment,
            logLevel, eventBuffering);
    },
    trackEvent: function (eventToken, parameters) {
        this.callCordova('trackEvent', eventToken, parameters);
    },
    trackRevenue: function (amountInCents, eventToken, parameters) {
        this.callCordova('trackRevenue', amountInCents, eventToken, parameters);
    },
    setFinishedTrackingCallback: function (callback) {
        cordova.exec(callback,
                     function errorHandler(err) { },
                     'Adjust',
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

module.exports = Adjust;
