package com.taskerlite.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootComplete extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            TimeNotification timeNotification = new TimeNotification();
            timeNotification.startNotify(context);
        }
    }
}
