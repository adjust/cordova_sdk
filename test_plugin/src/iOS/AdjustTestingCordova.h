//
//  AdjustTestingCordova.h
//  Adjust SDK
//
//  Created by Abdullah Obaied (@obaied) on 20th February 2018.
//  Copyright (c) 2012-2018 Adjust GmbH. All rights reserved.
//

#import <Cordova/CDV.h>

@interface AdjustTestingCordova : CDVPlugin

- (void)startTestSession:(CDVInvokedUrlCommand *)command;
- (void)addInfoToSend:(CDVInvokedUrlCommand *)command;
- (void)sendInfoToServer:(CDVInvokedUrlCommand *)command;
- (void)addTest:(CDVInvokedUrlCommand *)command;
- (void)addTestDirectory:(CDVInvokedUrlCommand *)command;

@end
