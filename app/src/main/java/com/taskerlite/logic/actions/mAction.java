package com.taskerlite.logic.actions;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.taskerlite.main.TaskerTypes.*;

public abstract class mAction {

    private String name = "";

	public abstract boolean isMyAction(Context context, TYPES type);
    public abstract void show(FragmentManager fm);

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
