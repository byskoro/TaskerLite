package com.taskerlite.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootComplete extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){

            if(!TService.isRunning(context))
                context.startService(new Intent(context, TService.class));
        }
    }
}
