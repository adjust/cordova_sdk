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

#define BOOL_TRUE                       @"true"
#define BOOL_FALSE                      @"false"

#define SDK_PREFIX                      @"cordova4.0.0"

#define KEY_APP_TOKEN                   @"appToken"
#define KEY_ENVIRONMENT                 @"environment"
#define KEY_LOG_LEVEL                   @"logLevel"
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

@implementation AdjustCordova

static NSString *callbackId = nil;
static BOOL isAttributionCallbackSet = NO;

- (CDVPlugin *)initWithWebView:(UIWebView *)theWebView {
    self = [super initWithWebView:theWebView];

    if (self) {
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
    NSString *defaultTracker = [[command.arguments objectAtIndex:0] objectForKey:KEY_DEFAULT_TRACKER];
    NSString *eventBufferingEnabled = [[command.arguments objectAtIndex:0] objectForKey:KEY_EVENT_BUFFERING_ENABLED];
    NSString *macMd5TrackingEnabled = [[command.arguments objectAtIndex:0] objectForKey:KEY_MAC_MD5_TRACKING_ENABLED];

    ADJConfig *adjustConfig = [ADJConfig configWithAppToken:appToken environment:environment];

    if ([adjustConfig isValid]) {
        // Log level
        if ([self isFieldValid:logLevel]) {
            [adjustConfig setLogLevel:[ADJLogger LogLevelFromString:[logLevel lowercaseString]]];
        }

        // Event buffering
        if ([self isFieldValid:eventBufferingEnabled]) {
            if ([eventBufferingEnabled isEqualToString:BOOL_TRUE]) {
                [adjustConfig setEventBufferingEnabled:YES];
            } else {
                [adjustConfig setEventBufferingEnabled:NO];
            }
        }

        // SDK Prefix
        // No matter what is maybe set, we're setting it in here.
        [adjustConfig setSdkPrefix:SDK_PREFIX];

        // MAC MD5 tracking
        if ([self isFieldValid:macMd5TrackingEnabled]) {
            if ([macMd5TrackingEnabled isEqualToString:BOOL_TRUE]) {
                [adjustConfig setMacMd5TrackingEnabled:YES];
            } else {
                [adjustConfig setMacMd5TrackingEnabled:NO];
            }
        }

        // Default tracker
        if ([self isFieldValid:defaultTracker]) {
            [adjustConfig setDefaultTracker:defaultTracker];
        }

        // Attribution delegate
        if (isAttributionCallbackSet) {
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

    NSMutableDictionary *callbackParameters = [[NSMutableDictionary alloc] init];
    NSMutableDictionary *partnerParameters = [[NSMutableDictionary alloc] init];

    for (id key in [[command.arguments objectAtIndex:0] objectForKey:KEY_CALLBACK_PARAMETERS]) {
        id value = [[[command.arguments objectAtIndex:0] objectForKey:KEY_CALLBACK_PARAMETERS] objectForKey:key];

        [callbackParameters setObject:value forKey:key];
    }

    for (id key in [[command.arguments objectAtIndex:0] objectForKey:KEY_PARTNER_PARAMETERS]) {
        id value = [[[command.arguments objectAtIndex:0] objectForKey:KEY_PARTNER_PARAMETERS] objectForKey:key];

        [partnerParameters setObject:value forKey:key];
    }

    ADJEvent *adjustEvent = [ADJEvent eventWithEventToken:eventToken];

    if ([adjustEvent isValid]) {
        if ([self isFieldValid:revenue]) {
            double revenueValue = [revenue doubleValue];

            [adjustEvent setRevenue:revenueValue currency:currency];
        }

        for (id key in callbackParameters) {
            id value = [callbackParameters objectForKey:key];

            [adjustEvent addCallbackParameter:key value:value];
        }

        for (id key in partnerParameters) {
            id value = [partnerParameters objectForKey:key];

            [adjustEvent addPartnerParameter:key value:value];
        }

        if ([self isFieldValid:receipt]) {
            if ([self isFieldValid:transactionId]) {
                [adjustEvent setReceipt:[receipt dataUsingEncoding:NSUTF8StringEncoding] transactionId:transactionId];
            }
        } else {
            if ([self isFieldValid:transactionId]) {
                [adjustEvent setTransactionId:transactionId];
            }
        }

        [Adjust trackEvent:adjustEvent];
    }
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
    isAttributionCallbackSet = YES;
}

- (BOOL)isFieldValid:(NSString *)field {
    if (![field isKindOfClass:[NSNull class]]) {
        if (field != nil) {
            return YES;
        }
    }
    
    return NO;
}

@end