package com.taskerlite.main;

import java.util.Calendar;
import com.taskerlite.R;
import com.taskerlite.logic.actions.mAction;
import com.taskerlite.receiver.BootCompleteBR;
import com.taskerlite.source.Settings;
import com.taskerlite.logic.ProfileController.*;
import com.taskerlite.logic.*;
import com.taskerlite.logic.tasks.mTask;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import com.taskerlite.source.Types.*;

public class TService extends Service {

    SchedulerLogic schedulerLogic;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        schedulerLogic = SchedulerLogic.getInstance(getApplicationContext());

        registerBroadcastReceivers();
        runAsForeground();

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getApplicationContext().unregisterReceiver(screenStateReceiver);
    }

    private void registerBroadcastReceivers(){

        IntentFilter theFilter = new IntentFilter();

        // Screen on/off
        theFilter.addAction(Intent.ACTION_SCREEN_ON);
        theFilter.addAction(Intent.ACTION_SCREEN_OFF);
        getApplicationContext().registerReceiver(screenStateReceiver, theFilter);
    }

    BroadcastReceiver screenStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String strAction = intent.getAction();

            if (strAction.equals(Intent.ACTION_SCREEN_OFF))
                schedulerLogic.checkForAction(TYPES.A_SCREEN_OFF);
            else if(strAction.equals(Intent.ACTION_SCREEN_ON))
                schedulerLogic.checkForAction(TYPES.A_SCREEN_ON);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void runAsForeground(){

        Intent main = new Intent(this, mActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, main,  PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText(getString(R.string.app_open));
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentIntent(pendingIntent);
        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification = builder.build();
        } else {
            notification = builder.getNotification();
        }
        notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_NO_CLEAR;

        int uniqueId = (int)System.currentTimeMillis();
        startForeground(uniqueId, notification);
    }

    public static boolean isRunning(Context ctx) {

        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

            if (TService.class.getName().equals(service.service.getClassName()))
                return true;
        }
        return false;
    }
}