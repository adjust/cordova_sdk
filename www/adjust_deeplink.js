function AdjustDeeplink(deeplink) {
    this.deeplink = deeplink;
    this.referrer = null;
}

AdjustDeeplink.prototype.setReferrer = function(referrer) {
    this.referrer = referrer;
};

module.exports = AdjustDeeplink;