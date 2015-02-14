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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentTaskList extends Fragment {

    SceneL sceneList;

    LayoutInflater inflater;
    Activity activity;
    Context context;

    AppAdapter mAdapter;
    SwipeMenuListView mListView;

    ImageButton buttonPlus;
    LinearLayout topLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        this.inflater = inflater;
        activity = getActivity();
        context = (Context) getActivity();

        sceneList = MainActivity.sceneList;

        mListView = (SwipeMenuListView) view.findViewById(R.id.listView);
        mAdapter = new AppAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setMenuCreator(creator);
        mListView.setOnMenuItemClickListener(itemClickListener);
        mListView.setOnItemClickListener(onSceneClickListener);


        topLayout = (LinearLayout) view.findViewById(R.id.topLayout);
        buttonPlus = (ImageButton) view.findViewById(R.id.btnPlus);

        topLayout.post(new Runnable() {
            public void run() {
                int height = topLayout.getHeight();
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(dp2px(56), dp2px(56)));
                lp.setMargins(dp2px(20), height - dp2px(56 / 2), 0, 0);
                buttonPlus.setLayoutParams(lp);
            }
        });
        return view;
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {
        @Override
        public void create(SwipeMenu menu) {

            SwipeMenuItem onItem = new SwipeMenuItem(context);
            onItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
            onItem.setWidth(dp2px(90));
            onItem.setTitle("ON");
            onItem.setTitleSize(18);
            onItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(onItem);

            SwipeMenuItem offItem = new SwipeMenuItem(context);
            offItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
            offItem.setWidth(dp2px(90));
            offItem.setTitle("OFF");
            offItem.setTitleSize(18);
            offItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(offItem);

            SwipeMenuItem deleteItem = new SwipeMenuItem(context);
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
            deleteItem.setWidth(dp2px(90));
            deleteItem.setIcon(R.drawable.ic_delete);
            menu.addMenuItem(deleteItem);
        }
    };

    AdapterView.OnItemClickListener onSceneClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            getFragmentManager().beginTransaction().
                    replace(R.id.fragmentConteiner, MainActivity.taskBuilderFragment).
                    commit();
        }
    };

    OnMenuItemClickListener itemClickListener = new OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

            switch (index) {
                case 0:

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