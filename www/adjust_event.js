function AdjustEvent(eventToken) {
    this.eventToken = eventToken;
    this.revenue = null;
    this.currency = null;
    this.deduplicationId = null;
    this.callbackId = null;
    this.transactionId = null;
    this.productId = null;
    this.callbackParameters = [];
    this.partnerParameters = [];
    // Android only
    this.purchaseToken = null;
}

AdjustEvent.prototype.setRevenue = function(revenue, currency) {
    this.revenue = revenue;
    this.currency = currency;
};

AdjustEvent.prototype.addCallbackParameter = function(key, value) {
    this.callbackParameters.push(key);
    this.callbackParameters.push(value);
};

AdjustEvent.prototype.addPartnerParameter = function(key, value) {
    this.partnerParameters.push(key);
    this.partnerParameters.push(value);
};

AdjustEvent.prototype.setCallbackId = function(callbackId) {
    this.callbackId = callbackId;
};

AdjustEvent.prototype.setDeduplicationId = function(deduplicationId) {
    this.deduplicationId = deduplicationId;
};

AdjustEvent.prototype.setProductId = function(productId) {
    this.productId = productId;
};

AdjustEvent.prototype.setTransactionId = function(transactionId) {
    this.transactionId = transactionId;
};

// Android only

AdjustEvent.prototype.setPurchaseToken = function(purchaseToken) {
    this.purchaseToken = purchaseToken;
};

module.exports = AdjustEvent;