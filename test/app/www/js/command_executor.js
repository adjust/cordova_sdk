'use strict';

/* 
 * A note on scheduling:
 *
 * Callbacks sent from Java -> Javascript through PluginResult are by nature not ordered.
 *  scheduleCommand(command) tries to receive commands and schedule them in an array (this.savedCommand)
 *  that would be executed based on what `this.nextToSendCounter` specifies.
 * 
 * Sorting mechanism goes as follows
 * - When receiving a new command...
 *   * if the new command is in order, send it immediately.
 *   * If the new command is not in order
 *     * Store it in a list
 *     * Check the list if it contains the next command to send
 * - After sending a command...
 *     * Check the list if it contains the next command to send
 * 
 * So this system is rechecking the list for items to send on two events:
 * - When a new not-in-order object is added.
 * - After a successful send
 */

// A wrapper for a command received from test server.
function AdjustCommand(functionName, params, order) {
    this.functionName = functionName;
    this.params = params;
    this.order = order;
}

function CommandExecutor(urlOverwrite) {
    this.adjustCommandExecutor = new AdjustCommandExecutor(urlOverwrite);
};

CommandExecutor.prototype.scheduleCommand = function(className, functionName, params, order) {
    switch (className) {
        case 'Adjust':
            var command = new AdjustCommand(functionName, params, order);
            this.adjustCommandExecutor.scheduleCommand(command);
            break;
    }
};

function AdjustCommandExecutor(urlOverwrite) {
    this.urlOverwrite = urlOverwrite;
    this.extraPath = null;
    this.savedEvents = {};
    this.savedConfigs = {};
    this.savedCommands = [];
    this.nextToSendCounter = 0;
};

// First point of entry for scheduling commands. Takes a 'AdjustCommand {command}' parameter.
AdjustCommandExecutor.prototype.scheduleCommand = function(command) {
    // If the command is in order, send in immediately.
    if (command.order === this.nextToSendCounter) {
        this.executeCommand(command, -1);
        return;
    }

    // Not in order, schedule it.
    this.savedCommands.push(command);

    // Recheck list.
    this.checkList();
}

// Check the list of commands to see which one is in order.
AdjustCommandExecutor.prototype.checkList = function() {
    for (var i = 0; i < this.savedCommands.length; i += 1) {
        var command = this.savedCommands[i];
        if (command.order === this.nextToSendCounter) {
            this.executeCommand(command, i);
            return;
        }
    }
}

// Execute the command. This will always be invoked either from:
//  - checkList() after scheduling a command
//  - scheduleCommand() only if the package was in order
//
// (AdjustCommand {command}) : The command to be executed
// (Number {idx})            : index of the command in the schedule list. -1 if it was sent directly
AdjustCommandExecutor.prototype.executeCommand = function(command, idx) {
    console.log(`[*] executeCommand(): ${JSON.stringify(command)}`);
    switch (command.functionName) {
        case 'testOptions' : this.testOptions(command.params); break;
        case 'config' : this.config(command.params); break;
        case 'start' : this.start(command.params); break;
        case 'event' : this.event(command.params); break;
        case 'resume' : this.resume(command.params); break;
        case 'pause' : this.pause(command.params); break;
        case 'setEnabled' : this.setEnabled(command.params); break;
        case 'setOfflineMode' : this.setOfflineMode(command.params); break;
        case 'addGlobalCallbackParameter' : this.addGlobalCallbackParameter(command.params); break;
        case 'addGlobalPartnerParameter' : this.addGlobalPartnerParameter(command.params); break;
        case 'removeGlobalCallbackParameter' : this.removeGlobalCallbackParameter(command.params); break;
        case 'removeGlobalPartnerParameter' : this.removeGlobalPartnerParameter(command.params); break;
        case 'removeGlobalCallbackParameters' : this.removeGlobalCallbackParameters(command.params); break;
        case 'removeGlobalPartnerParameters' : this.removeGlobalPartnerParameters(command.params); break;
        case 'setReferrer' : this.setReferrer(command.params); break;
        case 'setPushToken' : this.setPushToken(command.params); break;
        case 'gdprForgetMe' : this.gdprForgetMe(command.params); break;
        case 'attributionGetter' : this.attributionGetter(command.params); break;
        case 'trackEvent' : this.trackEvent(command.params); break;
        case 'trackAdRevenue' : this.trackAdRevenue(command.params); break;
        case 'trackSubscription' : this.trackAppStoreSubscription(command.params); break;
        case 'thirdPartySharing' : this.trackThirdPartySharing(command.params); break;
        case 'measurementConsent' : this.trackMeasurementConsent(command.params); break;
        case 'verifyPurchase' : this.verifyPurchase(command.params); break;
        case 'verifyTrack' : this.verifyTrack(command.params); break;
        case 'openDeeplink' : this.openDeeplink(command.params); break;
        case 'processDeeplink' : this.processDeeplink(command.params); break;
        case 'getLastDeeplink' : this.getLastDeeplink(command.params); break;
        case 'endFirstSessionDelay' : this.endFirstSessionDelay(command.params); break;
        case 'coppaComplianceInDelay' : this.coppaComplianceInDelay(command.params); break;
        case 'playStoreKidsComplianceInDelay' : this.playStoreKidsComplianceInDelay(command.params); break;
        case 'externalDeviceIdInDelay' : this.externalDeviceIdInDelay(command.params); break;
    }

    this.nextToSendCounter++;

    // If idx != -1, it means it was not sent directly. Delete its instance from the scheduling array.
    if (idx != -1) {
        this.savedCommands.splice(idx, 1);
    }

    // Recheck the list.
    this.checkList();
};

