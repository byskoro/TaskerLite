package com.taskerlite.logic.tasks;

import android.content.Context;

public abstract class mTask {

    public static enum TASK_TYPE {APP, ALARM, WIFI, MOBILEDATA};

	public abstract void start(Context context);
    public abstract void show(Context context);
}
