package com.adjust.sdk.purchase;

/**
 * Created by uerceg on 04/12/15.
 */
public class AdjustPurchase {
    private static ADJPMerchant defaultInstance;

    private AdjustPurchase() {
    }

    public static synchronized ADJPMerchant getDefaultInstance() {
        if (defaultInstance == null) {
            defaultInstance = new ADJPMerchant();
        }

        return defaultInstance;
    }

    public static void init(ADJPConfig config) {
        if (defaultInstance != null) {
            ADJPLogger.getInstance().error("AdjustPurchase SDK is already initialised");
            return;
        }

        // Check if config object was set properly.
        if (!config.isValid()) {
            return;
        }

        // Set log level.
        ADJPLogger.getInstance().setLogLevel(config.getLogLevel());

        // Config object set properly, initialize merchant.
        ADJPMerchant merchant = AdjustPurchase.getDefaultInstance();
        merchant.initialize(config);
    }

    public static void verifyPurchase(String itemSku, String itemToken, String developerPayload,
                                      OnADJPVerificationFinished callback) {
        // If there's no valid callback, don't process anything.
        if (callback == null) {
            ADJPLogger.getInstance().error("Invalid OnADJPVerificationFinished listener");
            return;
        }

        if (defaultInstance == null) {
            ADJPLogger.getInstance().error("AdjustPurchase SDK is not initialised");

            ADJPVerificationInfo info = new ADJPVerificationInfo();

            info.setStatusCode(ADJPConstants.STATUS_CODE_ERROR);
            info.setMessage("AdjustPurchase SDK is not initialised");
            info.setVerificationState(ADJPVerificationState.ADJPVerificationStateNotVerified);

            callback.onVerificationFinished(info);

            return;
        }

        // If merchant is not valid, check why config validation
        // failed and report that to responseBlock given by user.
        ADJPMerchant merchant = AdjustPurchase.getDefaultInstance();

        // Everything initialized properly, proceed with verification request.
        merchant.verifyPurchase(itemSku, itemToken, developerPayload, callback);
    }
}
