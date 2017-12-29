//
//  AdjustPurchaseStatus.m
//  AdjustPurchase
//
//  Created by Uglješa Erceg on 09/11/15.
//  Copyright © 2015 adjust GmbH. All rights reserved.
//

#import "ADJPVerificationInfo.h"

@implementation ADJPVerificationInfo

#pragma mark - Public methods

- (NSString *)getVerificationStateAsString {
    switch (self.verificationState) {
        case ADJPVerificationStateFailed:
            return @"ADJPVerificationStateFailed";
        case ADJPVerificationStatePassed:
            return @"ADJPVerificationStatePassed";
        case ADJPVerificationStateUnknown:
            return @"ADJPVerificationStateUnknown";
        case ADJPVerificationStateNotVerified:
            return @"ADJPVerificationStateNotVerified";
        default:
            return @"Unknown value";
    }
}

@end
