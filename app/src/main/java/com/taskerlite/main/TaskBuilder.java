package com.taskerlite.main;

import com.taskerlite.R;
import com.taskerlite.TaskerBuilderView;
import com.taskerlite.logic.ActionElement;
import com.taskerlite.logic.SceneList.*;
import com.taskerlite.logic.TaskElement;
import com.taskerlite.other.Vibro;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

public class TaskBuilder extends Fragment {

    private TaskerBuilderView taskerView;
    private Context context;
    private Scene scene;
    private static int sceneIndex;

    private float iconSize = mActivity.iconSize;
    Bitmap selectIcon;

    Dialog dialogMenu;
    Dialog dialogActions;
    Dialog dialogTasks;

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

        Bitmap selectBigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_select);
        selectIcon = Bitmap.createScaledBitmap(selectBigIcon, (int)iconSize, (int)iconSize, true);

		return view;
	}

    TaskerBuilderView.ViewCallBack viewCallBack = new TaskerBuilderView.ViewCallBack(){

        private boolean isActionElementMoving = false;

        @Override
        public void shortPress(MotionEvent event) {

            if(findTouchedTask(event) != null || findTouchedAction(event) != null) {

                Toast.makeText(getActivity(),"Open element", Toast.LENGTH_SHORT).show();

                //findTouchedTask(event).getTaskObject().show();

                Vibro.playShort(context);
            }

            unselectAllElements();

            taskerView.postInvalidate();
        }

        @Override
        public void longPress(MotionEvent event) {

            if (findTouchedAction(event) != null) {

                if (!findTouchedAction(event).isElementSelect()) {
                    findTouchedAction(event).selectElement();
                    checkConnection();
                }else
                    findTouchedAction(event).unSelectElement();

            } else if (findTouchedTask(event) != null) {

                if (!findTouchedTask(event).isElementSelect()) {
                    findTouchedTask(event).selectElement();
                    checkConnection();
                }else
                    findTouchedTask(event).unSelectElement();

            }else{

                unselectAllElements();
                showMenuDialog();
            }

            taskerView.postInvalidate();
            Vibro.playLong(context);
        }

        @Override
        public void movement(MotionEvent event) {

            if(findTouchedTask(event) != null && isActionElementMoving == false) {

                findTouchedTask(event).setNewCoordinate(event);
                Vibro.playMovement(context);
                taskerView.postInvalidate();

            } else if(findTouchedAction(event) != null) {

                isActionElementMoving = true;
                findTouchedAction(event).setNewCoordinate(event);
                Vibro.playMovement(context);
                taskerView.postInvalidate();
            }

            unselectAllElements();
        }

        @Override
        public void onDrawView(Canvas canvas, MotionEvent event) {

            try{

                Paint p = new Paint();
                p.setColor(Color.WHITE);
                p.setTextSize(getResources().getInteger(R.integer.icon_text_size));
                p.setTextAlign(Paint.Align.CENTER);
                p.setStrokeWidth(5);

                // 1. If present relationship between action and task - draw a lines
                for (ActionElement action : scene.getActionList()) {
                    for (TaskElement task : scene.getTaskList()) {
                        if (action.isTaskElementIdPresent(task.getTaskId())) {
                            canvas.drawLine(action.getX() + iconSize/2, action.getY() + iconSize/2,
                                    task.getX() + iconSize/2, task.getY() + iconSize/2, p);
                        }
                    }
                }

                // 2.Draw select icons
                for(ActionElement action : scene.getActionList())
                    if(action.isElementSelect())
                        canvas.drawBitmap(selectIcon, action.getX(), action.getY(), null);
                for(TaskElement task : scene.getTaskList())
                    if(task.isElementSelect())
                        canvas.drawBitmap(selectIcon, task.getX(), task.getY(), null);

                // 3. Take all resource and draw picture
                for(ActionElement action : scene.getActionList()){
                    Bitmap icon = action.getIcon(context, iconSize);
                    canvas.drawBitmap(icon, action.getX(), action.getY(), null);
                    float textX = action.getX() + iconSize/2;
                    float textY = action.getY() + iconSize + getResources().getInteger(R.integer.icon_text_margin);
                    canvas.drawText(action.getActionName(), textX, textY, p);
                }

                for(TaskElement task : scene.getTaskList()){
                    Bitmap icon = task.getIcon(context, iconSize);
                    canvas.drawBitmap(icon, task.getX(), task.getY(), null);
                    float textX = task.getX() + iconSize/2;
                    float textY = task.getY() + iconSize + getResources().getInteger(R.integer.icon_text_margin);
                    canvas.drawText(task.getTaskName(), textX, textY, p);
                }

            }catch (Exception e){ }
        }
    };

    private void checkConnection( ){

        for(ActionElement action : scene.getActionList()) {
           if( action.isElementSelect() ){
               for(TaskElement task : scene.getTaskList()) {
                   if (task.isElementSelect()) {
                       if(!action.isTaskElementIdPresent(task.getTaskId()))
                            action.addNewTaskElementId(task.getTaskId());
                       else
                           action.deleteTaskElementId(task.getTaskId());
                       unselectAllElements();
                   }
               }
           }
        }
    }

    private void unselectAllElements( ){

        for(ActionElement action : scene.getActionList())
            action.unSelectElement();
        for(TaskElement task : scene.getTaskList())
            task.unSelectElement();
    }

    private TaskElement findTouchedTask(MotionEvent event){

        for(TaskElement task : scene.getTaskList()){
            if(task.isTouched(event, iconSize))
                return task;
        }
        return null;
    }

    private ActionElement findTouchedAction(MotionEvent event){

        for(ActionElement action : scene.getActionList()){
            if(action.isTouched(event, iconSize))
                return action;
        }
        return null;
    }

    private void showMenuDialog( ){

        dialogMenu = new Dialog(context);
        dialogMenu.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMenu.setContentView(R.layout.dialog_menu);

        ((ImageButton) dialogMenu.findViewById(R.id.actionElement)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Open Action List", Toast.LENGTH_SHORT).show();
                dialogMenu.dismiss();
            }
        });

        ((ImageButton) dialogMenu.findViewById(R.id.taskElement)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Open Task List", Toast.LENGTH_SHORT).show();
                dialogMenu.dismiss();
            }
        });

        dialogMenu.show();
    }
}
