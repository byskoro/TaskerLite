package com.taskerlite.logic.tasks;

import android.app.FragmentManager;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import com.taskerlite.R;
import com.taskerlite.source.Notification;

public class tScreen extends mTask{

    public tScreen(Context context){
        setName(context.getResources().getString(R.string.t_screen_short));
    }

    @Override
    public void start(Context context) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        wakeLock.acquire();
        wakeLock.release();

        String header = context.getResources().getString(R.string.t_screen_short);
        Notification.getInstance(context).createInfoNotification(header, "");
    }

    @Override
    public void stop(Context context) {

    }
    @Override
    public void show(FragmentManager fm) {

    }
}
