//
//  AdjustCordovaDelegate.h
//  Adjust
//
//  Created by uerceg on 11/16/16.
//  Copyright (c) 2012-2016 adjust GmbH. All rights reserved.
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
@property (nonatomic) id<CDVCommandDelegate> adjustCordovaCommandDelegate;

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
					          withCommandDelegate:(id<CDVCommandDelegate>)adjustCordovaCommandDelegate;

@end
