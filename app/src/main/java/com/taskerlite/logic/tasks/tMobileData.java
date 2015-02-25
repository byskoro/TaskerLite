package com.taskerlite.logic.tasks;


import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.app.FragmentManager;

import com.taskerlite.other.NotificationUtils;

import java.lang.reflect.Method;

public class tMobileData extends mTask{

    public tMobileData(){
        setName("Internet");
    }

    @Override
    public void start(Context context) {

        set3GEnable(true, context);

        NotificationUtils.getInstance(context).createInfoNotification(getName());
    }

    @Override
    public void stop(Context context) {

        set3GEnable(false, context);
    }

    @Override
    public void show(FragmentManager fm) {

    }

    private boolean set3GEnable(boolean paramBoolean, Context context)
    {
        try
        {
            ConnectivityManager localConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Class[] arrayOfClass = new Class[1];
            arrayOfClass[0] = Boolean.TYPE;
            Method localMethod = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", arrayOfClass);
            localMethod.setAccessible(true);
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Boolean.valueOf(paramBoolean);
            localMethod.invoke(localConnectivityManager, arrayOfObject);
            return true;
        }
        catch (Exception localException) { }
        return false;
    }
}
