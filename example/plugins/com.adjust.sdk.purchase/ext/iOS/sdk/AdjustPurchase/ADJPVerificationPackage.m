//
//  ADJPVerificationPackage.m
//  AdjustPurchase
//
//  Created by Uglješa Erceg on 13/11/15.
//  Copyright © 2015 adjust GmbH. All rights reserved.
//

#import "ADJPVerificationPackage.h"

@implementation ADJPVerificationPackage

- (NSString *)extendedString {
    NSMutableString *builder = [NSMutableString string];

    if (self.parameters != nil) {
        NSArray *sortedKeys = [[self.parameters allKeys] sortedArrayUsingSelector:@selector(localizedStandardCompare:)];
        NSUInteger keyCount = [sortedKeys count];

        [builder appendFormat:@"Purchase verification request parameters:"];

        for (int i = 0; i < keyCount; i++) {
            NSString *key = (NSString *)[sortedKeys objectAtIndex:i];
            NSString *value = [self.parameters objectForKey:key];

            [builder appendFormat:@"\n\t\t%-22s %@", [key UTF8String], value];
        }
    }

    return builder;
}

@end
