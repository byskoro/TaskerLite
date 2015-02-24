package com.taskerlite.logic.actions;

import java.util.Calendar;
import java.util.GregorianCalendar;
import com.taskerlite.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import com.taskerlite.main.TaskerTypes.*;

public class aTimer extends mAction {

	private int hour, minute;
	
	public aTimer(int hour, int minute){

		this.hour = hour;
		this.minute = minute;
	}

	@Override
	public boolean isMyAction(Context context, TYPES type) {
		
		boolean state = false;
		
		if(type == TYPES.A_TIME){
			
			Calendar cal = new GregorianCalendar();
			
			if( cal.get(Calendar.HOUR_OF_DAY) == hour && cal.get(Calendar.MINUTE) == minute)
				state = true;			
		}
		
		return state;
	}

    @Override
    public void show(Context context) {

        final Dialog mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_action_time);

        final TimePicker timePicker = (TimePicker) mDialog.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentMinute(minute);
        timePicker.setCurrentHour(hour);

        Button saveBtn = (Button) mDialog.findViewById(R.id.saveBtnId);
        final EditText nameInput = (EditText) mDialog.findViewById(R.id.nameId);
        nameInput.setText(getName());

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    hour   = timePicker.getCurrentHour();
                    minute = timePicker.getCurrentMinute();

                    String name = String.valueOf(nameInput.getText());
                    setName(name);

                }catch(Exception e){

                } finally {
                    mDialog.dismiss();
                }
            }
        });

        mDialog.show();
    }
}
