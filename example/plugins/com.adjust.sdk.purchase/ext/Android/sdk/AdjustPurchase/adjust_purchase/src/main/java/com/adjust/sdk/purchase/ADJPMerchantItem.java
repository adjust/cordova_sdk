package com.adjust.sdk.purchase;

/**
 * Created by uerceg on 04/12/15.
 */
public class ADJPMerchantItem {
    private String itemSku;
    private String itemToken;
    private String developerPayload;
    private OnADJPVerificationFinished callback;

    public ADJPMerchantItem(String itemSku, String itemToken, String developerPayload,
                            OnADJPVerificationFinished callback) {
        this.itemSku = itemSku;
        this.itemToken = itemToken;
        this.developerPayload = developerPayload;
        this.callback = callback;
    }

    public String getItemSku() {
        return this.itemSku;
    }

    public String getItemToken() {
        return this.itemToken;
    }

    public String getDeveloperPayload() {
        return this.developerPayload;
    }

    public OnADJPVerificationFinished getCallback() {
        return this.callback;
    }

    public boolean isValid() {
        if (this.itemSku == null) {
            ADJPLogger.getInstance().error("SKU not set");
            return false;
        }

        if (this.itemSku.isEmpty()) {
            ADJPLogger.getInstance().error("SKU not valid");
            return false;
        }

        if (this.itemToken == null) {
            ADJPLogger.getInstance().error("Token not set");
            return false;
        }

        if (this.itemToken.isEmpty()) {
            ADJPLogger.getInstance().error("Token not valid");
            return false;
        }

        if (this.developerPayload == null) {
            ADJPLogger.getInstance().error("Developer payload not set");
            return false;
        }

        // Skip checking if developer payload is empty.
        // In case user isn't able to provide developer payload information.
        /*s
        if (this.developerPayload.isEmpty()) {
            ADJPLogger.getInstance().error("Developer payload not valid");
            return false;
        }
        */

        return true;
    }

    public boolean isValid(String errorMessage) {
        if (this.itemSku == null) {
            errorMessage = "SKU value can't be null";
            ADJPLogger.getInstance().error(errorMessage);
            return false;
        }

        if (this.itemSku.isEmpty()) {
            errorMessage = "SKU value can't be empty string";
            ADJPLogger.getInstance().error(errorMessage);
            return false;
        }

        if (this.itemToken == null) {
            errorMessage = "Token value can't be null";
            ADJPLogger.getInstance().error(errorMessage);
            return false;
        }

        if (this.itemToken.isEmpty()) {
            errorMessage = "Token value can't be empty string";
            ADJPLogger.getInstance().error(errorMessage);
            return false;
        }

        if (this.developerPayload == null) {
            errorMessage = "Developer payload value can't be null";
            ADJPLogger.getInstance().error(errorMessage);
            return false;
        }

        // Developer payload not tested for empty string value like explained in isValid() method.

        return true;
    }
}
