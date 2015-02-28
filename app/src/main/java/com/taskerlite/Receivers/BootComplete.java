package com.taskerlite.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.taskerlite.logic.ActionElement;
import com.taskerlite.logic.ProfileController;
import com.taskerlite.logic.TaskElement;
import com.taskerlite.logic.actions.mAction;
import com.taskerlite.logic.tasks.mTask;
import com.taskerlite.main.TService;
import com.taskerlite.main.Types;
import com.taskerlite.other.Flash;

public class BootComplete extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){

            try {

                ProfileController profileController = Flash.getProfileController();

                for(ProfileController.Profile profile : profileController.getProfileList()){

                    for(ActionElement action : profile.getActionList()){

                        mAction actionObj = action.getActionObject();

                        if(actionObj.isMyAction(context, Types.TYPES.A_BOOT_COMPLETE)){

                            for(TaskElement task : profile.getTaskList()){

                                if(action.isTaskElementIdPresent(task.getTaskId())){

                                    mTask taskObj = task.getTaskObject();
                                    taskObj.start(context);
                                }
                            }
                        }
                    }
                }

            }catch (Exception e) { }

            if(!TService.isRunning(context))
                context.startService(new Intent(context, TService.class));
        }
    }
}
