package com.taskerlite.logic.actions;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.taskerlite.R;
import com.taskerlite.logic.SceneList.*;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TimePicker;

public class aTimer extends mAction {

	private int hour, minute;
	
	public aTimer(int hour, int minute){

		this.hour = hour;
		this.minute = minute;
	}

	@Override
	public boolean isMyAction(Context context, ACTION_TYPE type) {
		
		boolean state = false;
		
		if(type == ACTION_TYPE.TIMER){
			
			Calendar cal = new GregorianCalendar();
			
			if( cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == minute)
				state = true;			
		}
		
		return state;
	}

    @Override
    public void show(Context context) {

        Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_action_time);

        TimePicker timePicker = (TimePicker) mDialog.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentMinute(minute);
        timePicker.setCurrentHour(hour);

        mDialog.show();
    }
}
