## Summary

This is the Cordova SDK of adjust™. You can read more about adjust™ at
[adjust.com].

N.B. At the moment, SDK 4.1.0 for Cordova supports Android platform version `4.0.0 and higher`
and iOS platform version `3.0.0 and higher`.

## Basic Installation

These are the minimal steps required to integrate the adjust SDK into your
Cordova project. We are going to assume the command line interface is used.

### 1. Get the SDK

Download the latest version from our [releases page][releases]. Extract the
zip file in a folder of your choice.

### 2. Add the SDK to your project

After you added iOS and/or Android as a platform for your project, enter the
following command in your project folder:

```
> cordova plugin add path_to_folder/cordova_sdk
Installing "com.adjust.sdk" for android
Installing "com.adjust.sdk" for ios
```

### 3. Integrate with your app

The adjust plugin automatically registers with the Cordova events `deviceready`, 
`resume` and `pause`.

#### Basic setup

In your `index.js` file after you have received `deviceready` event, add the 
following code to initialize the adjust SDK:

```javascript
var adjustConfig = new AdjustConfig("{YourAppToken}", AdjustConfig.EnvironmentSandbox);

Adjust.create(adjustConfig);
```

Replace `{YourAppToken}` with your app token. You can find this in your [dashboard].

Depending on whether you build your app for testing or for production, you must
set `environment` with one of these values:

```javascript
AdjustConfig.EnvironmentSandbox
AdjustConfig.EnvironmentProduction
```

**Important:** This value should be set to `AdjustConfig.EnvironmentSandbox`
if and only if you or someone else is testing your app. Make sure to set the
environment to `AdjustConfig.EnvironmentProduction` just before you publish
the app. Set it back to `AdjustConfig.EnvironmentSandbox` when you start
developing and testing it again.

We use this environment to distinguish between real traffic and test traffic
from test devices. It is very important that you keep this value meaningful at
all times! This is especially important if you are tracking revenue.

#### Adjust Logging

You can increase or decrease the amount of logs you see in tests by calling
`setLogLevel` on your `AdjustConfig` instance with one of the following
parameters:

```javascript
adjustConfig.setLogLevel(AdjustConfig.LogLevelVerbose);   // enable all logging
adjustConfig.setLogLevel(AdjustConfig.LogLevelDebug);     // enable more logging
adjustConfig.setLogLevel(AdjustConfig.LogLevelInfo);      // the default
adjustConfig.setLogLevel(AdjustConfig.LogLevelWarn);      // disable info logging
adjustConfig.setLogLevel(AdjustConfig.LogLevelError);     // disable warnings as well
adjustConfig.setLogLevel(AdjustConfig.LogLevelAssert);    // disable errors as well
```

### 4. Google Play Services

Since the 1st of August of 2014, apps in the Google Play Store must use the 
[Google Advertising ID][google_ad_id] to uniquely identify the devices. To allow the adjust SDK 
to use the Google Advertising ID, you must integrate the [Google Play Services][google_play_services].

The adjust SDK plugin adds Google Play Services by default to your app.

If you are using Proguard, add these lines to your Proguard file:

````
-keep class com.adjust.sdk.** { *; }
-keep class com.google.android.gms.common.** { *; }
-keep class com.google.android.gms.ads.identifier.** { *; }
```

If you don't want to use Google Play Services in your app, you can remove them by editing `plugin.xml`
file of the adjust SDK plugin. Go to `plugins/com.adjust.sdk` folder and open `plugin.xml` file.
As part of the `<platform name="android">`, you can find following line which adds Google Play Services
dependency:

```xml
<framework src="com.google.android.gms:play-services-ads:+" />
```

If you want to remove Google Play Services, simply remove this line, save your changes and rebuild
your app.

## Additional Features

Once you have integrated the adjust SDK into your project, you can take
advantage of the following features.

### 5. Add tracking of custom events

You can use adjust to track events. You should create a new event token in your 
dashboard, which has an associated event token - looking something like `abc123`. 
In your app you would then add the following lines to track the event you are 
interested in:

```javascript
var adjustEvent = new AdjustEvent("abc123");
Adjust.trackEvent(adjustEvent);
```

The event instance can be used to configure the event even more before tracking
it.

### 6. Add tracking of revenue

If your users can generate revenue by tapping on advertisements or making
in-app purchases you can track those revenues with events. Lets say a tap is
worth one Euro cent. You could then track the revenue event like this:

```javascript
var adjustEvent = new AdjustEvent("abc123");
adjustEvent.setRevenue(0.01, "EUR");
Adjust.trackEvent(adjustEvent);
```

This can be combined with callback parameters of course.

When you set a currency token, adjust will automatically convert the incoming revenues 
into a reporting revenue of your choice. Read more about [currency conversion here.][currency-conversion]

You can read more about revenue and event tracking in the [event tracking guide.][event-tracking]

### 7. Add callback parameters

You can register a callback URL for your events in your [dashboard]. We will
send a GET request to that URL whenever the event gets tracked. You can add
callback parameters to that event by calling `addCallbackParameter` on the
event instance before tracking it. We will then append these parameters to 
your callback URL.

For example, suppose you have registered the URL
`http://www.adjust.com/callback` then track an event like this:

```javascript
var adjustEvent = new AdjustEvent("abc123");

adjustEvent.addCallbackParameter("key", "value");
adjustEvent.addCallbackParameter("foo", "bar");

Adjust.trackEvent(adjustEvent);
```

In that case we would track the event and send a request to:

```
http://www.adjust.com/callback?key=value&foo=bar
```

