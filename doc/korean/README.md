요약
---

Adjust™의 Cordova SDK에 관한 설명서입니다. [adjust.com](http://adjust.com)에서 Adjust™에 대한 정보를 더 자세히 알아보세요.

N.B. 현재 Cordova SDK는 안드로이드 플랫폼 `4.0.0 버전 이상`과 iOS 플랫폼 `3.0.0 버전 이상`을 지원합니다.

목차
---

* [예시 앱](#example-app)
* [기본 연동](#basic-integration) 
  * [SDK 설치](#sdk-get)
  * [프로젝트에 SDK 추가](#sdk-add)
  * [앱에 SDK 연동](#sdk-integrate)
  * [Adjust 로깅](#adjust-logging)
  * [Adjust 프로젝트 설정](#adjust-project-settings) 
    * [Android 권한](#android-permissions)
    * [Google Play 서비스](#android-gps)
    * [Proguard 설정](#android-proguard)
    * [설치 참조자](#android-referrer) 
      * [Google Play Referrer API](#android-referrer-gpr-api)
      * [Google Play 스토어 intent](#android-referrer-gps-intent)
      * [Huawei 리퍼러 API](#android-huawei-referrer-api)

    * [iOS 프레임워크](#ios-frameworks)

* [부가 기능](#additional-features) 
  * [AppTrackingTransparency 프레임워크](#att-framework) 
    * [앱 트래킹 인증 래퍼](#ata-wrapper)

  * [SKAdNetwork 프레임워크](#skadn-framework)
  * [이벤트 추적](#event-tracking) 
    * [매출 추적](#revenue-tracking)
    * [매출 중복 제거](#revenue-deduplication)
    * [인앱 결제 검증](#iap-verification)
    * [콜백 파라미터](#callback-parameters)
    * [파트너 파라미터](#partner-parameters)
    * [콜백 ID](#callback-id)

  * [구독 트래킹\(Subscription Tracking\)](#subscription-tracking)
  * [세션 파라미터](#session-parameters) 
    * [세션 콜백 파라미터](#session-callback-parameters)
    * [세션 파트너 파라미터](#session-partner-parameters)
    * [시작 지연](#delay-start)

  * [어트리뷰션 콜백](#attribution-callback)
  * [세션 및 이벤트 콜백](#session-event-callbacks)
  * [추적 비활성화](#disable-tracking)
  * [오프라인 모드](#offline-mode)
  * [이벤트 버퍼링](#event-buffering)
  * [GDPR 잊혀질 권리](#gdpr-forget-me)
  * [제3자 공유 비활성화](#disable-third-party-sharing)
  * [SDK Signature\(SDK 서명\)](#sdk-signature)
  * [백그라운드 추적](#background-tracking)
  * [기기 ID](#device-ids) 
    * [iOS 광고 ID](#di-idfa)
    * [Google Play 서비스 광고 ID](#di-gps-adid)
    * [Amazon 광고 ID](#di-fire-adid)
    * [Adjust 기기 식별자](#di-adid)

  * [사용자 어트리뷰션](#user-attribution)
  * [푸시 토큰](#push-token)
  * [사전 설치 트래커](#pre-installed-trackers)
  * [딥링크](#deeplinking) 
    * [표준 딥링크 시나리오](#deeplinking-standard)
    * [안드로이드와 iOS 8 이하 버전에서의 딥링크](#deeplinking-android-ios-old)
    * [iOS 9 이상 버전에서의 딥링크](#deeplinking-ios-new)
    * [디퍼드 딥링크 시나리오](#deeplinking-deferred)
    * [딥링크를 통한 리어트리뷰션](#deeplinking-reattribution)

* [라이센스](#license)

예시 앱
----

[`example-cordova` 경로](./example-cordova)에는 Cordova 예시 앱이, [`example-ionic` 경로](./example-ionic)에는 Ionic 예시 앱이 들어있습니다. 이를 통해 Adjust SDK를 앱에 연동하는 방법을 확인할 수 있습니다.

기본 연동
-----

Adjust SDK를 Cordova 프로젝트에 연동하는 데 필요한 최소한의 단계입니다.

### SDK 설치

`npm` [리퍼지토리](https://www.npmjs.com/package/com.adjust.sdk)나 Adjust의 [출시 페이지](https://github.com/adjust/cordova_sdk/releases)에서 Adjust SDK의 최신 버전을 다운로드할 수 있습니다.

### 프로젝트에 SDK 추가

`npm` 리퍼지토리에서 Adjust의 SDK를 플러그인으로 직접 다운로드할 수 있습니다. 이를 위해서는 프로젝트 폴더에 다음의 명령어를 실행해야 합니다.

    > cordova plugin add com.adjust.sdk
    Fetching plugin "com.adjust.sdk" via npm
    Installing "com.adjust.sdk" for android
    Installing "com.adjust.sdk" for ios

Adjust의 출시 페이지에서 Adjust SDK를 다운로드한 경우에는 원하는 폴더에 아카이브를 추출한 뒤 프로젝트 폴더에 다음의 명령어를 실행해야 합니다.

    > cordova plugin add path_to_folder/cordova_sdk/plugin
    Installing "com.adjust.sdk" for android
    Installing "com.adjust.sdk" for ios

Ionic Native를 사용하는 경우, `ionic-native` 리포지터리에서 Adjust SDK를 추가할 수 있습니다.

    > npm install @ionic-native/adjust --save
    > ionic cordova plugin add com.adjust.sdk

### 앱에 SDK 연동

Adjust SDK는 `deviceready(준비)`, `resume(재실행)`, `pause(대기)`와 같은 Cordova 이벤트 발생 시 자동으로 등록됩니다.

`deviceready` 이벤트 발생 후, `index.js` 파일에 다음의 코드를 입력하면 Adjust SDK를 초기화할 수 있습니다.

```js
var adjustConfig = new AdjustConfig("{YourAppToken}", AdjustConfig.EnvironmentSandbox);
Adjust.create(adjustConfig);
```

`{YourAppToken}`을 사용 중인 앱 토큰으로 교체한 다음, [대시보드](http://adjust.com)에서 확인할 수 있습니다.

테스트 또는 배포 등 어떤 목적으로 앱을 빌드하는지에 따라, 다음의 값 중 하나를 `environment`로 설정해야 합니다.

```javascript
AdjustConfig.EnvironmentSandbox
AdjustConfig.EnvironmentProduction
```

**중요** : 앱을 테스트해야 하는 경우, 해당 값을 `AdjustConfig.EnvironmentSandbox`로 설정해야 합니다. 앱을 퍼블리시할 준비가 완료되면 환경 설정을 `AdjustConfig.EnvironmentProduction`으로 변경하고, 앱 개발 및 테스트를 새로 시작한다면 `AdjustConfig.EnvironmentSandbox`로 다시 설정하세요.

테스트 기기로 인해 발생하는 테스트 트래픽과 실제 트래픽을 구분하기 위해 다른 환경을 사용하고 있으니, 상황에 알맞은 설정을 적용하시기 바랍니다. 이는 매출을 추적하는 경우에 특히 중요합니다.

### Adjust 로깅

다음 파라미터 중 하나를 사용하여 `AdjustConfig` 인스턴스에 `setLogLevel`을 호출함으로써 테스트에서 확인하는 로그 수를 늘리거나 줄일 수 있습니다.

```js
adjustConfig.setLogLevel(AdjustConfig.LogLevelVerbose);   // enable all logging
adjustConfig.setLogLevel(AdjustConfig.LogLevelDebug);     // enable more logging
adjustConfig.setLogLevel(AdjustConfig.LogLevelInfo);      // the default
adjustConfig.setLogLevel(AdjustConfig.LogLevelWarn);      // disable info logging
adjustConfig.setLogLevel(AdjustConfig.LogLevelError);     // disable warnings as well
adjustConfig.setLogLevel(AdjustConfig.LogLevelAssert);    // disable errors as well
adjustConfig.setLogLevel(AdjustConfig.LogLevelSuppress);  // disable all logging
```

### Adjust 프로젝트 설정

Adjust SDK가 앱에 추가되고 나면, Adjust SDK가 올바르게 작동하도록 하는 특정 동작이 수행됩니다. 이 과정에서 발생하는 모든 실행은 Adjust SDK 플러그인의 `plugin.xml` 파일에서 확인할 수 있습니다. 아래는 Adjust SDK를 앱에 추가한 이후의 추가적인 실행과 관련된 내용입니다.

### Android 권한

Adjust SDK는 안드로이드 매니페스트 파일에 다음의 3개 권한을 추가합니다: `INTERNET`, `ACCESS_WIFI_STATE`, `ACCESS_NETWORK_STATE` Adjust SDK 플러그인의 `plugin.xml` 파일에서 이러한 세팅을 확인할 수 있습니다.

```xml
<config-file target="AndroidManifest.xml" parent="/manifest">
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
</config-file>
```

`INTERNET`은 Adjust SDK가 추후 필요할 수 있는 권한입니다. `ACCESS_WIFI_STATE`는 앱이 Google Play 스토어를 타겟팅하지 않으며, Google Play 서비스를 사용하지 않는 경우 Adjust SDK가 필요로 하는 권한입니다.  Google Play 스토어를 타겟팅하며 Google Play 서비스를 사용하고 있는 경우에는 Adjust SDK가 이 권한을 필요로 하지 않으며, 앱 어디에서도 이 권한이 필요하지 않은 경우에는 삭제해도 됩니다. `ACCESS_NETWORK_STATE`는 MMC와 MNC 파라미터 읽기에 필요한 권합입니다.

### Google Play 서비스

2014년 8월 1일부터 고유 기기 식별을 위해 Google Play 스토어 앱에 대해 [Google 광고 ID](https://developer.android.com/google/play-services/id.html) 사용이 의무화되었습니다. Adjust SDK가 Google 광고 ID를 사용하도록 허용하려면 [Google Play 서비스](http://developer.android.com/google/play-services/index.html)를 연동해야 합니다.

Adjust SDK는 앱에 디폴트로 Google Play 서비스를 추가합니다. 이는 `plugin.xml` 파일에 아래의 라인으로 수행됩니다.

```xml
<framework src="com.google.android.gms:play-services-analytics:+" />
```

기타 Cordova 플러그인을 사용하는 경우, 앱에 Google Play 서비스가 디폴트로 또 추가될 수 있습니다. 이러한 경우, Adjust SDK의 Google Play 서비스가 기타 플러그인과 충돌하여 빌드타임 에러가 발생할 수 있습니다. Google Play 서비스가 Adjust SDK를 통해서만 앱에 연동되어 있어야 하는 것은 아닙니다. 앱에 **Google Play 서비스 라이브러리의 분석이 연동되어있는 한** , Adjust SDK는 필요한 모든 정보를 읽을 수 있습니다. 기타 Cordova 플러그인을 통해 앱에 Google Play 서비스를 추가하고자 하는 경우, Adjust SDK의 `plugin.xml` 파일에서 위 라인을 삭제하면 됩니다.

Google Play 서비스의 애널리틱스 부분이 앱에 성공적으로 설치되어 Adjust SDK가 올바르게 읽을 수 있는지 확인하려면, SDK가 `sandbox` 모드에서 실행되도록 설정하고 로그 레벨을 `verbose`로 설정하여 앱을 시작해야 합니다. 그런 다음 세션이나 앱 내 특정 이벤트를 트래킹하고, 트래킹이 완료되면 verbose 로그에서 읽어들이는 파라미터 리스트를 확인합니다. `gps_adid`라는 파라미터가 확인되면 Google Play 서비스 라이브러리의 애널리틱스 파트가 앱에 성공적으로 추가되었다는 것을 확인하실 수 있고, 이제 Adjust SDK가 이로부터 필요한 정보를 읽을 수 있습니다.

### Proguard 설정

Proguard를 사용하는 경우, Proguard 파일에 다음 줄을 추가하세요.

    -keep public class com.adjust.sdk.** { *; }
    -keep class com.google.android.gms.common.ConnectionResult {
        int SUCCESS;
    }
    -keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
        com.google.android.gms.ads.identifier.AdvertisingIdClient$Info getAdvertisingIdInfo(android.content.Context);
    }
    -keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
        java.lang.String getId();
        boolean isLimitAdTrackingEnabled();
    }
    -keep public class com.android.installreferrer.** { *; }

### 설치 참조자

Android 앱의 설치를 소스에 올바르게 어트리뷰션하려면 Adjust에 **설치 참조자** 에 대한 정보를 제공해야 합니다. 이를 위해 **Google Play Referrer API** 를 사용하거나 **Google Play 스토어 intent** 브로드캐스트 리시버를 캐치할 수 있습니다.

**중요** : Google Play Referrer API는 설치 참조자 정보를 더욱 신뢰할 수 있고 안전한 방법으로 제공하고 어트리뷰션 공급자가 클릭 인젝션에 대응할 수 있도록 하기 위한 목적으로 Google에서 새롭게 도입한 기능입니다. 애플리케이션에서 이를 지원하도록 할 것을 **강력히 권장드립니다** . Google Play 스토어 intent는 설치 참조자 정보를 획득할 수 있는 비교적 덜 안전한 방식입니다. 당분간은 새로운 Google Play Referrer API와 함께 사용 가능하지만, 향후에 지원이 중단될 예정입니다.

#### Google Play Referrer API

앱에서 이를 지원하기 위해 Adjust SDK는 앱에 디폴트로 지원을 추가합니다. 이는 `plugin.xml` 파일에 아래의 라인을 통해 수행됩니다.

```xml
<framework src="com.android.installreferrer:installreferrer:1.0" />
```

`installreferrer` 라이브러리는 Google Maven 리포지토리의 일부입니다. 따라서  앱을 빌드하려면 Google Maven 리포지토리를 앱의 `build.gradle` 파일에 추가해야 합니다.  \(한번도 이를 추가하지 않았다면\) :

```gradle
allprojects {
    repositories {
        jcenter()
        maven {
            url "https://maven.google.com"
        }
    }
}
```

또한 [Proguard 설정](#android-proguard) 챕터의 내용을 숙지하고, 해당 챕터에서 언급한 모든 규칙을 비롯하여 특히 다음 기능을 위해 필요한 규칙을 추가했는지 확인하십시오.

    -keep public class com.android.installreferrer.** { *; }

**Adjust SDK v4\.12\.0 이상** 버전을 사용 중인 경우 이 기능이 지원됩니다.

#### Google Play 스토어 intent

Google Play 스토어 `INSTALL_REFERRER` intent는 브로드캐스트 리시버에 의해 캡처됩니다. Adjust 설치 리퍼러 브로드캐스트 리시버는 앱에 디폴트로 추가됩니다. 자세한 내용은 Adjust의 [Android SDK README](https://github.com/adjust/android_sdk#gps-intent)에서 확인하시기 바랍니다. Adjust SDK 플러그인의 `plugin.xml` 파일에서 이러한 세팅을 확인할 수 있습니다.

```xml
<config-file target="AndroidManifest.xml" parent="/manifest/application">
    <receiver
        android:name="com.adjust.sdk.AdjustReferrerReceiver"
        android:exported="true">
        <intent-filter>
            <action android:name="com.android.vending.INSTALL_REFERRER" />
        </intent-filter>
    </receiver>
</config-file>
```

INSTALL\_REFERRER intent를 처리하는 자체 브로드캐스트 리시버를 사용하는 경우, Adjust 브로드캐스트 리시버를 매니페스트 파일에 추가할 필요가 없습니다. 이를 삭제할 수 있지만, [Android 가이드](https://github.com/adjust/android_sdk/blob/master/doc/english/referrer.md)에 설명된 대로 자체 리시버 내에서 Adjust 브로드캐스트 리시버에 호출을 추가해야 합니다.

#### Huawei 리퍼러 API

Adjust SDK 4\.21\.1 버전부터는 Huawei 앱 갤러리 버전이 10\.4 이상인 Huawei 기기에 설치 추적을 지원합니다. Huawei 리퍼러 API를 사용하기 위해 추가적인 연동 단계를 수행하지 않아도 됩니다.

### iOS 프레임워크

Adjust SDK 플러그인은 생성된 Xcode 프로젝트에 다음의 3개 iOS 프레임워크를 추가합니다.

* `iAd.framework` \- iAd 캠페인을 실행 중인 경우
* `AdSupport.framework` \- iOS 광고 Id \(IDFA\) 읽기용
* `CoreTelephony.framework` \- MCC 및 MNC 정보 읽기용
* `StoreKit.framework` \- SKAdNetwork 프레임워크와의 커뮤니케이션용
* `AppTrackingTransparency.framework` \- 트래킹에 대한 사용자의 동의 여부를 묻고 동의 의사를 취득하기 위해
* `AdjustSdk.framework` \- Adjust 자체 iOS SDK 프레임워크

이러한 세팅은 Adjust SDK 플러그인의 `plugin.xml` 파일에서 확인할 수 있습니다.

```xml
<framework src="src/ios/AdjustSdk.framework" custom="true" />
<framework src="AdSupport.framework" weak="true" />
<framework src="iAd.framework" weak="true" />
<framework src="CoreTelephony.framework" weak="true" />
<framework src="StoreKit.framework" weak="true" />
<!-- Uncomment line below if you are building your app for iOS 14 -->
<!-- <framework src="AppTrackingTransparency.framework" weak="true" /> -->
```

iAd 캠페인을 실행하고 있지 않은 경우, `iAd.framework` dependency\(의존성\)을 삭제할 수 있습니다. SKAdNetwork 프레임워크를 사용하지 않는 경우, `StoreKit.framework` dependency를 삭제할 수 있습니다. \(단, 기타 목적을 위해 필요한 경우 제외\)

**참고** : iOS 14 베타 버전에서는 `AppTrackingTransparency.framework`의 추가가 자동적으로 수행되지 않습니다. Xcode 베타 버전을 사용하지 않는 경우 컴파일 타임 에러가 발생할 수 있기 때문입니다. 따라서, Adjust SDK의 iOS 14 기능을 사용하고 싶은 경우에는 Xcode 베타 버전을 사용해야 하며, 이 라인을 `plugin.xml` 파일에서 언커멘트하거나 직접 Xcode 프로젝트에 `AppTrackingTransparency.framework`를 추가해야 합니다.

부가 기능
-----

Adjust SDK가 프로젝트에 연동되면 다음과 같은 기능의 장점을 활용할 수 있습니다.

### AppTrackingTransparency 프레임워크

**참고** : 본 기능은 iOS 플랫폼에서만 사용할 수 있습니다.

전송된 각 패키지에 대해 Adjust 백엔드는 사용자 또는 기기를 추적하는 데 사용할 수 있는 앱 관련 데이터에 대한 액세스 동의를 다음 네 가지 상태 중 하나로 수신합니다.

* Authorized\(승인됨\)
* Denied\(거부됨\)
* Not Determined\(결정되지 않음\)
* Restricted\(제한됨\)

기기가 사용자 기기 추적에 사용되는 앱 관련 데이터에 대한 액세스를 승인하는 인증 요청을 수신한 후에는 Authorized 또는 Denied 상태가 반환됩니다.

기기가 사용자 또는 기기를 추적하는 데 사용되는 앱 관련 데이터에 대한 액세스 인증 요청을 수신하기 전에는 Not Determined 상태가 반환됩니다.

앱 추적 데이터 인증 권한이 제한되면 Restricted 상태가 반환됩니다.

사용자에게 표시되는 대화 상자 팝업을 맞춤 설정하지 않으려는 경우, SDK에는 사용자가 대화 상자 팝업에 응답하면 업데이트된 상태를 수신하는 자체 메커니즘이 있습니다. 새로운 동의 상태를 백엔드에 편리하고 효율적으로 전달하기 위해 Adjust SDK는 다음 챕터 '앱 트래킹 인증 래퍼'에 설명된 앱 트래킹 인 메서드와 관련한 래퍼를 제공합니다.

### 앱 트래킹 인증 래퍼

**참고** : 본 기능은 iOS 플랫폼에서만 사용할 수 있습니다.

Adjust SDK는 앱 관련 데이터 액세스에 대한 사용자의 허용을 요청하기 위한 래퍼를 제공합니다. Adjust SDK는 [requestTrackingAuthorizationWithCompletionHandler:](https://developer.apple.com/documentation/apptrackingtransparency/attrackingmanager/3547037-requesttrackingauthorizationwith?language=objc) 메서드 상에 빌드된 래퍼가 있습니다. 여기서 사용자의 선택에 관한 정보를 얻기 위해 콜백 메서드를 정의할 수 있습니다. 또한 이 래퍼를 사용하면 사용자가 팝업 대화 상자에 응답하는 즉시 콜백 메서드를 사용하여 다시 전달됩니다. 또한 SDK는 사용자의 선택 정보를 백엔드에 알립니다. Integer 값은 다음과 같은 의미로 콜백 메서드를 통해 전달됩니다.

* 0: `ATTrackingManagerAuthorizationStatusNotDetermined`
* 1: `ATTrackingManagerAuthorizationStatusRestricted`
* 2: `ATTrackingManagerAuthorizationStatusDenied`
* 3: `ATTrackingManagerAuthorizationStatusAuthorized`

이 래퍼를 사용하려면 다음과 같이 호출하면 됩니다:

```js
Adjust.requestTrackingAuthorizationWithCompletionHandler(function(status) {
    switch (status) {
        case 0:
            // ATTrackingManagerAuthorizationStatusNotDetermined case
            break;
        case 1:
            // ATTrackingManagerAuthorizationStatusRestricted case
            break;
        case 2:
            // ATTrackingManagerAuthorizationStatusDenied case
            break;
        case 3:
            // ATTrackingManagerAuthorizationStatusAuthorized case
            break;
    }
});
```

### SKAdNetwork 프레임워크

**참고** : 본 기능은 iOS 플랫폼에서만 사용할 수 있습니다.

Adjust iOS SDK v4\.23\.0 이상을 설치했으며 iOS 14에서 앱을 실행하는 경우, SKAdNetwork와의 통신이 기본적으로 활성화되며 비활성화하도록 설정할 수 있습니다. 활성화하면 SDK가 실행될때 SKAdNetwork 어트리뷰션에 대해 Adjust가 자동으로 등록합니다. 이벤트가 Adjust 대시보드에서 전환 값을 수신하도록 설정된 경우, Adjust 백엔드가 전환 값 데이터를 SDK로 전송합니다. 그런 다음 SDK가 전환 값을 설정합니다. Adjust가 SKAdNetwork 콜백 데이터를 수신한 후에는 해당 정보가 대시보드에 표시됩니다.

Adjust SDK가 SKAdNetwork와 자동으로 통신하지 않도록 하려면 구성 객체에 대해 다음 메서드를 호출하여 해당 메서드를 사용하지 않도록 설정할 수 있습니다:

```js
adjustConfig.deactivateSKAdNetworkHandling();
```

### 이벤트 추적

Adjust를 사용하여 모든 유형의 이벤트를 추적할 수 있습니다. 버튼을 누르는 동작을 모두 트래킹 원하신다면 [대시보드](http://adjust.com)에서 새 이벤트 토큰을 생성하면 됩니다. 이벤트 토큰이 `abc123`이라고 가정해 보겠습니다. 버튼의 클릭 핸들러 메서드에 다음 줄을 추가하여 클릭을 추적할 수 있습니다.

```js
var adjustEvent = new AdjustEvent("abc123");
Adjust.trackEvent(adjustEvent);
```

### 매출 추적

사용자가 광고를 누르거나 앱 내 구매를 진행하여 매출을 창출할 수 있다면 해당 매출과 이벤트를 추적할 수 있습니다. 광고를 한 번 누르는 행위에 €0\.01의 매출 금액이 발생한다고 가정해 보겠습니다. 매출 이벤트를 다음과 같이 추적할 수 있습니다.

```js
var adjustEvent = new AdjustEvent("abc123");
adjustEvent.setRevenue(0.01, "EUR");
Adjust.trackEvent(adjustEvent);
```

사용자가 통화 토큰을 설정하면 Adjust는 사용자의 선택에 따라 발생 매출을 보고 매출로 자동 전환합니다. [여기에서 통화 전환](https://docs.adjust.com/en/event-tracking/#tracking-purchases-in-different-currencies)에 대해 자세히 알아보세요.

### 매출 중복 제거

중복되는 매출을 추적하는 것을 방지하기 위해 트랜잭션 ID를 선택적으로 추가할 수 있습니다. 마지막 열 개의 트랜잭션 ID가 보관되며, 중복되는 트랜잭션 ID가 있는 매출 이벤트는 건너뛰게 됩니다. 이러한 방식은 인앱 구매 추적에 특히 유용합니다. 아래 예시를 참조하세요.

앱 내 구매를 추적하려는 경우, 트랜잭션이 완료되고 품목이 구매되었을 때만 `TrackEvent`를 호출하십시오. 이렇게 하면 실제로 발생하지 않은 매출을 추적하는 것을 방지할 수 있습니다.

```js
var adjustEvent = new AdjustEvent("abc123");
adjustEvent.setRevenue(0.01, "EUR");
adjustEvent.setTransactionId("{YourTransactionId}");
Adjust.trackEvent(adjustEvent);
```

**참고** : Transaction ID\(트랜젝션 ID\)는 iOS 용어입니다. 완료된 Android 인앱 구매를 위한 고유 ID는 **Order ID\(주문 ID\)** 라고 합니다.

### 인앱 결제 검증

인앱 결제를 검증하고 싶은 경우, 서버에서 결제 수신 정보를 검증하는 툴인 Adjust의 Purchase Verification 제품을 사용할 수 있습니다. Adjust의 Cordova 구매 SDK에 대한 자세한 내용은 [여기](https://github.com/adjust/cordova_purchase_sdk)에서 확인할 수 있습니다.

### 콜백 파라미터

[대시보드](http://adjust.com)에서 이러한 이벤트에 대한 콜백 URL을 등록할 수 있으며, 이벤트가 추적될 때마다 Adjust가 해당 URL에 GET 요청을 전송합니다. 이러한 경우 개체에 키\-값 쌍\(key\-value pair\)을 입력하고 `trackEvent` 메서드에 전달할 수도 있습니다. 이후 Adjust는 이름이 지정된 이러한 파라미터를 사용자의 콜백 URL에 추가합니다.

예를 들어, 사용자가 이벤트를 위해 `http://www.adjust.com/callback` URL을 등록했으며 이벤트 토큰 `abc123`을 추가하고 다음 줄을 실행했다고 가정해 보겠습니다.

```js
var adjustEvent = new AdjustEvent("abc123");
adjustEvent.addCallbackParameter("key", "value");
adjustEvent.addCallbackParameter("foo", "bar");
Adjust.trackEvent(adjustEvent);
```

이 경우, Adjust가 이벤트를 추적하여 다음으로 요청을 전송합니다.

    http://www.adjust.com/callback?key=value&foo=bar

Adjust는 iOS용 `{idfa}`나 Android용 `{gps_adid}` 등 파라미터 값으로 사용될 수 있는 다양한 자리 표시자를 지원합니다. 결과 콜백에서 `{idfa}` 자리 표시자는 현재 iOS 기기의 광고주를 위한 ID로 대체되며 `{gps_adid}`는 현재 Android 기기의 Google 광고 ID로 대체됩니다. Adjust는 사용자의 맞춤 파라미터를 저장하지 않으며 콜백에 추가하기만 합니다. 또한 이벤트를 위한 콜백을 등록하지 않은 경우에는 이러한 파라미터를 읽지도 않습니다.

Adjust [콜백 가이드](https://docs.adjust.com/en/callbacks)에서 사용 가능한 값의 전체 목록을 비롯하여 URL 콜백을 사용하는 방법을 자세히 알아보실 수 있습니다.

### 파트너 파라미터

앞서 언급한 콜백 파라미터와 유사하게, 사용자가 선택한 네트워크 파트너에 Adjust가 전송할 파라미터를 추가할 수 있습니다. Adjust 대시보드에서 이러한 네트워크를 활성화할 수 있습니다.

이는 상기 콜백 파라미터와 유사한 방식으로 이루어지지만, `AdjustEvent` 인스턴스의 `addPartnerParameter` 파라미터를 호출하여 추가할 수 있습니다.

```js
var adjustEvent = new AdjustEvent("abc123");
adjustEvent.addPartnerParameter("key", "value");
adjustEvent.addPartnerParameter("foo", "bar");
Adjust.trackEvent(adjustEvent);
```

Adjust의 [특별 파트너 가이드](https://docs.adjust.com/en/special-partners)에서 특별 파트너에 대한 자세한 내용을 알아보실 수 있습니다.

### 콜백 ID

추적할 각 이벤트에 맞춤 문자열 ID를 추가할 수도 있습니다. 이 ID는 이후에 이벤트 성공 및/또는 이벤트 실패 콜백에서 보고되며, 이를 통해 성공적으로 추적된 이벤트와 그렇지 않은 이벤트를 확인할 수 있습니다. `AdjustEvent` 인스턴스에 `setCallbackId` 메서드를 호출하여 이 ID를 설정할 수 있습니다.

```js
var adjustEvent = new AdjustEvent("abc123");
adjustEvent.setCallbackId("Your-Custom-Id");
Adjust.trackEvent(adjustEvent);
```

### 구독 트래킹

**참고** : 이 기능은 SDK 4\.22\.0 버전 이상에서만 사용할 수 있습니다.

App Store 및 Play 스토어 구독을 추적한 후 Adjust SDK로 유효성을 검증할 수 있습니다. 구독 항목이 구매되면 다음을 Adjust SDK로 호출하세요.

**앱스토어 구독용** 

```js
var subscription = new AdjustAppStoreSubscription(price, currency, transactionId, receipt);
subscription.setTransactionDate(transactionDate);
subscription.setSalesRegion(salesRegion);Adjust.trackAppStoreSubscription(subscription);
```

**플레이 스토어 구독용** 

```js
var subscription = new AdjustPlayStoreSubscription(price, currency, sku, orderId, signature, purchaseToken);
subscription.setPurchaseTime(purchaseTime);

Adjust.trackPlayStoreSubscription(subscription);
```

App Store 구독에 대한 구독 추적 파라미터

* [가격](https://developer.apple.com/documentation/storekit/skproduct/1506094-price?language=objc)
* 통화 \(개체 [currencyCode](https://developer.apple.com/documentation/foundation/nslocale/1642836-currencycode?language=objc)의 [priceLocale](https://developer.apple.com/documentation/storekit/skproduct/1506145-pricelocale?language=objc)를 전달해야 함\)
* [트랜잭션 ID](https://developer.apple.com/documentation/storekit/skpaymenttransaction/1411288-transactionidentifier?language=objc)
* [receipt\(수령인\)](https://developer.apple.com/documentation/foundation/nsbundle/1407276-appstorereceipturl)
* [transactionDate\(결제일\)](https://developer.apple.com/documentation/storekit/skpaymenttransaction/1411273-transactiondate?language=objc)
* salesRegion\(판매 지역\)\(개체 [countryCode](https://developer.apple.com/documentation/foundation/nslocale/1643060-countrycode?language=objc)의 [priceLocale](https://developer.apple.com/documentation/storekit/skproduct/1506145-pricelocale?language=objc) 를 전달해야 함\)

Play 스토어 구독에 대한 구독 추적 파라미터

* [가격](https://developer.android.com/reference/com/android/billingclient/api/SkuDetails#getpriceamountmicros)
* [통화](https://developer.android.com/reference/com/android/billingclient/api/SkuDetails#getpricecurrencycode)
* [sku](https://developer.android.com/reference/com/android/billingclient/api/Purchase#getsku)
* [주문 ID](https://developer.android.com/reference/com/android/billingclient/api/Purchase#getorderid)
* [서명](https://developer.android.com/reference/com/android/billingclient/api/Purchase#getsignature)
* [purchaseToken\(구매 토큰\)](https://developer.android.com/reference/com/android/billingclient/api/Purchase#getpurchasetoken)
* [purchaseTime\(구매 시간\)](https://developer.android.com/reference/com/android/billingclient/api/Purchase#getpurchasetime)

**참고:** Adjust SDK의 구독 추적 API를 사용하려면 모든 파라미터가 `string`값으로 전달되어야 합니다. 위에 기술한 파라미터는 구독 추적 이전에 구독 개체로 전달되어야 합니다. Cordova에는 인앱 구매를 처리하는 다양한 라이브러리가 있으며, 구독 구매가 완료된 후 각각 위에 기술된 정보를 특정 형태로 반환해야 합니다. 인앱 구매를 위해 사용하고 있는 라이브러리에서 얻은 응답으로 이 파라미터가 어디에 있는지 확인한 후 그 값을 추출하고, Adjust API에 문자열 값으로 전달해야 합니다.

이벤트 추적과 마찬가지로 콜백 및 파트너 파라미터를 구독 객체에 연결할 수 있습니다.

**앱스토어 구독용** 

```js
var subscription = new AdjustAppStoreSubscription(price, currency, transactionId, receipt);
subscription.setTransactionDate(transactionDate);
subscription.setSalesRegion(salesRegion);// add callback parameters
subscription.addCallbackParameter("key", "value");
subscription.addCallbackParameter("foo", "bar");

// add partner parameters
subscription.addPartnerParameter("key", "value");
subscription.addPartnerParameter("foo", "bar");

Adjust.trackAppStoreSubscription(subscription);
```

**플레이 스토어 구독용** 

```js
var subscription = new AdjustPlayStoreSubscription(price, currency, sku, orderId, signature, purchaseToken);
subscription.setPurchaseTime(purchaseTime);

// add callback parameters
subscription.addCallbackParameter("key", "value");
subscription.addCallbackParameter("foo", "bar");

// add partner parameters
subscription.addPartnerParameter("key", "value");
subscription.addPartnerParameter("foo", "bar");

Adjust.trackPlayStoreSubscription(subscription);
```

### 세션 파라미터

일부 파라미터는 저장되어 Adjust SDK의 모든 이벤트 및 세션에 전송됩니다. 이러한 파라미터를 한 번 추가하면 로컬로 저장되기 때문에 매번 추가할 필요가 없습니다. 동일한 파라미터를 다시 추가해도 아무 일도 일어나지 않습니다.

이러한 세션 파라미터는 설치 중에도 전송될 수 있도록 Adjust SDK가 실행되기 전에 호출될 수 있습니다. 설치 시에 파라미터를 전송해야 하지만 필요한 값을 실행 이후에만 확보할 수 있는 경우, 이러한 동작이 가능하게 하려면 Adjust SDK의 첫 실행을 [연기](#delay-start)하면 됩니다.

### 세션 콜백 파라미터

Adjust SDK의 모든 이벤트 또는 세션에서 전송될 [이벤트](#callback-parameters)를 위해 등록된 동일한 콜백 파라미터를 저장할 수 있습니다.

세션 콜백 파라미터는 이벤트 콜백 파라미터와 유사한 인터페이스를 가집니다. 키와 값을 이벤트에 추가하는 대신, `Adjust` 인스턴스의 `addSessionCallbackParameter`로의 호출을 통해 추가합니다.

```js
Adjust.addSessionCallbackParameter("foo", "bar");
```

세션 콜백 파라미터는 콜백 파라미터와 병합되며 이벤트에 추가됩니다. 이벤트에 추가된 콜백 파라미터는 세션 콜백 파라미터보다 높은 우선순위를 가집니다. 세션에서 추가된 것과 동일한 키로 콜백 파라미터를 이벤트에 추가하면 이벤트에 추가된 콜백 파라미터의 값이 우선시됩니다.

원하는 키를 `Adjust` 인스턴스의 `removeSessionCallbackParameter` 메서드에 전달하여 특정 세션 콜백 파라미터를 삭제할 수 있습니다.

```js
Adjust.removeSessionCallbackParameter("foo");
```

세션 콜백 파라미터에서 모든 키와 값을 삭제하려면 `Adjust` 인스턴스의 `resetSessionCallbackParameters` 메서드로 재설정하면 됩니다.

```js
Adjust.resetSessionCallbackParameters();
```

### 세션 파트너 파라미터

Adjust SDK의 모든 이벤트 또는 세션마다 전송되는 [세션 콜백 파라미터](#session-callback-parameters)가 있는 것처럼, 세션 파트너 파라미터도 있습니다.

이러한 파라미터는 사용자의 Adjust [대시보드](http://adjust.com)에서 활성화된 네트워크 파트너 연동에 전송됩니다.

세션 파트너 파라미터는 이벤트 파트너 파라미터와 유사한 인터페이스를 가집니다. 키와 값을 이벤트에 추가하는 대신, `Adjust` 인스턴스의 `addSessionPartnerParameter`로의 호출을 통해 추가합니다.

```js
Adjust.addSessionPartnerParameter("foo", "bar");
```

세션 파트너 파라미터는 이벤트에 추가된 파트너 파라미터와 병합됩니다. 이벤트에 추가된 파트너 파라미터는 세션 파트너 파라미터보다 높은 우선순위를 가집니다. 세션에서 추가된 것과 동일한 키로 파트너 파라미터를 이벤트에 추가하면 이벤트에 추가된 파트너 파라미터의 값이 우선시됩니다.

원하는 키를 `Adjust` 인스턴스의 `removeSessionPartnerParameter` 메서드에 전달하여 특정 세션 파트너 파라미터를 삭제할 수 있습니다.

```js
Adjust.removeSessionPartnerParameter("foo");
```

세션 파트너 파라미터에서 모든 키와 값을 삭제하려면 `Adjust` 인스턴스의 `resetSessionPartnerParameters` 메서드로 재설정하면 됩니다.

```js
Adjust.resetSessionPartnerParameters();
```

### 시작 지연

Adjust SDK의 시작을 지연시키면 앱이 고유 ID와 같은 세션 파라미터를 획득할 시간이 확보되므로, 세션 파라미터를 설치 시에 전송할 수 있게 됩니다.

`AdjustConfig` 인스턴스의 `setDelayStart` 필드로 초기 지연 시간을 초 단위로 설정하세요.

```js
adjustConfig.setDelayStart(5.5);
```

이렇게 설정하면 Adjust SDK가 초기 설치 세션과 5\.5초 이내로 생성된 이벤트를 전송하지 않습니다. 이 시간이 만료되거나 그동안 `Adjust` 클래스의 `sendFirstPackages()`를 호출하면 모든 세션 파라미터가 지연된 설치 세션 및 이벤트에 추가되며 Adjust SDK가 평소대로 되돌아갑니다.

**Adjust SDK의 최대 시작 지연 시간은 10초입니다** .

### 어트리뷰션 콜백

리스너를 등록하여 트래커 어트리뷰션의 변경 사항에 대한 알림을 받을 수 있습니다. 어트리뷰션에는 다양한 소스가 관련되어 있기 때문에 이 정보는 즉각적으로 제공될 수 없습니다. 가장 간단한 방법은 단일 익명 수신기를 생성하여 **사용자의 어트리뷰션 값이 변경될 때마다** 호출되도록 하는 것입니다.

SDK를 시작하기 전에 `AdjustConfig` 인스턴스를 사용하여 익명 리스너를 추가합니다.

```js
var adjustConfig = new AdjustConfig(appToken, environment);

adjustConfig.setAttributionCallbackListener(function(attribution) {
// Printing all attribution properties.
    console.log("Attribution changed!");
    console.log(attribution.trackerToken);
    console.log(attribution.trackerName);
    console.log(attribution.network);
    console.log(attribution.campaign);
    console.log(attribution.adgroup);
    console.log(attribution.creative);
    console.log(attribution.clickLabel);
    console.log(attribution.adid);
});

Adjust.create(adjustConfig);
```

리스너 함수 내에서 `attribution` 파라미터에 액세스할 수 있습니다. 그 속성에 대한 요약 정보는 다음과 같습니다.

* `trackerToken` 현재 어트리뷰션의 트래커 토큰.
* `trackerToken` 현재 어트리뷰션의 트래커 토큰.
* `network` 현재 어트리뷰션의 네트워크 그룹화 수준.
* `campaign` 현재 어트리뷰션의 캠페인 그룹화 수준.
* `network` 현재 어트리뷰션의 광고 그룹 그룹화 수준.
* `network` 현재 어트리뷰션의 크리에이티브 그룹화 수준.
* `clickLabel` 현재 어트리뷰션의 클릭 레이블.
* `adid` Adjust 기기 식별자.

[해당되는 어트리뷰션 데이터 정책](https://github.com/adjust/sdks/blob/master/doc/attribution-data.md)을 고려해야 합니다.

### 세션 및 이벤트 콜백

콜백을 등록하여 성공적으로 추적되었거나 추적에 실패한 이벤트 및/또는 세션에 대한 알림을 받을 수 있습니다.

성공적으로 추적된 이벤트에 대한 콜백 함수를 구현하려면 어트리뷰션 콜백과 동일한 단계를 따르세요.

```js
var adjustConfig = new AdjustConfig(appToken, environment);

adjustConfig.setEventTrackingSucceededCallbackListener(function(eventSuccess) {
// Printing all event success properties.
    console.log("Event tracking succeeded!");
    console.log(eventSuccess.message);
    console.log(eventSuccess.timestamp);
    console.log(eventSuccess.eventToken);
    console.log(eventSuccess.callbackId);
    console.log(eventSuccess.adid);
    console.log(eventSuccess.jsonResponse);
});

Adjust.create(adjustConfig);
```

다음 콜백 함수는 실패한 이벤트에 사용됩니다.

```js
var adjustConfig = new AdjustConfig(appToken, environment);

adjustConfig.setEventTrackingFailedCallbackListener(function(eventFailure) {
// Printing all event failure properties.
    console.log("Event tracking failed!");
    console.log(eventFailure.message);
    console.log(eventFailure.timestamp);
    console.log(eventFailure.eventToken);
    console.log(eventFailure.callbackId);
    console.log(eventFailure.adid);
    console.log(eventFailure.willRetry);
    console.log(eventFailure.jsonResponse);
});

Adjust.create(adjustConfig);
```

성공적으로 추적된 세션의 경우:

```js
var adjustConfig = new AdjustConfig(appToken, environment);

adjustConfig.setSessionTrackingSucceededCallbackListener(function(sessionSuccess) {
// Printing all session success properties.
    console.log("Session tracking succeeded!");
    console.log(sessionSuccess.message);
    console.log(sessionSuccess.timestamp);
    console.log(sessionSuccess.adid);
    console.log(sessionSuccess.jsonResponse);
});

Adjust.create(adjustConfig);
```

추적에 실패한 세션의 경우:

```js
var adjustConfig = new AdjustConfig(appToken, environment);

adjustConfig.setSessionTrackingFailedCallbackListener(function(sessionFailure) {
// Printing all session failure properties.
    console.log("Session tracking failed!");
    console.log(sessionFailure.message);
    console.log(sessionFailure.timestamp);
    console.log(sessionFailure.adid);
    console.log(sessionFailure.willRetry);
    console.log(sessionFailure.jsonResponse);
});

Adjust.create(adjustConfig);
```

SDK가 패키지를 서버로 전송하려고 시도한 후에 콜백 함수가 호출됩니다. 콜백 내에서 콜백에 대한 반응 데이터 개체에 액세스할 수 있습니다. 세션 반응 데이터 속성에 대한 요약 정보는 다음과 같습니다.

* `var message` 서버로부터의 메시지 또는 SDK에 의해 로깅된 오류.
* `var timestamp` 서버의 타임스탬프.
* `var adid` Adjust에서 제공하는 고유 기기 식별자.
* `var jsonResponse` 서버로부터의 응답을 포함하는 JSON 개체.

두 이벤트 응답 데이터 개체는 다음을 포함합니다.

* `var eventToken` 추적된 패키지가 이벤트인 경우 해당 이벤트 토큰.
* `var callbackId` 이벤트 오브젝트에 맞춤 설정된 콜백 ID.

두 이벤트 및 세션 실패 개체는 다음을 포함합니다.

* `var willRetry` 이후 패키지 재전송 시도가 있을 것임을 알립니다.

### 추적 비활성화

`Adjust` 인스턴스의 `setEnabled` 메서드를 호출하고 활성화된 파라미터를 `false`로 설정하여 Adjust SDK의 추적을 비활성화할 수 있습니다. 이 설정은 **세션 간에 유지되지만** , 첫 세션 이후에만 활성화될 수 있습니다.

```js
Adjust.setEnabled(false);
```

Adjust SDK가 `Adjust` 인스턴스의 `IsEnabled` 메서드를 사용하여 현재 활성화된 경우 검증할 수 있습니다. 언제든지 `setEnabled` 메서드를 호출하고 파라미터를 `true`로 설정하여 Adjust SDK를 활성화할 수 있습니다.

### 오프라인 모드

Adjust 서버에 대한 전송을 연기하고 추적된 데이터가 이후에 전송되도록 유지함으로써 Adjust SDK를 오프라인 모드로 설정할 수 있습니다. 오프라인 모드에서는 모든 정보가 파일에 저장되기 때문에 너무 많은 이벤트를 발생시키지 않도록 주의해야 합니다.

`Adjust` 인스턴스의 `setOfflineMode` 메서드를 호출하고 파라미터를 `true`로 설정하여 오프라인 모드를 활성화할 수 있습니다.

```js
Adjust.setOfflineMode(true);
```

반대로 `setOfflineMode`를 `false`로 호출하여 오프라인 모드를 비활성화할 수 있습니다. Adjust SDK가 다시 온라인 모드가 되면 저장된 모든 정보가 정확한 시간 정보와 함께 Adjust 서버로 전송됩니다.

추적 비활성화와는 다르게 **이 설정은 세션 간에 유지되지 않습니다** . 즉, 앱이 오프라인 모드에서 종료되었더라도 Adjust SDK는 항상 온라인 모드로 시작됩니다.

### 이벤트 버퍼링

앱이 이벤트 추적을 많이 사용하는 경우, 일부 HTTP 요청을 연기하여 HTTP 요청을 1분에 한 번씩 일괄로 보내고자 할 수 있습니다. `setEventBufferingEnabled` 메서드를 호출하여 `AdjustConfig` 인스턴스를 통해 이벤트 버퍼링을 활성화할 수 있습니다.

```js
var adjustConfig = new AdjustConfig(appToken, environment);
adjustConfig.setEventBufferingEnabled(true);
Adjust.create(adjustConfig);
```

### GDPR 잊혀질 권리

EU의 개인정보보호법\(GDPR\) 제 17조에 따라, 사용자는 잊혀질 권리\(Right to be Forgotten\)를 행사했음을 Adjust에 알릴 수 있습니다. 다음 메서드를 호출하면 Adjust SDK가 잊혀질 권리에 대한 사용자의 선택과 관련된 정보를 Adjust 백엔드에 보냅니다.

```js
Adjust.gdprForgetMe();
```

이 정보를 수신한 후 Adjust는 해당 사용자의 데이터를 삭제하며 Adjust SDK는 해당 사용자에 대한 추적을 중지합니다. 이 기기로부터의 요청은 향후 Adjust에 전송되지 않습니다.

### \#\#\# <a id="disable-third-party-sharing"></a>특정 사용자의 경우 제3자 공유 비활성화

이제 사용자가 마케팅 목적으로 파트너와 데이터가 공유되지 않도록 중단할 수 있는 권리를 행사했으나 통계 목적으로는 공유할 수 있도록 허용한 경우, 이를 Adjust에 알릴 수 있습니다.

다음 메서드를 호출하여 Adjust SDK가 데이터 공유 비활성화에 대한 사용자의 선택과 관련된 정보를 Adjust 백엔드에 보냅니다:

```cs
Adjust.disableThirdPartySharing();
```

이 정보를 수신하면 Adjust는 특정 사용자의 데이터를 파트너와 공유하는 것을 차단하고 Adjust SDK는 계속 정상적으로 작동합니다.

### SDK Signature\(SDK 서명\)

계정 관리자는 Adjust SDK 서명을 활성화해야 합니다. 이 기능의 사용에 관심이 있는 경우 Adjust 고객 지원팀\([support@adjust.com](mailto:support@adjust.com)\)에 문의하시기 바랍니다.

SDK 서명이 이미 계정에서 활성화되어 있으며 Adjust 대시보드의 App Secret에 액세스할 수 있는 경우, 아래 방법을 사용하여 SDK 서명을 앱에 연동하세요.

App Secret은 모든 비밀 파라미터\(`secretId`, `info1`, `info2`, `info3`, `info4`\)를 `AdjustConfig` 인스턴스의 `setAppSecret` 메서드에 전달하여 설정됩니다.

```js
var adjustConfig = new AdjustConfig(appToken, environment);
adjustConfig.setAppSecret(secretId, info1, info2, info3, info4);
Adjust.create(adjustConfig);
```

### 백그라운드 추적

Adjust SDK는 기본적으로 **앱이 백그라운드에서 작동하는 동안 HTTP 요청 전송을 일시 중지** 하도록 설정되어 있습니다. 이 설정은 `setSendInBackground` 메서드를 호출하여 `AdjustConfig` 인스턴스에서 변경할 수 있습니다.

```js
var adjustConfig = new AdjustConfig(appToken, environment);
adjustConfig.setSendInBackground(true);
Adjust.create(adjustConfig);
```

아무 것도 설정되지 않으면 백그라운드 전송이 **기본적으로 비활성화됩니다** .

### 기기 ID

특정 서비스\(예: Google Analytics\)는 중복 보고를 방지하기 위해 기기 및 클라이언트 ID 통합을 요청합니다.

### iOS 광고 ID<a id="di-idfa"></a>

IDFA를 획득하려면 `Adjust` 인스턴스의 `getIdfat` 메서드를 호출합니다. 값을 획득하려면 해당 메서드로 콜백을 전달해야 합니다.

```js
Adjust.getIdfa(function(idfa) {
    // Use idfa value.
});
```

### Google Play 서비스 광고 ID

Google 광고 ID를 획득하려면 `Adjust` 인스턴스의 `getGoogleAdId` 메서드를 호출하면 됩니다. 값을 획득하려면 해당 메서드로 콜백을 전달해야 합니다.

```js
Adjust.getGoogleAdId(function(googleAdId) {
    // Use googleAdId value.
});
```

콜백 메서드에서 `googleAdId` 변수로 Google 광고 ID에 액세스할 수 있습니다.

### Amazon 광고 ID

Amazon 광고 ID를 획득하려면 `Adjust` 인스턴스의 `getAmazonAdID` 메서드를 호출하면 됩니다.

```js
Adjust.getAmazonAdId(function(amazonAdId) {
    // Use amazonAdId value.
});
```

콜백 메서드에서 `amazonAdId` 변수로 Amazon 광고 ID에 액세스할 수 있습니다.

### Adjust 기기 식별자

앱이 설치된 모든 기기에 대해 Adjust 백엔드는 고유한 **Adjust 기기 식별자** \( **adid** \)를 생성합니다. 이 식별자를 획득하려면 `Adjust` 인스턴스의 `getAdid` 메서드를 호출하면 됩니다. 값을 획득하려면 해당 메서드로 콜백을 전달해야 합니다.

```js
Adjust.getAdid(function(adid) {
    // Use adid value.
});
```

**참고** : **adid** 정보는 Adjust 백엔드가 앱의 설치를 추적한 다음에만 확보할 수 있습니다. 그 다음부터는 Adjust SDK가 기기 **adid** 정보를 보유하게 되며, 이 메서드를 사용하여 해당 정보에 액세스할 수 있습니다. 따라서 SDK가 초기화되고 앱 설치가 추적되기 전까지는 **adid** 값에 액세스할 수 **없습니다** .

### 사용자 어트리뷰션

<a id="di-idfa"></a>

이 콜백은 <a id="di-idfa"></a>[어트리뷰션 콜백 섹션](#attribution-callback)에 설명된 대로 실행되며, 변경될 때마다 새 어트리뷰션에 대한 정보를 제공합니다. 사용자의 현재 애트리뷰션에 대해 언제든지 액세스하고 싶다면, `Adjust` 인스턴스의 `getAttribution` 메서드를 호출하면 됩니다.

```js
Adjust.getAttribution(function(attribution) {
    // Use attribution object in same way like in attribution callback.
});
```

**참고** : 사용자의 현재 어트리뷰션 상태에 대한 정보는 Adjust 백엔드가 앱의 설치를 추적하고 어트리뷰션 콜백이 실행된 다음에만 사용할 수 있습니다. 그 다음부터는 Adjust SDK가 사용자의 어트리뷰션 상태를 보유하게 되며, 이 메서드를 사용하여 해당 정보에 액세스할 수 있습니다. 따라서 SDK가 초기화되고 어트리뷰션 콜백이 실행되기 전까지는 사용자의 어트리뷰션 값에 액세스할 수 **없습니다** .

### <a id="push-token"></a>푸시 토큰

Adjust에 푸시 알림 토큰을 전송하려면 **앱이 토큰을 수신하거나 업데이트될 때** 다음 호출을 Adjust에 추가하세요.

```js
Adjust.setPushToken("YourPushNotificationToken");
```

푸시 토큰은 Audience Builder 및 클라이언트 콜백에 사용되며 향후 출시될 삭제 추적 기능에 필요합니다.

### <a id="pre-installed-trackers"></a>사전 설치 트래커

Adjust SDK를 사용하여 본인의 앱을 발견하고 기기에 사전 설치한 사용자를 식별하려면 다음 단계를 따르세요.

1. [대시보드](http://adjust.com)에서 새 트래커를 생성합니다.

2. 앱 델리게이트를 열고 `AdjustConfig` 인스턴스의 기본 트래커를 설정합니다.

   ```js
   var adjustConfig = new AdjustConfig(appToken, environment);
   adjustConfig.setDefaultTracker("{TrackerToken}");
   Adjust.create(adjustConfig);
   ```

`{TrackerToken}`을 2단계에서 생성한 트래커 토큰으로 교체합니다. 대시보드에는 트래커 URL이 표시됩니다\(`http://app.adjust.com/` 포함\). 소스 코드에서 전체 URL이 아닌 6글자의 토큰만 지정해야 합니다.

1. 앱을 빌드하고 실행합니다. 앱의 로그 출력에서 다음과 같은 라인이 표시됩니다.

       Default tracker: 'abc123'

### <a id="deeplinking"></a>딥링크

Adjust 트래커 URL을 사용하며 URL로부터 앱으로 딥링킹하는 옵션을 설정한 경우, 딥링크 및 그 콘텐츠에 대한 정보를 얻을 수 있습니다. 사용자가 앱을 이미 설치한 경우\(표준 딥링크 시나리오\) 또는 기기에 앲이 없는 경우\(지연 딥링크 시나리오\)에 URL 조회가 발생할 수 있습니다.

### <a id="deeplinking-standard"></a>표준 딥링크 시나리오

표준 딥링크 시나리오는 플랫폼에 특화된 기능으로, 이를 지원하려면 앱에 추가적인 설정을 더해야 합니다. 만일 사용자가 이미 앱을 설치하였고 딥링크 정보가 담긴 트래커 URL에 도달하였다면, 앱이 열리고 딥링크의 내용이 앱으로 전송되어 파싱 및 다음 작업을 결정할 수 있습니다.

**iOS용 참고** : iOS 9가 나오면서 Apple은 에서 딥링크 처리 방식을 변경했습니다. 앱에 사용하려는 시나리오\(또는 다양한 기기를 지원하기 위해 둘 다 사용하려는 경우\)에 따라 다음 시나리오 중 하나 또는 둘 다를 처리하도록 앱을 설정해야합니다.

### 안드로이드와 iOS 8 이하 버전에서의 딥링크

안드로이드와 iOS 8 이하 버전에서 딥링크 처리를 지원하려면 `커스텀 URL 스킴` 플러그인을 [여기](https://github.com/EddyVerbruggen/Custom-URL-scheme)에서 사용하면 됩니다..

해당 플러그인을 성공적으로 연동 후, 본 [섹션](https://github.com/EddyVerbruggen/Custom-URL-scheme#3-usage)에서 언급된 플러그인에 사용되었던 콜백 메서드를 통해 사용자의 기기에서 앱을 열은 URL 내용에 액세스할 수 있습니다.

```js
function handleOpenURL(url) {
    setTimeout(function () {
        // Check content of the url object and get information about the URL.
    }, 300);
};
```

이 플러그인의 연동이 완료되면, **안드로이드와 iOS 8 이하** 에서도 딥링크를 처리할 수 있습니다.

### iOS 9 이상 버전에서의 딥링크

**iOS 9** 가 나오면서 Apple은 위에 기술된 기존의 커스텀 URL 스킴을 더 이상 지원하지 않으며, `유니버설 링크`를 사용해야 합니다. iOS 9 이상 버전에서 앱에 딥링크를 지원하고 싶은 경우, 유니버설 링크 처리에 대한 지원을 추가해야 합니다.

우선 Adjust 대시보드에서 앱의 유니버설 링크를 활성화해야 합니다. 이에 대한 정보는 Adjust의 iOS SDK [설명서](https://github.com/adjust/ios_sdk#deeplinking-setup-new)에서 확인할 수 있습니다.

대시보드에서 앱의 유니버설 링크 처리를 활성화한 이후, 앱에도 이에 대한 지원을 추가해야 합니다. 이는 Cordova 앱에 본 [플러그인](https://github.com/nordnet/cordova-universal-links-plugin)을 추가하면 됩니다. 본 플러그인의 설명서에는 성공적인 통합을 위해 수행되어야 하는 과정들이 자세히 명시되어 있으니 꼭 읽기 바랍니다.

**참고** : 본 설명서에서 도메인/웹사이트가 있어야 하거나 루트 도메인에 파일을 업로드해야한다는 내용은 무시해도 됩니다. 이 부분은 Adjust가 관리하고 있으니, 설명서의 이 부분은 건너뛰어도 됩니다. 또한, 안드로이드 플랫폼에서는 본 플러그인의 설명을 따르지 않아도 됩니다. 안드로이드의 딥링크는 여전히 `커스텀 URL 스킴` 플러그인을 통해 처리되기 때문입니다.

Adjust의 대시보드에서 앱의 유니버설 링크를 활성화한 이후 `Cordova 유니버설 링크 플러그인`의 연동을 완료하려면 다음의 과정이 반드시 수행되어아 합니다.

### `config.xml`파일 수정

`config.xml` 파일에 다음의 엔트리를 추가해야 합니다.

```xml
<widget>
    <universal-links>
        <host name="[hash].adj.st" scheme="https" event="adjustDeepLinking" />
    </universal-links>
</widget>
```

`[hash]` 값은 Adjust 대시보드에서 생성한 값으로 바꿔야 합니다. 또한 원하는 이름으로 이벤트 이름을 변경할 수 있습니다.

### 플러그인의 내용은 `ul_web_hooks/ios/`에서 확인할 수 있습니다.

앱의 `Cordova 유니버설 링크 플러그인` 설치 경로에서 `ul_web_hooks/ios/` 폴더의 내용을 확인하기 바랍니다. 해당 폴더에는 `[hash].ulink.adjust.com#apple-app-site-association`라는 이름으로 생성된 파일이 있습니다. 해당 파일의 내용은 다음와 같아야 합니다.

    {
      "applinks": {
        "apps": [],
        "details": [
          {
            "appID": "<YOUR_TEAM_ID_FROM_MEMBER_CENTER>.com.adjust.examples",
            "paths": [
              "/ulink/*"
            ]
          }
        ]
      }
    }

### `index.js` 파일에 플러그인 통합

`deviceready` 이벤트의 발생 이후, `config.xml` 파일에서 정의한 이벤트를 구독해야 하며, 해당 이벤트가 발생할 때 실행되는 콜백 메서드를 정의해야 합니다. 안드로이드에서는 딥링크를 처리하기 위한 본 플러그인이 필요하지 않으며, 앱이 iOS 기기에서 시행되는 경우에만 이를 구독해야 합니다.

```js
// ...

var app = {
    initialize: function() {
        this.bindEvents();
    },

    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },

    onDeviceReady: function() {
        if (device.platform == "iOS") {
            universalLinks.subscribe('adjustDeepLinking', app.didLaunchAppFromLink);
        }
    },

    didLaunchAppFromLink: function(eventData) {
        // Check content of the eventData.url object and get information about the URL.
    }
}
// ...
```

본 과정이 완료되면 iOS 9 이상 버전에서 딥링크에 대한 지원이 성공적으로 추가됩니다.

### 디퍼드 딥링크 시나리오

Android와 iOS에서 지연 딥링크가 바로 지원되지 않지만, Adjust SDK를 통해 지연 딥링크를 사용할 수 있습니다.

디퍼드 딥링크 시나리오에서 URL 콘텐츠에 대한 정보를 얻으려면 URL 콘텐츠가 전달되는 파라미터 하나를 수신하는 `AdjustConfig` 개체에 콜백 메서드를 설정해야 합니다. `setDeeplinkCallbackListener` 메서드를 호출하여 설정 개체에 이 메서드를 설정해야 합니다.

```js
var adjustConfig = new AdjustConfig(appToken, environment);

adjustConfig.setDeferredDeeplinkCallbackListener(function(deeplink) {
    console.log("Deferred deep link URL content: " + deeplink);
});

Adjust.create(adjustConfig);
```

디퍼드 딥링크 시나리오에서 `AdjustConfig` 개체에 설정할 수 있는 추가 설정이 하나 있습니다. Adjust SDK가 디퍼드 딥링크 정보를 획득하면 SDK가 이 URL을 열도록 할지 선택할 수 있습니다. 설정 개체의 `setShouldLaunchDeeplink` 메서드를 호출하여 이 옵션을 설정할 수 있습니다.

```js
var adjustConfig = new AdjustConfig(appToken, environment);

adjustConfig.setShouldLaunchDeeplink(true);
// or adjustConfig.setShouldLaunchDeeplink(false);adjustConfig.setDeeplinkCallbackListener(function(deeplink) {
    console.log("Deferred deep link URL content: " + deeplink);
});

Adjust.create(adjustConfig);
```

아무 것도 설정되지 않은 경우 **Adjust SDK는 기본적으로 항상 URL 실행을 시도합니다** .

### 딥링크를 통한 리어트리뷰션

Adjust를 사용하면 딥링크를 사용하여 리타겟팅 캠페인을 실행할 수 있습니다. 이에 대한 자세한 정보는 [공식 문서](https://docs.adjust.com/en/deeplinking/#manually-appending-attribution-data-to-a-deep-link)에서 참조하실 수 있습니다.

이 기능을 사용하는 경우, 사용자에 대한 리어트리뷰션이 적절히 이루어지려면 앱에서 Adjust SDK에 대한 추가적인 호출을 수행해야 합니다.

앱의 딥링크 콘텐츠 정보를 수신했으면 `Adjust` 인스턴스의 `appWillOpenUrl` 메서드에 호출을 추가합니다. 이 호출을 수행함으로써 Adjust SDK는 딥링크 내부에서 새 어트리뷰션 정보를 찾으며, 정보가 있는 경우에는 Adjust 백앤드에 전송됩니다. 딥링크 콘텐츠를 포함하는 Adjust 트래커 URL에 대한 클릭으로 인해 사용자에 대한 리어트리뷰션이 이루어져야 하는 경우, 앱에서 해당 사용자에 대한 새 어트리뷰션 정보와 함께 [어트리뷰션 콜백](#attribution-callback)이 실행되는 것을 볼 수 있습니다.

위에 명시된 코드 예시에서 `appWillOpenUrl` 메서드의 호출은 다음과 같이 수행되어야 합니다.

```js
function handleOpenURL(url) {
    setTimeout(function () {
        // Check content of the url object and get information about the URL.
        Adjust.appWillOpenUrl(url);
    }, 300);
};
```

```js
// ...

var app = {
    initialize: function() {
        this.bindEvents();
    },

    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },

    onDeviceReady: function() {
        if (device.platform == "iOS") {
            universalLinks.subscribe('adjustDeepLinking', app.didLaunchAppFromLink);
        }
    },

    didLaunchAppFromLink: function(eventData) {
        // Check content of the eventData.url object and get information about the URL.
        Adjust.appWillOpenUrl(eventData.url);
    }
}
// ...
```

라이선스
----

Adjust SDK는 MIT 라이센스 하에 사용이 허가됩니다.

Copyright \(c\) 2012\-2018 Adjust GmbH, http://www.adjust.com

다음 조건하에서 본 소프트웨어와 관련 문서 파일\(이하 "소프트웨어"\)의 사본을 보유한 제3자에게 소프트웨어의 사용, 복사, 수정, 병합, 게시, 배포, 재실시권 및/또는 사본의 판매 등을 포함하여 소프트웨어를 제한 없이 사용할 수 있는 권한을 무료로 부여하며, 해당 제3자는 소프트웨어를 보유한 이에게 이러한 이용을 허가할 수 있습니다.

본 소프트웨어의 모든 사본 또는 상당 부분에 위 저작권 공고와 본 권한 공고를 포함해야 합니다.

소프트웨어는 "있는 그대로" 제공되며, 소프트웨어의 상품성과 특정 목적에의 적합성 및 비 침해성에 대해 명시적이거나 묵시적인 일체의 보증을 하지 않습니다. 저자 또는 저작권자는 본 소프트웨어와 이의 사용 또는 기타 소프트웨어 관련 거래로 인해 발생하는 모든 클레임, 손해 또는 기타 법적 책임에 있어서 계약 또는 불법 행위와 관련된 소송에 대해 어떠한 책임도 부담하지 않습니다.

