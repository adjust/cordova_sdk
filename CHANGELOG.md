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

[android_sdk_v3.5.0]: https://github.com/adjust/android_sdk/tree/v3.5.0
[android_sdk_v4.1.0]: https://github.com/adjust/android_sdk/tree/v4.1.0
[android_sdk_v4.1.3]: https://github.com/adjust/android_sdk/tree/v4.1.3
[android_sdk_v4.2.0]: https://github.com/adjust/android_sdk/tree/v4.2.0
[android_sdk_v4.2.3]: https://github.com/adjust/android_sdk/tree/v4.2.3
