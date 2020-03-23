//
//  AdjustCordova.h
//  Adjust SDK
//
//  Created by Pedro Filipe (@nonelse) on 3rd April 2014.
//  Copyright (c) 2012-2018 Adjust GmbH. All rights reserved.
//

#import <Cordova/CDV.h>
#import <AdjustSdk/Adjust.h>

@interface AdjustCordova : CDVPlugin

- (void)create:(CDVInvokedUrlCommand *)command;
- (void)isEnabled:(CDVInvokedUrlCommand *)command;
- (void)setEnabled:(CDVInvokedUrlCommand *)command;
- (void)trackEvent:(CDVInvokedUrlCommand *)command;
- (void)setOfflineMode:(CDVInvokedUrlCommand *)command;
- (void)appWillOpenUrl:(CDVInvokedUrlCommand *)command;
- (void)getIdfa:(CDVInvokedUrlCommand *)command;
- (void)getAdid:(CDVInvokedUrlCommand *)command;
- (void)getAttribution:(CDVInvokedUrlCommand *)command;
- (void)getSdkVersion:(CDVInvokedUrlCommand *)command;
- (void)setPushToken:(CDVInvokedUrlCommand *)command;
- (void)sendFirstPackages:(CDVInvokedUrlCommand *)command;
- (void)gdprForgetMe:(CDVInvokedUrlCommand *)command;
- (void)disableThirdPartySharing:(CDVInvokedUrlCommand *)command;
- (void)trackAdRevenue:(CDVInvokedUrlCommand *)command;
- (void)setAttributionCallback:(CDVInvokedUrlCommand *)command;
- (void)setEventTrackingSucceededCallback:(CDVInvokedUrlCommand *)command;
- (void)setEventTrackingFailedCallback:(CDVInvokedUrlCommand *)command;
- (void)setSessionTrackingSucceededCallback:(CDVInvokedUrlCommand *)command;
- (void)setSessionTrackingFailedCallback:(CDVInvokedUrlCommand *)command;
- (void)setDeferredDeeplinkCallback:(CDVInvokedUrlCommand *)command;
- (void)addSessionPartnerParameter:(CDVInvokedUrlCommand *)command;
- (void)addSessionCallbackParameter:(CDVInvokedUrlCommand *)command;
- (void)removeSessionPartnerParameter:(CDVInvokedUrlCommand *)command;
- (void)removeSessionCallbackParameter:(CDVInvokedUrlCommand *)command;
- (void)resetSessionPartnerParameters:(CDVInvokedUrlCommand *)command;
- (void)resetSessionCallbackParameters:(CDVInvokedUrlCommand *)command;

- (void)onPause:(CDVInvokedUrlCommand *)command;
- (void)onResume:(CDVInvokedUrlCommand *)command;
- (void)setReferrer:(CDVInvokedUrlCommand *)command;
- (void)getGoogleAdId:(CDVInvokedUrlCommand *)command;
- (void)getAmazonAdId:(CDVInvokedUrlCommand *)command;

- (void)setTestOptions:(CDVInvokedUrlCommand *)command;
- (void)teardown:(CDVInvokedUrlCommand *)command;

@end
