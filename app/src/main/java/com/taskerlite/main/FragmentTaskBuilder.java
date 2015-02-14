package com.taskerlite.main;

import com.taskerlite.R;
import com.taskerlite.ui.TaskerView;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentTaskBuilder extends Fragment {

	TaskerView taskerView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_task_builder, container, false);

		taskerView = (TaskerView) view.findViewById(R.id.dot);

		return view;
	}
	
}
