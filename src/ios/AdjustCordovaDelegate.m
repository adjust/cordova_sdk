//
//  AdjustCordovaDelegate.m
//  Adjust SDK
//
//  Created by Uglješa Erceg (@uerceg) on 16th November 2016.
//  Copyright (c) 2016-2021 Adjust GmbH. All rights reserved.
//

#import <objc/runtime.h>
#import <Cordova/CDVPluginResult.h>
#import "AdjustCordovaDelegate.h"

static dispatch_once_t onceToken;
static AdjustCordovaDelegate *defaultInstance = nil;

@implementation AdjustCordovaDelegate

#pragma mark - Object lifecycle methods

- (id)init {
    self = [super init];
    if (nil == self) {
        return nil;
    }
    return self;
}

#pragma mark - Public methods

+ (id)getInstanceWithSwizzledAttributionCallbackId:(NSString *)attributionChangedCallbackId
                          eventSucceededCallbackId:(NSString *)eventSucceededCallbackId
                             eventFailedCallbackId:(NSString *)eventFailedCallbackId
                        sessionSucceededCallbackId:(NSString *)sessionSucceededCallbackId
                           sessionFailedCallbackId:(NSString *)sessionFailedCallbackId
                deferredDeeplinkReceivedCallbackId:(NSString *)deferredDeeplinkReceivedCallbackId
               skanConversionDataUpdatedCallbackId:(NSString *)skanConversionDataUpdatedCallbackId
                      shouldLaunchDeferredDeeplink:(BOOL)shouldLaunchDeferredDeeplink
                               withCommandDelegate:(id<CDVCommandDelegate>)adjustCordovaCommandDelegate {
    dispatch_once(&onceToken, ^{
        defaultInstance = [[AdjustCordovaDelegate alloc] init];

        // Do the swizzling where and if needed.
        if (attributionChangedCallbackId != nil &&
            attributionChangedCallbackId.length > 0) {
            [defaultInstance swizzleCallbackMethod:@selector(adjustAttributionChanged:)
                                  swizzledSelector:@selector(adjustAttributionChangedWannabe:)];
        }
        if (eventSucceededCallbackId != nil &&
            eventSucceededCallbackId.length > 0) {
            [defaultInstance swizzleCallbackMethod:@selector(adjustEventTrackingSucceeded:)
                                  swizzledSelector:@selector(adjustEventTrackingSucceededWannabe:)];
        }
        if (eventFailedCallbackId != nil &&
            eventFailedCallbackId.length > 0) {
            [defaultInstance swizzleCallbackMethod:@selector(adjustEventTrackingFailed:)
                                  swizzledSelector:@selector(adjustEventTrackingFailedWannabe:)];
        }
        if (sessionSucceededCallbackId != nil &&
            sessionSucceededCallbackId.length > 0) {
            [defaultInstance swizzleCallbackMethod:@selector(adjustSessionTrackingSucceeded:)
                                  swizzledSelector:@selector(adjustSessionTrackingSucceededWannabe:)];
        }
        if (sessionFailedCallbackId != nil &&
            sessionFailedCallbackId.length > 0) {
            [defaultInstance swizzleCallbackMethod:@selector(adjustSessionTrackingFailed:)
                                  swizzledSelector:@selector(adjustSessionTrackingFailedWananbe:)];
        }
        if (deferredDeeplinkReceivedCallbackId != nil &&
            deferredDeeplinkReceivedCallbackId.length > 0) {
            [defaultInstance swizzleCallbackMethod:@selector(adjustDeferredDeeplinkReceived:)
                                  swizzledSelector:@selector(adjustDeferredDeeplinkReceivedWannabe:)];
        }
        if (skanConversionDataUpdatedCallbackId != nil &&
            skanConversionDataUpdatedCallbackId.length > 0) {
            [defaultInstance swizzleCallbackMethod:@selector(adjustSkanUpdatedWithConversionData:)
                                  swizzledSelector:@selector(adjustSkanUpdatedWithConversionDataWannabe:)];
        }

        [defaultInstance setAttributionChangedCallbackId:attributionChangedCallbackId];
        [defaultInstance setEventSucceededCallbackId:eventSucceededCallbackId];
        [defaultInstance setEventFailedCallbackId:eventFailedCallbackId];
        [defaultInstance setSessionSucceededCallbackId:sessionSucceededCallbackId];
        [defaultInstance setSessionFailedCallbackId:sessionFailedCallbackId];
        [defaultInstance setDeferredDeeplinkReceivedCallbackId:deferredDeeplinkReceivedCallbackId];
        [defaultInstance setSkanConversionDataUpdatedCallbackId:skanConversionDataUpdatedCallbackId];
        [defaultInstance setShouldLaunchDeferredDeeplink:shouldLaunchDeferredDeeplink];
        [defaultInstance setAdjustCordovaCommandDelegate:adjustCordovaCommandDelegate];
    });
    
    return defaultInstance;
}

