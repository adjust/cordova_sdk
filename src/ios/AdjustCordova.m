//
//  AdjustCordova.m
//  Adjust SDK
//
//  Created by Pedro Filipe (@nonelse) on 3rd April 2014.
//  Copyright (c) 2012-2018 Adjust GmbH. All rights reserved.
//

#import <Cordova/CDVPluginResult.h>

#import "AdjustCordova.h"
#import "AdjustCordovaDelegate.h"

#define KEY_APP_TOKEN @"appToken"
#define KEY_ENVIRONMENT @"environment"
#define KEY_LOG_LEVEL @"logLevel"
#define KEY_SDK_PREFIX @"sdkPrefix"
#define KEY_EVENT_BUFFERING_ENABLED @"eventBufferingEnabled"
#define KEY_EVENT_TOKEN @"eventToken"
#define KEY_REVENUE @"revenue"
#define KEY_CURRENCY @"currency"
#define KEY_RECEIPT @"receipt"
#define KEY_DEFAULT_TRACKER @"defaultTracker"
#define KEY_EXTERNAL_DEVICE_ID @"externalDeviceId"
#define KEY_TRANSACTION_ID @"transactionId"
#define KEY_CALLBACK_ID @"callbackId"
#define KEY_CALLBACK_PARAMETERS @"callbackParameters"
#define KEY_PARTNER_PARAMETERS @"partnerParameters"
#define KEY_IS_RECEIPT_SET @"isReceiptSet"
#define KEY_USER_AGENT @"userAgent"
#define KEY_REFERRER @"referrer"
#define KEY_SHOULD_LAUNCH_DEEPLINK @"shouldLaunchDeeplink"
#define KEY_SEND_IN_BACKGROUND @"sendInBackground"
#define KEY_DELAY_START @"delayStart"
#define KEY_DEVICE_KNOWN @"isDeviceKnown"
#define KEY_ALLOW_IAD_INFO_READING @"allowiAdInfoReading"
#define KEY_ALLOW_IDFA_READING @"allowIdfaReading"
#define KEY_SECRET_ID @"secretId"
#define KEY_INFO_1 @"info1"
#define KEY_INFO_2 @"info2"
#define KEY_INFO_3 @"info3"
#define KEY_INFO_4 @"info4"
#define KEY_BASE_URL @"baseUrl"
#define KEY_GDPR_URL @"gdprUrl"
#define KEY_SUBSCRIPTION_URL @"gdprUrl"
#define KEY_EXTRA_PATH @"extraPath"
#define KEY_USE_TEST_CONNECTION_OPTIONS @"useTestConnectionOptions"
#define KEY_TIMER_INTERVAL @"timerIntervalInMilliseconds"
#define KEY_TIMER_START @"timerStartInMilliseconds"
#define KEY_SESSION_INTERVAL @"sessionIntervalInMilliseconds"
#define KEY_SUBSESSION_INTERVAL @"subsessionIntervalInMilliseconds"
#define KEY_TEARDOWN @"teardown"
#define KEY_NO_BACKOFF_WAIT @"noBackoffWait"
#define KEY_HAS_CONTEXT @"hasContext"
#define KEY_IAD_ENABLED @"iAdFrameworkEnabled"
#define KEY_PRICE @"price"
#define KEY_TRANSACTION_DATE @"transactionDate"
#define KEY_SALES_REGION @"salesRegion"

@implementation AdjustCordova {
    NSString *attributionCallbackId;
    NSString *eventFailedCallbackId;
    NSString *eventSucceededCallbackId;
    NSString *sessionFailedCallbackId;
    NSString *sessionSucceededCallbackId;
    NSString *deferredDeeplinkCallbackId;
}

#pragma mark - Object lifecycle methods

- (void)pluginInitialize {
    attributionCallbackId = nil;
    eventFailedCallbackId = nil;
    eventSucceededCallbackId = nil;
    sessionFailedCallbackId = nil;
    sessionSucceededCallbackId = nil;
    deferredDeeplinkCallbackId = nil;
}

