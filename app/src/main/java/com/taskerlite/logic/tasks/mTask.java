package com.taskerlite.logic.tasks;

import android.content.Context;

public abstract class mTask {

    private String name = "";

	public abstract void start(Context context);
    public abstract void show(Context context);

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
