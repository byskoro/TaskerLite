package com.taskerlite.logic.tasks;

import android.app.FragmentManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import com.taskerlite.R;
import com.taskerlite.other.Notification;
import com.taskerlite.other.Vibro;

public class tUnlockScreen extends mTask{

    public tUnlockScreen(Context context){
        setName(context.getResources().getString(R.string.t_unlock_screen_short));
    }

    @Override
    public void start(Context context) {

        Vibro.playLong(context);

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        if(!pm.isScreenOn()) {

            // wake up screen
            WakeLock wakeLock = pm.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
            wakeLock.acquire();
            wakeLock.release();

            // delete lock screen
            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            km.newKeyguardLock("myTag").disableKeyguard();
            km.exitKeyguardSecurely(null);
            km.newKeyguardLock("myTag").reenableKeyguard();
            km.exitKeyguardSecurely(null);

            //put notification
            String header = context.getResources().getString(R.string.t_unlock_screen_short);
            Notification.getInstance(context).createInfoNotification(header, "");
        }
    }

    @Override
    public void stop(Context context) { }
    @Override
    public void show(FragmentManager fm) { }
}
