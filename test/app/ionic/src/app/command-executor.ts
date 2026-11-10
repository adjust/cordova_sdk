/*
 * TypeScript port of test/app/cordova/www/js/command_executor.js.
 *
 * Every SDK call goes through the Ionic wrapper (@awesome-cordova-plugins/adjust), so a test session
 * exercises the classes, methods and types of ionic-native/index.ts. The only calls that go to the
 * plugin directly are the test hooks listed in test-plugin.ts.
 *
 * A note on scheduling:
 *
 * Commands coming from the native side are not guaranteed to arrive in order. scheduleCommand() runs
 * a command right away when it is the next one in line and parks it otherwise. The parked commands
 * are checked again when a command is parked and after every command that was run.
 */

import { Capacitor } from '@capacitor/core';
import {
  Adjust,
  AdjustAdRevenue,
  AdjustAppStorePurchase,
  AdjustAppStoreSubscription,
  AdjustAttribution,
  AdjustConfig,
  AdjustDeeplink,
  AdjustEnvironment,
  AdjustEvent,
  AdjustLogLevel,
  AdjustPlayStorePurchase,
  AdjustPlayStoreSubscription,
  AdjustPurchaseVerificationResult,
  AdjustStoreInfo,
  AdjustThirdPartySharing,
} from '@awesome-cordova-plugins/adjust/ngx';

import { adjustPlugin, adjustTest, AdjustTestOptions } from './test-plugin';

/** Parameters of a command, as the test server sends them: every key holds a list of strings. */
export type CommandParams = Record<string, string[]>;

interface AdjustCommand {
  functionName: string;
  params: CommandParams;
  order: number;
}

const isIos = Capacitor.getPlatform() === 'ios';
const isAndroid = Capacitor.getPlatform() === 'android';

export class CommandExecutor {
  private readonly adjustCommandExecutor: AdjustCommandExecutor;

  constructor(adjust: Adjust, urlOverwrite: string) {
    this.adjustCommandExecutor = new AdjustCommandExecutor(adjust, urlOverwrite);
  }

  scheduleCommand(className: string, functionName: string, params: CommandParams, order: number): void {
    switch (className) {
      case 'Adjust':
        this.adjustCommandExecutor.scheduleCommand({ functionName, params, order });
        break;
    }
  }
}

class AdjustCommandExecutor {
  private extraPath: string | null = null;
  private savedEvents: Record<number, AdjustEvent> = {};
  private savedConfigs: Record<number, AdjustConfig> = {};
  private savedCommands: AdjustCommand[] = [];
  private nextToSendCounter = 0;

  constructor(
    private readonly adjust: Adjust,
    private readonly urlOverwrite: string
  ) {}

  // First point of entry for scheduling commands.
  scheduleCommand(command: AdjustCommand): void {
    // If the command is in order, run it immediately.
    if (command.order === this.nextToSendCounter) {
      this.executeCommand(command, -1);
      return;
    }

    // Not in order, park it and recheck the list.
    this.savedCommands.push(command);
    this.checkList();
  }

  // Check the parked commands to see which one is in order.
  private checkList(): void {
    for (let i = 0; i < this.savedCommands.length; i += 1) {
      const command = this.savedCommands[i];
      if (command.order === this.nextToSendCounter) {
        this.executeCommand(command, i);
        return;
      }
    }
  }

  // Runs the command. idx is its position in the parked list, or -1 when it was run directly.
  private executeCommand(command: AdjustCommand, idx: number): void {
    console.log(`[*] executeCommand(): ${JSON.stringify(command)}`);
    const params = command.params;
    switch (command.functionName) {
      case 'testOptions': this.testOptions(params); break;
      case 'config': this.config(params); break;
      case 'start': this.start(params); break;
      case 'event': this.event(params); break;
      case 'resume': this.resume(); break;
      case 'pause': this.pause(); break;
      case 'setEnabled': this.setEnabled(params); break;
      case 'setOfflineMode': this.setOfflineMode(params); break;
      case 'addGlobalCallbackParameter': this.addGlobalCallbackParameter(params); break;
      case 'addGlobalPartnerParameter': this.addGlobalPartnerParameter(params); break;
      case 'removeGlobalCallbackParameter': this.removeGlobalCallbackParameter(params); break;
      case 'removeGlobalPartnerParameter': this.removeGlobalPartnerParameter(params); break;
      case 'removeGlobalCallbackParameters': this.removeGlobalCallbackParameters(); break;
      case 'removeGlobalPartnerParameters': this.removeGlobalPartnerParameters(); break;
      case 'setPushToken': this.setPushToken(params); break;
      case 'gdprForgetMe': this.gdprForgetMe(); break;
      case 'attributionGetter': this.attributionGetter(params); break;
      case 'adidGetter': this.adidGetter(params); break;
      case 'adidGetterWithTimeout': this.adidGetterWithTimeout(params); break;
      case 'attributionGetterWithTimeout': this.attributionGetterWithTimeout(params); break;
      case 'idfaGetter': this.idfaGetter(params); break;
      case 'idfvGetter': this.idfvGetter(params); break;
      case 'googleAdIdGetter': this.googleAdIdGetter(params); break;
      case 'amazonAdIdGetter': this.amazonAdIdGetter(params); break;
      case 'sdkVersionGetter': this.sdkVersionGetter(params); break;
      case 'trackEvent': this.trackEvent(params); break;
      case 'trackAdRevenue': this.trackAdRevenue(params); break;
      case 'trackSubscription': this.trackSubscription(params); break;
      case 'thirdPartySharing': this.trackThirdPartySharing(params); break;
      case 'measurementConsent': this.trackMeasurementConsent(params); break;
      case 'verifyPurchase': this.verifyPurchase(params); break;
      case 'verifyTrack': this.verifyTrack(params); break;
      case 'openDeeplink': this.openDeeplink(params); break;
      case 'processDeeplink': this.processDeeplink(params); break;
      case 'getLastDeeplink': this.getLastDeeplink(params); break;
      case 'tpsSettingsGetter': this.tpsSettingsGetter(params); break;
      case 'endFirstSessionDelay': this.endFirstSessionDelay(); break;
      case 'coppaComplianceInDelay': this.coppaComplianceInDelay(params); break;
      case 'playStoreKidsComplianceInDelay': this.playStoreKidsComplianceInDelay(params); break;
      case 'externalDeviceIdInDelay': this.externalDeviceIdInDelay(params); break;
    }

    this.nextToSendCounter++;

    // If idx is not -1, the command was parked. Remove it from the list.
    if (idx !== -1) {
      this.savedCommands.splice(idx, 1);
    }

    // Recheck the list.
    this.checkList();
  }

