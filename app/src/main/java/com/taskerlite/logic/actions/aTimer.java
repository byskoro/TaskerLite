package com.taskerlite.logic.actions;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.taskerlite.R;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import com.taskerlite.source.Types.*;

public class aTimer extends mAction {

	private int hour, minute;
	
	public aTimer(){

        Calendar cal = new GregorianCalendar();

		this.hour   = cal.get(Calendar.HOUR_OF_DAY);
		this.minute = cal.get(Calendar.MINUTE);
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
    public void show(FragmentManager fm) {

        UI ui = new UI();
        ui.setParent(this);
        ui.show(fm.beginTransaction(), "");
    }

    public static class UI extends DialogFragment {

        private aTimer action;
        private TimePicker timePicker;
        private EditText nameInput;
        private Button saveBtn;
        private LinearLayout clearRequestLay;

        public void setParent (aTimer action){
            this.action = action;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

            View view = inflater.inflate(R.layout.dialog_action_time, container);

            timePicker = (TimePicker) view.findViewById(R.id.timePicker);
            timePicker.setIs24HourView(true);
            timePicker.setCurrentMinute(action.minute);
            timePicker.setCurrentHour(action.hour);

            saveBtn = (Button) view.findViewById(R.id.saveBtnId);
            saveBtn.setOnClickListener(btnListener);
            nameInput = (EditText) view.findViewById(R.id.nameId);
            nameInput.setText(action.getName());
            nameInput.setOnEditorActionListener(textWatcher);

            clearRequestLay = (LinearLayout) view.findViewById(R.id.clearRequestLay);
            clearRequest(nameInput);

            return view;
        }

        TextView.OnEditorActionListener textWatcher = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                clearRequest(v);
                return false;
            }
        };

        View.OnClickListener btnListener =  new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    action.hour   = timePicker.getCurrentHour();
                    action.minute = timePicker.getCurrentMinute();

                    String name = String.valueOf(nameInput.getText());
                    action.setName(name);

                }catch(Exception e){ }

                dismiss();
            }
        };

        private void clearRequest(TextView textView){
            clearRequestLay.requestFocus();
            InputMethodManager imm = (InputMethodManager)textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(textView.getApplicationWindowToken(), 0);
        }
    }
}