+ (void)teardown {
    defaultInstance = nil;
    onceToken = 0;
}

#pragma mark - Private & helper methods

- (void)adjustAttributionChangedWannabe:(ADJAttribution *)attribution {
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
    [self addValueOrEmpty:attribution.costType withKey:@"costType" toDictionary:dictionary];
    [self addValueOrEmpty:attribution.costAmount withKey:@"costAmount" toDictionary:dictionary];
    [self addValueOrEmpty:attribution.costCurrency withKey:@"costCurrency" toDictionary:dictionary];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictionary];
    pluginResult.keepCallback = [NSNumber numberWithBool:YES];
    [_adjustCordovaCommandDelegate sendPluginResult:pluginResult callbackId:_attributionChangedCallbackId];
}

- (void)adjustEventTrackingSucceededWannabe:(ADJEventSuccess *)eventSuccessResponseData {
    if (nil == eventSuccessResponseData) {
        return;
    }

    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
    [self addValueOrEmpty:eventSuccessResponseData.message withKey:@"message" toDictionary:dictionary];
    [self addValueOrEmpty:eventSuccessResponseData.timestamp withKey:@"timestamp" toDictionary:dictionary];
    [self addValueOrEmpty:eventSuccessResponseData.adid withKey:@"adid" toDictionary:dictionary];
    [self addValueOrEmpty:eventSuccessResponseData.eventToken withKey:@"eventToken" toDictionary:dictionary];
    [self addValueOrEmpty:eventSuccessResponseData.callbackId withKey:@"callbackId" toDictionary:dictionary];
    if (eventSuccessResponseData.jsonResponse != nil) {
        [dictionary setObject:eventSuccessResponseData.jsonResponse forKey:@"jsonResponse"];
    }

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictionary];
    pluginResult.keepCallback = [NSNumber numberWithBool:YES];
    [_adjustCordovaCommandDelegate sendPluginResult:pluginResult callbackId:_eventSucceededCallbackId];
}

- (void)adjustEventTrackingFailedWannabe:(ADJEventFailure *)eventFailureResponseData {
    if (nil == eventFailureResponseData) {
        return;
    }

    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
    [self addValueOrEmpty:eventFailureResponseData.message withKey:@"message" toDictionary:dictionary];
    [self addValueOrEmpty:eventFailureResponseData.timestamp withKey:@"timestamp" toDictionary:dictionary];
    [self addValueOrEmpty:eventFailureResponseData.adid withKey:@"adid" toDictionary:dictionary];
    [self addValueOrEmpty:eventFailureResponseData.eventToken withKey:@"eventToken" toDictionary:dictionary];
    [self addValueOrEmpty:eventFailureResponseData.callbackId withKey:@"callbackId" toDictionary:dictionary];
    [dictionary setObject:(eventFailureResponseData.willRetry ? @"true" : @"false") forKey:@"willRetry"];
    if (eventFailureResponseData.jsonResponse != nil) {
        [dictionary setObject:eventFailureResponseData.jsonResponse forKey:@"jsonResponse"];
    }

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictionary];
    pluginResult.keepCallback = [NSNumber numberWithBool:YES];
    [_adjustCordovaCommandDelegate sendPluginResult:pluginResult callbackId:_eventFailedCallbackId];
}