#pragma mark - Public methods

- (void)create:(CDVInvokedUrlCommand *)command {
    NSString *arguments = [command.arguments objectAtIndex:0];
    NSArray *jsonObject = [NSJSONSerialization JSONObjectWithData:[arguments dataUsingEncoding:NSUTF8StringEncoding]
                                                          options:0
                                                            error:NULL];

    NSString *appToken = [[jsonObject valueForKey:KEY_APP_TOKEN] objectAtIndex:0];
    NSString *environment = [[jsonObject valueForKey:KEY_ENVIRONMENT] objectAtIndex:0];
    NSString *logLevel = [[jsonObject valueForKey:KEY_LOG_LEVEL] objectAtIndex:0];
    NSString *defaultTracker = [[jsonObject valueForKey:KEY_DEFAULT_TRACKER] objectAtIndex:0];
    NSString *externalDeviceId = [[jsonObject valueForKey:KEY_EXTERNAL_DEVICE_ID] objectAtIndex:0];
    NSString *userAgent = [[jsonObject valueForKey:KEY_USER_AGENT] objectAtIndex:0];
    NSString *secretId = [[jsonObject valueForKey:KEY_SECRET_ID] objectAtIndex:0];
    NSString *info1 = [[jsonObject valueForKey:KEY_INFO_1] objectAtIndex:0];
    NSString *info2 = [[jsonObject valueForKey:KEY_INFO_2] objectAtIndex:0];
    NSString *info3 = [[jsonObject valueForKey:KEY_INFO_3] objectAtIndex:0];
    NSString *info4 = [[jsonObject valueForKey:KEY_INFO_4] objectAtIndex:0];
    NSNumber *delayStart = [[jsonObject valueForKey:KEY_DELAY_START] objectAtIndex:0];
    NSNumber *isDeviceKnown = [[jsonObject valueForKey:KEY_DEVICE_KNOWN] objectAtIndex:0];
    NSNumber *allowiAdInfoReading = [[jsonObject valueForKey:KEY_ALLOW_IAD_INFO_READING] objectAtIndex:0];
    NSNumber *allowIdfaReading = [[jsonObject valueForKey:KEY_ALLOW_IDFA_READING] objectAtIndex:0];
    NSNumber *eventBufferingEnabled = [[jsonObject valueForKey:KEY_EVENT_BUFFERING_ENABLED] objectAtIndex:0];
    NSNumber *sendInBackground = [[jsonObject valueForKey:KEY_SEND_IN_BACKGROUND] objectAtIndex:0];
    NSNumber *shouldLaunchDeeplink = [[jsonObject valueForKey:KEY_SHOULD_LAUNCH_DEEPLINK] objectAtIndex:0];
    NSString *sdkPrefix = [[jsonObject valueForKey:KEY_SDK_PREFIX] objectAtIndex:0];
    BOOL allowSuppressLogLevel = NO;

    // Check for SUPPRESS log level.
    if ([self isFieldValid:logLevel]) {
        if ([ADJLogger logLevelFromString:[logLevel lowercaseString]] == ADJLogLevelSuppress) {
            allowSuppressLogLevel = YES;
        }
    }

    ADJConfig *adjustConfig = [ADJConfig configWithAppToken:appToken
                                                environment:environment
                                      allowSuppressLogLevel:allowSuppressLogLevel];

    if (![adjustConfig isValid]) {
        return;
    }

    // Log level.
    if ([self isFieldValid:logLevel]) {
        [adjustConfig setLogLevel:[ADJLogger logLevelFromString:[logLevel lowercaseString]]];
    }

    // Event buffering.
    if ([self isFieldValid:eventBufferingEnabled]) {
        [adjustConfig setEventBufferingEnabled:[eventBufferingEnabled boolValue]];
    }

    // SDK prefix.
    if ([self isFieldValid:sdkPrefix]) {
        [adjustConfig setSdkPrefix:sdkPrefix];
    }

    // Default tracker.
    if ([self isFieldValid:defaultTracker]) {
        [adjustConfig setDefaultTracker:defaultTracker];
    }

    // External device ID.
    if ([self isFieldValid:externalDeviceId]) {
        [adjustConfig setExternalDeviceId:externalDeviceId];
    }

    // Send in background.
    if ([self isFieldValid:sendInBackground]) {
        [adjustConfig setSendInBackground:[sendInBackground boolValue]];
    }

    // User agent.
    if ([self isFieldValid:userAgent]) {
        [adjustConfig setUserAgent:userAgent];
    }

    // Delay start.
    if ([self isFieldValid:delayStart]) {
        [adjustConfig setDelayStart:[delayStart doubleValue]];
    }

    // Device known.
    if ([self isFieldValid:isDeviceKnown]) {
        [adjustConfig setIsDeviceKnown:[isDeviceKnown boolValue]];
    }

    // iAd info reading.
    if ([self isFieldValid:allowiAdInfoReading]) {
        [adjustConfig setAllowiAdInfoReading:[allowiAdInfoReading boolValue]];
    }

    // IDFA reading.
    if ([self isFieldValid:allowIdfaReading]) {
        [adjustConfig setAllowIdfaReading:[allowIdfaReading boolValue]];
    }

    // App Secret.
    if ([self isFieldValid:secretId]
        && [self isFieldValid:info1]
        && [self isFieldValid:info2]
        && [self isFieldValid:info3]
        && [self isFieldValid:info4]) {
        [adjustConfig setAppSecret:[[NSNumber numberWithLongLong:[secretId longLongValue]] unsignedIntegerValue]
                             info1:[[NSNumber numberWithLongLong:[info1 longLongValue]] unsignedIntegerValue]
                             info2:[[NSNumber numberWithLongLong:[info2 longLongValue]] unsignedIntegerValue]
                             info3:[[NSNumber numberWithLongLong:[info3 longLongValue]] unsignedIntegerValue]
                             info4:[[NSNumber numberWithLongLong:[info4 longLongValue]] unsignedIntegerValue]];
    }

    BOOL isAttributionCallbackImplemented = attributionCallbackId != nil ? YES : NO;
    BOOL isEventSucceededCallbackImplemented = eventSucceededCallbackId != nil ? YES : NO;
    BOOL isEventFailedCallbackImplemented = eventFailedCallbackId != nil ? YES : NO;
    BOOL isSessionSucceededCallbackImplemented = sessionSucceededCallbackId != nil ? YES : NO;
    BOOL isSessionFailedCallbackImplemented = sessionFailedCallbackId != nil ? YES : NO;
    BOOL isDeferredDeeplinkCallbackImplemented = deferredDeeplinkCallbackId != nil ? YES : NO;
    BOOL shouldLaunchDeferredDeeplink = [self isFieldValid:shouldLaunchDeeplink] ? [shouldLaunchDeeplink boolValue] : YES;

    // Attribution delegate & other delegates
    if (isAttributionCallbackImplemented
        || isEventSucceededCallbackImplemented
        || isEventFailedCallbackImplemented
        || isSessionSucceededCallbackImplemented
        || isSessionFailedCallbackImplemented
        || isDeferredDeeplinkCallbackImplemented) {
        [adjustConfig setDelegate:
            [AdjustCordovaDelegate getInstanceWithSwizzleOfAttributionCallback:isAttributionCallbackImplemented
                                                        eventSucceededCallback:isEventSucceededCallbackImplemented
                                                           eventFailedCallback:isEventFailedCallbackImplemented
                                                      sessionSucceededCallback:isSessionSucceededCallbackImplemented
                                                         sessionFailedCallback:isSessionFailedCallbackImplemented
                                                      deferredDeeplinkCallback:isDeferredDeeplinkCallbackImplemented
                                                      andAttributionCallbackId:attributionCallbackId
                                                      eventSucceededCallbackId:eventSucceededCallbackId
                                                         eventFailedCallbackId:eventFailedCallbackId
                                                    sessionSucceededCallbackId:sessionSucceededCallbackId
                                                       sessionFailedCallbackId:sessionFailedCallbackId
                                                    deferredDeeplinkCallbackId:deferredDeeplinkCallbackId
                                                  shouldLaunchDeferredDeeplink:shouldLaunchDeferredDeeplink
                                                           withCommandDelegate:self.commandDelegate]];
    }

    // Start SDK.
    [Adjust appDidLaunch:adjustConfig];
    [Adjust trackSubsessionStart];
}

