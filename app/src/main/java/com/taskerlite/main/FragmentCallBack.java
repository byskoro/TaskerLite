package com.taskerlite.main;

import com.taskerlite.logic.ProfileController;

public interface FragmentCallBack {

    abstract public ProfileController getProfileController();
    abstract public int getCurrentProfileIndex ();
    abstract public void setCurrentProfileIndex (int index);
    abstract public void gotoFragmentList();
    abstract public void gotoFragmentBuilder();
}
