package com.taskerlite.main;

import java.util.Calendar;

import com.taskerlite.logic.actions.mAction;
import com.taskerlite.other.Flash;
import com.taskerlite.logic.SceneList.*;
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

	public static boolean newDataCome = false;
	private SceneList sceneList;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

    	newDataCome = true;
        
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
				
        		if(newDataCome == true){
        			
            		sceneList = Flash.getList();
            		newDataCome = false;
            	}
            	
            	for(Scene scene : sceneList.getSceneList()){
            		
            		for(ActionElement action : scene.getActionList()){
            			
            			mAction actionObj = action.getActionObject();
            			
            			if(actionObj.isMyAction(getApplicationContext(), TYPES.A_TIME)){

                            for(TaskElement task : scene.getTaskList()){

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
    	int minutes = cal.get(Calendar.MINUTE);
    	int hour = cal.get(Calendar.HOUR_OF_DAY);
    	
    	if((minutes++) == 60){
    		
    		minutes = 0;
    		hour++;
    		
    		if(hour==24)
        		hour = 0;
            // need add ++day
    	}
    	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), hour, minutes, 0);
    	
    	long timeOffset = cal.getTimeInMillis() - System.currentTimeMillis();
    	
    	return timeOffset;
    }
}
