package com.taskerlite.logic.tasks;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.kyleduo.switchbutton.SwitchButton;
import com.taskerlite.R;
import com.taskerlite.source.mNotification;

import java.lang.reflect.Method;

public class tAccessPoint extends mTask{

    private String ssidName = "";
    private String ssidPassword = "";
    private boolean state = true;

    @Override
    public void start(Context context) {

        if(state){

            setAccessPointState(true, context);
            String header = context.getResources().getString(R.string.t_access_point_short);
            mNotification.getInstance(context).createInfoNotification(header, getName());

        } else
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
            localWifiConfiguration.preSharedKey = ssidPassword;
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
        private EditText ssidNameInput, ssidPasswordInput;
        private SwitchButton switchButton;
        private LinearLayout clearRequestLay;

        public void setParent (tAccessPoint task){
            this.task = task;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

            View view = inflater.inflate(R.layout.dialog_task_access_point, container);

            switchButton = (SwitchButton) view.findViewById(R.id.switchBtnId);
            switchButton.setChecked(task.state);
            switchButton.setOnCheckedChangeListener(swBtnListener);
            ssidNameInput = (EditText) view.findViewById(R.id.ssidNameId);
            ssidNameInput.setText(task.ssidName);
            ssidNameInput.setOnEditorActionListener(textWatcher);
            ssidPasswordInput = (EditText) view.findViewById(R.id.ssidPasswordId);
            ssidPasswordInput.setText(task.ssidPassword);
            ssidPasswordInput.setOnEditorActionListener(textWatcher);

            clearRequestLay = (LinearLayout) view.findViewById(R.id.clearRequestLay);
            clearRequest(ssidNameInput);

            return view;
        }

        TextView.OnEditorActionListener textWatcher = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                clearRequest(v);
                return false;
            }
        };

        OnCheckedChangeListener swBtnListener = new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    ssidNameInput.setVisibility(View.VISIBLE);
                    ssidPasswordInput.setVisibility(View.VISIBLE);

                }else{

                    ssidNameInput.setVisibility(View.GONE);
                    ssidPasswordInput.setVisibility(View.GONE);

                }
            }
        };

        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            try {
                task.setName(String.valueOf(ssidNameInput.getText()));
                task.ssidName = String.valueOf(ssidNameInput.getText());
                task.ssidPassword = String.valueOf(ssidPasswordInput.getText());
                task.state = switchButton.isChecked();
            }catch(Exception e){ }
        }

        private void clearRequest(TextView textView){
            clearRequestLay.requestFocus();
            InputMethodManager imm = (InputMethodManager)textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(textView.getApplicationWindowToken(), 0);
        }
    }
}
