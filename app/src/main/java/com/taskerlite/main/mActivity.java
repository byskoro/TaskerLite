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

        Handler handlerLogic = new Handler();

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

            getFragmentManager().beginTransaction().
            replace(R.id.fragmentConteiner, new TaskList()).
            commit();
        }
    }
}
