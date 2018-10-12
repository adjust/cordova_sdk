//
//  AdjustCordovaDelegate.m
//  Adjust SDK
//
//  Created by Uglješa Erceg (@uerceg) on 16th November 2016.
//  Copyright (c) 2012-2018 Adjust GmbH. All rights reserved.
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

+ (id)getInstanceWithSwizzleOfAttributionCallback:(BOOL)swizzleAttributionCallback
                           eventSucceededCallback:(BOOL)swizzleEventSucceededCallback
                              eventFailedCallback:(BOOL)swizzleEventFailedCallback
                         sessionSucceededCallback:(BOOL)swizzleSessionSucceededCallback
                            sessionFailedCallback:(BOOL)swizzleSessionFailedCallback
                         deferredDeeplinkCallback:(BOOL)swizzleDeferredDeeplinkCallback
                         andAttributionCallbackId:(NSString *)attributionCallbackId
                         eventSucceededCallbackId:(NSString *)eventSucceededCallbackId
                            eventFailedCallbackId:(NSString *)eventFailedCallbackId
                       sessionSucceededCallbackId:(NSString *)sessionSucceededCallbackId
                          sessionFailedCallbackId:(NSString *)sessionFailedCallbackId
                       deferredDeeplinkCallbackId:(NSString *)deferredDeeplinkCallbackId
                     shouldLaunchDeferredDeeplink:(BOOL)shouldLaunchDeferredDeeplink
                              withCommandDelegate:(id<CDVCommandDelegate>)adjustCordovaCommandDelegate {
    dispatch_once(&onceToken, ^{
        defaultInstance = [[AdjustCordovaDelegate alloc] init];

        // Do the swizzling where and if needed.
        if (swizzleAttributionCallback) {
            [defaultInstance swizzleCallbackMethod:@selector(adjustAttributionChanged:)
                                  swizzledSelector:@selector(adjustAttributionChangedWannabe:)];
        }
        if (swizzleEventSucceededCallback) {
            [defaultInstance swizzleCallbackMethod:@selector(adjustEventTrackingSucceeded:)
                                  swizzledSelector:@selector(adjustEventTrackingSucceededWannabe:)];
        }
        if (swizzleEventFailedCallback) {
            [defaultInstance swizzleCallbackMethod:@selector(adjustEventTrackingFailed:)
                                  swizzledSelector:@selector(adjustEventTrackingFailedWannabe:)];
        }
        if (swizzleSessionSucceededCallback) {
            [defaultInstance swizzleCallbackMethod:@selector(adjustSessionTrackingSucceeded:)
                                  swizzledSelector:@selector(adjustSessionTrackingSucceededWannabe:)];
        }
        if (swizzleSessionFailedCallback) {
            [defaultInstance swizzleCallbackMethod:@selector(adjustSessionTrackingFailed:)
                                  swizzledSelector:@selector(adjustSessionTrackingFailedWananbe:)];
        }
        if (swizzleDeferredDeeplinkCallback) {
            [defaultInstance swizzleCallbackMethod:@selector(adjustDeeplinkResponse:)
                                  swizzledSelector:@selector(adjustDeeplinkResponseWannabe:)];
        }

        [defaultInstance setAttributionCallbackId:attributionCallbackId];
        [defaultInstance setEventSucceededCallbackId:eventSucceededCallbackId];
        [defaultInstance setEventFailedCallbackId:eventFailedCallbackId];
        [defaultInstance setSessionSucceededCallbackId:sessionSucceededCallbackId];
        [defaultInstance setSessionFailedCallbackId:sessionFailedCallbackId];
        [defaultInstance setDeferredDeeplinkCallbackId:deferredDeeplinkCallbackId];
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
    [self addValueOrEmpty:attribution.adid withKey:@"adid" toDictionary:dictionary];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictionary];
    pluginResult.keepCallback = [NSNumber numberWithBool:YES];
    [_adjustCordovaCommandDelegate sendPluginResult:pluginResult callbackId:_attributionCallbackId];
}

- (void)adjustEventTrackingSucceededWannabe:(ADJEventSuccess *)eventSuccessResponseData {
    if (nil == eventSuccessResponseData) {
        return;
    }

    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
    [self addValueOrEmpty:eventSuccessResponseData.message withKey:@"message" toDictionary:dictionary];
    [self addValueOrEmpty:eventSuccessResponseData.timeStamp withKey:@"timestamp" toDictionary:dictionary];
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
    [self addValueOrEmpty:eventFailureResponseData.timeStamp withKey:@"timestamp" toDictionary:dictionary];
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
    [self addValueOrEmpty:sessionSuccessResponseData.timeStamp withKey:@"timestamp" toDictionary:dictionary];
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
    [self addValueOrEmpty:sessionFailureResponseData.timeStamp withKey:@"timestamp" toDictionary:dictionary];
    [self addValueOrEmpty:sessionFailureResponseData.adid withKey:@"adid" toDictionary:dictionary];
    [dictionary setObject:(sessionFailureResponseData.willRetry ? @"true" : @"false") forKey:@"willRetry"];
    if (sessionFailureResponseData.jsonResponse != nil) {
        [dictionary setObject:sessionFailureResponseData.jsonResponse forKey:@"jsonResponse"];
    }

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictionary];
    pluginResult.keepCallback = [NSNumber numberWithBool:YES];
    [_adjustCordovaCommandDelegate sendPluginResult:pluginResult callbackId:_sessionFailedCallbackId];
}

- (BOOL)adjustDeeplinkResponseWannabe:(NSURL *)deeplink {
    NSString *path = [deeplink absoluteString];
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:path];
    pluginResult.keepCallback = [NSNumber numberWithBool:YES];
    [_adjustCordovaCommandDelegate sendPluginResult:pluginResult callbackId:_deferredDeeplinkCallbackId];

    return _shouldLaunchDeferredDeeplink;
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
