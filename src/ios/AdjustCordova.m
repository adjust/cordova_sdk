//
//  AdjustCordova.m
//  Adjust
//
//  Created by Pedro Filipe on 04/03/14.
//  Copyright (c) 2012-2014 adjust GmbH. All rights reserved.
//

#import "AdjustCordova.h"
#import "AIAdjustFactory.h"
#import "AILogger.h"
#import "Adjust.h"

@implementation AdjustCordova

static NSString *callbackId = nil;

- (void)appDidLaunch:(CDVInvokedUrlCommand *)command {
    NSString *appToken = [command.arguments objectAtIndex:0];
    [Adjust appDidLaunch:appToken];

    [Adjust setSdkPrefix:@"cordova3.0.0"];

    NSString *environment = [command.arguments objectAtIndex:1];
    [Adjust setEnvironment:environment];

    NSString *logLevelString = [command.arguments objectAtIndex:2];
    NSDictionary *logLevelMap = @{
                                  @"verbose" : [NSNumber numberWithInt:AILogLevelVerbose],
                                  @"debug" : [NSNumber numberWithInt:AILogLevelDebug],
                                  @"info" : [NSNumber numberWithInt:AILogLevelInfo],
                                  @"warn" : [NSNumber numberWithInt:AILogLevelWarn],
                                  @"error" : [NSNumber numberWithInt:AILogLevelError],
                                  @"assert" : [NSNumber numberWithInt:AILogLevelAssert],
                                  };

    NSNumber* logLevelNumber = [logLevelMap objectForKey:logLevelString];
    if (logLevelNumber == nil) {
        logLevelNumber = [NSNumber numberWithInt:AILogLevelInfo];
    }
    AILogLevel logLevel = [logLevelNumber intValue];
    [Adjust setLogLevel:logLevel];

    NSNumber *enabledNumber = [command.arguments objectAtIndex:3];
    BOOL enabled = [enabledNumber boolValue];
    [Adjust setEventBufferingEnabled:enabled];
}

- (void)trackEvent:(CDVInvokedUrlCommand *)command {
    NSString *eventToken = [command.arguments objectAtIndex:0];
    NSDictionary *parameters = [command.arguments objectAtIndex:1];

    if ([parameters isKindOfClass:[NSDictionary class]]) {
        [Adjust trackEvent:eventToken withParameters:parameters];
    } else {
        [Adjust trackEvent:eventToken];
    }

}

- (void)trackRevenue:(CDVInvokedUrlCommand *)command {
    NSNumber *amountInCentsNumber = [command.arguments objectAtIndex:0];
    NSString *eventToken = [command.arguments objectAtIndex:1];
    NSDictionary *parameters = [command.arguments objectAtIndex:2];

    double amountInCentsDouble = [amountInCentsNumber doubleValue];

    if ([parameters isKindOfClass:[NSDictionary class]]) {
        [Adjust trackRevenue:amountInCentsDouble forEvent:eventToken withParameters:parameters];
    } else if ([eventToken isKindOfClass:[NSString class]]) {
        [Adjust trackRevenue:amountInCentsDouble forEvent:eventToken];
    } else {
        [Adjust trackRevenue:amountInCentsDouble];
    }
}

- (void)setFinishedTrackingCallback:(CDVInvokedUrlCommand *)command {
    callbackId = command.callbackId;
    [Adjust setDelegate:self];
}

- (void)adjustFinishedTrackingWithResponse:(AIResponseData *)responseData {
    NSDictionary *responseDataDic = [responseData dictionary];

    CDVPluginResult *pluginResult = [ CDVPluginResult
                                     resultWithStatus: CDVCommandStatus_OK
                                     messageAsDictionary: responseDataDic
                                     ];

    pluginResult.keepCallback = [NSNumber numberWithBool:YES];

    [self.commandDelegate sendPluginResult:pluginResult callbackId:callbackId];
}

- (void)onPause:(CDVInvokedUrlCommand *)command {
    [Adjust trackSubsessionEnd];
}

- (void)onResume:(CDVInvokedUrlCommand *)command {
    [Adjust trackSubsessionStart];
}

@end
