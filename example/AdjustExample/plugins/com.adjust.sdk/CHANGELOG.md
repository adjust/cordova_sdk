### Version 4.3.0 (26th February 2016)
### Added
- `CHANGELOG.md` is now added to the repository.
- `Bitcode` support for iOS framework.
- `getIdfa` method for getting `IDFA` on iOS device.
- `getGoogleAdId` method for getting Google `Play Services Ad Id` on Android device.

#### Changed
- Native iOS SDK updated to version **4.5.4**.
- Native Android SDK updated to version **4.2.3**.

#### Native SDKs
- [iOS@v4.5.4][ios_sdk_v4.5.4]
- [Android@v4.2.3][android_sdk_v4.2.3]

---

### Version 4.2.0 (18th January 2016)
#### Changed
- `MAC MD5` reading from iOS platform is now removed.
- `initWithWebView` is now removed from Cordova iOS platform 4.0.0 so we removed it from our SDK and replaced with `pluginInitialize` method.
- Native iOS SDK updated to version **4.5.0**.
- Native Android SDK updated to version **4.2.0**.

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
- Adjust plugin is now added to `npm` repository.

#### Changed
- Documentation updated.

#### Native SDKs
- [iOS@v4.4.1][ios_sdk_v4.4.1]
- [Android@v4.1.3][android_sdk_v4.1.3]

---

### Version 4.1.0 (5th November 2015)
### Added
- Deep linking now possible on JavaScript level.

#### Changed
- Documentation updated.
- Native iOS SDK updated to version **4.4.1**.
- Native Android SDK updated to version **4.1.3**.

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
- Plugin paths are now case sensitive.

#### Native SDKs
- [iOS@v4.2.7][ios_sdk_v4.2.7]
- [Android@v4.1.0][android_sdk_v4.1.0]

---

### Version 4.0.0 (17th July 2015)
### Added
- Upgrade to the adjust SDK version 4.0.0.

#### Changed
- Documentation updated.
- Native iOS SDK updated to version **4.2.7**.
- Native Android SDK updated to version **4.1.0**.

#### Native SDKs
- [iOS@v4.2.7][ios_sdk_v4.2.7]
- [Android@v4.1.0][android_sdk_v4.1.0]

---

### Version 3.4.1 (9th October 2014)
### Added
- Automatically add needed iOS frameworks.

#### Native SDKs
- [iOS@v3.4.0][ios_sdk_v3.4.0]
- [Android@v3.5.0][android_sdk_v3.5.0]

---

### Version 3.4.0 (29th July 2014)
### Added
- Automated startup - device stop and device start.
- Setup using config file.
- Option to disable and enable the SDK temporarily.

#### Changed
- Native iOS SDK updated to version **3.4.0**.
- Native Android SDK updated to version **3.5.0**.
- Sources updated.

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
