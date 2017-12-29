## Summary

This is the Android purchase SDK of adjust™. You can read more about adjust™ at [adjust.com].

## Table of contents

* [Basic integration](#basic-integration)
   * [Get the SDK](#sdk-get)
   * [Import the SDK module](#sdk-import)
   * [Add the SDK library to your project](#sdk-add)
   * [Integrate the SDK into your app](#sdk-integrate)
   * [Adjust Purchase logging](#sdk-logging)
* [Verify your purchases](#verify-purchases)
   * [Make the verification request](#verification-request)
   * [Process verification response](#verification-response)
   * [Track your verified purchases](#track-purchases)
* [Best practices](#best-practices)
* [License](#license)

## Basic integration

In order to use the adjust purchase SDK, you must **first enable fraud prevention** for your app. You can find the 
instructions in our official [fraud prevention guide][fraud-prevention] documentation.

These are the basic steps required to integrate the adjust purchase SDK into your Android project. We are going to assume 
that you use Android Studio for your Android development and target an Android API level 9 (Gingerbread) or later.

### <a id="sdk-get"></a>Get the SDK

Download the latest version from our [releases page][releases]. Extract the archive in a folder of your choice.

### <a id="sdk-import"></a>Import the SDK module

In the Android Studio menu select `File → New → Import Module...`.

![][import_module]

In the `Source directory` field, locate the folder you extracted in [previous step](#sdk-get). Select and choose the folder 
`./android_purchase_sdk/AdjustPurchase/adjust_purchase`. Make sure the module name `:adjust_purchase` appears before 
finishing.

![][select_module]

The `adjust_purchase` module should be imported into your Android Studio project afterwards.

![][imported_module]

### <a id="sdk-add"></a>Add the SDK library to your project

Open the `build.gradle` file of your app and find the `dependencies` block. Add
the following line:

```
compile project(":adjust_purchase")
```

![][gradle_adjust_purchase]

### <a id="sdk-integrate"></a>Integrate the SDK into your app

In your `Application` class find or create the `onCreate` method and add the following code to initialize the adjust  
purchase SDK:

```java
import com.adjust.sdk.purchase.ADJPConfig;
import com.adjust.sdk.purchase.ADJPConstants;
import com.adjust.sdk.purchase.AdjustPurchase;

public class GlobalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
    
        String yourAppToken = "{YourAppToken}";
        String environment = ADJPConstants.ENVIRONMENT_SANDBOX;

        ADJPConfig config = new ADJPConfig(yourAppToken, environment);

        AdjustPurchase.init(config);
    }
}
```

![][application_config]

Replace `{YourAppToken}` with your app token. You can find this in your [dashboard].

Depending on whether you build your app for testing or for production, you must set `environment` with one of these values:
    
```java
String environment = ADJPConstants.ENVIRONMENT_SANDBOX;
String environment = ADJPConstants.ENVIRONMENT_PRODUCTION;
```
    
**Important:** This value should be set to `ADJPConstants.ENVIRONMENT_SANDBOX` if and only if you or someone else is testing
your app. Make sure to set the environment to `ADJPConstants.ENVIRONMENT_PRODUCTION` just before you publish the app. Set it
back to `ADJPConstants.ENVIRONMENT_SANDBOX` when you start developing and testing it again.

We use this environment to distinguish between real traffic and test traffic from test devices. It is very important that 
you keep this value meaningful at all times!

### <a id="sdk-logging"></a>Adjust Purchase logging

You can increase or decrease the amount of logs you see in tests by calling `setLogLevel` on your `ADJPConfig` instance with
one of the following parameters:

```java
config.setLogLevel(ADJPLogLevel.VERBOSE);   // Enable all logging.
config.setLogLevel(ADJPLogLevel.DEBUG);     // Enable more logging.
config.setLogLevel(ADJPLogLevel.INFO);      // The default.
config.setLogLevel(ADJPLogLevel.WARN);      // Disable info logging.
config.setLogLevel(ADJPLogLevel.ERROR);     // Disable warnings as well.
config.setLogLevel(ADJPLogLevel.ASSERT);    // Disable errors as well.
```

## <a id="verify-purchases"></a>Verify your purchases

### <a id="verification-request"></a>Make the verification request

In order to verify purchase in your app, you need to call the `verifyPurchase` method on the `AdjustPurchase` instance. 
Please make sure to call this method once your purchase has been successfully performed.

Here is one example (depending on which IAP API you are using) for how you can do this:

```java
public class MainActivity extends Activity implements OnADJPVerificationFinished {
    static IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            if (result.isSuccess()) {
                // Do your stuff.

                AdjustPurchase.verifyPurchase(purchase.getSku(), purchase.getToken(), 
                    purchase.getDeveloperPayload(), mCurrentActivity);
            } else  {
                // Do your else stuff.
            }
        }
    };
    
    // ...
    
    public void onVerificationFinished(ADJPVerificationInfo info) {
        // ...
    }
}
```

Method of the adjust purchase SDK used to make verification request exects you to pass following parameters:

```
itemSku           // Unique order ID (SKU)
itemToken         // A token that uniquely identifies a purchase
developerPayload  // A developer-specified string that contains supplemental information about an order
callback          // Callback method which will process the verification response
```

### <a id="verification-response"></a>Process verification response

As described in code above, in the last parameter of this method you should pass an object that implements the 
`OnADJPVerificationFinished` protocol. To do this, you need to override the method called `onVerificationFinished`. This 
method will called by our purchase SDK once the response arrives. The response to purchase verification is represented with 
an `ADJPVerificationInfo` object and it contains following information:

```java
info.getVerificationState()     // State of purchase verification.
info.getStatusCode()            // Integer which displays backend response status code.
info.getMessage()               // Message describing purchase verification state.
```

The verification state can have one of the following values:

```
ADJPVerificationState.ADJPVerificationStatePassed       - Purchase verification successful.
ADJPVerificationState.ADJPVerificationStateFailed       - Purchase verification failed.
ADJPVerificationState.ADJPVerificationStateUnknown      - Purchase verification state unknown.
ADJPVerificationState.ADJPVerificationStateNotVerified  - Purchase was not verified.
```

* If the purchase was successfully verified by Google servers, `ADJPVerificationStatePassed` will be reported together with 
the status code `200`.
* If the Google servers recognized the purchase as invalid, `ADJPVerificationStateFailed` will be reported together with the
status code `406`.
* If the Google servers didn't provide us with any kind of answer to our request to verify your purchase, 
`ADJPVerificationStateUnknown` will be reported together with status code `204`. This situation means that we didn't manage 
to get any information from the Google servers regarding validity of your purchase. This does not say anything about the 
purchase itself. It might be both - valid or invalid. This state will also be reported if any other situation occurs that 
prevents us from reporting the correct state of your purchase verification. More details about these errors can be found by 
calling the `getMessage()` method on the `ADJPVerificationInfo` object.
* If `ADJPVerificationStateNotVerified` is reported, that means that the call to the `verifyPurchase` method was done with 
invalid parameters.

### <a id="track-purchases"></a>Track your verified purchases

After a purchase is successfully verified, you can track it with our official adjust SDK and keep track of revenue in your 
dashboard.

Using the examples from above, you can do this as follows:

```java
@Override
public void onVerificationFinished(ADJPVerificationInfo info) {
    if (info.getVerificationState() == ADJPVerificationState.ADJPVerificationStatePassed) {
        AdjustEvent event = new AdjustEvent("{YourEventToken}");
        event.setRevenue(0.01, "EUR");
        
        Adjust.trackEvent(event);
    }
}
```

## <a id="best-practices"></a>Best practices

Once `ADJPVerificationStatePassed` or `ADJPVerificationStateFailed` are reported, you can be sure that this decision was 
made by Google servers and you can rely on them to track or not to track your purchase revenue. Once 
`ADJPVerificationStateUnknown` is reported, you can decide what you want to do with this purchase.

For statistical purposes, it may be wise to have a single defined event for each of these scenarios in the adjust dashboard.
This way, you can have better overview of how many of your purchases was marked as passed, how many of them failed and how 
many of them were not able to be verified and returned an unknown status. You can also keep track of unverified purchases if
you would like to.

If you decide to do so, your method for handling the response can look like this:

```java
@Override
public void onVerificationFinished(ADJPVerificationInfo info) {
    if (info.getVerificationState() == ADJPVerificationState.ADJPVerificationStatePassed) {
        AdjustEvent event = new AdjustEvent("{RevenueEventPassedToken}");
        event.setRevenue(0.01, "EUR");
        
        Adjust.trackEvent(event);
    } else if (info.getVerificationState() == ADJPVerificationState.ADJPVerificationStateFailed) {
        AdjustEvent event = new AdjustEvent("{RevenueEventFailedToken}");
        Adjust.trackEvent(event);
    } else if (info.getVerificationState() == ADJPVerificationState.ADJPVerificationStateUnknown) {
        AdjustEvent event = new AdjustEvent("{RevenueEventUnknownToken}");
        Adjust.trackEvent(event);
    } else {
        AdjustEvent event = new AdjustEvent("{RevenueEventNotVerifiedToken}");
        Adjust.trackEvent(event);
    }
}
```

Purchase Verification is not intended to be used to approve/reject delivery of goods sold. Purchase Verification is 
intended to align reported transaction data with actual transaction data.

[dashboard]:                http://adjust.com
[adjust.com]:               http://adjust.com

[maven]:                    http://maven.org
[releases]:                 https://github.com/adjust/android_purchase_sdk/releases
[fraud-prevention]:         https://docs.adjust.com/en/fraud-prevention/

[import_module]:            https://raw.github.com/adjust/sdks/master/Resources/android_purchase/import_module.png
[select_module]:            https://raw.github.com/adjust/sdks/master/Resources/android_purchase/select_module.png
[imported_module]:          https://raw.github.com/adjust/sdks/master/Resources/android_purchase/imported_module.png
[application_config]:       https://raw.github.com/adjust/sdks/master/Resources/android_purchase/application_config.png
[gradle_adjust_purchase]:   https://raw.github.com/adjust/sdks/master/Resources/android_purchase/gradle_adjust_purchase.png

## <a id="license"></a>License

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
