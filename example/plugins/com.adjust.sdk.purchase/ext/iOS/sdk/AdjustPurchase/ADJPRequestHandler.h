//
//  ADJPRequestHandler.h
//  AdjustPurchase
//
//  Created by Uglješa Erceg on 16/11/15.
//  Copyright © 2015 adjust GmbH. All rights reserved.
//

#import <Foundation/Foundation.h>

@class ADJPVerificationPackage;

@interface ADJPRequestHandler : NSObject

+ (void)sendURLRequestForPackage:(ADJPVerificationPackage *)package
                 responseHandler:(void (^)(NSDictionary *response, ADJPVerificationPackage *package))responseHandler;

@end
