package com.taskerlite.logic.actions;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.taskerlite.R;
import com.taskerlite.main.Types;

public class aBootComplete extends mAction{

    public aBootComplete(Context context){
        setName(context.getResources().getString(R.string.a_boot_complete_short));
    }

    @Override
    public boolean isMyAction(Context context, Types.TYPES type) {
        return type == Types.TYPES.A_BOOT_COMPLETE;
    }

    @Override
    public void show(FragmentManager fm) {

    }
}
