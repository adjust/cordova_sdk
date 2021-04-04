#import "AdjustCordovaDelegate.h"

@interface EagerAdjustBuffer : NSObject

+ (id)getInstance;
- (void)addPluginResult:(CDVPluginResult*)result andCallbackId:(NSString *)callbackId;
- (void)replayResultsWithCommand:(id <CDVCommandDelegate>) commandDelegate;

@end
