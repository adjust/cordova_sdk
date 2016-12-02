### Version 4.10.2 (xxth December 2016)
#### Changed
- Not implementing all callbacks in iOS middleware part anymore, only those which user has defined in the app.
- SDK is now `cocoon.io` compatible in both, `npm` repository and on `master` branch, so no need for `cocoon.io` users to use `cocoon` branch of the repository anymore.
- Updated docs.
- Native SDKs stability updates and improvements.
- Updated native iOS SDK to version **4.10.3**.
- Updated native Android SDK to version **4.10.4**.

#### Fixed
- Not displaying error message in iOS logs after event was successfully tracked and event tracking succeeded callback is not implemented in user's app.
- Deferred deep link arrival to the app is no longer dependent from implementation of the attribution callback.

#### Native SDKs
- [iOS@v4.10.3][ios_sdk_v4.10.3]
- [Android@v4.10.4][android_sdk_v4.10.4]

---

### Version 4.10.1 (24th October 2016)
#### Fixed
- Fixed error when adding the SDK plugin for Android platform due to missing `adjust-android.jar` file.

#### Native SDKs
- [iOS@v4.10.2][ios_sdk_v4.10.2]
- [Android@v4.10.2][android_sdk_v4.10.2]

---

### Version 4.10.0 (24th October 2016)
#### Added
- Added support for iOS 10.
- Added revenue deduplication for Android platform.
- Added an option for enabling/disabling tracking while app is in background.
- Added a callback to be triggered if event is successfully tracked.
- Added a callback callback to be triggered if event tracking failed.
- Added a callback to be triggered if session is successfully tracked.
- Added a callback to be triggered if session tracking failed.
- Added possibility to set session callback and partner parameters with `addSessionCallbackParameter` and `addSessionPartnerParameter` methods.
- Added possibility to remove session callback and partner parameters by key with `removeSessionCallbackParameter` and `removeSessionPartnerParameter` methods.
- Added possibility to remove all session callback and partner parameters with `resetSessionCallbackParameters` and `resetSessionPartnerParameters` methods.
- Added new `Suppress` log level.
- Added possibility to delay initialisation of the SDK while maybe waiting to obtain some session callback or partner parameters with `delayed start` feature on adjust config instance.
- Added callback method to get deferred deep link content into the app.
- Added possibility to decide whether the SDK should launch the deferred deep link or not.
- Added possibility to set user agent manually on adjust config instance.

#### Changed
- Deferred deep link info will now arrive as part of the attribution response and not as part of the answer to first session.
- Updated docs.
- Native SDKs stability updates and improvements.
- Updated native iOS SDK to version **4.10.1**.
- Updated native Android SDK to version **4.10.2**.

#### Native SDKs
- [iOS@v4.10.2][ios_sdk_v4.10.2]
- [Android@v4.10.2][android_sdk_v4.10.2]

---

### Version 4.3.0 (26th February 2016)
### Added
- Added `CHANGELOG.md` to the repository.
- Added `Bitcode` support for iOS framework.
- Added `getIdfa` method for getting `IDFA` on iOS device.
- Added `getGoogleAdId` method for getting Google `Play Services Ad Id` on Android device.

#### Changed
- Updated Native iOS SDK to version **4.5.4**.
- Updated Native Android SDK to version **4.2.3**.

#### Native SDKs
- [iOS@v4.5.4][ios_sdk_v4.5.4]
- [Android@v4.2.3][android_sdk_v4.2.3]

---

### Version 4.2.0 (18th January 2016)
#### Changed
- Removed `MAC MD5` reading from iOS platform.
- Removed `initWithWebView` from our SDK and replaced with `pluginInitialize` method as it was removed from Cordova iOS platform 4.0.0.
- Updated Native iOS SDK to version **4.5.0**.
- Updated Native Android SDK to version **4.2.0**.

#### Native SDKs
- [iOS@v4.5.0][ios_sdk_v4.5.0]
- [Android@v4.2.0][android_sdk_v4.2.0]

---

### Version 4.1.0 (5th November 2015)
#### Changed
- Ignoring unnecessary folders in `com.adjust.sdk` on `npm` repository.

#### Native SDKs
- [iOS@v4.4.1][ios_sdk_v4.4.1]
- [Android@v4.1.3][android_sdk_v4.1.3]

