package com.taskerlite.logic.tasks;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.taskerlite.R;
import com.taskerlite.other.NotificationUtils;

import java.util.List;

public class tApp extends mTask {

    private String packageName = "";

	@Override
	public void start(Context context) {

        try {

            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            context.startActivity(intent);

            String header = context.getResources().getString(R.string.t_app_short);
            NotificationUtils.getInstance(context).createInfoNotification(header, getName());

        }catch (Exception e){ }
	}

    @Override
    public void stop(Context context) {

        /*
        List<ApplicationInfo> packages;
    PackageManager pm;
    pm = getPackageManager();
    //get a list of installed apps.
    packages = pm.getInstalledApplications(0);

    ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);

   for (ApplicationInfo packageInfo : packages) {
        if((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM)==1)continue;
        if(packageInfo.packageName.equals("mypackage")) continue;
        mActivityManager.killBackgroundProcesses(packageInfo.packageName);
   }
         */

    }

    @Override
    public void show(FragmentManager fm) {

        UI ui = new UI();
        ui.setParent(this);
        ui.show(fm.beginTransaction(), "T_APP");
    }

    public static class UI extends DialogFragment{

        private List<ApplicationInfo> mAppList;
        private mAdapter adapter;
        private tApp task;

        public void setParent (tApp task){
            this.task = task;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

            View view = inflater.inflate(R.layout.dialog_task_app, container);
            ListView lvMain = (ListView) view.findViewById(R.id.listView);

            mAppList = getActivity().getPackageManager().getInstalledApplications(0);
            adapter = new mAdapter(getActivity());
            lvMain.setAdapter(adapter);
            lvMain.setOnItemClickListener(listListener);

            return view;
        }

        AdapterView.OnItemClickListener listListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ApplicationInfo item = mAppList.get(position);
                task.setName(String.valueOf(item.loadLabel(getActivity().getPackageManager())));
                task.packageName = item.packageName;
                dismiss();
            }
        };

        class mAdapter extends BaseAdapter {

            private Context context;

            public mAdapter(Context context){
                this.context = context;
            }

            public int getCount() {
                return mAppList.size();
            }

            public ApplicationInfo getItem(int position) {
                return mAppList.get(position);
            }

            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View rowView = View.inflate(context, R.layout.list_item_element, null);

                ApplicationInfo item = getItem(position);
                ImageView imageView = (ImageView) rowView.findViewById(R.id.imageId);
                imageView.setImageDrawable(item.loadIcon(context.getPackageManager()));
                TextView textView = (TextView) rowView.findViewById(R.id.textDescriptionId);
                textView.setText(item.loadLabel(context.getPackageManager()));
                return rowView;
            }
        }
    }
}
