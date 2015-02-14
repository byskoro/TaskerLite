package com.taskerlite.taskLogic;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

public class tApp extends mTask{
		
	String link;	
	
	public tApp(String link) {

		this.link = link;
	}

	@Override
	public void start(Context context) {
		
		Intent i = new Intent(Intent.ACTION_MAIN);
    	PackageManager manager = context.getPackageManager();
    	i = manager.getLaunchIntentForPackage(link).addCategory(Intent.CATEGORY_LAUNCHER);
    	context.startActivity(i);
	}

}
