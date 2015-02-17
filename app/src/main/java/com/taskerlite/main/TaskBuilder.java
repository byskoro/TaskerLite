package com.taskerlite.main;

import com.taskerlite.R;
import com.taskerlite.logic.ActionElement;
import com.taskerlite.logic.SceneList.*;
import com.taskerlite.logic.TaskElement;
import com.taskerlite.other.Vibro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

public class TaskBuilder extends Fragment {

    private TaskerBuilderView taskerView;
    private Context context;
    private Scene scene;

    private float iconSize = mActivity.iconSize;
    private static int sceneIndex;

    Paint p;

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

        p = new Paint();
        p.setColor(Color.WHITE);
        p.setTextSize(35);
        p.setTextAlign(Paint.Align.CENTER);

		return view;
	}

    TaskerBuilderView.ViewCallBack viewCallBack = new TaskerBuilderView.ViewCallBack(){

        @Override
        public void shortPress(MotionEvent event) {

            if(findSelectedTask(event) != null || findSelectedAction(event) != null) {

                Toast.makeText(getActivity(),"Open element", Toast.LENGTH_SHORT).show();
                Vibro.playShort(context);
            }
        }

        @Override
        public void longPress(MotionEvent event) {

            if(findSelectedTask(event) != null || findSelectedAction(event) != null) {

                Toast.makeText(getActivity(),"Select element", Toast.LENGTH_SHORT).show();

            }else {

                showMenuDialog();
                Vibro.playLong(context);
            }
        }

        @Override
        public void movement(MotionEvent event) {

            if(findSelectedTask(event) != null) {

                findSelectedTask(event).setNewCoordinate(event);
                Vibro.playMovement(context);
                taskerView.postInvalidate();

            } else if(findSelectedAction(event) != null) {

                findSelectedAction(event).setNewCoordinate(event);
                Vibro.playMovement(context);
                taskerView.postInvalidate();

            }
        }

        @Override
        public void onDrawView(Canvas canvas, MotionEvent event) {

            try{

                // 1. Take all resource and draw picture
                for(ActionElement action : scene.getActionList()){
                    Bitmap icon = action.getIcon(context, iconSize);
                    canvas.drawBitmap(icon, action.getX(), action.getY(), null);
                    float textX = action.getX() + iconSize/2;
                    float textY = action.getY() + iconSize*1.3f;
                    canvas.drawText(action.getActionName(), textX, textY, p);
                }

                for(TaskElement task : scene.getTaskList()){
                    Bitmap icon = task.getIcon(context, iconSize);
                    canvas.drawBitmap(icon, task.getX(), task.getY(), null);
                    float textX = task.getX() + iconSize/2;
                    float textY = task.getY() + iconSize*1.3f;
                    canvas.drawText(task.getTaskName(), textX, textY, p);
                }

                // 2. If present relationship action - task draw a lines



            }catch (Exception e){ }
        }
    };

    private TaskElement findSelectedTask(MotionEvent event){

        for(TaskElement task : scene.getTaskList()){
            if(task.isSelected(event, iconSize))
                return task;
        }
        return null;
    }

    private ActionElement findSelectedAction(MotionEvent event){

        for(ActionElement action : scene.getActionList()){
            if(action.isSelected(event, iconSize))
                return action;
        }
        return null;
    }

    private void showMenuDialog(){

        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_menu);
        dialog.show();
    }
}
