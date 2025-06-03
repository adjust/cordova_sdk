//
//  AdjustCordova.m
//  Adjust SDK
//
//  Created by Pedro Filipe (@nonelse) on 3rd April 2014.
//  Copyright (c) 2012-Present Adjust GmbH. All rights reserved.
//

#import <Cordova/CDVPluginResult.h>

#import "AdjustCordova.h"
#import "AdjustCordovaDelegate.h"

#define KEY_APP_TOKEN @"appToken"
#define KEY_ENVIRONMENT @"environment"
#define KEY_LOG_LEVEL @"logLevel"
#define KEY_SDK_PREFIX @"sdkPrefix"
#define KEY_EVENT_TOKEN @"eventToken"
#define KEY_REVENUE @"revenue"
#define KEY_CURRENCY @"currency"
#define KEY_DEFAULT_TRACKER @"defaultTracker"
#define KEY_EXTERNAL_DEVICE_ID @"externalDeviceId"
#define KEY_TRANSACTION_ID @"transactionId"
#define KEY_CALLBACK_ID @"callbackId"
#define KEY_DEDUPLICATION_ID @"deduplicationId"
#define KEY_CALLBACK_PARAMETERS @"callbackParameters"
#define KEY_PARTNER_PARAMETERS @"partnerParameters"
#define KEY_IS_ENABLED @"isEnabled"
#define KEY_GRANULAR_OPTIONS @"granularOptions"
#define KEY_PARTNER_SHARING_SETTINGS @"partnerSharingSettings"
#define KEY_IS_DEFERRED_DEEP_LINK_OPENING_ENABLED @"isDeferredDeeplinkOpeningEnabled"
#define KEY_IS_SENDING_IN_BACKGROUND_ENABLED @"isSendingInBackgroundEnabled"
#define KEY_IS_COST_DATA_IN_ATTRIBUTION_ENABLED @"isCostDataInAttributionEnabled"
#define KEY_IS_AD_SERVICES_ENABLED @"isAdServicesEnabled"
#define KEY_IS_IDFA_READING_ENABLED @"isIdfaReadingEnabled"
#define KEY_IS_IDFV_READING_ENABLED @"isIdfvReadingEnabled"
#define KEY_IS_SKAN_ATTRIBUTION_ENABLED @"isSkanAttributionEnabled"
#define KEY_IS_LINK_ME_ENABLED @"isLinkMeEnabled"
#define KEY_IS_COPPA_COMPLIANCE_ENABLED @"isCoppaComplianceEnabled"
#define KEY_TEST_URL_OVERWRITE @"testUrlOverwrite"
#define KEY_EXTRA_PATH @"extraPath"
#define KEY_USE_TEST_CONNECTION_OPTIONS @"useTestConnectionOptions"
#define KEY_TIMER_INTERVAL @"timerIntervalInMilliseconds"
#define KEY_TIMER_START @"timerStartInMilliseconds"
#define KEY_SESSION_INTERVAL @"sessionIntervalInMilliseconds"
#define KEY_SUBSESSION_INTERVAL @"subsessionIntervalInMilliseconds"
#define KEY_TEARDOWN @"teardown"
#define KEY_NO_BACKOFF_WAIT @"noBackoffWait"
#define KEY_ATT_STATUS @"attStatus"
#define KEY_IDFA @"idfa"
#define KEY_DELETE_STATE @"deleteState"
#define KEY_ADSERVICES_ENABLED @"adServicesFrameworkEnabled"
#define KEY_PRICE @"price"
#define KEY_TRANSACTION_DATE @"transactionDate"
#define KEY_SALES_REGION @"salesRegion"
#define KEY_SOURCE @"source"
#define KEY_AD_IMPRESSIONS_COUNT @"adImpressionsCount"
#define KEY_AD_REVENUE_NETWORK @"adRevenueNetwork"
#define KEY_AD_REVENUE_UNIT @"adRevenueUnit"
#define KEY_AD_REVENUE_PLACEMENT @"adRevenuePlacement"
#define KEY_ATT_CONSENT_WAITING_INTERVAL @"attConsentWaitingInterval"
#define KEY_PRODUCT_ID @"productId"
#define KEY_IS_DEVICE_IDS_READING_ONCE_ENABLED @"isDeviceIdsReadingOnceEnabled"
#define KEY_URL_STRATEGY_DOMAINS @"urlStrategyDomains"
#define KEY_USE_SUBDOMAINS @"useSubdomains"
#define KEY_IS_DATA_RESIDENCY @"isDataResidency"
#define KEY_EVENT_DEDUPLICATION_IDS_MAX_SIZE @"eventDeduplicationIdsMaxSize"
#define KEY_DEEPLINK @"deeplink"
#define KEY_IS_ATT_USAGE_ENABLED @"isAppTrackingTransparencyUsageEnabled"
#define KEY_REFERRER @"referrer"
#define KEY_IS_FIRST_SESSION_DELAY_ENABLED @"isFirstSessionDelayEnabled"
#define KEY_STORE_INFO @"storeInfo"
#define KEY_STORE_NAME @"storeName"
#define KEY_STORE_APP_ID @"storeAppId"

@implementation AdjustCordova {
    NSString *attributionCallbackId;
    NSString *eventTrackingFailedCallbackId;
    NSString *eventTrackingSucceededCallbackId;
    NSString *sessionTrackingFailedCallbackId;
    NSString *sessionTrackingSucceededCallbackId;
    NSString *deferredDeeplinkCallbackId;
    NSString *skanUpdatedCallbackId;
}

- (void)pluginInitialize {
    attributionCallbackId = nil;
    eventTrackingFailedCallbackId = nil;
    eventTrackingSucceededCallbackId = nil;
    sessionTrackingFailedCallbackId = nil;
    sessionTrackingSucceededCallbackId = nil;
    deferredDeeplinkCallbackId = nil;
    skanUpdatedCallbackId = nil;
}