  private testOptions(params: CommandParams): void {
    const testOptions = new AdjustTestOptions();
    testOptions.testUrlOverwrite = this.urlOverwrite;
    if ('basePath' in params) {
      this.extraPath = getFirstParameterValue(params, 'basePath');
    }
    if ('timerInterval' in params) {
      testOptions.timerIntervalInMilliseconds = getFirstParameterValue(params, 'timerInterval');
    }
    if ('timerStart' in params) {
      testOptions.timerStartInMilliseconds = getFirstParameterValue(params, 'timerStart');
    }
    if ('sessionInterval' in params) {
      testOptions.sessionIntervalInMilliseconds = getFirstParameterValue(params, 'sessionInterval');
    }
    if ('subsessionInterval' in params) {
      testOptions.subsessionIntervalInMilliseconds = getFirstParameterValue(params, 'subsessionInterval');
    }
    if ('attStatus' in params) {
      testOptions.attStatus = getFirstParameterValue(params, 'attStatus');
    }
    if ('idfa' in params) {
      testOptions.idfa = getFirstParameterValue(params, 'idfa');
    }
    if ('noBackoffWait' in params) {
      testOptions.noBackoffWait = getFirstParameterValue(params, 'noBackoffWait') === 'true';
    }

    // AdServices is not used in the test app unless a test asks for it
    testOptions.adServicesFrameworkEnabled = false;
    if ('adServicesFrameworkEnabled' in params) {
      testOptions.adServicesFrameworkEnabled = getFirstParameterValue(params, 'adServicesFrameworkEnabled') === 'true';
    }

    if ('doNotIgnoreSystemLifecycleBootstrap' in params) {
      if (getFirstParameterValue(params, 'doNotIgnoreSystemLifecycleBootstrap') === 'true') {
        testOptions.ignoreSystemLifecycleBootstrap = false;
      }
    }

    let useTestConnectionOptions = false;
    if ('teardown' in params) {
      for (const option of params['teardown']) {
        if (option === 'resetSdk') {
          testOptions.teardown = true;
          testOptions.extraPath = this.extraPath;
          useTestConnectionOptions = true;
          adjustPlugin().teardown('test');
        }
        if (option === 'deleteState') {
          testOptions.deleteState = true;
        }
        if (option === 'resetTest') {
          this.savedConfigs = {};
          this.savedEvents = {};
          testOptions.timerIntervalInMilliseconds = '-1';
          testOptions.timerStartInMilliseconds = '-1';
          testOptions.sessionIntervalInMilliseconds = '-1';
          testOptions.subsessionIntervalInMilliseconds = '-1';
        }
        if (option === 'sdk') {
          testOptions.teardown = true;
          testOptions.extraPath = null;
          adjustPlugin().teardown('test');
        }
        if (option === 'test') {
          this.savedConfigs = {};
          this.savedEvents = {};
          this.extraPath = null;
          testOptions.timerIntervalInMilliseconds = '-1';
          testOptions.timerStartInMilliseconds = '-1';
          testOptions.sessionIntervalInMilliseconds = '-1';
          testOptions.subsessionIntervalInMilliseconds = '-1';
        }
      }
    }

    adjustPlugin().setTestOptions(testOptions);
    if (useTestConnectionOptions) {
      adjustTest().setTestConnectionOptions();
    }
  }

