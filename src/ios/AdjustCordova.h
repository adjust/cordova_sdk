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
//#import "Adjust.h"
#import <AdjustSdk/AdjustSdk.h>
#endif

@interface AdjustCordova : CDVPlugin

// Initialization
- (void)create:(CDVInvokedUrlCommand *)command;

// Adjust API Callbacks setters
- (void)setAttributionChangedCallback:(CDVInvokedUrlCommand *)command;
- (void)setEventTrackingSucceededCallback:(CDVInvokedUrlCommand *)command;
- (void)setEventTrackingFailedCallback:(CDVInvokedUrlCommand *)command;
- (void)setSessionTrackingSucceededCallback:(CDVInvokedUrlCommand *)command;
- (void)setSessionTrackingFailedCallback:(CDVInvokedUrlCommand *)command;
- (void)setDeferredDeeplinkReceivedCallback:(CDVInvokedUrlCommand *)command;
- (void)setSkanConversionDataUpdatedCallback:(CDVInvokedUrlCommand *)command;

// Adjust API
- (void)trackEvent:(CDVInvokedUrlCommand *)command;
- (void)switchToOfflineMode:(CDVInvokedUrlCommand *)command;
- (void)switchBackToOnlineMode:(CDVInvokedUrlCommand *)command;
- (void)setPushTokenAsString:(CDVInvokedUrlCommand *)command;
- (void)processDeeplink:(CDVInvokedUrlCommand *)command;
- (void)processAndResolveDeeplink:(CDVInvokedUrlCommand *)command;
- (void)getIdfa:(CDVInvokedUrlCommand *)command;
- (void)getIdfv:(CDVInvokedUrlCommand *)command;
- (void)getAdid:(CDVInvokedUrlCommand *)command;
- (void)getAttribution:(CDVInvokedUrlCommand *)command;
- (void)getSdkVersion:(CDVInvokedUrlCommand *)command;
- (void)enable:(CDVInvokedUrlCommand *)command;
- (void)disable:(CDVInvokedUrlCommand *)command;
- (void)isEnabled:(CDVInvokedUrlCommand *)command;
- (void)gdprForgetMe:(CDVInvokedUrlCommand *)command;
- (void)enableCoppaCompliance:(CDVInvokedUrlCommand *)command;
- (void)disableCoppaCompliance:(CDVInvokedUrlCommand *)command;
// TODO: Remove the usage of trackAdRevenue with 2 arguments
- (void)trackAdRevenue:(CDVInvokedUrlCommand *)command;
// TODO: remove receipt from the usage of trackAppStoreSubscription
- (void)trackAppStoreSubscription:(CDVInvokedUrlCommand *)command;
- (void)addGlobalCallbackParameter:(CDVInvokedUrlCommand *)command;
- (void)removeGlobalCallbackParameter:(CDVInvokedUrlCommand *)command;
- (void)removeGlobalCallbackParameters:(CDVInvokedUrlCommand *)command;
- (void)addGlobalPartnerParameter:(CDVInvokedUrlCommand *)command;
- (void)removeGlobalPartnerParameter:(CDVInvokedUrlCommand *)command;
- (void)removeGlobalPartnerParameters:(CDVInvokedUrlCommand *)command;
- (void)requestAppTrackingAuthorization:(CDVInvokedUrlCommand *)command;
- (void)getAppTrackingAuthorizationStatus:(CDVInvokedUrlCommand *)command;
- (void)updateSkanConversionValueWithErrorCallback:(CDVInvokedUrlCommand *)command;
- (void)trackThirdPartySharing:(CDVInvokedUrlCommand *)command;
- (void)trackMeasurementConsent:(CDVInvokedUrlCommand *)command;
- (void)getLastDeeplink:(CDVInvokedUrlCommand *)command;
- (void)verifyAppStorePurchase:(CDVInvokedUrlCommand *)command;
- (void)verifyAndTrackAppStorePurchase:(CDVInvokedUrlCommand *)command;
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
