'use strict';

function callCordova(action) {
    var args = Array.prototype.slice.call(arguments, 1);

    cordova.exec(function callback(data) { },
        function errorHandler(err) { },
        'AdjustTesting',
        action,
        args
    );
}

function callCordovaCallback(action, callback) {
    var args = Array.prototype.slice.call(arguments, 2);

    cordova.exec(callback,
        function errorHandler(err) { },
        'AdjustTesting',
        action,
        args
    );
}

var AdjustTesting = {
    startTestSession: function(baseUrl, callback) {
        callCordovaCallback('startTestSession', callback, baseUrl);
    },

    addInfoToSend: function(key, value) {
        callCordova('addInfoToSend', key, value);
    },

    sendInfoToServer: function(basePath) {
        callCordova('sendInfoToServer', basePath);
    },

    addTest: function(test) {
        callCordova('addTest', test);
    },

    addTestDirectory: function(testDir) {
        callCordova('addTestDirectory', testDir);
    }
};

module.exports = AdjustTesting;