  private config(params: CommandParams): void {
    const configNumber = getNumberSuffix(params, 'configName');

    let adjustConfig: AdjustConfig;
    if (configNumber in this.savedConfigs) {
      adjustConfig = this.savedConfigs[configNumber];
    } else {
      const environment = getFirstParameterValue(params, 'environment') as AdjustEnvironment;
      const appToken = getFirstParameterValue(params, 'appToken')!;
      adjustConfig = new AdjustConfig(appToken, environment);
      adjustConfig.setLogLevel(AdjustLogLevel.Verbose);
      this.savedConfigs[configNumber] = adjustConfig;
    }

    if ('logLevel' in params) {
      const logLevels: Record<string, AdjustLogLevel> = {
        verbose: AdjustLogLevel.Verbose,
        debug: AdjustLogLevel.Debug,
        info: AdjustLogLevel.Info,
        warn: AdjustLogLevel.Warn,
        error: AdjustLogLevel.Error,
        assert: AdjustLogLevel.Assert,
        suppress: AdjustLogLevel.Suppress,
      };
      adjustConfig.setLogLevel(logLevels[getFirstParameterValue(params, 'logLevel')!]);
    }

    // 'sdkPrefix' is not handled: the wrapper has no setter for it and the plugin sets its own in initSdk()

    if ('defaultTracker' in params) {
      adjustConfig.setDefaultTracker(getFirstParameterValue(params, 'defaultTracker')!);
    }

    if ('needsCost' in params) {
      adjustConfig.enableCostDataInAttribution();
    }

    if ('sendInBackground' in params) {
      if (getFirstParameterValue(params, 'sendInBackground') === 'true') {
        adjustConfig.enableSendingInBackground();
      }
    }

    if ('allowIdfaReading' in params) {
      if (getFirstParameterValue(params, 'allowIdfaReading') !== 'true') {
        adjustConfig.disableIdfaReading();
      }
    }

    if ('deviceIdsReadingEnabled' in params) {
      if (getFirstParameterValue(params, 'deviceIdsReadingEnabled') !== 'true') {
        adjustConfig.disableDeviceIdsReading();
      }
    }

    if ('fbIdReadingEnabled' in params) {
      if (getFirstParameterValue(params, 'fbIdReadingEnabled') !== 'true') {
        adjustConfig.disableFbIdReading();
      }
    }

    if ('allowAdServicesInfoReading' in params) {
      if (getFirstParameterValue(params, 'allowAdServicesInfoReading') !== 'true') {
        adjustConfig.disableAdServices();
      }
    }

    if ('allowSkAdNetworkHandling' in params) {
      if (getFirstParameterValue(params, 'allowSkAdNetworkHandling') !== 'true') {
        adjustConfig.disableSkanAttribution();
      }
    }

    if ('allowAttUsage' in params) {
      if (getFirstParameterValue(params, 'allowAttUsage') !== 'true') {
        adjustConfig.disableAppTrackingTransparencyUsage();
      }
    }

    if ('externalDeviceId' in params) {
      adjustConfig.setExternalDeviceId(getFirstParameterValue(params, 'externalDeviceId')!);
    }

    if ('attributionCallbackSendAll' in params) {
      adjustConfig.setAttributionCallback((attribution) => {
        this.addAttributionInfo(attribution, attribution.costAmount);
        adjustTest().sendInfoToServer(this.extraPath);
      });
    }

    if ('sessionCallbackSendSuccess' in params) {
      adjustConfig.setSessionTrackingSucceededCallback((sessionSuccess) => {
        adjustTest().addInfoToSend('message', sessionSuccess.message);
        adjustTest().addInfoToSend('timestamp', sessionSuccess.timestamp);
        adjustTest().addInfoToSend('adid', sessionSuccess.adid);
        adjustTest().addInfoToSend('jsonResponse', sessionSuccess.jsonResponse);
        adjustTest().sendInfoToServer(this.extraPath);
      });
    }

    if ('sessionCallbackSendFailure' in params) {
      adjustConfig.setSessionTrackingFailedCallback((sessionFailed) => {
        adjustTest().addInfoToSend('message', sessionFailed.message);
        adjustTest().addInfoToSend('timestamp', sessionFailed.timestamp);
        adjustTest().addInfoToSend('adid', sessionFailed.adid);
        adjustTest().addInfoToSend('willRetry', sessionFailed.willRetry);
        adjustTest().addInfoToSend('jsonResponse', sessionFailed.jsonResponse);
        adjustTest().sendInfoToServer(this.extraPath);
      });
    }

    if ('eventCallbackSendSuccess' in params) {
      adjustConfig.setEventTrackingSucceededCallback((eventSuccess) => {
        adjustTest().addInfoToSend('message', eventSuccess.message);
        adjustTest().addInfoToSend('timestamp', eventSuccess.timestamp);
        adjustTest().addInfoToSend('adid', eventSuccess.adid);
        adjustTest().addInfoToSend('eventToken', eventSuccess.eventToken);
        adjustTest().addInfoToSend('callbackId', eventSuccess.callbackId);
        adjustTest().addInfoToSend('jsonResponse', eventSuccess.jsonResponse);
        adjustTest().sendInfoToServer(this.extraPath);
      });
    }

    if ('eventCallbackSendFailure' in params) {
      adjustConfig.setEventTrackingFailedCallback((eventFailed) => {
        adjustTest().addInfoToSend('message', eventFailed.message);
        adjustTest().addInfoToSend('timestamp', eventFailed.timestamp);
        adjustTest().addInfoToSend('adid', eventFailed.adid);
        adjustTest().addInfoToSend('eventToken', eventFailed.eventToken);
        adjustTest().addInfoToSend('callbackId', eventFailed.callbackId);
        adjustTest().addInfoToSend('willRetry', eventFailed.willRetry);
        adjustTest().addInfoToSend('jsonResponse', eventFailed.jsonResponse);
        adjustTest().sendInfoToServer(this.extraPath);
      });
    }

    if ('deferredDeeplinkCallback' in params) {
      const launchDeferredDeeplink = getFirstParameterValue(params, 'deferredDeeplinkCallback') === 'true';
      console.log(`[*] Launch deferred deeplink set to: ${launchDeferredDeeplink}`);
      if (!launchDeferredDeeplink) {
        adjustConfig.disableDeferredDeeplinkOpening();
      }
      adjustConfig.setDeferredDeeplinkCallback((uri) => {
        adjustTest().addInfoToSend('deeplink', uri);
        adjustTest().sendInfoToServer(this.extraPath);
      });
    }

    if ('remoteTriggerCallback' in params) {
      adjustConfig.setRemoteTriggerCallback((remoteTrigger) => {
        adjustTest().addInfoToSend('label', remoteTrigger.label);
        adjustTest().addInfoToSend('payload', remoteTrigger.payloadJson);
        adjustTest().sendInfoToServer(this.extraPath);
      });
    }

    if ('thirdPartySharingSettingsChangedCallbackSendAll' in params) {
      adjustConfig.setThirdPartySharingSettingsChangedCallback((thirdPartySharingSettings) => {
        adjustTest().addInfoToSend(
          'third_party_sharing_settings',
          thirdPartySharingSettings.thirdPartySharingSettingsJson
        );
        adjustTest().sendInfoToServer(this.extraPath);
      });
    }

    if ('skanCallback' in params) {
      adjustConfig.setSkanUpdatedCallback((skanData) => {
        // only send what the native SDK actually reported
        const reported = JSON.parse(JSON.stringify(skanData)) as Record<string, unknown>;
        if ('conversionValue' in reported) {
          adjustTest().addInfoToSend('conversion_value', skanData.conversionValue);
        }
        if ('coarseValue' in reported) {
          adjustTest().addInfoToSend('coarse_value', skanData.coarseValue);
        }
        if ('lockWindow' in reported) {
          adjustTest().addInfoToSend('lock_window', skanData.lockWindow);
        }
        adjustTest().sendInfoToServer(this.extraPath);
      });
    }

    if ('attConsentWaitingSeconds' in params) {
      adjustConfig.setAttConsentWaitingInterval(parseFloat(getFirstParameterValue(params, 'attConsentWaitingSeconds')!));
    }

    if ('eventDeduplicationIdsMaxSize' in params) {
      adjustConfig.setEventDeduplicationIdsMaxSize(
        parseInt(getFirstParameterValue(params, 'eventDeduplicationIdsMaxSize')!, 10)
      );
    }

    if ('coppaCompliant' in params) {
      if (getFirstParameterValue(params, 'coppaCompliant') === 'true') {
        adjustConfig.enableCoppaCompliance();
      }
    }

    if ('playStoreKids' in params) {
      if (getFirstParameterValue(params, 'playStoreKids') === 'true') {
        adjustConfig.enablePlayStoreKidsCompliance();
      }
    }

    if ('appSetIdReadingEnabled' in params) {
      if (getFirstParameterValue(params, 'appSetIdReadingEnabled') !== 'true') {
        adjustConfig.disableAppSetIdReading();
      }
    }

    if ('googleAdIdReadingEnabled' in params) {
      if (getFirstParameterValue(params, 'googleAdIdReadingEnabled') !== 'true') {
        adjustConfig.disableGoogleAdIdReading();
      }
    }

    if ('androidIdReadingEnabled' in params) {
      if (getFirstParameterValue(params, 'androidIdReadingEnabled') !== 'true') {
        adjustConfig.disableAndroidIdReading();
      }
    }

    if ('fireAdIdReadingEnabled' in params) {
      if (getFirstParameterValue(params, 'fireAdIdReadingEnabled') !== 'true') {
        adjustConfig.disableFireAdIdReading();
      }
    }

    if ('deviceIdsFromPluginsReadingEnabled' in params) {
      if (getFirstParameterValue(params, 'deviceIdsFromPluginsReadingEnabled') !== 'true') {
        adjustConfig.disableDeviceIdsFromPluginsReading();
      }
    }

    if ('firstSessionDelayEnabled' in params) {
      if (getFirstParameterValue(params, 'firstSessionDelayEnabled') === 'true') {
        adjustConfig.enableFirstSessionDelay();
      }
    }

    if ('storeName' in params) {
      const storeInfo = new AdjustStoreInfo(getFirstParameterValue(params, 'storeName')!);
      if ('storeAppId' in params) {
        storeInfo.setStoreAppId(getFirstParameterValue(params, 'storeAppId')!);
      }
      adjustConfig.setStoreInfo(storeInfo);
    }
  }

