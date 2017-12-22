package com.adjust.sdk.purchase;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by uerceg on 03/12/15.
 */
public class ADJPVerificationPackage {
    private HashMap<String, String> parameters;
    private OnADJPVerificationFinished callback;

    public ADJPVerificationPackage(HashMap<String, String> parameters,
                                   OnADJPVerificationFinished callback) {
        this.callback = callback;
        this.parameters = parameters;
    }

    public HashMap<String, String> getParameters() {
        return this.parameters;
    }

    public OnADJPVerificationFinished getCallback() {
        return this.callback;
    }

    public String getExtendedString() {
        StringBuilder builder = new StringBuilder();

        if (this.parameters != null) {
            builder.append("Purchase verification request parameters:");
            SortedMap<String, String> sortedParameters = new TreeMap<String, String>(parameters);

            for (Map.Entry<String, String> entry : sortedParameters.entrySet() ) {
                builder.append(String.format(Locale.US, "\n\t%-16s %s", entry.getKey(), entry.getValue()));
            }
        }

        return builder.toString();
    }
}
