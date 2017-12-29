package com.adjust.sdk.purchase;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by uerceg on 04/12/15.
 */
public class ADJPMerchant extends HandlerThread implements OnADJPRequestFinished {
    private ADJPConfig config;
    private InternalHandler internalHandler;
    private ADJPRequestHandler requestWorker;

    public ADJPMerchant() {
        super(ADJPConstants.TAG, MIN_PRIORITY);
        setDaemon(true);
        start();

        this.requestWorker = new ADJPRequestHandler(this);
        this.internalHandler = new InternalHandler(getLooper(), this);
    }

    public void initialize(ADJPConfig config) {
        this.config = config;
    }

    public void verifyPurchase(String itemSku, String itemToken, String developerPayload,
                               OnADJPVerificationFinished callback) {
        String errorMessage = null;
        ADJPMerchantItem item = new ADJPMerchantItem(itemSku, itemToken, developerPayload, callback);

        if (!item.isValid(errorMessage)) {
            if (item.getCallback() != null) {
                ADJPVerificationInfo info = new ADJPVerificationInfo();

                info.setMessage(errorMessage);
                info.setStatusCode(ADJPConstants.STATUS_CODE_ERROR);
                info.setVerificationState(ADJPVerificationState.ADJPVerificationStateNotVerified);

                item.getCallback().onVerificationFinished(info);
            }

            return;
        }

        Message message = Message.obtain();
        message.arg1 = InternalHandler.VERIFY;
        message.obj = item;
        internalHandler.sendMessage(message);
    }

    @Override
    public void requestFinished(Map<String, Object> response, final ADJPVerificationPackage verificationPackage) {
        String message = (String)response.get(ADJPConstants.KEY_ADJUST_MESSAGE);
        Integer statusCode = (Integer)response.get(ADJPConstants.KEY_ADJUST_STATUS_CODE);
        ADJPVerificationState verificationState = (ADJPVerificationState)response.get(ADJPConstants.KEY_ADJUST_STATE);

        final ADJPVerificationInfo status = new ADJPVerificationInfo(message, statusCode, verificationState);

        // Execute callback on main thread.
        Handler mainHandler = new Handler(Looper.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                verificationPackage.getCallback().onVerificationFinished(status);
            }
        };

        mainHandler.post(myRunnable);
    }

    private static final class InternalHandler extends Handler {
        private static final int VERIFY = 72400;
        private final WeakReference<ADJPMerchant> merchantReference;

        protected InternalHandler(Looper looper, ADJPMerchant merchant) {
            super(looper);
            this.merchantReference = new WeakReference<ADJPMerchant>(merchant);
        }

        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);

            ADJPMerchant merchant = merchantReference.get();

            if (merchant == null) {
                return;
            }

            switch (message.arg1) {
                case VERIFY:
                    ADJPMerchantItem item = (ADJPMerchantItem) message.obj;
                    merchant.verifyInternal(item);
                    break;
                default:
                    break;
            }
        }
    }

    private void verifyInternal(ADJPMerchantItem item) {
        HashMap<String, String> parameters = new HashMap<String, String>();
        addString(parameters, ADJPConstants.KEY_SDK_VERSION, this.config.getClientSdk());
        addString(parameters, ADJPConstants.KEY_APP_TOKEN, this.config.getAppToken());
        addString(parameters, ADJPConstants.KEY_ENVIRONMENT, this.config.getEnvironment());
        addString(parameters, ADJPConstants.KEY_GPS_PRODUCT_ID, item.getItemSku());
        addString(parameters, ADJPConstants.KEY_GPS_PURCHASE_TOKEN, item.getItemToken());
        addString(parameters, ADJPConstants.KEY_GPS_DEVELOPER_PAYLOAD, item.getDeveloperPayload());

        ADJPVerificationPackage verificationPackage = new ADJPVerificationPackage(parameters, item.getCallback());
        this.requestWorker.sendPackage(verificationPackage);
    }

    private void addString(Map<String, String> parameters, String key, String value) {
        if (TextUtils.isEmpty(value)) {
            return;
        }

        parameters.put(key, value);
    }
}
