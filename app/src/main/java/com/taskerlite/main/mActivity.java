package com.taskerlite.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.taskerlite.R;
import com.taskerlite.other.Flash;
import com.taskerlite.logic.SceneList;
import com.taskerlite.other.Screen;

public class mActivity extends Activity {

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

        getFragmentManager().beginTransaction().
        add(R.id.fragmentConteiner, new FragmentTaskList()).
        commit();

        if(!TService.isRunning(this))
            startService(new Intent(this, TService.class));
    }

    @Override
    public void onBackPressed() {

        if (getFragmentManager().findFragmentById(R.id.fragmentConteiner) instanceof FragmentTaskList) {

            finish();

        }else {

            Flash.saveList(mActivity.sceneList);

            getFragmentManager().beginTransaction().
                    setCustomAnimations(R.animator.slide_in_left2, R.animator.slide_in_right2).
                    replace(R.id.fragmentConteiner, new FragmentTaskList()).
            commit();
        }
    }
}
