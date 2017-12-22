//
//  ADJPMerchant.h
//  AdjustPurchase
//
//  Created by Uglješa Erceg on 05/11/15.
//  Copyright © 2015 adjust GmbH. All rights reserved.
//

#import <StoreKit/StoreKit.h>
#import <Foundation/Foundation.h>

@class ADJPConfig;
@class ADJPVerificationInfo;

@interface ADJPMerchant : NSObject

- (id)initWithConfig:(ADJPConfig *)config;
- (void)verifyPurchase:(NSData *)receipt
        forTransaction:(SKPaymentTransaction *)transaction
             productId:(NSString *)productId
     withResponseBlock:(ADJPVerificationAnswerBlock)responseBlock;
- (void)verifyPurchase:(NSData *)receipt
      forTransactionId:(NSString *)transactionId
             productId:(NSString *)productId
     withResponseBlock:(ADJPVerificationAnswerBlock)responseBlock;

@end
