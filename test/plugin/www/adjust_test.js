'use strict';

function callCordova(action) {
    var args = Array.prototype.slice.call(arguments, 1);
    cordova.exec(function callback(data) { },
        function errorHandler(err) { console.log("[AdjustTest]: Could not execute call inside of 'callCordova': " + err); },
        'AdjustTest',
        action,
        args
    );
}

function callCordovaCallback(action, callback) {
    var args = Array.prototype.slice.call(arguments, 2);
    cordova.exec(callback,
        function errorHandler(err) { console.log("[AdjustTest]: Could not execute call inside of 'callCordovaCallback': " + err); },
        'AdjustTest',
        action,
        args
    );
}

var AdjustTest = {
    startTestSession: function(baseUrl, sdkVersion, callback) {
        callCordovaCallback('startTestSession', callback, baseUrl, sdkVersion);
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

module.exports = AdjustTest;
