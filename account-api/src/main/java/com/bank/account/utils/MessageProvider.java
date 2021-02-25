package com.bank.account.utils;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public final class MessageProvider {

    private static final ResourceBundle messageBundle;

    static {
        messageBundle = ResourceBundle.getBundle("messages");
    }

    public static String getString(String key) {
        return messageBundle.getString(key);
    }

    public static String formatString(String key, Object... arguments) {
        return MessageFormat.format(getString(key), arguments);
    }
}