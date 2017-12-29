//
//  ADJPMerchant.m
//  AdjustPurchase
//
//  Created by Uglješa Erceg on 05/11/15.
//  Copyright © 2015 adjust GmbH. All rights reserved.
//

#import "ADJPLogger.h"
#import "ADJPConfig.h"
#import "ADJPMerchant.h"
#import "ADJPMerchantItem.h"
#import "ADJPRequestHandler.h"
#import "ADJPVerificationInfo.h"
#import "ADJPVerificationPackage.h"

static NSString *kSdkVersion            = @"ios_purchase1.0.0";
static NSString *kReceiptRegexPattern   = @"^[A-Za-z0-9+/]*=?=?$";
static const char* kInternalQueueName   = "io.adjust.PurchaseQueue";

@interface ADJPMerchant()

@property (nonatomic) BOOL isVerificationInProgress;

@property (nonatomic) NSMutableArray *items;
@property (nonatomic) dispatch_queue_t internalQueue;
@property (nonatomic) NSRegularExpression *receiptRegex;

@property (nonatomic) ADJPConfig *config;

@end

@implementation ADJPMerchant

#pragma mark - Object lifecycle

- (id)initWithConfig:(ADJPConfig *)config {
    self = [super init];

    if (!self) {
        return self;
    }

    self.config = config;
    self.isVerificationInProgress = NO;

    self.internalQueue = dispatch_queue_create(kInternalQueueName, DISPATCH_QUEUE_SERIAL);

    dispatch_async(self.internalQueue, ^{
        self.items = [[NSMutableArray alloc] init];
    });

    NSError *error = NULL;
    NSRegularExpression *regex = [NSRegularExpression regularExpressionWithPattern:kReceiptRegexPattern
                                                                           options:NSRegularExpressionCaseInsensitive
                                                                             error:&error];

    if (error != nil) {
        [ADJPLogger error:@"Receipt link regex rule error (%@)", [error description]];
    } else {
        self.receiptRegex = regex;
    }

    return self;
}

#pragma mark - Public methods

- (void)verifyPurchase:(NSData *)receipt
        forTransaction:(SKPaymentTransaction *)transaction
             productId:(NSString *)productId
     withResponseBlock:(ADJPVerificationAnswerBlock)responseBlock {
    dispatch_async(self.internalQueue, ^{
        ADJPMerchantItem *item = [[ADJPMerchantItem alloc] initWithReceipt:receipt
                                                             transactionId:transaction.transactionIdentifier
                                                                 productId:productId
                                                          andResponseBlock:responseBlock];
        NSString *errorMessage;

        // Check validity of receipt and transaction object.
        if (![item isValid:errorMessage]) {
            ADJPVerificationInfo *info = [[ADJPVerificationInfo alloc] init];

            info.statusCode = -1;
            info.message = errorMessage;
            info.verificationState = ADJPVerificationStateNotVerified;

            responseBlock(info);

            return;
        }

        [self asyncAddItem:item];
        [self asyncProcessItem];
    });
}

- (void)verifyPurchase:(NSData *)receipt
      forTransactionId:(NSString *)transactionId
             productId:(NSString *)productId
     withResponseBlock:(ADJPVerificationAnswerBlock)responseBlock {
    dispatch_async(self.internalQueue, ^{
        ADJPMerchantItem *item = [[ADJPMerchantItem alloc] initWithReceipt:receipt
                                                             transactionId:transactionId
                                                                 productId:productId
                                                          andResponseBlock:responseBlock];
        NSString *errorMessage;

        // Check validity of receipt and transaction object.
        if (![item isValid:errorMessage]) {
            ADJPVerificationInfo *info = [[ADJPVerificationInfo alloc] init];

            info.statusCode = -1;
            info.message = errorMessage;
            info.verificationState = ADJPVerificationStateNotVerified;

            responseBlock(info);

            return;
        }

        [self asyncAddItem:item];
        [self asyncProcessItem];
    });
}

#pragma mark - Private methods

- (void)asyncAddItem:(ADJPMerchantItem *)item {
    [self.items addObject:item];
}

