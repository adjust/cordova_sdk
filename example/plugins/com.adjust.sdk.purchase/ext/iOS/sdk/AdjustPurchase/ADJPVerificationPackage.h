//
//  ADJPVerificationPackage.h
//  AdjustPurchase
//
//  Created by Uglješa Erceg on 13/11/15.
//  Copyright © 2015 adjust GmbH. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "ADJPCommon.h"

@interface ADJPVerificationPackage : NSObject

@property (nonatomic, retain) NSDictionary *parameters;
@property (nonatomic, weak) ADJPVerificationAnswerBlock responseBlock;

- (NSString *)extendedString;

@end
