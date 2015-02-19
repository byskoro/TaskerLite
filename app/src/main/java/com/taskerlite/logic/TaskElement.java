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
import com.taskerlite.main.TaskBuilder;
import com.taskerlite.main.mActivity;

public class TaskElement {

    // non serializable
    private transient mTask taskObject;
    private transient Bitmap icon;
    private transient boolean isSelect = false;
    private transient boolean isMoving = false;

    // serializable
    private TASK_TYPE taskType;
    private String taskINStr;
    private String taskName;
    private long taskId;
    private int x, y;

    public TaskElement(String objName, mTask obj, TASK_TYPE objType, int x, int y){

        this.taskName = objName;
        this.taskObject = obj;
        this.taskType = objType;
        this.x = x;
        this.y = y;

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

    public boolean isTouched(int xPointer, int yPointer, float size){

        if((xPointer > x) && (xPointer < x + size)
            && (yPointer > y) && (yPointer < y + size)){
            return true;
        }
        else
            return false;
    }

    public void setNewCoordinate(int xPointer, int yPointer){

        if(yPointer < mActivity.iconSizeElement /2)
            yPointer = mActivity.iconSizeElement /2;
        else if(yPointer > TaskBuilder.screenHeight  - mActivity.iconSizeElement /2)
            yPointer = TaskBuilder.screenHeight - mActivity.iconSizeElement /2;

        if(xPointer < mActivity.iconSizeElement /2)
            xPointer = mActivity.iconSizeElement /2;
        else if(xPointer > TaskBuilder.screenWidth - mActivity.iconSizeElement /2)
            xPointer = TaskBuilder.screenWidth - mActivity.iconSizeElement /2;

        x = xPointer - mActivity.iconSizeElement /2;
        y = yPointer - mActivity.iconSizeElement /2;
    }

    public String getTaskName() { return taskName; }
    public long getTaskId() { return taskId; }
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isSelect(){ return isSelect; }
    public void select(){ isSelect = true; }
    public void unselect(){ isSelect = false; }

    public boolean isMoving(){
        return isMoving;
    }
    public void setMoving(){
        isMoving = true;
    }
    public void clearMoving(){
        isMoving = false;
    }
}
