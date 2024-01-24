//
//  AdjustCordova.h
//  Adjust SDK
//
//  Created by Pedro Filipe (@nonelse) on 3rd April 2014.
//  Copyright (c) 2012-2018 Adjust GmbH. All rights reserved.
//

#import <Cordova/CDV.h>
#if defined(__has_include) && __has_include(<Adjust/Adjust.h>)
#import <Adjust/Adjust.h>
#else
#import "Adjust.h"
#endif

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
- (void)setConversionValueUpdatedCallback:(CDVInvokedUrlCommand *)command;
- (void)setSkad4ConversionValueUpdatedCallback:(CDVInvokedUrlCommand *)command;
- (void)addSessionPartnerParameter:(CDVInvokedUrlCommand *)command;
- (void)addSessionCallbackParameter:(CDVInvokedUrlCommand *)command;
- (void)removeSessionPartnerParameter:(CDVInvokedUrlCommand *)command;
- (void)removeSessionCallbackParameter:(CDVInvokedUrlCommand *)command;
- (void)resetSessionPartnerParameters:(CDVInvokedUrlCommand *)command;
- (void)resetSessionCallbackParameters:(CDVInvokedUrlCommand *)command;
- (void)trackAppStoreSubscription:(CDVInvokedUrlCommand *)command;
- (void)requestTrackingAuthorizationWithCompletionHandler:(CDVInvokedUrlCommand *)command;
- (void)updateConversionValue:(CDVInvokedUrlCommand *)command;
- (void)updateConversionValueWithErrorCallback:(CDVInvokedUrlCommand *)command;
- (void)updateSkad4ConversionValueWithErrorCallback:(CDVInvokedUrlCommand *)command;
- (void)getAppTrackingAuthorizationStatus:(CDVInvokedUrlCommand *)command;
- (void)trackThirdPartySharing:(CDVInvokedUrlCommand *)command;
- (void)trackMeasurementConsent:(CDVInvokedUrlCommand *)command;
- (void)getLastDeeplink:(CDVInvokedUrlCommand *)command;
- (void)verifyAppStorePurchase:(CDVInvokedUrlCommand *)command;
- (void)getIdfv:(CDVInvokedUrlCommand *)command;

- (void)onPause:(CDVInvokedUrlCommand *)command;
- (void)onResume:(CDVInvokedUrlCommand *)command;
- (void)setReferrer:(CDVInvokedUrlCommand *)command;
- (void)getGoogleAdId:(CDVInvokedUrlCommand *)command;
- (void)getAmazonAdId:(CDVInvokedUrlCommand *)command;
- (void)trackPlayStoreSubscription:(CDVInvokedUrlCommand *)command;
- (void)verifyPlayStorePurchase:(CDVInvokedUrlCommand *)command;

- (void)setTestOptions:(CDVInvokedUrlCommand *)command;
- (void)teardown:(CDVInvokedUrlCommand *)command;

@end
