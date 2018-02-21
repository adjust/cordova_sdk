//
//  AdjustCordova.h
//  Adjust SDK
//
//  Created by Pedro Filipe (@nonelse) on 3rd April 2014.
//  Copyright (c) 2012-2017 Adjust GmbH. All rights reserved.
//

#import <Cordova/CDV.h>

@interface AdjustTestingCordova : CDVPlugin

- (void)startTestSession:(CDVInvokedUrlCommand *)command;
- (void)addInfoToSend:(CDVInvokedUrlCommand *)command;
- (void)sendInfoToServer:(CDVInvokedUrlCommand *)command;
- (void)addTest:(CDVInvokedUrlCommand *)command;
- (void)addTestDirectory:(CDVInvokedUrlCommand *)command;

@end
