//
//  ADJPRequestHandler.m
//  AdjustPurchase
//
//  Created by Uglješa Erceg on 16/11/15.
//  Copyright © 2015 adjust GmbH. All rights reserved.
//

#import "ADJPCommon.h"
#import "ADJPLogger.h"
#import "ADJPRequestHandler.h"
#import "ADJPVerificationInfo.h"
#import "ADJPVerificationPackage.h"

static const double kRequestTimeout     = 5;
static NSString * const kBaseUrl        = @"https://ssrv.adjust.com/verify";

@implementation ADJPRequestHandler

#pragma mark - Public methods

+ (void)sendURLRequestForPackage:(ADJPVerificationPackage *)package
                 responseHandler:(void (^)(NSDictionary *response, ADJPVerificationPackage *package))responseHandler {
    [ADJPLogger verbose:[package extendedString]];

    Class NSURLSessionClass = NSClassFromString(@"NSURLSession");

    if (NSURLSessionClass != nil) {
        [self sendNSURLSessionRequest:[self requestForPackage:package]
                      responseHandler:^(NSDictionary *response) {
                          responseHandler(response, package);
                      }];
    } else {
        [self sendNSURLConnectionRequest:[self requestForPackage:package]
                         responseHandler:^(NSDictionary *response) {
                             responseHandler(response, package);
                         }];
    }
}

#pragma mark - Private methods

+ (NSMutableURLRequest *)requestForPackage:(ADJPVerificationPackage *)package {
    NSURL *url = [NSURL URLWithString:kBaseUrl];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:url];
    request.timeoutInterval = kRequestTimeout;
    request.HTTPMethod = @"POST";

    [request setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request setHTTPBody:[self bodyForParameters:package.parameters]];

    return request;
}

+ (NSData *)bodyForParameters:(NSDictionary *)parameters {
    NSString *bodyString = [self queryString:parameters];
    NSData *body = [NSData dataWithBytes:bodyString.UTF8String length:bodyString.length];

    return body;
}

+ (NSString *)queryString:(NSDictionary *)parameters {
    NSMutableArray *pairs = [NSMutableArray array];

    for (NSString *key in parameters) {
        NSString *value         = [parameters objectForKey:key];
        NSString *escapedKey    = [self urlEncode:key];
        NSString *escapedValue  = [self urlEncode:value];
        NSString *pair          = [NSString stringWithFormat:@"%@=%@", escapedKey, escapedValue];

        [pairs addObject:pair];
    }

    NSString *dateString    = [self getDateString];
    NSString *escapedDate   = [self urlEncode:dateString];
    NSString *sentAtPair    = [NSString stringWithFormat:@"%@=%@", @"sent_at", escapedDate];

    [pairs addObject:sentAtPair];

    NSString *queryString = [pairs componentsJoinedByString:@"&"];

    return queryString;
}

+ (NSString *)getDateString {
    NSString *kDateFormat = @"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'Z";
    NSDateFormatter *dateFormat = [[NSDateFormatter alloc] init];

    if ([NSCalendar instancesRespondToSelector:@selector(calendarWithIdentifier:)]) {
        // http://stackoverflow.com/a/3339787
        NSString *calendarIdentifier;

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wtautological-pointer-compare"
        if (&NSCalendarIdentifierGregorian != NULL) {
#pragma clang diagnostic pop
            calendarIdentifier = NSCalendarIdentifierGregorian;
        } else {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunreachable-code"
#pragma clang diagnostic ignored "-Wdeprecated-declarations"
            calendarIdentifier = NSGregorianCalendar;
#pragma clang diagnostic pop
#pragma clang diagnostic pop
        }

        dateFormat.calendar = [NSCalendar calendarWithIdentifier:calendarIdentifier];
    }

    dateFormat.locale = [NSLocale systemLocale];
    [dateFormat setDateFormat:kDateFormat];

    double now = [NSDate.date timeIntervalSince1970];
    NSDate *date = [NSDate dateWithTimeIntervalSince1970:now];

    return [dateFormat stringFromDate:date];
}

+ (NSString *)urlEncode:(NSString *)stringToEncode {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wdeprecated-declarations"
    return (NSString *)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(NULL,
                                                                                 (CFStringRef)stringToEncode,
                                                                                 NULL,
                                                                                 (CFStringRef)@"!*'\"();:@&=+$,/?%#[]% ",
                                                                                 CFStringConvertNSStringEncodingToEncoding(NSUTF8StringEncoding)));
#pragma clang diagnostic pop
}

