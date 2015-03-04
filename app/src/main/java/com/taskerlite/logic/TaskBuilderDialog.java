package com.taskerlite.logic;

import android.app.DialogFragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.taskerlite.R;
import com.taskerlite.logic.tasks.tAccessPoint;
import com.taskerlite.logic.tasks.tApp;
import com.taskerlite.logic.tasks.tMobileData;
import com.taskerlite.logic.tasks.tScreen;
import com.taskerlite.logic.tasks.tWiFi;
import com.taskerlite.main.FragmentCallBack;
import com.taskerlite.source.Types.TYPES;

public class TaskBuilderDialog extends DialogFragment {

    private ProfileController.Profile profile;
    private ListView lvMain;
    private mAdapter adapter;
    private FragmentCallBack dataActivity;

    private String[] actionsDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dataActivity = (FragmentCallBack) getActivity();
        profile = dataActivity.getCurrentProfile();

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_action_list, container);

        lvMain = (ListView) view.findViewById(R.id.listview);

        actionsDescription = getResources().getStringArray(R.array.tasksList);

        adapter = new mAdapter(getActivity(), actionsDescription);
        lvMain.setAdapter(adapter);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                        profile.addNewTask(new tApp(), TYPES.T_APP, 0, 0);
                    break;
                    case 1:
                        profile.addNewTask(new tAccessPoint(), TYPES.T_ACCESS_POINT, 0, 0);
                        break;
                    case 2:
                        profile.addNewTask(new tMobileData(), TYPES.T_THREE_G, 0, 0);
                        break;
                    case 3:
                        profile.addNewTask(new tWiFi(), TYPES.T_WIFI, 0, 0);
                        break;
                    case 4:
                        profile.addNewTask(new tScreen(), TYPES.T_SCREEN, 0, 0);
                        break;
                }

                dismiss();
            }
        });

        return view;
    }

    public class mAdapter extends ArrayAdapter<String> {

        private Context context;

        public mAdapter(Context context, String[] roomNames) {
            super(context, R.layout.dialog_action_list, roomNames);

            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.list_item_element, parent, false);

            ImageView imageView = (ImageView) rowView.findViewById(R.id.imageId);
            TypedArray actionsId = getResources().obtainTypedArray(R.array.tasksIcons);
            imageView.setImageResource(actionsId.getResourceId(position, -1));

            TextView textView = (TextView) rowView.findViewById(R.id.textDescriptionId);
            textView.setText(actionsDescription[position]);
            return rowView;
        }
    }
}
