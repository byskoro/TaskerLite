package com.taskerlite.main;

import java.util.Calendar;

import com.taskerlite.logic.actions.mAction;
import com.taskerlite.other.Flash;
import com.taskerlite.logic.ProfileController.*;
import com.taskerlite.logic.*;
import com.taskerlite.logic.tasks.mTask;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.taskerlite.main.TaskerTypes.*;

public class TService extends Service {

	private ProfileController profileController;
    private String previousRawData = "";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
    	serviceHandler.sendEmptyMessageDelayed(0, generateOffsetTime());
    	
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler serviceHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	
        	try {
				
        		if(!previousRawData.equals(Flash.getRawData())){
        			
            		profileController = Flash.getProfileList();
                    previousRawData   = Flash.getRawData();
            	}
            	
            	for(Profile profile : profileController.getProfileList()){
            		
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

        return cal.getTimeInMillis() - System.currentTimeMillis();
    }
}
