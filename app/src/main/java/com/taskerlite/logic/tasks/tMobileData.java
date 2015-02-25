package com.taskerlite.logic.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
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

public class tMobileData extends mTask{

    private boolean state = true;

    @Override
    public void start(Context context) {

        if(state){

            set3GEnable(true, context);
            NotificationUtils.getInstance(context).createInfoNotification(getName());

        }
        else
            stop(context);
    }

    @Override
    public void stop(Context context) {

        set3GEnable(false, context);
    }

    @Override
    public void show(FragmentManager fm) {

        UI ui = new UI();
        ui.setParent(this);
        ui.show(fm.beginTransaction(), "A_TREE_G");
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

    public static class UI extends DialogFragment {

        private tMobileData task;
        private EditText nameInput;
        private Button saveBtn;
        private SwitchButton switchButton;

        public void setParent (tMobileData task){
            this.task = task;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

            View view = inflater.inflate(R.layout.dialog_task_mobile_data, container);

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
