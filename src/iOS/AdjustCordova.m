//
//  AdjustCordova.m
//  Adjust
//
//  Created by Pedro Filipe on 04/03/14.
//  Copyright (c) 2012-2014 adjust GmbH. All rights reserved.
//

#import <Cordova/CDVPluginResult.h>
#import "AdjustCordova.h"

#define KEY_APP_TOKEN                   @"appToken"
#define KEY_ENVIRONMENT                 @"environment"
#define KEY_LOG_LEVEL                   @"logLevel"
#define KEY_SDK_PREFIX                  @"sdkPrefix"
#define KEY_DEFAULT_TRACKER             @"defaultTracker"
#define KEY_EVENT_BUFFERING_ENABLED     @"eventBufferingEnabled"
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

- (void)pluginInitialize {
    callbackId = nil;
}

- (void)adjustAttributionChanged:(ADJAttribution *)attribution {
    NSDictionary *attributionDictionary = [attribution dictionary];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:attributionDictionary];
    pluginResult.keepCallback = [NSNumber numberWithBool:YES];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
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

    ADJConfig *adjustConfig = [ADJConfig configWithAppToken:appToken environment:environment];

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

        // Attribution delegate
        if (callbackId != nil) {
            [adjustConfig setDelegate:self];
        }

        [Adjust appDidLaunch:adjustConfig];
    }
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
