package com.taskerlite.main;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.taskerlite.R;
import com.taskerlite.logic.SceneList;
import com.taskerlite.logic.SceneList.*;
import com.taskerlite.other.Screen;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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

        buttonPlus = (ImageButton) view.findViewById(R.id.btnPlus);
        buttonPlus.setOnClickListener(this);

        return view;
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {
        @Override
        public void create(SwipeMenu menu) {

            int iconSize = getResources().getInteger(R.integer.swipe_menu_icon_size);
            int bgSize   = getResources().getInteger(R.integer.swipe_menu_bg_size);

            Bitmap tmpBigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.swipe_start);
            Bitmap tmpIcon    = Bitmap.createScaledBitmap(tmpBigIcon, Screen.dp2px(context, iconSize), Screen.dp2px(context, iconSize), true);

            SwipeMenuItem onItem = new SwipeMenuItem(context);
            onItem.setBackground(new ColorDrawable(Color.rgb(0xC6, 0xB7, 0xA9)));
            onItem.setWidth(Screen.dp2px(context, bgSize));
            onItem.setIcon(new BitmapDrawable(getResources(), tmpIcon));
            menu.addMenuItem(onItem);

            tmpBigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.swipe_pause);
            tmpIcon    = Bitmap.createScaledBitmap(tmpBigIcon, Screen.dp2px(context, iconSize), Screen.dp2px(context, iconSize), true);

            SwipeMenuItem offItem = new SwipeMenuItem(context);
            offItem.setBackground(new ColorDrawable(Color.rgb(0xC6, 0xB7, 0xA9)));
            offItem.setWidth(Screen.dp2px(context, bgSize));
            offItem.setIcon(new BitmapDrawable(getResources(), tmpIcon));
            menu.addMenuItem(offItem);

            tmpBigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.swipe_delete);
            tmpIcon    = Bitmap.createScaledBitmap(tmpBigIcon, Screen.dp2px(context, iconSize), Screen.dp2px(context, iconSize), true);

            SwipeMenuItem deleteItem = new SwipeMenuItem(context);
            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xC6, 0xB7, 0xA9)));
            deleteItem.setWidth(Screen.dp2px(context, bgSize));
            deleteItem.setIcon(new BitmapDrawable(getResources(), tmpIcon));
            menu.addMenuItem(deleteItem);
        }
    };

    AdapterView.OnItemClickListener onSceneClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            goToBuilderFragment(position);
        }
    };

    OnMenuItemClickListener itemClickListener = new OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

            switch (index) {
                case 0:

                    break;
                case 1:

                    break;
                case 2:
                    sceneList.removeSceneFromList(position);
                    mAdapter.notifyDataSetChanged();

                    break;
            }
            return false;
        }
    };

    @Override
    public void onClick(View view) {

        sceneList.addNewScene("");
        goToBuilderFragment(sceneList.getSceneListSize() - 1);
    }

    private void goToBuilderFragment(int index){

        getFragmentManager().beginTransaction().
        setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right).
        replace(R.id.fragmentConteiner, FragmentTaskBuilder.getInstance(index)).
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