  private start(params: CommandParams): void {
    this.config(params);
    const adjustConfig = this.savedConfigs[getNumberSuffix(params, 'configName')];
    this.adjust.initSdk(adjustConfig);

    delete this.savedConfigs[0];
  }

  private event(params: CommandParams): void {
    const eventNumber = getNumberSuffix(params, 'eventName');

    let adjustEvent: AdjustEvent;
    if (eventNumber in this.savedEvents) {
      adjustEvent = this.savedEvents[eventNumber];
    } else {
      const eventToken = getFirstParameterValue(params, 'eventToken');
      // the test server can send a command without an event token
      if (typeof eventToken !== 'string') {
        return;
      }
      adjustEvent = new AdjustEvent(eventToken);
      this.savedEvents[eventNumber] = adjustEvent;
    }

    if ('revenue' in params) {
      const [currency, revenue] = params['revenue'];
      adjustEvent.setRevenue(parseFloat(revenue), currency);
    }

    if ('callbackParams' in params) {
      forEachPair(params['callbackParams'], (key, value) => adjustEvent.addCallbackParameter(key, value));
    }

    if ('partnerParams' in params) {
      forEachPair(params['partnerParams'], (key, value) => adjustEvent.addPartnerParameter(key, value));
    }

    if ('orderId' in params) {
      adjustEvent.setTransactionId(getFirstParameterValue(params, 'orderId')!);
    }

    if ('callbackId' in params) {
      adjustEvent.setCallbackId(getFirstParameterValue(params, 'callbackId')!);
    }

    if ('productId' in params) {
      adjustEvent.setProductId(getFirstParameterValue(params, 'productId')!);
    }

    if ('purchaseToken' in params) {
      adjustEvent.setPurchaseToken(getFirstParameterValue(params, 'purchaseToken')!);
    }

    if ('transactionId' in params) {
      adjustEvent.setTransactionId(getFirstParameterValue(params, 'transactionId')!);
    }

    if ('deduplicationId' in params) {
      adjustEvent.setDeduplicationId(getFirstParameterValue(params, 'deduplicationId')!);
    }
  }