- (void)asyncProcessItem {
    if (self.isVerificationInProgress == YES) {
        return;
    }

    if ([self.items count] <= 0) {
        return;
    }

    self.isVerificationInProgress = YES;

    ADJPMerchantItem *currentItem = [self.items objectAtIndex:0];

    [self verifyReceipt:currentItem.receipt
       forTransactionId:currentItem.transactionId
              productId:currentItem.productId
      withResponseBlock:currentItem.responseBlock];
}

- (void)asyncRemoveItem {
    if ([self.items count] <= 0) {
        return;
    }

    [self.items removeObjectAtIndex:0];

    self.isVerificationInProgress = NO;

    [self asyncProcessItem];
}

- (void)verifyReceipt:(NSData *)receipt
     forTransactionId:(NSString *)transactionId
            productId:(NSString *)productId
    withResponseBlock:(ADJPVerificationAnswerBlock)responseBlock {
    [ADJPLogger debug:@"Sending verification request to adjust backend"];

    NSMutableDictionary *parameters = [[NSMutableDictionary alloc] init];

    if (self.config.sdkPrefix == nil) {
        [self parameters:parameters setString:kSdkVersion forKey:@"sdk_version"];
    } else {
        [self parameters:parameters setString:[NSString stringWithFormat:@"%@@%@", self.config.sdkPrefix, kSdkVersion] forKey:@"sdk_version"];
    }

    [self parameters:parameters setString:self.config.appToken forKey:@"app_token"];
    [self parameters:parameters setString:self.config.environment forKey:@"environment"];
    [self parameters:parameters setString:transactionId forKey:@"transaction_id"];
    [self parameters:parameters setString:productId forKey:@"product_id"];
    [self parameters:parameters setString:[self getEncodedReceipt:receipt] forKey:@"receipt"];

    ADJPVerificationPackage *verificationPackage = [[ADJPVerificationPackage alloc] init];
    verificationPackage.parameters = parameters;
    verificationPackage.responseBlock = responseBlock;

    [ADJPRequestHandler sendURLRequestForPackage:verificationPackage
                                 responseHandler:^(NSDictionary *response, ADJPVerificationPackage *package) {
                                     [self processResponse:response forVerificationPackage:package];
                                 }];
}

- (void)parameters:(NSMutableDictionary *)parameters setString:(NSString *)value forKey:(NSString *)key {
    if (value == nil || [value isEqualToString:@""]) {
        return;
    }

    [parameters setObject:value forKey:key];
}

- (void)processResponse:(NSDictionary *)response forVerificationPackage:(ADJPVerificationPackage *)package {
    [ADJPLogger debug:@"Response to verification request arrived from adjust backend"];

    ADJPVerificationInfo *info = [[ADJPVerificationInfo alloc] init];

    if (response == nil) {
        info.verificationState = ADJPVerificationStateUnknown;
        info.message = @"No response from server";
    } else {
        NSString *message = [response objectForKey:@"adjust_message"];
        int statusCode = [(NSNumber *)[response objectForKey:@"adjust_status_code"] intValue];
        ADJPVerificationState state = (ADJPVerificationState)[(NSNumber *)[response objectForKey:@"adjust_state"] intValue];

        info.statusCode = statusCode;
        info.verificationState = state;
        info.message = message;
    }

    [ADJPLogger debug:info.message];
    package.responseBlock(info);

    dispatch_async(self.internalQueue, ^{
        [self asyncRemoveItem];
    });
}

- (NSString *)getEncodedReceipt:(NSData *)receipt {
    NSString *stringReceipt = [[NSString alloc] initWithData:receipt encoding:NSUTF8StringEncoding];

    if (stringReceipt != nil) {
        NSArray<NSTextCheckingResult *> *matches = [self.receiptRegex matchesInString:stringReceipt options:0 range:NSMakeRange(0, [stringReceipt length])];

        if ([matches count] == 0) {
            [ADJPLogger debug:@"Receipt is not base64 encoded"];
            return [receipt base64EncodedStringWithOptions:0];
        }

        [ADJPLogger debug:@"Receipt is base64 encoded"];

        return stringReceipt;
    }

    [ADJPLogger debug:@"Receipt is not base64 encoded"];

    return [receipt base64EncodedStringWithOptions:0];
}

@end