- (void)trackEvent:(CDVInvokedUrlCommand *)command {
    NSString *arguments = [command.arguments objectAtIndex:0];
    NSArray *jsonObject = [NSJSONSerialization JSONObjectWithData:[arguments dataUsingEncoding:NSUTF8StringEncoding]
                                                          options:0
                                                            error:NULL];

    NSString *eventToken = [[jsonObject valueForKey:KEY_EVENT_TOKEN] objectAtIndex:0];
    NSString *revenue = [[jsonObject valueForKey:KEY_REVENUE] objectAtIndex:0];
    NSString *currency = [[jsonObject valueForKey:KEY_CURRENCY] objectAtIndex:0];
    NSString *receipt = [[jsonObject valueForKey:KEY_RECEIPT] objectAtIndex:0];
    NSString *transactionId = [[jsonObject valueForKey:KEY_TRANSACTION_ID] objectAtIndex:0];
    NSString *callbackId = [[jsonObject valueForKey:KEY_CALLBACK_ID] objectAtIndex:0];
    NSNumber *isReceiptSet = [[jsonObject valueForKey:KEY_IS_RECEIPT_SET] objectAtIndex:0];
    NSMutableArray *callbackParameters = [[NSMutableArray alloc] init];
    NSMutableArray *partnerParameters = [[NSMutableArray alloc] init];

    for (id item in [[jsonObject valueForKey:KEY_CALLBACK_PARAMETERS] objectAtIndex:0]) {
        [callbackParameters addObject:item];
    }
    for (id item in [[jsonObject valueForKey:KEY_PARTNER_PARAMETERS] objectAtIndex:0]) {
        [partnerParameters addObject:item];
    }

    ADJEvent *adjustEvent = [ADJEvent eventWithEventToken:eventToken];

    if (![adjustEvent isValid]) {
        return;
    }

    // Revenue and currency.
    if ([self isFieldValid:revenue]) {
        double revenueValue = [revenue doubleValue];
        [adjustEvent setRevenue:revenueValue currency:currency];
    }

    // Callback parameters.
    for (int i = 0; i < [callbackParameters count]; i += 2) {
        NSString *key = [callbackParameters objectAtIndex:i];
        NSObject *value = [callbackParameters objectAtIndex:(i+1)];
        [adjustEvent addCallbackParameter:key value:[NSString stringWithFormat:@"%@", value]];
    }

    // Partner parameters.
    for (int i = 0; i < [partnerParameters count]; i += 2) {
        NSString *key = [partnerParameters objectAtIndex:i];
        NSObject *value = [partnerParameters objectAtIndex:(i+1)];
        [adjustEvent addPartnerParameter:key value:[NSString stringWithFormat:@"%@", value]];
    }

    // Deprecated.
    // Transaction ID and receipt.
    BOOL isTransactionIdSet = NO;
    if ([self isFieldValid:isReceiptSet]) {
        if ([isReceiptSet boolValue]) {
            [adjustEvent setReceipt:[receipt dataUsingEncoding:NSUTF8StringEncoding] transactionId:transactionId];
        } else {
            if ([self isFieldValid:transactionId]) {
                [adjustEvent setTransactionId:transactionId];
                isTransactionIdSet = YES;
            }
        }
    }

    if (NO == isTransactionIdSet) {
        if ([self isFieldValid:transactionId]) {
            [adjustEvent setTransactionId:transactionId];
        }
    }

    // Callback ID.
    if ([self isFieldValid:callbackId]) {
        [adjustEvent setCallbackId:callbackId];
    }

    // Track event.
    [Adjust trackEvent:adjustEvent];
}

