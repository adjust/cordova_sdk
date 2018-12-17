import { Component } from '@angular/core';
import { NavController, AlertController } from 'ionic-angular';
import { Adjust, AdjustEvent } from '@ionic-native/adjust';

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  constructor(public alertCtrl: AlertController, public navCtrl: NavController, public adjust: Adjust) {

  }

  trackSimpleEvent() {
    const adjustEvent: AdjustEvent = new AdjustEvent('g3mfiw');
    this.adjust.trackEvent(adjustEvent);
  }

  trackRevenueEvent() {
    const adjustEvent: AdjustEvent = new AdjustEvent('a4fd35');
    adjustEvent.setRevenue(0.01, 'USD');
    this.adjust.trackEvent(adjustEvent);
  }

  trackCallbackEvent() {
    const adjustEvent: AdjustEvent = new AdjustEvent('34vgg9');
    adjustEvent.addCallbackParameter('key', 'stuff');
    adjustEvent.addCallbackParameter('x', 'y');
    adjustEvent.addCallbackParameter('key', 'lock');
    this.adjust.trackEvent(adjustEvent);
  }

  trackPartnerEvent() {
    const adjustEvent: AdjustEvent = new AdjustEvent('w788qs');
    adjustEvent.addPartnerParameter('foo', 'bar');
    adjustEvent.addPartnerParameter('x', 'y');
    adjustEvent.addPartnerParameter('foo', 'foot');
    adjustEvent.addPartnerParameter('x', 'z');
    this.adjust.trackEvent(adjustEvent);
  }

  enableOfflineMode() {
    this.adjust.setOfflineMode(true);
  }

  disableOfflineMode() {
    this.adjust.setOfflineMode(false);
  }

  enableSdk() {
    this.adjust.setEnabled(true);
  }

  disableSdk() {
    this.adjust.setEnabled(false);
  }

  isSdkEnabled() {
    this.adjust.isEnabled().then((isEnabled) => {
      let alert = this.alertCtrl.create({
        title: 'Adjust - Is SDK Enabled?',
        subTitle: 'SDK enabled = ' + isEnabled,
        buttons: ['OK']
      });
      alert.present();
    });
  }

  getIds() {
    this.adjust.getIdfa().then((idfa) => {
      console.log("IDFA = " + idfa);
    });

    this.adjust.getGoogleAdId().then((gpsAdId) => {
        console.log("Google Ad Id = " + gpsAdId);
    });

    this.adjust.getAmazonAdId().then((gpsAdId) => {
        console.log("Amazon Ad Id = " + gpsAdId);
    });

    this.adjust.getAdid().then((adid) => {
        console.log("Adid = " + adid);
    });
  }

  gdprForgetMe() {
    this.adjust.gdprForgetMe();
  }

  getAttribution() {
    this.adjust.getAttribution().then((att) => {
      let alert = this.alertCtrl.create({
        title: 'Adjust Attribution',
        subTitle: JSON.stringify(att),
        buttons: ['OK']
      });
      alert.present();
    });
  }

  getSdkVersion() {
    this.adjust.getSdkVersion().then((sdkVersion) => {
      let alert = this.alertCtrl.create({
        title: 'SDK Version',
        subTitle: sdkVersion,
        buttons: ['OK']
      });
      alert.present();
    });
  }
}