- (void)initSdk:(CDVInvokedUrlCommand *)command {
    NSString *arguments = [command.arguments objectAtIndex:0];
    NSArray *jsonObject = [NSJSONSerialization JSONObjectWithData:[arguments dataUsingEncoding:NSUTF8StringEncoding]
                                                          options:0
                                                            error:NULL];
    NSString *appToken = [[jsonObject valueForKey:KEY_APP_TOKEN] objectAtIndex:0];
    NSString *environment = [[jsonObject valueForKey:KEY_ENVIRONMENT] objectAtIndex:0];
    NSString *logLevel = [[jsonObject valueForKey:KEY_LOG_LEVEL] objectAtIndex:0];
    NSString *defaultTracker = [[jsonObject valueForKey:KEY_DEFAULT_TRACKER] objectAtIndex:0];
    NSString *externalDeviceId = [[jsonObject valueForKey:KEY_EXTERNAL_DEVICE_ID] objectAtIndex:0];
    NSNumber *eventDeduplicationIdsMaxSize = [[jsonObject valueForKey:KEY_EVENT_DEDUPLICATION_IDS_MAX_SIZE] objectAtIndex:0];
    NSNumber *isAdServicesEnabled = [[jsonObject valueForKey:KEY_IS_AD_SERVICES_ENABLED] objectAtIndex:0];
    NSNumber *isIdfaReadingEnabled = [[jsonObject valueForKey:KEY_IS_IDFA_READING_ENABLED] objectAtIndex:0];
    NSNumber *isIdfvReadingEnabled = [[jsonObject valueForKey:KEY_IS_IDFV_READING_ENABLED] objectAtIndex:0];
    NSNumber *isSkanAttributionEnabled = [[jsonObject valueForKey:KEY_IS_SKAN_ATTRIBUTION_ENABLED] objectAtIndex:0];
    NSNumber *isLinkMeEnabled = [[jsonObject valueForKey:KEY_IS_LINK_ME_ENABLED] objectAtIndex:0];
    NSNumber *isCoppaComplianceEnabled = [[jsonObject valueForKey:KEY_IS_COPPA_COMPLIANCE_ENABLED] objectAtIndex:0];
    NSNumber *isDeviceIdsReadingOnceEnabled = [[jsonObject valueForKey:KEY_IS_DEVICE_IDS_READING_ONCE_ENABLED] objectAtIndex:0];
    NSNumber *isSendingInBackgroundEnabled = [[jsonObject valueForKey:KEY_IS_SENDING_IN_BACKGROUND_ENABLED] objectAtIndex:0];
    NSNumber *isDeferredDeeplinkOpeningEnabled = [[jsonObject valueForKey:KEY_IS_DEFERRED_DEEP_LINK_OPENING_ENABLED] objectAtIndex:0];
    NSNumber *isCostDataInAttributionEnabled = [[jsonObject valueForKey:KEY_IS_COST_DATA_IN_ATTRIBUTION_ENABLED] objectAtIndex:0];
    NSNumber *attConsentWaitingInterval = [[jsonObject valueForKey:KEY_ATT_CONSENT_WAITING_INTERVAL] objectAtIndex:0];
    NSString *sdkPrefix = [[jsonObject valueForKey:KEY_SDK_PREFIX] objectAtIndex:0];
    id urlStrategyDomains = [[jsonObject valueForKey:KEY_URL_STRATEGY_DOMAINS] objectAtIndex:0];
    NSNumber *useSubdomains = [[jsonObject valueForKey:KEY_USE_SUBDOMAINS] objectAtIndex:0];
    NSNumber *isDataResidency = [[jsonObject valueForKey:KEY_IS_DATA_RESIDENCY] objectAtIndex:0];
    NSNumber *isAppTrackingTransparencyUsageEnabled = [[jsonObject valueForKey:KEY_IS_ATT_USAGE_ENABLED] objectAtIndex:0];
    NSNumber *isFirstSessionDelayEnabled = [[jsonObject valueForKey:KEY_IS_FIRST_SESSION_DELAY_ENABLED] objectAtIndex:0];
    id storeInfo = [[jsonObject valueForKey:KEY_STORE_INFO] objectAtIndex:0];

    BOOL allowSuppressLogLevel = NO;

    // Check for SUPPRESS log level.
    if ([self isFieldValid:logLevel]) {
        if ([ADJLogger logLevelFromString:[logLevel lowercaseString]] == ADJLogLevelSuppress) {
            allowSuppressLogLevel = YES;
        }
    }

    ADJConfig *adjustConfig = [[ADJConfig alloc] initWithAppToken:appToken
                                                      environment:environment
                                                 suppressLogLevel:allowSuppressLogLevel];

    if (![adjustConfig isValid]) {
        return;
    }

    // Log level.
    if ([self isFieldValid:logLevel]) {
        [adjustConfig setLogLevel:[ADJLogger logLevelFromString:[logLevel lowercaseString]]];
    }

    // Read device info just once.
    if ([self isFieldValid:isDeviceIdsReadingOnceEnabled] &&
        [isDeviceIdsReadingOnceEnabled boolValue]) {
        [adjustConfig enableDeviceIdsReadingOnce];
    }

    // SDK prefix.
    if ([self isFieldValid:sdkPrefix]) {
        [adjustConfig setSdkPrefix:sdkPrefix];
    }

    // Default tracker.
    if ([self isFieldValid:defaultTracker]) {
        [adjustConfig setDefaultTracker:defaultTracker];
    }

    // External device ID.
    if ([self isFieldValid:externalDeviceId]) {
        [adjustConfig setExternalDeviceId:externalDeviceId];
    }

    // ATT consent waiting interval.
    if ([self isFieldValid:attConsentWaitingInterval]) {
        [adjustConfig setAttConsentWaitingInterval:[attConsentWaitingInterval intValue]];
    }

    if ([self isFieldValid:eventDeduplicationIdsMaxSize]) {
        [adjustConfig setEventDeduplicationIdsMaxSize:[eventDeduplicationIdsMaxSize integerValue]];
    }

    // Send in background.
    if ([self isFieldValid:isSendingInBackgroundEnabled] &&
        [isSendingInBackgroundEnabled boolValue]) {
        [adjustConfig enableSendingInBackground];
    }

    // Cost data.
    if ([self isFieldValid:isCostDataInAttributionEnabled] &&
        [isCostDataInAttributionEnabled boolValue]) {
        [adjustConfig enableCostDataInAttribution];
    }

    // AdServices info reading.
    if ([self isFieldValid:isAdServicesEnabled] &&
        ![isAdServicesEnabled boolValue]) {
        [adjustConfig disableAdServices];
    }

    // IDFA reading.
    if ([self isFieldValid:isIdfaReadingEnabled] &&
        ![isIdfaReadingEnabled boolValue]) {
        [adjustConfig disableIdfaReading];
    }

    // IDFV reading.
    if ([self isFieldValid:isIdfvReadingEnabled] &&
        ![isIdfvReadingEnabled boolValue]) {
        [adjustConfig disableIdfvReading];
    }

    // SKAdNetwork handling.
    if ([self isFieldValid:isSkanAttributionEnabled] &&
        ![isSkanAttributionEnabled boolValue]) {
        [adjustConfig disableSkanAttribution];
    }

    // ATT usage
    if ([self isFieldValid:isAppTrackingTransparencyUsageEnabled] &&
        ![isAppTrackingTransparencyUsageEnabled boolValue]) {
        [adjustConfig disableAppTrackingTransparencyUsage];
    }

    // LinkMe.
    if ([self isFieldValid:isLinkMeEnabled] &&
        [isLinkMeEnabled boolValue]) {
        [adjustConfig enableLinkMe];
    }

    // Coppa
    if ([self isFieldValid:isCoppaComplianceEnabled] &&
        [isCoppaComplianceEnabled boolValue]) {
        [adjustConfig enableCoppaCompliance];
    }

    if ([self isFieldValid:isFirstSessionDelayEnabled] &&
        [isFirstSessionDelayEnabled boolValue]) {
        [adjustConfig enableFirstSessionDelay];
    }

    // Url Strategy
    if ([self isFieldValid:urlStrategyDomains] &&
        [urlStrategyDomains count] > 0 &&
        [self isFieldValid:useSubdomains] &&
        [self isFieldValid:isDataResidency]) {

        NSMutableArray *urlStrategyDomainsArray = [NSMutableArray array];
        for (int i = 0; i < [urlStrategyDomains count]; i += 1) {
            NSString *domain = [[urlStrategyDomains objectAtIndex:i] description];
            [urlStrategyDomainsArray addObject:domain];
        }
        [adjustConfig setUrlStrategy:urlStrategyDomains
                       useSubdomains:[useSubdomains boolValue]
                     isDataResidency:[isDataResidency boolValue]];
    }

    // store info
    if ([self isFieldValid:storeInfo]) {
        NSString *storeName = [storeInfo valueForKey:KEY_STORE_NAME];
        if ([self isFieldValid:storeName]) {
            ADJStoreInfo *adjustStoreInfo = [[ADJStoreInfo alloc] initWithStoreName:storeName];
            NSString *storeAppId = [storeInfo valueForKey:KEY_STORE_APP_ID];
            if ([self isFieldValid:storeAppId]) {
                [adjustStoreInfo setStoreAppId:storeAppId];
            }
            [adjustConfig setStoreInfo:adjustStoreInfo];
        }
    }

    BOOL shouldLaunchDeferredDeeplink = [self isFieldValid:isDeferredDeeplinkOpeningEnabled] ? [isDeferredDeeplinkOpeningEnabled boolValue] : YES;

    // Attribution delegate & other delegates
    if (attributionCallbackId != nil
        || eventTrackingSucceededCallbackId != nil
        || eventTrackingFailedCallbackId != nil
        || sessionTrackingSucceededCallbackId != nil
        || sessionTrackingFailedCallbackId != nil
        || deferredDeeplinkCallbackId != nil
        || skanUpdatedCallbackId != nil
        ) {
        [adjustConfig setDelegate:
         [AdjustCordovaDelegate getInstanceWithSwizzledAttributionCallbackId:attributionCallbackId
                                            eventTrackingSucceededCallbackId:eventTrackingSucceededCallbackId
                                               eventTrackingFailedCallbackId:eventTrackingFailedCallbackId
                                          sessionTrackingSucceededCallbackId:sessionTrackingSucceededCallbackId
                                             sessionTrackingFailedCallbackId:sessionTrackingFailedCallbackId
                                                  deferredDeeplinkCallbackId:deferredDeeplinkCallbackId
                                                       skanUpdatedCallbackId:skanUpdatedCallbackId
                                                shouldLaunchDeferredDeeplink:shouldLaunchDeferredDeeplink
                                                         withCommandDelegate:self.commandDelegate]];
    }

    // Start SDK.
    [Adjust initSdk:adjustConfig];
}

