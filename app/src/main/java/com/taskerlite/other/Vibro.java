package com.taskerlite.other;

import android.content.Context;
import android.os.Vibrator;

public class Vibro {
	
	public static void playShort(Context context){
		
		 Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		 v.vibrate(500);
	}
}
