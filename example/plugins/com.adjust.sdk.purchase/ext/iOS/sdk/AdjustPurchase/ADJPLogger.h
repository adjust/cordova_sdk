//
//  ADJP.h
//  AdjustPurchase
//
//  Created by Uglješa Erceg on 24/11/15.
//  Copyright © 2015 adjust GmbH. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ADJPCommon.h"

@interface ADJPLogger : NSObject

+ (id)getInstance;

+ (void)verbose:(NSString *)message, ...;
+ (void)debug:  (NSString *)message, ...;
+ (void)info:   (NSString *)message, ...;
+ (void)warn:   (NSString *)message, ...;
+ (void)error:  (NSString *)message, ...;
+ (void)assert: (NSString *)message, ...;

+ (void)setLogLevel:(ADJPLogLevel)logLevel;

@end
