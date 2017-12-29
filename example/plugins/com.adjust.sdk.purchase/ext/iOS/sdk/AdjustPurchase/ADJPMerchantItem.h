//
//  ADJPMerchantItem.h
//  AdjustPurchase
//
//  Created by Uglješa Erceg on 05/11/15.
//  Copyright © 2015 adjust GmbH. All rights reserved.
//

#import <StoreKit/StoreKit.h>
#import <Foundation/Foundation.h>

#import "ADJPCommon.h"

@interface ADJPMerchantItem : NSObject

@property (nonatomic, readonly) NSData *receipt;
@property (nonatomic, readonly) NSString *productId;
@property (nonatomic, readonly) NSString *transactionId;
@property (nonatomic, readonly) ADJPVerificationAnswerBlock responseBlock;

- (id)initWithReceipt:(NSData *)receipt
        transactionId:(NSString *)transactionId
            productId:(NSString *)productId
     andResponseBlock:(ADJPVerificationAnswerBlock)responseBlock;

- (BOOL)isValid:(NSString *)errorMessage;

@end
