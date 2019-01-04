import { Component } from '@angular/core';
import { Platform } from 'ionic-angular';
import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { Adjust, AdjustConfig, AdjustAttribution, AdjustLogLevel, AdjustEnvironment, AdjustEventFailure, AdjustEventSuccess, AdjustSessionSuccess, AdjustSessionFailure } from '@ionic-native/adjust';

import { HomePage } from '../pages/home/home';
@Component({
  templateUrl: 'app.html'
})
export class MyApp {
  rootPage:any = HomePage;

  constructor(platform: Platform, adjust: Adjust, statusBar: StatusBar, splashScreen: SplashScreen) {
    platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      statusBar.styleDefault();
      splashScreen.hide();

      const config = new AdjustConfig('2fm9gkqubvpc', AdjustEnvironment.Sandbox);
      config.setLogLevel(AdjustLogLevel.Verbose);
      config.setShouldLaunchDeeplink(true);
      config.setAttributionCallback((attribution: AdjustAttribution) => {
        console.log("[AdjustExample]: Attribution callback received.");
        console.log("[AdjustExample]: Tracker token = " + attribution.trackerToken);
        console.log("[AdjustExample]: Tracker name = " + attribution.trackerName);
        console.log("[AdjustExample]: Network = " + attribution.network);
        console.log("[AdjustExample]: Campaign = " + attribution.campaign);
        console.log("[AdjustExample]: Adgroup = " + attribution.adgroup);
        console.log("[AdjustExample]: Creative = " + attribution.creative);
        console.log("[AdjustExample]: Click label = " + attribution.clickLabel);
        console.log("[AdjustExample]: Adid = " + attribution.adid);
      });
      config.setEventTrackingFailedCallback((eventFailed: AdjustEventFailure) => {
        console.log("[AdjustExample]: Event tracking failed callback received.");
        console.log("[AdjustExample]: Message: " + eventFailed.message);
        console.log("[AdjustExample]: Timestamp: " + eventFailed.timestamp);
        console.log("[AdjustExample]: Adid: " + eventFailed.adid);
        console.log("[AdjustExample]: Event token: " + eventFailed.eventToken);
        console.log("[AdjustExample]: Will retry: " + eventFailed.willRetry);
        console.log("[AdjustExample]: Callback Id: " + eventFailed.callbackId);
        console.log("[AdjustExample]: JSON response: " + eventFailed.jsonResponse);
      });
      config.setEventTrackingSucceededCallback((eventSuccess: AdjustEventSuccess) => {
        console.log("[AdjustExample]: Event tracking succeeded callback received.");
        console.log("[AdjustExample]: Message: " + eventSuccess.message);
        console.log("[AdjustExample]: Timestamp: " + eventSuccess.timestamp);
        console.log("[AdjustExample]: Adid: " + eventSuccess.adid);
        console.log("[AdjustExample]: Event token: " + eventSuccess.eventToken);
        console.log("[AdjustExample]: Callback Id: " + eventSuccess.callbackId);
        console.log("[AdjustExample]: JSON response: " + eventSuccess.jsonResponse);
      });
      config.setSessionTrackingSucceededCallback((sessionSuccess: AdjustSessionSuccess) => {
        console.log("[AdjustExample]: Session tracking succeeded callback received.");
        console.log("[AdjustExample]: Message: " + sessionSuccess.message);
        console.log("[AdjustExample]: Timestamp: " + sessionSuccess.timestamp);
        console.log("[AdjustExample]: Adid: " + sessionSuccess.adid);
        console.log("[AdjustExample]: JSON response: " + sessionSuccess.jsonResponse);
      });
      config.setSessionTrackingFailedCallback((sessionFailed: AdjustSessionFailure) => {
        console.log("[AdjustExample]: Session tracking failed callback received.");
        console.log("[AdjustExample]: Message: " + sessionFailed.message);
        console.log("[AdjustExample]: Timestamp: " + sessionFailed.timestamp);
        console.log("[AdjustExample]: Adid: " + sessionFailed.adid);
        console.log("[AdjustExample]: Will retry: " + sessionFailed.willRetry);
        console.log("[AdjustExample]: JSON response: " + sessionFailed.jsonResponse);
      });
      config.setDeferredDeeplinkCallback((uri: string) => {
        console.log("[AdjustExample]: Deferred Deeplink Callback received.");
        console.log("[AdjustExample]: URL: " + uri);
      });

      adjust.create(config);
    });
  }
}

