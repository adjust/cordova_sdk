## Summary

This is the iOS purchase SDK of adjust™. You can read more about adjust™ at [adjust.com].

## Table of contents

* [Basic integration](#basic-integration)
   * [Get the SDK](#sdk-get)
   * [Add the SDK to your project](#sdk-add)
   * [Integrate the SDK into your app](#sdk-integrate)
      * [Import statement](#sdk-import)
      * [Basic setup](#basic-setup)
      * [Adjust Purchase logging](#sdk-logging)
   * [Verify your purchases](#verify-purchases)
      * [Make the verification request](#verification-request)
      * [Process verification response](#verification-response)
   * [Track your verified purchases](#track-purchases)
* [Best practices](#best-practices)
* [License](#license)

## <a id="basic-integration">Basic integration

In order to use the adjust purchase SDK, you must **first enable fraud prevention** for your app. You can find the 
instructions in our official [fraud prevention guide][fraud-prevention] documentation.

We will describe the steps to integrate the adjust purchase SDK into your iOS project. We are going to assume that you use 
Xcode for your iOS development.

If you're using [Carthage][carthage], you can add following line to your `Cartfile` and continue with [step 3](#step3):

```ruby
github "adjust/ios_purchase_sdk"
```

You can also choose to integrate the adjust purchase SDK by adding it to your project as a framework. On the [releases 
page][releases] you can find four archives:

* `AdjustPurchaseSdkStatic.framework.zip`
* `AdjustPurchaseSdkDynamic.framework.zip`

Since the release of iOS 8, Apple has introduced dynamic frameworks (also known as embedded frameworks). If your app is 
targeting iOS 8 or higher, you can use the adjust purchase SDK dynamic framework. Choose which framework you want to use – 
static or dynamic – and add it to your project before continuing with [step 3](#step3).

### <a id="sdk-get"></a>Get the SDK

Download the latest version from our [releases page][releases]. Extract the archive into a directory of your choice.

### <a id="sdk-add"></a>Add the SDK to your project

In Xcode's Project Navigator locate the `Supporting Files` group (or any other group of your choice). From Finder, drag the 
`AdjustPurchase` subdirectory into Xcode's `Supporting Files` group.

![][drag]

In the dialog `Choose options for adding these files` make sure to check the checkbox to `Copy items if needed` and select 
the radio button to `Create groups`.

![][add]

### <a id="sdk-integrate"></a>Integrate the SDK into your app

#### <a id="sdk-import"></a>Import statement

If you added the adjust SDK from the source, you should use following import statement:

```objc
#import "AdjustPurchase.h"
```

If you added the adjust SDK as a framework or via Carthage, you should use following import statement:

```objc
#import <AdjustPurchaseSdk/AdjustPurchase.h>
```

To begin, we'll set up the iOS purchase SDK.

#### <a id="basic-setup"></a>Basic setup

In the Project Navigator, open the source file of your application delegate. Add the `import` statement at the top of the 
file, then add the following call to `AdjustPurchase` in the `didFinishLaunching` or `didFinishLaunchingWithOptions` method 
of your app delegate:

```objc
#import "AdjustPurchase.h"
// or #import <AdjustPurchaseSdk/AdjustPurchase.h>

// ...

NSString *yourAppToken = @"{YourAppToken}";
NSString *environment = ADJPEnvironmentSandbox;

ADJPConfig *config = [[ADJPConfig alloc] initWithAppToken:yourAppToken andEnvironment:environment];
[AdjustPurchase init:config];
```
![][integration]

Replace `{YourAppToken}` with your app token. You can find this in your [dashboard].

Depending on whether you build your app for testing or for production, you must set `environment` with one of these values:

```objc
NSString *environment = ADJPEnvironmentSandbox;
NSString *environment = ADJPEnvironmentProduction;
```

**Important:** This value should be set to `ADJPEnvironmentSandbox` if and only if you or someone else is testing your app. 
Make sure to set the environment to `ADJPEnvironmentProduction` just before you publish the app. Set it back to 
`ADJPEnvironmentSandbox` when you start developing and testing it again.

We use this environment to distinguish between real traffic and test traffic from test devices. It is very important that 
you keep this value meaningful at all times!

#### <a id="sdk-logging"></a>Adjust Purchase logging

You can increase or decrease the amount of logs you see in tests by calling `setLogLevel:` on your `ADJPConfig` instance 
with one of the following parameters:

```objc
[config setLogLevel:ADJPLogLevelVerbose]; // Enable all logging.
[config setLogLevel:ADJPLogLevelDebug];   // Enable more logging.
[config setLogLevel:ADJPLogLevelInfo];    // The default.
[config setLogLevel:ADJPLogLevelWarn];    // Disable info logging.
[config setLogLevel:ADJPLogLevelError];   // Disable warnings as well.
[config setLogLevel:ADJPLogLevelAssert];  // Disable errors as well.
```

### <a id="verify-purchases"></a>Verify your purchases

#### <a id="verification-request"></a>Make the verification request

In order to verify in-app purchases, you need to call the `verifyPurchase:forTransaction:productId:withResponseBlock` 
method on the `AdjustPurchase` instance. Please make sure to call this method after `finishTransaction` in 
`paymentQueue:updatedTransaction` only if the state changed to `SKPaymentTransactionStatePurchased`.

```objc
- (void)paymentQueue:(SKPaymentQueue *)queue updatedTransactions:(NSArray *)transactions {
    for (SKPaymentTransaction *transaction in transactions) {
        switch (transaction.transactionState) {
            case SKPaymentTransactionStatePurchasing: {
                // Your stuff if any.
                break;
            }
            case SKPaymentTransactionStatePurchased: {
                // Your stuff if any.
                NSURL *receiptURL = [[NSBundle mainBundle] appStoreReceiptURL];
                NSData *receipt = [NSData dataWithContentsOfURL:receiptURL];

                [[SKPaymentQueue defaultQueue] finishTransaction:transaction];

                [AdjustPurchase verifyPurchase:receipt
                                forTransaction:transaction
                                productId:@"your_product_id"
                             withResponseBlock:^(ADJPVerificationInfo *info) {
                                 // Process ADJPVerificationInfo object... 
                                 [self adjustVerificationUpdate:info];
                             }];

                break;
            }
            case SKPaymentTransactionStateFailed: {
                // Your stuff if any.
                break;
            }
            default:
                break;
        }
    }
}

// ...

- (void)adjustVerificationUpdate:(ADJPVerificationInfo *)info {
    // ...
}
```

Method of the adjust purchase SDK used to make verification request exects you to pass following parameters:

```objc
receipt         // App receipt of NSData type
transaction     // Finished transaction object of SKPaymentTransaction type
productId       // Your purchased product identifier of NSString type
responseBlock   // Callback method which will process the verification response
```

#### <a id="verification-response"></a>Process verification response

As described in the code above, you need to pass a block which is going to process verification response to 
`verifyPurchase:forTransaction:productId:withResponseBlock` method.

In the example above, we designed the `adjustVerificationUpdate` method to be called once the response arrives. The 
response to purchase verification is represented with an `ADJPVerificationInfo` object and it contains following 
information:

```objc
[info verificationState];   // State of purchase verification.
[info statusCode];          // Integer which displays backend response status code.
[info message];             // Message describing purchase verification state.
```

The verification state can have one of the following values:

```
ADJPVerificationStatePassed         - Purchase verification successful.
ADJPVerificationStateFailed         - Purchase verification failed.
ADJPVerificationStateUnknown        - Purchase verification state unknown.
ADJPVerificationStateNotVerified    - Purchase was not verified.
```

* If the purchase was successfully verified by Apple servers, `ADJPVerificationStatePassed` will be reported together with 
the status code `200`.
* If the Apple servers recognized the purchase as invalid, `ADJPVerificationStateFailed` will be reported together with the 
status code `406`.
* If the Apple server did not provide us with an answer for our request to verify your purchase, 
`ADJPVerificationStateUnknown` will be reported together with the status code `204`. This means that we did not recieve any 
information from Apple servers regarding validity of your purchase. This does not say anything about the purchase itself. 
It might be both - valid or invalid. This state will also be reported if any other situation prevents us from reporting the 
correct state of your purchase verification. More details about these errors can be found in the `message` field of 
`ADJPVerificationInfo` object.
* If `ADJPVerificationStateNotVerified` is reported, that means that the call to  
`verifyPurchase:forTransaction:productId:withResponseBlock` method was done with invalid parameters.

### <a id="track-purchases"></a>Track your verified purchases

After a purchase is successfully verified, you can track it with our official adjust SDK and keep track of revenue in your 
dashboard. You can also pass in an optional transaction ID created in an event in order to avoid tracking duplicate 
revenues. The last ten transaction IDs are remembered and revenue events with duplicate transaction IDs are skipped.

Using the examples from above, you can do this as follows:

```objc
#import "Adjust.h"
// or #import <AdjustSdk/Adjust.h>
#import "AdjustPurchase.h"
// or #import <AdjustPurchaseSdk/AdjustPurchase.h>

// ...

- (void)adjustVerificationUpdate:(ADJPVerificationInfo *)info {
    if ([info verificationState] == ADJPVerificationStatePassed) {
        ADJEvent *event = [[ADJEvent alloc] initWithEventToken:@"{YourEventToken}"];

        [event setRevenue:0.01 currency:@"EUR"];
        [event setTransactionId:@"{YourTransactionId}"];

        [Adjust trackEvent:event];
    }
}
```

## <a id="best-practices"></a>Best practices

Once `ADJPVerificationStatePassed` or `ADJPVerificationStateFailed` are reported, you can be secure that this decision was 
made by Apple servers and can rely on them to track or not to track your purchase revenue. Once 
`ADJPVerificationStateUnknown` is reported, you can decide what do you want to do with this purchase.

For statistical purposes, it may be wise to have a single defined event for each of these scenarios in the adjust 
dashboard. This way, you can have better overview of how many of your purchases was marked as passed, how many of them 
failed and how  many of them were not able to be verified and returned an unknown status. You can also keep track of 
unverified purchases if you would like to.

If you decide to do so, your method for handling the response can look like this:

```objc
- (void)adjustVerificationUpdate:(ADJPVerificationInfo *)info {
    if ([info verificationState] == ADJPVerificationStatePassed) {
        ADJEvent *event = [[ADJEvent alloc] initWithEventToken:@"{RevenueEventPassedToken}"];

        [event setRevenue:0.01 currency:@"EUR"];
        [event setTransactionId:@"{YourTransactionId}"];

        [Adjust trackEvent:event];
    } else if ([info verificationState] == ADJVerificationStateFailed) {
        ADJEvent *event = [[ADJEvent alloc] initWithEventToken:@"{RevenueEventFailedToken}"];
        [Adjust trackEvent:event];
    } else if ([info verificationState] == ADJPVerificationStateUnknown) {
        ADJEvent *event = [[ADJEvent alloc] initWithEventToken:@"{RevenueEventUnknownToken}"];
        [Adjust trackEvent:event];
    } else {
        ADJEvent *event = [[ADJEvent alloc] initWithEventToken:@"{RevenueEventNotVerifiedToken}"];
        [Adjust trackEvent:event];
    }
}
```

Purchase Verification is not intended to be used to approve/reject delivery of goods sold. Purchase Verification is 
intended to align reported transaction data with actual transaction data.

[dashboard]:        http://adjust.com
[adjust.com]:       http://adjust.com

[carthage]:         https://github.com/Carthage/Carthage
[releases]:         https://github.com/adjust/ios_purchase_sdk/releases
[cocoapods]:        http://cocoapods.org
[fraud-prevention]: https://docs.adjust.com/en/fraud-prevention/

[add]:              https://raw.github.com/adjust/sdks/master/Resources/ios_purchase/add.png
[drag]:             https://raw.github.com/adjust/sdks/master/Resources/ios_purchase/drag.png
[integration]:      https://raw.github.com/adjust/sdks/master/Resources/ios_purchase/integration.png

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
