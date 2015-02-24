package com.taskerlite.logic;

import android.graphics.Bitmap;
import com.google.gson.GsonBuilder;
import com.taskerlite.logic.actions.aTimer;
import com.taskerlite.logic.actions.mAction;
import com.taskerlite.main.FragmentTaskBuilder;
import com.taskerlite.main.TaskerIcons;
import com.taskerlite.main.TaskerTypes.*;

import java.util.ArrayList;

public class ActionElement {

    // non serializable
    private transient mAction actionObject;
    private transient boolean isSelect = false;
    private transient boolean isMoving = false;

    // serializable
    private TYPES actionType;
    private String actionINStr;
    private Integer actionId;
    private int x, y;
    private ArrayList<Long> taskElementID = new ArrayList<Long>();

    public ActionElement(mAction actionObject, TYPES actionType, int x, int y){

        this.actionType = actionType;
        this.actionObject = actionObject;
        this.x = x;
        this.y = y;

        actionId = (int)System.currentTimeMillis();
        actionINStr = new GsonBuilder().create().toJson(actionObject);
    }

    public mAction getActionObject() {

        if(actionObject == null){
            switch (actionType) {
                case A_TIME:
                    actionObject = new GsonBuilder().create().fromJson(actionINStr, aTimer.class);
                    break;
                default:
                    break;
            }
        }

        return actionObject;
    }

    public Bitmap getIcon() {

        return TaskerIcons.getInstance().getBuilderIcon(actionType);
    }

    public void invalidateData(){
        actionINStr = new GsonBuilder().create().toJson(actionObject);
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

    public void addNewTaskElementId(long id){

        taskElementID.add(id);
    }
    public void deleteTaskElementId(long id){

        taskElementID.remove((Long)id);
        taskElementID.trimToSize();
    }
    public boolean isTaskElementIdPresent(long id) {

        for(Long item : taskElementID){
            if (id == item) {
                return true;
            }
        }
        return false;
    }

    public TYPES getActionType() {
        return actionType;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isSelect(){ return isSelect; }
    public void select(){ isSelect = true; }
    public void unselect(){ isSelect = false; }
    public Integer getActionId() { return actionId; }
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