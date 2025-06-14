### Version 5.4.1 (10th June 2025)
#### Changed
- Removed `deployment-target` iOS preference from `plugin.xml` (https://github.com/adjust/cordova_sdk/pull/198).

#### Native SDKs
- [iOS@v5.4.0][ios_sdk_v5.4.0]
- [Android@v5.4.0][android_sdk_v5.4.0]

---

### Version 5.4.0 (4th June 2025)
#### Added
- Added support for configuring store information via the `AdjustStoreInfo` object. You can now specify the store name and store app ID via `setStoreInfo` method on your `AdjustConfig` instance. This enables the SDK to record the intended app store source during initialization. For more details about this feature, refer to the [official documentation](https://dev.adjust.com/en/sdk/cordova/setup/store-type).
- Added ability to initialize the SDK for the first session in delayed mode. You can start the SDK in the delayed mode by calling the `enableFirstSessionDelay` method on your `AdjustConfig` instance. To end the delay, make sure to call `endFirstSessionDelay` method of `Adjust` instance. For more details about this feature, refer to the [official documentation](https://dev.adjust.com/en/sdk/cordova/features/first-session-delay).
- Added ability to send organic search referrer together with deep link. You can send it via `setReferrer` method of the `AdjustDeeplink` instance. For more details about this feature, refer to the [official documentation](https://dev.adjust.com/en/sdk/cordova/features/deep-links#handling-deeplinks-with-referrer).
- Added ability to disable SDK's interaction with `AppTrackingTransparency.framework` API. You can disable it by calling the `disableAppTrackingTransparencyUsage` method on your `AdjustConfig` instance. For more details about this feature, refer to the [official documentation](https://dev.adjust.com/en/sdk/cordova/features/att#disable-att-framework).
- Added `jsonResponse` to attribution which represents the JSON string sent by the backend as part of the attribution response.

#### Native SDKs
- [iOS@v5.4.0][ios_sdk_v5.4.0]
- [Android@v5.4.0][android_sdk_v5.4.0]

---

### Version 5.0.4 (5th February 2025)
#### Added
- Added sending of the additional parameter to improve troubleshooting of `SKAdNetwork` related issues.

#### Fixed
- Fixed occasional occurrences in which ATT waiting interval timer was not being started.
- Fixed occasional NPE occurrences when app was entering background (https://github.com/adjust/android_sdk/issues/630).

#### Native SDKs
- [iOS@v5.1.0][ios_sdk_v5.1.0]
- [Android@v5.1.0][android_sdk_v5.1.0]

---

### Version 5.0.3 (6th December 2024)
#### Changed
- Switched to native Android SDK version that depends on a specific version of the signature library.

#### Native SDKs
- [iOS@v5.0.1][ios_sdk_v5.0.1]
- [Android@v5.0.2][android_sdk_v5.0.2]

---

### Version 5.0.2 (22nd November 2024)
#### Fixed
- Removed platform detection logic from JS layer which was requiring `cordova-plugin-device` dependency.

#### Native SDKs
- [iOS@v5.0.1][ios_sdk_v5.0.1]
- [Android@v5.0.1][android_sdk_v5.0.1]

---

### Version 5.0.1 (23rd September 2024)
#### Fixed
- Fixed errors from `index.ts` (https://github.com/adjust/cordova_sdk/issues/190).
- Fixed occasional ANRs while reading install referrer from Shared Preferences during the SDK initialization.

#### Native SDKs
- [iOS@v5.0.1][ios_sdk_v5.0.1]
- [Android@v5.0.1][android_sdk_v5.0.1]

---

### Version 5.0.0 (14th September 2024)

We're excited to release our major new SDK version (v5). Among many internal improvements, our spoofing protection solution is now included out of the box, reinforcing our commitment to accurate, actionable, and fraud-free data.

To try out SDK v5 in your app, you can follow our new v4 to v5 [migration guide](https://dev.adjust.com/en/sdk/cordova/migration/v4-to-v5).

If you are a current Adjust client and have questions about SDK v5, please email [sdk-v5@adjust.com](mailto:sdk-v5@adjust.com).

In case you were using beta version of the SDK v5, please switch to the official v5 release.

#### Native SDKs
- [iOS@v5.0.1][ios_sdk_v5.0.1]
- [Android@v5.0.0][android_sdk_v5.0.0]

---

### Version 4.38.1 (30th April 2024)
#### Added
- Added sending of the additional iOS SDK observability parameters for debugging purposes.

#### Fixed
- Removed nullability type specifier warnings (https://github.com/adjust/ios_sdk/issues/703).

#### Native SDKs
- [iOS@v4.38.2][ios_sdk_v4.38.2]
- [Android@v4.38.3][android_sdk_v4.38.3]

---

### Version 4.38.0 (28th March 2024)
#### Added
- Added iOS Privacy Manifest for the Adjust SDK.
- Added new domains and corresponding payload restrictions for the Adjust SDK to direct the iOS traffic to:
    - https://consent.adjust.com - for consented users
    - https://analytics.adjust.com - for non-consented users

#### Native SDKs
- [iOS@v4.38.0][ios_sdk_v4.38.0]
- [Android@v4.38.3][android_sdk_v4.38.3]

---

### Version 4.37.1 (26th January 2024)
#### Added
- Added support for Meta install referrer.

#### Native SDKs
- [iOS@v4.37.0][ios_sdk_v4.37.0]
- [Android@v4.38.0][android_sdk_v4.38.0]

---

### Version 4.37.0 (26th January 2024)
#### Added
- Added ability to process shortened deep links and provide the unshortened link back as a response. You can achieve this by invoking `processDeeplink` method of the `Adjust` instance.
- Added getter for obtaining IDFV value of the iOS device. You can obtain IDFV value by calling `getIdfv` method of the `Adjust` instance.
- Added support for Google Play Games on PC.
- Added support for `TopOn` and `AD(X)` ad revenue tracking.
- Added a new type of URL strategy called `AdjustConfig.AdjustUrlStrategyCnOnly`. This URL strategy represents `AdjustConfig.AdjustUrlStrategyCn` strategy, but without fallback domains.
- Added ability to indicate if the device IDs should be read just once per SDK initialization via calling `setReadDeviceInfoOnceEnabled` method of the `AdjustConfig` instance.

#### Native SDKs
- [iOS@v4.37.0][ios_sdk_v4.37.0]
- [Android@v4.38.0][android_sdk_v4.38.0]

---

### Version 4.35.1 (10th October 2023)
#### Added
- Added sending of `event_callback_id` parameter (if set) with the event payload.

#### Native SDKs
- [iOS@v4.35.2][ios_sdk_v4.35.2]
- [Android@v4.35.1][android_sdk_v4.35.1]

---

### Version 4.35.0 (5th October 2023)
#### Added
- Added support for SKAdNetwork 4.0.
- Added support for setting a new China URL Strategy. You can choose this setting by calling `setUrlStrategy` method of `AdjustConfig` instance with `AdjustConfig.AdjustUrlStrategyCn` parameter.
- Added ability to read App Set ID on Android platform in case you opt in by adding the `com.google.android.gms:play-services-appset` dependency to your Android app.
- Added ability to delay SDK start on iOS platform in order to wait for an answer to the ATT dialog. You can set the number of seconds to wait (capped internally to 120) by calling the `setAttConsentWaitingInterval` method of the `AdjustConfig` instance.
- Added support for purchase verification. In case you are using this feature, you can now use it by calling `verifyAppStorePurchase` (for iOS) and `verifyPlayStorePurchase` (for Android) methods of the `Adjust` instance.
- Added support for SigV3 library. Update authorization header building logic to use `adj_signing_id`.
- Added `setAndroidFinalAttributionEnabled` method to `AdjustConfig` to indicate if only final Android attribution is needed in attribution callback (by default attribution callback return intermediate attribution as well before final attribution if not enabled with this setter method).

#### Native SDKs
- [iOS@v4.35.1][ios_sdk_v4.35.1]
- [Android@v4.35.0][android_sdk_v4.35.0]

---

### Version 4.32.0 (16th September 2022)
#### Added
- Added partner sharing settings to the third party sharing feature.
- Added `getLastDeeplink` getter to `Adjust` API to be able to get last tracked deep link by the SDK (iOS only).
- Added support for `LinkMe` feature (iOS only).
- Added support to get Facebook install referrer information in attribution callback (Android only).

#### Changed
- Switched to adding permission `com.google.android.gms.permission.AD_ID` in the Android app's manifest by default.

#### Fixed
- Fixed import statement errors in case CocoaPods is forcing usage of frameworks (https://github.com/adjust/cordova_sdk/issues/163).

#### Native SDKs
- [iOS@v4.32.0][ios_sdk_v4.32.0]
- [Android@v4.32.0][android_sdk_v4.32.0]

---

### Version 4.30.0 (24th May 2022)

#### Added
- Added ability to mark your app as COPPA compliant. You can enable this setting by calling `setCoppaCompliantEnabled` method of `AdjustConfig` instance with boolean parameter `true`.
- Added ability to mark your Android app as app for the kids in accordance to Google Play Families policies. You can enable this setting by calling `setPlayStoreKidsAppEnabled` method of `AdjustConfig` instance with boolean parameter `true`.
- Added `checkForNewAttStatus` method to `Adjust` API to allow iOS apps to instruct to SDK to check if `att_status` might have changed in the meantime.
- Added support for Unity ad revenue tracking.
- Added support for Helium Chartboost ad revenue tracking.
- Added support for Generic ad revenue tracking.

#### Changed
- Started using CocoaPods to pull native Adjust iOS SDK dependency.
- Started using Maven to pull native Adjust Android SDK dependency.

#### Native SDKs
- [iOS@v4.30.0][ios_sdk_v4.30.0]
- [Android@v4.30.1][android_sdk_v4.30.1]

---

### Version 4.29.1 (23rd September 2021)
#### Added
- Added support for `Admost` ad revenue source.
- Added ability to enable native Adjust OAID plugin.

#### Native SDKs
- [iOS@v4.29.6][ios_sdk_v4.29.6]
- [Android@v4.28.5][android_sdk_v4.28.5]

---

### Version 4.29.0 (13th August 2021)
#### Added
- Added data residency feature. You can choose this setting by calling `setUrlStrategy` method of `AdjustConfig` instance with `AdjustConfig.DataResidencyEU` (for EU data residency region), `AdjustConfig.DataResidencyTR` (for TR data residency region) or `AdjustConfig.DataResidencyUS` value (for US data residency region).
- Added possibility to `trackAdRevenue` method of `Adjust` interface to allow tracking of ad revenue by passing `AdjustAdRevenue` object as parameter.
- Added support for `AppLovin MAX` ad revenue tracking.
- Added `setConversionValueUpdatedCallbackListener` method to `AdjustConfig` which can be used to set a callback which will get information when Adjust SDK updates conversion value for the user.
- Added preinstall tracking with usage of system installer receiver on Android platform (`setPreinstallFilePath` method of the `AdjustConfig`).

#### Fixed
- Fixed missing authorization header in retry requests on Android.

#### Native SDKs
- [iOS@v4.29.4][ios_sdk_v4.29.4]
- [Android@v4.28.4][android_sdk_v4.28.4]

---

### Version 4.28.0 (2nd April 2021)
#### Changed
- Removed native iOS legacy code.

#### Native SDKs
- [iOS@v4.28.0][ios_sdk_v4.28.0]
- [Android@v4.27.0][android_sdk_v4.27.0]

---

### Version 4.26.0 (5th March 2021)
#### Added
- Added possibility to get cost data information in attribution callback.
- Added `setNeedsCost` method to `AdjustConfig` to indicate if cost data is needed in attribution callback (by default cost data will not be part of attribution callback if not enabled with this setter method).
- Added `setPreinstallTrackingEnabled` method to `AdjustConfig` to allow enabling of preinstall tracking (this feature is OFF by default).
- Added support for Apple Search Ads attribution with usage of `AdServices.framework`.
- Added `setAllowAdServicesInfoReading` method to `AdjustConfig` to allow option for users to prevent SDK from performing any tasks related to Apple Search Ads attribution with usage of `AdServices.framework`.
- Added wrapper method `updateConversionValue` method to `Adjust` to allow updating SKAdNetwork conversion value via SDK API.
- Added `getAppTrackingAuthorizationStatus` getter to `Adjust` instance to be able to get current iOS app tracking status.
- Added improved measurement consent management and third party sharing mechanism.
- Added public constants to be used as sources for ad revenue tracking with `trackAdRevenue` method.

#### Native SDKs
- [iOS@v4.26.1][ios_sdk_v4.26.1]
- [Android@v4.26.2][android_sdk_v4.26.2]

---

### Version 4.23.0 (25th August 2020)
#### Added
- Added communication with SKAdNetwork framework by default on iOS 14.
- Added method `deactivateSKAdNetworkHandling` method to `AdjustConfig` to switch off default communication with SKAdNetwork framework in iOS 14.
- Added wrapper method `requestTrackingAuthorizationWithCompletionHandler` to `Adjust` to allow asking for user's consent to be tracked in iOS 14 and immediate propagation of user's choice to backend.
- Added handling of new iAd framework error codes introduced in iOS 14.
- Added sending of value of user's consent to be tracked with each package.
- Added `setUrlStrategy` method to `AdjustConfig` class to allow selection of URL strategy for specific market.

#### Native SDKs
- [iOS@v4.23.0][ios_sdk_v4.23.0]
- [Android@v4.24.0][android_sdk_v4.24.0]

---

### Version 4.22.1 (11th June 2020)
#### Fixed
- Fixed tag usage for `adjust-android.jar` declaration inside of `plugin.xml` file (now using `lib-file` instead of `source-file`).

#### Native SDKs
- [iOS@v4.22.1][ios_sdk_v4.22.1]
- [Android@v4.22.0][android_sdk_v4.22.0]

---

### Version 4.22.0 (10th June 2020)
#### Added
- Added subscription tracking feature.

#### Native SDKs
- [iOS@v4.22.1][ios_sdk_v4.22.1]
- [Android@v4.22.0][android_sdk_v4.22.0]

---

### Version 4.21.2 (4th May 2020)
#### Fixed
- Removed iAd timer from iOS native SDK.

#### Native SDKs
- [iOS@v4.21.3][ios_sdk_v4.21.3]
- [Android@v4.21.1][android_sdk_v4.21.1]

---

### Version 4.21.1 (10th April 2020)
#### Added
- Added support for Huawei App Gallery install referrer.

#### Changed
- Updated communication flow with `iAd.framework`.

#### Native SDKs
- [iOS@v4.21.1][ios_sdk_v4.21.1]
- [Android@v4.21.1][android_sdk_v4.21.1]

---

### Version 4.21.0 (24th March 2020)
#### Added
- Added `disableThirdPartySharing` method to `Adjust` interface to allow disabling of data sharing with third parties outside of Adjust ecosystem.
- Added support for signature library as a plugin.
- Added more aggressive sending retry logic for install session package.
- Added additional parameters to `ad_revenue` package payload.
- Added external device ID support.

#### Native SDKs
- [iOS@v4.21.0][ios_sdk_v4.21.0]
- [Android@v4.21.0][android_sdk_v4.21.0]

---

### Version 4.18.0 (2nd July 2019)
#### Added
- Added `trackAdRevenue` method to `Adjust` interface to allow tracking of ad revenue. With this release added support for `MoPub` ad revenue tracking.
- Added reading of Facebook anonymous ID if available on iOS platform.

#### Native SDKs
- [iOS@v4.18.0][ios_sdk_v4.18.0]
- [Android@v4.18.0][android_sdk_v4.18.0]

---

### Version 4.17.1 (1st February 2019)
#### Fixed
- Fixed occurance of exception due to usage attempt of `getSdkPrefix()` method for `ionic-native` users.

#### Native SDKs
- [iOS@v4.17.1][ios_sdk_v4.17.1]
- [Android@v4.17.0][android_sdk_v4.17.0]

---

### Version 4.17.0 (8th January 2019)
#### Added
- Added support for `Ionic Native`.
- Added `getSdkVersion()` method to `Adjust` interface to obtain current SDK version string.
- Added `setCallbackId` method to `AdjustEvent` interface for users to set custom ID on event object which will later be reported in event success/failure callbacks.
- Added `callbackId` field to event tracking success callback object.
- Added `callbackId` field to event tracking failure callback object.

#### Changed
- Marked `setReadMobileEquipmentIdentity` method of `AdjustConfig` object as deprecated.
- SDK will now fire attribution request each time upon session tracking finished in case it lacks attribution info.

#### Native SDKs
- [iOS@v4.17.1][ios_sdk_v4.17.1]
- [Android@v4.17.0][android_sdk_v4.17.0]

---

### Version 4.14.0 (18th June 2018)
#### Added
- Added deep link caching in case `appWillOpenUrl` method is called natively before SDK is initialised.

#### Changed
- Updated the way how iOS native bridge handles push tokens from Unity interface - they are now being passed directly as strings to native iOS SDK.

#### Native SDKs
- [iOS@v4.14.1][ios_sdk_v4.14.1]
- [Android@v4.14.0][android_sdk_v4.14.0]

---

### Version 4.13.0 (15th May 2018)
#### Added
- Added `gdprForgetMe` method to `Adjust` interface to enable possibility for user to be forgotten in accordance with GDPR law.

#### Native SDKs
- [iOS@v4.13.0][ios_sdk_v4.13.0]
- [Android@v4.13.0][android_sdk_v4.13.0]

---

### Version 4.12.5 (12th March 2018)
#### Native changes
- Updated iOS SDK to `v4.12.3`.
- Updated Android SDK to `v4.12.4`.

#### Native SDKs
- [iOS@v4.12.3][ios_sdk_v4.12.3]
- [Android@v4.12.4][android_sdk_v4.12.4]

---

### Version 4.12.4 (1st February 2018)
#### Native changes
- https://github.com/adjust/android_sdk/blob/master/CHANGELOG.md#version-4121-31st-january-2018

#### Native SDKs
- [iOS@v4.12.1][ios_sdk_v4.12.1]
- [Android@v4.12.1][android_sdk_v4.12.1]

---

### Version 4.12.3 (12th January 2018)
#### Changed
- Rebuilt `AdjustSdk.framework` for pre Xcode 9 support.
- Removed symlinks from `AdjustSdk.framework`.

#### Native SDKs
- **[iOS]** [iOS@v4.12.1][ios_sdk_v4.12.1]
- **[AND]** [Android@v4.12.0][android_sdk_v4.12.0]

---

### Version 4.12.2 (11th January 2018)
#### Changed
- Moved plugin contents in root directory instead of subdirectory.

#### Native SDKs
- **[iOS]** [iOS@v4.12.1][ios_sdk_v4.12.1]
- **[AND]** [Android@v4.12.0][android_sdk_v4.12.0]

---

### Version 4.12.1 (8th January 2018)
#### Added
- Added symlink to `README` inside of `plugin` folder to have `README` up to date on `npm` repository.

#### Fixed
- Fixed typo in `ProGuard` section of `README`.

#### Native SDKs
- **[iOS]** [iOS@v4.12.1][ios_sdk_v4.12.1]
- **[AND]** [Android@v4.12.0][android_sdk_v4.12.0]

---

### Version 4.12.0 (29th December 2017)
#### Native changes:
- **[iOS]** https://github.com/adjust/ios_sdk/blob/master/CHANGELOG.md#version-4120-13th-december-2017
- **[iOS]** https://github.com/adjust/ios_sdk/blob/master/CHANGELOG.md#version-4121-13th-december-2017
- **[AND]** https://github.com/adjust/android_sdk/blob/master/CHANGELOG.md#version-4120-13th-december-2017

#### Added
- **[AND]** Added `getAmazonAdId` method to `Adjust` interface.
- **[iOS][AND]** Added `setAppSecret` method to `AdjustConfig` interface.
- **[iOS][AND]** Added `setReadMobileEquipmentIdentity` method to `AdjustConfig` interface.

#### Fixed
- **[AND]** Fixed handling of `null` value being passed as a currency to `setRevenue` method.

#### Native SDKs
- **[iOS]** [iOS@v4.12.1][ios_sdk_v4.12.1]
- **[AND]** [Android@v4.12.0][android_sdk_v4.12.0]

---

### Version 4.11.3 (28th September 2017)
#### Added
- **[iOS]** Improved iOS 11 support.

#### Changed
- **[iOS]** Re-added support for Xcode 7.
- **[iOS]** Removed iOS connection validity checks.
- **[iOS]** Updated native iOS SDK to version **4.11.5**.

#### Native SDKs
- **[iOS]** [iOS@v4.11.5][ios_sdk_v4.11.5]
- **[AND]** [Android@v4.11.4][android_sdk_v4.11.4]

---

### Version 4.11.2 (15th May 2017)
#### Added
- **[iOS][AND]** Added check if `sdk_click` package response contains attribution information.
- **[iOS][AND]** Added sending of attributable parameters with every `sdk_click` package.

#### Changed
- **[iOS][AND]** Replaced `assert` level logs with `warn` level.

#### Native SDKs
- **[iOS]** [iOS@v4.11.4][ios_sdk_v4.11.4]
- **[AND]** [Android@v4.11.4][android_sdk_v4.11.4]

---

### Version 4.11.1 (25th April 2017)
#### Added
- **[iOS]** Added nullability annotations to public headers for Swift 3.0 compatibility.
- **[iOS]** Added `BITCODE_GENERATION_MODE` to iOS framework for `Carthage` support.
- **[iOS][AND]** Added sending of the app's install time.
- **[iOS][AND]** Added sending of the app's update time.

#### Fixed
- **[iOS]** Fixed not processing of `sdk_info` package type causing logs not to print proper package name once tracked.
- **[AND]** Fixed query string parsing.
- **[AND]** Fixed issue of creating and destroying lots of threads on certain Android API levels (https://github.com/adjust/android_sdk/issues/265).
- **[AND]** Protected `Package Manager` from throwing unexpected exceptions (https://github.com/adjust/android_sdk/issues/266).

#### Changed
- **[AND]** Refactored native networking code.
- **[iOS]** Updated native iOS SDK to version **4.11.3**.
- **[AND]** Updated native Android SDK to version **4.11.3**.
- **[REPO]** Introduced `[iOS]`, `[AND]`, `[WIN]` and `[REPO]` tags to `CHANGELOG` to highlight the platform the change is referring to.

#### Native SDKs
- **[iOS]** [iOS@v4.11.3][ios_sdk_v4.11.3]
- **[AND]** [Android@v4.11.3][android_sdk_v4.11.3]

---

### Version 4.11.0 (1st February 2017)
#### Added
- **[iOS][AND]** Added `adid` property to the attribution callback response.
- **[iOS][AND]** Added `Adjust.getAdid` method to be able to get adid value at any time after obtaining it, not only when session/event callbacks have been triggered.
- **[iOS][AND]** Added `Adjust.getAttribution` method to be able to get current attribution value at any time after obtaining it, not only when attribution callback has been triggered.
- **[iOS]** Added handling for numeric types being passed as callback/partner parameter values on iOS platform.
- **[AND]** Added sending of **Amazon Fire Advertising Identifier** for Android platform.
- **[AND]** Added possibility to set default tracker for the app by adding `adjust_config.properties` file to the `assets` folder of your Android app. Mostly meant to be used by the `Adjust Store & Pre-install Tracker Tool` (https://github.com/adjust/android_sdk/blob/master/doc/english/pre_install_tracker_tool.md).

#### Fixed
- **[iOS][AND]** Now reading push token value from activity state file when sending package.
- **[iOS]** Fixed memory leak by closing network session for iOS platform.
- **[iOS]** Fixed `TARGET_OS_TV` pre processor check for iOS platform.

#### Changed
- **[iOS][AND]** Firing attribution request as soon as install has been tracked, regardless of presence of attribution callback implementation in user's app.
- **[iOS]** Saving iAd/AdSearch details to prevent sending duplicated `sdk_click` packages for iOS platform.
- **[iOS]** Updated native iOS SDK to version **4.11.0**.
- **[AND]** Updated native Android SDK to version **4.11.0**.
- **[REPO]** Updated docs.

#### Native SDKs
- **[iOS]** [iOS@v4.11.0][ios_sdk_v4.11.0]
- **[AND]** [Android@v4.11.0][android_sdk_v4.11.0]

---

### Version 4.10.2 (13th December 2016)
#### Changed
- **[iOS]** Not implementing all callbacks in iOS middleware part anymore, only those which user has defined in the app.
- **[iOS]** SDK is now `cocoon.io` compatible in both, `npm` repository and on `master` branch, so no need for `cocoon.io` users to use `cocoon` branch of the repository anymore.
- **[iOS]** Updated native iOS SDK to version **4.10.3**.
- **[AND]** Updated native Android SDK to version **4.10.4**.
- **[REPO]** Updated docs.

#### Fixed
- **[iOS]** Not displaying error message in iOS logs after event was successfully tracked and event tracking succeeded callback is not implemented in user's app.
- **[iOS][AND]** Deferred deep link arrival to the app is no longer dependent from implementation of the attribution callback.

#### Native SDKs
- **[iOS]** [iOS@v4.10.3][ios_sdk_v4.10.3]
- **[AND]** [Android@v4.10.4][android_sdk_v4.10.4]

---

### Version 4.10.1 (24th October 2016)
#### Fixed
- **[AND]** Fixed error when adding the SDK plugin for Android platform due to missing `adjust-android.jar` file.

#### Native SDKs
- **[iOS]** [iOS@v4.10.2][ios_sdk_v4.10.2]
- **[AND]** [Android@v4.10.2][android_sdk_v4.10.2]

---

### Version 4.10.0 (24th October 2016)
#### Added
- **[iOS]** Added support for iOS 10.
- **[AND]** Added revenue deduplication for Android platform.
- **[iOS][AND]** Added an option for enabling/disabling tracking while app is in background.
- **[iOS][AND]** Added a callback to be triggered if event is successfully tracked.
- **[iOS][AND]** Added a callback callback to be triggered if event tracking failed.
- **[iOS][AND]** Added a callback to be triggered if session is successfully tracked.
- **[iOS][AND]** Added a callback to be triggered if session tracking failed.
- **[iOS][AND]** Added possibility to set session callback and partner parameters with `addSessionCallbackParameter` and `addSessionPartnerParameter` methods.
- **[iOS][AND]** Added possibility to remove session callback and partner parameters by key with `removeSessionCallbackParameter` and `removeSessionPartnerParameter` methods.
- **[iOS][AND]** Added possibility to remove all session callback and partner parameters with `resetSessionCallbackParameters` and `resetSessionPartnerParameters` methods.
- **[iOS][AND]** Added new `Suppress` log level.
- **[iOS][AND]** Added possibility to delay initialisation of the SDK while maybe waiting to obtain some session callback or partner parameters with `delayed start` feature on adjust config instance.
- **[iOS][AND]** Added callback method to get deferred deep link content into the app.
- **[iOS][AND]** Added possibility to decide whether the SDK should launch the deferred deep link or not.
- **[iOS][AND]** Added possibility to set user agent manually on adjust config instance.

#### Changed
- **[iOS][AND]** Deferred deep link info will now arrive as part of the attribution response and not as part of the answer to first session.
- **[iOS]** Updated native iOS SDK to version **4.10.1**.
- **[AND]** Updated native Android SDK to version **4.10.2**.
- **[REPO]** Updated docs.

#### Native SDKs
- **[iOS]** [iOS@v4.10.2][ios_sdk_v4.10.2]
- **[AND]** [Android@v4.10.2][android_sdk_v4.10.2]

---

### Version 4.3.0 (26th February 2016)
### Added
- **[iOS]** Added `Bitcode` support for iOS framework.
- **[iOS]** Added `getIdfa` method for getting `IDFA` on iOS device.
- **[AND]** Added `getGoogleAdId` method for getting Google `Play Services Ad Id` on Android device.
- **[REPO]** Added `CHANGELOG.md` to the repository.

#### Changed
- **[iOS]** Updated Native iOS SDK to version **4.5.4**.
- **[AND]** Updated Native Android SDK to version **4.2.3**.

#### Native SDKs
- **[iOS]** [iOS@v4.5.4][ios_sdk_v4.5.4]
- **[AND]** [Android@v4.2.3][android_sdk_v4.2.3]

---

### Version 4.2.0 (18th January 2016)
#### Changed
- **[iOS]** Removed `MAC MD5` reading from iOS platform.
- **[iOS]** Removed `initWithWebView` from our SDK and replaced with `pluginInitialize` method as it was removed from Cordova iOS platform 4.0.0.
- **[iOS]** Updated Native iOS SDK to version **4.5.0**.
- **[AND]** Updated Native Android SDK to version **4.2.0**.

#### Native SDKs
- **[iOS]** [iOS@v4.5.0][ios_sdk_v4.5.0]
- **[AND]** [Android@v4.2.0][android_sdk_v4.2.0]

---

### Version 4.1.0 (5th November 2015)
#### Changed
- **[REPO]** Ignoring unnecessary folders in `com.adjust.sdk` on `npm` repository.

#### Native SDKs
- **[iOS]** [iOS@v4.4.1][ios_sdk_v4.4.1]
- **[AND]** [Android@v4.1.3][android_sdk_v4.1.3]

---

### Version 4.1.1 (5th November 2015)
### Added
- **[REPO]** Added Adjust plugin to `npm` repository.

#### Changed
- **[REPO]** Updated docs.

#### Native SDKs
- **[iOS]** [iOS@v4.4.1][ios_sdk_v4.4.1]
- **[AND]** [Android@v4.1.3][android_sdk_v4.1.3]

---

### Version 4.1.0 (5th November 2015)
### Added
- **[iOS][AND]** Added deep linking on JavaScript level.

#### Changed
- **[iOS]** Updated Native iOS SDK to version **4.4.1**.
- **[AND]** Updated Native Android SDK to version **4.1.3**.
- **[REPO]** Updated docs.

#### Native SDKs
- **[iOS]** [iOS@v4.4.1][ios_sdk_v4.4.1]
- **[AND]** [Android@v4.1.3][android_sdk_v4.1.3]

---

### Version 4.0.2 (29th July 2015)
### Fixed
- **[iOS]** Symlinks are now being fixed during plugin installation (issues for iOS once adding the plugin on Windows OS).

#### Native SDKs
- **[iOS]** [iOS@v4.2.7][ios_sdk_v4.2.7]
- **[AND]** [Android@v4.1.0][android_sdk_v4.1.0]

---

### Version 4.0.1 (14th July 2015)
#### Changed
- **[REPO]** Changed plugin paths to be case sensitive.

#### Native SDKs
- **[iOS]** [iOS@v4.2.7][ios_sdk_v4.2.7]
- **[AND]** [Android@v4.1.0][android_sdk_v4.1.0]

---

### Version 4.0.0 (17th July 2015)
### Added
- **[iOS][AND]** Upgraded to the adjust SDK version 4.0.0.

#### Changed
- **[iOS]** Updated Native iOS SDK to version **4.2.7**.
- **[AND]** Updated Native Android SDK to version **4.1.0**.
- **[REPO]** Updated docs.

#### Native SDKs
- **[iOS]** [iOS@v4.2.7][ios_sdk_v4.2.7]
- **[AND]** [Android@v4.1.0][android_sdk_v4.1.0]

---

### Version 3.4.1 (9th October 2014)
### Added
- **[iOS]** Adding needed iOS frameworks automatically.

#### Native SDKs
- **[iOS]** [iOS@v3.4.0][ios_sdk_v3.4.0]
- **[AND]** [Android@v3.5.0][android_sdk_v3.5.0]

---

### Version 3.4.0 (29th July 2014)
### Added
- **[iOS][AND]** Added automated startup - device stop and device start.
- **[iOS][AND]** Added setup using config file.
- **[iOS][AND]** Added option to disable and enable the SDK temporarily.

#### Changed
- **[iOS]** Updated native iOS SDK to version **3.4.0**.
- **[AND]** Updated native Android SDK to version **3.5.0**.
- **[iOS][AND]** Updated sources.

#### Native SDKs
- **[iOS]** [iOS@v3.4.0][ios_sdk_v3.4.0]
- **[AND]** [Android@v3.5.0][android_sdk_v3.5.0]

---

### Version 3.0.0 (20th March 2014)
#### Added
- **[iOS][AND]** Initial release of the adjust SDK for Cordova. Supported platforms: `iOS` and `Android`.

[ios_sdk_v3.4.0]: https://github.com/adjust/ios_sdk/tree/v3.4.0
[ios_sdk_v4.2.7]: https://github.com/adjust/ios_sdk/tree/v4.2.7
[ios_sdk_v4.4.1]: https://github.com/adjust/ios_sdk/tree/v4.4.1
[ios_sdk_v4.5.0]: https://github.com/adjust/ios_sdk/tree/v4.5.0
[ios_sdk_v4.5.4]: https://github.com/adjust/ios_sdk/tree/v4.5.4
[ios_sdk_v4.10.2]: https://github.com/adjust/ios_sdk/tree/v4.10.2
[ios_sdk_v4.10.3]: https://github.com/adjust/ios_sdk/tree/v4.10.3
[ios_sdk_v4.11.0]: https://github.com/adjust/ios_sdk/tree/v4.11.0
[ios_sdk_v4.11.3]: https://github.com/adjust/ios_sdk/tree/v4.11.3
[ios_sdk_v4.11.4]: https://github.com/adjust/ios_sdk/tree/v4.11.4
[ios_sdk_v4.11.5]: https://github.com/adjust/ios_sdk/tree/v4.11.5
[ios_sdk_v4.12.1]: https://github.com/adjust/ios_sdk/tree/v4.12.1
[ios_sdk_v4.12.3]: https://github.com/adjust/ios_sdk/tree/v4.12.3
[ios_sdk_v4.13.0]: https://github.com/adjust/ios_sdk/tree/v4.13.0
[ios_sdk_v4.14.1]: https://github.com/adjust/ios_sdk/tree/v4.14.1
[ios_sdk_v4.15.0]: https://github.com/adjust/ios_sdk/tree/v4.15.0
[ios_sdk_v4.17.1]: https://github.com/adjust/ios_sdk/tree/v4.17.1
[ios_sdk_v4.18.0]: https://github.com/adjust/ios_sdk/tree/v4.18.0
[ios_sdk_v4.21.0]: https://github.com/adjust/ios_sdk/tree/v4.21.0
[ios_sdk_v4.21.1]: https://github.com/adjust/ios_sdk/tree/v4.21.1
[ios_sdk_v4.21.3]: https://github.com/adjust/ios_sdk/tree/v4.21.3
[ios_sdk_v4.22.1]: https://github.com/adjust/ios_sdk/tree/v4.22.1
[ios_sdk_v4.23.0]: https://github.com/adjust/ios_sdk/tree/v4.23.0
[ios_sdk_v4.26.1]: https://github.com/adjust/ios_sdk/tree/v4.26.1
[ios_sdk_v4.28.0]: https://github.com/adjust/ios_sdk/tree/v4.28.0
[ios_sdk_v4.29.4]: https://github.com/adjust/ios_sdk/tree/v4.29.4
[ios_sdk_v4.29.6]: https://github.com/adjust/ios_sdk/tree/v4.29.6
[ios_sdk_v4.30.0]: https://github.com/adjust/ios_sdk/tree/v4.30.0
[ios_sdk_v4.32.0]: https://github.com/adjust/ios_sdk/tree/v4.32.0
[ios_sdk_v4.35.1]: https://github.com/adjust/ios_sdk/tree/v4.35.1
[ios_sdk_v4.35.2]: https://github.com/adjust/ios_sdk/tree/v4.35.2
[ios_sdk_v4.37.0]: https://github.com/adjust/ios_sdk/tree/v4.37.0
[ios_sdk_v4.38.0]: https://github.com/adjust/ios_sdk/tree/v4.38.0
[ios_sdk_v4.38.2]: https://github.com/adjust/ios_sdk/tree/v4.38.2
[ios_sdk_v5.0.0]: https://github.com/adjust/ios_sdk/tree/v5.0.0
[ios_sdk_v5.0.1]: https://github.com/adjust/ios_sdk/tree/v5.0.1
[ios_sdk_v5.1.0]: https://github.com/adjust/ios_sdk/tree/v5.1.0
[ios_sdk_v5.4.0]: https://github.com/adjust/ios_sdk/tree/v5.4.0

[android_sdk_v3.5.0]: https://github.com/adjust/android_sdk/tree/v3.5.0
[android_sdk_v4.1.0]: https://github.com/adjust/android_sdk/tree/v4.1.0
[android_sdk_v4.1.3]: https://github.com/adjust/android_sdk/tree/v4.1.3
[android_sdk_v4.2.0]: https://github.com/adjust/android_sdk/tree/v4.2.0
[android_sdk_v4.2.3]: https://github.com/adjust/android_sdk/tree/v4.2.3
[android_sdk_v4.10.2]: https://github.com/adjust/android_sdk/tree/v4.10.2
[android_sdk_v4.10.4]: https://github.com/adjust/android_sdk/tree/v4.10.4
[android_sdk_v4.11.0]: https://github.com/adjust/android_sdk/tree/v4.11.0
[android_sdk_v4.11.1]: https://github.com/adjust/android_sdk/tree/v4.11.1
[android_sdk_v4.11.3]: https://github.com/adjust/android_sdk/tree/v4.11.3
[android_sdk_v4.11.4]: https://github.com/adjust/android_sdk/tree/v4.11.4
[android_sdk_v4.12.0]: https://github.com/adjust/android_sdk/tree/v4.12.0
[android_sdk_v4.12.1]: https://github.com/adjust/android_sdk/tree/v4.12.1
[android_sdk_v4.12.4]: https://github.com/adjust/android_sdk/tree/v4.12.4
[android_sdk_v4.13.0]: https://github.com/adjust/android_sdk/tree/v4.13.0
[android_sdk_v4.14.0]: https://github.com/adjust/android_sdk/tree/v4.14.0
[android_sdk_v4.15.0]: https://github.com/adjust/android_sdk/tree/v4.15.0
[android_sdk_v4.17.0]: https://github.com/adjust/android_sdk/tree/v4.17.0
[android_sdk_v4.18.0]: https://github.com/adjust/android_sdk/tree/v4.18.0
[android_sdk_v4.21.0]: https://github.com/adjust/android_sdk/tree/v4.21.0
[android_sdk_v4.21.1]: https://github.com/adjust/android_sdk/tree/v4.21.1
[android_sdk_v4.22.0]: https://github.com/adjust/android_sdk/tree/v4.22.0
[android_sdk_v4.24.0]: https://github.com/adjust/android_sdk/tree/v4.24.0
[android_sdk_v4.26.2]: https://github.com/adjust/android_sdk/tree/v4.26.2
[android_sdk_v4.27.0]: https://github.com/adjust/android_sdk/tree/v4.27.0
[android_sdk_v4.28.4]: https://github.com/adjust/android_sdk/tree/v4.28.4
[android_sdk_v4.28.5]: https://github.com/adjust/android_sdk/tree/v4.28.5
[android_sdk_v4.30.1]: https://github.com/adjust/android_sdk/tree/v4.30.1
[android_sdk_v4.32.0]: https://github.com/adjust/android_sdk/tree/v4.32.0
[android_sdk_v4.35.0]: https://github.com/adjust/android_sdk/tree/v4.35.0
[android_sdk_v4.35.1]: https://github.com/adjust/android_sdk/tree/v4.35.1
[android_sdk_v4.38.0]: https://github.com/adjust/android_sdk/tree/v4.38.0
[android_sdk_v4.38.3]: https://github.com/adjust/android_sdk/tree/v4.38.3
[android_sdk_v5.0.0]: https://github.com/adjust/android_sdk/tree/v5.0.0
[android_sdk_v5.0.1]: https://github.com/adjust/android_sdk/tree/v5.0.1
[android_sdk_v5.0.2]: https://github.com/adjust/android_sdk/tree/v5.0.2
[android_sdk_v5.1.0]: https://github.com/adjust/android_sdk/tree/v5.1.0
[android_sdk_v5.4.0]: https://github.com/adjust/android_sdk/tree/v5.4.0
