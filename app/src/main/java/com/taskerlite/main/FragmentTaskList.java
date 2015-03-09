package com.taskerlite.main;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.taskerlite.R;
import com.taskerlite.logic.ActionElement;
import com.taskerlite.logic.ProfileController;
import com.taskerlite.logic.ProfileController.*;
import com.taskerlite.logic.TaskElement;
import com.taskerlite.source.Icons;
import com.taskerlite.source.Screen;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentTaskList extends Fragment implements View.OnClickListener{

    private FragmentCallBack dataActivity;

    ProfileController profileController;

    LayoutInflater inflater;
    Activity activity;
    Context context;

    AppAdapter mAdapter;
    SwipeMenuListView mListView;

    ImageButton buttonPlus;
    ImageView logoPicture;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        dataActivity = (FragmentCallBack) activity;
        profileController = dataActivity.getProfileController();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        this.inflater = inflater;
        activity = getActivity();
        context  = getActivity();

        mListView = (SwipeMenuListView) view.findViewById(R.id.listView);
        mAdapter = new AppAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setMenuCreator(creator);
        mListView.setOnMenuItemClickListener(itemClickListener);
        mListView.setOnItemClickListener(onSceneClickListener);

        logoPicture = (ImageView) view.findViewById(R.id.logoId);
        buttonPlus = (ImageButton) view.findViewById(R.id.btnPlus);
        buttonPlus.setOnClickListener(this);

        logoPicture.post(new Runnable() {
            public void run() {
                int height = logoPicture.getHeight();
                int weight = logoPicture.getWidth();
                int iconSize = (int) getResources().getDimension(R.dimen.add_btn_size);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(new ViewGroup.MarginLayoutParams(iconSize, iconSize));
                lp.setMargins(weight - iconSize - iconSize/2, height - iconSize/2, 0, 0);
                buttonPlus.setLayoutParams(lp);

                //- Screen.dp2px(context, iconSize )
            }
        });

        return view;
    }

    SwipeMenuCreator creator = new SwipeMenuCreator() {
        @Override
        public void create(SwipeMenu menu) {

           int bgSize = getResources().getInteger(R.integer.swipe_menu_bg_size);
           bgSize = Screen.dp2px(context, bgSize);

           SwipeMenuItem onItem = new SwipeMenuItem(context);
           onItem.setBackground(null);
           onItem.setWidth(bgSize);
           onItem.setIcon(Icons.getInstance().getSwMenuStart());
           menu.addMenuItem(onItem);

           SwipeMenuItem offItem = new SwipeMenuItem(context);
           offItem.setBackground(null);
           offItem.setWidth(bgSize);
           offItem.setIcon(Icons.getInstance().getSwMenuStop());
           menu.addMenuItem(offItem);
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
                    for(TaskElement task : profileController.getProfile(position).getTaskList())
                        task.getTaskObject().start(context);
                    break;
                case 1:
                    for(TaskElement task : profileController.getProfile(position).getTaskList())
                        task.getTaskObject().stop(context);
                    break;
            }
            return false;
        }
    };

    @Override
    public void onClick(View view) {

        profileController.newProfile();
        goToBuilderFragment(profileController.getProfileListSize() - 1);
    }

    private void goToBuilderFragment(int index){

        dataActivity.setCurrentProfileIndex(index);
        dataActivity.openFragmentBuilder();
    }

    class AppAdapter extends BaseAdapter {

        public int getCount() {
            return profileController.getProfileListSize();
        }

        public Profile getItem(int position) {
            return profileController.getProfile(position);
        }

        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null)
                convertView = View.inflate(context, R.layout.list_item_profile, null);

            TextView t = (TextView) convertView.findViewById(R.id.profileNameID);
            t.setText(getItem(position).getName());
            LinearLayout iconsLay = (LinearLayout) convertView.findViewById(R.id.profileIconsPlaceId);
            iconsLay.removeAllViews();

            for(ActionElement action : getItem(position).getActionList()){
                ImageView img = new ImageView(context);
                img.setBackgroundDrawable(Icons.getInstance().getPreviewIcon(action.getActionType()));
                iconsLay.addView(img);
            }

            for(TaskElement task : getItem(position).getTaskList()){
                ImageView img = new ImageView(context);
                img.setBackgroundDrawable(Icons.getInstance().getPreviewIcon(task.getTaskType()));
                iconsLay.addView(img);
            }

            return convertView;
        }
    }
}