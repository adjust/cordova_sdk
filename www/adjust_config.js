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
    this.shouldLaunchDeeplink = null;
    this.referrer = null;
    this.sendInBackground = null;

    this.userAgent = null;
    this.delayStart = 0.0;

    // Android only
    this.processName = null;
}

AdjustConfig.EnvironmentSandbox    = "sandbox";
AdjustConfig.EnvironmentProduction = "production";

AdjustConfig.LogLevelVerbose       = "VERBOSE";
AdjustConfig.LogLevelDebug         = "DEBUG";
AdjustConfig.LogLevelInfo          = "INFO";
AdjustConfig.LogLevelWarn          = "WARN";
AdjustConfig.LogLevelError         = "ERROR";
AdjustConfig.LogLevelAssert        = "ASSERT";
AdjustConfig.LogLevelSuppress      = "SUPPRESS";

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
    return this.sessionTrackingSuccessfulCallbackListener;
};

AdjustConfig.prototype.getSessionTrackingFailedCallback = function() {
    return this.sessionTrackingFailedCallbackListener;
};

AdjustConfig.prototype.getDeeplinkCallback = function() {
    return this.deeplinkCallbackListener;
};

AdjustConfig.prototype.getUserAgent = function() {
    return this.userAgent;
}

AdjustConfig.prototype.getDelayStart = function() {
    return this.delayStart;
}

AdjustConfig.prototype.getReferrer = function() {
    return this.referrer;
}

AdjustConfig.prototype.getSendInBackground = function() {
    return this.sendInBackground;
}

AdjustConfig.prototype.getShouldLaunchDeeplink = function() {
    return this.shouldLaunchDeeplink;
}

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

//@deprecated
AdjustConfig.prototype.setCallbackListener = function(callbackListener) {
    console.warn("Calling deprecated function! Use a dedicated setCallbackListener for the specific callback you need. Check adjust_config.js for more info");
    return false;
};

AdjustConfig.prototype.setUserAgent = function(userAgent) {
    this.userAgent = userAgent;
}

AdjustConfig.prototype.setDelayStart = function(delayStart) {
    this.delayStart = delayStart;
}

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
    = function(sessionTrackingSuccessfulCallbackListener) {
        this.sessionTrackingSuccessfulCallbackListener = sessionTrackingSuccessfulCallbackListener;
    };

AdjustConfig.prototype.setSessionTrackingFailedCallbackListener 
    = function(sessionTrackingFailedCallbackListener) {
        this.sessionTrackingFailedCallbackListener = sessionTrackingFailedCallbackListener;
    };

AdjustConfig.prototype.setDeeplinkCallbackListener 
    = function(deeplinkCallbackListener) {
        this.deeplinkCallbackListener = deeplinkCallbackListener;
    };

AdjustConfig.prototype.setReferrer = function(referrer) {
    this.referrer = referrer;
}

AdjustConfig.prototype.setSendInBackground = function(sendInBackground) {
    this.sendInBackground = sendInBackground;
}

AdjustConfig.prototype.setShouldLaunchDeeplink = function(shouldLaunchDeeplink) {
    this.shouldLaunchDeeplink = shouldLaunchDeeplink;
}

//HAS
//=================================

//@deprecated
AdjustConfig.prototype.hasListener = function() {
    console.warn("Calling deprecated function! Use a dedicated hasListener for the specific callback you need. Check adjust_config.js for more info");
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
    return this.sessionTrackingSuccessfulCallbackListener != null;
};

AdjustConfig.prototype.hasSessionTrackingFailedListener = function() {
    return this.sessionTrackingFailedCallbackListener != null;
};

AdjustConfig.prototype.hasDeeplinkCallbackListener = function() {
    return this.deeplinkCallbackListener != null;
};

module.exports = AdjustConfig;
