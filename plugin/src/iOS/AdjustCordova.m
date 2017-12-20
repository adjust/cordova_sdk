//
//  AdjustCordova.m
//  Adjust
//
//  Created by Pedro Filipe on 04/03/14.
//  Copyright (c) 2012-2014 adjust GmbH. All rights reserved.
//

#import <Cordova/CDVPluginResult.h>

#import "AdjustCordova.h"
#import "AdjustCordovaDelegate.h"

#define KEY_APP_TOKEN               @"appToken"
#define KEY_ENVIRONMENT             @"environment"
#define KEY_LOG_LEVEL               @"logLevel"
#define KEY_SDK_PREFIX              @"sdkPrefix"
#define KEY_DEFAULT_TRACKER         @"defaultTracker"
#define KEY_EVENT_BUFFERING_ENABLED @"eventBufferingEnabled"
#define KEY_EVENT_TOKEN             @"eventToken"
#define KEY_REVENUE                 @"revenue"
#define KEY_CURRENCY                @"currency"
#define KEY_RECEIPT                 @"receipt"
#define KEY_TRANSACTION_ID          @"transactionId"
#define KEY_CALLBACK_PARAMETERS     @"callbackParameters"
#define KEY_PARTNER_PARAMETERS      @"partnerParameters"
#define KEY_IS_RECEIPT_SET          @"isReceiptSet"
#define KEY_USER_AGENT              @"userAgent"
#define KEY_REFERRER                @"referrer"
#define KEY_SHOULD_LAUNCH_DEEPLINK  @"shouldLaunchDeeplink"
#define KEY_SEND_IN_BACKGROUND      @"sendInBackground"
#define KEY_DELAY_START             @"delayStart"

@implementation AdjustCordova {
    NSString *attributionCallbackId;
    NSString *eventFailedCallbackId;
    NSString *eventSucceededCallbackId;
    NSString *sessionFailedCallbackId;
    NSString *sessionSucceededCallbackId;
    NSString *deferredDeeplinkCallbackId;
}

- (void)pluginInitialize {
    attributionCallbackId = nil;
    eventFailedCallbackId = nil;
    eventSucceededCallbackId = nil;
    sessionFailedCallbackId = nil;
    sessionSucceededCallbackId = nil;
    deferredDeeplinkCallbackId = nil;
}

