package com.adjust.sdk.purchase;

import android.util.Log;

import java.util.Arrays;
import java.util.Locale;

/**
 * Created by uerceg on 04/12/15.
 */
public class ADJPLogger {
    private static String formatErrorMessage = "Error formatting log message: %s, with params: %s";

    private ADJPLogLevel logLevel;
    private static ADJPLogger instance = null;

    private ADJPLogger() {
        setLogLevel(ADJPLogLevel.INFO);
    }

    public static ADJPLogger getInstance() {
        if (instance == null) {
            instance = new ADJPLogger();
        }

        return instance;
    }

    public void setLogLevel(ADJPLogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public void setLogLevelString(String logLevelString) {
        if (null == logLevelString) {
            return;
        }

        try {
            setLogLevel(ADJPLogLevel.valueOf(logLevelString.toUpperCase(Locale.US)));
        } catch (IllegalArgumentException iae) {
            error("Malformed logLevel '%s', falling back to 'info'", logLevelString);
        }
    }

    public void verbose(String message, Object... parameters) {
        if (logLevel.getAndroidLogLevel() > Log.VERBOSE) {
            return;
        }

        try {
            Log.v(ADJPConstants.TAG, String.format(Locale.US, message, parameters));
        } catch (Exception e) {
            Log.e(ADJPConstants.TAG, String.format(Locale.US, formatErrorMessage, message, Arrays.toString(parameters)));
        }
    }

    public void debug(String message, Object... parameters) {
        if (logLevel.getAndroidLogLevel() > Log.DEBUG) {
            return;
        }

        try {
            Log.d(ADJPConstants.TAG, String.format(Locale.US, message, parameters));
        } catch (Exception e) {
            Log.e(ADJPConstants.TAG, String.format(Locale.US, formatErrorMessage, message, Arrays.toString(parameters)));
        }
    }

    public void info(String message, Object... parameters) {
        if (logLevel.getAndroidLogLevel() > Log.INFO) {
            return;
        }

        try {
            Log.i(ADJPConstants.TAG, String.format(Locale.US, message, parameters));
        } catch (Exception e) {
            Log.e(ADJPConstants.TAG, String.format(Locale.US, formatErrorMessage, message, Arrays.toString(parameters)));
        }
    }

    public void warn(String message, Object... parameters) {
        if (logLevel.getAndroidLogLevel() > Log.WARN) {
            return;
        }

        try {
            Log.w(ADJPConstants.TAG, String.format(Locale.US, message, parameters));
        } catch (Exception e) {
            Log.e(ADJPConstants.TAG, String.format(Locale.US, formatErrorMessage, message, Arrays.toString(parameters)));
        }
    }

    public void error(String message, Object... parameters) {
        if (logLevel.getAndroidLogLevel() > Log.ERROR) {
            return;
        }

        try {
            Log.e(ADJPConstants.TAG, String.format(Locale.US, message, parameters));
        } catch (Exception e) {
            Log.e(ADJPConstants.TAG, String.format(Locale.US, formatErrorMessage, message, Arrays.toString(parameters)));
        }
    }

    public void Assert(String message, Object... parameters) {
        if (logLevel.getAndroidLogLevel() > Log.ASSERT) {
            return;
        }

        try {
            Log.println(Log.ASSERT, ADJPConstants.TAG, String.format(Locale.US, message, parameters));
        } catch (Exception e) {
            Log.e(ADJPConstants.TAG, String.format(Locale.US, formatErrorMessage, message, Arrays.toString(parameters)));
        }
    }
}
