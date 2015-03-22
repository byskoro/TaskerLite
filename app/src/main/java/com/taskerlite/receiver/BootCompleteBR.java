package com.taskerlite.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.taskerlite.main.SchedulerLogic;
import com.taskerlite.source.Types.*;

public class BootCompleteBR extends BroadcastReceiver {

    public static boolean isBootComplete = false;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){

            SchedulerLogic.getInstance(context).checkForAction(TYPES.A_BOOT_COMPLETE);
        }
    }
}
