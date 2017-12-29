//
//  AdjustPurchase.m
//  AdjustPurchase
//
//  Created by Uglješa Erceg on 13/11/15.
//  Copyright © 2015 adjust GmbH. All rights reserved.
//

#import "ADJPLogger.h"
#import "ADJPMerchant.h"
#import "AdjustPurchase.h"

@interface AdjustPurchase()

@property (nonatomic, retain) NSString *errorMessage;
@property (nonatomic, retain) ADJPMerchant *merchant;

@end

@implementation AdjustPurchase

#pragma mark - Public methods

+ (void)init:(ADJPConfig *)config {
    [[AdjustPurchase getInstance] init:config];
}

+ (void)verifyPurchase:(NSData *)receipt
        forTransaction:(id)transaction
             productId:(NSString *)productId
     withResponseBlock:(ADJPVerificationAnswerBlock)responseBlock {
    [[AdjustPurchase getInstance] verifyPurchase:receipt forTransaction:transaction productId:productId withResponseBlock:responseBlock];
}

+ (void)verifyPurchase:(NSData *)receipt
      forTransactionId:(NSString *)transactionId
             productId:(NSString *)productId
     withResponseBlock:(ADJPVerificationAnswerBlock)responseBlock {
    [[AdjustPurchase getInstance] verifyPurchase:receipt forTransactionId:transactionId productId:productId withResponseBlock:responseBlock];
}

#pragma mark - Private methods

+ (id)getInstance {
    static AdjustPurchase *defaultInstance = nil;
    static dispatch_once_t onceToken;

    dispatch_once(&onceToken, ^{
        defaultInstance = [[self alloc] init];
    });

    return defaultInstance;
}

- (void)init:(ADJPConfig *)config {
    // Check if config object was set properly.
    if (![config isValid:self.errorMessage]) {
        return;
    }

    // Set log level.
    [ADJPLogger setLogLevel:config.logLevel];

    // Config object set properly, initialize merchant.
    self.merchant = [[ADJPMerchant alloc] initWithConfig:config];
}

- (void)verifyPurchase:(NSData *)receipt
        forTransaction:(id)transaction
             productId:(NSString *)productId
     withResponseBlock:(ADJPVerificationAnswerBlock)responseBlock {
    // If response block is not valid, ignore everything.
    if (responseBlock == nil) {
        [ADJPLogger error:@"Invalid response block"];
        return;
    }

    // If merchant is not valid, check why config verification
    // failed and report that to responseBlock given by user.
    if (!self.merchant) {
        ADJPVerificationInfo *info = [[ADJPVerificationInfo alloc] init];

        info.statusCode = -1;
        info.message = self.errorMessage;
        info.verificationState = ADJPVerificationStateNotVerified;

        responseBlock(info);

        return;
    }

    // Everything initialized properly, proceed with verification request.
    [self.merchant verifyPurchase:receipt forTransaction:transaction productId:productId withResponseBlock:responseBlock];
}

- (void)verifyPurchase:(NSData *)receipt
      forTransactionId:(NSString *)transactionId
             productId:(NSString *)productId
     withResponseBlock:(ADJPVerificationAnswerBlock)responseBlock {
    // If response block is not valid, ignore everything.
    if (responseBlock == nil) {
        [ADJPLogger error:@"Invalid response block"];
        return;
    }

    // If merchant is not valid, check why config verification
    // failed and report that to responseBlock given by user.
    if (!self.merchant) {
        ADJPVerificationInfo *info = [[ADJPVerificationInfo alloc] init];

        info.statusCode = -1;
        info.message = self.errorMessage;
        info.verificationState = ADJPVerificationStateNotVerified;

        responseBlock(info);

        return;
    }

    // Everything initialized properly, proceed with verification request.
    [self.merchant verifyPurchase:receipt forTransactionId:transactionId productId:productId withResponseBlock:responseBlock];
}

@end
