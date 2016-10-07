//
//  AdjustCordova.h
//  Adjust
//
//  Created by Pedro Filipe on 04/03/14.
//  Copyright (c) 2012-2014 adjust GmbH. All rights reserved.
//

#import <Cordova/CDV.h>
#import <AdjustSdk/Adjust.h>

@interface AdjustCordova : CDVPlugin<AdjustDelegate>

- (void)create:(CDVInvokedUrlCommand *)command;
- (void)onPause:(CDVInvokedUrlCommand *)command;
- (void)onResume:(CDVInvokedUrlCommand *)command;
- (void)isEnabled:(CDVInvokedUrlCommand *)command;
- (void)setEnabled:(CDVInvokedUrlCommand *)command;
- (void)trackEvent:(CDVInvokedUrlCommand *)command;
- (void)setOfflineMode:(CDVInvokedUrlCommand *)command;
- (void)appWillOpenUrl:(CDVInvokedUrlCommand *)command;
- (void)getIdfa:(CDVInvokedUrlCommand *)command;
- (void)getGoogleAdId:(CDVInvokedUrlCommand *)command;
- (void)sendFirstPackages:(CDVInvokedUrlCommand *)command;

- (void)setAttributionCallback:(CDVInvokedUrlCommand *)command;
- (void)setEventTrackingSuccessfulCallback:(CDVInvokedUrlCommand *)command;
- (void)setEventTrackingFailureCallback:(CDVInvokedUrlCommand *)command;
- (void)setSessionTrackingSuccessfulCallback:(CDVInvokedUrlCommand *)command;
- (void)setSessionTrackingFailureCallback:(CDVInvokedUrlCommand *)command;
- (void)setDeeplinkCallback:(CDVInvokedUrlCommand *)command;

- (void)addSessionCallbackParameter:(CDVInvokedUrlCommand *)command;
- (void)RemoveSessionCallbackParameter:(CDVInvokedUrlCommand *)command;
- (void)ResetSessionCallbackParameters:(CDVInvokedUrlCommand *)command;
- (void)addSessionPartnerParameter:(CDVInvokedUrlCommand *)command;
- (void)RemoveSessionPartnerParameter:(CDVInvokedUrlCommand *)command;
- (void)ResetSessionPartnerParameters:(CDVInvokedUrlCommand *)command;

@end
