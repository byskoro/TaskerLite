package com.taskerlite.logic;

import android.graphics.Bitmap;
import com.google.gson.GsonBuilder;
import com.taskerlite.logic.tasks.mTask;
import com.taskerlite.logic.tasks.tAccessPoint;
import com.taskerlite.logic.tasks.tApp;
import com.taskerlite.logic.tasks.tGPS;
import com.taskerlite.logic.tasks.tMobileData;
import com.taskerlite.logic.tasks.tMobileLight;
import com.taskerlite.logic.tasks.tScreen;
import com.taskerlite.logic.tasks.tWiFi;
import com.taskerlite.source.Icons;
import com.taskerlite.source.Types.*;

public class TaskElement {

    // non serializable
    private transient mTask   taskObject;
    private transient boolean isSelect = false;
    private transient boolean isMoving = false;

    // serializable
    private TYPES taskType;
    private String taskINStr;
    private long taskId;
    private int x, y;

    public TaskElement(mTask obj, TYPES objType, int x, int y){

        this.taskObject = obj;
        this.taskType = objType;
        this.x = x;
        this.y = y;

        taskId = System.currentTimeMillis();
    }

    public mTask getTaskObject() {

        if(taskObject == null){
            switch (taskType) {
                case T_APP:
                    taskObject = new GsonBuilder().create().fromJson(taskINStr, tApp.class);
                    break;
                case T_THREE_G:
                    taskObject = new GsonBuilder().create().fromJson(taskINStr, tMobileData.class);
                    break;
                case T_ACCESS_POINT:
                    taskObject = new GsonBuilder().create().fromJson(taskINStr, tAccessPoint.class);
                    break;
                case T_SCREEN:
                    taskObject = new GsonBuilder().create().fromJson(taskINStr, tScreen.class);
                    break;
                case T_MOBILE_LIGHT:
                    taskObject = new GsonBuilder().create().fromJson(taskINStr, tMobileLight.class);
                    break;
                case T_WIFI:
                    taskObject = new GsonBuilder().create().fromJson(taskINStr, tWiFi.class);
                    break;
                case T_GPS:
                    taskObject = new GsonBuilder().create().fromJson(taskINStr, tGPS.class);
                    break;
                default:
                    break;
            }
        }

        return taskObject;
    }

    public Bitmap getIcon() {

        return Icons.getInstance().getBuilderIcon(taskType);
    }

    public boolean isTouched(int xPointer, int yPointer, float size){

        if((xPointer > x) && (xPointer < x + size)
            && (yPointer > y) && (yPointer < y + size)){
            return true;
        }
        else
            return false;
    }

    public void setNewCoordinate(int xPointer, int yPointer, int width, int height){

        if(yPointer < Icons.builderSize /2)
            yPointer = Icons.builderSize /2;
        else if(yPointer > height  - Icons.builderSize /2)
            yPointer = height - Icons.builderSize /2;

        if(xPointer < Icons.builderSize /2)
            xPointer = Icons.builderSize /2;
        else if(xPointer > width - Icons.builderSize /2)
            xPointer = width - Icons.builderSize /2;

        x = xPointer - Icons.builderSize /2;
        y = yPointer - Icons.builderSize /2;
    }

    public void invalidateData(){
        taskINStr = new GsonBuilder().create().toJson(taskObject);
    }

    public TYPES getTaskType() {
        return taskType;
    }
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
