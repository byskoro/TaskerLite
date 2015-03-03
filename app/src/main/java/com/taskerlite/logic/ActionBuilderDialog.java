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
import com.taskerlite.logic.ProfileController.*;
import com.taskerlite.logic.actions.aBootComplete;
import com.taskerlite.logic.actions.aScreenOff;
import com.taskerlite.logic.actions.aScreenOn;
import com.taskerlite.logic.actions.aTimer;
import com.taskerlite.main.FragmentCallBack;
import com.taskerlite.source.Types.*;

public class ActionBuilderDialog extends DialogFragment {

    private Profile profile;
    private ListView lvMain;
    private RoomArrayAdapter adapter;
    private FragmentCallBack dataActivity;

    private String[] actionsDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        dataActivity = (FragmentCallBack) getActivity();
        profile = dataActivity.getCurrentProfile();

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_action_list, container);

        lvMain = (ListView) view.findViewById(R.id.listview);

        actionsDescription = getResources().getStringArray(R.array.actionsList);

        adapter = new RoomArrayAdapter(getActivity(), actionsDescription);
        lvMain.setAdapter(adapter);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                        profile.addNewAction(new aTimer(), TYPES.A_TIME, 0, 0);
                        break;
                    case 1:
                        profile.addNewAction(new aBootComplete(getActivity()), TYPES.A_BOOT_COMPLETE, 0, 0);
                        break;
                    case 2:
                        profile.addNewAction(new aScreenOn(getActivity()), TYPES.A_SCREEN_ON, 0, 0);
                        break;
                    case 3:
                        profile.addNewAction(new aScreenOff(getActivity()), TYPES.A_SCREEN_OFF, 0, 0);
                        break;
                }

                dismiss();
            }
        });

        return view;
    }

    public class RoomArrayAdapter extends ArrayAdapter<String> {

        private Context context;

        public RoomArrayAdapter(Context context, String[] roomNames) {
            super(context, R.layout.dialog_action_list, roomNames);

            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.list_item_element, parent, false);

            ImageView imageView = (ImageView) rowView.findViewById(R.id.imageId);
            TypedArray actionsId = getResources().obtainTypedArray(R.array.actionsIcons);
            imageView.setImageResource(actionsId.getResourceId(position, -1));

            TextView textView = (TextView) rowView.findViewById(R.id.textDescriptionId);
            textView.setText(actionsDescription[position]);
            return rowView;
        }
    }
}
