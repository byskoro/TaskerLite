package com.taskerlite.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.taskerlite.R;
import com.taskerlite.logic.ProfileController;
import com.taskerlite.other.Flash;
import java.util.Calendar;

public class mActivity extends FragmentActivity implements FragmentTaskBuilder.DataActivity, FragmentTaskList.DataActivity{

    private ProfileController profileController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TaskerIcons.getInstance(this);

        profileController = Flash.getProfileList();

        getSupportFragmentManager().beginTransaction().
        add(R.id.fragmentConteiner, new FragmentTaskList()).
        commit();

        TimeNotification timeNotification = new TimeNotification();
        timeNotification.startNotify(this);
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().findFragmentById(R.id.fragmentConteiner) instanceof FragmentTaskList) {

            finish();

        }else {

            getSupportFragmentManager().beginTransaction().
            setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).
            replace(R.id.fragmentConteiner, new FragmentTaskList()).
            addToBackStack(null).
            commit();
        }
    }

    @Override
    public ProfileController taskBuilderAskProfileController() {
        return profileController;
    }

    @Override
    public ProfileController taskListAskProfileController() {
        return profileController;
    }
}
