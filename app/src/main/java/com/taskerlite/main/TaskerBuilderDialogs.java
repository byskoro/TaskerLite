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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.taskerlite.R;
import com.taskerlite.logic.actions.aTimer;
import com.taskerlite.logic.actions.mAction;
import com.taskerlite.logic.tasks.mTask;
import com.taskerlite.logic.tasks.tApp;

public class TaskerBuilderDialogs extends DialogFragment {

    LinearLayout actionListLay;
    ImageButton  actionBtn, taskBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_menu, container);

        actionListLay = (LinearLayout) view.findViewById(R.id.actionList);
        actionBtn = (ImageButton) view.findViewById(R.id.actionElement);
        actionBtn.setOnClickListener(actionBtnListener);
        taskBtn   = (ImageButton) view.findViewById(R.id.taskElement);
        taskBtn.setOnClickListener(taskBtnListener);

        return view;
    }

    View.OnClickListener actionBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //

            Toast.makeText(getActivity(),"Open Action List", Toast.LENGTH_SHORT).show();
            scene.addNewAction("Timer 2", new aTimer(18, 47), TaskerTypes.TYPES.TIME, 0, 0);
            updateScreenUI();
            //mDialog.dismiss();
            actionListLay.setVisibility(View.GONE);

        }
    };

    View.OnClickListener taskBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Toast.makeText(getActivity(),"Open Task List", Toast.LENGTH_SHORT).show();
            scene.addNewTask("Skype 2", new tApp("com.skype.raider"), TaskerTypes.TYPES.APP, 0, 0);
            updateScreenUI();
            //mDialog.dismiss();
            actionListLay.setVisibility(View.VISIBLE);

        }
    };

}
