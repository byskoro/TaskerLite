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
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams (iconSize, iconSize);
                lp.setMargins(weight - iconSize - iconSize/2, height - iconSize/2, 0, 0);
                buttonPlus.setLayoutParams(lp);
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

        ProfileNameDialog nameDialog = new ProfileNameDialog();
        nameDialog.show(getFragmentManager(), "profileNameDialog");
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

            return convertView;
        }
    }

    public static class ProfileNameDialog extends DialogFragment {

        private FragmentCallBack dataActivity;
        private EditText nameInput;
        private Button saveBtn, cancelBtn;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            dataActivity   = (FragmentCallBack) getActivity();

            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = inflater.inflate(R.layout.dialog_name, container);
            nameInput = (EditText) view.findViewById(R.id.nameId);
            saveBtn = (Button) view.findViewById(R.id.saveBtnId);
            cancelBtn = (Button) view.findViewById(R.id.cancelId);

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String name = String.valueOf(nameInput.getText());

                    if (name.length()!=0){

                        dataActivity.getProfileController().newProfile(name);
                        dataActivity.setCurrentProfileIndex(dataActivity.getProfileController().getProfileListSize() - 1);
                        dataActivity.openFragmentBuilder();

                        dismiss();

                    } else
                        Toast.makeText(getActivity(), "Not correct name", Toast.LENGTH_LONG).show();
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dismiss();
                }
            });

            return view;
        }
    }
}