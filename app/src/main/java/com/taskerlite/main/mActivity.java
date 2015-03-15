package com.taskerlite.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import com.taskerlite.R;
import com.taskerlite.logic.ProfileController;
import com.taskerlite.source.Eeprom;
import com.taskerlite.logic.ProfileController.Profile;
import com.taskerlite.source.Icons;

public class mActivity extends Activity implements FragmentCallBack {

    private FragmentTaskList fragmentTaskList;
    private FragmentTaskBuilder fragmentTaskBuilder;
    private FragmentPreference fragmentPreference;

    private ProfileController profileController;
    private int currentProfileIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Icons.prepareResource(this);
        profileController = Eeprom.getInstance(this).getProfileController();

        fragmentTaskBuilder = new FragmentTaskBuilder();
        fragmentTaskList    = new FragmentTaskList();
        fragmentPreference  = new FragmentPreference();

        addFragmentList();

        if(!TService.isRunning(this))
            startService(new Intent(this, TService.class));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        openPreference();

        return false;
    }

    public void addFragmentList(){

        getFragmentManager().beginTransaction().
        add(R.id.fragmentContainer, fragmentTaskList).
        commit();
    }

    public void openPreference(){

        getFragmentManager().beginTransaction().
        setCustomAnimations(R.anim.anim_visible, R.anim.anim_gone, R.anim.anim_visible, R.anim.anim_gone).
        replace(R.id.fragmentContainer, fragmentPreference).
        addToBackStack(null).
        commit();
    }

    @Override
    public ProfileController getProfileController() {
        return profileController;
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
    public int getCurrentProfileIndex() {
        return currentProfileIndex;
    }

    @Override
    public void returnToFragmentList() {

        getFragmentManager().popBackStack();
    }

    @Override
    public void openFragmentBuilder() {

        getFragmentManager().beginTransaction().
        setCustomAnimations(R.anim.anim_visible, R.anim.anim_gone, R.anim.anim_visible, R.anim.anim_gone).
        replace(R.id.fragmentContainer, fragmentTaskBuilder).
        addToBackStack(null).
        commit();
    }
}
