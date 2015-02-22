package com.taskerlite.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.GsonBuilder;
import com.taskerlite.R;
import com.taskerlite.logic.actions.aTimer;
import com.taskerlite.logic.actions.mAction;
import com.taskerlite.logic.actions.mAction.*;
import com.taskerlite.main.TaskBuilderFragment;
import com.taskerlite.main.mActivity;

import java.util.ArrayList;

public class ActionElement {

    // non serializable
    private transient mAction actionObject;
    private transient Bitmap icon;
    private transient boolean isSelect = false;
    private transient boolean isMoving = false;

    // serializable
    private ACTION_TYPE actionType;
    private String actionINStr;
    private String actionName;
    private Integer actionId;
    private int x, y;
    private ArrayList<Long> taskElementID = new ArrayList<Long>();

    public ActionElement(String actionName, mAction actionObject, ACTION_TYPE actionType, int x, int y){

        this.actionName = actionName;
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
                case TIMER:
                    actionObject = new GsonBuilder().create().fromJson(actionINStr, aTimer.class);
                    break;
                case FINISHBOOT:
                    break;
                case SCREENOFF:
                    break;
                case SCREENON:
                    break;
                default:
                    break;
            }
        }

        return actionObject;
    }

    public Bitmap getIcon(Context context, float size) {

        Bitmap bigIcon = null;

        if(icon == null){
            switch (actionType) {
                case TIMER:
                    bigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.a_timer);
                    break;
                case FINISHBOOT:
                    break;
                case SCREENOFF:
                    break;
                case SCREENON:
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
        else if(yPointer > TaskBuilderFragment.screenHeight  - mActivity.iconSizeElement /2)
            yPointer = TaskBuilderFragment.screenHeight - mActivity.iconSizeElement /2;

        if(xPointer < mActivity.iconSizeElement /2)
            xPointer = mActivity.iconSizeElement /2;
        else if(xPointer > TaskBuilderFragment.screenWidth - mActivity.iconSizeElement /2)
            xPointer = TaskBuilderFragment.screenWidth - mActivity.iconSizeElement /2;

        x = xPointer - mActivity.iconSizeElement /2;
        y = yPointer - mActivity.iconSizeElement /2;
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
    public int getX() { return x; }
    public int getY() { return y; }
    public String getActionName() { return actionName; }
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