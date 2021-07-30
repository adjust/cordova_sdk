//
//  AdjustCordovaDelegate.h
//  Adjust SDK
//
//  Created by Uglješa Erceg (@uerceg) on 16th November 2016.
//  Copyright (c) 2016-2021 Adjust GmbH. All rights reserved.
//

#import <Cordova/CDV.h>
#import <AdjustSdk/Adjust.h>

@interface AdjustCordovaDelegate : NSObject<AdjustDelegate>

@property (nonatomic) BOOL shouldLaunchDeferredDeeplink;
@property (nonatomic, copy) NSString *attributionCallbackId;
@property (nonatomic, copy) NSString *eventSucceededCallbackId;
@property (nonatomic, copy) NSString *eventFailedCallbackId;
@property (nonatomic, copy) NSString *sessionSucceededCallbackId;
@property (nonatomic, copy) NSString *sessionFailedCallbackId;
@property (nonatomic, copy) NSString *deferredDeeplinkCallbackId;
@property (nonatomic, copy) NSString *conversionValueUpdatedCallbackId;
@property (nonatomic) id<CDVCommandDelegate> adjustCordovaCommandDelegate;

+ (id)getInstanceWithSwizzleOfAttributionCallback:(BOOL)swizzleAttributionCallback
						   eventSucceededCallback:(BOOL)swizzleEventSucceededCallback
							  eventFailedCallback:(BOOL)swizzleEventFailedCallback
						 sessionSucceededCallback:(BOOL)swizzleSessionSucceededCallback
						    sessionFailedCallback:(BOOL)swizzleSessionFailedCallback
					     deferredDeeplinkCallback:(BOOL)swizzleDeferredDeeplinkCallback
				   conversionValueUpdatedCallback:(BOOL)swizzleConversionValueUpdatedCallback
						 andAttributionCallbackId:(NSString *)attributionCallbackId
						 eventSucceededCallbackId:(NSString *)eventSucceededCallbackId
						    eventFailedCallbackId:(NSString *)eventFailedCallbackId
					   sessionSucceededCallbackId:(NSString *)sessionSucceededCallbackId
						  sessionFailedCallbackId:(NSString *)sessionFailedCallbackId
					   deferredDeeplinkCallbackId:(NSString *)deferredDeeplinkCallbackId
				 conversionValueUpdatedCallbackId:(NSString *)conversionValueUpdatedCallbackId
					 shouldLaunchDeferredDeeplink:(BOOL)shouldLaunchDeferredDeeplink
					          withCommandDelegate:(id<CDVCommandDelegate>)adjustCordovaCommandDelegate;

+ (void)teardown;

@end
