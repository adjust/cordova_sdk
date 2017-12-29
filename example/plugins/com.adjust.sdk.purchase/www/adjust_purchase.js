function callCordova (action) {
    var args = Array.prototype.slice.call(arguments, 1);

    cordova.exec(function callback(data) { },
                 function errorHandler(err) { },
                 'AdjustPurchase',
                 action,
                 args
    );
}

function callCordovaCallback (action, callback) {
    var args = Array.prototype.slice.call(arguments, 2);

    cordova.exec(callback,
        function errorHandler(err) { },
        'AdjustPurchase',
        action,
        args
    );
}

var AdjustPurchase = {
    init: function (adjustConfig) {
        callCordova('init', adjustConfig);
    },

    verifyPurchaseAndroid: function (itemSku, purchaseToken, developerPayload, verificationCallback) {
        if (verificationCallback != null) {
            callCordovaCallback('setVerificationCallback', verificationCallback);
        }

        callCordova('verifyPurchaseAndroid', itemSku, purchaseToken, developerPayload);
    },

    verifyPurchaseiOS: function (receipt, transactionId, productId, verificationCallback) {
        if (verificationCallback != null) {
            callCordovaCallback('setVerificationCallback', verificationCallback);
        }

        callCordova('verifyPurchaseiOS', receipt, transactionId, productId);
    }
};

module.exports = AdjustPurchase;