---

### Version 4.1.1 (5th November 2015)
### Added
- Added Adjust plugin to `npm` repository.

#### Changed
- Updated documentation.

#### Native SDKs
- [iOS@v4.4.1][ios_sdk_v4.4.1]
- [Android@v4.1.3][android_sdk_v4.1.3]

---

### Version 4.1.0 (5th November 2015)
### Added
- Added deep linking on JavaScript level.

#### Changed
- Updated documentation.
- Updated Native iOS SDK to version **4.4.1**.
- Updated Native Android SDK to version **4.1.3**.

#### Native SDKs
- [iOS@v4.4.1][ios_sdk_v4.4.1]
- [Android@v4.1.3][android_sdk_v4.1.3]

---

### Version 4.0.2 (29th July 2015)
### Fixed
- Symlinks are now being fixed during plugin installation (issues for iOS once adding the plugin on Windows OS).

#### Native SDKs
- [iOS@v4.2.7][ios_sdk_v4.2.7]
- [Android@v4.1.0][android_sdk_v4.1.0]

---

### Version 4.0.1 (14th July 2015)
#### Changed
- Changed plugin paths to be case sensitive.

#### Native SDKs
- [iOS@v4.2.7][ios_sdk_v4.2.7]
- [Android@v4.1.0][android_sdk_v4.1.0]

---

### Version 4.0.0 (17th July 2015)
### Added
- Upgrade to the adjust SDK version 4.0.0.

#### Changed
- Updated documentation.
- Updated Native iOS SDK to version **4.2.7**.
- Updated Native Android SDK to version **4.1.0**.

#### Native SDKs
- [iOS@v4.2.7][ios_sdk_v4.2.7]
- [Android@v4.1.0][android_sdk_v4.1.0]

---

### Version 3.4.1 (9th October 2014)
### Added
- Adds needed iOS frameworks automatically.

#### Native SDKs
- [iOS@v3.4.0][ios_sdk_v3.4.0]
- [Android@v3.5.0][android_sdk_v3.5.0]

---

### Version 3.4.0 (29th July 2014)
### Added
- Added automated startup - device stop and device start.
- Added setup using config file.
- Added option to disable and enable the SDK temporarily.

#### Changed
- Updated Native iOS SDK to version **3.4.0**.
- Updated Native Android SDK to version **3.5.0**.
- Updated sources.

#### Native SDKs
- [iOS@v3.4.0][ios_sdk_v3.4.0]
- [Android@v3.5.0][android_sdk_v3.5.0]

---

### Version 3.0.0 (20th March 2014)
#### Added
- Initial release of the adjust SDK for Cordova.
- Supported platforms: `iOS` and `Android`.

[ios_sdk_v3.4.0]: https://github.com/adjust/ios_sdk/tree/v3.4.0
[ios_sdk_v4.2.7]: https://github.com/adjust/ios_sdk/tree/v4.2.7
[ios_sdk_v4.4.1]: https://github.com/adjust/ios_sdk/tree/v4.4.1
[ios_sdk_v4.5.0]: https://github.com/adjust/ios_sdk/tree/v4.5.0
[ios_sdk_v4.5.4]: https://github.com/adjust/ios_sdk/tree/v4.5.4
[ios_sdk_v4.10.2]: https://github.com/adjust/ios_sdk/tree/v4.10.2
[ios_sdk_v4.10.3]: https://github.com/adjust/ios_sdk/tree/v4.10.3

[android_sdk_v3.5.0]: https://github.com/adjust/android_sdk/tree/v3.5.0
[android_sdk_v4.1.0]: https://github.com/adjust/android_sdk/tree/v4.1.0
[android_sdk_v4.1.3]: https://github.com/adjust/android_sdk/tree/v4.1.3
[android_sdk_v4.2.0]: https://github.com/adjust/android_sdk/tree/v4.2.0
[android_sdk_v4.2.3]: https://github.com/adjust/android_sdk/tree/v4.2.3
[android_sdk_v4.10.2]: https://github.com/adjust/android_sdk/tree/v4.10.2
[android_sdk_v4.10.4]: https://github.com/adjust/android_sdk/tree/v4.10.4