- (void)create:(CDVInvokedUrlCommand *)command {
    NSString *arguments = [command.arguments objectAtIndex:0];
    NSArray *jsonObject = [NSJSONSerialization JSONObjectWithData:[arguments dataUsingEncoding:NSUTF8StringEncoding]
                                                          options:0
                                                            error:NULL];

    NSString *appToken = [[jsonObject valueForKey:KEY_APP_TOKEN] objectAtIndex:0];
    NSString *environment = [[jsonObject valueForKey:KEY_ENVIRONMENT] objectAtIndex:0];
    NSString *logLevel = [[jsonObject valueForKey:KEY_LOG_LEVEL] objectAtIndex:0];
    NSString *sdkPrefix = [[jsonObject valueForKey:KEY_SDK_PREFIX] objectAtIndex:0];
    NSString *defaultTracker = [[jsonObject valueForKey:KEY_DEFAULT_TRACKER] objectAtIndex:0];
    NSNumber *eventBufferingEnabled = [[jsonObject valueForKey:KEY_EVENT_BUFFERING_ENABLED] objectAtIndex:0];
    NSNumber *sendInBackground = [[jsonObject valueForKey:KEY_SEND_IN_BACKGROUND] objectAtIndex:0];
    NSNumber *shouldLaunchDeeplink = [[jsonObject valueForKey:KEY_SHOULD_LAUNCH_DEEPLINK] objectAtIndex:0];
    NSString *userAgent = [[jsonObject valueForKey:KEY_USER_AGENT] objectAtIndex:0];
    NSNumber *delayStart = [[jsonObject valueForKey:KEY_DELAY_START] objectAtIndex:0];

    BOOL allowSuppressLogLevel = false;

    // Log level
    if ([self isFieldValid:logLevel]) {
        if ([ADJLogger logLevelFromString:[logLevel lowercaseString]] == ADJLogLevelSuppress) {
            allowSuppressLogLevel = true;
        }
    }

    ADJConfig *adjustConfig = [ADJConfig configWithAppToken:appToken environment:environment allowSuppressLogLevel:allowSuppressLogLevel];

    if (![adjustConfig isValid]) {
        return;
    }
    // Log level
    if ([self isFieldValid:logLevel]) {
        [adjustConfig setLogLevel:[ADJLogger logLevelFromString:[logLevel lowercaseString]]];
    }

    // Event buffering
    if ([self isFieldValid:eventBufferingEnabled]) {
        [adjustConfig setEventBufferingEnabled:[eventBufferingEnabled boolValue]];
    }

    // SDK prefix
    if ([self isFieldValid:sdkPrefix]) {
        [adjustConfig setSdkPrefix:sdkPrefix];
    }

    // Default tracker
    if ([self isFieldValid:defaultTracker]) {
        [adjustConfig setDefaultTracker:defaultTracker];
    }

    BOOL isAttributionCallbackImplemented = attributionCallbackId != nil ? YES : NO;
    BOOL isEventSucceededCallbackImplemented = eventSucceededCallbackId != nil ? YES : NO;
    BOOL isEventFailedCallbackImplemented = eventFailedCallbackId != nil ? YES : NO;
    BOOL isSessionSucceededCallbackImplemented = sessionSucceededCallbackId != nil ? YES : NO;
    BOOL isSessionFailedCallbackImplemented = sessionFailedCallbackId != nil ? YES : NO;
    BOOL isDeferredDeeplinkCallbackImplemented = deferredDeeplinkCallbackId != nil ? YES : NO;
    BOOL shouldLaunchDeferredDeeplink = [self isFieldValid:shouldLaunchDeeplink] ? [shouldLaunchDeeplink boolValue] : YES;

    // Attribution delegate & other delegates
    if (isAttributionCallbackImplemented ||
        isEventSucceededCallbackImplemented ||
        isEventFailedCallbackImplemented ||
        isSessionSucceededCallbackImplemented ||
        isSessionFailedCallbackImplemented ||
        isDeferredDeeplinkCallbackImplemented) 
    {
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

    // Send in background
    if ([self isFieldValid:sendInBackground]) {
        [adjustConfig setSendInBackground:[sendInBackground boolValue]];
    }

    // User agent
    if ([self isFieldValid:userAgent]) {
        [adjustConfig setUserAgent:userAgent];
    }

    // Delay start
    if ([self isFieldValid:delayStart]) {
        [adjustConfig setDelayStart:[delayStart doubleValue]];
    }

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

    if ([self isFieldValid:revenue]) {
        double revenueValue = [revenue doubleValue];

        [adjustEvent setRevenue:revenueValue currency:currency];
    }

    for (int i = 0; i < [callbackParameters count]; i += 2) {
        NSString *key = [callbackParameters objectAtIndex:i];
        NSObject *value = [callbackParameters objectAtIndex:(i+1)];

        [adjustEvent addCallbackParameter:key value:[NSString stringWithFormat:@"%@", value]];
    }

    for (int i = 0; i < [partnerParameters count]; i += 2) {
        NSString *key = [partnerParameters objectAtIndex:i];
        NSObject *value = [partnerParameters objectAtIndex:(i+1)];

        [adjustEvent addPartnerParameter:key value:[NSString stringWithFormat:@"%@", value]];
    }

    BOOL isTransactionIdSet = false;

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

    [Adjust trackEvent:adjustEvent];
}

- (void)setOfflineMode:(CDVInvokedUrlCommand *)command {
    NSNumber *isEnabledNumber = [command argumentAtIndex:0 withDefault:nil];

    if (isEnabledNumber == nil) {
        return;
    }

    [Adjust setOfflineMode:[isEnabledNumber boolValue]];
}

- (void)appWillOpenUrl:(CDVInvokedUrlCommand *)command {
    NSString *urlString = [command argumentAtIndex:0 withDefault:nil];

    if (urlString == nil) {
        return;
    }

    NSURL *url = [NSURL URLWithString:[urlString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];

    [Adjust appWillOpenUrl:url];
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

    [self addValueOrEmpty:dictionary key:@"trackerToken" value:attribution.trackerToken];
    [self addValueOrEmpty:dictionary key:@"trackerName" value:attribution.trackerName];
    [self addValueOrEmpty:dictionary key:@"network" value:attribution.network];
    [self addValueOrEmpty:dictionary key:@"campaign" value:attribution.campaign];
    [self addValueOrEmpty:dictionary key:@"creative" value:attribution.creative];
    [self addValueOrEmpty:dictionary key:@"adgroup" value:attribution.adgroup];
    [self addValueOrEmpty:dictionary key:@"clickLabel" value:attribution.clickLabel];
    [self addValueOrEmpty:dictionary key:@"adid" value:attribution.adid];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictionary];

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

- (void)onPause:(CDVInvokedUrlCommand *)command {}

- (void)onResume:(CDVInvokedUrlCommand *)command {}

- (void)getGoogleAdId:(CDVInvokedUrlCommand *)command {}

- (void)sendFirstPackages:(CDVInvokedUrlCommand *)command {
    [Adjust sendFirstPackages];
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

- (void)setPushToken:(CDVInvokedUrlCommand *)command {
    NSString *token = [command argumentAtIndex:0 withDefault:nil];

    if (!([self isFieldValid:token])) {
        return;
    }

    [Adjust setDeviceToken:[token dataUsingEncoding:NSUTF8StringEncoding]];
}

- (BOOL)isFieldValid:(NSObject *)field {
    return field != nil && ![field isKindOfClass:[NSNull class]];
}

- (void)addValueOrEmpty:(NSMutableDictionary *)dictionary
                    key:(NSString *)key
                  value:(NSObject *)value {
    if (nil != value) {
        [dictionary setObject:[NSString stringWithFormat:@"%@", value] forKey:key];
    } else {
        [dictionary setObject:@"" forKey:key];
    }
}

@end
