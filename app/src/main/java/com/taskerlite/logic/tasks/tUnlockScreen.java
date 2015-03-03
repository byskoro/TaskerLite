package com.taskerlite.logic.tasks;

import android.app.FragmentManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import com.taskerlite.R;
import com.taskerlite.source.Notification;
import com.taskerlite.source.Vibro;

public class tUnlockScreen extends mTask{

    private boolean state = true;

    public tUnlockScreen(Context context){
        setName(context.getResources().getString(R.string.t_unlock_screen_short));
    }

    @Override
    public void start(Context context) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        if(state) {

            // wake up screen
            WakeLock wakeLock = pm.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
            wakeLock.acquire();
            wakeLock.release();

            //put notification
            String header = context.getResources().getString(R.string.t_unlock_screen_short);
            Notification.getInstance(context).createInfoNotification(header, "");

        } else
            stop(context);
    }

    @Override
    public void stop(Context context) {



    }
    @Override
    public void show(FragmentManager fm) { }
}