+ (void)sendNSURLSessionRequest:(NSMutableURLRequest *)request
                responseHandler:(void (^)(NSDictionary *dictionary))responseHandler {
    NSURLSession *session = [NSURLSession sharedSession];
    NSURLSessionDataTask *task = [session dataTaskWithRequest:request
                                            completionHandler:
                                  ^(NSData *data, NSURLResponse *urlResponse, NSError *error) {
                                      NSDictionary *response = [self completionHandler:data
                                                                              response:(NSHTTPURLResponse *)urlResponse
                                                                                 error:error];
                                      responseHandler(response);
                                  }];
    [task resume];
}

+ (void)sendNSURLConnectionRequest:(NSMutableURLRequest *)request
                   responseHandler:(void (^)(NSDictionary *dictionary))responseHandler {
    NSError *responseError = nil;
    NSHTTPURLResponse *urlResponse = nil;

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wdeprecated-declarations"
    NSData *responseData = [NSURLConnection sendSynchronousRequest:request
                                                 returningResponse:&urlResponse
                                                             error:&responseError];
#pragma clang diagnostic pop

    NSDictionary *response = [self completionHandler:responseData
                                            response:(NSHTTPURLResponse *)urlResponse
                                               error:responseError];

    responseHandler(response);
}

+ (NSDictionary *)completionHandler:(NSData *)responseData
                           response:(NSHTTPURLResponse *)urlResponse
                              error:(NSError *)responseError {
    NSMutableDictionary *response = [[NSMutableDictionary alloc] init];

    // Connection error.
    if (responseError != nil) {
        [ADJPLogger error:[responseError description]];

        [response setObject:[NSNumber numberWithInt:ADJPVerificationStateUnknown] forKey:@"adjust_state"];
        [response setObject:[responseError localizedDescription] forKey:@"adjust_message"];

        if (urlResponse) {
            [response setObject:[NSNumber numberWithInteger:[urlResponse statusCode]] forKey:@"adjust_status_code"];
        } else {
            [response setObject:[NSNumber numberWithInt:-1] forKey:@"adjust_status_code"];
        }

        return response;
    }

    // Check status code and determine verification state based on it.
    if (urlResponse) {
        NSUInteger statusCode = [urlResponse statusCode];
        NSString *message = [[[NSString alloc] initWithData:responseData encoding:NSUTF8StringEncoding]
                                    stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];

        if (statusCode == 200) {
            // All good.
            [response setObject:message forKey:@"adjust_message"];
            [response setObject:[NSNumber numberWithInteger:statusCode] forKey:@"adjust_status_code"];
            [response setObject:[NSNumber numberWithInt:ADJPVerificationStatePassed] forKey:@"adjust_state"];
        } else if (statusCode == 204) {
            // No idea. No response from Apple servers.
            [response setObject:@"Verification state unknown" forKey:@"adjust_message"];
            [response setObject:[NSNumber numberWithInteger:statusCode] forKey:@"adjust_status_code"];
            [response setObject:[NSNumber numberWithInt:ADJPVerificationStateUnknown] forKey:@"adjust_state"];
        } else if (statusCode == 406) {
            // Not valid.
            [response setObject:message forKey:@"adjust_message"];
            [response setObject:[NSNumber numberWithInteger:statusCode] forKey:@"adjust_status_code"];
            [response setObject:[NSNumber numberWithInt:ADJPVerificationStateFailed] forKey:@"adjust_state"];
        } else {
            // Spread the word from the backend.
            [response setObject:message forKey:@"adjust_message"];
            [response setObject:[NSNumber numberWithInteger:statusCode] forKey:@"adjust_status_code"];
            [response setObject:[NSNumber numberWithInt:ADJPVerificationStateUnknown] forKey:@"adjust_state"];
        }
    } else {
        [response setObject:@"URL response did not arrive" forKey:@"adjust_message"];
        [response setObject:[NSNumber numberWithInt:-1] forKey:@"adjust_status_code"];
        [response setObject:[NSNumber numberWithInteger:ADJPVerificationStateUnknown] forKey:@"adjust_state"];
    }

    return response;
}

+ (void)printRequest:(NSURLRequest *)request {
    NSMutableString *message = [NSMutableString stringWithString:@""];
    [message appendString:@"\n------------------REQUEST------------------\n"];
    [message appendFormat:@"URL: %@\n",[request.URL description] ];
    [message appendFormat:@"METHOD: %@\n",[request HTTPMethod]];

    for (NSString *header in [request allHTTPHeaderFields]) {
        [message appendFormat:@"%@: %@\n",header,[request valueForHTTPHeaderField:header]];
    }

    [message appendFormat:@"BODY: %@\n",[[NSString alloc] initWithData:[request HTTPBody] encoding:NSUTF8StringEncoding]];
    [message appendString:@"-------------------------------------------\n"];
    
    NSLog(@"%@", message);
}

@end
