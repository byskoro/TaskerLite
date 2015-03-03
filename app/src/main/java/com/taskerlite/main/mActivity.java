package com.taskerlite.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.taskerlite.R;
import com.taskerlite.logic.ProfileController;
import com.taskerlite.other.Flash;
import com.taskerlite.logic.ProfileController.Profile;

public class mActivity extends Activity implements FragmentCallBack{

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

        addFragmentLis();

        if(!TService.isRunning(this))
            startService(new Intent(this, TService.class));
    }
/*
    @Override
    public void onBackPressed() {

        if (getFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof FragmentTaskList)
            finish();
        else
            returnToFragmentList();
    }
*/
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

    public void addFragmentLis(){

        getFragmentManager().beginTransaction().
        add(R.id.fragmentContainer, fragmentTaskList).
        commit();
    }

    @Override
    public void returnToFragmentList() {

        getFragmentManager().popBackStack();
    }

    @Override
    public void gotoFragmentBuilder() {

        getFragmentManager().beginTransaction().
        setCustomAnimations( R.anim.anim_visible, R.anim.anim_gone, R.anim.anim_visible, R.anim.anim_gone).
        replace(R.id.fragmentContainer, fragmentTaskBuilder).
        addToBackStack(null).
        commit();
    }
}
