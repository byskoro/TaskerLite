package com.taskerlite.logic;

import com.google.gson.GsonBuilder;
import com.taskerlite.logic.actions.aTimer;
import com.taskerlite.logic.actions.mAction;
import com.taskerlite.logic.actions.mAction.*;

public class ActionElement {

    private transient mAction actionObject;
    private ACTION_TYPE actionType;
    private String actionINStr;
    private String actionName;
    private long actionId;
    private int xCoordinate, yCoordinate;

    public ActionElement(String actionName, mAction actionObject, ACTION_TYPE actionType, int xCoordinate, int yCoordinate){

        this.actionName = actionName;
        this.actionType = actionType;
        this.actionObject = actionObject;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;

        actionId = System.currentTimeMillis();
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

    public long getActionId() {
        return actionId;
    }
}