package com.taskerlite.logic.actions;

import android.app.FragmentManager;
import android.content.Context;

import com.taskerlite.source.Types.*;

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
