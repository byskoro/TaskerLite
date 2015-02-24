package com.taskerlite.logic;

import android.graphics.Bitmap;
import com.google.gson.GsonBuilder;
import com.taskerlite.logic.tasks.mTask;
import com.taskerlite.logic.tasks.tApp;
import com.taskerlite.main.FragmentTaskBuilder;
import com.taskerlite.main.TaskerIcons;
import com.taskerlite.main.TaskerTypes.*;

public class TaskElement {

    // non serializable
    private transient mTask taskObject;
    private transient Bitmap icon;
    private transient boolean isSelect = false;
    private transient boolean isMoving = false;

    // serializable
    private TYPES taskType;
    private String taskINStr;
    private String taskName;
    private long taskId;
    private int x, y;

    public TaskElement(String objName, mTask obj, TYPES objType, int x, int y){

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
                case T_APP:
                    taskObject = new GsonBuilder().create().fromJson(taskINStr, tApp.class);
                    break;
                default:
                    break;
            }
        }

        return taskObject;
    }

    public Bitmap getIcon() {

        return TaskerIcons.getInstance().getBuilderIcon(taskType);
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

        if(yPointer < TaskerIcons.builderSize /2)
            yPointer = TaskerIcons.builderSize /2;
        else if(yPointer > FragmentTaskBuilder.screenHeight  - TaskerIcons.builderSize /2)
            yPointer = FragmentTaskBuilder.screenHeight - TaskerIcons.builderSize /2;

        if(xPointer < TaskerIcons.builderSize /2)
            xPointer = TaskerIcons.builderSize /2;
        else if(xPointer > FragmentTaskBuilder.screenWidth - TaskerIcons.builderSize /2)
            xPointer = FragmentTaskBuilder.screenWidth - TaskerIcons.builderSize /2;

        x = xPointer - TaskerIcons.builderSize /2;
        y = yPointer - TaskerIcons.builderSize /2;
    }

    public TYPES getTaskType() {
        return taskType;
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
