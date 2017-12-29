//
//  AdjustPurchaseCordova.m
//  AdjustPurchase
//
//  Created by Ugljesa Erceg on 04/03/14.
//  Copyright (c) 2012-2014 adjust GmbH. All rights reserved.
//

#import <Cordova/CDVPluginResult.h>
#import "AdjustPurchaseCordova.h"

#define KEY_APP_TOKEN                   @"appToken"
#define KEY_ENVIRONMENT                 @"environment"
#define KEY_LOG_LEVEL                   @"logLevel"
#define KEY_SDK_PREFIX                  @"sdkPrefix"

@implementation AdjustPurchaseCordova {
    NSString *verificationCallbackId;
}

- (void)pluginInitialize {
    verificationCallbackId = nil;
}

- (void)adjustVerificationUpdate:(ADJPVerificationInfo *)info {
    NSMutableDictionary *dictVerificationInfo = [[NSMutableDictionary alloc] init];

    [dictVerificationInfo setObject:info.message forKey:@"message"];
    [dictVerificationInfo setObject:[info getVerificationStateAsString] forKey:@"verificationState"];
    [dictVerificationInfo setObject:[NSString stringWithString:[NSString stringWithFormat:@"%d", info.statusCode]] forKey:@"statusCode"];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictVerificationInfo];
    pluginResult.keepCallback = [NSNumber numberWithBool:YES];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:verificationCallbackId];
    verificationCallbackId = nil;
}

- (void)init:(CDVInvokedUrlCommand *)command {
    NSString *appToken = [[command.arguments objectAtIndex:0] objectForKey:KEY_APP_TOKEN];
    NSString *environment = [[command.arguments objectAtIndex:0] objectForKey:KEY_ENVIRONMENT];
    NSString *logLevel = [[command.arguments objectAtIndex:0] objectForKey:KEY_LOG_LEVEL];
    NSString *sdkPrefix = [[command.arguments objectAtIndex:0] objectForKey:KEY_SDK_PREFIX];

    ADJPConfig *adjustConfig = [[ADJPConfig alloc] initWithAppToken:appToken andEnvironment:environment];

    if (adjustConfig == nil) {
        return;
    }

    // Log level
    if ([self isFieldValid:logLevel]) {
        if ([logLevel caseInsensitiveCompare:@"VERBOSE"] == NSOrderedSame) {
            [adjustConfig setLogLevel:ADJPLogLevelVerbose];
        } else if ([logLevel caseInsensitiveCompare:@"DEBUG"] == NSOrderedSame) {
            [adjustConfig setLogLevel:ADJPLogLevelDebug];
        } else if ([logLevel caseInsensitiveCompare:@"INFO"] == NSOrderedSame) {
            [adjustConfig setLogLevel:ADJPLogLevelInfo];
        } else if ([logLevel caseInsensitiveCompare:@"WARN"] == NSOrderedSame) {
            [adjustConfig setLogLevel:ADJPLogLevelWarn];
        } else if ([logLevel caseInsensitiveCompare:@"ERROR"] == NSOrderedSame) {
            [adjustConfig setLogLevel:ADJPLogLevelError];
        } else if ([logLevel caseInsensitiveCompare:@"ASSERT"] == NSOrderedSame) {
            [adjustConfig setLogLevel:ADJPLogLevelAssert];
        } else {
            [adjustConfig setLogLevel:ADJPLogLevelInfo];
        }
    }

    // SDK prefix
    if ([self isFieldValid:sdkPrefix]) {
        [adjustConfig setSdkPrefix:sdkPrefix];
    }

    [AdjustPurchase init:adjustConfig];
}

- (void)verifyPurchaseiOS:(CDVInvokedUrlCommand *)command {
    if (verificationCallbackId == nil) {
        return;
    }

    NSString *receipt = [command argumentAtIndex:0 withDefault:nil];
    NSString *transactionId = [command argumentAtIndex:1 withDefault:nil];
    NSString *productId = [command argumentAtIndex:2 withDefault:nil];

    [AdjustPurchase verifyPurchase:[receipt dataUsingEncoding:NSUTF8StringEncoding]
                  forTransactionId:transactionId
                         productId:productId
                 withResponseBlock:^(ADJPVerificationInfo *info) {
                     [self adjustVerificationUpdate:info];
                 }];
}

- (void)verifyPurchaseAndroid:(CDVInvokedUrlCommand *)command {

}

- (void)setVerificationCallback:(CDVInvokedUrlCommand *)command {
    verificationCallbackId = command.callbackId;
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
