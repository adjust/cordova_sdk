function AdjustEvent(eventToken) {
    this.eventToken = eventToken;
    this.revenue = null;
    this.currency = null;
    this.deduplicationId = null;
    this.callbackId = null;
    this.productId = null;
    this.callbackParameters = [];
    this.partnerParameters = [];
    
    // ios only
    this.transactionId = null;
    
    // android only
    this.purchaseToken = null;
}

AdjustEvent.prototype.setRevenue = function(revenue, currency) {
    if (typeof revenue !== 'number' || typeof currency !== 'string') {
        console.log("[Adjust] Event revenue or currency is not of a proper data type");
    }
    this.revenue = revenue;
    this.currency = currency;
};

AdjustEvent.prototype.addCallbackParameter = function(key, value) {
    if (typeof key !== 'string' || typeof value !== 'string') {
        console.log("[Adjust] Event callback parameter key or value is not of type string");
        return;
    }
    this.callbackParameters.push(key);
    this.callbackParameters.push(value);
};

AdjustEvent.prototype.addPartnerParameter = function(key, value) {
    if (typeof key !== 'string' || typeof value !== 'string') {
        console.log("[Adjust] Event partner parameter key or value is not of type string");
        return;
    }
    this.partnerParameters.push(key);
    this.partnerParameters.push(value);
};

AdjustEvent.prototype.setCallbackId = function(callbackId) {
    if (typeof callbackId !== 'string') {
        console.log("[Adjust] Event callback ID is not of type string");
        return;
    }
    this.callbackId = callbackId;
};

AdjustEvent.prototype.setDeduplicationId = function(deduplicationId) {
    if (typeof deduplicationId !== 'string') {
        console.log("[Adjust] Event deduplication ID is not of type string");
        return;
    }
    this.deduplicationId = deduplicationId;
};

AdjustEvent.prototype.setProductId = function(productId) {
    if (typeof productId !== 'string') {
        console.log("[Adjust] Event product ID is not of type string");
        return;
    }
    this.productId = productId;
};

// ios only

AdjustEvent.prototype.setTransactionId = function(transactionId) {
    if (typeof transactionId !== 'string') {
        console.log("[Adjust] Event transaction ID is not of type string");
        return;
    }
    this.transactionId = transactionId;
};

// android only

AdjustEvent.prototype.setPurchaseToken = function(purchaseToken) {
    if (typeof purchaseToken !== 'string') {
        console.log("[Adjust] Event purchase token is not of type string");
        return;
    }
    this.purchaseToken = purchaseToken;
};

module.exports = AdjustEvent;
