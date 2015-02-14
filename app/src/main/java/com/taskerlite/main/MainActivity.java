package com.taskerlite.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.taskerlite.R;
import com.taskerlite.other.Flash;
import com.taskerlite.taskLogic.SceneL;

public class MainActivity extends Activity {

    public static SceneL sceneList;
    FragmentTaskList taskListFragment;
    FragmentTaskBuilder taskBuilderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sceneList = Flash.getList();

        taskListFragment    = new FragmentTaskList();
        taskBuilderFragment = new FragmentTaskBuilder();

        //Flash.saveList(sl);

        getFragmentManager().beginTransaction().
                replace(R.id.fragmentConteiner, taskListFragment).
                commit();

        if(!TaskerService.isRunning(this))
            startService(new Intent(this, TaskerService.class));
    }

}
