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
            [defaultInstance swizzleAttributionCallbackMethod];
        }

        if (swizzleEventSucceededCallback) {
            [defaultInstance swizzleEventSucceededCallbackMethod];
        }

        if (swizzleEventFailedCallback) {
            [defaultInstance swizzleEventFailedCallbackMethod];
        }

        if (swizzleSessionSucceededCallback) {
            [defaultInstance swizzleSessionSucceededCallbackMethod];
        }

        if (swizzleSessionFailedCallback) {
            [defaultInstance swizzleSessionFailedCallbackMethod];
        }

        if (swizzleDeferredDeeplinkCallback) {
            [defaultInstance swizzleDeferredDeeplinkCallbackMethod];
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

- (void)adjustEventTrackingSucceededWannabe:(ADJEventSuccess *)eventSuccessResponseData {
    if (nil == eventSuccessResponseData) {
        return;
    }

    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];

    if (nil != eventSuccessResponseData.message) {
        [dictionary setObject:eventSuccessResponseData.message forKey:@"message"];
    } else {
        [dictionary setObject:@"" forKey:@"message"];
    }

    if (nil != eventSuccessResponseData.timeStamp) {
        [dictionary setObject:eventSuccessResponseData.timeStamp forKey:@"timestamp"];
    } else {
        [dictionary setObject:@"" forKey:@"timestamp"];
    }

    if (nil != eventSuccessResponseData.adid) {
        [dictionary setObject:eventSuccessResponseData.adid forKey:@"adid"];
    } else {
        [dictionary setObject:@"" forKey:@"adid"];
    }

    if (nil != eventSuccessResponseData.eventToken) {
        [dictionary setObject:eventSuccessResponseData.eventToken forKey:@"eventToken"];
    } else {
        [dictionary setObject:@"" forKey:@"eventToken"];
    }

    if (nil != eventSuccessResponseData.jsonResponse) {
        [dictionary setObject:[NSString stringWithFormat:@"%@", eventSuccessResponseData.jsonResponse] forKey:@"jsonResponse"];
    } else {
        [dictionary setObject:@"" forKey:@"jsonResponse"];
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

    if (nil != eventFailureResponseData.message) {
        [dictionary setObject:eventFailureResponseData.message forKey:@"message"];
    } else {
        [dictionary setObject:@"" forKey:@"message"];
    }

    if (nil != eventFailureResponseData.timeStamp) {
        [dictionary setObject:eventFailureResponseData.timeStamp forKey:@"timestamp"];
    } else {
        [dictionary setObject:@"" forKey:@"timestamp"];
    }

    if (nil != eventFailureResponseData.adid) {
        [dictionary setObject:eventFailureResponseData.adid forKey:@"adid"];
    } else {
        [dictionary setObject:@"" forKey:@"adid"];
    }

    if (nil != eventFailureResponseData.eventToken) {
        [dictionary setObject:eventFailureResponseData.eventToken forKey:@"eventToken"];
    } else {
        [dictionary setObject:@"" forKey:@"eventToken"];
    }

    [dictionary setObject:(eventFailureResponseData.willRetry ? @"true" : @"false") forKey:@"willRetry"];

    if (nil != eventFailureResponseData.jsonResponse) {
        [dictionary setObject:[NSString stringWithFormat:@"%@", eventFailureResponseData.jsonResponse] forKey:@"jsonResponse"];
    } else {
        [dictionary setObject:@"" forKey:@"jsonResponse"];
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

    if (nil != sessionSuccessResponseData.message) {
        [dictionary setObject:sessionSuccessResponseData.message forKey:@"message"];
    } else {
        [dictionary setObject:@"" forKey:@"message"];
    }

    if (nil != sessionSuccessResponseData.timeStamp) {
        [dictionary setObject:sessionSuccessResponseData.timeStamp forKey:@"timestamp"];
    } else {
        [dictionary setObject:@"" forKey:@"timestamp"];
    }

    if (nil != sessionSuccessResponseData.adid) {
        [dictionary setObject:sessionSuccessResponseData.adid forKey:@"adid"];
    } else {
        [dictionary setObject:@"" forKey:@"adid"];
    }

    if (nil != sessionSuccessResponseData.jsonResponse) {
        [dictionary setObject:[NSString stringWithFormat:@"%@", sessionSuccessResponseData.jsonResponse] forKey:@"jsonResponse"];
    } else {
        [dictionary setObject:@"" forKey:@"jsonResponse"];
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

    if (nil != sessionFailureResponseData.message) {
        [dictionary setObject:sessionFailureResponseData.message forKey:@"message"];
    } else {
        [dictionary setObject:@"" forKey:@"message"];
    }

    if (nil != sessionFailureResponseData.timeStamp) {
        [dictionary setObject:sessionFailureResponseData.timeStamp forKey:@"timestamp"];
    } else {
        [dictionary setObject:@"" forKey:@"timestamp"];
    }

    if (nil != sessionFailureResponseData.adid) {
        [dictionary setObject:sessionFailureResponseData.adid forKey:@"adid"];
    } else {
        [dictionary setObject:@"" forKey:@"adid"];
    }

    [dictionary setObject:(sessionFailureResponseData.willRetry ? @"true" : @"false") forKey:@"willRetry"];

    if (nil != sessionFailureResponseData.jsonResponse) {
        [dictionary setObject:[NSString stringWithFormat:@"%@", sessionFailureResponseData.jsonResponse] forKey:@"jsonResponse"];
    } else {
        [dictionary setObject:@"" forKey:@"jsonResponse"];
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

- (void)swizzleAttributionCallbackMethod {
    Class class = [self class];

    SEL originalSelector = @selector(adjustAttributionChanged:);
    SEL swizzledSelector = @selector(adjustAttributionChangedWannabe:);

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

- (void)swizzleEventSucceededCallbackMethod {
    Class class = [self class];

    SEL originalSelector = @selector(adjustEventTrackingSucceeded:);
    SEL swizzledSelector = @selector(adjustEventTrackingSucceededWannabe:);

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

- (void)swizzleEventFailedCallbackMethod {
    Class class = [self class];

    SEL originalSelector = @selector(adjustEventTrackingFailed:);
    SEL swizzledSelector = @selector(adjustEventTrackingFailedWannabe:);

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

- (void)swizzleSessionSucceededCallbackMethod {
    Class class = [self class];

    SEL originalSelector = @selector(adjustSessionTrackingSucceeded:);
    SEL swizzledSelector = @selector(adjustSessionTrackingSucceededWannabe:);

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

- (void)swizzleSessionFailedCallbackMethod {
    Class class = [self class];

    SEL originalSelector = @selector(adjustSessionTrackingFailed:);
    SEL swizzledSelector = @selector(adjustSessionTrackingFailedWananbe:);

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

- (void)swizzleDeferredDeeplinkCallbackMethod {
    Class class = [self class];

    SEL originalSelector = @selector(adjustDeeplinkResponse:);
    SEL swizzledSelector = @selector(adjustDeeplinkResponseWannabe:);

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
