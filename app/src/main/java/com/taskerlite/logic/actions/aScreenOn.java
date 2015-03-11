package com.taskerlite.logic.actions;


import android.app.FragmentManager;
import android.content.Context;

import com.taskerlite.R;
import com.taskerlite.source.Types;

public class aScreenOn extends mAction{

    public aScreenOn(Context context){
        setName(context.getResources().getString(R.string.a_screen_on));
    }

    @Override
    public boolean isMyAction(Context context, Types.TYPES type) {
        return type == Types.TYPES.A_SCREEN_ON;
    }

    @Override
    public void show(FragmentManager fm) {

    }
}
