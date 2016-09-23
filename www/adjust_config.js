function AdjustConfig(appToken, environment) {
    // iOS & Android
    this.appToken = appToken;
    this.environment = environment;

    this.sdkPrefix = "cordova4.10.0";

    this.logLevel = null;
    this.defaultTracker = null;

    this.attributionCallbackListener = null;
    this.eventTrackingSuccessfulCallbackListener = null;
    this.eventTrackingFailedCallbackListener = null;
    this.sessionTrackingSuccessfulCallbackListener = null;
    this.sessionTrackingFailedCallbackListener = null;
    this.deeplinkCallbackListener = null;

    this.eventBufferingEnabled = null;

    // Android only
    this.processName = null;
}

AdjustConfig.EnvironmentSandbox     = "sandbox";
AdjustConfig.EnvironmentProduction  = "production";

AdjustConfig.LogLevelVerbose        = "VERBOSE";
AdjustConfig.LogLevelDebug          = "DEBUG";
AdjustConfig.LogLevelInfo           = "INFO";
AdjustConfig.LogLevelWarn           = "WARN";
AdjustConfig.LogLevelError          = "ERROR";
AdjustConfig.LogLevelAssert         = "ASSERT";

//GETTERS
//===========================
AdjustConfig.prototype.getAttributionCallback = function() {
    return this.attributionCallbackListener;
};

AdjustConfig.prototype.getEventTrackingSuccessfulCallback = function() {
    return this.eventTrackingSuccessfulCallbackListener;
};

AdjustConfig.prototype.getEventTrackingFailedCallback = function() {
    return this.eventTrackingFailedCallbackListener;
};

AdjustConfig.prototype.getSessionTrackingSuccessfulCallback = function() {
    return this.eventTrackingSuccessfulCallbackListener;
};

AdjustConfig.prototype.getSessionTrackingFailedCallback = function() {
    return this.eventTrackingFailedCallbackListener;
};

AdjustConfig.prototype.getDeeplinkCallback = function() {
    return this.deeplinkCallbackListener;
};

//SETTERS
//===========================
AdjustConfig.prototype.setEventBufferingEnabled = function(isEnabled) {
    this.eventBufferingEnabled = isEnabled;
};

AdjustConfig.prototype.setLogLevel = function(logLevel) {
    this.logLevel = logLevel;
};

AdjustConfig.prototype.setProcessName = function(processName) {
    this.processName = processName;
};

AdjustConfig.prototype.setDefaultTracker = function(defaultTracker) {
    this.defaultTracker = defaultTracker;
};

//DEPRECATED
AdjustConfig.prototype.setCallbackListener = function(callbackListener) {
    //TODO: LOG ERROR/WARNING
    return false;
};

AdjustConfig.prototype.setAttributionCallbackListener = function(attributionCallbackListener) {
    this.attributionCallbackListener = attributionCallbackListener;
};

AdjustConfig.prototype.setEventTrackingSuccessfulCallbackListener 
    = function(eventTrackingSuccessfulCallbackListener) {
        this.eventTrackingSuccessfulCallbackListener = eventTrackingSuccessfulCallbackListener;
    };

AdjustConfig.prototype.setEventTrackingFailedCallbackListener 
    = function(eventTrackingFailedCallbackListener) {
        this.eventTrackingFailedCallbackListener = eventTrackingFailedCallbackListener;
    };

AdjustConfig.prototype.setSessionTrackingSuccessfulCallbackListener 
    = function(eventTrackingSuccessfulCallbackListener) {
        this.eventTrackingSuccessfulCallbackListener = eventTrackingSuccessfulCallbackListener;
    };

AdjustConfig.prototype.setSessionTrackingFailedCallbackListener 
    = function(eventTrackingFailedCallbackListener) {
        this.eventTrackingFailedCallbackListener = eventTrackingFailedCallbackListener;
    };

AdjustConfig.prototype.setDeeplinkCallbackListener 
    = function(deeplinkCallbackListener) {
        this.deeplinkCallbackListener = deeplinkCallbackListener;
    };

//HAS
//=================================

//@DEPRECATED
AdjustConfig.prototype.hasListener = function() {
    //TODO LOG ERROR/WARNING
    return false;
};

AdjustConfig.prototype.hasAttributionListener = function() {
    return this.attributionCallbackListener != null;
};

AdjustConfig.prototype.hasEventTrackingSuccessfulListener = function() {
    return this.eventTrackingSuccessfulCallbackListener != null;
};

AdjustConfig.prototype.hasEventTrackingFailedListener = function() {
    return this.eventTrackingFailedCallbackListener != null;
};

AdjustConfig.prototype.hasSessionTrackingSuccessfulListener = function() {
    return this.eventTrackingSuccessfulCallbackListener != null;
};

AdjustConfig.prototype.hasSessionTrackingFailedListener = function() {
    return this.eventTrackingFailedCallbackListener != null;
};

AdjustConfig.prototype.hasDeeplinkCallbackListener = function() {
    return this.deeplinkCallbackListener != null;
};

module.exports = AdjustConfig;
