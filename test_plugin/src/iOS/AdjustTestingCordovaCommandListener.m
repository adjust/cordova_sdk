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
    
    // Order of packages sent through PluginResult is not reliable, this is solved
    //  through a scheduling mechanism in command_executor.js#scheduleCommand() side.
    // The 'order' entry is used to schedule commands
    NSNumber *_num = [NSNumber numberWithInt:orderCounter];
    [dict setObject:_num forKey:@"order"];
    orderCounter++;
    
    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:dict];
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.commandExecutorCallbackId];
}

@end