- (void)setOfflineMode:(CDVInvokedUrlCommand *)command {
    NSNumber *isEnabledNumber = [command argumentAtIndex:0 withDefault:nil];
    if (isEnabledNumber == nil) {
        return;
    }
    [Adjust setOfflineMode:[isEnabledNumber boolValue]];
}

- (void)setPushToken:(CDVInvokedUrlCommand *)command {
    NSString *token = [command argumentAtIndex:0 withDefault:nil];
    if (!([self isFieldValid:token])) {
        return;
    }
    [Adjust setPushToken:token];
}

- (void)appWillOpenUrl:(CDVInvokedUrlCommand *)command {
    NSString *urlString = [command argumentAtIndex:0 withDefault:nil];
    if (urlString == nil) {
        return;
    }

    NSURL *url;
    if ([NSString instancesRespondToSelector:@selector(stringByAddingPercentEncodingWithAllowedCharacters:)]) {
        url = [NSURL URLWithString:[urlString stringByAddingPercentEncodingWithAllowedCharacters:[NSCharacterSet URLFragmentAllowedCharacterSet]]];
    } else {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wdeprecated-declarations"
        url = [NSURL URLWithString:[urlString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    }
#pragma clang diagnostic pop
    [Adjust appWillOpenUrl:url];
}

- (void)gdprForgetMe:(CDVInvokedUrlCommand *)command {
    [Adjust gdprForgetMe];
}

- (void)disableThirdPartySharing:(CDVInvokedUrlCommand *)command {
    [Adjust disableThirdPartySharing];
}

- (void)getIdfa:(CDVInvokedUrlCommand *)command {
    NSString *idfa = [Adjust idfa];
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:idfa];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)getAdid:(CDVInvokedUrlCommand *)command {
    NSString *adid = [Adjust adid];
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:adid];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)getAttribution:(CDVInvokedUrlCommand *)command {
    ADJAttribution *attribution = [Adjust attribution];
    if (attribution == nil) {
        return;
    }
    
    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
    [self addValueOrEmpty:attribution.trackerToken withKey:@"trackerToken" toDictionary:dictionary];
    [self addValueOrEmpty:attribution.trackerName withKey:@"trackerName" toDictionary:dictionary];
    [self addValueOrEmpty:attribution.network withKey:@"network" toDictionary:dictionary];
    [self addValueOrEmpty:attribution.campaign withKey:@"campaign" toDictionary:dictionary];
    [self addValueOrEmpty:attribution.creative withKey:@"creative" toDictionary:dictionary];
    [self addValueOrEmpty:attribution.adgroup withKey:@"adgroup" toDictionary:dictionary];
    [self addValueOrEmpty:attribution.clickLabel withKey:@"clickLabel" toDictionary:dictionary];
    [self addValueOrEmpty:attribution.adid withKey:@"adid" toDictionary:dictionary];
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictionary];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)getSdkVersion:(CDVInvokedUrlCommand *)command {
    NSString *sdkVersion = [Adjust sdkVersion];
    if (sdkVersion == nil) {
        sdkVersion = @"";
    }
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:sdkVersion];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)setEnabled:(CDVInvokedUrlCommand *)command {
    NSNumber *isEnabledNumber = [command argumentAtIndex:0 withDefault:nil];
    if (isEnabledNumber == nil) {
        return;
    }

    [Adjust setEnabled:[isEnabledNumber boolValue]];
}