AdjustCommandExecutor.prototype.testOptions = function(params) {
    var testOptions = new AdjustTestOptions();
    testOptions.testUrlOverwrite = this.urlOverwrite;
    if ('basePath' in params) {
        this.extraPath = getFirstParameterValue(params, 'basePath');
    }
    if ('timerInterval' in params) {
        testOptions.timerIntervalInMilliseconds = getFirstParameterValue(params, 'timerInterval').toString();
    }
    if ('timerStart' in params) {
        testOptions.timerStartInMilliseconds = getFirstParameterValue(params, 'timerStart').toString();
    }
    if ('sessionInterval' in params) {
        testOptions.sessionIntervalInMilliseconds = getFirstParameterValue(params, 'sessionInterval').toString();
    }
    if ('subsessionInterval' in params) {
        testOptions.subsessionIntervalInMilliseconds = getFirstParameterValue(params, 'subsessionInterval').toString();
    }
    if ('attStatus' in params) {
        testOptions.attStatus = getFirstParameterValue(params, 'attStatus').toString();
    }
    if ('idfa' in params) {
        testOptions.idfa = getFirstParameterValue(params, 'idfa').toString();
    }
    if ('noBackoffWait' in params) {
        testOptions.noBackoffWait = getFirstParameterValue(params, 'noBackoffWait').toString() === 'true';
    }

    // default value -> NO - AdServices will not be used in test app by default
    testOptions.adServicesFrameworkEnabled = false;
    if ('adServicesFrameworkEnabled' in params) {
        testOptions.adServicesFrameworkEnabled = getFirstParameterValue(params, 'adServicesFrameworkEnabled').toString() === 'true';
    }

    if ('doNotIgnoreSystemLifecycleBootstrap' in params) {
         var doNotIgnoreSystemLifecycleBootstrap = getFirstParameterValue(params, 'doNotIgnoreSystemLifecycleBootstrap').toString() === 'true';
         if (doNotIgnoreSystemLifecycleBootstrap) {
             testOptions.ignoreSystemLifecycleBootstrap = false;
         }
     }

    var useTestConnectionOptions = false;
    if ('teardown' in params) {
        var teardownOptions = getValueFromKey(params, 'teardown');
        for (var i = 0; i < teardownOptions.length; i++) {
            var option = teardownOptions[i];
            if ('resetSdk' === option) {
                testOptions.teardown = true;
                testOptions.extraPath = this.extraPath;
                useTestConnectionOptions = true;
                Adjust.teardown('test');
            }
            if ('deleteState' === option) {
                testOptions.deleteState = true;
            }
            if ('resetTest' === option) {
                this.savedConfigs = {};
                this.savedEvents = {};
                testOptions.timerIntervalInMilliseconds = (-1).toString();
                testOptions.timerStartInMilliseconds = (-1).toString();
                testOptions.sessionIntervalInMilliseconds = (-1).toString();
                testOptions.subsessionIntervalInMilliseconds = (-1).toString();
            }
            if ('sdk' === option) {
                testOptions.teardown = true;
                testOptions.extraPath = null;
                Adjust.teardown('test');
            }
            if ('test' === option) {
                this.savedConfigs = null;
                this.savedEvents = null;
                this.extraPath = null;
                testOptions.timerIntervalInMilliseconds = (-1).toString();
                testOptions.timerStartInMilliseconds = (-1).toString();
                testOptions.sessionIntervalInMilliseconds = (-1).toString();
                testOptions.subsessionIntervalInMilliseconds = (-1).toString();
            }
        }
    }

    Adjust.setTestOptions(testOptions);
    if (useTestConnectionOptions == true) {
        AdjustTest.setTestConnectionOptions();
    }
};

