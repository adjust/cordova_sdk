function AdjustThirdPartySharing(isEnabled) {
	if (isEnabled == null) {
		this.isEnabled = undefined;
	} else {
		this.isEnabled = isEnabled == true ? "true" : "false";
	}
    this.granularOptions = [];
    this.partnerSharingSettings = [];
}

AdjustThirdPartySharing.prototype.addGranularOption = function(partnerName, key, value) {
    if (typeof partnerName !== 'string' || typeof key !== 'string' || typeof value !== 'string') {
        console.log("[Adjust] Granular option parameterName, key or value is not of a type string");
        return;
    }
    this.granularOptions.push(partnerName);
    this.granularOptions.push(key);
    this.granularOptions.push(value);
};

AdjustThirdPartySharing.prototype.addPartnerSharingSetting = function(partnerName, key, value) {
    if (typeof partnerName !== 'string' || typeof key !== 'string' || typeof value !== 'boolean') {
        console.log("[Adjust] Partner sharing setting parameters are not of a proper data type");
        return;
    }
    this.partnerSharingSettings.push(partnerName);
    this.partnerSharingSettings.push(key);
    this.partnerSharingSettings.push(value);
};

module.exports = AdjustThirdPartySharing;