- (void)setAttributionCallback:(CDVInvokedUrlCommand *)command {
    attributionCallbackId = command.callbackId;
}

- (void)setEventTrackingSucceededCallback:(CDVInvokedUrlCommand *)command {
    eventTrackingSucceededCallbackId = command.callbackId;
}

- (void)setEventTrackingFailedCallback:(CDVInvokedUrlCommand *)command {
    eventTrackingFailedCallbackId = command.callbackId;
}

- (void)setSessionTrackingSucceededCallback:(CDVInvokedUrlCommand *)command {
    sessionTrackingSucceededCallbackId = command.callbackId;
}

- (void)setSessionTrackingFailedCallback:(CDVInvokedUrlCommand *)command {
    sessionTrackingFailedCallbackId = command.callbackId;
}

- (void)setDeferredDeeplinkCallback:(CDVInvokedUrlCommand *)command {
    deferredDeeplinkCallbackId = command.callbackId;
}

- (void)setPushToken:(CDVInvokedUrlCommand *)command {
    NSString *token = [command argumentAtIndex:0 withDefault:nil];
    if (!([self isFieldValid:token])) {
        return;
    }
    [Adjust setPushTokenAsString:token];
}

- (void)getAttribution:(CDVInvokedUrlCommand *)command {

    [Adjust attributionWithCompletionHandler:^(ADJAttribution * _Nullable attribution) {
        if (attribution == nil) {
            return;
        }

        NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
        [self addValueOrEmpty:attribution.trackerToken withKey:@"trackerToken" toDictionary:dictionary];
        [self addValueOrEmpty:attribution.trackerName withKey:@"trackerName" toDictionary:dictionary];
        [self addValueOrEmpty:attribution.network withKey:@"network" toDictionary:dictionary];
        [self addValueOrEmpty:attribution.campaign withKey:@"campaign" toDictionary:dictionary];
        [self addValueOrEmpty:attribution.creative withKey:@"creative" toDictionary:dictionary];
        [self addValueOrEmpty:attribution.adgroup withKey:@"adgroup" toDictionary:dictionary];
        [self addValueOrEmpty:attribution.clickLabel withKey:@"clickLabel" toDictionary:dictionary];
        [self addValueOrEmpty:attribution.costType withKey:@"costType" toDictionary:dictionary];
        [self addValueOrEmpty:attribution.costAmount withKey:@"costAmount" toDictionary:dictionary];
        [self addValueOrEmpty:attribution.costCurrency withKey:@"costCurrency" toDictionary:dictionary];
        if (attribution.jsonResponse != nil) {
            NSData *dataJsonResponse = [NSJSONSerialization dataWithJSONObject:attribution.jsonResponse
                                                                       options:0
                                                                         error:nil];
            NSString *stringJsonResponse = [[NSString alloc] initWithBytes:[dataJsonResponse bytes]
                                                                    length:[dataJsonResponse length]
                                                                  encoding:NSUTF8StringEncoding];
            [dictionary setObject:stringJsonResponse forKey:@"jsonResponse"];
        }

        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dictionary];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)getAdid:(CDVInvokedUrlCommand *)command {
    [Adjust adidWithCompletionHandler:^(NSString * _Nullable adid) {
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:adid];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}


- (void)getSdkVersion:(CDVInvokedUrlCommand *)command {
    [Adjust sdkVersionWithCompletionHandler:^(NSString * _Nullable sdkVersion) {
        if (sdkVersion == nil) {
            sdkVersion = @"";
        }
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:sdkVersion];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)addGlobalCallbackParameter:(CDVInvokedUrlCommand *)command {
    NSString *key = [command argumentAtIndex:0 withDefault:nil];
    NSString *value = [command argumentAtIndex:1 withDefault:nil];
    if (!([self isFieldValid:key]) || !([self isFieldValid:value])) {
        return;
    }
    [Adjust addGlobalCallbackParameter:value forKey:key];
}

- (void)removeGlobalCallbackParameter:(CDVInvokedUrlCommand *)command {
    NSString *key = [command argumentAtIndex:0 withDefault:nil];
    if (!([self isFieldValid:key])) {
        return;
    }
    [Adjust removeGlobalCallbackParameterForKey:key];
}

- (void)removeGlobalCallbackParameters:(CDVInvokedUrlCommand *)command {
    [Adjust removeGlobalCallbackParameters];
}

- (void)addGlobalPartnerParameter:(CDVInvokedUrlCommand *)command {
    NSString *key = [command argumentAtIndex:0 withDefault:nil];
    NSString *value = [command argumentAtIndex:1 withDefault:nil];
    if (!([self isFieldValid:key]) || !([self isFieldValid:value])) {
        return;
    }
    [Adjust addGlobalPartnerParameter:value forKey:key];
}

- (void)removeGlobalPartnerParameter:(CDVInvokedUrlCommand *)command {
    NSString *key = [command argumentAtIndex:0 withDefault:nil];
    if (!([self isFieldValid:key])) {
        return;
    }
    [Adjust removeGlobalPartnerParameterForKey:key];
}

- (void)removeGlobalPartnerParameters:(CDVInvokedUrlCommand *)command {
    [Adjust removeGlobalPartnerParameters];
}

- (void)switchToOfflineMode:(CDVInvokedUrlCommand *)command {
    [Adjust switchToOfflineMode];
}

- (void)switchBackToOnlineMode:(CDVInvokedUrlCommand *)command {
    [Adjust switchBackToOnlineMode];
}

- (void)enable:(CDVInvokedUrlCommand *)command {
    [Adjust enable];
}

- (void)disable:(CDVInvokedUrlCommand *)command {
    [Adjust disable];
}

- (void)isEnabled:(CDVInvokedUrlCommand *)command {
    [Adjust isEnabledWithCompletionHandler:^(BOOL isEnabled) {
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsBool:isEnabled];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)gdprForgetMe:(CDVInvokedUrlCommand *)command {
    [Adjust gdprForgetMe];
}

- (void)trackEvent:(CDVInvokedUrlCommand *)command {
    ADJEvent *adjustEvent = [self serializeAdjustEventFromCommand:command];
    if (adjustEvent == nil) {
        return;
    }

    // Track event.
    [Adjust trackEvent:adjustEvent];
}

- (void)trackAdRevenue:(CDVInvokedUrlCommand *)command {
    if ([command.arguments count] == 1) {
        NSString *arguments = [command.arguments objectAtIndex:0];
        NSArray *jsonObject = [NSJSONSerialization JSONObjectWithData:[arguments dataUsingEncoding:NSUTF8StringEncoding]
                                                              options:0
                                                                error:NULL];

        NSString *source = [[jsonObject valueForKey:KEY_SOURCE] objectAtIndex:0];
        NSString *revenue = [[jsonObject valueForKey:KEY_REVENUE] objectAtIndex:0];
        NSString *currency = [[jsonObject valueForKey:KEY_CURRENCY] objectAtIndex:0];
        NSString *adImpressionsCount = [[jsonObject valueForKey:KEY_AD_IMPRESSIONS_COUNT] objectAtIndex:0];
        NSString *adRevenueNetwork = [[jsonObject valueForKey:KEY_AD_REVENUE_NETWORK] objectAtIndex:0];
        NSString *adRevenueUnit = [[jsonObject valueForKey:KEY_AD_REVENUE_UNIT] objectAtIndex:0];
        NSString *adRevenuePlacement = [[jsonObject valueForKey:KEY_AD_REVENUE_PLACEMENT] objectAtIndex:0];
        NSMutableArray *callbackParameters = [[NSMutableArray alloc] init];
        NSMutableArray *partnerParameters = [[NSMutableArray alloc] init];

        if ([self isFieldValid:[[jsonObject valueForKey:KEY_CALLBACK_PARAMETERS] objectAtIndex:0]]) {
            for (id item in [[jsonObject valueForKey:KEY_CALLBACK_PARAMETERS] objectAtIndex:0]) {
                [callbackParameters addObject:item];
            }
        }
        if ([self isFieldValid:[[jsonObject valueForKey:KEY_PARTNER_PARAMETERS] objectAtIndex:0]]) {
            for (id item in [[jsonObject valueForKey:KEY_PARTNER_PARAMETERS] objectAtIndex:0]) {
                [partnerParameters addObject:item];
            }
        }

        ADJAdRevenue *adjustAdRevenue = [[ADJAdRevenue alloc] initWithSource:source];

        // Revenue and currency.
        if ([self isFieldValid:revenue]) {
            double revenueValue = [revenue doubleValue];
            [adjustAdRevenue setRevenue:revenueValue currency:currency];
        }

        // Ad impressions count.
        if ([self isFieldValid:adImpressionsCount]) {
            int adImpressionsCountValue = [adImpressionsCount intValue];
            [adjustAdRevenue setAdImpressionsCount:adImpressionsCountValue];
        }

        // Ad revenue network.
        if ([self isFieldValid:adRevenueNetwork]) {
            [adjustAdRevenue setAdRevenueNetwork:adRevenueNetwork];
        }

        // Ad revenue unit.
        if ([self isFieldValid:adRevenueUnit]) {
            [adjustAdRevenue setAdRevenueUnit:adRevenueUnit];
        }

        // Ad revenue placement.
        if ([self isFieldValid:adRevenuePlacement]) {
            [adjustAdRevenue setAdRevenuePlacement:adRevenuePlacement];
        }

        // Callback parameters.
        for (int i = 0; i < [callbackParameters count]; i += 2) {
            NSString *key = [callbackParameters objectAtIndex:i];
            NSObject *value = [callbackParameters objectAtIndex:(i+1)];
            [adjustAdRevenue addCallbackParameter:key value:[NSString stringWithFormat:@"%@", value]];
        }

        // Partner parameters.
        for (int i = 0; i < [partnerParameters count]; i += 2) {
            NSString *key = [partnerParameters objectAtIndex:i];
            NSObject *value = [partnerParameters objectAtIndex:(i+1)];
            [adjustAdRevenue addPartnerParameter:key value:[NSString stringWithFormat:@"%@", value]];
        }

        // Track ad revenue.
        [Adjust trackAdRevenue:adjustAdRevenue];
    }
}

- (void)trackThirdPartySharing:(CDVInvokedUrlCommand *)command {
    NSString *arguments = [command.arguments objectAtIndex:0];
    NSArray *jsonObject = [NSJSONSerialization JSONObjectWithData:[arguments dataUsingEncoding:NSUTF8StringEncoding]
                                                          options:0
                                                            error:NULL];

    NSNumber *isEnabled = [[jsonObject valueForKey:KEY_IS_ENABLED] objectAtIndex:0];
    NSMutableArray *granularOptions = [[NSMutableArray alloc] init];
    if ([self isFieldValid:[[jsonObject valueForKey:KEY_GRANULAR_OPTIONS] objectAtIndex:0]]) {
        for (id item in [[jsonObject valueForKey:KEY_GRANULAR_OPTIONS] objectAtIndex:0]) {
            [granularOptions addObject:item];
        }
    }
    NSMutableArray *partnerSharingSettings = [[NSMutableArray alloc] init];
    if ([self isFieldValid:[[jsonObject valueForKey:KEY_PARTNER_SHARING_SETTINGS] objectAtIndex:0]]) {
        for (id item in [[jsonObject valueForKey:KEY_PARTNER_SHARING_SETTINGS] objectAtIndex:0]) {
            [partnerSharingSettings addObject:item];
        }
    }

    if (isEnabled != nil && [isEnabled isKindOfClass:[NSNull class]]) {
        isEnabled = nil;
    }

    ADJThirdPartySharing *adjustThirdPartySharing = [[ADJThirdPartySharing alloc] initWithIsEnabled:isEnabled];

    // Granular options.
    if ([self isFieldValid:granularOptions]) {
        for (int i = 0; i < [granularOptions count]; i += 3) {
            NSString *partnerName = [granularOptions objectAtIndex:i];
            NSString *key = [granularOptions objectAtIndex:i+1];
            NSString *value = [granularOptions objectAtIndex:i+2];
            [adjustThirdPartySharing addGranularOption:partnerName key:key value:value];
        }
    }

    // Partner sharing settings.
    if ([self isFieldValid:partnerSharingSettings]) {
        for (int i = 0; i < [partnerSharingSettings count]; i += 3) {
            NSString *partnerName = [partnerSharingSettings objectAtIndex:i];
            NSString *key = [partnerSharingSettings objectAtIndex:i+1];
            NSString *value = [partnerSharingSettings objectAtIndex:i+2];
            [adjustThirdPartySharing addPartnerSharingSetting:partnerName key:key value:[value boolValue]];
        }
    }

    // Track third party sharing.
    [Adjust trackThirdPartySharing:adjustThirdPartySharing];
}

- (void)trackMeasurementConsent:(CDVInvokedUrlCommand *)command {
    NSNumber *isEnabledNumber = [command argumentAtIndex:0 withDefault:nil];
    if (isEnabledNumber == nil) {
        return;
    }

    [Adjust trackMeasurementConsent:[isEnabledNumber boolValue]];
}

- (void)processDeeplink:(CDVInvokedUrlCommand *)command {
    NSString *arguments = [command.arguments objectAtIndex:0];
    NSArray *jsonObject = [NSJSONSerialization JSONObjectWithData:[arguments dataUsingEncoding:NSUTF8StringEncoding]
                                                          options:0
                                                            error:NULL];
    NSString *deeplink;
    if ([self isFieldValid:[[jsonObject valueForKey:KEY_DEEPLINK] objectAtIndex:0]]) {
        deeplink = [[jsonObject valueForKey:KEY_DEEPLINK] objectAtIndex:0];
    } else {
        return;
    }

    NSURL *urlDeeplink = [NSURL URLWithString:deeplink];
    ADJDeeplink *adjustDeeplink = [[ADJDeeplink alloc] initWithDeeplink:urlDeeplink];

    if ([self isFieldValid:[[jsonObject valueForKey:KEY_REFERRER] objectAtIndex:0]]) {
        NSString *referrer = [[jsonObject valueForKey:KEY_REFERRER] objectAtIndex:0];
        NSURL *urlReferrer = [NSURL URLWithString:referrer];
        [adjustDeeplink setReferrer:urlReferrer];
    }

    [Adjust processDeeplink:adjustDeeplink];
}

- (void)processAndResolveDeeplink:(CDVInvokedUrlCommand *)command {
    NSString *arguments = [command.arguments objectAtIndex:0];
    NSArray *jsonObject = [NSJSONSerialization JSONObjectWithData:[arguments dataUsingEncoding:NSUTF8StringEncoding]
                                                          options:0
                                                            error:NULL];
    NSString *deeplink;
    if ([self isFieldValid:[[jsonObject valueForKey:KEY_DEEPLINK] objectAtIndex:0]]) {
        deeplink = [[jsonObject valueForKey:KEY_DEEPLINK] objectAtIndex:0];
    } else {
        return;
    }

    NSURL *urlDeeplink = [NSURL URLWithString:deeplink];
    ADJDeeplink *adjustDeeplink = [[ADJDeeplink alloc] initWithDeeplink:urlDeeplink];

    if ([self isFieldValid:[[jsonObject valueForKey:KEY_REFERRER] objectAtIndex:0]]) {
        NSString *referrer = [[jsonObject valueForKey:KEY_REFERRER] objectAtIndex:0];
        NSURL *urlReferrer = [NSURL URLWithString:referrer];
        [adjustDeeplink setReferrer:urlReferrer];
    }

    [Adjust processAndResolveDeeplink:adjustDeeplink
                withCompletionHandler:^(NSString * _Nullable resolvedLink) {
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:resolvedLink];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)getLastDeeplink:(CDVInvokedUrlCommand *)command {
    [Adjust lastDeeplinkWithCompletionHandler:^(NSURL * _Nullable lastDeeplink) {
        NSString *lastDeeplinkString = nil;
        if (lastDeeplink == nil) {
            lastDeeplinkString = @"";
        } else {
            lastDeeplinkString = [lastDeeplink absoluteString];
        }
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:lastDeeplinkString];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)endFirstSessionDelay:(CDVInvokedUrlCommand *)command {
    [Adjust endFirstSessionDelay];
}

- (void)enableCoppaComplianceInDelay:(CDVInvokedUrlCommand *)command {
    [Adjust enableCoppaComplianceInDelay];
}

- (void)disableCoppaComplianceInDelay:(CDVInvokedUrlCommand *)command {
    [Adjust disableCoppaComplianceInDelay];
}

- (void)setExternalDeviceIdInDelay:(CDVInvokedUrlCommand *)command {
    NSString *externalDeviceId = [command argumentAtIndex:0 withDefault:nil];
    if (!([self isFieldValid:externalDeviceId])) {
        return;
    }
    [Adjust setExternalDeviceIdInDelay:externalDeviceId];
}

#pragma mark - iOS methods

- (void)setSkanUpdatedCallback:(CDVInvokedUrlCommand *)command {
    skanUpdatedCallbackId = command.callbackId;
}

- (void)getIdfa:(CDVInvokedUrlCommand *)command {
    [Adjust idfaWithCompletionHandler:^(NSString * _Nullable idfa) {
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:idfa];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)getIdfv:(CDVInvokedUrlCommand *)command {
    [Adjust idfvWithCompletionHandler:^(NSString * _Nullable idfv) {
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:idfv];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)trackAppStoreSubscription:(CDVInvokedUrlCommand *)command {
    NSString *arguments = [command.arguments objectAtIndex:0];
    NSArray *jsonObject = [NSJSONSerialization JSONObjectWithData:[arguments dataUsingEncoding:NSUTF8StringEncoding]
                                                          options:0
                                                            error:NULL];

    NSString *price = [[jsonObject valueForKey:KEY_PRICE] objectAtIndex:0];
    NSString *currency = [[jsonObject valueForKey:KEY_CURRENCY] objectAtIndex:0];
    NSString *transactionId = [[jsonObject valueForKey:KEY_TRANSACTION_ID] objectAtIndex:0];
    NSString *transactionDate = [[jsonObject valueForKey:KEY_TRANSACTION_DATE] objectAtIndex:0];
    NSString *salesRegion = [[jsonObject valueForKey:KEY_SALES_REGION] objectAtIndex:0];
    NSMutableArray *callbackParameters = [[NSMutableArray alloc] init];
    NSMutableArray *partnerParameters = [[NSMutableArray alloc] init];

    for (id item in [[jsonObject valueForKey:KEY_CALLBACK_PARAMETERS] objectAtIndex:0]) {
        [callbackParameters addObject:item];
    }
    for (id item in [[jsonObject valueForKey:KEY_PARTNER_PARAMETERS] objectAtIndex:0]) {
        [partnerParameters addObject:item];
    }

    // Price.
    NSDecimalNumber *priceValue;
    if ([self isFieldValid:price]) {
        priceValue = [NSDecimalNumber decimalNumberWithString:price];
    }

    ADJAppStoreSubscription *subscription = [[ADJAppStoreSubscription alloc] initWithPrice:priceValue
                                                                                  currency:currency
                                                                             transactionId:transactionId];

    // Transaction date.
    if ([self isFieldValid:transactionDate]) {
        NSTimeInterval transactionDateInterval = [transactionDate doubleValue];
        NSDate *oTransactionDate = [NSDate dateWithTimeIntervalSince1970:transactionDateInterval];
        [subscription setTransactionDate:oTransactionDate];
    }

    // Sales region.
    if ([self isFieldValid:salesRegion]) {
        [subscription setSalesRegion:salesRegion];
    }

    // Callback parameters.
    for (int i = 0; i < [callbackParameters count]; i += 2) {
        NSString *key = [callbackParameters objectAtIndex:i];
        NSObject *value = [callbackParameters objectAtIndex:(i+1)];
        [subscription addCallbackParameter:key value:[NSString stringWithFormat:@"%@", value]];
    }

    // Partner parameters.
    for (int i = 0; i < [partnerParameters count]; i += 2) {
        NSString *key = [partnerParameters objectAtIndex:i];
        NSObject *value = [partnerParameters objectAtIndex:(i+1)];
        [subscription addPartnerParameter:key value:[NSString stringWithFormat:@"%@", value]];
    }

    // Track subscription.
    [Adjust trackAppStoreSubscription:subscription];
}

- (void)verifyAppStorePurchase:(CDVInvokedUrlCommand *)command {
    NSString *arguments = [command.arguments objectAtIndex:0];
    NSArray *jsonObject = [NSJSONSerialization JSONObjectWithData:[arguments dataUsingEncoding:NSUTF8StringEncoding]
                                                          options:0
                                                            error:NULL];

    NSString *productId = [[jsonObject valueForKey:KEY_PRODUCT_ID] objectAtIndex:0];
    NSString *transactionId = [[jsonObject valueForKey:KEY_TRANSACTION_ID] objectAtIndex:0];

    // Create purchase instance.
    ADJAppStorePurchase *purchase = [[ADJAppStorePurchase alloc] initWithTransactionId:transactionId
                                                                             productId:productId];
    // Verify purchase.
    [Adjust verifyAppStorePurchase:purchase
             withCompletionHandler:^(ADJPurchaseVerificationResult * _Nonnull verificationResult) {
        NSDictionary *dictionary = nil;
        if (verificationResult == nil) {
            dictionary = [NSMutableDictionary dictionary];
            CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                                          messageAsDictionary:dictionary];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
            return;
        }

        dictionary = [self deserializePvResult:verificationResult];
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                                      messageAsDictionary:dictionary];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)verifyAndTrackAppStorePurchase:(CDVInvokedUrlCommand *)command {
    ADJEvent *adjustEvent = [self serializeAdjustEventFromCommand:command];
    if (adjustEvent == nil) {
        return;
    }

    [Adjust verifyAndTrackAppStorePurchase:adjustEvent
                     withCompletionHandler:^(ADJPurchaseVerificationResult * _Nonnull verificationResult) {
        NSDictionary *dictionary = nil;;
        if (verificationResult == nil) {
            dictionary = [NSMutableDictionary dictionary];
            CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                                          messageAsDictionary:dictionary];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
            return;
        }

        dictionary = [self deserializePvResult:verificationResult];
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK
                                                      messageAsDictionary:dictionary];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)requestAppTrackingAuthorization:(CDVInvokedUrlCommand *)command {
    [Adjust requestAppTrackingAuthorizationWithCompletionHandler:^(NSUInteger status) {
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsNSUInteger:status];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

- (void)getAppTrackingAuthorizationStatus:(CDVInvokedUrlCommand *)command {
    int appTrackingAuthorizationStatus = [Adjust appTrackingAuthorizationStatus];
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsInt:appTrackingAuthorizationStatus];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)updateSkanConversionValue:(CDVInvokedUrlCommand *)command {
    NSNumber *conversionValue = [command argumentAtIndex:0 withDefault:nil];
    NSString *coarseValue = [command argumentAtIndex:1 withDefault:nil];
    NSNumber *lockWindow = [command argumentAtIndex:2 withDefault:nil];
    if (![self isFieldValid:conversionValue]) {
        return;
    }

    [Adjust updateSkanConversionValue:[conversionValue intValue]
                          coarseValue:coarseValue
                           lockWindow:lockWindow
                withCompletionHandler:^(NSError * _Nullable error) {
        CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:[error localizedDescription]];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}

#pragma mark - Android methods

- (void)getGoogleAdId:(CDVInvokedUrlCommand *)command {
    // ignore on ios
    NSString *googleAdId = @"";
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:googleAdId];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)getAmazonAdId:(CDVInvokedUrlCommand *)command {
    // ignore on ios
    NSString *amazonAdId = @"";
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:amazonAdId];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)trackPlayStoreSubscription:(CDVInvokedUrlCommand *)command {
    // ignore on ios
}

- (void)verifyPlayStorePurchase:(CDVInvokedUrlCommand *)command {
    // ignore on ios
    NSMutableDictionary *verificationResult = [NSMutableDictionary dictionary];
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:verificationResult];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)verifyAndTrackPlayStorePurchase:(CDVInvokedUrlCommand *)command {
    // ignore on ios
    NSMutableDictionary *verificationResult = [NSMutableDictionary dictionary];
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:verificationResult];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

#pragma mark - Testing methods

- (void)onPause:(CDVInvokedUrlCommand *)command {
    [Adjust trackSubsessionEnd];
}

- (void)onResume:(CDVInvokedUrlCommand *)command {
    [Adjust trackSubsessionStart];
}

- (void)setTestOptions:(CDVInvokedUrlCommand *)command {
    NSString *testUrlOverwrite = [[command.arguments valueForKey:KEY_TEST_URL_OVERWRITE] objectAtIndex:0];
    NSString *extraPath = [[command.arguments valueForKey:KEY_EXTRA_PATH] objectAtIndex:0];
    NSString *timerInterval = [[command.arguments valueForKey:KEY_TIMER_INTERVAL] objectAtIndex:0];
    NSString *timerStart = [[command.arguments valueForKey:KEY_TIMER_START] objectAtIndex:0];
    NSString *sessionInterval = [[command.arguments valueForKey:KEY_SESSION_INTERVAL] objectAtIndex:0];
    NSString *subsessionInterval = [[command.arguments valueForKey:KEY_SUBSESSION_INTERVAL] objectAtIndex:0];
    NSString *teardown = [[command.arguments valueForKey:KEY_TEARDOWN] objectAtIndex:0];
    NSString *deleteState = [[command.arguments valueForKey:KEY_DELETE_STATE] objectAtIndex:0];
    NSString *noBackoffWait = [[command.arguments valueForKey:KEY_NO_BACKOFF_WAIT] objectAtIndex:0];
    NSString *adServicesFrameworkEnabled = [[command.arguments valueForKey:KEY_ADSERVICES_ENABLED] objectAtIndex:0];
    NSString *attStatus = [[command.arguments valueForKey:KEY_ATT_STATUS] objectAtIndex:0];
    NSString *idfa = [[command.arguments valueForKey:KEY_IDFA] objectAtIndex:0];

    NSMutableDictionary *testOptions = [NSMutableDictionary dictionary];

    if ([self isFieldValid:testUrlOverwrite]) {
        [testOptions setObject:testUrlOverwrite forKey:KEY_TEST_URL_OVERWRITE];
    }
    if ([self isFieldValid:extraPath]) {
        [testOptions setObject:extraPath forKey:KEY_EXTRA_PATH];
    }
    if ([self isFieldValid:timerInterval]) {
        NSNumber *timerIntervalInMilliseconds = [self convertMilliStringToNumber:timerInterval];
        [testOptions setObject:timerIntervalInMilliseconds forKey:KEY_TIMER_INTERVAL];
    }
    if ([self isFieldValid:timerStart]) {
        NSNumber *timerStartInMilliseconds = [self convertMilliStringToNumber:timerStart];
        [testOptions setObject:timerStartInMilliseconds forKey:KEY_TIMER_START];
    }
    if ([self isFieldValid:sessionInterval]) {
        NSNumber *sessionIntervalInMilliseconds = [self convertMilliStringToNumber:sessionInterval];
        [testOptions setObject:sessionIntervalInMilliseconds forKey:KEY_SESSION_INTERVAL];
    }
    if ([self isFieldValid:subsessionInterval]) {
        NSNumber *subsessionIntervalInMilliseconds = [self convertMilliStringToNumber:subsessionInterval];
        [testOptions setObject:subsessionIntervalInMilliseconds forKey:KEY_SUBSESSION_INTERVAL];
    }
    if ([self isFieldValid:attStatus]) {
        NSNumber *attStatusNum = [NSNumber numberWithInt:[attStatus intValue]];
        [testOptions setObject:attStatusNum forKey:@"attStatusInt"];
    }
    if ([self isFieldValid:idfa]) {
        [testOptions setObject:idfa forKey:KEY_IDFA];
    }
    if ([self isFieldValid:deleteState]) {
        NSNumber *deleteStateNumber = [NSNumber numberWithBool:[deleteState boolValue]];
        [testOptions setObject:deleteStateNumber forKey:KEY_DELETE_STATE];
    }
    if ([self isFieldValid:teardown]) {
        NSNumber *tearDownNumber = [NSNumber numberWithBool:[teardown boolValue]];
        [testOptions setObject:tearDownNumber forKey:KEY_TEARDOWN];
    }
    if ([self isFieldValid:noBackoffWait]) {
        NSNumber *noBackoffWaitNum = [NSNumber numberWithBool:[noBackoffWait boolValue]];
        [testOptions setObject:noBackoffWaitNum forKey:KEY_NO_BACKOFF_WAIT];

    }
    if ([self isFieldValid:adServicesFrameworkEnabled]) {
        NSNumber *adServicesFrameworkEnabledNum = [NSNumber numberWithBool:[adServicesFrameworkEnabled boolValue]];
        [testOptions setObject:adServicesFrameworkEnabledNum forKey:KEY_ADSERVICES_ENABLED];
    }

    [Adjust setTestOptions:testOptions];
}

- (void)teardown:(CDVInvokedUrlCommand *)command {
    attributionCallbackId = nil;
    eventTrackingFailedCallbackId = nil;
    eventTrackingSucceededCallbackId = nil;
    sessionTrackingFailedCallbackId = nil;
    sessionTrackingSucceededCallbackId = nil;
    deferredDeeplinkCallbackId = nil;
    skanUpdatedCallbackId = nil;
    [AdjustCordovaDelegate teardown];
}

#pragma mark - Private & helper methods

- (NSNumber *)convertMilliStringToNumber:(NSString *)milliS {
    NSNumber *number = [NSNumber numberWithInt:[milliS intValue]];
    return number;
}

- (BOOL)isFieldValid:(NSObject *)field {
    if (field == nil) {
        return NO;
    }

    // Check if its an instance of the singleton NSNull
    if ([field isKindOfClass:[NSNull class]]) {
        return NO;
    }

    // If field can be converted to a string, check if it has any content.
    NSString *str = [NSString stringWithFormat:@"%@", field];
    if (str != nil) {
        if ([str length] == 0) {
            return NO;
        }
    }

    return YES;
}

- (void)addValueOrEmpty:(NSObject *)value
                withKey:(NSString *)key
           toDictionary:(NSMutableDictionary *)dictionary {
    if (nil != value) {
        [dictionary setObject:[NSString stringWithFormat:@"%@", value] forKey:key];
    } else {
        [dictionary setObject:@"" forKey:key];
    }
}

- (ADJEvent *)serializeAdjustEventFromCommand:(CDVInvokedUrlCommand *)command {
    NSString *arguments = [command.arguments objectAtIndex:0];
    NSArray *jsonObject = [NSJSONSerialization JSONObjectWithData:[arguments dataUsingEncoding:NSUTF8StringEncoding]
                                                          options:0
                                                            error:NULL];

    NSString *eventToken = [[jsonObject valueForKey:KEY_EVENT_TOKEN] objectAtIndex:0];
    NSString *revenue = [[jsonObject valueForKey:KEY_REVENUE] objectAtIndex:0];
    NSString *currency = [[jsonObject valueForKey:KEY_CURRENCY] objectAtIndex:0];
    NSString *productId = [[jsonObject valueForKey:KEY_PRODUCT_ID] objectAtIndex:0];
    NSString *transactionId = [[jsonObject valueForKey:KEY_TRANSACTION_ID] objectAtIndex:0];
    NSString *callbackId = [[jsonObject valueForKey:KEY_CALLBACK_ID] objectAtIndex:0];
    NSString *deduplicationId = [[jsonObject valueForKey:KEY_DEDUPLICATION_ID] objectAtIndex:0];
    NSMutableArray *callbackParameters = [[NSMutableArray alloc] init];
    NSMutableArray *partnerParameters = [[NSMutableArray alloc] init];

    if ([self isFieldValid:[[jsonObject valueForKey:KEY_CALLBACK_PARAMETERS] objectAtIndex:0]]) {
        for (id item in [[jsonObject valueForKey:KEY_CALLBACK_PARAMETERS] objectAtIndex:0]) {
            [callbackParameters addObject:item];
        }
    }
    if ([self isFieldValid:[[jsonObject valueForKey:KEY_PARTNER_PARAMETERS] objectAtIndex:0]]) {
        for (id item in [[jsonObject valueForKey:KEY_PARTNER_PARAMETERS] objectAtIndex:0]) {
            [partnerParameters addObject:item];
        }
    }

    ADJEvent *adjustEvent = [[ADJEvent alloc] initWithEventToken:eventToken];

    if (![adjustEvent isValid]) {
        return nil;
    }

    // Revenue and currency.
    if ([self isFieldValid:revenue]) {
        double revenueValue = [revenue doubleValue];
        [adjustEvent setRevenue:revenueValue currency:currency];
    }

    // Callback parameters.
    for (int i = 0; i < [callbackParameters count]; i += 2) {
        NSString *key = [callbackParameters objectAtIndex:i];
        NSObject *value = [callbackParameters objectAtIndex:(i+1)];
        [adjustEvent addCallbackParameter:key value:[NSString stringWithFormat:@"%@", value]];
    }

    // Partner parameters.
    for (int i = 0; i < [partnerParameters count]; i += 2) {
        NSString *key = [partnerParameters objectAtIndex:i];
        NSObject *value = [partnerParameters objectAtIndex:(i+1)];
        [adjustEvent addPartnerParameter:key value:[NSString stringWithFormat:@"%@", value]];
    }

    // Deduplication ID.
    if ([self isFieldValid:deduplicationId]) {
        [adjustEvent setDeduplicationId:deduplicationId];
    }

    // Callback ID.
    if ([self isFieldValid:callbackId]) {
        [adjustEvent setCallbackId:callbackId];
    }


    // Product ID.
    if ([self isFieldValid:productId]) {
        [adjustEvent setProductId:productId];
    }

    // Transaction ID.
    if ([self isFieldValid:transactionId]) {
        [adjustEvent setTransactionId:transactionId];
    }

    return adjustEvent;
}

- (NSMutableDictionary *)deserializePvResult:(ADJPurchaseVerificationResult *)pvResult {
    NSMutableDictionary *dictionary = [NSMutableDictionary dictionary];
    [self addValueOrEmpty:pvResult.verificationStatus
                  withKey:@"verificationStatus"
             toDictionary:dictionary];
    [self addValueOrEmpty:[NSString stringWithFormat:@"%d", pvResult.code]
                  withKey:@"code"
             toDictionary:dictionary];
    [self addValueOrEmpty:pvResult.message
                  withKey:@"message"
             toDictionary:dictionary];

    return dictionary;
}


@end
