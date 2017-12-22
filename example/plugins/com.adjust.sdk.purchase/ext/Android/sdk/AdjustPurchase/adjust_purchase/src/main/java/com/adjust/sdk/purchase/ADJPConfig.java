package com.adjust.sdk.purchase;

import java.util.Locale;

/**
 * Created by uerceg on 09/12/15.
 */
public class ADJPConfig {
    private String appToken;
    private String sdkPrefix;
    private String environment;
    private ADJPLogLevel logLevel;

    public ADJPConfig(String appToken, String environment) {
        this.appToken = appToken;
        this.environment = environment;
        this.logLevel = ADJPLogLevel.INFO;
    }

    public String getAppToken() {
        return this.appToken;
    }

    public String getEnvironment() {
        return this.environment;
    }

    public String getClientSdk() {
        if (this.sdkPrefix == null) {
            return ADJPConstants.SDK_VERSION;
        } else {
            return String.format(Locale.US, "%s@%s", sdkPrefix, ADJPConstants.SDK_VERSION);
        }
    }

    public ADJPLogLevel getLogLevel() {
        return this.logLevel;
    }

    public void setSdkPrefix(String sdkPrefix) {
        this.sdkPrefix = sdkPrefix;
    }

    public void setLogLevel(ADJPLogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public boolean isValid() {
        if (this.appToken == null) {
            ADJPLogger.getInstance().error("Invalid app token");
            return false;
        }

        if (this.appToken.length() != ADJPConstants.APP_TOKEN_SIZE) {
            ADJPLogger.getInstance().error("Invalid app token");
            return false;
        }

        if (this.environment == null) {
            ADJPLogger.getInstance().error("Invalid environment");
            return false;
        }

        if (this.environment.equalsIgnoreCase(ADJPConstants.ENVIRONMENT_SANDBOX) == false &&
                this.environment.equalsIgnoreCase(ADJPConstants.ENVIRONMENT_PRODUCTION) == false) {
            ADJPLogger.getInstance().error("Invalid environment");
            return false;
        }

        if (this.environment.equalsIgnoreCase(ADJPConstants.ENVIRONMENT_SANDBOX)) {
            ADJPLogger.getInstance().Assert("SANDBOX: AdjustPurchase is running in sandbox mode. " +
                    "Use this setting for testing. " +
                    "Don't forget to set the environment to `production` before publishing!");
            return true;
        }

        if (this.environment.equalsIgnoreCase(ADJPConstants.ENVIRONMENT_PRODUCTION)) {
            ADJPLogger.getInstance().Assert("PRODUCTION: AdjustPurchase is running in production mode. " +
                    "Use this setting only for the build that you want to publish. " +
                    "Set the environment to `sandbox` if you want to test your app!");
            return true;
        }

        return true;
    }
}
