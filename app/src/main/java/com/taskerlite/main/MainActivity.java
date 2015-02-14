package com.taskerlite.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.taskerlite.R;
import com.taskerlite.other.Flash;
import com.taskerlite.taskLogic.SceneL;
import com.taskerlite.taskLogic.aTimer;
import com.taskerlite.taskLogic.tApp;

public class MainActivity extends Activity {

    public static SceneL sceneList;
    FragmentTaskList taskListFragment;
    FragmentTaskBuilder taskBuilderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sceneList = Flash.getList();

        sceneList = new SceneL();

        sceneList.addNewSnene("Scene 1");

        sceneList.getScene(0).addNewAction("Action_1", new aTimer(17, 27), SceneL.ACTION_TYPE.TIMER, 0, 0);
        sceneList.getScene(0).addNewTask("startSkype", new tApp("com.skype.raider"), SceneL.TASK_TYPE.APP);

        Flash.saveList(sceneList);

        taskListFragment    = new FragmentTaskList();
        taskBuilderFragment = new FragmentTaskBuilder();

        getFragmentManager().beginTransaction().replace(R.id.fragmentConteiner, taskListFragment).commit();

        if(!TaskerService.isRunning(this))
            startService(new Intent(this, TaskerService.class));
    }

}
