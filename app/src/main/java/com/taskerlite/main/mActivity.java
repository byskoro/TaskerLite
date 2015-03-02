package com.taskerlite.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.taskerlite.R;
import com.taskerlite.logic.ProfileController;
import com.taskerlite.other.Flash;
import com.taskerlite.logic.ProfileController.Profile;

public class mActivity extends FragmentActivity implements FragmentCallBack{

    private FragmentTaskList fragmentTaskList;
    private FragmentTaskBuilder fragmentTaskBuilder;

    private ProfileController profileController;
    private int currentProfileIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Icons.prepareResource(this);
        profileController = Flash.getProfileController();

        fragmentTaskBuilder = new FragmentTaskBuilder();
        fragmentTaskList = new FragmentTaskList();

        gotoFragmentList();

        if(!TService.isRunning(this))
            startService(new Intent(this, TService.class));
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().findFragmentById(R.id.fragmentConteiner) instanceof FragmentTaskList)
            finish();
        else
            gotoFragmentList();
    }

    @Override
    public Profile getCurrentProfile() {
        return profileController.getProfile(currentProfileIndex);
    }

    @Override
    public void setCurrentProfileIndex(int index) {
        currentProfileIndex = index;
    }

    @Override
    public ProfileController getProfileController() {
        return profileController;
    }

    @Override
    public int getCurrentProfileIndex() {
        return currentProfileIndex;
    }

    @Override
    public void gotoFragmentList() {

        getSupportFragmentManager().beginTransaction().
        setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).
        replace(R.id.fragmentConteiner, new FragmentTaskList()).
        commit();
    }

    @Override
    public void gotoFragmentBuilder() {

        getSupportFragmentManager().beginTransaction().
        setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).
        replace(R.id.fragmentConteiner, fragmentTaskBuilder).
        commit();
    }
}
