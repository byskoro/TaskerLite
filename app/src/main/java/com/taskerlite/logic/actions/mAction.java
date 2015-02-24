package com.taskerlite.logic.actions;

import android.content.Context;
import com.taskerlite.main.TaskerTypes.*;

public abstract class mAction {

    private String name = "";

	public abstract boolean isMyAction(Context context, TYPES type);
    public abstract void show(Context context);

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
