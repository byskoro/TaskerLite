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
        private SwitchButton switchButton;

        public void setParent (tMobileData task){
            this.task = task;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

            View view = inflater.inflate(R.layout.dialog_task_custom, container);
            TextView taskName = (TextView) view.findViewById(R.id.taskNameId);
            taskName.setText(getString(R.string.t_mobile_internet_short));

            switchButton = (SwitchButton) view.findViewById(R.id.switchBtnId);
            switchButton.setChecked(task.state);

            return view;
        }

        @Override
        public void onDismiss(DialogInterface dialogInterface) {

            task.state = switchButton.isChecked();

            if(task.state)
                task.setName(getString(R.string.on));
            else
                task.setName(getString(R.string.off));

            dismiss();
        }
    }
}