- (void)isEnabled:(CDVInvokedUrlCommand *)command {
    BOOL isEnabled = [Adjust isEnabled];
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:isEnabled];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)sendFirstPackages:(CDVInvokedUrlCommand *)command {
    [Adjust sendFirstPackages];
}

- (void)trackAdRevenue:(CDVInvokedUrlCommand *)command {
    NSString *source = [command argumentAtIndex:0 withDefault:nil];
    NSString *payload = [command argumentAtIndex:1 withDefault:nil];
    NSData *dataPayload = [payload dataUsingEncoding:NSUTF8StringEncoding];
    [Adjust trackAdRevenue:source payload:dataPayload];
}

- (void)trackAppStoreSubscription:(CDVInvokedUrlCommand *)command {
    NSString *arguments = [command.arguments objectAtIndex:0];
    NSArray *jsonObject = [NSJSONSerialization JSONObjectWithData:[arguments dataUsingEncoding:NSUTF8StringEncoding]
                                                          options:0
                                                            error:NULL];

    NSString *price = [[jsonObject valueForKey:KEY_PRICE] objectAtIndex:0];
    NSString *currency = [[jsonObject valueForKey:KEY_CURRENCY] objectAtIndex:0];
    NSString *transactionId = [[jsonObject valueForKey:KEY_TRANSACTION_ID] objectAtIndex:0];
    NSString *receipt = [[jsonObject valueForKey:KEY_RECEIPT] objectAtIndex:0];
    NSString *transactionDate = [[jsonObject valueForKey:KEY_TRANSACTION_DATE] objectAtIndex:0];
    NSString *salesRegion = [[jsonObject valueForKey:KEY_SALES_REGION] objectAtIndex:0];
    NSMutableArray *callbackParameters = [[NSMutableArray alloc] init];
    NSMutableArray *partnerParameters = [[NSMutableArray alloc] init];

    for (id item in [[jsonObject valueForKey:KEY_CALLBACK_PARAMETERS] objectAtIndex:0]) {
        [callbackParameters addObject:item];
    }
    for (id item in [[jsonObject valueForKey:KEY_PARTNER_PARAMETERS] objectAtIndex:0]) {
        [partnerParameters addObject:item];
    }

    // Price.
    NSDecimalNumber *priceValue;
    if ([self isFieldValid:price]) {
        priceValue = [NSDecimalNumber decimalNumberWithString:price];
    }

    // Receipt.
    NSData *receiptValue;
    if ([self isFieldValid:receipt]) {
        receiptValue = [receipt dataUsingEncoding:NSUTF8StringEncoding];
    }

    ADJSubscription *subscription = [[ADJSubscription alloc] initWithPrice:priceValue
                                                                  currency:currency
                                                             transactionId:transactionId
                                                                andReceipt:receiptValue];

    // Transaction date.
    if ([self isFieldValid:transactionDate]) {
        NSTimeInterval transactionDateInterval = [transactionDate doubleValue];
        NSDate *oTransactionDate = [NSDate dateWithTimeIntervalSince1970:transactionDateInterval];
        [subscription setTransactionDate:oTransactionDate];
    }

    // Sales region.
    if ([self isFieldValid:salesRegion]) {
        [subscription setSalesRegion:salesRegion];
    }

    // Callback parameters.
    for (int i = 0; i < [callbackParameters count]; i += 2) {
        NSString *key = [callbackParameters objectAtIndex:i];
        NSObject *value = [callbackParameters objectAtIndex:(i+1)];
        [subscription addCallbackParameter:key value:[NSString stringWithFormat:@"%@", value]];
    }

    // Partner parameters.
    for (int i = 0; i < [partnerParameters count]; i += 2) {
        NSString *key = [partnerParameters objectAtIndex:i];
        NSObject *value = [partnerParameters objectAtIndex:(i+1)];
        [subscription addPartnerParameter:key value:[NSString stringWithFormat:@"%@", value]];
    }

    // Track subscription.
    [Adjust trackSubscription:subscription];
}

