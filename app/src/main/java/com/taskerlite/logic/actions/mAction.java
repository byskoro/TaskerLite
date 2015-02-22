package com.taskerlite.logic.actions;

import android.content.Context;
import com.taskerlite.main.TaskerTypes.*;

public abstract class mAction {

	public abstract boolean isMyAction(Context context, TYPES type);
    public abstract void show(Context context);
}
