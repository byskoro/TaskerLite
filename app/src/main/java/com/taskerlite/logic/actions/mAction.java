package com.taskerlite.logic.actions;

import android.content.Context;
import com.taskerlite.logic.SceneList.*;

public abstract class mAction {

	public abstract boolean isMyAction(Context context, ACTION_TYPE type);
}
