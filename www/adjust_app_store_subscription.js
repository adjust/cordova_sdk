function AdjustAppStoreSubscription(price, currency, transactionId) {
    this.price = price;
    this.currency = currency;
    this.transactionId = transactionId;
    this.transactionDate = null;
    this.salesRegion = null;
    this.callbackParameters = [];
    this.partnerParameters = [];
}

AdjustAppStoreSubscription.prototype.setTransactionDate = function(transactionDate) {
    if (typeof transactionDate !== 'string') {
        console.log("[Adjust] App Store subscription transaction date is not of type string");
        return;
    }
    this.transactionDate = transactionDate;
};

AdjustAppStoreSubscription.prototype.setSalesRegion = function(salesRegion) {
    if (typeof salesRegion !== 'string') {
        console.log("[Adjust] App Store subscription sales region is not of type string");
        return;
    }
    this.salesRegion = salesRegion;
};

AdjustAppStoreSubscription.prototype.addCallbackParameter = function(key, value) {
    if (typeof key !== 'string' || typeof value !== 'string') {
        console.log("[Adjust] App Store subscription callback parameter key or value is not of type string");
        return;
    }
    this.callbackParameters.push(key);
    this.callbackParameters.push(value);
};

AdjustAppStoreSubscription.prototype.addPartnerParameter = function(key, value) {
    if (typeof key !== 'string' || typeof value !== 'string') {
        console.log("[Adjust] App Store subscription partner parameter key or value is not of type string");
        return;
    }
    this.partnerParameters.push(key);
    this.partnerParameters.push(value);
};

module.exports = AdjustAppStoreSubscription;
