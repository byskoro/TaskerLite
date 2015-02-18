package com.taskerlite.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;

import com.google.gson.GsonBuilder;
import com.taskerlite.R;
import com.taskerlite.logic.tasks.mTask;
import com.taskerlite.logic.tasks.mTask.*;
import com.taskerlite.logic.tasks.tApp;
import com.taskerlite.main.mActivity;

public class TaskElement {

    // non serializable
    private transient mTask taskObject;
    private transient Bitmap icon;
    private transient boolean isSelect = false;

    // serializable
    private TASK_TYPE taskType;
    private String taskINStr;
    private String taskName;
    private long taskId;
    private int x, y;

    public TaskElement(String objName, mTask obj, TASK_TYPE objType){

        this.taskName = objName;
        this.taskObject = obj;
        this.taskType = objType;

        taskId    = System.currentTimeMillis();
        taskINStr = new GsonBuilder().create().toJson(obj);
    }

    public mTask getTaskObject() {

        if(taskObject == null){
            switch (taskType) {
                case APP:
                    taskObject = new GsonBuilder().create().fromJson(taskINStr, tApp.class);
                    break;
                case WIFI:
                    taskObject = new GsonBuilder().create().fromJson(taskINStr, tApp.class);
                    break;
                case ALARM:
                    break;
                case MOBILEDATA:
                    break;
                default:
                    break;
            }
        }

        return taskObject;
    }

    public Bitmap getIcon(Context context, float size) {

        Bitmap bigIcon = null;

        if(icon == null){
            switch (taskType) {
                case APP:
                    bigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.t_app);
                    break;
                case WIFI:
                    break;
                case ALARM:
                    break;
                case MOBILEDATA:
                    break;
                default:
                    break;
            }
            icon = Bitmap.createScaledBitmap(bigIcon, (int)size, (int)size, true);
        }

        return icon;
    }

    public boolean isTouched(MotionEvent event, float size){

        if((event.getRawX() > x) && (event.getRawX() < x + size)
            && (event.getRawY() > y) && (event.getRawY() < y + size)){
            return true;
        }
        else
            return false;
    }

    public void setNewCoordinate(MotionEvent event){
        x = (int) event.getX() - mActivity.iconSizeElement /2;
        y = (int) event.getY() - mActivity.iconSizeElement /2;
    }

    public String getTaskName() { return taskName; }
    public long getTaskId() { return taskId; }
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isElementSelect(){ return isSelect; }
    public void selectElement(){ isSelect = true; }
    public void unSelectElement(){ isSelect = false; }
}
