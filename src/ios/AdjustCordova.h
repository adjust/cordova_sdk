//
//  AdjustCordova.h
//  Adjust
//
//  Created by Pedro Filipe on 04/03/14.
//  Copyright (c) 2012-2014 adeven. All rights reserved.
//

#import <Cordova/CDV.h>
#import "Adjust.h"

@interface AdjustCordova : CDVPlugin<AdjustDelegate>

- (void)appDidLaunch:(CDVInvokedUrlCommand *)command;
- (void)trackEvent:(CDVInvokedUrlCommand *)command;
- (void)trackRevenue:(CDVInvokedUrlCommand *)command;
- (void)setFinishedTrackingCallback:(CDVInvokedUrlCommand *)command;
- (void)adjustFinishedTrackingWithResponse:(AIResponseData *)responseData;

- (void)onPause:(CDVInvokedUrlCommand *)command;
- (void)onResume:(CDVInvokedUrlCommand *)command;


@end
