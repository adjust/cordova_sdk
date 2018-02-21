//
//  AdjustCordova.m
//  Adjust SDK
//
//  Created by Pedro Filipe (@nonelse) on 3rd April 2014.
//  Copyright (c) 2012-2017 Adjust GmbH. All rights reserved.
//

#import <Cordova/CDVPluginResult.h>

#import <AdjustTestLibrary/ATLTestLibrary.h>
#import "AdjustTestingCordova.h"
#import "AdjustTestingCordovaCommandListener.h"

@interface AdjustTestingCordova ()
@property (nonatomic, strong) ATLTestLibrary * testLibrary;
@property (nonatomic, strong) AdjustTestingCordovaCommandListener * adjustCommandListener;
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
    NSLog(@"startTestSession():");
    NSString *baseUrl = [command.arguments objectAtIndex:0];
    if (![self isFieldValid:baseUrl]) {
        return;
    }

    self.adjustCommandListener = [[AdjustTestingCordovaCommandListener alloc]
                                  initWithCallbackId:command.callbackId
                                  andCommandDelegate:self.commandDelegate];
    
    self.testLibrary = [ATLTestLibrary testLibraryWithBaseUrl:baseUrl
                                           andCommandDelegate:self.adjustCommandListener];
    
    for (id object in selectedTests) {
        [self.testLibrary addTest:object];
    }
    
    for (id object in selectedTestDirs) {
        [self.testLibrary addTestDirectory:object];
    }
    
    [self.testLibrary startTestSession:@"cordova4.12.4@ios4.12.2"];
}

- (void)addInfoToSend:(CDVInvokedUrlCommand *)command {
    NSString *key = [command.arguments objectAtIndex:0];
    NSString *value = [command.arguments objectAtIndex:1];
    
    if (self.testLibrary != nil) {
        NSLog(@"addInfoToSend(): with key %@ and %@", key, value);
        [self.testLibrary addInfoToSend:key value:value];
    }
}

- (void)sendInfoToServer:(CDVInvokedUrlCommand *)command {
    NSString *basePath = [command.arguments objectAtIndex:0];
    
    if (self.testLibrary != nil) {
        NSLog(@"sendInfoToServer(): with basePath %@", basePath);
        [self.testLibrary sendInfoToServer:basePath];
    }
}

- (void)addTest:(CDVInvokedUrlCommand *)command {
    NSString *testToAdd = [command.arguments objectAtIndex:0];
    NSLog(@"addtest(): %@", testToAdd);
    [selectedTests addObject:testToAdd];
}

- (void)addTestDirectory:(CDVInvokedUrlCommand *)command {
    NSString *testDirToAdd = [command.arguments objectAtIndex:0];
    NSLog(@"addtestDir(): %@", testDirToAdd);
    [selectedTestDirs addObject:testDirToAdd];
}

#pragma mark - Private & helper methods

- (BOOL)isFieldValid:(NSObject *)field {
    return field != nil && ![field isKindOfClass:[NSNull class]];
}

@end
