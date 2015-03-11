package com.taskerlite.logic.tasks;

import android.app.FragmentManager;
import android.content.Context;

public abstract class mTask {

    private String name = "";

	public abstract void start(Context context);
    public abstract void stop(Context context);
    public abstract void show(FragmentManager fm, Context context);

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }
}
