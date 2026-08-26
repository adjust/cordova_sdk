import { Component } from '@angular/core';
import { Adjust, AdjustConfig, AdjustEnvironment, AdjustLogLevel, AdjustEvent } from '@awesome-cordova-plugins/adjust/ngx';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
})
export class AppComponent {
  constructor(private adjust: Adjust) {
    const config = new AdjustConfig('2fm9gkqubvpc', AdjustEnvironment.Sandbox);
    config.setLogLevel(AdjustLogLevel.Verbose);

    config.setAttributionCallback((attribution) => {
      console.log('[AdjustExample]: Attribution callback received.');
      console.log('[AdjustExample]: Tracker token = ' + attribution.trackerToken);
      console.log('[AdjustExample]: Tracker name = ' + attribution.trackerName);
      console.log('[AdjustExample]: Network = ' + attribution.network);
      console.log('[AdjustExample]: Campaign = ' + attribution.campaign);
      console.log('[AdjustExample]: Adgroup = ' + attribution.adgroup);
      console.log('[AdjustExample]: Creative = ' + attribution.creative);
      console.log('[AdjustExample]: Click label = ' + attribution.clickLabel);
      console.log('[AdjustExample]: Adid = ' + attribution.adid);
    });

    config.setEventTrackingSucceededCallback((eventSuccess) => {
      console.log('[AdjustExample]: Event tracking succeeded callback received.');
      console.log('[AdjustExample]: Message: ' + eventSuccess.message);
      console.log('[AdjustExample]: Timestamp: ' + eventSuccess.timestamp);
      console.log('[AdjustExample]: Adid: ' + eventSuccess.adid);
      console.log('[AdjustExample]: Event token: ' + eventSuccess.eventToken);
      console.log('[AdjustExample]: Callback Id: ' + eventSuccess.callbackId);
      console.log('[AdjustExample]: JSON response: ' + eventSuccess.jsonResponse);
    });

    config.setEventTrackingFailedCallback((eventFailed) => {
      console.log('[AdjustExample]: Event tracking failed callback received.');
      console.log('[AdjustExample]: Message: ' + eventFailed.message);
      console.log('[AdjustExample]: Timestamp: ' + eventFailed.timestamp);
      console.log('[AdjustExample]: Adid: ' + eventFailed.adid);
      console.log('[AdjustExample]: Event token: ' + eventFailed.eventToken);
      console.log('[AdjustExample]: Will retry: ' + eventFailed.willRetry);
      console.log('[AdjustExample]: Callback Id: ' + eventFailed.callbackId);
      console.log('[AdjustExample]: JSON response: ' + eventFailed.jsonResponse);
    });

    config.setSessionTrackingSucceededCallback((sessionSuccess) => {
      console.log('[AdjustExample]: Session tracking succeeded callback received.');
      console.log('[AdjustExample]: Message: ' + sessionSuccess.message);
      console.log('[AdjustExample]: Timestamp: ' + sessionSuccess.timestamp);
      console.log('[AdjustExample]: Adid: ' + sessionSuccess.adid);
      console.log('[AdjustExample]: JSON response: ' + sessionSuccess.jsonResponse);
    });

    config.setSessionTrackingFailedCallback((sessionFailed) => {
      console.log('[AdjustExample]: Session tracking failed callback received.');
      console.log('[AdjustExample]: Message: ' + sessionFailed.message);
      console.log('[AdjustExample]: Timestamp: ' + sessionFailed.timestamp);
      console.log('[AdjustExample]: Adid: ' + sessionFailed.adid);
      console.log('[AdjustExample]: Will retry: ' + sessionFailed.willRetry);
      console.log('[AdjustExample]: JSON response: ' + sessionFailed.jsonResponse);
    });

    config.setDeferredDeeplinkCallback((uri) => {
      console.log('[AdjustExample]: Deferred Deeplink Callback received.');
      console.log('[AdjustExample]: URL: ' + uri);
    });

    this.adjust.initSdk(config);
  }