- (void)setAttributionCallback:(CDVInvokedUrlCommand *)command {
    attributionCallbackId = command.callbackId;
}

- (void)setEventTrackingSucceededCallback:(CDVInvokedUrlCommand *)command {
    eventSucceededCallbackId = command.callbackId;
}

- (void)setEventTrackingFailedCallback:(CDVInvokedUrlCommand *)command {
    eventFailedCallbackId = command.callbackId;
}

- (void)setSessionTrackingSucceededCallback:(CDVInvokedUrlCommand *)command {
    sessionSucceededCallbackId = command.callbackId;
}

- (void)setSessionTrackingFailedCallback:(CDVInvokedUrlCommand *)command {
    sessionFailedCallbackId = command.callbackId;
}

- (void)setDeferredDeeplinkCallback:(CDVInvokedUrlCommand *)command {
    deferredDeeplinkCallbackId = command.callbackId;
}

- (void)addSessionCallbackParameter:(CDVInvokedUrlCommand *)command {
    NSString *key = [command argumentAtIndex:0 withDefault:nil];
    NSString *value = [command argumentAtIndex:1 withDefault:nil];
    if (!([self isFieldValid:key]) || !([self isFieldValid:value])) {
        return;
    }
    [Adjust addSessionCallbackParameter:key value:value];
}

