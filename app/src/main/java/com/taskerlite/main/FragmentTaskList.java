package com.taskerlite.main;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuLayout;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.taskerlite.R;
import com.taskerlite.logic.SceneList;
import com.taskerlite.logic.SceneList.*;
import com.taskerlite.other.Screen;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentTaskList extends Fragment implements View.OnClickListener{

    SceneList sceneList;

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
        context  = getActivity();

        sceneList = mActivity.sceneList;

        mListView = (SwipeMenuListView) view.findViewById(R.id.listView);
        mAdapter = new AppAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setMenuCreator(creator);
        mListView.setOnMenuItemClickListener(itemClickListener);
        mListView.setOnItemClickListener(onSceneClickListener);
        mListView.setOnItemLongClickListener(onLongClickListener);

        buttonPlus = (ImageButton) view.findViewById(R.id.btnPlus);
        buttonPlus.setOnClickListener(this);

        return view;
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {
        @Override
        public void create(SwipeMenu menu) {

            SwipeMenuItem onItem = new SwipeMenuItem(context);
            onItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
            onItem.setWidth(Screen.dp2px(context, 90));
            onItem.setTitle("ON");
            onItem.setTitleSize(18);
            onItem.setTitleColor(Color.WHITE);
            menu.addMenuItem(onItem);

            SwipeMenuItem deleteItem = new SwipeMenuItem(context);
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
            deleteItem.setWidth(Screen.dp2px(context, 90));
            deleteItem.setIcon(R.drawable.ic_delete);
            menu.addMenuItem(deleteItem);
        }
    };

    AdapterView.OnItemClickListener onSceneClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            getFragmentManager().beginTransaction().
            replace(R.id.fragmentConteiner, FragmentTaskBuilder.getInstance(position)).
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

    AdapterView.OnItemLongClickListener onLongClickListener = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long l) {

            Toast.makeText(context, index + " long click", Toast.LENGTH_LONG).show();

            return true;
        }
    };

    @Override
    public void onClick(View view) {

        sceneList.addNewScene("");

        getFragmentManager().beginTransaction().
        replace(R.id.fragmentConteiner, FragmentTaskBuilder.getInstance(sceneList.getSceneListSize() - 1)).
        commit();
    }

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
}