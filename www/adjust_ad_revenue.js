var AdjustAdRevenue = function(source) {
    this.source = source;
    this.revenue = null;
    this.currency = null;
    this.adImpressionsCount = null;
    this.adRevenueNetwork = null;
    this.adRevenueUnit = null;
    this.adRevenuePlacement = null;
    this.callbackParameters = [];
    this.partnerParameters = [];
};

AdjustAdRevenue.prototype.setRevenue = function(revenue, currency) {
    if (typeof revenue !== 'number' || typeof currency !== 'string') {
        console.log("[Adjust] Ad revenue or currency is not of a proper data type");
    }
    this.revenue = revenue.toString();
    this.currency = currency;
};

AdjustAdRevenue.prototype.setAdImpressionsCount = function(adImpressionsCount) {
    if (!Number.isInteger(adImpressionsCount)) {
        console.log("[Adjust] Ad impressions count is not of type integer");
        return;
    }
    this.adImpressionsCount = adImpressionsCount.toString();
};

AdjustAdRevenue.prototype.setAdRevenueNetwork = function(adRevenueNetwork) {
    if (typeof adRevenueNetwork !== 'string') {
        console.log("[Adjust] Ad revenue network is not of type string");
        return;
    }
    this.adRevenueNetwork = adRevenueNetwork;
};

AdjustAdRevenue.prototype.setAdRevenueUnit = function(adRevenueUnit) {
    if (typeof adRevenueUnit !== 'string') {
        console.log("[Adjust] Ad revenue unit is not of type string");
        return;
    }
    this.adRevenueUnit = adRevenueUnit;
};

AdjustAdRevenue.prototype.setAdRevenuePlacement = function(adRevenuePlacement) {
    if (typeof adRevenuePlacement !== 'string') {
        console.log("[Adjust] Ad revenue placement is not of type string");
        return;
    }
    this.adRevenuePlacement = adRevenuePlacement;
};

AdjustAdRevenue.prototype.addCallbackParameter = function(key, value) {
    if (typeof key !== 'string' || typeof value !== 'string') {
        console.log("[Adjust] Ad revenue callback parameter key or value is not of type string");
        return;
    }
    this.callbackParameters.push(key);
    this.callbackParameters.push(value);
};

AdjustAdRevenue.prototype.addPartnerParameter = function(key, value) {
    if (typeof key !== 'string' || typeof value !== 'string') {
        console.log("[Adjust] Ad revenue partner parameter key or value is not of type string");
        return;
    }
    this.partnerParameters.push(key);
    this.partnerParameters.push(value);
};

module.exports = AdjustAdRevenue;
