//
//  AdjustTestingCordovaCommandListener.h
//  HelloCordova
//
//  Created by Abdullah Obaied on 20.02.18.
//

#import <Foundation/Foundation.h>
#import <AdjustTestLibrary/ATLTestLibrary.h>
#import <Cordova/CDV.h>

@interface AdjustTestingCordovaCommandListener : NSObject<AdjustCommandDelegate>

@property (nonatomic, strong) NSString *commandExecutorCallbackId;
@property (nonatomic, strong) id<CDVCommandDelegate> commandDelegate;
- (id)initWithCallbackId:(NSString *)_callbackId andCommandDelegate:(id<CDVCommandDelegate>) _commandDelegate;

@end
