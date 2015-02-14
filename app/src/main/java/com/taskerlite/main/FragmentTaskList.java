package com.taskerlite.main;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.taskerlite.R;
import com.taskerlite.taskLogic.SceneL;
import com.taskerlite.taskLogic.SceneL.*;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FragmentTaskList extends Fragment {

	SceneL sceneList;
	
	LayoutInflater inflater;
	Activity activity;
	Context context;
	
	AppAdapter mAdapter;
	SwipeMenuListView mListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_task_list, container, false);

		this.inflater = inflater;
		activity = getActivity();
		context  = getActivity();

        sceneList = MainActivity.sceneList;

		mListView = (SwipeMenuListView) view.findViewById(R.id.listView);
		mAdapter = new AppAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setMenuCreator(creator);
		mListView.setOnMenuItemClickListener(itemClickListener);
		
		return view;
	}
	
	SwipeMenuCreator creator = new SwipeMenuCreator() {
		@Override
		public void create(SwipeMenu menu) {
			// create "open" item
			SwipeMenuItem openItem = new SwipeMenuItem(context);
			openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
			// set item width
			openItem.setWidth(dp2px(90));
			// set item title
			openItem.setTitle("Activate");
			// set item title fontsize
			openItem.setTitleSize(18);
			// set item title font color
			openItem.setTitleColor(Color.WHITE);
			// add to menu
			menu.addMenuItem(openItem);

			// create "delete" item
			SwipeMenuItem deleteItem = new SwipeMenuItem(context);
			// set item background
			deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F, 0x25)));
			// set item width
			deleteItem.setWidth(dp2px(90));
			// set a icon
			deleteItem.setIcon(R.drawable.ic_delete);
			// add to menu
			menu.addMenuItem(deleteItem);
		}
	};
	
	OnMenuItemClickListener itemClickListener = new OnMenuItemClickListener() {
		@Override
		public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
			switch (index) {
			case 0:
				
				getFragmentManager().beginTransaction().
				replace(R.id.fragmentConteiner, new FragmentTaskBuilder()).
				commit();
				
				break;
			case 1:
				sceneList.removeSceneFromList(position);
				mAdapter.notifyDataSetChanged();
				break;
			}
			return false;
		}
	};
	
	class AppAdapter extends BaseAdapter {
		
		public int getCount() {
			return sceneList.getSceneListSize();
		}

		public Scene getItem(int position) {
			return sceneList.getScene(position);
		}

		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (convertView == null) 
				convertView = View.inflate(context, R.layout.item_list_scene, null);
			
			TextView t = (TextView) convertView.findViewById(R.id.sceneNameID);
			t.setText(getItem(position).getName());
			
			return convertView;
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
	}
}

/*
	
	sl.addNewSnene("Scene 1");
	
	sl.getScene(0).addNewAction("Action_1", new aTimer(20, 31), ACTION_TYPE.TIMER, 0, 0);
	sl.getScene(0).addNewTask("startSkype", new tApp("com.skype.raider"), TASK_TYPE.APP);
	

*/