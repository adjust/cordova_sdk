import { Injectable } from '@angular/core';
import { Cordova, IonicNativePlugin, Plugin } from '@ionic-native/core';

export class AdjustEvent {
  private eventToken: string;
  private revenue: number;
  private currency: string;
  transactionId: string;
  callbackId: string;
  // // iOS only
  receipt: string;
  // // iOS only
  isReceiptSet = false;

  private callbackParameters: string[] = [];
  private partnerParameters: string[] = [];

  constructor(eventToken: string) {
    this.eventToken = eventToken;
  }

  setRevenue(revenue: number, currency: string): void {
    this.revenue = revenue;
    this.currency = currency;
  }

  addCallbackParameter(key: string, value: string): void {
    this.callbackParameters.push(key);
    this.callbackParameters.push(value);
  }

  addPartnerParameter(key: string, value: string): void {
    this.partnerParameters.push(key);
    this.partnerParameters.push(value);
  }
}

export class AdjustConfig {
  private appToken: string;
  private environment: AdjustEnvironment;
  sdkPrefix = 'cordova4.17.0';
  delayStart = 0.0;
  logLevel: AdjustLogLevel = null;
  referrer: string = null;
  userAgent: string = null;
  isDeviceKnown: boolean = null;
  defaultTracker: string = null;
  sendInBackground: boolean = null;
  shouldLaunchDeeplink: boolean = null;
  eventBufferingEnabled: boolean = null;
  // Android only
  processName: string = null;
  readMobileEquipmentIdentity: boolean = null;

  attributionCallback: (attribution: AdjustAttribution) => void = null;
  eventTrackingSucceededCallback: (event: AdjustEventSuccess) => void = null;
  eventTrackingFailedCallback: (event: AdjustEventFailure) => void = null;
  sessionTrackingSucceededCallback: (session: AdjustSessionSuccess) => void = null;
  sessionTrackingFailedCallback: (session: AdjustSessionFailure) => void = null;
  deferredDeeplinkCallback: (uri: string) => void = null;

  private secretId: number = null;
  private info1: number = null;
  private info2: number = null;
  private info3: number = null;
  private info4: number = null;

  constructor(appToken: string, environment: AdjustEnvironment) {
    this.appToken = appToken;
    this.environment = environment;
  }

  setAppSecret(secretId: number, info1: number, info2: number, info3: number, info4: number): void {
    this.secretId = secretId;
    this.info1 = info1;
    this.info2 = info2;
    this.info3 = info3;
    this.info4 = info4;
  }

  private getAttributionCallback() {
      return this.attributionCallback;
  }

  private getEventTrackingSucceededCallback() {
      return this.eventTrackingSucceededCallback;
  }

  private getEventTrackingFailedCallback() {
      return this.eventTrackingFailedCallback;
  }

  private getSessionTrackingSucceededCallback() {
      return this.sessionTrackingSucceededCallback;
  }

  private getSessionTrackingFailedCallback() {
      return this.sessionTrackingFailedCallback;
  }

  private getDeferredDeeplinkCallback() {
      return this.deferredDeeplinkCallback;
  }

  private hasAttributionListener() {
    return this.attributionCallback !== null;
  }

  private hasEventTrackingSucceededListener() {
    return this.eventTrackingSucceededCallback !== null;
  }

  private hasEventTrackingFailedListener() {
    return this.eventTrackingFailedCallback !== null;
  }

  private hasSessionTrackingSucceededListener() {
    return this.sessionTrackingSucceededCallback !== null;
  }

  private hasSessionTrackingFailedListener() {
    return this.sessionTrackingFailedCallback !== null;
  }

  private hasDeferredDeeplinkCallbackListener() {
    return this.deferredDeeplinkCallback !== null;
  }
}

export interface AdjustAttribution {
  trackerToken: string;
  trackerName: string;
  network: string;
  campaign: string;
  adgroup: string;
  creative: string;
  clickLabel: string;
  adid: string;
}

export interface AdjustSessionSuccess {
  message: string;
  timestamp: string;
  adid: string;
  jsonResponse: string;
}

export interface AdjustSessionFailure {
  message: string;
  timestamp: string;
  adid: string;
  willRetry: boolean;
  jsonResponse: string;
}

export interface AdjustEventSuccess {
  message: string;
  timestamp: string;
  adid: string;
  eventToken: string;
  jsonResponse: string;
}

export interface AdjustEventFailure {
  message: string;
  timestamp: string;
  adid: string;
  eventToken: string;
  willRetry: boolean;
  jsonResponse: string;
}

export enum AdjustEnvironment {
  Sandbox = 'sandbox',
  Production = 'production'
}