  trackSimpleEvent(): void {
    const adjustEvent = new AdjustEvent('g3mfiw');
    this.adjust.trackEvent(adjustEvent);
  }

  trackRevenueEvent(): void {
    const adjustEvent = new AdjustEvent('a4fd35');
    adjustEvent.setRevenue(0.01, 'USD');
    adjustEvent.setTransactionId('dummy_id');
    this.adjust.trackEvent(adjustEvent);
  }

  trackCallbackEvent(): void {
    const adjustEvent = new AdjustEvent('34vgg9');
    adjustEvent.addCallbackParameter('key', 'stuff');
    adjustEvent.addCallbackParameter('x', 'y');
    adjustEvent.addCallbackParameter('key', 'lock');
    this.adjust.trackEvent(adjustEvent);
  }

  trackPartnerEvent(): void {
    const adjustEvent = new AdjustEvent('w788qs');
    adjustEvent.addPartnerParameter('foo', 'bar');
    adjustEvent.addPartnerParameter('x', 'y');
    adjustEvent.addPartnerParameter('foo', 'foot');
    adjustEvent.addPartnerParameter('x', 'z');
    this.adjust.trackEvent(adjustEvent);
  }

  enableOfflineMode(): void {
    this.adjust.switchToOfflineMode();
  }

  disableOfflineMode(): void {
    this.adjust.switchBackToOnlineMode();
  }

  enableSdk(): void {
    this.adjust.enable();
  }

  disableSdk(): void {
    this.adjust.disable();
  }

  isSdkEnabled(): void {
    this.adjust.isEnabled().then((isEnabled) => {
      window.alert(isEnabled ? 'Yes, it is enabled.' : 'No, it is not enabled.');
    });
  }

  getIds(): void {
    this.adjust.getIdfa().then((idfa) => {
      console.log('[AdjustExample]: IDFA = ' + idfa);
    });

    this.adjust.getIdfv().then((idfv) => {
      console.log('[AdjustExample]: IDFV = ' + idfv);
    });

    this.adjust.getGoogleAdId().then((gpsAdId) => {
      console.log('[AdjustExample]: Google Ad Id = ' + gpsAdId);
    });

    this.adjust.getAmazonAdId().then((amazonAdId) => {
      console.log('[AdjustExample]: Amazon Ad Id = ' + amazonAdId);
    });

    this.adjust.getAdid().then((adid) => {
      console.log('[AdjustExample]: Adjust Id = ' + adid);
    });

    this.adjust.getAttribution().then((attribution) => {
      if (!attribution) {
        return;
      }
      console.log('[AdjustExample]: Tracker token = ' + attribution.trackerToken);
      console.log('[AdjustExample]: Tracker name = ' + attribution.trackerName);
      console.log('[AdjustExample]: Network = ' + attribution.network);
      console.log('[AdjustExample]: Campaign = ' + attribution.campaign);
      console.log('[AdjustExample]: Adgroup = ' + attribution.adgroup);
      console.log('[AdjustExample]: Creative = ' + attribution.creative);
      console.log('[AdjustExample]: Click label = ' + attribution.clickLabel);
      console.log('[AdjustExample]: Cost Type = ' + attribution.costType);
      console.log('[AdjustExample]: Cost Amount = ' + attribution.costAmount);
      console.log('[AdjustExample]: Cost Currency = ' + attribution.costCurrency);
    });

    this.adjust.requestAppTrackingAuthorization().then((status) => {
      console.log('[AdjustExample]: ATT status = ' + status);
    });

    this.adjust.getAppTrackingAuthorizationStatus().then((status) => {
      console.log('[AdjustExample]: ATT status = ' + status);
    });
  }

  getSdkVersion(): void {
    this.adjust.getSdkVersion().then((sdkVersion) => {
      window.alert(sdkVersion ?? 'unknown');
    });
  }
}
