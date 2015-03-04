package com.taskerlite.logic.tasks;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.taskerlite.R;
import com.taskerlite.source.mNotification;

public class tWiFi extends mTask {

    private boolean state = true;

    @Override
    public void start(Context context) {

        if(state){
            wifiOn(context);
            String header = context.getResources().getString(R.string.t_wifi_short);
            mNotification.getInstance(context).createInfoNotification(header, getName());
        }
        else
            stop(context);
    }

    @Override
    public void stop(Context context) {

        wifiOff(context);
    }

    @Override
    public void show(FragmentManager fm) {

        UI ui = new UI();
        ui.setParent(this);
        ui.show(fm.beginTransaction(), "");
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

    public static class UI extends DialogFragment {

        private tWiFi task;
        private EditText nameInput;
        private Button saveBtn;
        private SwitchButton switchButton;
        private LinearLayout clearRequestLay;

        public void setParent (tWiFi task){
            this.task = task;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

            View view = inflater.inflate(R.layout.dialog_task_custom, container);
            TextView taskName = (TextView) view.findViewById(R.id.taskNameId);
            taskName.setText(getString(R.string.t_wifi_short));

            saveBtn = (Button) view.findViewById(R.id.saveBtnId);
            saveBtn.setOnClickListener(btnListener);
            switchButton = (SwitchButton) view.findViewById(R.id.switchBtnId);
            switchButton.setChecked(task.state);
            nameInput = (EditText) view.findViewById(R.id.nameId);
            nameInput.setText(task.getName());
            nameInput.setOnEditorActionListener(textWatcher);

            clearRequestLay = (LinearLayout) view.findViewById(R.id.clearRequestLay);
            clearRequest(nameInput);

            return view;
        }

        TextView.OnEditorActionListener textWatcher = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                clearRequest(v);
                return false;
            }
        };

        View.OnClickListener btnListener =  new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    task.setName(String.valueOf(nameInput.getText()));
                    task.state = switchButton.isChecked();
                }catch(Exception e){ }
                dismiss();
            }
        };

        private void clearRequest(TextView textView){
            clearRequestLay.requestFocus();
            InputMethodManager imm = (InputMethodManager)textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(textView.getApplicationWindowToken(), 0);
        }
    }
}

