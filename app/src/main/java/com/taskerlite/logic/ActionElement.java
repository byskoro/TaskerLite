package com.taskerlite.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;

import com.google.gson.GsonBuilder;
import com.taskerlite.R;
import com.taskerlite.logic.actions.aTimer;
import com.taskerlite.logic.actions.mAction;
import com.taskerlite.logic.actions.mAction.*;
import com.taskerlite.main.mActivity;

import java.util.ArrayList;
import java.util.List;

public class ActionElement {

    // non serializable
    private transient mAction actionObject;
    private transient Bitmap icon;
    private transient boolean isSelect = false;

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

    public boolean isSelected(MotionEvent event, float size){
        if((event.getRawX() > x) && (event.getRawX() < x + size)
                && (event.getRawY() > y) && (event.getRawY() < y + size)){
            return true;
        }
        else
            return false;
    }

    public void setNewCoordinate(MotionEvent event){
        x = (int) event.getX() - mActivity.iconSize/2;
        y = (int) event.getY() - mActivity.iconSize/2;
    }

    public void addNewTaskElementId(long id){
        taskElementID.add(id);
    }
    public void deleteTaskElementId(long id){
        taskElementID.remove((Long)id);
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
    public boolean isElementSelect(){ return isSelect; }
    public void selectElement(){ isSelect = true; }
    public void unSelectElement(){ isSelect = false; }
}