  private trackEvent(params: CommandParams): void {
    this.event(params);
    const adjustEvent = this.savedEvents[getNumberSuffix(params, 'eventName')];
    this.adjust.trackEvent(adjustEvent);

    delete this.savedEvents[0];
  }

  private pause(): void {
    adjustPlugin().onPause('test');
  }

  private resume(): void {
    adjustPlugin().onResume('test');
  }

  private setEnabled(params: CommandParams): void {
    if (getFirstParameterValue(params, 'enabled') === 'true') {
      this.adjust.enable();
    } else {
      this.adjust.disable();
    }
  }

  private setOfflineMode(params: CommandParams): void {
    if (getFirstParameterValue(params, 'enabled') === 'true') {
      this.adjust.switchToOfflineMode();
    } else {
      this.adjust.switchBackToOnlineMode();
    }
  }

  private addGlobalCallbackParameter(params: CommandParams): void {
    forEachPair(params['KeyValue'], (key, value) => this.adjust.addGlobalCallbackParameter(key, value));
  }

  private addGlobalPartnerParameter(params: CommandParams): void {
    forEachPair(params['KeyValue'], (key, value) => this.adjust.addGlobalPartnerParameter(key, value));
  }

  private removeGlobalCallbackParameter(params: CommandParams): void {
    if ('key' in params) {
      for (const key of params['key']) {
        this.adjust.removeGlobalCallbackParameter(key);
      }
    }
  }

  private removeGlobalPartnerParameter(params: CommandParams): void {
    if ('key' in params) {
      for (const key of params['key']) {
        this.adjust.removeGlobalPartnerParameter(key);
      }
    }
  }

  private removeGlobalCallbackParameters(): void {
    this.adjust.removeGlobalCallbackParameters();
  }

  private removeGlobalPartnerParameters(): void {
    this.adjust.removeGlobalPartnerParameters();
  }

  private setPushToken(params: CommandParams): void {
    this.adjust.setPushToken(getFirstParameterValue(params, 'pushToken')!);
  }

  private openDeeplink(params: CommandParams): void {
    const adjustDeeplink = buildDeeplink(params);
    if (adjustDeeplink) {
      this.adjust.processDeeplink(adjustDeeplink);
    }
  }

  private gdprForgetMe(): void {
    this.adjust.gdprForgetMe();
  }

  private trackThirdPartySharing(params: CommandParams): void {
    let isEnabled: boolean | null = null;
    if ('isEnabled' in params) {
      isEnabled = getFirstParameterValue(params, 'isEnabled') === 'true';
    }
    const adjustThirdPartySharing = new AdjustThirdPartySharing(isEnabled);

    if ('granularOptions' in params) {
      const granularOptions = params['granularOptions'];
      for (let i = 0; i < granularOptions.length; i += 3) {
        adjustThirdPartySharing.addGranularOption(granularOptions[i], granularOptions[i + 1], granularOptions[i + 2]);
      }
    }

    if ('partnerSharingSettings' in params) {
      const partnerSharingSettings = params['partnerSharingSettings'];
      for (let i = 0; i < partnerSharingSettings.length; i += 3) {
        adjustThirdPartySharing.addPartnerSharingSetting(
          partnerSharingSettings[i],
          partnerSharingSettings[i + 1],
          partnerSharingSettings[i + 2] === 'true'
        );
      }
    }

    this.adjust.trackThirdPartySharing(adjustThirdPartySharing);
  }