AdjustCommandExecutor.prototype.config = function(params) {
    var configNumber = 0;
    if ('configName' in params) {
        var configName = getFirstParameterValue(params, 'configName');
        configNumber = parseInt(configName.substr(configName.length - 1))
    }

    var adjustConfig;
    if (configNumber in this.savedConfigs) {
        adjustConfig = this.savedConfigs[configNumber];
    } else {
        var environment = getFirstParameterValue(params, 'environment');
        var appToken = getFirstParameterValue(params, 'appToken');
        adjustConfig = new AdjustConfig(appToken, environment);
        adjustConfig.setLogLevel(AdjustConfig.LogLevelVerbose);
        this.savedConfigs[configNumber] = adjustConfig;
    }

    if ('logLevel' in params) {
        var logLevelS = getFirstParameterValue(params, 'logLevel');
        var logLevel = null;
        switch (logLevelS) {
            case 'verbose':
                logLevel = AdjustConfig.LogLevelVerbose;
                break;
            case 'debug':
                logLevel = AdjustConfig.LogLevelDebug;
                break;
            case 'info':
                logLevel = AdjustConfig.LogLevelInfo;
                break;
            case 'warn':
                logLevel = AdjustConfig.LogLevelWarn;
                break;
            case 'error':
                logLevel = AdjustConfig.LogLevelError;
                break;
            case 'assert':
                logLevel = AdjustConfig.LogLevelAssert;
                break;
            case 'suppress':
                logLevel = AdjustConfig.LogLevelSuppress;
                break;
        }

        adjustConfig.setLogLevel(logLevel);
    }

    if ('sdkPrefix' in params) {
        var sdkPrefix = getFirstParameterValue(params, 'sdkPrefix');
        adjustConfig.setSdkPrefix(sdkPrefix);
    }

    if ('defaultTracker' in params) {
        var defaultTracker = getFirstParameterValue(params, 'defaultTracker');
        adjustConfig.setDefaultTracker(defaultTracker);
    }

    if ('needsCost' in params) {
        var defaultTracker = getFirstParameterValue(params, 'needsCost');
        adjustConfig.enableCostDataInAttribution();
    }

    if ('sendInBackground' in params) {
        var sendInBackgroundS = getFirstParameterValue(params, 'sendInBackground');
        if (sendInBackgroundS == 'true') {
            adjustConfig.enableSendingInBackground();
        }
    }

    if ('allowIdfaReading' in params) {
        var allowIdfaReadingS = getFirstParameterValue(params, 'allowIdfaReading');
        if (allowIdfaReadingS != 'true') {
            adjustConfig.disableIdfaReading();
        }
    }

    if ('allowAdServicesInfoReading' in params) {
        var allowAdServicesInfoReadingS = getFirstParameterValue(params, 'allowAdServicesInfoReading');
        if (allowAdServicesInfoReadingS != 'true') {
            adjustConfig.disableAdServices();
    }
    }

    if ('allowSkAdNetworkHandling' in params) {
        var allowSkAdNetworkHandlingS = getFirstParameterValue(params, 'allowSkAdNetworkHandling');
        if (allowSkAdNetworkHandlingS != 'true') {
            adjustConfig.disableSkanAttribution();
        }
    }

    if ('allowAttUsage' in params) {
        var allowAttUsageS = getFirstParameterValue(params, 'allowAttUsage');
        if (allowAttUsageS != 'true') {
            adjustConfig.disableAppTrackingTransparencyUsage();
        }
    }

    if ('externalDeviceId' in params) {
        var externalDeviceId = getFirstParameterValue(params, 'externalDeviceId');
        adjustConfig.setExternalDeviceId(externalDeviceId);
    }

    if ('attributionCallbackSendAll' in params) {
        var _this = this;
        adjustConfig.setAttributionCallback(function(attribution) {
            AdjustTest.addInfoToSend('tracker_token', attribution.trackerToken);
            AdjustTest.addInfoToSend('tracker_name', attribution.trackerName);
            AdjustTest.addInfoToSend('network', attribution.network);
            AdjustTest.addInfoToSend('campaign', attribution.campaign);
            AdjustTest.addInfoToSend('adgroup', attribution.adgroup);
            AdjustTest.addInfoToSend('creative', attribution.creative);
            AdjustTest.addInfoToSend('click_label', attribution.clickLabel);
            AdjustTest.addInfoToSend('cost_type', attribution.costType);
            AdjustTest.addInfoToSend('cost_amount', attribution.costAmount);
            AdjustTest.addInfoToSend('cost_currency', attribution.costCurrency);
            if (device.platform === 'Android') {
                AdjustTest.addInfoToSend('fb_install_referrer', attribution.fbInstallReferrer);
            }

            // Remove fb_install_referrer from jsonResponse if it exists
            if (attribution.jsonResponse && device.platform === 'iOS') {
                try {
                    var json = JSON.parse(attribution.jsonResponse);
                    delete json.fb_install_referrer;
                    attribution.jsonResponse = JSON.stringify(json);
                } catch (e) {
                    console.warn('Failed to parse attribution.jsonResponse:', e);
                }
            }

            AdjustTest.addInfoToSend('json_response', attribution.jsonResponse);
            AdjustTest.sendInfoToServer(_this.extraPath);
        });
    }

    if ('sessionCallbackSendSuccess' in params) {
        var _this = this;
        adjustConfig.setSessionTrackingSucceededCallback(function(sessionSuccess) {
            AdjustTest.addInfoToSend('message', sessionSuccess.message);
            AdjustTest.addInfoToSend('timestamp', sessionSuccess.timestamp);
            AdjustTest.addInfoToSend('adid', sessionSuccess.adid);
            AdjustTest.addInfoToSend('jsonResponse', sessionSuccess.jsonResponse);
            AdjustTest.sendInfoToServer(_this.extraPath);
        });
    }

    if ('sessionCallbackSendFailure' in params) {
        var _this = this;
        adjustConfig.setSessionTrackingFailedCallback(function(sessionFailed) {
            AdjustTest.addInfoToSend('message', sessionFailed.message);
            AdjustTest.addInfoToSend('timestamp', sessionFailed.timestamp);
            AdjustTest.addInfoToSend('adid', sessionFailed.adid);
            AdjustTest.addInfoToSend('willRetry', sessionFailed.willRetry);
            AdjustTest.addInfoToSend('jsonResponse', sessionFailed.jsonResponse);
            AdjustTest.sendInfoToServer(_this.extraPath);
        });
    }

    if ('eventCallbackSendSuccess' in params) {
        var _this = this;
        adjustConfig.setEventTrackingSucceededCallback(function(eventSuccess) {
            AdjustTest.addInfoToSend('message', eventSuccess.message);
            AdjustTest.addInfoToSend('timestamp', eventSuccess.timestamp);
            AdjustTest.addInfoToSend('adid', eventSuccess.adid);
            AdjustTest.addInfoToSend('eventToken', eventSuccess.eventToken);
            AdjustTest.addInfoToSend('callbackId', eventSuccess.callbackId);
            AdjustTest.addInfoToSend('jsonResponse', eventSuccess.jsonResponse);
            AdjustTest.sendInfoToServer(_this.extraPath);
        });
    }

    if ('eventCallbackSendFailure' in params) {
        var _this = this;
        adjustConfig.setEventTrackingFailedCallback(function(eventFailed) {
            AdjustTest.addInfoToSend('message', eventFailed.message);
            AdjustTest.addInfoToSend('timestamp', eventFailed.timestamp);
            AdjustTest.addInfoToSend('adid', eventFailed.adid);
            AdjustTest.addInfoToSend('eventToken', eventFailed.eventToken);
            AdjustTest.addInfoToSend('callbackId', eventFailed.callbackId);
            AdjustTest.addInfoToSend('willRetry', eventFailed.willRetry);
            AdjustTest.addInfoToSend('jsonResponse', eventFailed.jsonResponse);
            AdjustTest.sendInfoToServer(_this.extraPath);
        });
    }

    if ('deferredDeeplinkCallback' in params) {
        var _this = this;
        var launchDeferredDeeplinkS = getFirstParameterValue(params, 'deferredDeeplinkCallback');
        var launchDeferredDeeplink = launchDeferredDeeplinkS === 'true';
        console.log(`[*] Launch deferred deeplink set to: ${launchDeferredDeeplink}`);
        if (launchDeferredDeeplink == false) {
            adjustConfig.disableDeferredDeeplinkOpening();
        }
        adjustConfig.setDeferredDeeplinkCallback(function(uri) {
            AdjustTest.addInfoToSend('deeplink', uri);
            AdjustTest.sendInfoToServer(_this.extraPath);
        });
    }

    if ('skanCallback' in params) {
        var _this = this;
        adjustConfig.setSkanUpdatedCallback(function(data) {
            var jsonObject = JSON.parse(JSON.stringify(data));
            for (let [key, value] of Object.entries(jsonObject)) {
                if (key === 'conversionValue') {
                    AdjustTest.addInfoToSend('conversion_value', value);
                }
                if (key === 'coarseValue') {
                    AdjustTest.addInfoToSend('coarse_value', value);
                }
                if (key === 'lockWindow') {
                    AdjustTest.addInfoToSend('lock_window', value);
                }
            }
            AdjustTest.sendInfoToServer(_this.extraPath);
        });
    }

    if ('attConsentWaitingSeconds' in params) {
        var attConsentWaitingSecondsS = getFirstParameterValue(params, 'attConsentWaitingSeconds');
        var attConsentWaitingSeconds = parseFloat(attConsentWaitingSecondsS);
        adjustConfig.setAttConsentWaitingInterval(attConsentWaitingSeconds);
    }

    if ('eventDeduplicationIdsMaxSize' in params) {
        var eventDeduplicationIdsMaxSizeS = getFirstParameterValue(params, 'eventDeduplicationIdsMaxSize');
        var eventDeduplicationIdsMaxSize = parseInt(eventDeduplicationIdsMaxSizeS);
        adjustConfig.setEventDeduplicationIdsMaxSize(eventDeduplicationIdsMaxSize);
    }

    if ('coppaCompliant' in params) {
        var coppaCompliantEnabledS = getFirstParameterValue(params, 'coppaCompliant');
        if (coppaCompliantEnabledS == 'true') {
            adjustConfig.enableCoppaCompliance();
        }
    }

    if ('playStoreKids' in params) {
        var playStoreKidsEnabledS = getFirstParameterValue(params, 'playStoreKids');
        var playStoreKidsEnabled = playStoreKidsEnabledS == 'true';
        adjustConfig.enablePlayStoreKidsCompliance();
    }

    if ('firstSessionDelayEnabled' in params) {
        var firstSessionDelayEnabledS = getFirstParameterValue(params, 'firstSessionDelayEnabled');
        if (firstSessionDelayEnabledS == 'true') {
            adjustConfig.enableFirstSessionDelay();
        }
    }
    
    if ('storeName' in params) {
        var storeNameS = getFirstParameterValue(params, 'storeName');
        var storeInfo = new AdjustStoreInfo(storeNameS);
        if ('storeAppId' in params) {
            var storeAppIdS = getFirstParameterValue(params, 'storeAppId');
            storeInfo.setStoreAppId(storeAppIdS);
        }
        adjustConfig.setStoreInfo(storeInfo);
    }
};

