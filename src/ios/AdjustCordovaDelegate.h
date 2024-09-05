//
//  AdjustCordovaDelegate.h
//  Adjust SDK
//
//  Created by Uglješa Erceg (@uerceg) on 16th November 2016.
//  Copyright (c) 2016-2021 Adjust GmbH. All rights reserved.
//

#import <Cordova/CDV.h>
#if defined(__has_include) && __has_include(<Adjust/Adjust.h>)
#import <Adjust/Adjust.h>
#else
//#import "Adjust.h"
#import <AdjustSdk/AdjustSdk.h>
#endif

@interface AdjustCordovaDelegate : NSObject<AdjustDelegate>

@property (nonatomic) BOOL shouldLaunchDeferredDeeplink;
@property (nonatomic, copy) NSString *attributionCallbackId;
@property (nonatomic, copy) NSString *eventTrackingSucceededCallbackId;
@property (nonatomic, copy) NSString *eventTrackingFailedCallbackId;
@property (nonatomic, copy) NSString *sessionTrackingSucceededCallbackId;
@property (nonatomic, copy) NSString *sessionTrackingFailedCallbackId;
@property (nonatomic, copy) NSString *deferredDeeplinkCallbackId;
@property (nonatomic, copy) NSString *skanUpdatedCallbackId;
@property (nonatomic) id<CDVCommandDelegate> adjustCordovaCommandDelegate;

+ (id)getInstanceWithSwizzledAttributionCallbackId:(NSString *)attributionCallbackId
                  eventTrackingSucceededCallbackId:(NSString *)eventTrackingSucceededCallbackId
                     eventTrackingFailedCallbackId:(NSString *)eventTrackingFailedCallbackId
                sessionTrackingSucceededCallbackId:(NSString *)sessionTrackingSucceededCallbackId
                   sessionTrackingFailedCallbackId:(NSString *)sessionTrackingFailedCallbackId
                        deferredDeeplinkCallbackId:(NSString *)deferredDeeplinkCallbackId
                             skanUpdatedCallbackId:(NSString *)skanUpdatedCallbackId
                      shouldLaunchDeferredDeeplink:(BOOL)shouldLaunchDeferredDeeplink
                               withCommandDelegate:(id<CDVCommandDelegate>)adjustCordovaCommandDelegate;
+ (void)teardown;

@end
