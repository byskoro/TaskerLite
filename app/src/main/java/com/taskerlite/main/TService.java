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
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.taskerlite.main.Types.*;

public class TService extends Service {

    private ProfileController sceneList;
    private String previousRawData = "";

    ToneGenerator toneGenerator;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        toneGenerator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 0);

        runAsForeground();

        serviceHandler.sendEmptyMessageDelayed(0, generateOffsetTime());

        return Service.START_STICKY;
    }

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
        builder.setSmallIcon(android.R.drawable.ic_menu_mylocation);
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

    Handler serviceHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            pleaseDontKillMe();

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

    private void pleaseDontKillMe(){

        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 0);
    }

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

        return cal.getTimeInMillis() - System.currentTimeMillis();
    }
}