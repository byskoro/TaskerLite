package com.taskerlite.logic.tasks;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.taskerlite.R;

import java.util.List;

public class tApp extends mTask {

    private transient List<ApplicationInfo> mAppList;
    private transient mAdapter adapter;
    private transient Dialog mDialog;

    private String link = "";

	@Override
	public void start(Context context) {

        try {

            Intent intent = context.getPackageManager().getLaunchIntentForPackage(link);
            context.startActivity(intent);

        }catch (Exception e){ }
	}

    @Override
    public void show(final Context context) {

        mDialog = new Dialog(context);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_task_app);

        ListView lvMain = (ListView) mDialog.findViewById(R.id.listView);

        mAppList = context.getPackageManager().getInstalledApplications(0);
        adapter = new mAdapter(context);
        lvMain.setAdapter(adapter);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ApplicationInfo item = mAppList.get(position);
                setName(String.valueOf(item.loadLabel(context.getPackageManager())));
                link = item.packageName;
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

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