AdjustCommandExecutor.prototype.start = function(params) {
    this.config(params);
    var configNumber = 0;
    if ('configName' in params) {
        var configName = getFirstParameterValue(params, 'configName');
        configNumber = parseInt(configName.substr(configName.length - 1))
    }

    var adjustConfig = this.savedConfigs[configNumber];
    Adjust.initSdk(adjustConfig);

    delete this.savedConfigs[0];
};

AdjustCommandExecutor.prototype.event = function(params) {
    var eventNumber = 0;
    if ('eventName' in params) {
        var eventName = getFirstParameterValue(params, 'eventName');
        eventNumber = parseInt(eventName.substr(eventName.length - 1))
    }

    var adjustEvent;
    if (eventNumber in this.savedEvents) {
        adjustEvent = this.savedEvents[eventNumber];
    } else {
        var eventToken = getFirstParameterValue(params, 'eventToken');
        // weird behaviour with null value
        // double check this later to handle it on event instance level
        if (typeof eventToken !== 'string') {
            return;
        }
        adjustEvent = new AdjustEvent(eventToken);
        this.savedEvents[eventNumber] = adjustEvent;
    }

    if ('revenue' in params) {
        var revenueParams = getValueFromKey(params, 'revenue');
        var currency = revenueParams[0];
        var revenue = parseFloat(revenueParams[1]);
        adjustEvent.setRevenue(revenue, currency);
    }

    if ('callbackParams' in params) {
        var callbackParams = getValueFromKey(params, 'callbackParams');
        for (var i = 0; i < callbackParams.length; i += 2) {
            var key = callbackParams[i];
            var value = callbackParams[i+1];
            adjustEvent.addCallbackParameter(key, value);
        }
    }

    if ('partnerParams' in params) {
        var partnerParams = getValueFromKey(params, 'partnerParams');
        for (var i = 0; i < partnerParams.length; i += 2) {
            var key = partnerParams[i];
            var value = partnerParams[i+1];
            adjustEvent.addPartnerParameter(key, value);
        }
    }

    if ('orderId' in params) {
        var orderId = getFirstParameterValue(params, 'orderId');
        adjustEvent.setTransactionId(orderId);
    }

    if ('callbackId' in params) {
        var callbackId = getFirstParameterValue(params, 'callbackId');
        adjustEvent.setCallbackId(callbackId);
    }

    if ('productId' in params) {
        var productId = getFirstParameterValue(params, 'productId');
        adjustEvent.setProductId(productId);
    }

    if ('purchaseToken' in params) {
        var purchaseToken = getFirstParameterValue(params, 'purchaseToken');
        adjustEvent.setPurchaseToken(purchaseToken);
    }

    if ('transactionId' in params) {
        var transactionId = getFirstParameterValue(params, 'transactionId');
        adjustEvent.setTransactionId(transactionId);
    }

    if ('deduplicationId' in params) {
        var deduplicationId = getFirstParameterValue(params, 'deduplicationId');
        adjustEvent.setDeduplicationId(deduplicationId);
    }
};

