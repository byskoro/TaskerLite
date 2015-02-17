package com.taskerlite.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.taskerlite.R;
import com.taskerlite.logic.actions.aTimer;
import com.taskerlite.other.Flash;
import com.taskerlite.logic.SceneList;
import com.taskerlite.logic.tasks.tApp;
import com.taskerlite.logic.actions.mAction.*;
import com.taskerlite.logic.tasks.mTask.*;
import com.taskerlite.other.Screen;

public class mActivity extends Activity {

    public static SceneList sceneList;
    public static int iconSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconSize = Screen.getWidth(this)/getResources().getInteger(R.integer.icon_divider);

        sceneList = Flash.getList();

        //sceneList = new SceneList();
        //sceneList.addNewScene("Test Scene");
        //sceneList.getScene(0).addNewAction("Timer", new aTimer(18, 47), ACTION_TYPE.TIMER, 0, 0);
        //sceneList.getScene(0).addNewTask("Skype", new tApp("com.skype.raider"), TASK_TYPE.APP);
        //Flash.saveList(sceneList);

        getFragmentManager().beginTransaction().
        add(R.id.fragmentConteiner, new TaskList()).
        commit();

        if(!TaskerService.isRunning(this))
            startService(new Intent(this, TaskerService.class));
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().findFragmentById(R.id.fragmentConteiner) instanceof TaskList) {

            super.onBackPressed();

        }else {

            Flash.saveList(sceneList);

            getFragmentManager().beginTransaction().
            replace(R.id.fragmentConteiner, new TaskList()).
            commit();
        }
    }
}
