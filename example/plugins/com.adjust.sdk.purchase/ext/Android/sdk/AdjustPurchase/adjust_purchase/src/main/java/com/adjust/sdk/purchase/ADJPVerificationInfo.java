package com.adjust.sdk.purchase;

/**
 * Created by uerceg on 04/12/15.
 */

public class ADJPVerificationInfo {
    private String message;
    private Integer statusCode;
    private ADJPVerificationState verificationState;

    public ADJPVerificationInfo() {
    }

    public ADJPVerificationInfo(String message, Integer statusCode, ADJPVerificationState verificationState) {
        this.message = message;
        this.statusCode = statusCode;
        this.verificationState = verificationState;
    }

    public String getMessage() {
        return this.message;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public ADJPVerificationState getVerificationState() {
        return this.verificationState;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public void setVerificationState(ADJPVerificationState verificationState) {
        this.verificationState = verificationState;
    }
}
