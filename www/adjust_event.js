function AdjustEvent(eventToken) {
    this.eventToken = eventToken;
    this.revenue = null;
    this.currency = null;
    this.productId = null;
    this.transactionId = null;
    this.callbackId = null;
    this.callbackParameters = [];
    this.partnerParameters = [];
    // iOS only
    this.receipt = null;
    this.isReceiptSet = false;
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

AdjustEvent.prototype.setProductId = function(productId) {
    this.productId = productId;
};

AdjustEvent.prototype.setTransactionId = function(transactionId) {
    this.transactionId = transactionId;
};

// iOS only

AdjustEvent.prototype.setReceipt = function(receipt) {
    this.receipt = receipt;
};

// Android only

AdjustEvent.prototype.setPurchaseToken = function(purchaseToken) {
    this.purchaseToken = purchaseToken;
};

// @deprecated
AdjustEvent.prototype.setReceiptForTransactionId = function(receipt, transactionId) {
    console.warn("Calling deprecated function! Please use official purchase verification method.");
    // this.receipt = receipt;
    // this.transactionId = transactionId;
    // this.isReceiptSet = true;
};

module.exports = AdjustEvent;
