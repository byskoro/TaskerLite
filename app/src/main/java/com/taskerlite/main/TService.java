package com.taskerlite.main;

import java.util.Calendar;

import com.taskerlite.R;
import com.taskerlite.logic.actions.mAction;
import com.taskerlite.other.Flash;
import com.taskerlite.logic.ProfileController.*;
import com.taskerlite.logic.*;
import com.taskerlite.logic.tasks.mTask;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.taskerlite.main.Types.*;

public class TService extends Service {

    private ProfileController sceneList;
    private String previousRawData = "";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        runAsForeground();

        serviceHandler.sendEmptyMessageDelayed(0, generateOffsetTime());

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void runAsForeground(){

        Intent notificationIntent = new Intent(this, mActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification=new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(getString(R.string.app_name))
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.app_open))
                .setContentIntent(pendingIntent).build();

        startForeground(1, notification);
    }

    Handler serviceHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {



            try {

                if(!previousRawData.equals(Flash.getRawData())){

                    sceneList = Flash.getProfileList();
                    previousRawData = Flash.getRawData();
                }

                for(Profile profile : sceneList.getProfileList()){

                    for(ActionElement action : profile.getActionList()){

                        mAction actionObj = action.getActionObject();

                        if(actionObj.isMyAction(getApplicationContext(), TYPES.A_TIME)){

                            for(TaskElement task : profile.getTaskList()){

                                if(action.isTaskElementIdPresent(task.getTaskId())){

                                    mTask taskObj = task.getTaskObject();
                                    taskObj.start(getApplicationContext());
                                }
                            }
                        }
                    }
                }

            } catch (Exception e) { }

            serviceHandler.sendEmptyMessageDelayed(0, generateOffsetTime());
        };
    };

    public static boolean isRunning(Context ctx) {

        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

            if (TService.class.getName().equals(service.service.getClassName()))
                return true;
        }
        return false;
    }

    private long generateOffsetTime(){

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 1);
        cal.set(Calendar.SECOND, 0);

        return 3000; //cal.getTimeInMillis() - System.currentTimeMillis();
    }
}