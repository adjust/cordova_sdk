function AdjustThirdPartySharing(isEnabled) {
	if (isEnabled == null) {
		this.isEnabled = "null";
	} else {
		this.isEnabled = isEnabled == true ? "true" : "false";
	}
    this.granularOptions = [];
}

AdjustThirdPartySharing.prototype.addGranularOption = function(partnerName, key, value) {
    if (typeof partnerName !== 'string' || typeof key !== 'string' || typeof value !== 'string') {
        return;
    }
    this.granularOptions.push(partnerName);
    this.granularOptions.push(key);
    this.granularOptions.push(value);
};

module.exports = AdjustThirdPartySharing;
