//
//  AdjustTestingCordova.m
//  Adjust SDK
//
//  Created by Abdullah Obaied (@obaied) on 20th February 2018.
//  Copyright (c) 2012-2018 Adjust GmbH. All rights reserved.
//

#import <Cordova/CDVPluginResult.h>
#import <AdjustTestLibrary/ATLTestLibrary.h>
#import "AdjustTestingCordova.h"
#import "AdjustTestingCordovaCommandListener.h"

@interface AdjustTestingCordova ()

@property (nonatomic, strong) ATLTestLibrary *testLibrary;
@property (nonatomic, strong) AdjustTestingCordovaCommandListener *adjustCommandListener;

@end

@implementation AdjustTestingCordova {
    NSMutableArray *selectedTests;
    NSMutableArray *selectedTestDirs;
}

#pragma mark - Object lifecycle methods

- (void)pluginInitialize {
    selectedTests = [NSMutableArray array];
    selectedTestDirs = [NSMutableArray array];
}

#pragma mark - Public methods

- (void)startTestSession:(CDVInvokedUrlCommand *)command {
    NSString *baseUrl = [command.arguments objectAtIndex:0];
    if (![self isFieldValid:baseUrl]) {
        return;
    }

    self.adjustCommandListener = [[AdjustTestingCordovaCommandListener alloc] initWithCallbackId:command.callbackId
                                                                              andCommandDelegate:self.commandDelegate];
    self.testLibrary = [ATLTestLibrary testLibraryWithBaseUrl:baseUrl
                                           andCommandDelegate:self.adjustCommandListener];

    for (id object in selectedTests) {
        [self.testLibrary addTest:object];
    }
    for (id object in selectedTestDirs) {
        [self.testLibrary addTestDirectory:object];
    }

    [self.testLibrary startTestSession:@"cordova4.14.0@ios4.14.1"];
}

- (void)addInfoToSend:(CDVInvokedUrlCommand *)command {
    NSString *key = [command.arguments objectAtIndex:0];
    NSString *value = [command.arguments objectAtIndex:1];
    if (self.testLibrary != nil) {
        [self.testLibrary addInfoToSend:key value:value];
    }
}

- (void)sendInfoToServer:(CDVInvokedUrlCommand *)command {
    NSString *basePath = [command.arguments objectAtIndex:0];
    if (self.testLibrary != nil) {
        [self.testLibrary sendInfoToServer:basePath];
    }
}

- (void)addTest:(CDVInvokedUrlCommand *)command {
    NSString *testToAdd = [command.arguments objectAtIndex:0];
    [selectedTests addObject:testToAdd];
}

- (void)addTestDirectory:(CDVInvokedUrlCommand *)command {
    NSString *testDirToAdd = [command.arguments objectAtIndex:0];
    [selectedTestDirs addObject:testDirToAdd];
}

#pragma mark - Private & helper methods

- (BOOL)isFieldValid:(NSObject *)field {
    return field != nil && ![field isKindOfClass:[NSNull class]];
}

@end