  private trackMeasurementConsent(params: CommandParams): void {
    this.adjust.trackMeasurementConsent(getFirstParameterValue(params, 'isEnabled') === 'true');
  }

  private trackSubscription(params: CommandParams): void {
    if (isIos) {
      const subscription = new AdjustAppStoreSubscription(
        getFirstParameterValue(params, 'revenue')!,
        getFirstParameterValue(params, 'currency')!,
        getFirstParameterValue(params, 'transactionId')!
      );
      subscription.setTransactionDate(getFirstParameterValue(params, 'transactionDate')!);
      subscription.setSalesRegion(getFirstParameterValue(params, 'salesRegion')!);

      if ('callbackParams' in params) {
        forEachPair(params['callbackParams'], (key, value) => subscription.addCallbackParameter(key, value));
      }
      if ('partnerParams' in params) {
        forEachPair(params['partnerParams'], (key, value) => subscription.addPartnerParameter(key, value));
      }

      this.adjust.trackAppStoreSubscription(subscription);
    } else if (isAndroid) {
      const subscription = new AdjustPlayStoreSubscription(
        Number(getFirstParameterValue(params, 'revenue')),
        getFirstParameterValue(params, 'currency')!,
        getFirstParameterValue(params, 'productId')!,
        getFirstParameterValue(params, 'transactionId')!,
        getFirstParameterValue(params, 'receipt')!,
        getFirstParameterValue(params, 'purchaseToken')!
      );
      subscription.setPurchaseTime(Number(getFirstParameterValue(params, 'transactionDate')));

      if ('callbackParams' in params) {
        forEachPair(params['callbackParams'], (key, value) => subscription.addCallbackParameter(key, value));
      }
      if ('partnerParams' in params) {
        forEachPair(params['partnerParams'], (key, value) => subscription.addPartnerParameter(key, value));
      }

      this.adjust.trackPlayStoreSubscription(subscription);
    }
  }

  private trackAdRevenue(params: CommandParams): void {
    const source = getFirstParameterValue(params, 'adRevenueSource');
    // the test server can send a command without a source
    if (typeof source !== 'string') {
      return;
    }
    const adjustAdRevenue = new AdjustAdRevenue(source);

    if ('revenue' in params) {
      const [currency, revenue] = params['revenue'];
      adjustAdRevenue.setRevenue(parseFloat(revenue), currency);
    }

    if ('adImpressionsCount' in params) {
      adjustAdRevenue.setAdImpressionsCount(parseInt(getFirstParameterValue(params, 'adImpressionsCount')!, 10));
    }

    // the test server sends a missing value as the string 'null', which has to reach the SDK as null
    if ('adRevenueUnit' in params) {
      adjustAdRevenue.setAdRevenueUnit(nullIfNullString(getFirstParameterValue(params, 'adRevenueUnit')));
    }

    if ('adRevenuePlacement' in params) {
      adjustAdRevenue.setAdRevenuePlacement(nullIfNullString(getFirstParameterValue(params, 'adRevenuePlacement')));
    }

    if ('adRevenueNetwork' in params) {
      adjustAdRevenue.setAdRevenueNetwork(nullIfNullString(getFirstParameterValue(params, 'adRevenueNetwork')));
    }

    if ('callbackParams' in params) {
      forEachPair(params['callbackParams'], (key, value) => adjustAdRevenue.addCallbackParameter(key, value));
    }

    if ('partnerParams' in params) {
      forEachPair(params['partnerParams'], (key, value) => adjustAdRevenue.addPartnerParameter(key, value));
    }

    this.adjust.trackAdRevenue(adjustAdRevenue);
  }

  private getLastDeeplink(params: CommandParams): void {
    const testCallbackId = getFirstParameterValue(params, 'testCallbackId');
    this.adjust.getLastDeeplink().then((lastDeeplink) => {
      adjustTest().addInfoToSend('last_deeplink', lastDeeplink == null ? '' : lastDeeplink);
      adjustTest().addInfoToSend('test_callback_id', testCallbackId);
      adjustTest().sendInfoToServer(this.extraPath);
    });
  }

  private verifyPurchase(params: CommandParams): void {
    const productId = getFirstParameterValue(params, 'productId')!;
    if (isIos) {
      const purchase = new AdjustAppStorePurchase(productId, getFirstParameterValue(params, 'transactionId')!);
      this.adjust.verifyAppStorePurchase(purchase).then((result) => this.sendVerificationResult(result));
    } else if (isAndroid) {
      const purchase = new AdjustPlayStorePurchase(productId, getFirstParameterValue(params, 'purchaseToken')!);
      this.adjust.verifyPlayStorePurchase(purchase).then((result) => this.sendVerificationResult(result));
    }
  }

