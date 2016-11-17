//
//  AdjustCordovaDelegate.m
//  Adjust
//
//  Created by uerceg on 11/16/16.
//  Copyright (c) 2012-2016 adjust GmbH. All rights reserved.
//

#import <objc/runtime.h>
#import <Cordova/CDVPluginResult.h>
#import "AdjustCordovaDelegate.h"

@implementation AdjustCordovaDelegate

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
    static dispatch_once_t onceToken;
    static AdjustCordovaDelegate *defaultInstance = nil;
    
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

- (id)init {
    self = [super init];
    
    if (nil == self) {
        return nil;
    }
    
    return self;
}

- (void)adjustAttributionChangedWannabe:(ADJAttribution *)attribution {
    if (attribution == nil) {
        return;
    }
    
    NSDictionary *attributionDictionary = [attribution dictionary];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:attributionDictionary];
    pluginResult.keepCallback = [NSNumber numberWithBool:YES];

    [_adjustCordovaCommandDelegate sendPluginResult:pluginResult callbackId:_attributionCallbackId];
}

- (void)addValueOrEmpty:(NSMutableDictionary *)dictionary
                    key:(NSString *)key
                  value:(NSObject *)value {
    if (nil != value) {
        [dictionary setObject:[NSString stringWithFormat:@"%@", value] forKey:key];
    } else {
        [dictionary setObject:@"" forKey:key];
    }
}

- (void)adjustEventTrackingSucceededWannabe:(ADJEventSuccess *)eventSuccessResponseData {
    if (nil == eventSuccessResponseData) {
        return;
    }

    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];

    [self addValueOrEmpty:dictionary key:@"message" value:eventSuccessResponseData.message];
    [self addValueOrEmpty:dictionary key:@"timestamp" value:eventSuccessResponseData.timeStamp];
    [self addValueOrEmpty:dictionary key:@"adid" value:eventSuccessResponseData.adid];
    [self addValueOrEmpty:dictionary key:@"eventToken" value:eventSuccessResponseData.eventToken];
    [self addValueOrEmpty:dictionary key:@"jsonResponse" value:eventSuccessResponseData.jsonResponse];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictionary];
    pluginResult.keepCallback = [NSNumber numberWithBool:YES];

    [_adjustCordovaCommandDelegate sendPluginResult:pluginResult callbackId:_eventSucceededCallbackId];
}

- (void)adjustEventTrackingFailedWannabe:(ADJEventFailure *)eventFailureResponseData {
    if (nil == eventFailureResponseData) {
        return;
    }

    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];

    [self addValueOrEmpty:dictionary key:@"message" value:eventFailureResponseData.message];
    [self addValueOrEmpty:dictionary key:@"timestamp" value:eventFailureResponseData.timeStamp];
    [self addValueOrEmpty:dictionary key:@"adid" value:eventFailureResponseData.adid];
    [self addValueOrEmpty:dictionary key:@"eventToken" value:eventFailureResponseData.eventToken];
    [dictionary setObject:(eventFailureResponseData.willRetry ? @"true" : @"false") forKey:@"willRetry"];
    [self addValueOrEmpty:dictionary key:@"jsonResponse" value:eventFailureResponseData.jsonResponse];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictionary];
    pluginResult.keepCallback = [NSNumber numberWithBool:YES];

    [_adjustCordovaCommandDelegate sendPluginResult:pluginResult callbackId:_eventFailedCallbackId];
}

- (void)adjustSessionTrackingSucceededWannabe:(ADJSessionSuccess *)sessionSuccessResponseData {
    if (nil == sessionSuccessResponseData) {
        return;
    }

    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];

    [self addValueOrEmpty:dictionary key:@"message" value:sessionSuccessResponseData.message];
    [self addValueOrEmpty:dictionary key:@"timestamp" value:sessionSuccessResponseData.timeStamp];
    [self addValueOrEmpty:dictionary key:@"adid" value:sessionSuccessResponseData.adid];
    [self addValueOrEmpty:dictionary key:@"jsonResponse" value:sessionSuccessResponseData.jsonResponse];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictionary];
    pluginResult.keepCallback = [NSNumber numberWithBool:YES];

    [_adjustCordovaCommandDelegate sendPluginResult:pluginResult callbackId:_sessionSucceededCallbackId];
}

- (void)adjustSessionTrackingFailedWananbe:(ADJSessionFailure *)sessionFailureResponseData {
    if (nil == sessionFailureResponseData) {
        return;
    }

    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];

    [self addValueOrEmpty:dictionary key:@"message" value:sessionFailureResponseData.message];
    [self addValueOrEmpty:dictionary key:@"timestamp" value:sessionFailureResponseData.timeStamp];
    [self addValueOrEmpty:dictionary key:@"adid" value:sessionFailureResponseData.adid];
    [dictionary setObject:(sessionFailureResponseData.willRetry ? @"true" : @"false") forKey:@"willRetry"];
    [self addValueOrEmpty:dictionary key:@"jsonResponse" value:sessionFailureResponseData.jsonResponse];

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

@end
