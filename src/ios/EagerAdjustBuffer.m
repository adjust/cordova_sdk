#import "EagerAdjustBuffer.h"


@implementation EagerAdjustBuffer

static EagerAdjustBuffer *defaultInstance = nil;
static dispatch_once_t onceToken;
NSMutableDictionary<NSString*, NSMutableArray*> *results;

+ (id)getInstance {
  dispatch_once(&onceToken, ^{
    defaultInstance = [[EagerAdjustBuffer alloc] init];
    results = [NSMutableDictionary new];
  });
  
  return defaultInstance;
}


- (void)addPluginResult:(CDVPluginResult*)result andCallbackId:(NSString *)callbackId {
  NSMutableArray *callBackResults = [results objectForKey:callbackId];
  if(callBackResults != nil) {
    [callBackResults addObject:result];
  } else {
    callBackResults = [NSMutableArray arrayWithObject:result];
  }
  [results setObject:callBackResults forKey:callbackId];
}

- (void)replayResultsWithCommand:(id <CDVCommandDelegate>) commandDelegate {
  for(NSString *callbackId in results.allKeys) {
    NSMutableArray *callBackResults = [results objectForKey:callbackId];
    for (CDVPluginResult *result in callBackResults) {
      [commandDelegate sendPluginResult:result callbackId:callbackId];
    }
    [results removeObjectForKey:callbackId];
  }
 
}

@end
