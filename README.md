## Summary

This is the Cordova SDK of adjust.io™. You can read more about adjust.io™ at [adjust.io].

## Basic Installation

These are the minimal steps required to integrate the adjust SDK into your Cordova/PhoneGap project. The distinction between Cordova and PhoneGap is in the name of the commands. The differences will get pointed out. We are going to assume the command line interface is used.

### 1. Add the SDK to your project

After you added iOS and/or android as a platform for your project, enter the following command in your project folder:

#### With the cordova interface
```
> cordova plugin add https://github.com/adjust/cordova_sdk.git
Installing com.adjust.sdk (android)
Installing com.adjust.sdk (ios)
```

#### With the phonegap interface
```
> phonegap local plugin add https://github.com/adjust/cordova_sdk.git
[phonegap] adding the plugin: ...
[phonegap] successfully added the plugin
```

### 2. Integrate with your app

You can call the adjust plugin when the `'deviceready'` javascript event from Cordova is called. You can use the binding for this event in the initial file `www\js\index.js` or create your own. Inside this fuction you can call the `appDidLaunch` function. This tells adjust about the launch of your Application:

```javascript
document.addEventListener('deviceready', function () {
    var appToken = '{YourAppToken}';
    var environment = 'sandbox';
    var logLevel = 'info';
    var enableEventBuffering = false;
    
    Adjust.appDidLaunch(appToken, environment, logLevel, enableEventBuffering);
});
```

Replace `{YourAppToken}` with your App Token. You can find in your [dashboard].

Depending on whether or not you build your app for testing or for production
you must set the var `var environment` with one of these values:

```javascript
    var environment = 'sandbox';
    var environment = 'production';
```

You can increase or decrease the amount of logs you see by setting the
`var logLevel` with one of the following values:

```javascript
    var logLevel = 'verbose'; // enable all logging
    var logLevel = 'debug';   // enable more logging
    var logLevel = 'info';    // the default
    var logLevel = 'warn';    // disable info logging
    var logLevel = 'error';   // disable warnings as well
    var logLevel = 'assert';  // disable errors as well
```

If your app makes heavy use of event tracking, you might want to delay some
HTTP requests in order to send them in one batch every minute. You can enable
event buffering by setting the `var enableEventBuffering` to `true`.

To provide proper session tracking it is required to call certain Adjust
function every time the app resumes or pauses. Otherwise the SDK might miss
a session start or session end. In order to do so you should add these binding
for the Cordova events:

```javascript
document.addEventListener('pause', function () { Adjust.onPause(); });
document.addEventListener('resume', function() { Adjust.onResume(); });
```

## Additional Features

Once you integrated the adjust SDK into your project, you can take advantage
of the following features.

### 3. Add tracking of custom events.

You can tell adjust about every event you want. Suppose you want to track
every tap on a button. You would have to create a new Event Token in your
[dashboard]. Let's say that Event Token is `abc123`. In your button's
`click` event function you could then add the following line to track the click:

```javascript
Adjust.trackEvent('abc123');
```

You can also register a callback URL for that event in your [dashboard] and we
will send a GET request to that URL whenever the event gets tracked. In that
case you can also put some key-value-pairs in a dictionary and pass it to the
`trackEvent` function. We will then append these named parameters to your
callback URL.


For example, suppose you have registered the URL
`http://www.adjust.com/callback` for your event with Event Token `abc123` and
execute the following lines:

```javascript
var parameters = { 'key' : 'value', 'foo' : 'bar' };
Adjust.trackEvent('abc1234', parameters);
```

In that case we would track the event and send a request to:
```
    http://www.adjust.com/callback?key=value&foo=bar
```
It should be mentioned that we support a variety of placeholders like `{idfa}` 
for iOS or `{android_id}` for android that can be used as parameter values. 
In the resulting callback the `{idfa}` placeholder would be replaced with the ID 
for Advertisers of the current device for iOS and the `{android_id}` would be 
replaced with the AndroidID of the current device for android. Also note that 
we don't store any of your custom parameters, but only append them to your callbacks.
If you haven't registered a callback for an event, these parameters won't even be read.

### 4. Add tracking of revenue

If your users can generate revenue by clicking on advertisements or making
in-app purchases you can track those revenues. If, for example, a click is
worth one cent, you could make the following call to track that revenue:

```javascript
Adjust.trackRevenue(1.0);
```

The parameter is supposed to be in cents and will get rounded to one decimal
point. If you want to differentiate between different kinds of revenue you can
get different Event Tokens for each kind. Again, you need to create those Event
Tokens in your [dashboard]. In that case you would make a call like this:

```javascript
Adjust.trackRevenue(1.0, 'abc123');
```

Again, you can register a callback and provide a dictionary of named
parameters, just like it worked with normal events.

```javascript
var parameters = { 'key' : 'value', 'foo' : 'bar' };
Adjust.trackRevenue(1.0, 'abc1234', parameters);
```

### 5. Receive delegate callbacks

Every time your app tries to track a session, an event or some revenue, you can
be notified about the success of that operation and receive additional
information about the current install. For that you can pass a javascript callback 
function that receives one argument to the `setFinishedTrackingCallback`, such as:
```javascript
Adjust.setFinishedTrackingCallback(function (responseData) { });
```

The callback function will get called every time any activity was tracked or
failed to track. Within the callback function you have access to the
`responseData` object parameter. Here is a quick summary of its attributes:

- `activityKind` indicates what kind of activity was tracked. 
  Returns one of these values:

    ```
    session
    event
    revenue
    ```
- `success` indicates whether or not the tracking attempt was
  successful. Possible values `'true'` or `'false'`.
- `willRetry` is true when the request failed, but will be
  retried. Possible values `'true'` or `'false'`.
- `error` an error message when the activity failed to track or
  the response could not be parsed. Is `undefined` otherwise.
- `trackerToken` the tracker token of the current install. Is `undefined` if
  request failed or response could not be parsed.
- `trackerName` the tracker name of the current install. Is `undefined` if
  request failed or response could not be parsed.

[adjust.io]: http://adjust.io
[dashboard]: http://adjust.io

## License

The adjust-sdk is licensed under the MIT License.

Copyright (c) 2012-2013 adeven GmbH,
http://www.adeven.com

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
