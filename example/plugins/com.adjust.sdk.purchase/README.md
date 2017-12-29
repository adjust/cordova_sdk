## Summary

This is the Cordova purchase SDK of adjust™. You can read more about adjust™ at [adjust.com].

N.B. At the moment, the adjust purchase SDK for Cordova supports Android platform version  `4.0.0 and higher` and iOS 
platform version `3.0.0 and higher`.

## Table of contents

* [Basic integration](#basic-integration)
    * [Get the SDK](#sdk-get)
    * [Add the SDK to your project](#sdk-add)
    * [Integrate the SDK into your app](#sdk-integrate)
    * [Adjust Purchase logging](#sdk-logging)
* [Verify your purchases](#verify-purchases)
    * [Make the verification request](#verification-request)
    * [Process verification response](#verification-response)
    * [Track your verified purchases](#track-purchases)
* [Best practices](#best-practices)
* [License](#license)

## <a id="basic-integration">Basic integration

These are the minimal steps required to integrate the adjust purchase SDK into your Cordova project. We are going to assume 
you are using the command line interface.

### <a id="sdk-get">Get the SDK

Download the latest version from our [releases page][releases]. Extract the zip file in a folder of your choice.

### <a id="sdk-add">Add the SDK to your project

After you have added iOS and/or Android as a platform for your project, enter the following command in your project folder:

```
> cordova plugin add path_to_folder/cordova_purchase_sdk
Installing "com.adjust.sdk.purchase" for android
Installing "com.adjust.sdk.purchase" for ios
```

You can alternatively download our Purchase SDK directly as a plugin from the `npm` [repository][npm-repo]. To do so, simply
execute the following command in your project folder:

```
> cordova plugin add com.adjust.sdk.purchase
Fetching plugin "com.adjust.sdk.purchase" via npm
Installing "com.adjust.sdk.purchase" for android
Installing "com.adjust.sdk.purchase" for ios
```

### <a id="sdk-integrate">Integrate the SDK into your app

Add the following code to initialize the adjust purchase SDK in your `index.js` file, after you have received `deviceready` 
event:

```javascript
var adjustPurchaseConfig = new ADJPConfig("{YourAppToken}", ADJPConfig.EnvironmentSandbox);

Adjust.init(adjustPurchaseConfig);
```

Replace `{YourAppToken}` with your app token. You can find this in your [dashboard].

Depending on whether you build your app for testing or for production, you must set `environment` with one of these values:

```javascript
ADJPConfig.EnvironmentSandbox
ADJPConfig.EnvironmentProduction
```

**Important:** This value should be set to `ADJPConfig.EnvironmentSandbox` if and only if you or someone else is testing 
your app. Make sure to set the environment to `ADJPConfig.EnvironmentProduction` just before you publish the app. Set it 
back to `ADJPConfig.EnvironmentSandbox` when you start developing and testing it again.

We use this environment to distinguish between real traffic and artificial traffic from test devices. It is very important 
that you keep this value meaningful at all times: we are using it to determine whether your purchases should be verified on 
Apple/Google sandbox or production servers!

### <a id="sdk-logging">Adjust Purchase logging

You can increase or decrease the amount of logs you see in tests by calling `setLogLevel` on your `ADJPConfig` instance with
one of the following parameters:

```javascript
adjustPurchaseConfig.setLogLevel(ADJPConfig.LogLevelVerbose);   // enable all logging
adjustPurchaseConfig.setLogLevel(ADJPConfig.LogLevelDebug);     // enable more logging
adjustPurchaseConfig.setLogLevel(ADJPConfig.LogLevelInfo);      // the default
adjustPurchaseConfig.setLogLevel(ADJPConfig.LogLevelWarn);      // disable info logging
adjustPurchaseConfig.setLogLevel(ADJPConfig.LogLevelError);     // disable warnings as well
adjustPurchaseConfig.setLogLevel(ADJPConfig.LogLevelAssert);    // disable errors as well
```

## <a id="verify-purchases">Verify your purchases

### <a id="verification-request">Make the verification request

In order to verify in-app purchases, you need to call the `verifyPurchaseiOS` method on the `AdjustPurchase` instance for 
purchase verification on iOS, or the `verifyPurchaseAndroid` method for purchase verification on Android. Please make sure 
to call this method after the transaction has been finished and your item purchased.

```javascript
// ...

// Purchase verification request on iOS.
AdjustPurchase.verifyPurchaseiOS("{Receipt}", "{TransactionId}", "{ProductId}", function(verificationInfo) {
    console.log("Verification State = " + verificationInfo.verificationState);
    console.log("Status code = " + verificationInfo.statusCode);
    console.log("Message = " + verificationInfo.message);
});

// Purchase verification request on Android.
AdjustPurchase.verifyPurchaseAndroid("{ItemSKU}", "{PurchaseToken}", "{DeveloperPayload}", function(verificationInfo) {
    console.log("Verification State = " + verificationInfo.verificationState);
    console.log("Status code = " + verificationInfo.statusCode);
    console.log("Message = " + verificationInfo.message);
});

// ...
```

Method of the adjust purchase SDK used to make iOS verification request exects you to pass following parameters:

```
Receipt         // App receipt
TransactionId   // Finished transaction identifier
ProductId       // Your purchased product identifier
Callback        // Callback method which will process the verification response
```

Method of the adjust purchase SDK used to make Android verification request exects you to pass following parameters:

```
ItemSKU           // Unique order ID (SKU)
ItemToken         // A token that uniquely identifies a purchase
DeveloperPayload  // A developer-specified string that contains supplemental information about an order
Callback          // Callback method which will process the verification response
```

### <a id="verification-response">Process verification response

As described in the code above, you need to pass a method which is going to process the verification response to the 
`verifyPurchaseiOS` or `verifyPurchaseAndroid` methods.

The response to purchase verification is represented with an `ADJPVerificationInfo` object and it contains following 
information:

```javascript
verificationInfo.verificationState   // State of purchase verification.
verificationInfo.statusCode          // Integer which displays backend response status code.
verificationInfo.message             // Message describing purchase verification state.
```

The verification state can have one of the following values:

```
ADJPConfig.ADJPVerificationStatePassed         - Purchase verification successful.
ADJPConfig.ADJPVerificationStateFailed         - Purchase verification failed.
ADJPConfig.ADJPVerificationStateUnknown        - Purchase verification state unknown.
ADJPConfig.ADJPVerificationStateNotVerified    - Purchase was not verified.
```

* If the purchase was successfully verified by Apple/Google servers, `ADJPVerificationStatePassed` will be reported together
with the status code `200`.
* If the Apple/Google servers recognized the purchase as invalid, `ADJPVerificationStateFailed` will be reported together 
with the status code `406`.
* If the Apple/Google servers did not provide us with an answer for our request to verify your purchase, 
`ADJPVerificationStateUnknown` will be reported together with the status code `204`. This means that we did not recieve any 
information from Apple/Google servers regarding validity of your purchase. This does not say anything about the purchase 
itself. It might be both - valid or invalid. This state will also be reported if any other situation prevents us from 
reporting the correct state of your purchase verification. More details about these errors can be found in the `message` 
property.
* If `ADJPVerificationStateNotVerified` is reported, that means that the call to `verifyPurchaseiOS` or 
`verifyPurchaseAndroid` method was done with invalid parameters or that in general for some reason verification request was 
not even sent from the purchase SDK. Again, more information about error which caused this may be found in `message` 
property.

### <a id="track-purchases">Track your verified purchases

After a purchase is successfully verified, you can track it with our official adjust SDK and keep track of revenue in your 
dashboard. You can also pass in an optional transaction ID created in an event in order to avoid tracking duplicate 
revenues. The last ten transaction IDs are remembered and revenue events with duplicate transaction IDs are skipped.

**At the moment, the transaction duplication protection mechanism is working only for the iOS platform.**

Using the examples from above, you can do this as follows:

```javascript
// ...

AdjustPurchase.verifyPurchaseiOS("{Receipt}", "{TransactionId}", function(verificationInfo) {
    if (verificationInfo.verificationState == ADJPConfig.ADJPVerificationStatePassed) {
        var adjustEvent = new AdjustEvent("{YourEventToken}");
        adjustEvent.setRevenue(0.01, "EUR");
        
        // iOS feature only!
        adjustEvent.setTransactionId("{TransactionId}");
        
        Adjust.trackEvent(adjustEvent);
    }
});
```

## <a id="best-practices">Best practices

Once `ADJPVerificationStatePassed` or `ADJPVerificationStateFailed` are reported, you can be secure that this decision was 
made by Apple/Google servers and can rely on them to track or not to track your purchase revenue. Once 
`ADJPVerificationStateUnknown` is reported, you can decide what do you want to do with this purchase.

For statistical purposes, it may be wise to have a single defined event for each of these scenarios in the adjust dashboard.
This way, you can have better overview of how many of your purchases was marked as passed, how many of them failed and how 
many of them were not able to be verified and returned an unknown status. You can also keep track of unverified purchases if
you would like to.

If you decide to do so, your method for handling the response can look like this:

```javascript
// ...

// Example shows verification request for iOS.
// Same logic applies for Android verification request as well.
AdjustPurchase.verifyPurchaseiOS("{Receipt}", "{TransactionId}", function(verificationInfo) {
    if (verificationInfo.verificationState == ADJPConfig.ADJPVerificationStatePassed) {
        var adjustEvent = new AdjustEvent("{YourEventPassedToken}");
        adjustEvent.setRevenue(0.01, "EUR");
        
        // iOS feature only!
        adjustEvent.setTransactionId("{TransactionId}");
        
        Adjust.trackEvent(adjustEvent);
    } else if (verificationInfo.verificationState == ADJPConfig.ADJPVerificationStateFailed) {
        var adjustEvent = new AdjustEvent("{YourEventFailedToken}");
        Adjust.trackEvent(adjustEvent);
    } else if (verificationInfo.verificationState == ADJPConfig.ADJPVerificationStateUnknown) {
        var adjustEvent = new AdjustEvent("{YourEventUnknownToken}");
        Adjust.trackEvent(adjustEvent);
    } else {
        var adjustEvent = new AdjustEvent("{YourEventNotVerifiedToken}");
        Adjust.trackEvent(adjustEvent);
    }
});
```

Purchase Verification is not intended to be used to approve/reject delivery of goods sold. Purchase Verification is intended
to align reported transaction data with actual transaction data.

[dashboard]:        http://adjust.com
[adjust.com]:       http://adjust.com

[npm-repo]:         https://www.npmjs.com/package/com.adjust.sdk
[releases]:         https://github.com/adjust/cordova_purchase_sdk/releases
[fraud-prevention]: https://docs.adjust.com/en/fraud-prevention/

## <a id="livense">License

The adjust purchase SDK is licensed under the MIT License.

Copyright (c) 2016 adjust GmbH,
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
