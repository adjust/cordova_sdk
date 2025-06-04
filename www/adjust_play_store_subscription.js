function AdjustPlayStoreSubscription(price, currency, sku, orderId, signature, purchaseToken) {
    this.price = price;
    this.currency = currency;
    this.sku = sku;
    this.orderId = orderId;
    this.signature = signature;
    this.purchaseToken = purchaseToken;
    this.purchaseTime = null;
    this.callbackParameters = [];
    this.partnerParameters = [];
}

AdjustPlayStoreSubscription.prototype.setPurchaseTime = function(purchaseTime) {
    if (!Number.isInteger(purchaseTime)) {
        console.log("[Adjust] Play Store subscription purchase time is not of type integer");
        return;
    }
    this.purchaseTime = purchaseTime;
};

AdjustPlayStoreSubscription.prototype.addCallbackParameter = function(key, value) {
    if (typeof key !== 'string' || typeof value !== 'string') {
        console.log("[Adjust] Play Store subscription callback parameter key or value is not of type string");
        return;
    }
    this.callbackParameters.push(key);
    this.callbackParameters.push(value);
};

AdjustPlayStoreSubscription.prototype.addPartnerParameter = function(key, value) {
    if (typeof key !== 'string' || typeof value !== 'string') {
        console.log("[Adjust] Play Store subscription partner parameter key or value is not of type string");
        return;
    }
    this.partnerParameters.push(key);
    this.partnerParameters.push(value);
};

module.exports = AdjustPlayStoreSubscription;
