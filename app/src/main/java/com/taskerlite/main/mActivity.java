package com.taskerlite.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.taskerlite.R;
import com.taskerlite.logic.ProfileController;
import com.taskerlite.other.Flash;

public class mActivity extends FragmentActivity implements FragmentCallBack{

    private ProfileController profileController;
    private int currentProfile=0;

    FragmentTaskList fragmentTaskList;
    FragmentTaskBuilder fragmentTaskBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Icons.prepareResource(this);
        profileController = Flash.getProfileController();

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
    public ProfileController getProfileController() {
        return profileController;
    }

    @Override
    public int getCurrentProfileIndex() {
        return currentProfile;
    }

    @Override
    public void setCurrentProfileIndex(int index) {
        currentProfile = index;
    }

    @Override
    public void gotoFragmentList() {

        fragmentTaskList = new FragmentTaskList();

        getSupportFragmentManager().beginTransaction().
        setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right).
        replace(R.id.fragmentConteiner, new FragmentTaskList()).
        //addToBackStack(null).
        commit();
    }

    @Override
    public void gotoFragmentBuilder() {

        fragmentTaskBuilder = new FragmentTaskBuilder();

        getSupportFragmentManager().beginTransaction().
        setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).
        replace(R.id.fragmentConteiner, fragmentTaskBuilder).
        //addToBackStack(null).
        commit();
    }
}
