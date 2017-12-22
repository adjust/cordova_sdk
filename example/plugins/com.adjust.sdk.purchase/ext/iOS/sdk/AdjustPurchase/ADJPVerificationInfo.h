//
//  ADJPVerificationInfo.h
//  AdjustPurchase
//
//  Created by Uglješa Erceg on 09/11/15.
//  Copyright © 2015 adjust GmbH. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "ADJPCommon.h"

/**
 *  @brief  Object which is sent as response from AdjustPurchase module in
 *          adjustVerificationUpdate: method.
 */
@interface ADJPVerificationInfo : NSObject

/**
 *  @property   message
 *
 *  @brief      Text message about current state of receipt verification.
 */
@property (nonatomic, copy) NSString *message;

/**
 *  @property   statusCode
 *
 *  @brief      Status code returned from adjust backend server.
 */
@property (nonatomic, assign) int statusCode;

/**
 *  @property   verificationState
 *
 *  @brief      State of server side receipt verification.
 */
@property (nonatomic, assign) ADJPVerificationState verificationState;

/**
 *  @brief  Get verification state enumeration value as string.
 *
 *  @return Verification state value as string.
 */
- (NSString *)getVerificationStateAsString;

@end
