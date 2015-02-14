package com.taskerlite.main;

import java.util.Calendar;

import com.taskerlite.other.Flash;
import com.taskerlite.taskLogic.SceneL.*;
import com.taskerlite.taskLogic.*;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class TaskerService extends Service {

	public static boolean newDataCome = false;
	private SceneL sceneList;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

    	newDataCome = true;
        
    	serviceHandler.sendEmptyMessageDelayed(0, generateOfsetTime());
    	
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
            		
            		for(Action action : scene.getActionList()){
            			
            			mAction actionObj = (mAction) action.getActionObject();
            			
            			if(actionObj.isMyAction(getApplicationContext(), ACTION_TYPE.TIMER)){
            				
            				for(Task task : scene.getTaskList()){     
            					
            					if(task.isMyTaskAction(action.getActionId())){
            						
            						mTask taskObj = (mTask) task.getObj();
            						taskObj.start(getApplicationContext());
            					}
            				}
            			}
            		}
            	}
        		
			} catch (Exception e) { }
        	
        	serviceHandler.sendEmptyMessageDelayed(0, generateOfsetTime());
        };
    };
    
    public static boolean isRunning(Context ctx) {
    	
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
        	
            if (TaskerService.class.getName().equals(service.service.getClassName()))
                return true;
        }
        return false;
    }
    
    private long generateOfsetTime(){
    	
    	Calendar cal = Calendar.getInstance();
    	int minutes = cal.get(Calendar.MINUTE);
    	int hour = cal.get(Calendar.HOUR_OF_DAY);
    	
    	if((minutes++) == 60){
    		
    		minutes = 0;
    		hour++;
    		
    		if(hour==24)
        		hour = 0;
    	}
    	cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), hour, minutes, 0);
    	
    	long timeOffset = cal.getTimeInMillis() - System.currentTimeMillis();
    	
    	return timeOffset;
    }

}
