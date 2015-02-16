package com.taskerlite.main;

import com.taskerlite.R;
import com.taskerlite.logic.SceneList.*;
import com.taskerlite.logic.TaskElement;
import com.taskerlite.other.Vibro;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class TaskBuilder extends Fragment {

    private TaskerBuilderView taskerView;
    private Context context;
    private Scene scene;

    private int iconSize = mActivity.iconSize;
    private static int sceneIndex;

    public static TaskBuilder getInstance(int sceneIndex){
        TaskBuilder.sceneIndex = sceneIndex;
        return new TaskBuilder();
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_task_builder, container, false);
        context = getActivity();

		taskerView = (TaskerBuilderView) view.findViewById(R.id.dot);
        taskerView.setViewCallBack(viewCallBack);

        scene = mActivity.sceneList.getScene(sceneIndex);

		return view;
	}

    TaskerBuilderView.ViewCallBack viewCallBack = new TaskerBuilderView.ViewCallBack(){

        @Override
        public void shortPress(MotionEvent event) {

            Vibro.playShort(context);
        }

        @Override
        public void longPress(MotionEvent event) {

            Vibro.playLong(context);
        }

        @Override
        public void movement(MotionEvent event) {

            Vibro.playMovement(context);
            taskerView.postInvalidate();
        }

        @Override
        public void onDrawView(Canvas canvas, MotionEvent event) {

            // 1. Take all resource and draw picture
            // 2. If present relationship draw lines

            try{

                canvas.drawColor(0xffff0000);

                for(TaskElement task : scene.getTaskList()){
                    Bitmap icon = task.getIcon(context, iconSize);
                    canvas.drawBitmap(icon, task.getXCoordinate(), task.getYCoordinate(), null);
                }

            }catch (Exception e){ }
        }
    };
}