  private verifyTrack(params: CommandParams): void {
    this.event(params);
    const adjustEvent = this.savedEvents[getNumberSuffix(params, 'eventName')];
    if (isIos) {
      this.adjust.verifyAndTrackAppStorePurchase(adjustEvent).then((result) => this.sendVerificationResult(result));
    } else if (isAndroid) {
      this.adjust.verifyAndTrackPlayStorePurchase(adjustEvent).then((result) => this.sendVerificationResult(result));
    }

    delete this.savedEvents[0];
  }

  private processDeeplink(params: CommandParams): void {
    const adjustDeeplink = buildDeeplink(params);
    if (!adjustDeeplink) {
      return;
    }
    this.adjust.processAndResolveDeeplink(adjustDeeplink).then((resolvedLink) => {
      adjustTest().addInfoToSend('resolved_link', resolvedLink);
      adjustTest().sendInfoToServer(this.extraPath);
    });
  }

  private attributionGetter(params: CommandParams): void {
    const testCallbackId = getFirstParameterValue(params, 'testCallbackId');
    this.adjust.getAttribution().then((attribution) => {
      if (attribution == null) {
        return;
      }
      this.addAttributionInfo(attribution, attribution.costAmount.toString());
      adjustTest().addInfoToSend('test_callback_id', testCallbackId);
      adjustTest().sendInfoToServer(this.extraPath);
    });
  }

  private attributionGetterWithTimeout(params: CommandParams): void {
    const timeout = parseInt(getFirstParameterValue(params, 'timeout')!, 10);
    const testCallbackId = getFirstParameterValue(params, 'testCallbackId');
    this.adjust.getAttributionWithTimeout(timeout).then((attribution) => {
      if (attribution != null) {
        this.addAttributionInfo(attribution, attribution.costAmount.toString());
      } else {
        adjustTest().addInfoToSend('attribution', missingValue());
      }
      adjustTest().addInfoToSend('test_callback_id', testCallbackId);
      adjustTest().sendInfoToServer(this.extraPath);
    });
  }

  private endFirstSessionDelay(): void {
    this.adjust.endFirstSessionDelay();
  }

  private coppaComplianceInDelay(params: CommandParams): void {
    if (getFirstParameterValue(params, 'isEnabled') === 'true') {
      this.adjust.enableCoppaComplianceInDelay();
    } else {
      this.adjust.disableCoppaComplianceInDelay();
    }
  }

  private playStoreKidsComplianceInDelay(params: CommandParams): void {
    if (getFirstParameterValue(params, 'isEnabled') === 'true') {
      this.adjust.enablePlayStoreKidsComplianceInDelay();
    } else {
      this.adjust.disablePlayStoreKidsComplianceInDelay();
    }
  }

  private externalDeviceIdInDelay(params: CommandParams): void {
    this.adjust.setExternalDeviceIdInDelay(getFirstParameterValue(params, 'externalDeviceId')!);
  }

  private adidGetter(params: CommandParams): void {
    const testCallbackId = getFirstParameterValue(params, 'testCallbackId');
    this.adjust.getAdid().then((adid) => {
      adjustTest().addInfoToSend('adid', adid);
      adjustTest().addInfoToSend('test_callback_id', testCallbackId);
      adjustTest().sendInfoToServer(this.extraPath);
    });
  }

  private adidGetterWithTimeout(params: CommandParams): void {
    const timeout = parseInt(getFirstParameterValue(params, 'timeout')!, 10);
    const testCallbackId = getFirstParameterValue(params, 'testCallbackId');
    this.adjust.getAdidWithTimeout(timeout).then((adid) => {
      adjustTest().addInfoToSend('adid', adid != null ? adid : missingValue());
      adjustTest().addInfoToSend('test_callback_id', testCallbackId);
      adjustTest().sendInfoToServer(this.extraPath);
    });
  }

  private tpsSettingsGetter(params: CommandParams): void {
    const timeout = parseInt(getFirstParameterValue(params, 'timeout')!, 10);
    const testCallbackId = getFirstParameterValue(params, 'testCallbackId');
    this.adjust.getThirdPartySharingSettingsWithTimeout(timeout).then((thirdPartySharingSettingsJson) => {
      adjustTest().addInfoToSend(
        'third_party_sharing',
        thirdPartySharingSettingsJson != null ? thirdPartySharingSettingsJson : missingValue()
      );
      adjustTest().addInfoToSend('test_callback_id', testCallbackId);
      adjustTest().sendInfoToServer(this.extraPath);
    });
  }

  private idfaGetter(params: CommandParams): void {
    if (!isIos) {
      console.log('[Adjust]: Error! IDFA is not available on this platform.');
      return;
    }
    const testCallbackId = getFirstParameterValue(params, 'testCallbackId');
    this.adjust.getIdfa().then((idfa) => {
      adjustTest().addInfoToSend('idfa', idfa);
      adjustTest().addInfoToSend('test_callback_id', testCallbackId);
      adjustTest().sendInfoToServer(this.extraPath);
    });
  }

  private idfvGetter(params: CommandParams): void {
    if (!isIos) {
      console.log('[Adjust]: Error! IDFV is not available on this platform.');
      return;
    }
    const testCallbackId = getFirstParameterValue(params, 'testCallbackId');
    this.adjust.getIdfv().then((idfv) => {
      adjustTest().addInfoToSend('idfv', idfv);
      adjustTest().addInfoToSend('test_callback_id', testCallbackId);
      adjustTest().sendInfoToServer(this.extraPath);
    });
  }

