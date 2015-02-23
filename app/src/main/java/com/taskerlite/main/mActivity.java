package com.taskerlite.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.taskerlite.R;
import com.taskerlite.other.Flash;
import com.taskerlite.logic.SceneList;

public class mActivity extends FragmentActivity {

    public static SceneList sceneList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TaskerIcons.getInstance(this);

        sceneList = Flash.getList();

        getSupportFragmentManager().beginTransaction().
        add(R.id.fragmentConteiner, new FragmentTaskList()).
        commit();

        if(!TService.isRunning(this))
            startService(new Intent(this, TService.class));
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().findFragmentById(R.id.fragmentConteiner) instanceof FragmentTaskList) {

            finish();

        }else {

            Flash.saveList(mActivity.sceneList);

            getSupportFragmentManager().beginTransaction().
            setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).
            replace(R.id.fragmentConteiner, new FragmentTaskList()).
            addToBackStack(null).
            commit();
        }
    }
}
