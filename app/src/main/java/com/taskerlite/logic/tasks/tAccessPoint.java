package com.taskerlite.logic.tasks;


import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v4.app.FragmentManager;

import java.lang.reflect.Method;

public class tAccessPoint extends mTask{

    private String ssidName = "test";
    private String password = "test";

    @Override
    public void start(Context context) {

    }

    @Override
    public void stop(Context context) {

    }

    @Override
    public void show(FragmentManager fm) {

    }

    void setAPenabled(boolean paramBoolean, Context context)
    {
        try
        {
            WifiManager wifiManager = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE));

            WifiConfiguration localWifiConfiguration = new WifiConfiguration();
            localWifiConfiguration.SSID = ssidName;
            localWifiConfiguration.preSharedKey = password;
            localWifiConfiguration.allowedAuthAlgorithms.set(0);
            localWifiConfiguration.status = 2;
            localWifiConfiguration.allowedKeyManagement.set(1);
            localWifiConfiguration.allowedProtocols.set(0);
            wifiManager.setWifiEnabled(false);
            Class localClass = wifiManager.getClass();
            Class[] arrayOfClass = new Class[2];
            arrayOfClass[0] = WifiConfiguration.class;
            arrayOfClass[1] = Boolean.TYPE;
            Method localMethod = localClass.getMethod("setWifiApEnabled", arrayOfClass);
            Object[] arrayOfObject = new Object[2];
            arrayOfObject[0] = localWifiConfiguration;
            arrayOfObject[1] = Boolean.valueOf(paramBoolean);
            localMethod.invoke(wifiManager, arrayOfObject);
            return;
        }
        catch (Exception localException) {}
    }
}
