package com.taskerlite.logic;

import android.graphics.Bitmap;
import com.google.gson.GsonBuilder;
import com.taskerlite.logic.actions.aBootComplete;
import com.taskerlite.logic.actions.aTimer;
import com.taskerlite.logic.actions.mAction;
import com.taskerlite.main.Icons;
import com.taskerlite.main.Types.*;

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
                case A_BOOT_COMPLETE:
                    actionObject = new GsonBuilder().create().fromJson(actionINStr, aBootComplete.class);
                    break;
                default:
                    break;
            }
        }

        return actionObject;
    }

    public Bitmap getIcon() {

        return Icons.getInstance().getBuilderIcon(actionType);
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