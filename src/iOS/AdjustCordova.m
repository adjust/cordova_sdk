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
    NSString *appToken = [[command.arguments objectAtIndex:0] objectForKey:KEY_APP_TOKEN];
    NSString *environment = [[command.arguments objectAtIndex:0] objectForKey:KEY_ENVIRONMENT];
    NSString *logLevel = [[command.arguments objectAtIndex:0] objectForKey:KEY_LOG_LEVEL];
    NSString *sdkPrefix = [[command.arguments objectAtIndex:0] objectForKey:KEY_SDK_PREFIX];
    NSString *defaultTracker = [[command.arguments objectAtIndex:0] objectForKey:KEY_DEFAULT_TRACKER];
    NSNumber *eventBufferingEnabled = [[command.arguments objectAtIndex:0] objectForKey:KEY_EVENT_BUFFERING_ENABLED];
    NSNumber *sendInBackground = [[command.arguments objectAtIndex:0] objectForKey:KEY_SEND_IN_BACKGROUND];
    NSNumber *shouldLaunchDeeplink = [[command.arguments objectAtIndex:0] objectForKey:KEY_SHOULD_LAUNCH_DEEPLINK];
    NSString *userAgent = [[command.arguments objectAtIndex:0] objectForKey:KEY_USER_AGENT];
    NSNumber *delayStart = [[command.arguments objectAtIndex:0] objectForKey:KEY_DELAY_START];

    BOOL allowSuppressLogLevel = false;

    // Log level
    if ([self isFieldValid:logLevel]) {
        if ([ADJLogger LogLevelFromString:[logLevel lowercaseString]] == ADJLogLevelSuppress) {
            allowSuppressLogLevel = true;
        }
    }

    ADJConfig *adjustConfig = [ADJConfig configWithAppToken:appToken environment:environment allowSuppressLogLevel:allowSuppressLogLevel];

    if ([adjustConfig isValid]) {
        // Log level
        if ([self isFieldValid:logLevel]) {
            [adjustConfig setLogLevel:[ADJLogger LogLevelFromString:[logLevel lowercaseString]]];
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
            isDeferredDeeplinkCallbackImplemented) {
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
}

- (void)trackEvent:(CDVInvokedUrlCommand *)command {
    NSString *eventToken = [[command.arguments objectAtIndex:0] objectForKey:KEY_EVENT_TOKEN];
    NSString *revenue = [[command.arguments objectAtIndex:0] objectForKey:KEY_REVENUE];
    NSString *currency = [[command.arguments objectAtIndex:0] objectForKey:KEY_CURRENCY];
    NSString *receipt = [[command.arguments objectAtIndex:0] objectForKey:KEY_RECEIPT];
    NSString *transactionId = [[command.arguments objectAtIndex:0] objectForKey:KEY_TRANSACTION_ID];
    NSNumber *isReceiptSet = [[command.arguments objectAtIndex:0] objectForKey:KEY_IS_RECEIPT_SET];

    NSMutableArray *callbackParameters = [[NSMutableArray alloc] init];
    NSMutableArray *partnerParameters = [[NSMutableArray alloc] init];

    for (id item in [[command.arguments objectAtIndex:0] objectForKey:KEY_CALLBACK_PARAMETERS]) {
        [callbackParameters addObject:item];
    }

    for (id item in [[command.arguments objectAtIndex:0] objectForKey:KEY_PARTNER_PARAMETERS]) {
        [partnerParameters addObject:item];
    }

    ADJEvent *adjustEvent = [ADJEvent eventWithEventToken:eventToken];

    if ([adjustEvent isValid]) {
        if ([self isFieldValid:revenue]) {
            double revenueValue = [revenue doubleValue];

            [adjustEvent setRevenue:revenueValue currency:currency];
        }

        for (int i = 0; i < [callbackParameters count]; i += 2) {
            NSString *key = [callbackParameters objectAtIndex:i];
            NSString *value = [callbackParameters objectAtIndex:(i+1)];

            [adjustEvent addCallbackParameter:key value:value];
        }

        for (int i = 0; i < [partnerParameters count]; i += 2) {
            NSString *key = [partnerParameters objectAtIndex:i];
            NSString *value = [partnerParameters objectAtIndex:(i+1)];

            [adjustEvent addPartnerParameter:key value:value];
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
    if (![field isKindOfClass:[NSNull class]]) {
        if (field != nil) {
            return YES;
        }
    }

    return NO;
}

@end
