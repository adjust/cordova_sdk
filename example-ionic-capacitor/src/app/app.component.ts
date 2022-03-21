import { Component } from '@angular/core';
import { Adjust, AdjustConfig, AdjustEnvironment, AdjustLogLevel } from '@awesome-cordova-plugins/adjust/ngx';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.scss'],
})
export class AppComponent {
  constructor(private adjust: Adjust) {
    const config = new AdjustConfig('2fm9gkqubvpc', AdjustEnvironment.Sandbox);
    config.setLogLevel(AdjustLogLevel.Verbose);
    this.adjust.create(config);
  }
}
