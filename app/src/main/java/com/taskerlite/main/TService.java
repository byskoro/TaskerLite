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
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;

import com.taskerlite.main.TaskerTypes.*;
import com.taskerlite.other.Vibro;

public class TService extends Service {

	private ProfileController sceneList;
    private String previousRawData = "";
    private boolean flashState = false;

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
        			
            		sceneList = Flash.getProfileList();
                    previousRawData = Flash.getRawData();
            	}

                //Vibro.playLong(getApplicationContext());

                if(!flashState){
                    flashLightOn(null);
                    flashState = true;
                }else {
                    flashLightOff(null);
                    flashState = false;
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

    Camera cam;

    public void flashLightOn(View view) {

        try {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                cam = Camera.open();
                Camera.Parameters p = cam.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                cam.setParameters(p);
                cam.startPreview();
            }
        } catch (Exception e) { }
    }

    public void flashLightOff(View view) {
        try {
            if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                cam.stopPreview();
                cam.release();
                cam = null;
            }
        } catch (Exception e) { }
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

    	return 2500;
    }
}
