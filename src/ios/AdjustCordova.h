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
// Setters
- (void)setPushToken:(CDVInvokedUrlCommand *)command;
// Getters
- (void)getAttribution:(CDVInvokedUrlCommand *)command;
- (void)getAdid:(CDVInvokedUrlCommand *)command;
- (void)getIdfa:(CDVInvokedUrlCommand *)command;
- (void)getIdfv:(CDVInvokedUrlCommand *)command;
- (void)getSdkVersion:(CDVInvokedUrlCommand *)command;
// Global Parameters manipulation commands
// Callback
- (void)addGlobalCallbackParameter:(CDVInvokedUrlCommand *)command;
- (void)removeGlobalCallbackParameter:(CDVInvokedUrlCommand *)command;
- (void)removeGlobalCallbackParameters:(CDVInvokedUrlCommand *)command;
// Partner
- (void)addGlobalPartnerParameter:(CDVInvokedUrlCommand *)command;
- (void)removeGlobalPartnerParameter:(CDVInvokedUrlCommand *)command;
- (void)removeGlobalPartnerParameters:(CDVInvokedUrlCommand *)command;
// SDK State
- (void)switchToOfflineMode:(CDVInvokedUrlCommand *)command;
- (void)switchBackToOnlineMode:(CDVInvokedUrlCommand *)command;
- (void)enable:(CDVInvokedUrlCommand *)command;
- (void)disable:(CDVInvokedUrlCommand *)command;
- (void)isEnabled:(CDVInvokedUrlCommand *)command;
- (void)gdprForgetMe:(CDVInvokedUrlCommand *)command;
// SDK Lifecycle
- (void)onPause:(CDVInvokedUrlCommand *)command;
- (void)onResume:(CDVInvokedUrlCommand *)command;
// Tracking
- (void)trackEvent:(CDVInvokedUrlCommand *)command;
- (void)trackAdRevenue:(CDVInvokedUrlCommand *)command;
- (void)trackAppStoreSubscription:(CDVInvokedUrlCommand *)command;
- (void)verifyAppStorePurchase:(CDVInvokedUrlCommand *)command;
- (void)verifyAndTrackAppStorePurchase:(CDVInvokedUrlCommand *)command;
- (void)trackThirdPartySharing:(CDVInvokedUrlCommand *)command;
- (void)trackMeasurementConsent:(CDVInvokedUrlCommand *)command;
// Deeplink
- (void)processDeeplink:(CDVInvokedUrlCommand *)command;
- (void)processAndResolveDeeplink:(CDVInvokedUrlCommand *)command;
- (void)getLastDeeplink:(CDVInvokedUrlCommand *)command;
// App Tracking Authorization
- (void)requestAppTrackingAuthorization:(CDVInvokedUrlCommand *)command;
- (void)getAppTrackingAuthorizationStatus:(CDVInvokedUrlCommand *)command;
// SKAN
- (void)updateSkanConversionValueWithErrorCallback:(CDVInvokedUrlCommand *)command;
// Android Only
- (void)setReferrer:(CDVInvokedUrlCommand *)command;
- (void)getGoogleAdId:(CDVInvokedUrlCommand *)command;
- (void)getAmazonAdId:(CDVInvokedUrlCommand *)command;
- (void)getGooglePlayInstallReferrer:(CDVInvokedUrlCommand *)command;
- (void)trackPlayStoreSubscription:(CDVInvokedUrlCommand *)command;
- (void)verifyPlayStorePurchase:(CDVInvokedUrlCommand *)command;
- (void)verifyAndTrackPlayStorePurchase:(CDVInvokedUrlCommand *)command;
// Testing
- (void)setTestOptions:(CDVInvokedUrlCommand *)command;
- (void)teardown:(CDVInvokedUrlCommand *)command;

@end