AdjustCommandExecutor.prototype.trackEvent = function(params) {
    this.event(params);
    var eventNumber = 0;
    if ('eventName' in params) {
        var eventName = getFirstParameterValue(params, 'eventName');
        eventNumber = parseInt(eventName.substr(eventName.length - 1))
    }

    var adjustEvent = this.savedEvents[eventNumber];
    Adjust.trackEvent(adjustEvent);

    delete this.savedEvents[0];
};

AdjustCommandExecutor.prototype.pause = function(params) {
    Adjust.onPause('test');
};

AdjustCommandExecutor.prototype.resume = function(params) {
    Adjust.onResume('test');
};

AdjustCommandExecutor.prototype.setEnabled = function(params) {
    if (getFirstParameterValue(params, 'enabled') == 'true') {
        Adjust.enable();
    } else {
        Adjust.disable();
    }
};

AdjustCommandExecutor.prototype.setOfflineMode = function(params) {
    if (getFirstParameterValue(params, 'enabled') == 'true') {
        Adjust.switchToOfflineMode();
    } else {
        Adjust.switchBackToOnlineMode();
    }
};

AdjustCommandExecutor.prototype.addGlobalCallbackParameter = function(params) {
    var list = getValueFromKey(params, 'KeyValue');
    for (var i = 0; i < list.length; i += 2) {
        var key = list[i];
        var value = list[i+1];
        Adjust.addGlobalCallbackParameter(key, value);
    }
};

AdjustCommandExecutor.prototype.addGlobalPartnerParameter = function(params) {
    var list = getValueFromKey(params, 'KeyValue');
    for (var i = 0; i < list.length; i += 2) {
        var key = list[i];
        var value = list[i+1];
        Adjust.addGlobalPartnerParameter(key, value);
    }
};

