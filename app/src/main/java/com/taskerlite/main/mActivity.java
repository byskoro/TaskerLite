package com.taskerlite.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.taskerlite.R;
import com.taskerlite.other.Flash;
import com.taskerlite.logic.SceneList;
import com.taskerlite.other.Screen;

public class mActivity extends FragmentActivity {

    public static SceneList sceneList;
    public static int iconSizeElement;
    public static int iconSizeDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconSizeElement = Screen.getWidth(this)/getResources().getInteger(R.integer.icon_divider);
        iconSizeDelete  = iconSizeElement/3;

        sceneList = Flash.getList();

        getSupportFragmentManager().beginTransaction().
        add(R.id.fragmentConteiner, new TaskListFragment()).
        commit();

        if(!TService.isRunning(this))
            startService(new Intent(this, TService.class));
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().findFragmentById(R.id.fragmentConteiner) instanceof TaskListFragment) {

            finish();

        }else {

            Flash.saveList(mActivity.sceneList);

            getSupportFragmentManager().beginTransaction().
            setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).
            replace(R.id.fragmentConteiner, new TaskListFragment()).
            addToBackStack(null).
            commit();
        }
    }
}
