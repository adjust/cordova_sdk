//
//  ADJPLogger.m
//  AdjustPurchase
//
//  Created by Uglješa Erceg on 24/11/15.
//  Copyright © 2015 adjust GmbH. All rights reserved.
//

#import "ADJPLogger.h"

static NSString * const kAdjustPurchaseLogTag = @"AdjustPurchase";

@interface ADJPLogger()

@property (nonatomic, assign) ADJPLogLevel loglevel;

@end

@implementation ADJPLogger

#pragma mark - Object lifecycle

+ (id)getInstance {
    static ADJPLogger *defaultInstance = nil;
    static dispatch_once_t onceToken;

    dispatch_once(&onceToken, ^{
        defaultInstance = [[self alloc] init];
    });

    return defaultInstance;
}

- (id)init {
    self = [super init];

    if (self == nil) {
        return nil;
    }

    self.loglevel = ADJPLogLevelInfo;

    return self;
}

#pragma mark - Public methods

+ (void)verbose:(NSString *)message, ... {
    [[ADJPLogger getInstance] verbose:message];
}

+ (void)debug:(NSString *)message, ... {
    [[ADJPLogger getInstance] debug:message];
}

+ (void)info:(NSString *)message, ... {
    [[ADJPLogger getInstance] info:message];
}

+ (void)warn:(NSString *)message, ... {
    [[ADJPLogger getInstance] warn:message];
}

+ (void)error:(NSString *)message, ... {
    [[ADJPLogger getInstance] error:message];
}

+ (void)assert:(NSString *)message, ... {
    [[ADJPLogger getInstance] assert:message];
}

+ (void)setLogLevel:(ADJPLogLevel)logLevel {
    [[ADJPLogger getInstance] setLogLevel:logLevel];
}

#pragma mark - Private methods

- (void)setLogLevel:(ADJPLogLevel)logLevel {
    self.loglevel = logLevel;
}

- (void)verbose:(NSString *)format, ... {
    if (self.loglevel > ADJPLogLevelVerbose) {
        return;
    }

    va_list parameters;
    va_start(parameters, format);

    [self logLevel:@"v" format:format parameters:parameters];
}

- (void)debug:(NSString *)format, ... {
    if (self.loglevel > ADJPLogLevelDebug) {
        return;
    }

    va_list parameters;
    va_start(parameters, format);

    [self logLevel:@"d" format:format parameters:parameters];
}

- (void)info:(NSString *)format, ... {
    if (self.loglevel > ADJPLogLevelInfo) {
        return;
    }

    va_list parameters;
    va_start(parameters, format);

    [self logLevel:@"i" format:format parameters:parameters];
}

- (void)warn:(NSString *)format, ... {
    if (self.loglevel > ADJPLogLevelWarn) {
        return;
    }

    va_list parameters;
    va_start(parameters, format);

    [self logLevel:@"w" format:format parameters:parameters];
}

- (void)error:(NSString *)format, ... {
    if (self.loglevel > ADJPLogLevelError) {
        return;
    }

    va_list parameters;
    va_start(parameters, format);

    [self logLevel:@"e" format:format parameters:parameters];
}

- (void)assert:(NSString *)format, ... {
    if (self.loglevel > ADJPLogLevelAssert) {
        return;
    }

    va_list parameters;
    va_start(parameters, format);

    [self logLevel:@"a" format:format parameters:parameters];
}

- (void)logLevel:(NSString *)logLevel format:(NSString *)format parameters:(va_list)parameters {
    NSString *string = [[NSString alloc] initWithFormat:format arguments:parameters];
    va_end(parameters);

    NSArray *lines = [string componentsSeparatedByString:@"\n"];

    for (NSString *line in lines) {
        NSLog(@"\t[%@]%@: %@", kAdjustPurchaseLogTag, logLevel, line);
    }
}

@end
