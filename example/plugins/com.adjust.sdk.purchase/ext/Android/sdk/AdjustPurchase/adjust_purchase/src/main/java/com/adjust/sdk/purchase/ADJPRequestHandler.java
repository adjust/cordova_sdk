package com.adjust.sdk.purchase;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.text.SimpleDateFormat;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by uerceg on 03/12/15.
 */
public class ADJPRequestHandler extends HandlerThread {
    private InternalHandler internalHandler;
    private OnADJPRequestFinished listener;

    public ADJPRequestHandler(OnADJPRequestFinished listener) {
        super(ADJPConstants.TAG, MIN_PRIORITY);
        setDaemon(true);
        start();

        this.listener = listener;
        this.internalHandler = new InternalHandler(getLooper(), this);
    }

    public void sendPackage(ADJPVerificationPackage verificationPackage) {
        Message message = Message.obtain();
        message.arg1 = InternalHandler.SEND;
        message.obj = verificationPackage;
        internalHandler.sendMessage(message);
    }

    private void sendURLRequest(ADJPVerificationPackage verificationPackage) {
        try {
            ADJPLogger.getInstance().verbose(verificationPackage.getExtendedString());

            HttpsURLConnection connection = createPOSTHttpsURLConnection(
                    ADJPConstants.BASE_URL,
                    verificationPackage.getParameters());

            Map<String, Object> response = readHttpsResponse(connection);
            this.listener.requestFinished(response, verificationPackage);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<String, Object>();
            response.put(ADJPConstants.KEY_ADJUST_MESSAGE, "Error while making URL request");
            response.put(ADJPConstants.KEY_ADJUST_STATUS_CODE, ADJPConstants.STATUS_CODE_ERROR);
            response.put(ADJPConstants.KEY_ADJUST_STATE, ADJPVerificationState.ADJPVerificationStateNotVerified);

            ADJPLogger.getInstance().error("Error while making URL request");
            ADJPLogger.getInstance().error(e.getMessage());

            this.listener.requestFinished(response, verificationPackage);
        }
    }

    private static final class InternalHandler extends Handler {
        private static final int SEND = 72400;
        private final WeakReference<ADJPRequestHandler> requestWorkerReference;

        protected InternalHandler(Looper looper, ADJPRequestHandler requestWorker) {
            super(looper);
            this.requestWorkerReference = new WeakReference<ADJPRequestHandler>(requestWorker);
        }

        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            ADJPRequestHandler requestWorker = requestWorkerReference.get();

            if (requestWorker == null) {
                return;
            }

            switch (message.arg1) {
                case SEND:
                    ADJPVerificationPackage verificationPackage = (ADJPVerificationPackage) message.obj;
                    requestWorker.sendInternal(verificationPackage);
                    break;
            }
        }
    }

    private void sendInternal(ADJPVerificationPackage verificationPackage) {
        sendURLRequest(verificationPackage);
    }

    private HttpsURLConnection createPOSTHttpsURLConnection(String urlString, Map<String, String> parameters)
            throws IOException {
        HttpsURLConnection connection = createHttpsURLConnection(urlString);
        connection.setRequestMethod("POST");

        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(getPostDataString(parameters));
        wr.flush();
        wr.close();

        return connection;
    }

    public static HttpsURLConnection createHttpsURLConnection(String urlString) throws IOException {
        URL url = new URL(urlString);

        HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();

        connection.setConnectTimeout(ADJPConstants.TIMEOUT);
        connection.setReadTimeout(ADJPConstants.TIMEOUT);

        return connection;
    }

    private static String getPostDataString(Map<String, String> body) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : body.entrySet()) {
            String encodedName = URLEncoder.encode(entry.getKey(), ADJPConstants.ENCODING);
            String value = entry.getValue();
            String encodedValue = value != null ? URLEncoder.encode(value, ADJPConstants.ENCODING) : "";

            if (result.length() > 0) {
                result.append("&");
            }

            result.append(encodedName);
            result.append("=");
            result.append(encodedValue);
        }

        long now = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat(ADJPConstants.DATE_FORMAT, Locale.US);
        String dateString = dateFormat.format(now);

        result.append("&");
        result.append(URLEncoder.encode("sent_at", ADJPConstants.ENCODING));
        result.append("=");
        result.append(URLEncoder.encode(dateString, ADJPConstants.ENCODING));

        return result.toString();
    }

    private Map<String, Object> readHttpsResponse(HttpsURLConnection connection) throws Exception {
        Integer statusCode;
        Map<String, Object> response = new HashMap<String, Object>();

        try {
            String message = null;
            StringBuffer sb = new StringBuffer();
            statusCode = connection.getResponseCode();

            try {
                String line;
                InputStream inputStream;

                if (statusCode >= 400) {
                    inputStream = connection.getErrorStream();
                } else {
                    inputStream = connection.getInputStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                message = sb.toString();
            } catch (Exception e) {
                // Unable to get message from backend.
            }

            if (statusCode == 200) {
                // All good.
                response.put(ADJPConstants.KEY_ADJUST_MESSAGE, message);
                response.put(ADJPConstants.KEY_ADJUST_STATUS_CODE, statusCode);
                response.put(ADJPConstants.KEY_ADJUST_STATE, ADJPVerificationState.ADJPVerificationStatePassed);

                ADJPLogger.getInstance().info(message);
            } else if (statusCode == 204) {
                // No idea. No response from Google servers.
                response.put(ADJPConstants.KEY_ADJUST_MESSAGE, "Verification state unknown");
                response.put(ADJPConstants.KEY_ADJUST_STATUS_CODE, statusCode);
                response.put(ADJPConstants.KEY_ADJUST_STATE, ADJPVerificationState.ADJPVerificationStateUnknown);

                ADJPLogger.getInstance().info("Verification state unknown");
            } else if (statusCode == 406) {
                // Not valid.
                response.put(ADJPConstants.KEY_ADJUST_MESSAGE, message);
                response.put(ADJPConstants.KEY_ADJUST_STATUS_CODE, statusCode);
                response.put(ADJPConstants.KEY_ADJUST_STATE, ADJPVerificationState.ADJPVerificationStateFailed);

                ADJPLogger.getInstance().info(message);
            } else {
                // Spread the word from the backend.
                response.put(ADJPConstants.KEY_ADJUST_MESSAGE, message);
                response.put(ADJPConstants.KEY_ADJUST_STATUS_CODE, statusCode);
                response.put(ADJPConstants.KEY_ADJUST_STATE, ADJPVerificationState.ADJPVerificationStateUnknown);

                ADJPLogger.getInstance().info(message);
            }
        } catch (Exception e) {
            ADJPLogger.getInstance().error("Failed to read response. (%s)", e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return response;
    }
}
