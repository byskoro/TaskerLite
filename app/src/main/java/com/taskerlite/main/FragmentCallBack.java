package com.taskerlite.main;

import com.taskerlite.logic.ProfileController;
import com.taskerlite.logic.ProfileController.Profile;

public interface FragmentCallBack {

    abstract public ProfileController getProfileController();
    abstract public int  getCurrentProfileIndex ();
    abstract public Profile getCurrentProfile ();
    abstract public void setCurrentProfileIndex (int index);
    abstract public void returnToFragmentList();
    abstract public void openFragmentBuilder();
}
