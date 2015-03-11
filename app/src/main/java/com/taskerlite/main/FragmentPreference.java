package com.taskerlite.main;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.taskerlite.R;

public class FragmentPreference extends PreferenceFragment {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }
}
