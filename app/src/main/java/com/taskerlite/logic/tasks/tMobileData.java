package com.taskerlite.logic.tasks;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.taskerlite.R;
import com.taskerlite.source.mNotification;

import java.lang.reflect.Method;

public class tMobileData extends mTask{

    private boolean state = false;

    public tMobileData(Context context){
        setName(context.getString(R.string.off));
    }

    @Override
    public void start(Context context) {

        if(state){
            set3GEnable(true, context);
            String header = context.getResources().getString(R.string.t_mobile_internet_short);
            mNotification.getInstance(context).createInfoNotification(header, getName());
        }
        else
            stop(context);
    }

    @Override
    public void stop(Context context) {

        set3GEnable(false, context);
    }

    @Override
    public void show(FragmentManager fm, Context context) {

        if(!state) {
            setName(context.getString(R.string.on));
            state = true;
        } else {
            setName(context.getString(R.string.off));
            state = false;
        }
    }

    private void set3GEnable(boolean paramBoolean, Context context) {

        try {

            ConnectivityManager localConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Class[] arrayOfClass = new Class[1];
            arrayOfClass[0] = Boolean.TYPE;
            Method localMethod = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", arrayOfClass);
            localMethod.setAccessible(true);
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Boolean.valueOf(paramBoolean);
            localMethod.invoke(localConnectivityManager, arrayOfObject);

        } catch (Exception localException) { }
    }
}
