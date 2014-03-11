//
//  AdjustCordova.h
//  HelloWorld
//
//  Created by Pedro Filipe on 04/03/14.
//
//

#import <Cordova/CDV.h>
#import "Adjust.h"

@interface AdjustCordova : CDVPlugin<AdjustDelegate>

- (void)appDidLaunch:(CDVInvokedUrlCommand *)command;
- (void)trackEvent:(CDVInvokedUrlCommand *)command;
- (void)trackRevenue:(CDVInvokedUrlCommand *)command;
- (void)setLogLevel:(CDVInvokedUrlCommand *)command;
- (void)setEnvironment:(CDVInvokedUrlCommand *)command;
- (void)setEventBufferingEnabled:(CDVInvokedUrlCommand *)command;
- (void)setMacMd5TrackingEnabled:(CDVInvokedUrlCommand *)command;
- (void)setFinishedTrackingCallback:(CDVInvokedUrlCommand *)command;
- (void)adjustFinishedTrackingWithResponse:(AIResponseData *)responseData;

- (void)onPause:(CDVInvokedUrlCommand *)command;
- (void)onResume:(CDVInvokedUrlCommand *)command;


@end
