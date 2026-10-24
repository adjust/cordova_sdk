# Adjust SDK for Cordova

This is the [Adjust](https://adjust.com)™  SDK for Cordova. Documentation is available on our [developer docs](https://dev.adjust.com/en/sdk/cordova/?version=v5).

## Google On-device Conversion Measurement (iOS)

To enable [Google ODM](https://dev.adjust.com/en/sdk/ios/plugins/google-odm), set the `IOS_GOOGLE_ODM` plugin variable
to `true`. The `Adjust` pod is then replaced with the `Adjust/AdjustGoogleOdm` subspec. No code change is needed.

The Adjust ODM plugin relies on Google's `GoogleAdsOnDeviceConversion` framework:

- If your app uses Google Analytics for Firebase 11.14.0 or later, Firebase already installs it.
- Otherwise, also set `IOS_GOOGLE_ADS_ON_DEVICE_CONVERSION` to `true` so that the plugin installs it.

```sh
cordova plugin add com.adjust.sdk --variable IOS_GOOGLE_ODM=true
```

Since Cordova does not support using plugin variables to select pods, these variables are applied to the plugin's
`plugin.xml` by an [npm post-install script](scripts/post_install.js), before Cordova parses it. The first time you set
one of them, the script runs before Cordova saves the variable in your project, so reinstall the plugin to apply it:

```sh
cordova plugin rm com.adjust.sdk --nosave && cordova plugin add com.adjust.sdk --nosave
```

ODM attribution depends on the launch time recorded when the SDK is initialized, so call `Adjust.initSdk()` as early as
possible. If your app has to wait before sending data, for example for the App Tracking Transparency prompt, use
`AdjustConfig.enableFirstSessionDelay()` and `Adjust.endFirstSessionDelay()` rather than delaying the initialization.

## License

Distributed under the [`MIT license`](LICENSE).
