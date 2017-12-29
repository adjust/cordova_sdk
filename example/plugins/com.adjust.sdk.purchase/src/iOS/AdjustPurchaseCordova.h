//
//  AdjustCordova.h
//  Adjust
//
//  Created by Pedro Filipe on 04/03/14.
//  Copyright (c) 2012-2014 adjust GmbH. All rights reserved.
//

#import <Cordova/CDV.h>
#import <AdjustPurchaseSdk/AdjustPurchase.h>

@interface AdjustPurchaseCordova : CDVPlugin

- (void)init:(CDVInvokedUrlCommand *)command;
- (void)verifyPurchaseiOS:(CDVInvokedUrlCommand *)command;
- (void)verifyPurchaseAndroid:(CDVInvokedUrlCommand *)command;
- (void)setVerificationCallback:(CDVInvokedUrlCommand *)command;

@end
