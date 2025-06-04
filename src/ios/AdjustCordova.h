//
//  AdjustCordova.h
//  Adjust SDK
//
//  Created by Pedro Filipe (@nonelse) on 3rd April 2014.
//  Copyright (c) 2012-Present Adjust GmbH. All rights reserved.
//

#import <Cordova/CDV.h>
#if defined(__has_include) && __has_include(<Adjust/Adjust.h>)
#import <Adjust/Adjust.h>
#else
#import <AdjustSdk/AdjustSdk.h>
#endif

@interface AdjustCordova : CDVPlugin

// common
- (void)initSdk:(CDVInvokedUrlCommand *)command;
- (void)setAttributionCallback:(CDVInvokedUrlCommand *)command;
- (void)setEventTrackingSucceededCallback:(CDVInvokedUrlCommand *)command;
- (void)setEventTrackingFailedCallback:(CDVInvokedUrlCommand *)command;
- (void)setSessionTrackingSucceededCallback:(CDVInvokedUrlCommand *)command;
- (void)setSessionTrackingFailedCallback:(CDVInvokedUrlCommand *)command;
- (void)setDeferredDeeplinkCallback:(CDVInvokedUrlCommand *)command;
- (void)setPushToken:(CDVInvokedUrlCommand *)command;
- (void)getAttribution:(CDVInvokedUrlCommand *)command;
- (void)getAdid:(CDVInvokedUrlCommand *)command;
- (void)getSdkVersion:(CDVInvokedUrlCommand *)command;
- (void)addGlobalCallbackParameter:(CDVInvokedUrlCommand *)command;
- (void)removeGlobalCallbackParameter:(CDVInvokedUrlCommand *)command;
- (void)removeGlobalCallbackParameters:(CDVInvokedUrlCommand *)command;
- (void)addGlobalPartnerParameter:(CDVInvokedUrlCommand *)command;
- (void)removeGlobalPartnerParameter:(CDVInvokedUrlCommand *)command;
- (void)removeGlobalPartnerParameters:(CDVInvokedUrlCommand *)command;
- (void)switchToOfflineMode:(CDVInvokedUrlCommand *)command;
- (void)switchBackToOnlineMode:(CDVInvokedUrlCommand *)command;
- (void)enable:(CDVInvokedUrlCommand *)command;
- (void)disable:(CDVInvokedUrlCommand *)command;
- (void)isEnabled:(CDVInvokedUrlCommand *)command;
- (void)gdprForgetMe:(CDVInvokedUrlCommand *)command;
- (void)trackEvent:(CDVInvokedUrlCommand *)command;
- (void)trackAdRevenue:(CDVInvokedUrlCommand *)command;
- (void)trackThirdPartySharing:(CDVInvokedUrlCommand *)command;
- (void)trackMeasurementConsent:(CDVInvokedUrlCommand *)command;
- (void)processDeeplink:(CDVInvokedUrlCommand *)command;
- (void)processAndResolveDeeplink:(CDVInvokedUrlCommand *)command;
- (void)getLastDeeplink:(CDVInvokedUrlCommand *)command;
- (void)endFirstSessionDelay:(CDVInvokedUrlCommand *)command;
- (void)enableCoppaComplianceInDelay:(CDVInvokedUrlCommand *)command;
- (void)disableCoppaComplianceInDelay:(CDVInvokedUrlCommand *)command;
- (void)setExternalDeviceIdInDelay:(CDVInvokedUrlCommand *)command;
// ios only
- (void)setSkanUpdatedCallback:(CDVInvokedUrlCommand *)command;
- (void)getIdfa:(CDVInvokedUrlCommand *)command;
- (void)getIdfv:(CDVInvokedUrlCommand *)command;
- (void)requestAppTrackingAuthorization:(CDVInvokedUrlCommand *)command;
- (void)getAppTrackingAuthorizationStatus:(CDVInvokedUrlCommand *)command;
- (void)updateSkanConversionValue:(CDVInvokedUrlCommand *)command;
- (void)trackAppStoreSubscription:(CDVInvokedUrlCommand *)command;
- (void)verifyAppStorePurchase:(CDVInvokedUrlCommand *)command;
- (void)verifyAndTrackAppStorePurchase:(CDVInvokedUrlCommand *)command;
// android only
- (void)getGoogleAdId:(CDVInvokedUrlCommand *)command;
- (void)getAmazonAdId:(CDVInvokedUrlCommand *)command;
- (void)trackPlayStoreSubscription:(CDVInvokedUrlCommand *)command;
- (void)verifyPlayStorePurchase:(CDVInvokedUrlCommand *)command;
- (void)verifyAndTrackPlayStorePurchase:(CDVInvokedUrlCommand *)command;
// testing only
- (void)onPause:(CDVInvokedUrlCommand *)command;
- (void)onResume:(CDVInvokedUrlCommand *)command;
- (void)setTestOptions:(CDVInvokedUrlCommand *)command;
- (void)teardown:(CDVInvokedUrlCommand *)command;

@end