export enum AdjustLogLevel {
  Verbose = 'VERBOSE',
  Debug = 'DEBUG',
  Info = 'INFO',
  Warn = 'WARN',
  Error = 'ERROR',
  Assert = 'ASSERT',
  Suppress = 'SUPPRESS'
}

/**
 * @name Adjust
 * @description
 * This is the Ionic Cordova SDK of Adjust™. You can read more about Adjust™ at adjust.com.
 *
 * Requires Cordova plugin: `com.adjust.sdk`. For more info, please see the [Adjust Cordova SDK](https://github.com/adjust/cordova_sdk)
 *
 * @usage
 * ```typescript
 *  import { Adjust, AdjustConfig, AdjustEnvironment } from '@ionic-native/adjust';
 *
 *  constructor(private adjust: Adjust) { }
 *
 *  ...
 *
 *  const config = new AdjustConfig('APP-TOKEN-HERE', AdjustEnvironment.Sandbox);
 *  config.logLevel = AdjustLogLevel.Verbose;
 *  // set other config properties ...
 *  adjust.create(config);
 *  // ...
 *
 * ```
 * @interfaces
 * AdjustAttribution
 * AdjustSessionSuccess
 * AdjustSessionFailure
 * AdjustEventSuccess
 * AdjustEventFailure
 * @classes
 * AdjustEvent
 * AdjustConfig
 * @enums
 * AdjustEnvironment
 * AdjustLogLevel
 */
@Plugin({
  pluginName: 'Adjust',
  plugin: 'com.adjust.sdk', // npm package name, example: cordova-plugin-camera
  pluginRef: 'Adjust', // the variable reference to call the plugin, example: navigator.geolocation (refers to the where on window the underlying Cordova plugin is normally exposed)
  repo: 'https://github.com/adjust/cordova_sdk', // the github repository URL for the plugin
  platforms: ['Android', 'iOS'] // Array of platforms supported, example: ['Android', 'iOS']
})
@Injectable()
export class Adjust extends IonicNativePlugin {

  /**
   * This method initializes Adjust SDK
   * @param {AdjustConig} config Adjust config object used as starting options
   */
  @Cordova({ sync: true })
  create(config: AdjustConfig): void {}

  /**
   * This method tracks an event
   * @param {AdjustEvent} event Adjust event object to be tracked
   */
  @Cordova({ sync: true })
  trackEvent(event: AdjustEvent): void {}

  /**
   * This method sets offline mode on or off
   * @param {boolean} enabled set to true for offline mode on
   */
  @Cordova({ sync: true })
  setOfflineMode(enabled: boolean): void {}

  /**
   * By making this call, the Adjust SDK will try to find if there is any new attribution info inside of the deep link and if any, it will be sent to the Adjust backend.
   * @param {string} url URL of the deeplink
   */
  @Cordova({ sync: true })
  appWillOpenUrl(url: string): void {}

  /**
   * You can disable/enable the Adjust SDK from tracking by invoking this method
   * @param {boolean} enabled set to false to disable SDK
   */
  @Cordova({ sync: true })
  setEnabled(enabled: boolean): void {}

  @Cordova({ sync: true })
  setPushToken(pushToken: string): void {}

  @Cordova({ sync: true })
  setReferrer(referrer: string): void {}

  @Cordova()
  isEnabled(): Promise<boolean> { return; }

  @Cordova({ sync: true })
  gdprForgetMe(): void {}

  /**
   * Function used to get Google AdId
   * @return {Promise<string>} Returns a promise with google AdId value
   */
  @Cordova()
  getGoogleAdId(): Promise<string> { return; }

  @Cordova()
  getAmazonAdId(): Promise<string> { return; }

  @Cordova()
  getIdfa(): Promise<string> { return; }

  @Cordova()
  getAdid(): Promise<string> { return; }

  @Cordova()
  getAttribution(): Promise<AdjustAttribution> { return; }

  @Cordova({ sync: true })
  addSessionCallbackParameter(key: string, value: string): void {}

  @Cordova({ sync: true })
  removeSessionCallbackParameter(key: string): void {}

  @Cordova({ sync: true })
  resetSessionCallbackParameters(): void {}

  @Cordova({ sync: true })
  addSessionPartnerParameter(key: string, value: string): void {}

  @Cordova({ sync: true })
  removeSessionPartnerParameter(key: string): void {}

  @Cordova({ sync: true })
  resetSessionPartnerParameters(): void {}

  @Cordova({ sync: true })
  sendFirstPackages(): void {}

  // not gonna be needed until we have a testapp for ionic ////////////////
  // @Cordova({ sync: true })
  // setTestOptions(testOptions: any): void {}
  // @Cordova({ sync: true })
  // teardown(testParam: string): void {}
  // @Cordova({ sync: true })
  // onResume(testParam: string): void {}
  // @Cordova({ sync: true })
  // onPause(testParam: string): void {}
}
