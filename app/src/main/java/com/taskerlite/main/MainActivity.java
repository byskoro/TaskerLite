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

public class MainActivity extends Activity {

    public static SceneList sceneList;
    public static TaskList taskListFragment;
    public static TaskBuilder taskBuilderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sceneList = Flash.getList();

        sceneList = new SceneList();
        sceneList.addNewSnene("Test Scene");
        sceneList.getScene(0).addNewAction("Timer", new aTimer(17, 27), ACTION_TYPE.TIMER, 0, 0);
        sceneList.getScene(0).addNewTask("Skype", new tApp("com.skype.raider"), TASK_TYPE.APP);
        Flash.saveList(sceneList);

        taskListFragment    = new TaskList();
        taskBuilderFragment = new TaskBuilder();

        getFragmentManager().beginTransaction().add(R.id.fragmentConteiner, taskListFragment).commit();

        if(!TaskerService.isRunning(this))
            startService(new Intent(this, TaskerService.class));
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().findFragmentById(R.id.fragmentConteiner) instanceof TaskList) {

            super.onBackPressed();

        }else {

            getFragmentManager().beginTransaction().
            replace(R.id.fragmentConteiner, MainActivity.taskListFragment).
            commit();
        }
    }
}
