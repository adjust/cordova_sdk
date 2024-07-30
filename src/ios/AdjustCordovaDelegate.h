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
@property (nonatomic, copy) NSString *attributionChangedCallbackId;
@property (nonatomic, copy) NSString *eventSucceededCallbackId;
@property (nonatomic, copy) NSString *eventFailedCallbackId;
@property (nonatomic, copy) NSString *sessionSucceededCallbackId;
@property (nonatomic, copy) NSString *sessionFailedCallbackId;
@property (nonatomic, copy) NSString *deferredDeeplinkReceivedCallbackId;
@property (nonatomic, copy) NSString *skanConversionDataUpdatedCallbackId;
@property (nonatomic) id<CDVCommandDelegate> adjustCordovaCommandDelegate;

+ (id)getInstanceWithSwizzledAttributionCallbackId:(NSString *)attributionChangedCallbackId
                          eventSucceededCallbackId:(NSString *)eventSucceededCallbackId
                             eventFailedCallbackId:(NSString *)eventFailedCallbackId
                        sessionSucceededCallbackId:(NSString *)sessionSucceededCallbackId
                           sessionFailedCallbackId:(NSString *)sessionFailedCallbackId
                deferredDeeplinkReceivedCallbackId:(NSString *)deferredDeeplinkReceivedCallbackId
               skanConversionDataUpdatedCallbackId:(NSString *)skanConversionDataUpdatedCallbackId
                      shouldLaunchDeferredDeeplink:(BOOL)shouldLaunchDeferredDeeplink
                               withCommandDelegate:(id<CDVCommandDelegate>)adjustCordovaCommandDelegate;
+ (void)teardown;

@end