AdjustCommandExecutor.prototype.removeGlobalCallbackParameter = function(params) {
    if ('key' in params) {
        var list = getValueFromKey(params, 'key');
        for (var i = 0; i < list.length; i += 1) {
            Adjust.removeGlobalCallbackParameter(list[i]);
        }
    }
};

AdjustCommandExecutor.prototype.removeGlobalPartnerParameter = function(params) {
    if ('key' in params) {
        var list = getValueFromKey(params, 'key');
        for (var i = 0; i < list.length; i += 1) {
            Adjust.removeGlobalPartnerParameter(list[i]);
        }
    }
};

AdjustCommandExecutor.prototype.removeGlobalCallbackParameters = function(params) {
    Adjust.removeGlobalCallbackParameters();
};

AdjustCommandExecutor.prototype.removeGlobalPartnerParameters = function(params) {
    Adjust.removeGlobalPartnerParameters();
};

AdjustCommandExecutor.prototype.setPushToken = function(params) {
    var token = getFirstParameterValue(params, 'pushToken');
    Adjust.setPushToken(token);
};

AdjustCommandExecutor.prototype.openDeeplink = function(params) {
    var deeplink = getFirstParameterValue(params, 'deeplink');
    if (typeof deeplink !== 'string') {
        return;
    }
    var adjustDeeplink = new AdjustDeeplink(deeplink);
    var referrer = getFirstParameterValue(params, 'referrer');
    if (typeof referrer === 'string') {
        adjustDeeplink.setReferrer(referrer);
    }
    Adjust.processDeeplink(adjustDeeplink);
};

AdjustCommandExecutor.prototype.gdprForgetMe = function(params) {
    Adjust.gdprForgetMe();
};

AdjustCommandExecutor.prototype.trackThirdPartySharing = function(params) {
    var isEnabled = null;
    if ('isEnabled' in params) {
        isEnabled = getFirstParameterValue(params, 'isEnabled') == 'true';
    }
    var adjustThirdPartySharing = new AdjustThirdPartySharing(isEnabled);

    if ('granularOptions' in params) {
        var granularOptions = getValueFromKey(params, 'granularOptions');
        for (var i = 0; i < granularOptions.length; i += 3) {
            var partnerName = granularOptions[i];
            var key = granularOptions[i+1];
            var value = granularOptions[i+2];
            adjustThirdPartySharing.addGranularOption(partnerName, key, value);
        }
    }

    if ('partnerSharingSettings' in params) {
        var partnerSharingSettings = getValueFromKey(params, 'partnerSharingSettings');
        for (var i = 0; i < partnerSharingSettings.length; i += 3) {
            var partnerName = partnerSharingSettings[i];
            var key = partnerSharingSettings[i+1];
            var value = partnerSharingSettings[i+2] === 'true';
            adjustThirdPartySharing.addPartnerSharingSetting(partnerName, key, value);
        }
    }

    Adjust.trackThirdPartySharing(adjustThirdPartySharing);
};

AdjustCommandExecutor.prototype.trackMeasurementConsent = function(params) {
    var isEnabled = getFirstParameterValue(params, 'isEnabled') == 'true';
    Adjust.trackMeasurementConsent(isEnabled);
};

AdjustCommandExecutor.prototype.trackAppStoreSubscription = function(params) {
    if (device.platform === 'iOS') {
        var price = getFirstParameterValue(params, 'revenue');
        var currency = getFirstParameterValue(params, 'currency');
        var transactionId = getFirstParameterValue(params, 'transactionId');
        var transactionDate = getFirstParameterValue(params, 'transactionDate');
        var salesRegion = getFirstParameterValue(params, 'salesRegion');

        var subscription = new AdjustAppStoreSubscription(price, currency, transactionId);
        subscription.setTransactionDate(transactionDate);
        subscription.setSalesRegion(salesRegion);

        if ('callbackParams' in params) {
            var callbackParams = getValueFromKey(params, 'callbackParams');
            for (var i = 0; i < callbackParams.length; i += 2) {
                var key = callbackParams[i];
                var value = callbackParams[i+1];
                subscription.addCallbackParameter(key, value);
            }
        }

        if ('partnerParams' in params) {
            var partnerParams = getValueFromKey(params, 'partnerParams');
            for (var i = 0; i < partnerParams.length; i += 2) {
                var key = partnerParams[i];
                var value = partnerParams[i+1];
                subscription.addPartnerParameter(key, value);
            }
        }

        Adjust.trackAppStoreSubscription(subscription);

    } else if (device.platform === 'Android') {
        var price = getFirstParameterValue(params, 'revenue');
        var currency = getFirstParameterValue(params, 'currency');
        var sku = getFirstParameterValue(params, 'productId');
        var signature = getFirstParameterValue(params, 'receipt');
        var purchaseToken = getFirstParameterValue(params, 'purchaseToken');
        var orderId = getFirstParameterValue(params, 'transactionId');
        var purchaseTime = getFirstParameterValue(params, 'transactionDate');

        var subscription = new AdjustPlayStoreSubscription(Number(price), currency, sku, orderId, signature, purchaseToken);
        subscription.setPurchaseTime(Number(purchaseTime));

        if ('callbackParams' in params) {
            var callbackParams = getValueFromKey(params, 'callbackParams');
            for (var i = 0; i < callbackParams.length; i += 2) {
                var key = callbackParams[i];
                var value = callbackParams[i+1];
                subscription.addCallbackParameter(key, value);
            }
        }

        if ('partnerParams' in params) {
            var partnerParams = getValueFromKey(params, 'partnerParams');
            for (var i = 0; i < partnerParams.length; i += 2) {
                var key = partnerParams[i];
                var value = partnerParams[i+1];
                subscription.addPartnerParameter(key, value);
            }
        }

        Adjust.trackPlayStoreSubscription(subscription);
    }
};

