//
//  ADJPCommon.h
//  AdjustPurchase
//
//  Created by Uglješa Erceg on 07/12/15.
//  Copyright © 2015 adjust GmbH. All rights reserved.
//

#import <Foundation/Foundation.h>

@class ADJPVerificationInfo;

/**
 *  @brief  Environment variables.
 *          We use this environment to distinguish between real traffic 
 *          and artificial traffic from test devices.
 *          It is very important that you keep this value meaningful at all times!
 *          Please use ADJPEnvironmentSandbox while TESTING.
 *          Please switch to ADJPEnvironmentProduction before you RELEASE your app.
 */
extern NSString * const ADJPEnvironmentSandbox;
extern NSString * const ADJPEnvironmentProduction;

/**
 *  @brief  Block typedef which will receive verification response info.
 */
typedef void(^ADJPVerificationAnswerBlock)(ADJPVerificationInfo *info);

/**
 *  @brief  Possible ADJPLogger log levels.
 */
typedef enum {
    ADJPLogLevelVerbose     = 1,
    ADJPLogLevelDebug       = 2,
    ADJPLogLevelInfo        = 3,
    ADJPLogLevelWarn        = 4,
    ADJPLogLevelError       = 5,
    ADJPLogLevelAssert      = 6,
    ADJPLogLevelNone        = 7
} ADJPLogLevel;

/**
 *  @brief  Possible states reported to adjustVerificationUpdate: method.
 */
typedef enum {
    ADJPVerificationStatePassed         = 0,
    ADJPVerificationStateFailed         = 1,
    ADJPVerificationStateUnknown        = 2,
    ADJPVerificationStateNotVerified    = 3
} ADJPVerificationState;
