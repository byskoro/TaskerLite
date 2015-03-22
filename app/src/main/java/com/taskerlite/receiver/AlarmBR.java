package com.taskerlite.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.taskerlite.main.SchedulerLogic;
import com.taskerlite.source.Types.*;

import java.util.Calendar;

public class AlarmBR extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TAG");

        wl.acquire();
        SchedulerLogic.getInstance(context).checkForAction(TYPES.A_TIME);
        setOnetimeTimer(context);
        wl.release();
    }


    public void CancelAlarm(Context context) {

        Intent intent = new Intent(context, AlarmBR.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }



    public void setOnetimeTimer(Context context) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBR.class);
        intent.putExtra("alarm", Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + generateOffsetTime(), pi);
    }

    private long generateOffsetTime(){

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 0);

        return cal.getTimeInMillis() - System.currentTimeMillis();
    }

    /*
     public void SetAlarm(Context context) {

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE); // Задаем параметр интента
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5, pi);
    }
    */

}
