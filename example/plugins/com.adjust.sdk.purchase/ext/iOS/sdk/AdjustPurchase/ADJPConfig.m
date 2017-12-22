//
//  ADJPConfig.m
//  AdjustPurchase
//
//  Created by Uglješa Erceg on 07/12/15.
//  Copyright © 2015 adjust GmbH. All rights reserved.
//

#import "ADJPConfig.h"
#import "ADJPLogger.h"

static const NSUInteger kAppTokenLength = 12;

@implementation ADJPConfig

#pragma mark - Object lifecycle

- (id)initWithAppToken:(NSString *)appToken
        andEnvironment:(NSString *)environment {
    self = [super init];

    if (!self) {
        return self;
    }

    _appToken = appToken;
    _environment = environment;
    self.logLevel = ADJPLogLevelInfo;

    return self;
}

#pragma mark - Public methods

- (BOOL)isValid:(NSString *)errorMessage {
    NSString *message;

    if (self.appToken == nil) {
        message = @"App token can't be nil";
        [ADJPLogger error:message];

        if (errorMessage != nil) {
            errorMessage = message;
        }

        return NO;
    }

    if ([self.appToken length] != kAppTokenLength) {
        message = @"App token must be 12 characters long";
        [ADJPLogger error:message];

        if (errorMessage != nil) {
            errorMessage = message;
        }

        return NO;
    }

    if (self.environment == nil) {
        message = @"Environment can't be nil";
        [ADJPLogger error:message];

        if (errorMessage != nil) {
            errorMessage = message;
        }

        return NO;
    }

    if ([self.environment isEqualToString:ADJPEnvironmentSandbox]) {
        [ADJPLogger assert:@"SANDBOX: AdjustPurchase is running in sandbox mode. Use this setting for testing. Don't forget to set the environment to `production` before publishing!"];
        return YES;
    } else if ([self.environment isEqualToString:ADJPEnvironmentProduction]) {
        [ADJPLogger assert:@"PRODUCTION: AdjustPurchase is running in production mode. Use this setting only for the build that you want to publish. Set the environment to `sandbox` if you want to test your app!"];
        return YES;
    } else {
        message = [NSString stringWithFormat:@"Unknown environment '%@'", self.environment];
        [ADJPLogger error:message];

        if (errorMessage != nil) {
            errorMessage = message;
        }

        return NO;
    }
}

@end
