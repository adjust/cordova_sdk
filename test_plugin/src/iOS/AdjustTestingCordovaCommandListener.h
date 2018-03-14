//
//  AdjustTestingCordovaCommandListener.h
//  Adjust SDK
//
//  Created by Abdullah Obaied (@obaied) on 20th February 2018.
//  Copyright (c) 2012-2018 Adjust GmbH. All rights reserved.
//

#import <Cordova/CDV.h>
#import <Foundation/Foundation.h>
#import <AdjustTestLibrary/ATLTestLibrary.h>

@interface AdjustTestingCordovaCommandListener : NSObject<AdjustCommandDelegate>

@property (nonatomic, strong) NSString *commandExecutorCallbackId;
@property (nonatomic, strong) id<CDVCommandDelegate> commandDelegate;

- (id)initWithCallbackId:(NSString *)callbackId andCommandDelegate:(id<CDVCommandDelegate>)commandDelegate;

@end
