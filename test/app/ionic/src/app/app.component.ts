import { Component, signal } from '@angular/core';
import { Capacitor } from '@capacitor/core';
import { IonApp, IonContent, IonHeader, IonTitle, IonToolbar } from '@ionic/angular';
import { Adjust } from '@awesome-cordova-plugins/adjust/ngx';

import { CommandExecutor, CommandParams } from './command-executor';
import { adjustTest } from './test-plugin';

// address of the machine that runs the test server
const TEST_SERVER_IP = '192.168.21.141';

interface TestCommand {
  className: string;
  functionName: string;
  params: CommandParams;
  order: number;
}

@Component({
  selector: 'app-root',
  imports: [IonApp, IonContent, IonHeader, IonTitle, IonToolbar],
  template: `
    <ion-app>
      <ion-header>
        <ion-toolbar>
          <ion-title>Adjust Ionic Test App</ion-title>
        </ion-toolbar>
      </ion-header>
      <ion-content class="ion-padding">
        <p>{{ status() }}</p>
      </ion-content>
    </ion-app>
  `,
})
export class AppComponent {
  protected readonly status = signal('Waiting for the device to be ready ...');

  constructor(private adjust: Adjust) {
    // the plugins can be called once Cordova reports that the device is ready
    document.addEventListener('deviceready', () => this.startTestSession(), false);
  }

  private startTestSession(): void {
    this.adjust.getSdkVersion().then((sdkVersion) => {
      const baseUrl =
        Capacitor.getPlatform() === 'android' ? `https://${TEST_SERVER_IP}:8443` : `http://${TEST_SERVER_IP}:8080`;
      const controlUrl = `ws://${TEST_SERVER_IP}:1987`;

      const commandExecutor = new CommandExecutor(this.adjust, baseUrl);
      // adjustTest().addTestDirectory('purchase-verification');

      this.status.set(`Test session started against ${TEST_SERVER_IP} with ${sdkVersion}.`);
      adjustTest().startTestSession(baseUrl, controlUrl, sdkVersion ?? '', (json) => {
        const command = JSON.parse(json) as TestCommand;
        commandExecutor.scheduleCommand(command.className, command.functionName, command.params, command.order);
      });
    });
  }
}
