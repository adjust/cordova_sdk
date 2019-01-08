//
//  ADJCommandListener.m
//  Adjust SDK
//
//  Created by Abdullah Obaied (@obaied) on 20th February 2018.
//  Copyright (c) 2018 Adjust GmbH. All rights reserved.
//

#import "ADJCommandListener.h"

@implementation ADJCommandListener {
    int orderCounter;
}

- (id)initWithCallbackId:(NSString *)callbackId andCommandDelegate:(id<CDVCommandDelegate>)commandDelegate {
    self = [super init];
    if (self == nil) {
        return nil;
    }

    orderCounter = 0;
    self.commandExecutorCallbackId = callbackId;
    self.commandDelegate = commandDelegate;

    return self;
}

- (void)executeCommandRawJson:(NSString *)json {
    NSError *jsonError;
    NSData *objectData = [json dataUsingEncoding:NSUTF8StringEncoding];
    NSMutableDictionary *dict = [NSJSONSerialization JSONObjectWithData:objectData
                                                                options:NSJSONReadingMutableContainers
                                                                  error:&jsonError];

    // Order of packages sent through PluginResult is not reliable, this is solved
    // through a scheduling mechanism in command_executor.js#scheduleCommand() side.
    // The 'order' entry is used to schedule commands.
    NSNumber *num = [NSNumber numberWithInt:orderCounter];
    [dict setObject:num forKey:@"order"];
    orderCounter++;

    // Making a JSON string from dictionary: this step is necessary, using 'messageAsDictionary' with 'resultWithStatus' method below
    // produced extra objects in the string that were not necessary.
    NSError *err;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dict options:0 error:&err];
    NSString *myString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];

    CDVPluginResult *pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:myString];
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:self.commandExecutorCallbackId];
}

@end
