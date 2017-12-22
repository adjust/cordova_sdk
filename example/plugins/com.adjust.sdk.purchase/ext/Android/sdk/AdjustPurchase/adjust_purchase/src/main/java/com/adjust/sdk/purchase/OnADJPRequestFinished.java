package com.adjust.sdk.purchase;

import java.util.Map;

/**
 * Created by uerceg on 04/12/15.
 */
public interface OnADJPRequestFinished {
    void requestFinished(Map<String, Object> response, ADJPVerificationPackage verificationPackage);
}
