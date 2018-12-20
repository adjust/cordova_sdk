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
      config.attributionCallback = (attribution: AdjustAttribution) => {
        console.log("### Attribution callback received");

        console.log("Tracker token = " + attribution.trackerToken);
        console.log("Tracker name = " + attribution.trackerName);
        console.log("Network = " + attribution.network);
        console.log("Campaign = " + attribution.campaign);
        console.log("Adgroup = " + attribution.adgroup);
        console.log("Creative = " + attribution.creative);
        console.log("Click label = " + attribution.clickLabel);
        console.log("Adid = " + attribution.adid);
      }
      config.eventTrackingFailedCallback = (eventFailed: AdjustEventFailure) => {
        console.log("### Event tracking failed callback received");

        console.log("Message: " + eventFailed.message);
        console.log("Timestamp: " + eventFailed.timestamp);
        console.log("Adid: " + eventFailed.adid);
        console.log("Event token: " + eventFailed.eventToken);
        console.log("Will retry: " + eventFailed.willRetry);
        console.log("JSON response: " + eventFailed.jsonResponse);
      }
      config.eventTrackingSucceededCallback = (eventSuccess: AdjustEventSuccess) => {
        console.log("### Event tracking succeeded callback received");

        console.log("Message: " + eventSuccess.message);
        console.log("Timestamp: " + eventSuccess.timestamp);
        console.log("Adid: " + eventSuccess.adid);
        console.log("Event token: " + eventSuccess.eventToken);
        console.log("JSON response: " + eventSuccess.jsonResponse);
      }
      config.sessionTrackingSucceededCallback = (sessionSuccess: AdjustSessionSuccess) => {
        console.log("### Session tracking succeeded callback received");

        console.log("Message: " + sessionSuccess.message);
        console.log("Timestamp: " + sessionSuccess.timestamp);
        console.log("Adid: " + sessionSuccess.adid);
        console.log("JSON response: " + sessionSuccess.jsonResponse);
      }
      config.sessionTrackingFailedCallback = (sessionFailed: AdjustSessionFailure) => {
        console.log("### Session tracking failed callback received");

        console.log("Message: " + sessionFailed.message);
        console.log("Timestamp: " + sessionFailed.timestamp);
        console.log("Adid: " + sessionFailed.adid);
        console.log("Will retry: " + sessionFailed.willRetry);
        console.log("JSON response: " + sessionFailed.jsonResponse);
      }
      config.deferredDeeplinkCallback = (uri: string) => {
        console.log("### Deferred Deeplink Callback received");

        console.log("URL: " + uri);
      }

      adjust.create(config);
    });
  }
}

