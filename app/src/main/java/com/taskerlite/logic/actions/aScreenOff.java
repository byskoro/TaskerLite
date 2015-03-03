package com.taskerlite.logic.actions;


import android.app.FragmentManager;
import android.content.Context;

import com.taskerlite.R;
import com.taskerlite.source.Types;

public class aScreenOff extends mAction{

    public aScreenOff(Context context){
        setName(context.getResources().getString(R.string.a_screen_on));
    }

    @Override
    public boolean isMyAction(Context context, Types.TYPES type) {
        return type == Types.TYPES.A_SCREEN_OFF;
    }

    @Override
    public void show(FragmentManager fm) {

    }
}