  private googleAdIdGetter(params: CommandParams): void {
    if (!isAndroid) {
      console.log('[Adjust]: Error! Google Advertising ID is not available on this platform.');
      return;
    }
    const testCallbackId = getFirstParameterValue(params, 'testCallbackId');
    this.adjust.getGoogleAdId().then((googleAdId) => {
      adjustTest().addInfoToSend('gps_adid', googleAdId);
      adjustTest().addInfoToSend('test_callback_id', testCallbackId);
      adjustTest().sendInfoToServer(this.extraPath);
    });
  }

  private amazonAdIdGetter(params: CommandParams): void {
    if (!isAndroid) {
      console.log('[Adjust]: Error! Amazon Fire Advertising ID is not available on this platform.');
      return;
    }
    const testCallbackId = getFirstParameterValue(params, 'testCallbackId');
    this.adjust.getAmazonAdId().then((amazonAdId) => {
      adjustTest().addInfoToSend('fire_adid', amazonAdId);
      adjustTest().addInfoToSend('test_callback_id', testCallbackId);
      adjustTest().sendInfoToServer(this.extraPath);
    });
  }

  private sdkVersionGetter(params: CommandParams): void {
    const testCallbackId = getFirstParameterValue(params, 'testCallbackId');
    this.adjust.getSdkVersion().then((sdkVersion) => {
      adjustTest().addInfoToSend('sdk_version', sdkVersion);
      adjustTest().addInfoToSend('test_callback_id', testCallbackId);
      adjustTest().sendInfoToServer(this.extraPath);
    });
  }

  // Queues every attribution field for the test server. The cost amount is passed in because the
  // attribution callback reports it as it arrives, while the getters report it as a string.
  private addAttributionInfo(attribution: AdjustAttribution, costAmount: unknown): void {
    adjustTest().addInfoToSend('tracker_token', attribution.trackerToken);
    adjustTest().addInfoToSend('tracker_name', attribution.trackerName);
    adjustTest().addInfoToSend('network', attribution.network);
    adjustTest().addInfoToSend('campaign', attribution.campaign);
    adjustTest().addInfoToSend('adgroup', attribution.adgroup);
    adjustTest().addInfoToSend('creative', attribution.creative);
    adjustTest().addInfoToSend('click_label', attribution.clickLabel);
    adjustTest().addInfoToSend('cost_type', attribution.costType);
    adjustTest().addInfoToSend('cost_amount', costAmount);
    adjustTest().addInfoToSend('cost_currency', attribution.costCurrency);
    if (isAndroid) {
      adjustTest().addInfoToSend('fb_install_referrer', attribution.fbInstallReferrer);
    }

    // on iOS the test server expects the JSON response without fb_install_referrer
    let jsonResponse = attribution.jsonResponse;
    if (jsonResponse && isIos) {
      try {
        const json = JSON.parse(jsonResponse) as Record<string, unknown>;
        delete json['fb_install_referrer'];
        jsonResponse = JSON.stringify(json);
      } catch (e) {
        console.warn('Failed to parse attribution.jsonResponse:', e);
      }
    }
    adjustTest().addInfoToSend('json_response', jsonResponse);
  }

  private sendVerificationResult(result: AdjustPurchaseVerificationResult): void {
    adjustTest().addInfoToSend('verification_status', result.verificationStatus);
    adjustTest().addInfoToSend('code', result.code);
    adjustTest().addInfoToSend('message', result.message);
    adjustTest().sendInfoToServer(this.extraPath);
  }
}

// Util functions

function getFirstParameterValue(params: CommandParams, key: string): string | null {
  if (key in params) {
    const param = params[key];
    if (param != null && param.length >= 1) {
      return param[0];
    }
  }

  return null;
}

// Config and event names end with their number, for example 'config1'. No name means number 0.
function getNumberSuffix(params: CommandParams, key: string): number {
  const name = getFirstParameterValue(params, key);
  return name ? parseInt(name.charAt(name.length - 1), 10) : 0;
}

// Calls back with every two neighbouring values of a flat [key, value, key, value, ...] list.
function forEachPair(list: string[], callback: (key: string, value: string) => void): void {
  for (let i = 0; i < list.length; i += 2) {
    callback(list[i], list[i + 1]);
  }
}

function buildDeeplink(params: CommandParams): AdjustDeeplink | null {
  const deeplink = getFirstParameterValue(params, 'deeplink');
  if (typeof deeplink !== 'string') {
    return null;
  }
  const adjustDeeplink = new AdjustDeeplink(deeplink);
  const referrer = getFirstParameterValue(params, 'referrer');
  if (typeof referrer === 'string') {
    adjustDeeplink.setReferrer(referrer);
  }
  return adjustDeeplink;
}

// The wrapper types these setters as string, so a null has to be forced through.
function nullIfNullString(value: string | null): string {
  return (value === 'null' ? null : value) as string;
}

// How the test server expects a value that the SDK did not return in time to be reported.
function missingValue(): string {
  return isIos ? 'nil' : 'null';
}
