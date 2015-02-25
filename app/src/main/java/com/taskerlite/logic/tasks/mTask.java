package com.taskerlite.logic.tasks;

import android.content.Context;
import android.support.v4.app.FragmentManager;

public abstract class mTask {

    private String name = "";

	public abstract void start(Context context);
    public abstract void stop(Context context);
    public abstract void show(FragmentManager fm);

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
