//
//  ADJCordovaTest.h
//  Adjust SDK
//
//  Created by Abdullah Obaied (@obaied) on 20th February 2018.
//  Copyright (c) 2018-Present Adjust GmbH. All rights reserved.
//

#import <Cordova/CDV.h>

@interface ADJCordovaTest : CDVPlugin

- (void)startTestSession:(CDVInvokedUrlCommand *)command;
- (void)addInfoToSend:(CDVInvokedUrlCommand *)command;
- (void)sendInfoToServer:(CDVInvokedUrlCommand *)command;
- (void)addTest:(CDVInvokedUrlCommand *)command;
- (void)addTestDirectory:(CDVInvokedUrlCommand *)command;
- (void)setTestConnectionOptions:(CDVInvokedUrlCommand *)command;

@end