- (void)removeSessionCallbackParameter:(CDVInvokedUrlCommand *)command {
    NSString *key = [command argumentAtIndex:0 withDefault:nil];
    if (!([self isFieldValid:key])) {
        return;
    }
    [Adjust removeSessionCallbackParameter:key];
}

- (void)resetSessionCallbackParameters:(CDVInvokedUrlCommand *)command {
    [Adjust resetSessionCallbackParameters];
}

- (void)addSessionPartnerParameter:(CDVInvokedUrlCommand *)command {
    NSString *key = [command argumentAtIndex:0 withDefault:nil];
    NSString *value = [command argumentAtIndex:1 withDefault:nil];
    if (!([self isFieldValid:key]) || !([self isFieldValid:value])) {
        return;
    }
    [Adjust addSessionPartnerParameter:key value:value];
}

- (void)removeSessionPartnerParameter:(CDVInvokedUrlCommand *)command {
    NSString *key = [command argumentAtIndex:0 withDefault:nil];
    if (!([self isFieldValid:key])) {
        return;
    }
    [Adjust removeSessionPartnerParameter:key];
}

- (void)resetSessionPartnerParameters:(CDVInvokedUrlCommand *)command {
    [Adjust resetSessionPartnerParameters];
}

- (void)setTestOptions:(CDVInvokedUrlCommand *)command {
    NSString *hasContext = [[command.arguments valueForKey:KEY_HAS_CONTEXT] objectAtIndex:0];
    NSString *baseUrl = [[command.arguments valueForKey:KEY_BASE_URL] objectAtIndex:0];
    NSString *gdprUrl = [[command.arguments valueForKey:KEY_GDPR_URL] objectAtIndex:0];
    NSString *subscriptionUrl = [[command.arguments valueForKey:KEY_SUBSCRIPTION_URL] objectAtIndex:0];
    NSString *extraPath = [[command.arguments valueForKey:KEY_EXTRA_PATH] objectAtIndex:0];
    NSString *timerInterval = [[command.arguments valueForKey:KEY_TIMER_INTERVAL] objectAtIndex:0];
    NSString *timerStart = [[command.arguments valueForKey:KEY_TIMER_START] objectAtIndex:0];
    NSString *sessionInterval = [[command.arguments valueForKey:KEY_SESSION_INTERVAL] objectAtIndex:0];
    NSString *subsessionInterval = [[command.arguments valueForKey:KEY_SUBSESSION_INTERVAL] objectAtIndex:0];
    NSString *teardown = [[command.arguments valueForKey:KEY_TEARDOWN] objectAtIndex:0];
    NSString *noBackoffWait = [[command.arguments valueForKey:KEY_NO_BACKOFF_WAIT] objectAtIndex:0];
    NSString *iAdFrameworkEnabled = [[command.arguments valueForKey:KEY_IAD_ENABLED] objectAtIndex:0];
    
    AdjustTestOptions *testOptions = [[AdjustTestOptions alloc] init];
    
    if ([self isFieldValid:baseUrl]) {
        testOptions.baseUrl = baseUrl;
    }

    if ([self isFieldValid:gdprUrl]) {
        testOptions.gdprUrl = gdprUrl;
    }

    if ([self isFieldValid:subscriptionUrl]) {
        testOptions.subscriptionUrl = subscriptionUrl;
    }
    
    if ([self isFieldValid:extraPath]) {
        testOptions.extraPath = extraPath;
    }
    
    if ([self isFieldValid:timerInterval]) {
        testOptions.timerIntervalInMilliseconds = [self convertMilliStringToNumber:timerInterval];
    }
    
    if ([self isFieldValid:timerStart]) {
        testOptions.timerStartInMilliseconds = [self convertMilliStringToNumber:timerStart];
    }
    
    if ([self isFieldValid:sessionInterval]) {
        testOptions.sessionIntervalInMilliseconds = [self convertMilliStringToNumber:sessionInterval];
    }
    
    if ([self isFieldValid:subsessionInterval]) {
        testOptions.subsessionIntervalInMilliseconds = [self convertMilliStringToNumber:subsessionInterval];
    }
    
    if ([self isFieldValid:teardown]) {
        testOptions.teardown = [teardown boolValue];
    }

    if ([self isFieldValid:noBackoffWait]) {
        testOptions.noBackoffWait = [noBackoffWait boolValue];
    }
    
    if ([self isFieldValid:hasContext]) {
        testOptions.deleteState = [hasContext boolValue];
    }

    if ([self isFieldValid:iAdFrameworkEnabled]) {
        testOptions.iAdFrameworkEnabled = [iAdFrameworkEnabled boolValue];
    }
    
    [Adjust setTestOptions:testOptions];
}

