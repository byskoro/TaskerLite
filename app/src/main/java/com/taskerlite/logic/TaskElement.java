package com.taskerlite.logic;

import android.graphics.Bitmap;

import com.google.gson.GsonBuilder;
import com.taskerlite.logic.tasks.mTask;
import com.taskerlite.logic.tasks.mTask.*;
import com.taskerlite.logic.tasks.tApp;

public class TaskElement {

    // non serializable
    private transient mTask taskObject;
    private transient Bitmap icon;

    // serializable
    private TASK_TYPE taskType;
    private String taskINStr;
    private String taskName;
    private long taskActionId;
    private long taskId;
    private int xCoordinate, yCoordinate;

    public TaskElement(String objName, mTask obj, TASK_TYPE objType){

        this.taskName = objName;
        this.taskObject = obj;
        this.taskType = objType;

        taskId = System.currentTimeMillis();
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
    public String getTaskName() {
        return taskName;
    }
    public boolean isMyTask(long id) {
        return id == taskId ? true : false;
    }
    public boolean isMyTaskAction(long id) {
        return id == taskActionId ? true : false;
    }
    public void setTaskActionId(long taskActionId) {
        this.taskActionId = taskActionId;
    }
    public int getXCoordinate() {
        return xCoordinate;
    }
    public int getYCoordinate() {
        return yCoordinate;
    }
}