AdjustCommandExecutor.prototype.trackAdRevenue = function(params) {
    var adjustAdRevenue;
    var source = getFirstParameterValue(params, 'adRevenueSource');
    // weird behaviour with null value
    // double check this later to handle it on event instance level
    if (typeof source !== 'string') {
        return;
    }
    adjustAdRevenue = new AdjustAdRevenue(source);

    if ('revenue' in params) {
        var revenueParams = getValueFromKey(params, 'revenue');
        var currency = revenueParams[0];
        var revenue = parseFloat(revenueParams[1]);
        adjustAdRevenue.setRevenue(revenue, currency);
    }

    if ('adImpressionsCount' in params) {
        var adImpressionsCount = getFirstParameterValue(params, 'adImpressionsCount');
        var adImpressionsCountValue = parseInt(adImpressionsCount);
        adjustAdRevenue.setAdImpressionsCount(adImpressionsCountValue);
    }

    if ('adRevenueUnit' in params) {
        var adRevenueUnit = getFirstParameterValue(params, 'adRevenueUnit');

        // test server might set adRevenueUnit to be undefined/null, which gets
        // serialized/deserialized as string 'null', leading to failed test
        if (adRevenueUnit === 'null') {
            adRevenueUnit = null;
        }

        adjustAdRevenue.setAdRevenueUnit(adRevenueUnit);
    }

    if ('adRevenuePlacement' in params) {
        var adRevenuePlacement = getFirstParameterValue(params, 'adRevenuePlacement');

        // test server might set adRevenuePlacement to be undefined/null, which gets
        // serialized/deserialized as string 'null', leading to failed test
        if (adRevenuePlacement === 'null') {
            adRevenuePlacement = null;
        }

        adjustAdRevenue.setAdRevenuePlacement(adRevenuePlacement);
    }

    if ('adRevenueNetwork' in params) {
        var adRevenueNetwork = getFirstParameterValue(params, 'adRevenueNetwork');

        // test server might set adRevenueNetwork to be undefined/null, which gets
        // serialized/deserialized as string 'null', leading to failed test
        if (adRevenueNetwork === 'null') {
            adRevenueNetwork = null;
        }

        adjustAdRevenue.setAdRevenueNetwork(adRevenueNetwork);
    }

    if ('callbackParams' in params) {
        var callbackParams = getValueFromKey(params, 'callbackParams');
        for (var i = 0; i < callbackParams.length; i += 2) {
            var key = callbackParams[i];
            var value = callbackParams[i+1];
            adjustAdRevenue.addCallbackParameter(key, value);
        }
    }

    if ('partnerParams' in params) {
        var partnerParams = getValueFromKey(params, 'partnerParams');
        for (var i = 0; i < partnerParams.length; i += 2) {
            var key = partnerParams[i];
            var value = partnerParams[i+1];
            adjustAdRevenue.addPartnerParameter(key, value);
        }
    }
    Adjust.trackAdRevenue(adjustAdRevenue);
};

AdjustCommandExecutor.prototype.getLastDeeplink = function(params) {
    var _this = this;
    Adjust.getLastDeeplink(function(lastDeeplink) {
        AdjustTest.addInfoToSend('last_deeplink', lastDeeplink);
        AdjustTest.sendInfoToServer(_this.extraPath);
    });
};

AdjustCommandExecutor.prototype.verifyPurchase = function(params) {
    if (device.platform === 'iOS') {
        var productId = getFirstParameterValue(params, 'productId');
        var transactionId = getFirstParameterValue(params, 'transactionId');
        var purchase = new AdjustAppStorePurchase(productId, transactionId);

        var _this = this;
        Adjust.verifyAppStorePurchase(purchase, function(verificationInfo) {
            AdjustTest.addInfoToSend('verification_status', verificationInfo.verificationStatus);
            AdjustTest.addInfoToSend('code', verificationInfo.code);
            AdjustTest.addInfoToSend('message', verificationInfo.message);
            AdjustTest.sendInfoToServer(_this.extraPath);
        });
    } else if (device.platform === 'Android') {
        var productId = getFirstParameterValue(params, 'productId');
        var purchaseToken = getFirstParameterValue(params, 'purchaseToken');
        var purchase = new AdjustPlayStorePurchase(productId, purchaseToken);

        var _this = this;
        Adjust.verifyPlayStorePurchase(purchase, function(verificationInfo) {
            AdjustTest.addInfoToSend('verification_status', verificationInfo.verificationStatus);
            AdjustTest.addInfoToSend('code', verificationInfo.code);
            AdjustTest.addInfoToSend('message', verificationInfo.message);
            AdjustTest.sendInfoToServer(_this.extraPath);
        });
    }
};

