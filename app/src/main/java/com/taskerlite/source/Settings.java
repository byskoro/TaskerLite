package com.taskerlite.source;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.GsonBuilder;
import com.taskerlite.logic.ProfileController;

public class Settings {

    private static Settings instance = null;
    private Context context;

    public static Settings getInstance(Context context){

        if(instance == null)
            instance = new Settings(context);

        return instance;
    }

    public static Settings getInstance(){

        return instance;
    }

    private Settings(Context context) {
        this.context = context;
    }

    public void saveList(ProfileController obj) {

        SharedPreferences dataPreferences = context.getSharedPreferences("settings", context.MODE_PRIVATE);
        String data = new GsonBuilder().create().toJson(obj);
        dataPreferences.edit().putString("data", data).commit();
    }

    public ProfileController getProfileController() {

        SharedPreferences dataPreferences = context.getSharedPreferences("settings", context.MODE_PRIVATE);
        String data = dataPreferences.getString("data", "N/A");

        ProfileController profileController;

        try {

            profileController = new GsonBuilder().create().fromJson(data, ProfileController.class);

        }catch (Exception e){

            profileController =  new ProfileController();

        }

        return profileController;
    }

    public String getRawData() {

        SharedPreferences dataPreferences = context.getSharedPreferences("settings", context.MODE_PRIVATE);

        return dataPreferences.getString("data", "N/A");
    }

}
