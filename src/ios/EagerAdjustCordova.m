#import "EagerAdjustCordova.h"
#import "EagerAdjustBuffer.h"

#define KEY_APP_TOKEN @"appToken"
#define KEY_ENVIRONMENT @"environment"

@implementation EagerAdjustCordova

- (void)pluginInitialize {
  [super pluginInitialize];
  [self initializeEagerly];
}

- (void)initCallbacks {
  [self setAttributionCallback:[[CDVInvokedUrlCommand alloc] initWithArguments:@[] callbackId:@"setAttributionCallback" className:@"EagerAdjustCordova" methodName:@"setAttributionCallback"]];
  [self setEventTrackingSucceededCallback:[[CDVInvokedUrlCommand alloc] initWithArguments:@[] callbackId:@"setEventTrackingSucceededCallback" className:@"EagerAdjustCordova" methodName:@"setEventTrackingSucceededCallback"]];
  [self setEventTrackingFailedCallback:[[CDVInvokedUrlCommand alloc] initWithArguments:@[] callbackId:@"setEventTrackingFailedCallback" className:@"EagerAdjustCordova" methodName:@"setEventTrackingFailedCallback"]];
  [self setSessionTrackingSucceededCallback:[[CDVInvokedUrlCommand alloc] initWithArguments:@[] callbackId:@"setSessionTrackingSucceededCallback" className:@"EagerAdjustCordova" methodName:@"setSessionTrackingSucceededCallback"]];
  [self setSessionTrackingFailedCallback:[[CDVInvokedUrlCommand alloc] initWithArguments:@[] callbackId:@"setSessionTrackingFailedCallback" className:@"EagerAdjustCordova" methodName:@"setSessionTrackingFailedCallback"]];
  [self setDeferredDeeplinkCallback:[[CDVInvokedUrlCommand alloc] initWithArguments:@[] callbackId:@"setDeferredDeeplinkCallback" className:@"EagerAdjustCordova" methodName:@"setDeferredDeeplinkCallback"]];
}

- (void)initializeEagerly {
  [self initCallbacks];
  if([self.appDelegate respondsToSelector:@selector(getAdjustToken)] && [self.appDelegate respondsToSelector:@selector(getEnvironment)]) {
    
    NSString *adjustKey = [self.appDelegate performSelector:@selector(getAdjustToken)];
    NSString *environment = [self.appDelegate performSelector:@selector(getEnvironment)];
  
    NSArray *commandArray = @[[self createCommandWithKey:adjustKey andEnvironment:environment]];
    CDVInvokedUrlCommand *command = [[CDVInvokedUrlCommand alloc] initWithArguments:commandArray callbackId:nil className:nil methodName:nil];
    [super create:command];
  }
}

- (NSString *)createCommandWithKey:(NSString *)adjustKey andEnvironment:(NSString *)environment {
  
  NSDictionary *dic = @{KEY_APP_TOKEN: adjustKey,
                        KEY_ENVIRONMENT : environment
  };
  
  NSData *data = [NSJSONSerialization dataWithJSONObject:@[dic] options:NSJSONWritingPrettyPrinted error:nil];
  NSString *jsonString = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
  return jsonString;
}

- (void)setAttributionCallback:(CDVInvokedUrlCommand *)command {
  [[EagerAdjustBuffer getInstance] replayResultsWithCommand:self.commandDelegate];
  [super setAttributionCallback:command];
}

- (void)setEventTrackingSucceededCallback:(CDVInvokedUrlCommand *)command {
  [[EagerAdjustBuffer getInstance] replayResultsWithCommand:self.commandDelegate];
  [super setEventTrackingSucceededCallback:command];
}

- (void)setEventTrackingFailedCallback:(CDVInvokedUrlCommand *)command {
  [[EagerAdjustBuffer getInstance] replayResultsWithCommand:self.commandDelegate];
  [super setEventTrackingFailedCallback:command];
}

- (void)setSessionTrackingSucceededCallback:(CDVInvokedUrlCommand *)command {
  [[EagerAdjustBuffer getInstance] replayResultsWithCommand:self.commandDelegate];
  [super setSessionTrackingSucceededCallback:command];
}

- (void)setSessionTrackingFailedCallback:(CDVInvokedUrlCommand *)command {
  [[EagerAdjustBuffer getInstance] replayResultsWithCommand:self.commandDelegate];
  [super setSessionTrackingFailedCallback:command];
}

- (void)setDeferredDeeplinkCallback:(CDVInvokedUrlCommand *)command {
  [[EagerAdjustBuffer getInstance] replayResultsWithCommand:self.commandDelegate];
  [super setDeferredDeeplinkCallback:command];
}

@end
