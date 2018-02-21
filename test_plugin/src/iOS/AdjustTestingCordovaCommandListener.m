//
//  AdjustTestingCordovaCommandListener.m
//  HelloCordova
//
//  Created by Abdullah Obaied on 20.02.18.
//

#import <Foundation/Foundation.h>
#import "AdjustTestingCordovaCommandListener.h"
#import <Cordova/CDV.h>

@interface AdjustTestingCordovaCommandListener ()

@end

@implementation AdjustTestingCordovaCommandListener {
    int orderCounter;
}

- (id)initWithCallbackId:(NSString *)_callbackId andCommandDelegate:(id<CDVCommandDelegate>) _commandDelegate {
    self = [super init];
    if (self == nil) {
        return nil;
    }
    
    self.commandExecutorCallbackId = _callbackId;
    self.commandDelegate = _commandDelegate;
    orderCounter = 0;
    
    return self;
}

- (void)executeCommandRawJson:(NSString *)json {
    NSLog(@"executeCommandRawJson: %@", json);
    
    NSError *jsonError;
    NSData *objectData = [json dataUsingEncoding:NSUTF8StringEncoding];
    NSMutableDictionary *dict = [NSJSONSerialization JSONObjectWithData:objectData
                                                         options:NSJSONReadingMutableContainers
                                                           error:&jsonError];
    
    NSNumber *_num = [NSNumber numberWithInt:orderCounter];
    [dict setObject:_num forKey:@"order"];
    orderCounter++;
    
    NSError * err;
    NSData * jsonData = [NSJSONSerialization dataWithJSONObject:dict options:0 error:&err];
    NSString * myString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:myString];
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.commandExecutorCallbackId];
}

@end
