//
//  AdjustCordova.m
//  Adjust
//
//  Created by Pedro Filipe on 04/03/14.
//  Copyright (c) 2012-2014 adjust GmbH. All rights reserved.
//

#import <Cordova/CDVPluginResult.h>
#import <Adjust/ADJLogger.h>
#import "AdjustCordova.h"

#define KEY_APP_TOKEN                   @"appToken"
#define KEY_ENVIRONMENT                 @"environment"
#define KEY_LOG_LEVEL                   @"logLevel"
#define KEY_SDK_PREFIX                  @"sdkPrefix"
#define KEY_DEFAULT_TRACKER             @"defaultTracker"
#define KEY_EVENT_BUFFERING_ENABLED     @"eventBufferingEnabled"
#define KEY_MAC_MD5_TRACKING_ENABLED    @"macMd5TrackingEnabled"
#define KEY_EVENT_TOKEN                 @"eventToken"
#define KEY_REVENUE                     @"revenue"
#define KEY_CURRENCY                    @"currency"
#define KEY_RECEIPT                     @"receipt"
#define KEY_TRANSACTION_ID              @"transactionId"
#define KEY_CALLBACK_PARAMETERS         @"callbackParameters"
#define KEY_PARTNER_PARAMETERS          @"partnerParameters"
#define KEY_IS_RECEIPT_SET              @"isReceiptSet"

@implementation AdjustCordova {
    NSString *callbackId;
}

- (CDVPlugin *)initWithWebView:(UIWebView *)theWebView {
    self = [super initWithWebView:theWebView];

    if (self) {
        callbackId = nil;
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(handleOpenUrl:) name:CDVPluginHandleOpenURLNotification object:nil];
    }

    return self;
}

- (void)handleOpenUrl:(NSNotification *)notification {
    [Adjust appWillOpenUrl:[notification object]];
}

- (void)adjustAttributionChanged:(ADJAttribution *)attribution {
    NSDictionary *attributionDictionary = [attribution dictionary];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:attributionDictionary];
    pluginResult.keepCallback = [NSNumber numberWithBool:YES];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void)create:(CDVInvokedUrlCommand *)command {
    NSString *appToken = [[command.arguments objectAtIndex:0] objectForKey:KEY_APP_TOKEN];
    NSString *environment = [[command.arguments objectAtIndex:0] objectForKey:KEY_ENVIRONMENT];
    NSString *logLevel = [[command.arguments objectAtIndex:0] objectForKey:KEY_LOG_LEVEL];
    NSString *sdkPrefix = [[command.arguments objectAtIndex:0] objectForKey:KEY_SDK_PREFIX];
    NSString *defaultTracker = [[command.arguments objectAtIndex:0] objectForKey:KEY_DEFAULT_TRACKER];
    NSNumber *eventBufferingEnabled = [[command.arguments objectAtIndex:0] objectForKey:KEY_EVENT_BUFFERING_ENABLED];
    NSNumber *macMd5TrackingEnabled = [[command.arguments objectAtIndex:0] objectForKey:KEY_MAC_MD5_TRACKING_ENABLED];

    ADJConfig *adjustConfig = [ADJConfig configWithAppToken:appToken environment:environment];

    if ([adjustConfig isValid]) {
        // Log level
        if ([self isFieldValid:logLevel]) {
            [adjustConfig setLogLevel:[ADJLogger LogLevelFromString:[logLevel lowercaseString]]];
        }

        // Event buffering
        if ([self isFieldValid:eventBufferingEnabled]) {
            [adjustConfig setMacMd5TrackingEnabled:[eventBufferingEnabled boolValue]];
        }

        // SDK prefix
        if ([self isFieldValid:sdkPrefix]) {
            [adjustConfig setSdkPrefix:sdkPrefix];
        }

        // MAC MD5 tracking
        if ([self isFieldValid:macMd5TrackingEnabled]) {
            [adjustConfig setMacMd5TrackingEnabled:[macMd5TrackingEnabled boolValue]];
        }

        // Default tracker
        if ([self isFieldValid:defaultTracker]) {
            [adjustConfig setDefaultTracker:defaultTracker];
        }

        // Attribution delegate
        if (callbackId != nil) {
            [adjustConfig setDelegate:self];
        }

        [Adjust appDidLaunch:adjustConfig];
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

- (void)onPause:(CDVInvokedUrlCommand *)command {

}

- (void)onResume:(CDVInvokedUrlCommand *)command {

}

- (void)setAttributionCallback:(CDVInvokedUrlCommand *)command {
    callbackId = command.callbackId;
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