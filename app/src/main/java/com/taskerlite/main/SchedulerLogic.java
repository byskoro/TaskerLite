package com.taskerlite.main;

import android.content.Context;
import android.os.PowerManager;

import com.taskerlite.logic.ActionElement;
import com.taskerlite.logic.ProfileController;
import com.taskerlite.logic.TaskElement;
import com.taskerlite.logic.actions.mAction;
import com.taskerlite.logic.tasks.mTask;
import com.taskerlite.source.Settings;
import com.taskerlite.source.Types;

public class SchedulerLogic {

    private static SchedulerLogic instance = null;

    private String previousRawData = "";
    private ProfileController profileController;
    private Settings settings;
    private Context context;

    public static SchedulerLogic getInstance(Context context){

        if(instance == null)
            instance = new SchedulerLogic(context);

        return instance;
    }

    private SchedulerLogic (Context context){

        this.context = context;
        settings = Settings.getInstance(context);
    }


    public void checkForAction(Types.TYPES type){

        if(!previousRawData.equals(settings.getRawData())){

            profileController = settings.getProfileController();
            previousRawData   = settings.getRawData();
        }

        for(ProfileController.Profile profile : profileController.getProfileList()){

            for(ActionElement action : profile.getActionList()){

                mAction actionObj = action.getActionObject();

                if(actionObj.isMyAction(context, type)){

                    for(TaskElement task : profile.getTaskList()){

                        if(action.isTaskElementIdPresent(task.getTaskId())){

                            mTask taskObj = task.getTaskObject();
                            taskObj.start(context);
                        }
                    }
                }
            }
        }
    }



}
