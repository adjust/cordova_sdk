//
//  ADJPMerchantItem.m
//  AdjustPurchase
//
//  Created by Uglješa Erceg on 05/11/15.
//  Copyright © 2015 adjust GmbH. All rights reserved.
//

#import "ADJPLogger.h"
#import "ADJPMerchantItem.h"

#pragma mark - Implementation

@implementation ADJPMerchantItem

#pragma mark - Object lifecycle

- (id)initWithReceipt:(NSData *)receipt
        transactionId:(NSString *)transactionId
            productId:(NSString *)productId
     andResponseBlock:(ADJPVerificationAnswerBlock)responseBlock {
    self = [super init];

    if (!self) {
        return self;
    }

    _receipt = receipt;
    _productId = productId;
    _transactionId = transactionId;
    _responseBlock = responseBlock;

    return self;
}

#pragma mark - Public methods

- (BOOL)isValid:(NSString *)errorMessage {
    NSString *message;

    if (self.receipt == nil) {
        message = @"Receipt can't be nil";
        [ADJPLogger error:message];

        if (errorMessage != nil) {
            errorMessage = message;
        }

        return NO;
    }

    if (self.transactionId == nil) {
        message = @"Transaction ID can't be nil";
        [ADJPLogger error:message];

        if (errorMessage != nil) {
            errorMessage = message;
        }

        return NO;
    }

    if (self.productId == nil) {
        message = @"Product ID can't be nil";
        [ADJPLogger error:message];

        if (errorMessage != nil) {
            errorMessage = message;
        }

        return NO;
    }

    // No need to check responseBlock, was already checked.
    
    return YES;
}

@end