- (void)teardown:(CDVInvokedUrlCommand *)command {
    attributionCallbackId = nil;
    eventFailedCallbackId = nil;
    eventSucceededCallbackId = nil;
    sessionFailedCallbackId = nil;
    sessionSucceededCallbackId = nil;
    deferredDeeplinkCallbackId = nil;
    [AdjustCordovaDelegate teardown];
}

- (void)onPause:(CDVInvokedUrlCommand *)command {
    [Adjust trackSubsessionEnd];
}

- (void)onResume:(CDVInvokedUrlCommand *)command {
    [Adjust trackSubsessionStart];
}

- (void)setReferrer:(CDVInvokedUrlCommand *)command {}

- (void)trackPlayStoreSubscription:(CDVInvokedUrlCommand *)command {}

- (void)getGoogleAdId:(CDVInvokedUrlCommand *)command {
    NSString *googleAdId = @"";
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:googleAdId];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)getAmazonAdId:(CDVInvokedUrlCommand *)command {
    NSString *amazonAdId = @"";
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:amazonAdId];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

#pragma mark - Private & helper methods

- (NSNumber *)convertMilliStringToNumber:(NSString *)milliS {
    NSNumber *number = [NSNumber numberWithInt:[milliS intValue]];
    return number;
}

- (BOOL)isFieldValid:(NSObject *)field {
    if (field == nil) {
        return NO;
    }
    
    // Check if its an instance of the singleton NSNull
    if ([field isKindOfClass:[NSNull class]]) {
        return NO;
    }
    
    // If field can be converted to a string, check if it has any content.
    NSString *str = [NSString stringWithFormat:@"%@", field];
    if (str != nil) {
        if ([str length] == 0) {
            return NO;
        }
    }
    
    return YES;
}

- (void)addValueOrEmpty:(NSObject *)value
                withKey:(NSString *)key
           toDictionary:(NSMutableDictionary *)dictionary {
    if (nil != value) {
        [dictionary setObject:[NSString stringWithFormat:@"%@", value] forKey:key];
    } else {
        [dictionary setObject:@"" forKey:key];
    }
}

@end
