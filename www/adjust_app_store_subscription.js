function AdjustAppStoreSubscription(price, currency, transactionId, receipt) {
    this.price = price;
    this.currency = currency;
    this.transactionId = transactionId;
    this.receipt = receipt;
    this.transactionDate = null;
    this.salesRegion = null;
    this.callbackParameters = [];
    this.partnerParameters = [];
}

AdjustAppStoreSubscription.prototype.setTransactionDate = function(transactionDate) {
    this.transactionDate = transactionDate;
};

AdjustAppStoreSubscription.prototype.setSalesRegion = function(salesRegion) {
    this.salesRegion = salesRegion;
};

AdjustAppStoreSubscription.prototype.addCallbackParameter = function(key, value) {
    this.callbackParameters.push(key);
    this.callbackParameters.push(value);
};

AdjustAppStoreSubscription.prototype.addPartnerParameter = function(key, value) {
    this.partnerParameters.push(key);
    this.partnerParameters.push(value);
};

module.exports = AdjustAppStoreSubscription;
