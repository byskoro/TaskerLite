package com.taskerlite.main;

import java.util.Calendar;
import com.taskerlite.R;
import com.taskerlite.logic.actions.mAction;
import com.taskerlite.other.Flash;
import com.taskerlite.logic.ProfileController.*;
import com.taskerlite.logic.*;
import com.taskerlite.logic.tasks.mTask;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.taskerlite.main.Types.*;
import com.taskerlite.other.Vibro;

public class TService extends Service {

    private String previousRawData = "";
    private ProfileController profileController;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        runAsForeground();

        TimeSchedule ts = new TimeSchedule();
        ts.startNotify(getApplicationContext());

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public class TimeSchedule extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Vibro.playShort(getApplicationContext());

            if(!previousRawData.equals(Flash.getRawData())){

                profileController = Flash.getProfileList();
                previousRawData = Flash.getRawData();
            }

            for(Profile profile : profileController.getProfileList()){

                for(ActionElement action : profile.getActionList()){

                    mAction actionObj = action.getActionObject();

                    if(actionObj.isMyAction(context, TYPES.A_TIME)){

                        for(TaskElement task : profile.getTaskList()){

                            if(action.isTaskElementIdPresent(task.getTaskId())){

                                mTask taskObj = task.getTaskObject();
                                taskObj.start(context);
                            }
                        }
                    }
                }
            }

            startNotify(context);
        }

        public void startNotify(Context context) {

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, 1);
            cal.set(Calendar.SECOND, 0);

            //AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            //Intent intent = new Intent(context, TimeSchedule.class);
            //PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT );
            //am.cancel(pendingIntent);
            //am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }
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

    public static boolean isRunning(Context ctx) {

        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

            if (TService.class.getName().equals(service.service.getClassName()))
                return true;
        }
        return false;
    }
}