It should be mentioned that we support a variety of placeholders like
`{android_id}` that can be used as parameter values. In the resulting callback
this placeholder would be replaced with the AndroidID of the current device.
Also note that we don't store any of your custom parameters, but only append
them to your callbacks. If you haven't registered a callback for an event,
these parameters won't even be read.

You can read more about using URL callbacks, including a full list of available
values, in our [callbacks guide][callbacks-guide].

### 8. Partner parameters

You can also add parameters to be transmitted to network partners, for the
integrations that have been activated in your adjust dashboard.

This works similarly to the callback parameters mentioned above, but can be
added by calling the `addPartnerParameter` method on your `AdjustEvent` instance.

```javascript
var adjustEvent = new AdjustEvent("abc123");

adjustEvent.addPartnerParameter("key", "value");
adjustEvent.addPartnerParameter("foo", "bar");

Adjust.trackEvent(adjustEvent);
```

You can read more about special partners and these integrations in our [guide
to special partners.][special-partners]

### 9. Set up deep link reattributions

You can set up the adjust SDK to handle deep links that are used to open your
app. We will only read certain adjust specific parameters. This is essential if
you are planning to run retargeting or re-engagement campaigns with deep links.

#### iOS

In the XCode Project Navigator, open the source file of your Application Delegate. Find
or add the method `openURL` and add the following call to adjust:

```objc
- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication annotation:(id)annotation
{
    [Adjust appWillOpenUrl:url];
    Bool canHandle = [self someLogic:url];
    return canHandle;
}
```

#### Android

For each activity that accepts deep links, find the `onCreate` or `onNewIntent` 
method and add the following call to adjust:

###### For activities with `standard` launch mode

```java
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Intent intent = getIntent();
    Uri data = intent.getData();
    Adjust.appWillOpenUrl(data);
    // ...
}
```

###### For activities with `singleTop` launch mode

```java
protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);

    Uri data = intent.getData();
    Adjust.appWillOpenUrl(data);
    // ...
}
```

You can read more about activity launch mode on this [page][google-launch-modes].

### 10. Enable event buffering

If your app makes heavy use of event tracking, you might want to delay some
HTTP requests in order to send them in one batch every minute. You can enable
event buffering with your `AdjustConfig` instance:

```javascript
var adjustConfig = new AdjustConfig(appToken, environment);
adjustConfig.setEventBufferingEnabled(true);
Adjust.create(adjustConfig);
```

### 11. Set listener for attribution changes

You can register a listener to be notified of tracker attribution changes. Due
to the different sources considered for attribution, this information can not
by provided synchronously. The simplest way is to create a single anonymous
listener:

Please make sure to consider our [applicable attribution data
policies][attribution-data].

With the `AdjustConfig` instance, before starting the SDK, add the anonymous listener:

```js
var adjustConfig = new AdjustConfig(appToken, environment);

adjustConfig.setCallbackListener(function(attribution) {
});

Adjust.create(adjustConfig);
```

The listener function will be called when the SDK receives the final attribution
information. Within the listener function you have access to the `attribution`
parameter. Here is a quick summary of its properties:

- `trackerToken`    the tracker token of the current install.
- `trackerName`     the tracker name of the current install.
- `network`         the network grouping level of the current install.
- `campaign`        the campaign grouping level of the current install.
- `adgroup`         the ad group grouping level of the current install.
- `creative`        the creative grouping level of the current install.
- `clickLabel`      the click label of the current install.

### 12. Disable tracking

You can disable the adjust SDK from tracking any activities of the current
device by calling `setEnabled` with parameter `false`. This setting is
remembered between sessions.

```javascript
Adjust.setEnabled(false);
```

You can check if the adjust SDK is currently enabled by calling the function
`isEnabled`. It is always possible to activate the adjust SDK by invoking
`setEnabled` with the enabled parameter as `true`.

You must invoke `isEnabled` by passing a function to it which will receive
boolean which indicates is SDK enabled or disabled.

```javascript
Adjust.isEnabled(function(isEnabled) {
    if (isEnabled) {
        // SDK is enabled.
    } else {
        // SDK is disabled.
    }
});
```

### 13. Offline mode

You can put the adjust SDK in offline mode to suspend transmission to our servers, 
while retaining tracked data to be sent later. While in offline mode, all information is saved
in a file, so be careful not to trigger too many events while in offline mode.

You can activate offline mode by calling `setOfflineMode` with the parameter `true`.

```javascript
Adjust.setOfflineMode(true);
```

Conversely, you can deactivate offline mode by calling `setOfflineMode` with `false`.
When the adjust SDK is put back in online mode, all saved information is send to our servers 
with the correct time information.

Unlike disabling tracking, this setting is *not remembered* bettween sessions. 
This means that the SDK is in online mode whenever it is started,
even if the app was terminated in offline mode.

[adjust.com]:           http://adjust.com
[dashboard]:            http://adjust.com
[releases]:             https://github.com/adjust/cordova_sdk/releases
[attribution-data]:     https://github.com/adjust/sdks/blob/master/doc/attribution-data.md
[callbacks-guide]:      https://docs.adjust.com/en/callbacks
[event-tracking]:       https://docs.adjust.com/en/event-tracking
[special-partners]:     https://docs.adjust.com/en/special-partners
[currency-conversion]:  https://docs.adjust.com/en/event-tracking/#tracking-purchases-in-different-currencies
[google-launch-modes]:   http://developer.android.com/guide/topics/manifest/activity-element.html#lmode
[google_play_services]: http://developer.android.com/google/play-services/index.html
[google_ad_id]: https://developer.android.com/google/play-services/id.html


## License

The adjust SDK is licensed under the MIT License.

Copyright (c) 2012-2015 adjust GmbH, 
http://www.adjust.com

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and to permit persons to whom the Software is furnished to do
so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
