package com.taskerlite.main;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.taskerlite.R;
import com.taskerlite.logic.actions.aTimer;
import com.taskerlite.logic.actions.mAction;
import com.taskerlite.logic.tasks.mTask;
import com.taskerlite.logic.tasks.tApp;

public class TaskerBuilderDialogs extends DialogFragment {

    public static enum LIST{NULL, DIALOG_MENU, DIALOG_ACTIONS, DIALOG_TASK};

    private Dialog mDialog;
    private static LIST val;

    public static TaskerBuilderDialogs getInstance(LIST val){

        TaskerBuilderDialogs.val = val;
        return new TaskerBuilderDialogs();
    }



}

/*
class  TackBuilderDialogs extends DialogFragment {

    public static enum LIST{NULL, DIALOG_MENU, DIALOG_ACTIONS, DIALOG_TASK};

    private Dialog mDialog;
    private LIST val;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        switch(val){

            case DIALOG_MENU:

                mDialog.setContentView(R.layout.dialog_menu);

                ((ImageButton) mDialog.findViewById(R.id.actionElement)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "Open Action List", Toast.LENGTH_SHORT).show();
                        //scene.addNewAction("Timer 2", new aTimer(18, 47), mAction.ACTION_TYPE.TIMER, 0, 0);
                        //updateScreenUI();
                        mDialog.dismiss();
                    }
                });

                ((ImageButton) mDialog.findViewById(R.id.taskElement)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(),"Open Task List", Toast.LENGTH_SHORT).show();
                        //scene.addNewTask("Skype 2", new tApp("com.skype.raider"), mTask.TASK_TYPE.APP, 0, 0);
                        //updateScreenUI();
                        mDialog.dismiss();
                    }
                });

                break;
        }

        return mDialog;
    }

    public TackBuilderDialogs() {  }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_action_time, container);
        //mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        getDialog().setTitle("Hello");

        return view;
    }
}
 */