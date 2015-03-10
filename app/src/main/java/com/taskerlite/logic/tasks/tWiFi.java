package com.taskerlite.logic.tasks;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.taskerlite.R;
import com.taskerlite.source.mNotification;

public class tWiFi extends mTask {

    private boolean state = false;

    public tWiFi(Context context){
        setName(context.getString(R.string.off));
    }

    @Override
    public void start(Context context) {

        if(state){

            wifiOn(context);
            String header = context.getResources().getString(R.string.t_wifi_short);
            mNotification.getInstance(context).createInfoNotification(header, getName());

        } else
            stop(context);
    }

    @Override
    public void stop(Context context) {

        wifiOff(context);
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

    public void wifiOn(Context context) {

        try {

            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);

        } catch (Exception e) { }
    }

    public void wifiOff(Context context) {

        try {

            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(false);

        } catch (Exception e) { }
    }
}

