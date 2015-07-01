//
//  AdjustCordova.h
//  Adjust
//
//  Created by Pedro Filipe on 04/03/14.
//  Copyright (c) 2012-2014 adjust GmbH. All rights reserved.
//

#import <Cordova/CDV.h>
#import <Adjust/Adjust.h>

@interface AdjustCordova : CDVPlugin<AdjustDelegate>

- (void)create:(CDVInvokedUrlCommand *)command;
- (void)onPause:(CDVInvokedUrlCommand *)command;
- (void)onResume:(CDVInvokedUrlCommand *)command;
- (void)isEnabled:(CDVInvokedUrlCommand *)command;
- (void)setEnabled:(CDVInvokedUrlCommand *)command;
- (void)trackEvent:(CDVInvokedUrlCommand *)command;
- (void)setOfflineMode:(CDVInvokedUrlCommand *)command;
- (void)setAttributionCallback:(CDVInvokedUrlCommand *)command;

@end