AdjustCommandExecutor.prototype.verifyTrack = function(params) {
    var _this = this;
    this.event(params);
    var eventNumber = 0;
    if ('eventName' in params) {
        var eventName = getFirstParameterValue(params, 'eventName');
        eventNumber = parseInt(eventName.substr(eventName.length - 1))
    }
    var adjustEvent = this.savedEvents[eventNumber];
    if (device.platform === 'iOS') {
        Adjust.verifyAndTrackAppStorePurchase(adjustEvent, function(verificationResult) {
            AdjustTest.addInfoToSend('verification_status', verificationResult.verificationStatus);
            AdjustTest.addInfoToSend('code', verificationResult.code);
            AdjustTest.addInfoToSend('message', verificationResult.message);
            AdjustTest.sendInfoToServer(_this.extraPath);
        });
    } else if (device.platform === 'Android') {
        Adjust.verifyAndTrackPlayStorePurchase(adjustEvent, function(verificationResult) {
            AdjustTest.addInfoToSend('verification_status', verificationResult.verificationStatus);
            AdjustTest.addInfoToSend('code', verificationResult.code);
            AdjustTest.addInfoToSend('message', verificationResult.message);
            AdjustTest.sendInfoToServer(_this.extraPath);
        });
    }

    delete this.savedEvents[0];
};


AdjustCommandExecutor.prototype.processDeeplink = function(params) {
    var deeplink = getFirstParameterValue(params, 'deeplink');
    if (typeof deeplink !== 'string') {
        return;
    }
    var adjustDeeplink = new AdjustDeeplink(deeplink);
    var referrer = getFirstParameterValue(params, 'referrer');
    if (typeof referrer === 'string') {
        adjustDeeplink.setReferrer(referrer);
    }
    var _this = this;
    Adjust.processAndResolveDeeplink(adjustDeeplink, function(resolvedLink) {
        AdjustTest.addInfoToSend('resolved_link', resolvedLink);
        AdjustTest.sendInfoToServer(_this.extraPath);
    });
};

AdjustCommandExecutor.prototype.attributionGetter = function(params) {
    var _this = this;
    Adjust.getAttribution(function(attribution) {
        AdjustTest.addInfoToSend('tracker_token', attribution.trackerToken);
        AdjustTest.addInfoToSend('tracker_name', attribution.trackerName);
        AdjustTest.addInfoToSend('network', attribution.network);
        AdjustTest.addInfoToSend('campaign', attribution.campaign);
        AdjustTest.addInfoToSend('adgroup', attribution.adgroup);
        AdjustTest.addInfoToSend('creative', attribution.creative);
        AdjustTest.addInfoToSend('click_label', attribution.clickLabel);
        AdjustTest.addInfoToSend('cost_type', attribution.costType);
        AdjustTest.addInfoToSend('cost_amount', attribution.costAmount);
        AdjustTest.addInfoToSend('cost_currency', attribution.costCurrency);
        if (device.platform === 'Android') {
            AdjustTest.addInfoToSend('fb_install_referrer', attribution.fbInstallReferrer);
        }

        // Remove fb_install_referrer from jsonResponse if it exists
        if (attribution.jsonResponse && device.platform === 'iOS') {
            try {
                var json = JSON.parse(attribution.jsonResponse);
                delete json.fb_install_referrer;
                attribution.jsonResponse = JSON.stringify(json);
            } catch (e) {
                console.warn('Failed to parse attribution.jsonResponse:', e);
            }
        }

        AdjustTest.addInfoToSend('json_response', attribution.jsonResponse);
        AdjustTest.sendInfoToServer(_this.extraPath);
    });
};

AdjustCommandExecutor.prototype.endFirstSessionDelay = function(params) {
    Adjust.endFirstSessionDelay();
};

AdjustCommandExecutor.prototype.coppaComplianceInDelay = function(params) {
    if (getFirstParameterValue(params, 'isEnabled') == 'true') {
        Adjust.enableCoppaComplianceInDelay();
    } else {
        Adjust.disableCoppaComplianceInDelay();
    }
};

AdjustCommandExecutor.prototype.playStoreKidsComplianceInDelay = function(params) {
    if (getFirstParameterValue(params, 'isEnabled') == 'true') {
        Adjust.enablePlayStoreKidsComplianceInDelay();
    } else {
        Adjust.disablePlayStoreKidsComplianceInDelay();
    }
};

AdjustCommandExecutor.prototype.externalDeviceIdInDelay = function(params) {
    var externalDeviceId = getFirstParameterValue(params, 'externalDeviceId');
    Adjust.setExternalDeviceIdInDelay(externalDeviceId);
};

// Util methods

function getValueFromKey(params, key) {
    if (key in params) {
        return params[key];
    }

    return null;
}

function getFirstParameterValue(params, key) {
    if (key in params) {
        var param = params[key];

        if (param != null && param.length >= 1) {
            return param[0];
        }
    }

    return null;
}
