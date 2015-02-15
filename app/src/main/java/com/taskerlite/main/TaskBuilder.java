package com.taskerlite.main;

import com.taskerlite.R;
import com.taskerlite.other.Vibro;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TaskBuilder extends Fragment {

	TaskerBuilderView taskerView;
    Context context;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_task_builder, container, false);
        context = getActivity();

		taskerView = (TaskerBuilderView) view.findViewById(R.id.dot);
        taskerView.setViewCallBack(viewCallBack);

		return view;
	}

    TaskerBuilderView.ViewCallBack viewCallBack = new TaskerBuilderView.ViewCallBack(){

        @Override
        public void shortPress() {
            Vibro.playShort(context);
        }

        @Override
        public void longPress() {
            Vibro.playLong(context);
        }

        @Override
        public void movement() {
            Vibro.playMovement(context);
            taskerView.postInvalidate();
        }
    };
	
}
