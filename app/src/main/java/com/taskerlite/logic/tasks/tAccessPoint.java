package com.taskerlite.logic.tasks;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.kyleduo.switchbutton.SwitchButton;
import com.taskerlite.R;
import com.taskerlite.other.NotificationUtils;
import java.lang.reflect.Method;

public class tAccessPoint extends mTask{

    private String ssidName = "test";
    private String password = "1234567890";
    private boolean state = true;

    @Override
    public void start(Context context) {

        if(state){

            setAccessPointState(true, context);
            NotificationUtils.getInstance(context).createInfoNotification(getName());

        }
        else
            stop(context);
    }

    @Override
    public void stop(Context context) {

        setAccessPointState(false, context);
    }

    @Override
    public void show(FragmentManager fm) {

        UI ui = new UI();
        ui.setParent(this);
        ui.show(fm.beginTransaction(), "A_ACCESS_POINT");
    }

    void setAccessPointState(boolean paramBoolean, Context context) {

        try {

            WifiConfiguration localWifiConfiguration = new WifiConfiguration();
            localWifiConfiguration.SSID = ssidName;
            localWifiConfiguration.preSharedKey = password;
            localWifiConfiguration.allowedAuthAlgorithms.set(0);
            localWifiConfiguration.status = 2;
            localWifiConfiguration.allowedKeyManagement.set(1);
            localWifiConfiguration.allowedProtocols.set(0);
            WifiManager wifiManager = ((WifiManager) context.getSystemService(Context.WIFI_SERVICE));
            wifiManager.setWifiEnabled(false);
            Class localClass = wifiManager.getClass();
            Class[] arrayOfClass = new Class[2];
            arrayOfClass[0] = WifiConfiguration.class;
            arrayOfClass[1] = Boolean.TYPE;
            Method localMethod = localClass.getMethod("setWifiApEnabled", arrayOfClass);
            Object[] arrayOfObject = new Object[2];
            arrayOfObject[0] = localWifiConfiguration;
            arrayOfObject[1] = paramBoolean;
            localMethod.invoke(wifiManager, arrayOfObject);

        } catch (Exception localException) {}
    }

    public static class UI extends DialogFragment {

        private tAccessPoint task;
        private EditText nameInput;
        private Button saveBtn;
        private SwitchButton switchButton;

        public void setParent (tAccessPoint task){
            this.task = task;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

            View view = inflater.inflate(R.layout.dialog_task_access_point, container);

            saveBtn = (Button) view.findViewById(R.id.saveBtnId);
            saveBtn.setOnClickListener(btnListener);
            switchButton = (SwitchButton) view.findViewById(R.id.switchBtnId);
            switchButton.setChecked(task.state);
            nameInput = (EditText) view.findViewById(R.id.nameId);
            nameInput.setText(task.getName());

            return view;
        }

        View.OnClickListener btnListener =  new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    String name = String.valueOf(nameInput.getText());
                    task.setName(name);
                    task.state = switchButton.isChecked();

                }catch(Exception e){ }

                dismiss();
            }
        };
    }
}
