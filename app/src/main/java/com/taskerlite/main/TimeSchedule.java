package com.taskerlite.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import com.taskerlite.logic.ProfileController;
import com.taskerlite.other.Flash;
import com.taskerlite.logic.actions.mAction;
import com.taskerlite.logic.ProfileController.*;
import com.taskerlite.logic.*;
import com.taskerlite.logic.tasks.mTask;
import com.taskerlite.main.TaskerTypes.*;

public class TimeSchedule extends BroadcastReceiver {

    private static ProfileController profileController;
    private static String previousRawData = "";

    @Override
    public void onReceive(Context context, Intent intent) {

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

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TimeSchedule.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT );
        am.cancel(pendingIntent);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }
}