- (void)adjustSessionTrackingSucceededWannabe:(ADJSessionSuccess *)sessionSuccessResponseData {
    if (nil == sessionSuccessResponseData) {
        return;
    }

    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
    [self addValueOrEmpty:sessionSuccessResponseData.message withKey:@"message" toDictionary:dictionary];
    [self addValueOrEmpty:sessionSuccessResponseData.timestamp withKey:@"timestamp" toDictionary:dictionary];
    [self addValueOrEmpty:sessionSuccessResponseData.adid withKey:@"adid" toDictionary:dictionary];
    if (sessionSuccessResponseData.jsonResponse != nil) {
        [dictionary setObject:sessionSuccessResponseData.jsonResponse forKey:@"jsonResponse"];
    }

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictionary];
    pluginResult.keepCallback = [NSNumber numberWithBool:YES];
    [_adjustCordovaCommandDelegate sendPluginResult:pluginResult callbackId:_sessionSucceededCallbackId];
}

- (void)adjustSessionTrackingFailedWananbe:(ADJSessionFailure *)sessionFailureResponseData {
    if (nil == sessionFailureResponseData) {
        return;
    }

    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
    [self addValueOrEmpty:sessionFailureResponseData.message withKey:@"message" toDictionary:dictionary];
    [self addValueOrEmpty:sessionFailureResponseData.timestamp withKey:@"timestamp" toDictionary:dictionary];
    [self addValueOrEmpty:sessionFailureResponseData.adid withKey:@"adid" toDictionary:dictionary];
    [dictionary setObject:(sessionFailureResponseData.willRetry ? @"true" : @"false") forKey:@"willRetry"];
    if (sessionFailureResponseData.jsonResponse != nil) {
        [dictionary setObject:sessionFailureResponseData.jsonResponse forKey:@"jsonResponse"];
    }

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictionary];
    pluginResult.keepCallback = [NSNumber numberWithBool:YES];
    [_adjustCordovaCommandDelegate sendPluginResult:pluginResult callbackId:_sessionFailedCallbackId];
}

- (BOOL)adjustDeferredDeeplinkReceivedWannabe:(NSURL *)deeplink {
    NSString *path = [deeplink absoluteString];
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:path];
    pluginResult.keepCallback = [NSNumber numberWithBool:YES];
    [_adjustCordovaCommandDelegate sendPluginResult:pluginResult callbackId:_deferredDeeplinkReceivedCallbackId];

    return _shouldLaunchDeferredDeeplink;
}

- (void)adjustSkanUpdatedWithConversionDataWannabe:(nonnull NSDictionary<NSString *, NSString *> *)data {
    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
    [self addValueOrEmpty:[data objectForKey:@"conversion_value"] withKey:@"fineValue" toDictionary:dictionary];
    [self addValueOrEmpty:[data objectForKey:@"coarse_value"] withKey:@"coarseValue" toDictionary:dictionary];
    [self addValueOrEmpty:[data objectForKey:@"lock_window"] withKey:@"lockWindow" toDictionary:dictionary];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictionary];
    pluginResult.keepCallback = [NSNumber numberWithBool:YES];
    [_adjustCordovaCommandDelegate sendPluginResult:pluginResult callbackId:_skanConversionDataUpdatedCallbackId];
}

- (void)swizzleCallbackMethod:(SEL)originalSelector
             swizzledSelector:(SEL)swizzledSelector {
    Class class = [self class];
    Method originalMethod = class_getInstanceMethod(class, originalSelector);
    Method swizzledMethod = class_getInstanceMethod(class, swizzledSelector);
    BOOL didAddMethod = class_addMethod(class,
                                        originalSelector,
                                        method_getImplementation(swizzledMethod),
                                        method_getTypeEncoding(swizzledMethod));
    if (didAddMethod) {
        class_replaceMethod(class,
                            swizzledSelector,
                            method_getImplementation(originalMethod),
                            method_getTypeEncoding(originalMethod));
    } else {
        method_exchangeImplementations(originalMethod, swizzledMethod);
    }
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
