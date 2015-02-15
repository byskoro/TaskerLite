package com.taskerlite.logic.actions;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.taskerlite.logic.SceneList.*;

import android.content.Context;

public class aTimer extends mAction {

	private int hour, minutes;
	
	public aTimer(int hour, int minute){

		this.hour = hour;
		this.minutes = minute;
	}

	@Override
	public boolean isMyAction(Context context, ACTION_TYPE type) {
		
		boolean state = false;
		
		if(type == ACTION_TYPE.TIMER){
			
			Calendar cal = new GregorianCalendar();
			
			if( cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == minutes)
				state = true;			
		}
		
		return state;
	}
}
